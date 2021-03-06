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
 * A rule based on the number of digits [0-9] in the password.
 *
 * @author Christophe Lauret
 */
public final class DigitCountRuleTest extends PasswordRuleTestBase {

  private static final Map<String, Integer> EXPECTED = new HashMap<>();
  static {
    // No digits
    EXPECTED.put("", 0);
    EXPECTED.put("a", 0);
    EXPECTED.put("abc", 0);
    EXPECTED.put("A", 0);
    // 1 digit
    EXPECTED.put("0", 1);
    EXPECTED.put("1abc", 1);
    EXPECTED.put("Ab1c", 1);
    EXPECTED.put("aBc1", 1);
    // multiple digits
    EXPECTED.put("aB1C1", 2);
    EXPECTED.put("A0B1C2", 3);
  }

  public DigitCountRuleTest() {
    super(new DigitCountRule(), EXPECTED);
  }

}
