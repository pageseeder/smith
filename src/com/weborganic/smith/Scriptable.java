package com.weborganic.smith;

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
