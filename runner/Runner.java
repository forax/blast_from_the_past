import static java.nio.file.Files.walk;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static java.util.stream.Stream.concat;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.UncheckedIOException;
import java.lang.ProcessBuilder.Redirect;
import java.lang.Runtime.Version;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Runner {
  private static final Pattern JAVA_CMD_EXTRACT_VERSION_PATTERN = Pattern.compile(".*\"([^\"]+)\".*");
  private static final Pattern OLD_JDK_VERSION = Pattern.compile("(.*)\\.(.*)\\..*");
  private static final Pattern PATH_VERSION_PATTERN = Pattern.compile(".*/blast_from_the_past(.*)/.*");
  
  interface ProcessExecutor {
    Process execute(String... commands) throws IOException;
    
    static ProcessExecutor local() {
      return Runner::execute;
    }
    
    static ProcessExecutor docker(String containerId) {
      return commands -> {
        return Runner.execute(concat(
            Stream.of("docker", "exec", "-t", containerId),
            Arrays.stream(commands)
          ).toArray(String[]::new));
      };
    }
  }
  
  static Process execute(String... commands) throws IOException {
    ProcessBuilder builder = new ProcessBuilder(commands);
    return
      builder
        .redirectInput(Redirect.INHERIT)
        .redirectOutput(Redirect.DISCARD)
        .redirectError(Redirect.INHERIT)
        .start();
  }
  
  private static List<Long> test(ProcessExecutor executor, Path javaCmd, Path repository, String className, int repetition) {
    return range(0, repetition).mapToObj(i -> tryTest(executor, javaCmd, repository, className)).collect(toList());
  }

  private static long tryTest(ProcessExecutor executor, Path javaCmd, Path repository, String className) {
    try {
      return test(executor, javaCmd, repository, className);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
  
  private static long test(ProcessExecutor executor, Path javaCmd, Path repository, String className) throws IOException {
    Process process = executor.execute(javaCmd.toString(), "-classpath", repository.toString(), className);
    long start = System.currentTimeMillis();
    awaitTermination(process);
    long end = System.currentTimeMillis();
    return end - start;
  }
  
  private static Version version(ProcessExecutor executor, Path javaCmd) throws IOException{
    Process process = executor.execute(javaCmd.toString(), "-version");
    
    List<String> result;
    try(InputStream input = process.getErrorStream();
        InputStreamReader reader = new InputStreamReader(input);
        BufferedReader bufferedReader = new BufferedReader(reader)) {
      result = bufferedReader.lines().collect(toList());
    }
    if (result.size() < 1) {
      throw new IOException("invalid version format");
    }
    
    awaitTermination(process);
    
    // extract version
    Matcher matcher = JAVA_CMD_EXTRACT_VERSION_PATTERN.matcher(result.get(0));
    if (!matcher.matches()) {
      throw new IOException("invalid version format\n" + result);
    }
    String textVersion = matcher.group(1);
    
    int major, minor;
    Matcher oldMatcher = OLD_JDK_VERSION.matcher(textVersion);
    if (oldMatcher.matches()) {
      major = Integer.parseInt(oldMatcher.group(1));
      minor = Integer.parseInt(oldMatcher.group(2));
    } else {
      Version sysVersion = Version.parse(textVersion);
      major = sysVersion.major();
      minor = sysVersion.minor();
    }
    String version = minor == 0 ? "" + major: major + "." + minor;
    return Version.parse(version);
  }

  private static void awaitTermination(Process process) throws IOException {
    int exitStatus;
    try {
      exitStatus = process.waitFor();
    } catch (InterruptedException e) {
      throw (InterruptedIOException)new InterruptedIOException().initCause(e);
    }
    if (exitStatus != 0) {
      throw new IOException("exit status " + exitStatus);
    }
  }
  
  private static Path javaHome() {
    String javaHome = Optional
        .ofNullable(System.getenv("JAVA_HOME"))
        .orElseGet(() -> System.getProperty("java.home"));
    return Paths.get(javaHome);
  }
  
  private static ProcessExecutor processExecutor(String containerId) {
    return Optional
        .ofNullable(containerId)
        .map(ProcessExecutor::docker)
        .orElseGet(ProcessExecutor::local);
  }
  
  private static Path repository(String[] args) {
    String classfiles = Optional.of(args)
      .filter(array -> array.length == 1)
      .map(array -> array[0])
      .orElse("classfiles");
    return Paths.get(classfiles);
  }
  
  private static Version findVersionOfFile(Path path) {
    String pathname = path.toString();
    Matcher matcher = PATH_VERSION_PATTERN.matcher(pathname);
    if (!matcher.matches()) {
      throw new UncheckedIOException(new IOException("invalid name " + pathname));
    }
    String version = matcher.group(1).replace('_', '.');
    return Runtime.Version.parse(version);
  }
  
  private static String asClassName(Path path) {
    String pathname = path.toString();
    return pathname.substring(0, pathname.length() - ".class".length()).replace(File.separatorChar, '.');
  }
  
  private static NavigableMap<Version, List<String>> findAllClassFiles(Path repository) throws IOException {
    try(Stream<Path> stream = walk(repository)) {
      return stream
        .filter(path -> path.toString().endsWith(".class") && !path.toString().contains("$"))
        .map(repository::relativize)
        .collect(groupingBy(path -> findVersionOfFile(path), TreeMap::new,
            Collectors.mapping(path -> asClassName(path), toList())));
    }
  }
  
  private static String stat(Consumer<Consumer<Long>> consumer) {
    class Stat {
      long min = Integer.MAX_VALUE,
           max = Integer.MIN_VALUE,
           count, sum, sum2;
      
      void accept(Long value) {
        min = Math.min(min, value);
        max = Math.max(max, value);
        count++;
        sum += value;
        sum2 += value * value;
      }
    }
    Stat stat = new Stat();
    consumer.accept(stat::accept);
    long count = stat.count;
    double average = ((double)stat.sum) / count;
    double sigma = Math.sqrt(stat.sum2 / count - average * average);
    return String.format("count %d min %d max %d average %f sigma %f",
        count,
        stat.min, stat.max,
        average,
        sigma);
  }
  
  
  
  public static void main(String[] args) throws IOException {
    int repetition = 24;
    
    String containerId = System.getenv("DOCKER_CONTAINER_ID");
    Optional.ofNullable(containerId)
            .ifPresent(id -> System.out.println("container id " + id));
    ProcessExecutor executor = processExecutor(containerId); 
    
    Path javaHome = javaHome();
    System.out.println("java home: " + javaHome);
    Path javaCmd = javaHome.resolve("bin/java");
    
    Version version = version(executor, javaCmd);
    System.out.println("test jdk version: " + version);
    
    Path repository = repository(args);
    System.out.println("test repository: " + repository);
    
    NavigableMap<Version, List<String>> allClassMap = findAllClassFiles(repository);
    NavigableMap<Version, List<String>> testClassMap = allClassMap.headMap(version, /*inclusive*/ true);
    System.out.println("tests " + testClassMap);
    
    testClassMap.forEach((testVersion, testClasses) -> {
      System.out.println("test version " + testVersion);
      testClasses.forEach(testClass -> {
        System.out.println("test class " + testClass);
        List<Long> times = test(executor, javaCmd, repository, testClass, repetition);
        
        times.sort(null);
        List<Long> values= times.subList(2, times.size() - 2);  // remove the 2 worst and the 2 best
        
        System.out.println(stat(values::forEach));
      });
    }); 
  }
}
