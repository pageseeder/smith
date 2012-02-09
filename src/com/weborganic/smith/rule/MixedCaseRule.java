package com.weborganic.smith.rule;

import java.util.Map;

import com.weborganic.smith.PasswordRule;
import com.weborganic.smith.ScoreFunction;
import com.weborganic.smith.function.ScoreArray;

/**
 * Evaluate a password based the number of mixed case pairs.
 *
 * @author Christophe Lauret
 * @version 9 February 2012
 */
public class MixedCaseRule implements PasswordRule {

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

}
