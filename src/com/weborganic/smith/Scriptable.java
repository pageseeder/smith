package com.weborganic.smith;

import java.io.PrintWriter;

/**
 * Indicates that a script can be build from the implementing object.
 *
 * @author Christophe Lauret
 * @version 13 February 2012
 */
public interface Scriptable {

  /**
   * Writes the script for the object on to the specified writer.
   *
   * @param out Where to write the script.
   */
  void toScript(PrintWriter out);

}
