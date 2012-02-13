package com.weborganic.smith.function;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import com.weborganic.smith.ScoreFunction;
import com.weborganic.smith.Scriptable;

/**
 * A utility class for scores
 *
 * @author Christophe Lauret
 * @version 9 February 2012
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

  @Override
  public String toString() {
    StringBuilder out = new StringBuilder();
    out.append('[');
    for (int i = 0; i < this._scores.length; i++) {
      if (i > 0) out.append(',');
      out.append(this._scores[i]);
    }
    out.append(']');
    return out.toString();
  }

  /**
   *
   * @param config
   * @return
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
   *
   * @param s
   * @return
   */
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
  public void toScript(PrintWriter out){
    out.print("function (i) {");
    // Store array
    out.print("  var x = [");
    for (int i = 0; i < this._scores.length; i++) {
      if (i > 0) out.print(',');
      out.print(this._scores[i]);
    }
    out.print("];");
    // Do function
    out.print(" if (i < 0 || x.length === 0) return 0;");
    out.print(" if (i < x.length) return x[i];");
    out.print(" return x[x.length -1];");
    out.print("}");
  }

  public static void main(String[] args) {
    Map<String, String> test = new HashMap<String, String>();
    test.put("xx", "yy");
    /*
    test.put("4", "50");
    test.put("2", "10");
    test.put("10", "100");
    test.put("-14", "17"); */
    ScoreArray x = parse(test);
    System.out.println(x.toString());
    System.out.println(x.get(-1));
    System.out.println(x.get(0));
    System.out.println(x.get(1));
    System.out.println(x.get(2));
    System.out.println(x.get(5));
    System.out.println(x.get(10));
    System.out.println(x.get(220));
  }

}
