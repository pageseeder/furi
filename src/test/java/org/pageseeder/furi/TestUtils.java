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

import junit.framework.Assert;

/**
 * Utility classes for tests.
 *
 * @author Christophe Lauret
 * @version 30 December 2008
 */
class TestUtils {

  private TestUtils() {
  }

  /**
   * Indicates whether a class satisfies the basic requirements of the <code>equals</code> method
   * contract.
   *
   * @param x An instance of the class to test.
   * @param y An instance of the class to test equal to the first parameter.
   * @param z An instance of the class to test NOT equal to the first parameter.
   */
  public static void satisfyEqualsContract(Object x, Object y, Object z) {
    // reflexive
    Assert.assertTrue(x.equals(x));
    Assert.assertTrue(y.equals(y));
    Assert.assertTrue(z.equals(z));
    // symmetric
    Assert.assertTrue(x.equals(y));
    Assert.assertTrue(y.equals(x));
    Assert.assertFalse(x.equals(z));
    Assert.assertFalse(z.equals(x));
    // consistent hashcode
    Assert.assertEquals(x.hashCode(), x.hashCode());
    Assert.assertEquals(y.hashCode(), y.hashCode());
    Assert.assertTrue(x.hashCode() != z.hashCode());
    Assert.assertTrue(y.hashCode() != z.hashCode());
    // null is false
    Assert.assertFalse(x.equals(null));
    Assert.assertFalse(z.equals(null));
    // different object is false;
    Assert.assertFalse(x.equals(new Object()));
  }

}
