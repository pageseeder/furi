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
 * A test class for the <code>URICoder</code>.
 *
 * @see <a href="http://tools.ietf.org/html/rfc3986#appendix-A">RFC 3986 - Uniform Resource
 *      Identifier (URI): Generic Syntax - Appendix A. Collected ABNF for URI</a>
 *
 * @author Christophe Lauret
 * @version 19 May 2009
 */
public class URICoderTest extends TestCase {

  /**
   * ALPHA characters as defined in RFC 3986.
   */
  private static final String ALPHA = getURange('a', 'z') + getURange('A', 'Z');

  /**
   * DIGIT characters as defined in RFC 3986.
   */
  private static final String DIGIT = getURange('0', '9');

  /**
   * Unreserved punctuation characters as defined in RFC 3986.
   */
  private static final String PUNC = "-_.~";

  /**
   * Unreserved characters as defined in RFC 3986.
   */
  private static final String UNRESERVED = ALPHA + DIGIT + PUNC;

  /**
   * Test the <code>encode</code> method with an empty string.
   */
  public void testEncode_EmptyString() {
    assertEquals("", URICoder.encode(""));
  }

  /**
   * Test the <code>encode</code> method for all unreserved characters.
   */
  public void testEncode_Unreserved() {
    assertEquals(ALPHA, URICoder.encode(ALPHA));
    assertEquals(DIGIT, URICoder.encode(DIGIT));
    assertEquals(PUNC, URICoder.encode(PUNC));
  }

  /**
   * Test the <code>encode</code> method for all ASCII characters [0x00 to 0x7f].
   */
  public void testEncode_ASCII() {
    for (char c = 0; c < 0x80; c++) {
      String s = String.valueOf(c);
      String enc = URICoder.encode(s);
      if (UNRESERVED.indexOf(c) >= 0) {
        assertEquals(s, enc);
      } else {
        String hex = Integer.toHexString(c);
        if (hex.length() == 1) {
          hex = "0" + hex;
        }
        assertEquals('%' + hex.toUpperCase(), enc);
      }
    }
  }

  /**
   * Test the <code>encode</code> method for some non ASCII characters not involving
   * unicode normalisation.
   */
  public void testEncode_NonASCII() {
    assertEquals("Caf%C3%A9", URICoder.encode("Caf\u00E9"));
    assertEquals("Caf%C3%A9%3F", URICoder.encode("Caf\u00E9?"));
  }

  /**
   * Test the <code>encode</code> method for characters involving unicode normalisation.
   */
  public void testEncode_Normalization() {
    // Greek upsilon with acute and hook symbol (different for NFC, NFD, NFKC, NFKD)
    assertEquals("%CE%8E", URICoder.encode("\u03d3"));
    assertEquals("%CE%8E", URICoder.encode("\u03d2\u0301"));
    // Latin small letter long s with combining dot above
    assertEquals("%E1%B9%A1", URICoder.encode("\u017F\u0307"));
    // Latin small letter s with combining dot above
    assertEquals("%E1%B9%A1", URICoder.encode("\u0073\u0307"));
  }

  /**
   * Test the <code>decode</code> method with an empty string.
   */
  public void testDecode_EmptyString() {
    assertEquals("", URICoder.decode(""));
  }

  /**
   * Test the <code>decode</code> method with a space character (encoded as a + and as %20).
   */
  public void testDecode_Space() {
    assertEquals(" ", URICoder.decode("+"));
    assertEquals(" ", URICoder.decode("%20"));
    assertEquals("Caf\u00E9 $1", URICoder.decode("Caf%C3%A9+$1"));
  }

  /**
   * Test the <code>decode</code> method for all unreserved characters.
   */
  public void testDecode_Unreserved() {
    assertEquals(ALPHA, URICoder.decode(ALPHA));
    assertEquals(DIGIT, URICoder.decode(DIGIT));
    assertEquals(PUNC, URICoder.decode(PUNC));
  }

  /**
   * Test the <code>decode</code> method for all ASCII characters [0x00 to 0x7f].
   */
  public void testDecode_ASCII() {
    for (char c = 0x00; c < 0x80; c++) {
      String s = String.valueOf(c);
      String enc = URICoder.encode(s);
      if (UNRESERVED.indexOf(c) >= 0) {
        assertEquals(s, enc);
      } else {
        assertEquals(s, URICoder.decode(enc));
      }
    }
  }

  /**
   * Test the <code>decode</code> method for some non ASCII characters not involving
   * unicode normalisation.
   */
  public void testDecode_NonASCII() {
    assertEquals("Caf\u00E9", URICoder.decode("Caf%C3%A9"));
    assertEquals("Caf\u00E9?", URICoder.decode("Caf%C3%A9%3F"));
  }

  // Helpers ====================================================================

  /**
   * Generate a string corresponding to a range of Unicode characters.
   *
   * @param from The beginning of the range (inclusive).
   * @param to   The end of the range (inclusive).
   */
  private static String getURange(int from, int to) {
    StringBuffer out = new StringBuffer(to - from);
    for (int i = from; i <= to; i++) {
      out.append((char) i);
    }
    return out.toString();
  }

}
