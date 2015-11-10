/*
 * Copyright 2010-2015 Allette Systems (Australia)
 * http://www.allette.com.au
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.pageseeder.smith.function;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;


/**
 * A utility class for scores
 *
 * @author Christophe Lauret
 */
public final class ScoreArrayTest {

  @Test
  public void testDefaultContructor() {
    ScoreArray f = new ScoreArray();
    int[] values = new int[]{Integer.MIN_VALUE, -17, -1, 0, 1, 31, Integer.MAX_VALUE};
    for (int v : values) {
      Assert.assertEquals(0, f.get(v));
    }
  }

  @Test
  public void testGet() {
    int[] array = new int[]{1, 6, 13, 2, 1};
    ScoreArray f = new ScoreArray(array);
    // Negative or zero must return zero.
    Assert.assertEquals(0, f.get(Integer.MIN_VALUE));
    Assert.assertEquals(0, f.get(-31));
    // Positive should look up in array
    Assert.assertEquals(array[0], f.get(0));
    Assert.assertEquals(array[1], f.get(1));
    Assert.assertEquals(array[2], f.get(2));
    // out of bound returns last value of array
    Assert.assertEquals(array[array.length-1], f.get(array.length-1));
    Assert.assertEquals(array[array.length-1], f.get(array.length));
    Assert.assertEquals(array[array.length-1], f.get(Integer.MAX_VALUE));
  }

  @Test
  public void testParseEmpty() {
    // Empty
    Map<String, String> empty = Collections.emptyMap();
    ScoreArray f = ScoreArray.parse(empty);
    int[] values = new int[]{Integer.MIN_VALUE, -17, -1, 0, 1, 31, Integer.MAX_VALUE};
    for (int v : values) {
      Assert.assertEquals(0, f.get(v));
    }
  }

  @Test
  public void testParseInvalid() {
    // Invalid = same as empty
    Map<String, String> invalid = new HashMap<>();
    invalid.put("x", "y");
    invalid.put("10", "a");
    invalid.put("b", "3");
    ScoreArray f = ScoreArray.parse(invalid);
    int[] values = new int[]{Integer.MIN_VALUE, -17, -1, 0, 1, 31, Integer.MAX_VALUE};
    for (int v : values) {
      Assert.assertEquals(0, f.get(v));
    }
  }

  @Test
  public void testLinear() {
    final int upto = 10;
    ScoreArray f = ScoreArray.linearRange(1, upto);
    // [MIN_INTEGER, 0] return 0
    Assert.assertEquals(0, f.get(Integer.MIN_VALUE));
    Assert.assertEquals(0, f.get(-31));
    Assert.assertEquals(0, f.get(-15));
    Assert.assertEquals(0, f.get(0));
    // Linear part return same as index
    for (int i=0; i < upto; i++) {
      Assert.assertEquals(i+1, f.get(i+1));
    }
    // After linear part return max
    Assert.assertEquals(upto, f.get(upto));
    Assert.assertEquals(upto, f.get(upto+1));
    Assert.assertEquals(upto, f.get(Integer.MAX_VALUE));
  }


  @Test
  public void testParseLinear() {
    final int upto = 10;
    Map<String, String> spec = new HashMap<>();
    for (int i = 0; i < upto; i++) {
      String s = Integer.toString(i+1);
      spec.put(s, s);
    }
    ScoreArray f = ScoreArray.parse(spec);
    // [MIN_INTEGER, 0] return 0
    Assert.assertEquals(0, f.get(Integer.MIN_VALUE));
    Assert.assertEquals(0, f.get(-31));
    Assert.assertEquals(0, f.get(-15));
    Assert.assertEquals(0, f.get(0));
    // Linear part return same as index
    for (int i=0; i < spec.size(); i++) {
      Assert.assertEquals(i+1, f.get(i+1));
    }
    // After linear part return max
    Assert.assertEquals(upto, f.get(spec.size()));
    Assert.assertEquals(upto, f.get(spec.size()+1));
    Assert.assertEquals(upto, f.get(Integer.MAX_VALUE));
  }

  @Test
  public void testParseCustom() {
    Map<String, String> spec = new HashMap<>();
    spec.put("4",   "50");  // From 4 return 50
    spec.put("2",   "10");  // From 2 return 10
    spec.put("10",  "100"); // From 10 return 100
    spec.put("-14", "17");  // Ignored
    ScoreArray f = ScoreArray.parse(spec);
    // [MIN_INTEGER, -15] return 0
    Assert.assertEquals(0, f.get(Integer.MIN_VALUE));
    Assert.assertEquals(0, f.get(-31));
    Assert.assertEquals(0, f.get(-15));
    // [-14, 1] return 0
    Assert.assertEquals(0, f.get(-14));
    Assert.assertEquals(0, f.get(-0));
    Assert.assertEquals(0, f.get(1));
    // [2,3] return 10
    Assert.assertEquals(10, f.get(2));
    Assert.assertEquals(10, f.get(3));
    // [4,9] return 50
    Assert.assertEquals(50, f.get(4));
    Assert.assertEquals(50, f.get(7));
    Assert.assertEquals(50, f.get(9));
    // [4,9] return 50
    Assert.assertEquals(100, f.get(10));
    Assert.assertEquals(100, f.get(31));
    Assert.assertEquals(100, f.get(Integer.MAX_VALUE));
  }
}
