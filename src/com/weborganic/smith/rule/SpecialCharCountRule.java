package com.weborganic.smith.rule;

import java.io.IOException;
import java.util.Map;

import com.weborganic.smith.PasswordRule;
import com.weborganic.smith.ScoreFunction;
import com.weborganic.smith.Scriptable;
import com.weborganic.smith.function.ScoreArray;

/**
 *
 * @author Christophe Lauret
 * @version 9 February 2012
 */
public final class SpecialCharCountRule implements PasswordRule, Scriptable {

  /**
   * The default special characters
   */
  public static final String DEFAULT_CHARS = "!@#$%^&*?_~";

  /**
   * The array of scores.
   */
  private ScoreFunction _function;

  /**
   * The special characters.
   */
  private String _chars;

  /**
   * Creates a new rule using the special characters
   *
   * @param chars
   */
  public SpecialCharCountRule() {
    this._chars = DEFAULT_CHARS;
  }

  /**
   *
   * @param chars
   */
  public SpecialCharCountRule(String chars) {
    this._chars = chars;
  }

  @Override
  public int score(String password) {
    if (password == null) return 0;
    int count = 0;
    for (int i = 0; i< password.length(); i++) {
      char c = password.charAt(i);
      if (this._chars.indexOf(c) >= 0) count++;
    }
    return this._function.get(count);
  }

  @Override
  public void configure(Map<String, String> config) {
    String chars = config.get("characters");
    if (chars != null) {
      this._chars = chars;
    }
    this._function = ScoreArray.parse(config);
  }

  @Override
  public Appendable toScript(Appendable script) throws IOException {
    script.append("function (p) {");
    script.append(" var f = ");
    this._function.toScript(script).append(";");
    StringBuilder regexp = new StringBuilder();
    for (int i=0; i < this._chars.length(); i++) {
      regexp.append('\\').append(this._chars.charAt(i));
    }
    script.append(" var s = p.length - p.replace(/"+regexp+"/g, '').length;");
    script.append(" return f(s);");
    script.append("}");
    return script;
  }
}
