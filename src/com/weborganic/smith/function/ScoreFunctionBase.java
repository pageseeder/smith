package com.weborganic.smith.function;

import com.weborganic.smith.ScoreFunction;

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
