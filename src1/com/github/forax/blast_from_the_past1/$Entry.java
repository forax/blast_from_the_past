package com.github.forax.blast_from_the_past1;

class $Entry {
  private final String key;
  private final long value;
  
  $Entry(String key, long value) {
    this.key = key;
    this.value = value;
  }

  int compareTo($Entry e) {
    long l1 = this.value;
    long l2 = e.value;
    if (l1 == l2) {
      return 0;
    }
    if (l1 < l2) {
      return 1;
    }
    return -1;
  }
  
  public String toString() {
    return key + '=' + value; 
  }
}