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

import java.util.ArrayList;
import java.util.List;

import org.pageseeder.furi.TokenFactory.Syntax;
import org.pageseeder.furi.TokenOperatorD3.Operator;

import junit.framework.TestCase;

/**
 * A test class for the <code>TokenFactory</code>.
 *
 * @author Christophe Lauret
 * @version 9 February 2009
 */
public class TokenFactoryTest extends TestCase {

  /**
   * Test that the <code>NewToken</code> method returns a <code>null</code> token for a
   * <code>null</code> expression
   */
  public void testNewToken_Null() {
    assertNull(TokenFactory.getInstance().newToken(null));
  }

  /**
   * Test that the <code>NewToken</code> method returns a <code>null</code> token for an empty
   * string.
   */
  public void testNewToken_EmptyString() {
    assertNull(TokenFactory.getInstance().newToken(""));
  }

  /**
   * Test that the <code>NewToken</code> method returns a <code>TokenLiteral</code> token
   * corresponding to the specified text.
   */
  public void testNewToken_Literal() {
    assertEquals(new TokenLiteral("x"), TokenFactory.getInstance().newToken("x"));
  }

  /**
   * Test that the <code>NewToken</code> method returns a <code>TokenVariable</code> token
   * corresponding to the specified variable definition.
   */
  public void testNewToken_Variable() {
    Variable x = new Variable("x");
    assertEquals(new TokenVariable(x), TokenFactory.getInstance().newToken("{x}"));
    Variable y = new Variable("y", "z");
    assertEquals(new TokenVariable(y), TokenFactory.getInstance().newToken("{y=z}"));
    Variable q = new Variable("q", "p", new VariableType("t"));
    assertEquals(new TokenVariable(q), TokenFactory.getInstance().newToken("{t:q=p}"));
  }

  /**
   * Test that the <code>NewToken</code> method returns a <code>TokenOperator</code> token
   * corresponding to the specified operator definition.
   */
  public void testNewToken_Operator() {
    List<Variable> vars = new ArrayList<Variable>();
    Variable y = new Variable("y");
    vars.add(y);
    // make sure that all defined operators are supported
    for (Operator o : Operator.values()) {
      TokenFactory factory = TokenFactory.getInstance(Syntax.DRAFT3);
      TokenOperator t = new TokenOperatorD3(o, "x", vars);
      assertEquals(t, factory.newToken("{-" + o.name().toLowerCase() + "|x|y}"));
    }
  }

  /**
   * Test that the <code>NewToken</code> method returns a <code>TokenOperator</code> token
   * corresponding to the specified operator definition.
   */
  public void testNewToken_Operator2() {
    List<Variable> vars = new ArrayList<Variable>();
    Variable y = new Variable("y");
    vars.add(y);
    // make sure that all defined operators are supported
    for (org.pageseeder.furi.TokenOperatorPS.Operator o : org.pageseeder.furi.TokenOperatorPS.Operator.values()) {
      TokenFactory factory = TokenFactory.getInstance(Syntax.PAGESEEDER);
      TokenOperatorPS t = new TokenOperatorPS(o, vars);
      assertEquals(t, factory.newToken("{" + o.character() + "y}"));
    }
  }

}
