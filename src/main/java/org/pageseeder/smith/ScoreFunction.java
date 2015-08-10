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

/**
 * A function to apply to a result in order to produce the score.
 *
 * @author Christophe Lauret
 * @version 14 February 2012
 */
public interface ScoreFunction extends Scriptable {

  /**
   * Returns the type for this function.
   *
   * @return the type for this function.
   */
  String type();

  /**
   * Returns the score based on the specified value.
   *
   * @param value The value
   * @return the corresponding score.
   */
  int get(int value);

}
