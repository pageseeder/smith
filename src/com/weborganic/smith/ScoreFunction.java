package com.weborganic.smith;

/**
 * A score
 *
 * @author Christophe Lauret
 * @version
 */
public interface ScoreFunction {

  /**
   * Returns the
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
