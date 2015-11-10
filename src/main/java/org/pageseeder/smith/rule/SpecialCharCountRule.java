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
 * A rule based on the number of upper case characters in the password.
 *
 * <p>The scoring is as follows:
 * <ul>
 *   <li>0 for <code>null</code> or empty string <code>""</code></li>
 *   <li>1 point for every special character found in the password</li>
 * </ul>
 *
 * <p>The maximum value is the length of the password if it is entirely
 * made up of special characters.
 *
 * @author Christophe Lauret
 */
public final class SpecialCharCountRule implements PasswordRule, Scriptable {

  /**
   * The default special characters
   */
  public static final String DEFAULT_CHARS = "!@#$%^&*?_~";

  /**
   * The array of scores.
   */
  private ScoreFunction _function = LinearScore.IDENTITY;

  /**
   * The special characters.
   */
  private String _chars;

  /**
   * Creates a new rule using the special characters
   */
  public SpecialCharCountRule() {
    this._chars = DEFAULT_CHARS;
  }

  /**
   *
   * @param chars The characters to look for
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
      if (this._chars.indexOf(c) >= 0) {
        count++;
      }
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
    script.append(" var s = p? p.length - p.replace(/["+regexp+"]/g, '').length : 0;");
    script.append(" return f(s);");
    script.append("}");
    return script;
  }
}
