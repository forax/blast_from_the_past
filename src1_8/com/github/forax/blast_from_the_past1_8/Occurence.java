package com.github.forax.blast_from_the_past1_8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Occurence {
  public static void main(String[] args) throws IOException {
    Pattern pattern = Pattern.compile(" ");
    Path path = Paths.get("romeo_and_juliet.txt");
    Map<String, Long> map;
    try(Stream<String> lines = Files.lines(path)) {
      map = lines.flatMap(pattern::splitAsStream)
                 .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }
    ArrayList<Map.Entry<String,Long>> list = new ArrayList<>(map.entrySet());
    list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
    System.out.println(list);
  }
}
