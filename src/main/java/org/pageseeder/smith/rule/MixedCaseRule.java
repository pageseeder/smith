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
package org.pageseeder.smith.rule;

import java.io.IOException;
import java.util.Map;

import org.pageseeder.smith.PasswordRule;
import org.pageseeder.smith.ScoreFunction;
import org.pageseeder.smith.Scriptable;
import org.pageseeder.smith.function.LinearScore;
import org.pageseeder.smith.function.ScoreArray;

/**
 * Evaluate a password based the number of mixed case pairs.
 *
 * <p>This rule counts the number of lower case letters and the
 * number of upper case letters and returns the minimum value.
 *
 * <p>The scoring is as follows:
 * <ul>
 *   <li>0 for <code>null</code> or empty string <code>""</code></li>
 *   <li>1 point per pair of lower/upper case letters found</li>
 * </ul>
 *
 * <p>The maximum value is half the password length if the password
 * is made up of half upper letters and half lower case letters
 *
 * @author Christophe Lauret
 */
public final class MixedCaseRule implements PasswordRule, Scriptable {

  /**
   * The array of scores.
   */
  private ScoreFunction _function = LinearScore.IDENTITY;

  @Override
  public int score(String password) {
    if (password == null) return 0;
    // count lower and upper case letters
    int lower = 0;
    int upper = 0;
    for (int i = 0; i < password.length(); i++) {
      char c = password.charAt(i);
      if (c >= 'a' && c <= 'z') {
        lower++;
      } else if (c >= 'A' && c <= 'Z') {
        upper++;
      }
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
    script.append(" var l = p? p.length - p.replace(/[a-z]/g, '').length : 0;");
    script.append(" var u = p? p.length - p.replace(/[A-Z]/g, '').length : 0;");
    script.append(" return f(Math.min(l,u));");
    script.append("}");
    return script;
  }

}
