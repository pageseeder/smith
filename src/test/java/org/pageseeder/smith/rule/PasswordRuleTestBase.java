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
package org.pageseeder.smith.rule;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.Assert;
import org.junit.Test;
import org.pageseeder.smith.PasswordRule;
import org.pageseeder.smith.Scriptable;

/**
 * Base class for password rules.
 */
public abstract class PasswordRuleTestBase {

  /**
   * Map of expected scores
   */
  private final Map<String, Integer> _expected;

  /**
   * The rule to test.
   */
  private final PasswordRule _rule;

  public PasswordRuleTestBase(PasswordRule rule, Map<String, Integer> expected) {
    this._rule = rule;
    this._expected = expected;
  }

  @Test
  public void testScoreNull() throws IOException, ScriptException {
    testScoreNull(this._rule);
  }

  @Test
  public void testScore(){
    testScore(this._rule, this._expected);
  }

  @Test
  public void testScriptNull() throws IOException, ScriptException {
    testScriptNull(this._rule);
  }

  @Test
  public void testScript() throws IOException, ScriptException {
    testScript(this._rule, this._expected);
  }

  /**
   * Check that a null password returns 0.
   *
   * @param rule the rule to test
   */
  public static void testScoreNull(PasswordRule rule) {
    Assert.assertEquals(0, rule.score(null));
  }

  public static void testScore(PasswordRule rule, Map<String, Integer> expected) {
    AssertionError failure = null;
    Assert.assertEquals(0, rule.score(null));
    for (Entry<String, Integer> test : expected.entrySet()) {
      String password = test.getKey();
      int exp = test.getValue().intValue();
      int got = rule.score(test.getKey());
      try {
        Assert.assertEquals(exp, got);
      } catch (AssertionError failed) {
        System.err.println(rule.getClass().getSimpleName()+" failed for password '"+password+"': expected "+exp+" got "+got);
        failure = failed;
      }
    }
    if (failure != null) throw failure;
  }

  /**
   * Check that the JavaScript returns 0 for a null password.
   *
   * @param rule the rule to test
   */
  public static void testScriptNull(PasswordRule rule) throws IOException, ScriptException {
    Assert.assertEquals(0, evalScript(rule, null));
  }

  public static void testScript(PasswordRule rule, Map<String, Integer> expected) throws IOException, ScriptException {
    AssertionError failure = null;
    for (Entry<String, Integer> test : expected.entrySet()) {
      String password = test.getKey();
      int exp = test.getValue().intValue();
      int got = evalScript(rule, test.getKey());
      try {
        Assert.assertEquals(exp, got);
      } catch (AssertionError failed) {
        System.err.println(rule.getClass().getSimpleName()+" failed for password '"+password+"': expected "+exp+" got "+got);
        failure = failed;
      }
    }
    if (failure != null) {
      System.err.println(rule.toScript(new StringBuilder()));
      throw failure;
    }
  }

  public static final int evalScript(Scriptable rule, String password) throws IOException, ScriptException {
    ScriptEngineManager factory = new ScriptEngineManager();
    ScriptEngine engine = factory.getEngineByName("JavaScript");
    StringBuilder script = new StringBuilder();
    script.append("(");
    rule.toScript(script);
    script.append(")(");
    if (password != null) {
      script.append("'").append(password).append("'");
    }
    script.append(")");
    return (int)Math.round((Double)engine.eval(script.toString()));
  }

}
