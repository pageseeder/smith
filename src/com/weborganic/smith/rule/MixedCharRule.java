package com.weborganic.smith.rule;

import java.io.IOException;
import java.util.Map;

import com.weborganic.smith.PasswordRule;
import com.weborganic.smith.ScoreFunction;
import com.weborganic.smith.Scriptable;
import com.weborganic.smith.function.ScoreArray;

/**
 * Evaluate a password based the number of mixed character.
 *
 * @author Christophe Lauret
 * @version 14 February 2012
 */
public class MixedCharRule implements PasswordRule, Scriptable {

  /**
   * The array of scores.
   */
  private ScoreFunction _function;

  @Override
  public int score(String password) {
    if (password == null) return 0;
    int letter = 0;
    int digit = 0;
    int special = 0;
    for (int i = 0; i < password.length(); i++) {
      char c = password.charAt(i);
      if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) letter = 1;
      else if (c >= '0' && c <= '1') digit = 1;
      else special = 1;
    }
    int mixed = letter + digit + special;
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
    script.append(" var r = (/[a-zA-Z]/.test(p)? 1 : 0) + (/[0-9]/.test(p)? 1 : 0) + (/[^a-zA-Z0-9]/.test(p)? 1 : 0);");
    script.append(" return f(r);");
    script.append("}");
    return script;
  }

}
