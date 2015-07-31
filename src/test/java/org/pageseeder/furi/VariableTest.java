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

import java.util.Arrays;

import org.pageseeder.furi.Variable.Form;

import junit.framework.TestCase;

/**
 * A test class for variables.
 *
 * @author Christophe Lauret
 * @version 5 November 2009
 */
public class VariableTest extends TestCase {

  /**
   * Test that the constructor throws a NullPointerException for a <code>null</code> variable name.
   */
  public void testNew_NullName() {
    boolean nullThrown = false;
    try {
      new Variable(null, null);
    } catch (NullPointerException ex) {
      nullThrown = true;
    } finally {
      assertTrue(nullThrown);
    }
  }

  /**
   * Test that the constructor throws an IllegalArgumentException for an empty string as a name.
   */
  public void testNew_EmptyString() {
    boolean illegalThrown = false;
    try {
      new Variable("", null);
    } catch (IllegalArgumentException ex) {
      illegalThrown = true;
    } finally {
      assertTrue(illegalThrown);
    }
  }

  /**
   * Test that the constructor sets the name appropriately.
   * The variable should remain untyped.
   */
  public void testNew_Default() {
    // default value specified and null
    Variable var = new Variable("name", null);
    assertEquals("", var.defaultValue());
    assertEquals(null, var.type());
   // default value unspecified
    var = new Variable("name");
    assertEquals("", var.defaultValue());
    assertEquals(null, var.type());
    assertEquals(Form.STRING, var.form());
  }

  /**
   * Test that the <code>isValidName</code> method work as specified.
   */
  public void testIsValidName() {
    // invalid
    assertFalse(Variable.isValidName(null));
    assertFalse(Variable.isValidName(""));
    assertFalse(Variable.isValidName("_"));
    assertFalse(Variable.isValidName("-"));
    assertFalse(Variable.isValidName("."));
    // valid
    assertTrue(Variable.isValidName("a"));
    assertTrue(Variable.isValidName("abc"));
    assertTrue(Variable.isValidName("a-"));
    assertTrue(Variable.isValidName("a_"));
    assertTrue(Variable.isValidName("a."));
  }

  /**
   * Test the <code>equals</code> method for variables with no default value.
   */
  public void testEquals_noDefault() {
    Variable x = new Variable("n");
    Variable y = new Variable("n");
    Variable z = new Variable("m");
    TestUtils.satisfyEqualsContract(x, y, z);
  }

  /**
   * Test the <code>equals</code> method for variables with a default value.
   */
  public void testEquals_default() {
    Variable x = new Variable("n", "x");
    Variable y = new Variable("n", "x");
    Variable z = new Variable("n", "y");
    TestUtils.satisfyEqualsContract(x, y, z);
  }

  /**
   * Test the <code>parse</code> method for normal situations.
   */
  public void testParse_OK() {
    VariableType t = new VariableType("t");
    assertEquals(new Variable("x"),      Variable.parse("x"));
    assertEquals(new Variable("x", "y"), Variable.parse("x=y"));
    assertEquals(new Variable("x", ""),  Variable.parse("x="));
    // typed
    assertEquals(new Variable("x", null), Variable.parse("t:x"));
    assertEquals(new Variable("x", null), Variable.parse("t:x="));
    assertEquals(new Variable("x", "y"),  Variable.parse("t:x=y"));
    assertEquals(new Variable("x", "y"),  Variable.parse(":x=y"));
    // with different form
    assertEquals(new Variable("x", null, null, Form.LIST), Variable.parse("@x"));
    assertEquals(new Variable("x", null, null, Form.MAP ), Variable.parse("%x="));
    assertEquals(new Variable("x", null, null, Form.LIST), Variable.parse("@t:x"));
    assertEquals(new Variable("x", null, null, Form.MAP ), Variable.parse("%t:x="));
    assertEquals(new Variable("x", "y", null, Form.LIST ),  Variable.parse("@t:x=y"));
    assertEquals(new Variable("x", "y", null, Form.MAP ),  Variable.parse("%:x=y"));
    // type does not affect equality
    assertEquals(t, Variable.parse("t:x").type());
    assertEquals(t, Variable.parse("t:x=").type());
    assertEquals(t, Variable.parse("t:x=y").type());

    assertEquals(null,  Variable.parse(":x=y").type());
  }

  /**
   * Test the <code>parse</code> method for a <code>null</code> value.
   */
  public void testParse_ErrorNull() {
    try {
      Variable.parse(null);
      assertTrue("No exception was thrown", false);
    } catch (Exception ex) {
      assertEquals(NullPointerException.class, ex.getClass());
    }
  }

  /**
   * Test the <code>parse</code> method with syntax error.
   */
  public void testParse_ErrorSyntax() {
    try {
      Variable.parse("=y");
      assertTrue("No exception was thrown", false);
    } catch (Exception ex) {
      assertEquals(URITemplateSyntaxException.class, ex.getClass());
    }
  }

  /**
   * Test the <code>value</code> method.
   */
  public void testValue() {
    // setup
    Parameters params = new URIParameters();
    params.set("a", new String[] {});
    params.set("b", "");
    params.set("c", "m");
    params.set("d", new String[] { "m", "n" });
    params.set("e", new String[] { "m", "", "n" });
    // test
    assertEquals("", new Variable("a").value(params));
    assertEquals("x", new Variable("a", "x").value(params));
    assertEquals("", new Variable("b").value(params));
    assertEquals("", new Variable("b", "x").value(params));
    assertEquals("m", new Variable("c").value(params));
    assertEquals("m", new Variable("c", "x").value(params));
    assertEquals("m", new Variable("d").value(params));
    assertEquals("m", new Variable("d", "x").value(params));
    assertEquals("m", new Variable("e").value(params));
    assertEquals("m", new Variable("e", "x").value(params));
  }

  /**
   * Test the <code>values</code> method.
   */
  public void testValues() {
    // setup
    Parameters params = new URIParameters();
    params.set("a", new String[] {});
    params.set("b", "");
    params.set("c", "m");
    params.set("d", new String[] { "m", "n" });
    params.set("e", new String[] { "m", "", "n" });
    // test
    assertArrayEquals(new String[] {}, new Variable("a").values(params));
    assertArrayEquals(new String[] { "x" }, new Variable("a", "x").values(params));
    assertArrayEquals(new String[] {}, new Variable("b").values(params));
    assertArrayEquals(new String[] { "x" }, new Variable("b", "x").values(params));
    assertArrayEquals(new String[] { "m" }, new Variable("c").values(params));
    assertArrayEquals(new String[] { "m" }, new Variable("c", "x").values(params));
    assertArrayEquals(new String[] { "m", "n" }, new Variable("d").values(params));
    assertArrayEquals(new String[] { "m", "n" }, new Variable("d", "x").values(params));
    assertArrayEquals(new String[] { "m", "", "n" }, new Variable("e").values(params));
    assertArrayEquals(new String[] { "m", "", "n" }, new Variable("e", "x").values(params));
  }

  // private helpers
  // --------------------------------------------------------------------------

  /**
   * Asserts that the arrays are equal by comparing the string value of their components.
   *
   * @param exp The expected string array.
   * @param act The actual string array.
   */
  private void assertArrayEquals(String[] exp, String[] act) {
    assertEquals(Arrays.deepToString(exp), Arrays.deepToString(act));
  }
}
