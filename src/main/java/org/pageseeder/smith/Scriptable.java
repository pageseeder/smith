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

import java.io.IOException;

/**
 * Indicates that a script can be build from the implementing object.
 *
 * @author Christophe Lauret
 * @version 14 February 2012
 */
public interface Scriptable {

  /**
   * Writes the script for the object on to the specified writer.
   *
   * @param script Where to write the script.
   *
   * @return A reference to this <code>Appendable</code>
   *
   * @throws NullPointerException If the specified appendable is <code>null</code>
   * @throws IOException          If an I/O error occurs
   */
  Appendable toScript(Appendable script) throws IOException;

}
