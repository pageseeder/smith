package com.weborganic.smith;

import java.io.PrintWriter;


/**
 * The main class
 *
 *
 * @author Christophe Lauret
 * @version 9 February 2012
 */
public final class PasswordMeter {

  /**
   * The configuration used by this meter.
   */
  private final PasswordConfig _config;

  /**
   * Creates a new password meter using the default built-in configuration.
   */
  public PasswordMeter() {
    this._config = PasswordConfig.defaultConfig();
  }

  /**
   * Creates a new password meter using the default configuration
   *
   * @throws NullPointerException if the configuration is not specified.
   */
  public PasswordMeter(PasswordConfig config) {
    if (config == null) throw new NullPointerException("Config");
    this._config = config;
  }

  /**
   * Evaluate the specified password.
   *
   * @return the password.
   */
  public String getLevel(String password) {
    int score = score(password);
    return this._config.getLevel(score);
  }

  /**
   *
   */
  public int[] vector(String password) {
    int[] vector = new int[this._config.rules().size()];
    int i = 0;
    for (PasswordRule rule : this._config.rules()) {
      vector[i++] = rule.score(password);
    }
    return vector;
  }

  /**
   * Returns the score for this password.
   *
   * @return the score given to this password.
   */
  public int score(String password) {
    if (password == null) return 0;
    int score = 0;
    for (int s : vector(password)) {
      score += s;
    }
    return score;
  }

  /**
   * Returns the configuration used by this password meter.
   *
   * @return the configuration used by this password meter.
   */
  public PasswordConfig configuration() {
    return this._config;
  }

  /**
   *
   * @param args
   */
  public static void main(String[] args) {
    PasswordMeter meter = new PasswordMeter();
    String password = "";
    int score = meter.score(password);
    String level = meter.configuration().getLevel(score);
    System.out.println(password+" -> "+level+" ("+score+")");
    for (PasswordRule rule : meter.configuration().rules()) {
      System.out.println(rule.getClass().getSimpleName()+"="+rule.score(password));
    }

    PrintWriter script = new PrintWriter(System.err);
    for (PasswordRule rule : meter.configuration().rules()) {
      if (rule instanceof Scriptable) {
        System.err.println(rule.toString());
        ((Scriptable)rule).toScript(script);
      }
      script.flush();
    }

  }

}
