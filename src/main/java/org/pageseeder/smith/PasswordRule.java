/*
 * Copyright 2010-2015 Allette Systems (Australia)
 * http://www.allette.com.au
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.pageseeder.smith;

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
