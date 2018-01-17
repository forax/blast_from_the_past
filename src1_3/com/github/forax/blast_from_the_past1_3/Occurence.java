package com.github.forax.blast_from_the_past1_3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

// duplicate of version 1.2
public class Occurence {
  private static final Long ONE = new Long(1);
  
  public static void main(String[] args) throws IOException {
    File file = new File("romeo_and_juliet.txt");
    HashMap map = new HashMap();
    FileReader reader = new FileReader(file);
    try {
      BufferedReader bufferedReader = new BufferedReader(reader);
      try {
        String line;
        while((line = bufferedReader.readLine()) != null) {
          StringTokenizer tokenizer = new StringTokenizer(line, " ");
          while(tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken();
            Long count = (Long)map.get(word);
            map.put(word, count == null? ONE: new Long(count.longValue() + 1));
          }
        }
      } finally {
        bufferedReader.close();
      }
    } finally {
      reader.close();
    }

    ArrayList list = new ArrayList(map.entrySet());
    Collections.sort(list, new Comparator() {
      public int compare(Object o1, Object o2) {
        long l1 = ((Long)((Map.Entry)o1).getValue()).longValue();
        long l2 = ((Long)((Map.Entry)o2).getValue()).longValue();
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
