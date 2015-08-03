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
import org.pageseeder.smith.function.ScoreArray;

/**
 * A rule based on the number of unique characters in the password.
 *
 * @author Christophe Lauret
 * @version 14 February 2012
 */
public class UniqueCharRule implements PasswordRule, Scriptable {

  /**
   * The array of scores.
   */
  private ScoreFunction _function;

  @Override
  public int score(String password) {
    if (password == null) return 0;
    StringBuilder unique = new StringBuilder();
    for (int i = 0; i< password.length(); i++) {
      char c = password.charAt(i);
      if (unique.indexOf(String.valueOf(c)) < 0) {
        unique.append(c);
      }
    }
    return this._function.get(unique.length());
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
    script.append(" var u = '';");
    script.append(" for (var i = 0; i < p.length; i++) {");
    script.append("  if (u.indexOf(p.charAt(i)) === -1) { u += p.charAt(i); }");
    script.append(" }");
    script.append(" return f(u.length);");
    script.append("}");
    return script;
  }
}