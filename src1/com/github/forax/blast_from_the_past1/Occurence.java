package com.github.forax.blast_from_the_past1;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

public class Occurence {
  public static void main(String[] args) throws IOException {
    File file = new File("romeo_and_juliet.txt");
    Hashtable hashtable = new Hashtable();
    FileInputStream fileInputStream = new FileInputStream(file);
    try {
      DataInputStream dataInputStream = new DataInputStream(fileInputStream);
      try {
        String line;
        while((line = dataInputStream.readLine()) != null) {
          StringTokenizer tokenizer = new StringTokenizer(line, " ");
          while(tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken();
            Long count = (Long)hashtable.get(word);
            hashtable.put(word, count == null? Long.valueOf(1): new Long(count.longValue() + 1));
          }
        }
      } finally {
        dataInputStream.close();
      }
    } finally {
      fileInputStream.close();
    }

    Vector vector = new Vector();
    for(Enumeration keys = hashtable.keys(); keys.hasMoreElements();) {
      String key = (String)keys.nextElement();
      long value = ((Long)hashtable.get(key)).longValue();
      vector.add(new $Entry(key, value));
    }
    quicksort(vector, 0, vector.size() - 1);
    System.out.println(vector);
  }

  private static void quicksort(Vector vector, int low, int high) {
    if (low == high) {
      return;
    }
    
    int i = low, j = high;
    $Entry pivot = ($Entry)vector.get(low + (high-low)/2);

    while (i <= j) {
      while ((($Entry)vector.get(i)).compareTo(pivot) < 0) {
        i++;
      }
      while ((($Entry)vector.get(j)).compareTo(pivot) > 0) {
        j--;
      }
      if (i <= j) {
        swap(vector, i, j);
        i++;
        j--;
      }
    }
    if (low < j) {
      quicksort(vector, low, j);
    }
    if (i < high) {
      quicksort(vector, i, high);
    }
  }

  private static void swap(Vector vector, int i, int j) {
    Object tmp = vector.get(i);
    vector.set(i, vector.get(j));
    vector.set(j, tmp);
  }
}
