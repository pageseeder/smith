package com.weborganic.smith.rule;

import java.io.IOException;
import java.util.Map;

import com.weborganic.smith.PasswordRule;
import com.weborganic.smith.ScoreFunction;
import com.weborganic.smith.Scriptable;
import com.weborganic.smith.function.ScoreArray;

/**
 * Evaluate a password based the number of mixed case pairs.
 *
 * @author Christophe Lauret
 * @version 14 February 2012
 */
public class MixedCaseRule implements PasswordRule, Scriptable {

  /**
   * The array of scores.
   */
  private ScoreFunction _function;

  @Override
  public int score(String password) {
    if (password == null) return 0;
    // count lower and upper case letters
    int lower = 0;
    int upper = 0;
    for (int i = 0; i < password.length(); i++) {
      char c = password.charAt(i);
      if (c >= 'a' && c <= 'z') lower++;
      else if (c >= 'A' && c <= 'Z') upper++;
    }
    int mixed = Math.min(lower, upper);
    return this._function.get(mixed);
  }

  @Override
  public void configure(Map<String, String> config) {
    this._function = ScoreArray.parse(config);
  }

  @Override
  public Appendable toScript(Appendable script) throws IOException {
    script.append("function (p) {");
    script.append(" var f = ");
    this._function.toScript(script).append(";");
    script.append(" var l = p.length - p.replace(/[a-z]/g, '').length;");
    script.append(" var u = p.length - p.replace(/[A-Z]/g, '').length;");
    script.append(" return f(Math.min(l,u));");
    script.append("}");
    return script;
  }

}
