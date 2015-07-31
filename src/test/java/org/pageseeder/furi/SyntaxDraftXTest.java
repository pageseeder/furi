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

import org.pageseeder.furi.TokenFactory.Syntax;
import org.pageseeder.furi.TokenOperatorDX.Operator;

import junit.framework.TestCase;

/**
 * A test class for the URI template syntax <code>DRAFTX</code>.
 *
 * <p>
 * This class uses the examples defined in the operators specifications.
 *
 * @see <a href="http://code.google.com/p/uri-templates/source/browse/trunk/spec/draft-gregorio-uritemplate.xml"></a>
 *
 * @author Christophe Lauret
 * @version 5 November 2009
 */
public class SyntaxDraftXTest extends TestCase {

  /**
   * Parameters for use in all tests.
   */
  private final Parameters params = new URIParameters();

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    // parameters for examples
    this.params.set("var", "value");
    this.params.set("hello", "Hello World!");
    this.params.set("empty","");
    this.params.set("list", new String[]{"val1", "val2", "val3"});
    this.params.set("keys", new String[]{"key1", "val1", "key2", "val2"});
    this.params.set("path", "/foo/bar");
    this.params.set("x", "1024");
    this.params.set("y", "768");

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
  public void testNew_TokenOperatorDX() {
    boolean nullThrown = false;
    try {
      new TokenOperatorDX(Operator.PATH_PARAMETER, (Variable) null);
    } catch (NullPointerException ex) {
      nullThrown = true;
    } finally {
      assertTrue(nullThrown);
    }
  }

  /**
   * Test Sec X.1: Simple substitution with comma-separated values.
   *
   * <pre>
   *   {var}                  value
   *   {hello}                Hello%20World%21
   *   {path}/here            %2Ffoo%2Fbar/here
   *   {x,y}                  1024,768
   *   {var=default}          value
   *   {undef=default}        default
   * </pre>
   */
  public void testExpand_SecX1() {
    assertExpandOK("{var}",           "value", this.params);
    assertExpandOK("{hello}",         "Hello%20World%21", this.params);
    assertExpandOK("{path}/here",     "%2Ffoo%2Fbar/here", this.params);
    assertExpandOK("{x,y}",           "1024,768", this.params);
    assertExpandOK("{var=default}",   "value", this.params);
    assertExpandOK("{undef=default}", "default", this.params);
  }

  /**
   * Test Sec  X.2: Reserved substitution with comma-separated values.
   *
   * <pre>
   *   {+var}                 value
   *   {+hello}               Hello%20World!
   *   {+path}/here           /foo/bar/here
   *   {+path,x}/here         /foo/bar,1024/here
   *   {+path}{x}/here        /foo/bar1024/here
   *   {+empty}/here          /here
   * </pre>
   */
  public void testExpand_SecX2() {
    assertExpandOK("{+var}",          "value", this.params);
    assertExpandOK("{+hello}",        "Hello%20World!", this.params);
    assertExpandOK("{+path}/here",    "/foo/bar/here", this.params);
    assertExpandOK("{+path,x}/here",  "/foo/bar,1024/here", this.params);
    assertExpandOK("{+path}{x}/here", "/foo/bar1024/here", this.params);
    assertExpandOK("{+empty}/here",   "/here", this.params);
  }

  /**
   * Sec X.3: Encoded path segment parameters, semicolon-prefixed.
   *
   * <pre>
   *   {;x,y}                 ;x=1024;y=768
   *   {;x,y,empty}           ;x=1024;y=768;empty
   *   {;x,y,undef}           ;x=1024;y=768
   *   {;%keys}               ;key1=val1;key2=val2
   * </pre>
   */
  public void testExpand_SecX3() {
    assertExpandOK("{;x,y}",       ";x=1024;y=768", this.params);
    assertExpandOK("{;x,y,empty}", ";x=1024;y=768;empty", this.params);
    assertExpandOK("{;x,y,undef}", ";x=1024;y=768", this.params);
    assertExpandOK("{;%keys}",     ";key1=val1;key2=val2", this.params);
  }

