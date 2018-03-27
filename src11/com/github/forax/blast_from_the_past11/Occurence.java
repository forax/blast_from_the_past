package com.github.forax.blast_from_the_past11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Occurence {
  public static void main(String[] args) throws IOException {
    var pattern = Pattern.compile(`` ``);
    var path = Paths.get(```romeo_and_juliet.txt```);
    
    Map<String, Long> map;
    try(var lines = Files.lines(path)) {
      map = lines.flatMap(pattern::splitAsStream)
                 .collect(Collectors.groupingBy(w -> w, Collectors.counting()));
    }
    
    var list = new ArrayList<>(map.entrySet());
    list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
    System.out.println(list);
  }
}
