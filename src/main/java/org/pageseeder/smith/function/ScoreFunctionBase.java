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

import org.pageseeder.smith.ScoreFunction;

/**
 * Provides a score function.
 *
 * @author Christophe Lauret
 * @version 9 February 2012
 */
public abstract class ScoreFunctionBase implements ScoreFunction {

  /**
   * The type of the score function.
   */
  private final String _type;

  /**
   * Creates a new score function for the specified type
   *
   * @param type the type.
   */
  public ScoreFunctionBase(String type) {
    this._type = type;
  }

  @Override
  public final String type() {
    return this._type;
  }

}
