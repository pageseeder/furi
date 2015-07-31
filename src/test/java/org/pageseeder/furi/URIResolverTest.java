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

import org.pageseeder.furi.URIResolver.MatchRule;

import junit.framework.TestCase;

/**
 * A test class for the <code>URIResolver</code>.
 *
 * @author Christophe Lauret
 * @version 27 May 2009
 */
public class URIResolverTest extends TestCase {

  /**
	 * Test the <code>find</code> method.
	 */
  public void testFind() {
    URIResolver resolver = new URIResolver("/group/1892/home");
    List<URIPattern> patterns = new ArrayList<URIPattern>();
    patterns.add(new URIPattern("/group/{groupid}/list"));
    patterns.add(new URIPattern("/group/{groupid}/home"));
    patterns.add(new URIPattern("/group/{groupid}/add"));
    assertEquals(new URIPattern("/group/{groupid}/home"), resolver.find(patterns));
  }

  /**
   * Test the <code>find</code> method.
   */
  public void testFind_First() {
    URIResolver resolver = new URIResolver("/document/history/dir/doc.xml");
    List<URIPattern> patterns = new ArrayList<URIPattern>();
    patterns.add(new URIPattern("/document/{+document}"));
    patterns.add(new URIPattern("/document/history/{+document}"));
    patterns.add(new URIPattern("/{+document}"));
    assertEquals(new URIPattern("/document/{+document}"), resolver.find(patterns, MatchRule.FIRST_MATCH));
  }

  /**
   * Test the <code>find</code> method.
   */
  public void testFind_Best() {
    URIResolver resolver = new URIResolver("/document/history/dir/doc.xml");
    List<URIPattern> patterns = new ArrayList<URIPattern>();
    patterns.add(new URIPattern("/document/{+document}"));
    patterns.add(new URIPattern("/document/history/{+document}"));
    patterns.add(new URIPattern("/{+document}"));
    assertEquals(new URIPattern("/document/history/{+document}"), resolver.find(patterns, MatchRule.BEST_MATCH));
  }

  /**
   * Test the <code>resolve</code> method with some int values.
   */
  public void testResolve_Int() {
    URIResolver resolver = new URIResolver("/group/1892/home");
    URIPattern p = new URIPattern("/group/{groupid}/home");
    assertTrue(p.match(resolver.uri()));
    URIResolveResult r = resolver.resolve(p);
    assertEquals(URIResolveResult.Status.RESOLVED, r.getStatus());
    assertEquals("1892", r.get("groupid"));
  }

  /**
   * Test the <code>resolve</code> method with some int values.
   */
  public void testResolve_IntTyped() {
    URIResolver resolver = new URIResolver("/group/1892/home");
    URIPattern p = new URIPattern("/group/{int:groupid}/home");
    assertTrue(p.match(resolver.uri()));
    VariableBinder b = new VariableBinder();
    b.bindType("int", new VariableResolver(){
      public boolean exists(String v) {return v.matches("\\d+");}
      public Integer resolve(String v) {return exists(v)? Integer.valueOf(v) : null;};
    });
    URIResolveResult r = resolver.resolve(p, b);
    assertEquals(URIResolveResult.Status.RESOLVED, r.getStatus());
    assertEquals(1892, r.get("groupid"));
  }

  /**
   * Test the <code>resolve</code> method with some String values.
   */
  public void testResolve_String() {
    URIResolver resolver = new URIResolver("/user/~clauret/home");
    URIPattern p = new URIPattern("/user/{account}/home");
    assertTrue(p.match(resolver.uri()));
    URIResolveResult r = resolver.resolve(p);
    assertEquals(URIResolveResult.Status.RESOLVED, r.getStatus());
    assertEquals("~clauret", r.get("account"));
  }

  /**
   * Test the <code>resolve</code> method with some String values.
   */
  public void testResolve_String2() {
    URIResolver resolver = new URIResolver("/user/~clauret/home");
    URIPattern p = new URIPattern("/{section}/{account}/home");
    assertTrue(p.match(resolver.uri()));
    VariableBinder b = new VariableBinder();
    b.bindName("section", new VariableResolverList(new String[]{"user","group"}));
    URIResolveResult r = resolver.resolve(p,b);
    assertEquals(URIResolveResult.Status.RESOLVED, r.getStatus());
    assertEquals("user", r.get("section"));
    assertEquals("~clauret", r.get("account"));
  }

  /**
   * Test the <code>resolve</code> method with some escaped values.
   */
  public void testResolve_Escape() {
    URIResolver resolver = new URIResolver("/tag/Caf%C3%A9");
    URIPattern p = new URIPattern("/tag/{tag}");
    assertTrue(p.match(resolver.uri()));
    URIResolveResult r = resolver.resolve(p);
    assertEquals(URIResolveResult.Status.RESOLVED, r.getStatus());
    assertEquals("Caf\u00e9", r.get("tag"));
  }

  /**
   * Test the <code>resolve</code> method with some multiple values.
   */
  public void testResolve_Multiple() {
    URIResolver resolver = new URIResolver("http://acme.com/dev/clauret");
    URIPattern p = new URIPattern("{scheme}://{domain}/{group}/{user}");
    assertTrue(p.match(resolver.uri()));
    URIResolveResult r = resolver.resolve(p);
    assertEquals(URIResolveResult.Status.RESOLVED, r.getStatus());
    assertEquals("http",     r.get("scheme"));
    assertEquals("acme.com", r.get("domain"));
    assertEquals("dev",      r.get("group"));
    assertEquals("clauret",  r.get("user"));
  }

