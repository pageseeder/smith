package com.weborganic.smith.rule;

import java.util.Map;

import com.weborganic.smith.PasswordRule;
import com.weborganic.smith.ScoreFunction;
import com.weborganic.smith.function.ScoreArray;

/**
 * A rule based on the number of digits [0-9] in the password.
 *
 * @author Christophe Lauret
 * @version 9 February 2012
 */
public class DigitCountRule implements PasswordRule {

  /**
   * The array of scores.
   */
  private ScoreFunction _function;

  @Override
  public int score(String password) {
    if (password == null) return 0;
    int count = 0;
    for (int i = 0; i< password.length(); i++) {
      if (password.charAt(i) >= '0' && password.charAt(i) <= '9') {
        count++;
      }
    }
    return this._function.get(count);
  }

  @Override
  public void configure(Map<String, String> config) {
    this._function = ScoreArray.parse(config);
  }

}
