package com.weborganic.smith;

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
   * @return
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
