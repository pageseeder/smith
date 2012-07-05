package com.weborganic.smith;

import java.io.IOException;
import java.util.List;


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
   * Returns the vector of scores for each rule in the order in which they are defined.
   *
   * @param password the password to evaluate
   * @return the corresponding scores for each rule.
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
   * Indicates whether the specified password matches at least the supplied level.
   *
   * @param password The password to evaluate.
   * @param level    The level that must be matched.
   *
   * @return <code>true</code> if the score is sufficient  to match at least this level;
   *         <code>false</code> otherwise.
   *
   * @throws IllegalArgumentException If the level is not defined in the config.
   * @throws NullPointerException     If the level is <code>null</code>.
   */
  public boolean isAtLeast(String password, String level) {
    int threshold = this._config.getThreshold(level);
    int score = score(password);
    return score > threshold;
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
    script.append(" var score = function score(p) {");
    script.append(" var rules = [];");
    for (PasswordRule rule : this._config.rules()) {
      script.append(" rules.push(");
      rule.toScript(script).append(");\n");
    }
    script.append(" var s = 0;");
    script.append(" for (var i=0; i < rules.length; i++) { s += rules[i](p); }");
    script.append(" return s;");
    script.append(" };");
    // Level function
    script.append(" var level = function level(s) {");
    List<String> levels = this._config.levels();
    for (int i = levels.size() - 1; i >= 0; i--) {
      String level = levels.get(i);
      if (i > 0) {
        int threshold = this._config.getThreshold(level);
        script.append(" if (s >= ").append(Integer.toString(threshold)).append(") { return '").append(level).append("';}");
      } else {
        script.append(" return '").append(level).append("';");
      }
    }
    script.append(" };");
    // Get Strength
    script.append(" var get = function get(p) {");
    script.append(" var s = score(p);");
    script.append(" var l = level(s);");
    script.append(" return {score: s, level: l};");
    script.append(" };");
    // Export
    script.append(" SMITH.score = score;");
    script.append(" SMITH.level = level;");
    script.append(" SMITH.get = get;");
    String version = Package.getPackage("com.weborganic.smith").getImplementationVersion();
    script.append(" SMITH.version = '").append(version != null? version : "unspecified").append("';");
    script.append("})();");
    return script;
  }

  /**
   *
   * @param args
   */
  public static void main(String[] args) throws IOException {
    PasswordMeter meter = new PasswordMeter();
    if (args.length == 0) {
      System.err.println("PasswordMeter [password]");
    }
    if (args.length > 0) {
      String password = args[0];
      int score = meter.score(password);
      String level = meter.configuration().getLevel(score);
      System.out.println(password+" -> "+level+" ("+score+")");
      for (PasswordRule rule : meter.configuration().rules()) {
        System.out.println(rule.getClass().getSimpleName()+"="+rule.score(password));
      }
    } else {
      meter.toScript(System.out);
    }
  }

}
