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
 * A test class for the Syntax defined <code>DRAFT3</code> and mostly implemented
 * in <code>TokenOperatorD3</code>.
 *
 * <p>
 * This class uses the examples defined in the operators specifications.
 *
 * @see <a
 *      href="http://bitworking.org/projects/URI-Templates/spec/draft-gregorio-uritemplate-03.html#evaluating">URI
 *      Template (Draft 3) - URI Template substitution </a>
 *
 * @author Christophe Lauret
 * @version 30 December 2008
 */
public class SyntaxDraft3Test extends TestCase {

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
   * Test the OPT operator expand method.
   */
  public void testExpand_Opt() {
    // setup
    Parameters params = new URIParameters();
    params.set("foo", new String[] { "fred" });
    // test
    assertExpandOK("{-opt|fred@example.org|foo}", "fred@example.org", params);
    assertExpandOK("{-opt|fred@example.org|bar}", "", params);
  }

  /**
   * Test the NEG operator expand method.
   */
  public void testExpand_Neg() {
    // setup
    Parameters params = new URIParameters();
    params.set("foo", new String[] { "fred" });
    // test
    assertExpandOK("{-neg|fred@example.org|foo}", "", params);
    assertExpandOK("{-neg|fred@example.org|bar}", "fred@example.org", params);
  }

  /**
   * Test the PREFIX operator expand method.
   */
  public void testExpand_Prefix() {
    // setup
    Parameters params = new URIParameters();
    params.set("foo", new String[] { "fred" });
    params.set("bar", new String[] { "fee", "fi", "fo", "fum" });
    params.set("baz", new String[] {});
    // test
    assertExpandOK("{-prefix|/|foo}", "/fred", params);
    assertExpandOK("{-prefix|/|bar}", "/fee/fi/fo/fum", params);
    assertExpandOK("{-prefix|/|baz}", "", params);
    assertExpandOK("{-prefix|/|qux}", "", params);
  }

  /**
   * Test the SUFFIX operator expand method.
   */
  public void testExpand_Suffix() {
    // setup
    Parameters params = new URIParameters();
    params.set("foo", new String[] { "fred" });
    params.set("bar", new String[] { "fee", "fi", "fo", "fum" });
    params.set("baz", new String[] {});
    // test
    assertExpandOK("{-suffix|/|foo}", "fred/", params);
    assertExpandOK("{-suffix|/|bar}", "fee/fi/fo/fum/", params);
    assertExpandOK("{-suffix|/|baz}", "", params);
    assertExpandOK("{-suffix|/|qux}", "", params);
  }

  /**
   * Test the JOIN operator expand method.
   */
  public void testExpand_Join() {
    // setup
    Parameters params = new URIParameters();
    params.set("foo", "fred");
    params.set("bar", "barney");
    params.set("baz", "");
    // test
    assertExpandOK("{-join|&|foo,bar,baz,qux}", "foo=fred&bar=barney&baz=", params);
    assertExpandOK("{-join|&|bar}", "bar=barney", params);
    assertExpandOK("{-join|&|qux}", "", params);
  }

  /**
   * Test the LIST operator expand method.
   */
  public void testExpand_List() {
    // setup
    Parameters params = new URIParameters();
    params.set("foo", new String[] { "fred", "barney", "wilma" });
    params.set("bar", new String[] { "a", "", "c" });
    params.set("baz", new String[] { "betty" });
    params.set("qux", new String[] {});
    // test
    assertExpandOK("{-list|/|foo}", "fred/barney/wilma", params);
    assertExpandOK("{-list|/|bar}", "a//c", params);
    assertExpandOK("{-list|/|baz}", "betty", params);
    assertExpandOK("{-list|/|qux}", "", params);
    assertExpandOK("{-list|/|corge}", "", params);
  }

  // private helpers
  // --------------------------------------------------------------------------

  /**
   * Asserts that given expansion is expanded correctly given a set of parameters.
   *
   * @param expansion The expression to expand.
   * @param value The expected value (expanded form).
   * @param parameters The parameters to use for expansion.
   */
  private void assertExpandOK(String expansion, String value, Parameters parameters) {
    Token t = TokenOperatorD3.parse(expansion);
    assertEquals(value, t.expand(parameters));
  }
}
