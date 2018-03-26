package com.github.forax.blast_from_the_past1_7;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Occurence {
  public static void main(String[] args) throws IOException {
    Path path = Paths.get("romeo_and_juliet.txt");
    HashMap<String, Long> map = new HashMap<>();
    try(BufferedReader bufferedReader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
      String line;
      while((line = bufferedReader.readLine()) != null) {
        for(String word: line.split(" ")) {
          Long count = map.get(word);
          map.put(word, count == null? 1: count + 1);
        }
      }
    }
    
    ArrayList<Map.Entry<String,Long>> list = new ArrayList<>(map.entrySet());
    Collections.sort(list, new Comparator<Map.Entry<String, Long>>() {
      @Override
      public int compare(Entry<String, Long> e1, Entry<String, Long> e2) {
        return Long.compare(e1.getValue(), e2.getValue());
      }
    });
    System.out.println(list);
  }
}
