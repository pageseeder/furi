/*
 * Copyright 2015 Allette Systems (Australia)
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
package org.pageseeder.furi;

import java.util.Map;

import junit.framework.TestCase;

/**
 * A test class for the <code>TokenBase</code> class.
 * <p>
 * Note: this class uses a basic implementation of the abstract <code>TokenBase</code> for testing.
 *
 * @author Christophe Lauret
 * @version 31 December 2008
 */
public class TokenBaseTest extends TestCase {

  /**
   * Test that the constructor throws a NullPointerException for a <code>null</code> expression.
   */
  public void testNew_Null() {
    boolean nullThrown = false;
    try {
      new TokenImpl(null);
    } catch (NullPointerException ex) {
      nullThrown = true;
    } finally {
      assertTrue(nullThrown);
    }
  }

  /**
   * Test the <code>equal</code> method.
   */
  public void testEquals() {
    TokenImpl x = new TokenImpl("t");
    TokenImpl y = new TokenImpl("t");
    TokenImpl z = new TokenImpl("T");
    TestUtils.satisfyEqualsContract(x, y, z);
  }

  /**
   * The most basic implementation of a Token, simply for testing.
   */
  static class TokenImpl extends TokenBase {
    public TokenImpl(String exp) {
      super(exp);
    }

    public String expand(Parameters variables) {
      return "";
    }

    public boolean resolve(String expanded, Map<Variable, Object> values) {
      return false;
    }
  }
}
