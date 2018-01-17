package com.github.forax.blast_from_the_past1_5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Occurence {
  public static void main(String[] args) throws IOException {
    File file = new File("romeo_and_juliet.txt");
    HashMap<String, Long> map = new HashMap<String, Long>();
    FileReader reader = new FileReader(file);
    try {
      BufferedReader bufferedReader = new BufferedReader(reader);
      try {
        String line;
        while((line = bufferedReader.readLine()) != null) {
          for(String word: line.split(" ")) {
            Long count = map.get(word);
            map.put(word, count == null? 1: count + 1);
          }
        }
      } finally {
        bufferedReader.close();
      }
    } finally {
      reader.close();
    }
    
    ArrayList<Map.Entry<String,Long>> list = new ArrayList<Map.Entry<String,Long>>(map.entrySet());
    Collections.sort(list, new Comparator<Map.Entry<String, Long>>() {
      public int compare(Entry<String, Long> e1, Entry<String, Long> e2) {
        long l1 = e1.getValue();
        long l2 = e2.getValue();
        if (l1 == l2) {
          return 0;
        }
        if (l1 < l2) {
          return 1;
        }
        return -1;
      }
    });
    System.out.println(list);
  }
}