  /**
   * Test the <code>resolve</code> method with some multiple values.
   */
  public void testResolve_Multiple2() {
    URIResolver resolver = new URIResolver("/documents;label=technical;version=1.0");
    URIPattern p = new URIPattern("/documents;label={label};version={version}");
    assertTrue(p.match(resolver.uri()));
    URIResolveResult r = resolver.resolve(p);
    assertEquals(URIResolveResult.Status.RESOLVED, r.getStatus());
    assertEquals("technical", r.get("label"));
    assertEquals("1.0",       r.get("version"));
  }

  /**
   * Test the <code>resolve</code> method with some multiple values.
   */
  public void testResolve_OperatorPathParam1Var() {
    URIResolver resolver = new URIResolver("/documents;label=technical");
    URIPattern p = new URIPattern("/documents{;label}");
    assertTrue(p.match(resolver.uri()));
    URIResolveResult r = resolver.resolve(p);
    assertEquals(URIResolveResult.Status.RESOLVED, r.getStatus());
    assertEquals("technical", r.get("label"));
  }

  /**
   * Test the <code>resolve</code> method with some multiple values.
   */
  public void testResolve_OperatorPathParamNVar() {
    URIResolver resolver = new URIResolver("/documents;label=technical;version=1.0");
    URIPattern p = new URIPattern("/documents{;label,version}");
    assertTrue(p.match(resolver.uri()));
    URIResolveResult r = resolver.resolve(p);
    assertEquals(URIResolveResult.Status.RESOLVED, r.getStatus());
    assertEquals("technical", r.get("label"));
    assertEquals("1.0",       r.get("version"));
  }

  /**
   * Test the <code>resolve</code> method with some objects values.
   */
  public void testResolve_Objects() {
    URIResolver resolver = new URIResolver("/documents;label=technical;version=1.0");
    URIPattern p = new URIPattern("/documents;label={label};version={version}");
    assertTrue(p.match(resolver.uri()));
    URIResolveResult r = resolver.resolve(p);
    assertEquals(URIResolveResult.Status.RESOLVED, r.getStatus());
    assertEquals("technical", r.get("label"));
    assertEquals("1.0",       r.get("version"));
  }

  /**
   * Test the <code>resolve</code> method with some objects values.
   */
  public void testResolve_URIInsert() {
    URIResolver resolver = new URIResolver("/path/dir/subdir/document.xml");
    URIPattern p = new URIPattern("/path/{+path}");
    assertTrue(p.match(resolver.uri()));
    URIResolveResult r = resolver.resolve(p);
    assertEquals(URIResolveResult.Status.RESOLVED, r.getStatus());
    assertEquals("dir/subdir/document.xml", r.get("path"));
  }

  /**
   * Test the <code>resolve</code> method with some objects values.
   */
  public void testResolve_URIInsert2() {
    URIResolver resolver = new URIResolver("/path/dir/subdir/document.xml/comments");
    URIPattern p = new URIPattern("/path/{+path}/comments");
    assertTrue(p.match(resolver.uri()));
    URIResolveResult r = resolver.resolve(p);
    assertEquals(URIResolveResult.Status.RESOLVED, r.getStatus());
    assertEquals("dir/subdir/document.xml", r.get("path"));
  }

  /**
   *
   */
  public void testResolve_NoValue() {
    // setup
    URIPattern pattern = new URIPattern("/{null}");
    URIResolver resolver = new URIResolver("/null");

    // create a variable resolve which always returns null
    VariableBinder binder = new VariableBinder();
    binder.bindName("null", new VariableResolverList());

    // resolve
    ResolvedVariables result = resolver.resolve(pattern, binder);
  }



  public void testSample() {
    // setting up the patterns when parsing the configuration
    List<URIPattern> patterns = new ArrayList<URIPattern>();
    patterns.add(new URIPattern("/home"));
    patterns.add(new URIPattern("/path/{+path}"));
    patterns.add(new URIPattern("/documents{;label}"));
    patterns.add(new URIPattern("/document/*"));

    // test case #0
    URIResolver resolver0 = new URIResolver("/home");
    URIPattern pattern0 = resolver0.find(patterns);
    assertEquals("/home", pattern0.toString());

    // test case #1
    URIResolver resolver1 = new URIResolver("/path/dir/subdir/doc.xml");
    URIPattern pattern1 = resolver1.find(patterns);
    ResolvedVariables result1 = resolver1.resolve(pattern1);
    String doc = (String)result1.get("path");
    assertEquals("dir/subdir/doc.xml", doc);

    // test case #2
    URIResolver resolver2 = new URIResolver("/documents;label=important");
    URIPattern pattern2 = resolver2.find(patterns);
    ResolvedVariables result2 = resolver2.resolve(pattern2);
    String name = (String)result2.get("label");
    assertEquals("important", name);

    // test case #3
    URIResolver resolver3 = new URIResolver("/document/doc.xml");
    URIPattern pattern3 = resolver3.find(patterns);
    ResolvedVariables result3 = resolver3.resolve(pattern3);
    String wildcard = (String)result3.get("*");
    assertEquals("doc.xml", wildcard);

  }

}
