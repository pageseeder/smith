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
package org.pageseeder.smith.function;

import java.io.IOException;

import org.pageseeder.smith.ScoreFunction;
import org.pageseeder.smith.Scriptable;

/**
 * Returns the score linearly based on the given value.
 *
 * @author Christophe Lauret
 */
public class LinearScore implements ScoreFunction, Scriptable {

  /**
   * Linear scoring implementing an identity function.
   */
  public static final LinearScore IDENTITY = new LinearScore();

  /**
   * A factor to apply to the score.
   */
  private final int _factor;

  /**
   * Creates a new linear score with a factor of 1.
   */
  public LinearScore() {
    this._factor = 1;
  }

  /**
   * Creates a new linear score with the specified factor;
   *
   * @param factor the factor to apply.
   */
  public LinearScore(int factor) {
    this._factor = factor;
  }

  /**
   * @return the factor applied to the score.
   */
  public int factor() {
    return this._factor;
  }

  /**
   * @return always "linear"
   */
  @Override
  public String type() {
    return "linear";
  }

  @Override
  public int get(int value) {
    return value * this._factor;
  }

  @Override
  public Appendable toScript(Appendable script)  throws IOException{
    script.append("function (i) {");
    script.append(" return i * "+this._factor+";");
    script.append("}");
    return script;
  }
}
