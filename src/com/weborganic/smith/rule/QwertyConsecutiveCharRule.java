package com.weborganic.smith.rule;

import java.io.IOException;
import java.util.Map;

import com.weborganic.smith.PasswordRule;
import com.weborganic.smith.ScoreFunction;
import com.weborganic.smith.Scriptable;
import com.weborganic.smith.function.ScoreArray;

/**
 * Evaluate a password by checking whether it follows a sequence from a QWERTY keyboard.
 *
 * @author Christophe Lauret
 * @version 15 June 2012
 */
public class QwertyConsecutiveCharRule implements PasswordRule, Scriptable {

  /**
   * The sequences on a QUERTY keyboard.
   */
  private static String[] QUERTY_SEQUENCES = new String[]{"qwertyuiop", "asdfghjkl", "zxcvbnm"};

  /**
   * The array of scores.
   */
  private ScoreFunction _function;

  @Override
  public int score(String password) {
    if (password == null) return 0;
    int count = 0;
    for (String sequence : QUERTY_SEQUENCES) {
      if (password.indexOf(sequence) >= 0) {
        count = password.length();
      }
    }
    return this._function.get(count);
  }

  @Override
  public void configure(Map<String, String> config) {
    this._function = ScoreArray.parse(config);
  }

  // Static helpers
  // ------------------------------------------------------------------------------------------------------------------

  @Override
  public Appendable toScript(Appendable script) throws IOException {
    script.append("function (p) {");
    script.append(" var f = ");
    this._function.toScript(script).append(";");
    script.append(" var q = [");
    for (int i = 0 ; i < QUERTY_SEQUENCES.length; i++) {
      if (i > 0) script.append(',');
      script.append('\'').append(QUERTY_SEQUENCES[i]).append('\'');
    }
    script.append("];");
    script.append(" var n = 0;");
    script.append(" for (var i = 0; i < q.length; i++) {");
    script.append(" if (q[i].indexOf(p) > 0) { n = p.length; }");
    script.append(" }");
    script.append(" return f(n);");
    script.append("}");
    return script;
  }
}
