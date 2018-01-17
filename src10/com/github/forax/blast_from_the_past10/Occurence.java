package com.github.forax.blast_from_the_past10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Occurence {
  public static void main(String[] args) throws IOException {
    var pattern = Pattern.compile(" ");
    var path = Paths.get("romeo_and_juliet.txt");
    
    Map<String, Long> map;
    try(var lines = Files.lines(path)) {
      map = lines/*.parallel()*/
                 .flatMap(pattern::splitAsStream)
                 .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }
    /*
    var map = new HashMap<String, Long>();
    try(var lines = Files.lines(path)) {
      lines.flatMap(pattern::splitAsStream)
           .forEach(word -> map.merge(word, 1L, Long::sum));
    }*/
    /*
    var map2 = new HashMap<String, long[]>();
    var map = (HashMap<String, Long>)(HashMap<String,?>)map2;
    try(var lines = Files.lines(path)) {
      lines.flatMap(pattern::splitAsStream)
           .forEach(word -> map2.computeIfAbsent(word, __ -> new long[1])[0]++);
    }
    ((HashMap<String, Object>)(HashMap<String,?>)map2).replaceAll((__, value) -> Long.valueOf(((long[])value)[0]));
    */
    var list = new ArrayList<>(map.entrySet());
    list.sort(Map.Entry.<String,Long>comparingByValue().reversed());
    System.out.println(list);
  }
}
