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

import org.pageseeder.furi.TokenOperatorPS.Operator;

import junit.framework.TestCase;

/**
 * A test class for the <code>PAGESEEDER</code> syntax.
 *
 * <p>
 * This class uses a collection of examples defined in the specifications and the W3C URI mailing
 * list.
 *
 * @see <a href="http://lists.w3.org/Archives/Public/uri/2008Sep/0007.html">Re: URI Templates? from
 *      Roy T. Fielding on 2008-09-16 (uri@w3.org)</a>
 *
 * @author Christophe Lauret
 * @version 6 November 2009
 */
public class SyntaxPageSeederTest extends TestCase {

  /**
   * Parameters for use in all tests.
   */
  private final Parameters params = new URIParameters();

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    // parameters for examples
    this.params.set("empty",      new String[] {""});
    this.params.set("x",          new String[] {"1024"});
    this.params.set("y",          new String[] {"768"});
    this.params.set("list",       new String[] {"val1", "val2", "val3"});
    this.params.set("path",  new String[] { "/foo/bar" });
    // parameters to check characters
    this.params.set("unreserved", new String[] {"abcABC123-_.~"});
    this.params.set("gendelim",   new String[] {":/?#[]@"});
    this.params.set("subdelim",   new String[] {"!$&'()*+,;="});
    this.params.set("space",      new String[] {"\t "});
    this.params.set("unicode",    new String[] {"\u00e9\u03b1"});
    this.params.set("illegal",    new String[] {"\"%<>\\^`{|}"});
  }


  /**
   * Test that the constructor throws a NullPointerException for a <code>null</code> expression.
   */
  public void testNew_TokenOperatorRF() {
    boolean nullThrown = false;
    try {
      new TokenOperatorPS(Operator.PATH_PARAMETER, (Variable) null);
    } catch (NullPointerException ex) {
      nullThrown = true;
    } finally {
      assertTrue(nullThrown);
    }
  }

  /**
   * Test the QUERY_PARAMETER (?) operator expand method.
   */
  public void testExpand_QueryParameter() {
    assertExpandOK("{?x,y}",        "?x=1024&y=768", this.params);
    assertExpandOK("{?x,y,empty}",  "?x=1024&y=768&empty=", this.params);
    assertExpandOK("{?x,y,undef}",  "?x=1024&y=768", this.params);
    assertExpandOK("{?unreserved}", "?unreserved=abcABC123-_.~", this.params);
    assertExpandOK("{?gendelim}",   "?gendelim=%3A%2F%3F%23%5B%5D%40", this.params);
    assertExpandOK("{?subdelim}",   "?subdelim=%21%24%26%27%28%29%2A%2B%2C%3B%3D", this.params);
    assertExpandOK("{?space}",      "?space=%09%20", this.params);
    assertExpandOK("{?unicode}",    "?unicode=%C3%A9%CE%B1", this.params);
    assertExpandOK("{?illegal}",    "?illegal=%22%25%3C%3E%5C%5E%60%7B%7C%7D", this.params);
    assertExpandOK("{?null}",       "", this.params);
    assertExpandOK("{?null}",       "", null);
  }

  /**
   * Test the PATH_PARAMETER (;) operator expand method.
   */
  public void testExpand_PathParameter() {
    assertExpandOK("{;x,y}",        ";x=1024;y=768", this.params);
    assertExpandOK("{;x,y,empty}",  ";x=1024;y=768;empty", this.params);
    assertExpandOK("{;x,y,undef}",  ";x=1024;y=768", this.params);
    assertExpandOK("{;unreserved}", ";unreserved=abcABC123-_.~", this.params);
    assertExpandOK("{;gendelim}",   ";gendelim=%3A%2F%3F%23%5B%5D%40", this.params);
    assertExpandOK("{;subdelim}",   ";subdelim=%21%24%26%27%28%29%2A%2B%2C%3B%3D", this.params);
    assertExpandOK("{;space}",      ";space=%09%20", this.params);
    assertExpandOK("{;unicode}",    ";unicode=%C3%A9%CE%B1", this.params);
    assertExpandOK("{;illegal}",    ";illegal=%22%25%3C%3E%5C%5E%60%7B%7C%7D", this.params);
    assertExpandOK("{;null}",       "", this.params);
    assertExpandOK("{;null}",       "", null);
  }

  /**
   * Test the PATH_SEGMENT (/) operator expand method.
   */
  public void testExpand_PathSegment() {
    assertExpandOK("{/list,x}",     "/val1/val2/val3/1024", this.params);
    assertExpandOK("{/unreserved}", "/abcABC123-_.~", this.params);
    assertExpandOK("{/gendelim}",   "/%3A%2F%3F%23%5B%5D%40", this.params);
    assertExpandOK("{/subdelim}",   "/%21%24%26%27%28%29%2A%2B%2C%3B%3D", this.params);
    assertExpandOK("{/space}",      "/%09%20", this.params);
    assertExpandOK("{/unicode}",    "/%C3%A9%CE%B1", this.params);
    assertExpandOK("{/illegal}",    "/%22%25%3C%3E%5C%5E%60%7B%7C%7D", this.params);
    assertExpandOK("{/null}",       "", this.params);
    assertExpandOK("{/null}",       "", null);
  }

  /**
   * Test the URI Insert (+) operator expand method.
   */
  public void testExpand_URIInsert() {
    assertExpandOK("{+path}",       "/foo/bar", this.params);
    assertExpandOK("{+path,x}",     "/foo/bar,1024", this.params);
    assertExpandOK("{+empty}",      "", this.params);
    assertExpandOK("{+unreserved}", "abcABC123-_.~", this.params);
    assertExpandOK("{+gendelim}",   ":/?#[]@", this.params);
    assertExpandOK("{+subdelim}",   "!$&'()*+,;=", this.params);
    // must be pct-encoded or would result in invalid URIs
    assertExpandOK("{+space}",      "%09%20", this.params);
    assertExpandOK("{+unicode}",    "%C3%A9%CE%B1", this.params);
    assertExpandOK("{+illegal}",    "%22%25%3C%3E%5C%5E%60%7B%7C%7D", this.params);
    assertExpandOK("{+null}",       "", this.params);
    assertExpandOK("{+null}",       "", null);
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
    Token t = TokenOperatorPS.parse(expansion);
    assertEquals(value, t.expand(parameters));
  }
}
