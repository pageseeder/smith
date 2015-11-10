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
 * Evaluate a password by checking against a list of banned passwords.
 */
public class BannedPasswordRuleTest extends PasswordRuleTestBase {

  private static final Map<String, Integer> EXPECTED = new HashMap<>();
  static {
    EXPECTED.put("",       0);
    // Some common banned passwords
    EXPECTED.put("123456", -100);
    EXPECTED.put("password", -100);
    // Unlikely to be banned
    EXPECTED.put("jd84ghJUS%#", 0);
    EXPECTED.put("G*sDo8@k($D", 0);
  }

  public BannedPasswordRuleTest() {
    super(new BannedPasswordRule(), EXPECTED);
  }

}
