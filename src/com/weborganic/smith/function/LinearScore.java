package com.weborganic.smith.function;

import java.io.PrintWriter;

import com.weborganic.smith.ScoreFunction;
import com.weborganic.smith.Scriptable;

/**
 * Returns the score linearly based on the given value.
 *
 * @author Christophe Lauret
 * @version 9 February 2012
 */
public class LinearScore implements ScoreFunction, Scriptable {

  /**
   * A factor to apply to the score.
   */
  private final int _factor;

  /**
   * Creates a new linear score with a factor of 1.
   */
  public LinearScore() {
    this._factor = 1;
  }

  /**
   * Creates a new linear score with the specified factor;
   *
   * @param factor the factor to apply.
   */
  public LinearScore(int factor) {
    this._factor = factor;
  }

  /**
   * @return always "linear"
   */
  @Override
  public String type() {
    return "linear";
  }

  @Override
  public int get(int value) {
    return value * this._factor;
  }

  @Override
  public void toScript(PrintWriter out) {
    out.println("function (i) {");
    out.println("  var f = "+this._factor+";");
    out.println("  return i * f;");
    out.print("}");
  }
}
