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

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.pageseeder.smith.ScoreFunction;
import org.pageseeder.smith.Scriptable;

/**
 * A utility class for scores
 *
 * @author Christophe Lauret
 */
public final class ScoreArray extends ScoreFunctionBase implements ScoreFunction, Scriptable {

  /**
   * The maximum size of the array.
   */
  public static final int MAX_ARRAY_SIZE = 1024;

  /**
   * Index of scores.
   */
  private final int[] _scores;

  /**
   * Creates a new index of scores where all scores return 0;
   */
  public ScoreArray() {
    super("array");
    this._scores = new int[0];
  }

  /**
   * Creates a new index of scores.
   */
  public ScoreArray(int[] scores) {
    super("array");
    this._scores = scores;
  }

  /**
   * Returns the score for the specified index.
   *
   * @param i The index
   * @return the score at that index.
   */
  @Override
  public int get(int i) {
    if (i < 0 || this._scores.length == 0) return 0;
    if (i < this._scores.length) return this._scores[i];
    return this._scores[this._scores.length - 1];
  }

  /**
   * @return a copy of the internal array.
   */
  protected int[] scores(){
    return Arrays.copyOf(this._scores, this._scores.length);
  }

  @Override
  public String toString() {
    StringBuilder out = new StringBuilder();
    out.append('[');
    for (int i = 0; i < this._scores.length; i++) {
      if (i > 0) {
        out.append(',');
      }
      out.append(this._scores[i]);
    }
    out.append(']');
    return out.toString();
  }

  /**
   *
   * @param config the configuration
   * @return the corresponding array
   */
  public static ScoreArray parse(Map<String, String> config) {
    // Grab the sorted the values
    SortedMap<Integer, Integer> sorted = new TreeMap<Integer, Integer>();
    for (Entry<String, String> e : config.entrySet()) {
      Integer i = asInteger(e.getKey());
      if (i != null && i.intValue() > 0 && i < MAX_ARRAY_SIZE) {
        Integer v = asInteger(e.getValue());
        if (v != null) {
          sorted.put(i, v);
        }
      }
    }
    // Generate the array of scores
    if (sorted.size() == 0) return new ScoreArray(new int[0]);
    final int size = sorted.lastKey().intValue() + 1;
    final int[] scores = new int[size];
    int score = 0;
    for (int i = 0; i < size; i++) {
      Integer x = sorted.get(Integer.valueOf(i));
      if (x != null) {
        score = x.intValue();
      }
      scores[i] = score;
    }
    return new ScoreArray(scores);
  }

  /**
   * Linear Range
   */
  protected static ScoreArray linearRange(int from, int to) {
   int[] scores = new int[to+1];
   for (int i = from, j = 0; i < to+1; i++, j++) {
     scores[i] = j+1;
   }
   return new ScoreArray(scores);
  }

  private static Integer asInteger(String s) {
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c < '0' || c > '9') {
        if (i > 0 || c != '-') return null;
      }
    }
    return Integer.valueOf(s);
  }

  @Override
  public Appendable toScript(Appendable script) throws IOException {
    script.append("function (i) {");
    // Store array
    script.append(" var x = [");
    for (int i = 0; i < this._scores.length; i++) {
      if (i > 0) {
        script.append(',');
      }
      script.append(Integer.toString(this._scores[i]));
    }
    script.append("];");
    // Do function
    script.append(" if (i < 0 || x.length === 0) return 0;");
    script.append(" if (i < x.length) return x[i];");
    script.append(" return x[x.length -1];");
    script.append("}");
    return script;
  }

}
