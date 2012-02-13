package com.weborganic.smith.rule;

import java.io.IOException;
import java.util.Map;

import com.weborganic.smith.PasswordRule;
import com.weborganic.smith.ScoreFunction;
import com.weborganic.smith.Scriptable;
import com.weborganic.smith.function.ScoreArray;

/**
 * A rule based on the number of upper case characters in the password.
 *
 * @author Christophe Lauret
 * @version 14 February 2012
 */
public class LowerCaseCountRule implements PasswordRule, Scriptable {

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
  public Appendable toScript(Appendable script) throws IOException{
    script.append("function (p) {");
    script.append(" var f = ");
    this._function.toScript(script).append(";");
    script.append(" var s = p.length - p.replace(/[a-z]/g, '').length;");
    script.append(" return f(s);");
    script.append("}");
    return script;
  }
}
