package com.weborganic.smith.rule;

import java.util.Map;

import com.weborganic.smith.PasswordRule;
import com.weborganic.smith.ScoreFunction;
import com.weborganic.smith.function.ScoreArray;

/**
 *
 * @author Christophe Lauret
 * @version 9 February 2012
 */
public final class SpecialCharCountRule implements PasswordRule {

  /**
   *
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

}
