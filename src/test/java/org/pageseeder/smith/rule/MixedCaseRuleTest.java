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

import java.util.HashMap;
import java.util.Map;

/**
 * Evaluate a password based the number of mixed case pairs.
 *
 */
public class MixedCaseRuleTest extends PasswordRuleTestBase {

  private static final Map<String, Integer> EXPECTED = new HashMap<>();
  static {
    EXPECTED.put("", 0);
    // No mixed case
    EXPECTED.put("a", 0);
    EXPECTED.put("A", 0);
    EXPECTED.put("aa", 0);
    EXPECTED.put("AA", 0);
    EXPECTED.put("AAA", 0);
    // Mixed case
    EXPECTED.put("Aa", 1);
    EXPECTED.put("Aaa", 1);
    EXPECTED.put("Aaaa", 1);
    EXPECTED.put("AAa", 1);
    EXPECTED.put("AAAa", 1);
    EXPECTED.put("AAaa", 2);
    EXPECTED.put("AaAa", 2);
    EXPECTED.put("AaaA", 2);
  }

  public MixedCaseRuleTest() {
    super(new MixedCaseRule(), EXPECTED);
  }

}
