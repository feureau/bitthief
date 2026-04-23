/*
 * BitThief - A Free Riding BitTorrent Client
 * Copyright (C) 2006 Patrick Moor <patrick@moor.ws>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package ws.moor.bt.bencoding;

import ws.moor.bt.util.ExtendedTestCase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

/**
 * TODO(pmoor): Javadoc
 */
public class BStringTest extends ExtendedTestCase {

  private BString a = new BString("a");
  private BString b = new BString("b");

  public void testEquals() {
    BString c = new BString("a");
    assertEquals(a, c);
  }

  public void testNotEquals() {
    assertNotEquals(a, b);
  }

  public void testHashCode() {
    BString c = new BString("a");
    assertEquals(a.hashCode(), c.hashCode());
  }

  public void testEncode() throws IOException {
    assertArrayEquals("1:a".getBytes(), a.encode());
  }

  public void testParseWithLargeLengthPrefix() throws IOException, ParseException {
    String longPrefix = "123456789012345678901234567890:";
    String testData = longPrefix + "test";
    InputStream source = new ByteArrayInputStream(testData.getBytes());
    PushbackInputStream is = new PushbackInputStream(source);
    BString result = BString.parse(is);
    assertEquals("test", result.toString());
  }

  public void testParseWithVeryLongLengthPrefix() throws IOException, ParseException {
    String veryLongPrefix = "999999999999999999999999999999:";
    String testData = veryLongPrefix + "data";
    InputStream source = new ByteArrayInputStream(testData.getBytes());
    PushbackInputStream is = new PushbackInputStream(source);
    try {
      BString.parse(is);
      fail("Should throw NumberFormatException for excessively large length");
    } catch (ParseException e) {
      assertTrue(e.getMessage().contains("could not parse number"));
    }
  }
}
