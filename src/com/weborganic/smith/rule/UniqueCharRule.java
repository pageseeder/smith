package com.weborganic.smith.rule;

import java.util.Map;

import com.weborganic.smith.PasswordRule;
import com.weborganic.smith.ScoreFunction;
import com.weborganic.smith.function.ScoreArray;

/**
 * A rule based on the number of unique characters in the password.
 *
 * @author Christophe Lauret
 * @version 9 February 2012
 */
public class UniqueCharRule implements PasswordRule {

  /**
   * The array of scores.
   */
  private ScoreFunction _function;

  @Override
  public int score(String password) {
    if (password == null) return 0;
    int count = 0;
    StringBuilder unique = new StringBuilder();
    for (int i = 0; i< password.length(); i++) {
      char c = password.charAt(i);
      if (unique.indexOf(String.valueOf(c)) < 0) {
        unique.append(c);
      }
    }
    return this._function.get(count);
  }

  @Override
  public void configure(Map<String, String> config) {
    this._function = ScoreArray.parse(config);
  }

}