  /**
   * Sec X.4: Encoded query parameters, ampersand-separated.
   *
   * <pre>
   *   {?x,y}          ?x=1024&amp;y=768
   *   {?x,y,empty}    ?x=1024&amp;y=768&amp;empty=
   *   {?x,y,undef}    ?x=1024&amp;y=768
   *   {?list}         ?list=val1,val2,val3
   *   {?%keys}        ?key1=val1&amp;key2=val2
   *   {?@list}        ?list=val1&amp;list2=val2&amp;list3=val3
   * </pre>
   */
  public void testExpand_SecX4() {
    assertExpandOK("{?x,y}",       "?x=1024&y=768", this.params);
    assertExpandOK("{?x,y,empty}", "?x=1024&y=768&empty=", this.params);
    assertExpandOK("{?x,y,undef}", "?x=1024&y=768", this.params);
    assertExpandOK("{?list}",      "?list=val1,val2,val3", this.params);
    assertExpandOK("{?%keys}",     "?key1=val1&key2=val2", this.params);
    assertExpandOK("{?@list}",     "?list=val1&list2=val2&list3=val3", this.params);
  }

  /**
   * Sec X.5: Encoded path segments, slash-separated.
   *
   * <pre>
   *   {/var}                 /value
   *   {/list}                /val1/val2/val3
   *   {/list,x}              /val1/val2/val3/1024
   * </pre>
   */
  public void testExpand_SecX5() {
    assertExpandOK("{/var}",    "/value", this.params);
    assertExpandOK("{/list}",   "/val1/val2/val3", this.params);
    assertExpandOK("{/list,x}", "/val1/val2/val3/1024", this.params);
  }

  /**
   * Given the variable assignments:
   *
   * <pre>
   *   var   := "value";
   *   empty := "";
   *   undef := null;
   *   name  := [ "Fred", "Wilma", "Pebbles" ];
   *   favs  := [ "color", "red", "volume", "high" ];
   *
   * Example Template     Expansion
   *
   *   {var=default}      value
   *   {undef=default}    default
   *
   *   x{empty}y          xy
   *   x{empty=_}y        xy
   *   x{undef}y          xy
   *   x{undef=_}y        x_y
   *
   *   x{?@name=none}     x?name=Fred&amp;name2=Wilma&amp;name3=Pebbles
   *   x{?@undef}         x
   *   x{?@undef=none}    x?undef=none
   *   x{?@empty}         x?empty=
   *   x{?@empty=none}    x?empty=
   *
   *   x{?%favs=none}     x?color=red&amp;volume=high
   *   x{?%undef}         x
   *   x{?%undef=none}    x?undef=none
   *   x{?%empty}         x?empty=
   *   x{?%empty=none}    x?empty=
   * </pre>
   */
  public void testExpand_DefaultValues() {
    // Set the parameters
    Parameters p = new URIParameters();
    p.set("var",   "value");
    p.set("empty", "");
    p.set("name",  new String[]{"Fred", "Wilma", "Pebbles"});
    p.set("favs",  new String[]{"color", "red", "volume", "high"});
    // Tests
    assertExpandOK("{var=default}",   "value", p);
    assertExpandOK("{undef=default}", "default", p);
    assertExpandOK("x{empty}y",       "xy", p);
    assertExpandOK("x{empty=_}y",     "xy", p);
    assertExpandOK("x{undef}y",       "xy", p);
    assertExpandOK("x{undef=_}y",     "x_y", p);
    assertExpandOK("x{?@name=none}",  "x?name=Fred&name2=Wilma&name3=Pebbles", p);
    assertExpandOK("x{?@undef}",      "x", p);
//    assertExpandOK("x{?@undef=none}", "x?undef=none", p);
    assertExpandOK("x{?@empty}",      "x?empty=", p);
//    assertExpandOK("x{?@empty=none}", "x?empty=", p);
    assertExpandOK("x{?%favs=none}",  "x?color=red&volume=high", p);
    assertExpandOK("x{?%undef}",      "x", p);
//    assertExpandOK("x{?%undef=none}", "x?undef=none", p);
//    assertExpandOK("x{?%empty}",      "x?empty=", p);
//    assertExpandOK("x{?%empty=none}", "x?empty=", p);
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
    TokenFactory factory = TokenFactory.getInstance(Syntax.DRAFTX);
    URITemplate template = new URITemplate(expansion, factory);
    assertEquals(value, template.expand(parameters));
  }
}
