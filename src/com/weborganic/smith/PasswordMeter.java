package com.weborganic.smith;

import java.io.IOException;


/**
 * The main class
 *
 *
 * @author Christophe Lauret
 * @version 14 February 2012
 */
public final class PasswordMeter implements Scriptable {

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

  @Override
  public Appendable toScript(Appendable script) throws IOException {
    script.append("var SMITH = {};");
    script.append("(function () {");
    // Score function
    script.append(" var score = function score(password) {");
    script.append(" var rules = [];");
    for (PasswordRule rule : this._config.rules()) {
      script.append(" rules.push(");
      rule.toScript(script).append(");\n");
    }
    script.append(" var s = 0;");
    script.append(" for (var i=0; i < rules.length; i++) { s += rules[i](password); }");
    script.append(" return s;");
    script.append(" };");
    // Export
    script.append("  SMITH.score = score;");
    script.append("})();");
    return script;
  }

  /**
   *
   * @param args
   */
  public static void main(String[] args) throws IOException {
    PasswordMeter meter = new PasswordMeter();
    String password = "";
    int score = meter.score(password);
    String level = meter.configuration().getLevel(score);
    System.out.println(password+" -> "+level+" ("+score+")");
    for (PasswordRule rule : meter.configuration().rules()) {
      System.out.println(rule.getClass().getSimpleName()+"="+rule.score(password));
    }

    // Generate the JavaScript
    meter.toScript(System.err);

  }

}
