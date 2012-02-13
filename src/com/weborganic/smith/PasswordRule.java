package com.weborganic.smith;

import java.util.Map;

/**
 * Defines a rule for a password.
 *
 * <p>A rule must provide a score for a given password.
 *
 * <p>It is possible for a rule to return a negative score.
 *
 * @author Christophe Lauret
 * @version 9 February 2012
 */
public interface PasswordRule extends Scriptable {

  /**
   * Configures the specified rule.
   *
   * <p>The properties are specific to each rule.
   *
   * @param config The configuration.
   */
  void configure(Map<String, String> config);

  /**
   * Returns the score for the password.
   *
   * @param password The password to evaluate.
   *
   * @return the corresponding score.
   */
  int score(String password);

}
