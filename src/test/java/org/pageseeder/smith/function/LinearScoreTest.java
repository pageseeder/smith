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
package org.pageseeder.smith.function;

import org.junit.Assert;
import org.junit.Test;

/**
 * Returns the score linearly based on the given value.
 */
public final class LinearScoreTest {

  @Test
  public void testDefaultConstructor() {
    LinearScore score = new LinearScore();
    Assert.assertEquals(1, score.factor());
  }

  @Test
  public void testConstructor() {
    int[] factors = new int[]{Integer.MIN_VALUE, -17, -1, 0, 1, 31, Integer.MAX_VALUE};
    for (int f : factors) {
      LinearScore score = new LinearScore(f);
      Assert.assertEquals(f, score.factor());
    }
  }

  @Test
  public void testGetWithPositiveFactor() {
    int[] factors = new int[]{1, 31};
    int[] values = new int[]{-17, -1, 0, 1, 31};
    for (int f : factors) {
      for (int v : values) {
        LinearScore score = new LinearScore(f);
        Assert.assertEquals(v*f, score.get(v));
      }
    }
  }

  @Test
  public void testGetWithFactorZero() {
    int[] values = new int[]{-17, -1, 0, 1, 31};
    LinearScore score = new LinearScore(0);
    for (int v : values) {
      Assert.assertEquals(0, score.get(v));
    }
  }

  @Test
  public void testGetWithNegativeFactor() {
    int[] factors = new int[]{-1, -31};
    int[] values = new int[]{-17, -1, 0, 1, 31};
    for (int f : factors) {
      for (int v : values) {
        LinearScore score = new LinearScore(f);
        Assert.assertEquals(v*f, score.get(v));
      }
    }
  }

}
