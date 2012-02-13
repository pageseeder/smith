package com.weborganic.smith.rule;

import java.io.PrintWriter;
import java.util.Map;

import com.weborganic.smith.PasswordRule;
import com.weborganic.smith.ScoreFunction;
import com.weborganic.smith.Scriptable;
import com.weborganic.smith.function.ScoreArray;

/**
 * A rule based on the number of upper case characters in the password.
 *
 * @author Christophe Lauret
 * @version 9 February 2012
 */
public class UpperCaseCountRule implements PasswordRule, Scriptable {

  /**
   * The array of scores.
   */
  private ScoreFunction _function;

  @Override
  public int score(String password) {
    if (password == null) return 0;
    int count = 0;
    for (int i = 0; i< password.length(); i++) {
      if (password.charAt(i) >= 'a' && password.charAt(i) <= 'z') {
        count++;
      }
    }
    return this._function.get(count);
  }

  @Override
  public void configure(Map<String, String> config) {
    this._function = ScoreArray.parse(config);
  }

  @Override
  public void toScript(PrintWriter out) {
    if (!(this._function instanceof Scriptable)) return;
    out.println("function (p) {");
    out.print("  var f = ");
    ((Scriptable)this._function).toScript(out);
    out.println(";");
    out.println("  var c = p.length - p.replace(/[A-Z]/g, '').length;");
    out.println("  return f(c);");
    out.print("}");
  }
}
