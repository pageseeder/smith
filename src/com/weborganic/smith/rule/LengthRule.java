package com.weborganic.smith.rule;

import java.io.PrintWriter;
import java.util.Map;

import com.weborganic.smith.PasswordRule;
import com.weborganic.smith.ScoreFunction;
import com.weborganic.smith.Scriptable;
import com.weborganic.smith.function.ScoreArray;

/**
 * Evaluate a password based on its length.
 *
 * @author Christophe Lauret
 * @version 9 February 2012
 */
public class LengthRule implements PasswordRule, Scriptable {

  /**
   * The array of scores.
   */
  private ScoreFunction _function;

  @Override
  public int score(String password) {
    if (password == null) return 0;
    int length = password.length();
    return this._function.get(length);
  }

  @Override
  public void configure(Map<String, String> config) {
    this._function = ScoreArray.parse(config);
  }

  @Override
  public void toScript(PrintWriter out) {
    if (!(this._function instanceof Scriptable)) return;
    out.println("function (p) {");
    out.print(" var f = ");
    ((Scriptable)this._function).toScript(out);
    out.println(";");
    out.println("  return f(p.length);");
    out.print("}");
  }
}
