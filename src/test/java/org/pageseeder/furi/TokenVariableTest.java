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

import junit.framework.TestCase;

/**
 * A test class for the <code>TokenVariable</code>.
 *
 * @author Christophe Lauret
 * @version 30 December 2008
 */
public class TokenVariableTest extends TestCase {

  /**
   * Test that the constructor throws a NullPointerException for a <code>null</code> expression.
   */
  public void testNew_Null() {
    boolean nullThrown = false;
    try {
      new TokenVariable((Variable) null);
    } catch (NullPointerException ex) {
      nullThrown = true;
    } finally {
      assertTrue(nullThrown);
    }
  }

  /**
   * Test the <code>equals</code> method.
   */
  public void testEquals() {
    Variable v = new Variable("v");
    Variable w = new Variable("w");
    TokenVariable x = new TokenVariable(v);
    TokenVariable y = new TokenVariable(v);
    TokenVariable z = new TokenVariable(w);
    TestUtils.satisfyEqualsContract(x, y, z);
  }

  /**
   * Test the <code>match</code> method.
   */
  public void testMatch() {
    TokenVariable v = new TokenVariable("X");
    // should match unreserved characters
    assertTrue(v.match("abcxyz"));
    assertTrue(v.match("ABCXYZ"));
    assertTrue(v.match("0123456789"));
    assertTrue(v.match("_"));
    assertTrue(v.match("-"));
    assertTrue(v.match("."));
    assertTrue(v.match("%45"));
    // should not match reserved characters in ASCII range
    assertFalse(v.match("%"));
    assertFalse(v.match("/"));
    assertFalse(v.match("*"));
    assertFalse(v.match("*"));
    // should not match reserved characters outside ASCII range
    assertFalse(v.match("\u00e9"));
  }
}
