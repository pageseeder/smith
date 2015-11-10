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
 * A rule based on the number of repeated characters in the password.
 */
public final class RepeatedCharRuleTest extends PasswordRuleTestBase {

  private static final Map<String, Integer> EXPECTED = new HashMap<>();
  static {
    EXPECTED.put("",      0);
    EXPECTED.put("a",     0);
    EXPECTED.put("Aa",    0);
    EXPECTED.put("Abc1",  0);
    EXPECTED.put("Abc1!", 0);
    EXPECTED.put("xx",    1);
    EXPECTED.put("AAA",   2);
    EXPECTED.put("1122",  2);
    EXPECTED.put("Abc1!Abc1!", 5);
  }

  public RepeatedCharRuleTest() {
    super(new RepeatedCharRule(), EXPECTED);
  }

}
