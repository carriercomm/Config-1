/**
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered.org <http://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.config.impl

import org.junit.Assert._
import org.junit._

class UtilTest extends TestUtils {
    private lazy val supplementaryChars = {
        val sb = new java.lang.StringBuilder()
        val codepoints = Seq(
            0x2070E, 0x20731, 0x20779, 0x20C53, 0x20C78,
            0x20C96, 0x20CCF, 0x20CD5, 0x20D15, 0x20D7C)
        for (c <- codepoints) {
            sb.appendCodePoint(c)
        }
        assertTrue(sb.length() > codepoints.length)
        sb.toString()
    }

    @Test
    def unicodeTrimSupplementaryChars() {
        assertEquals("", ConfigImplUtil.unicodeTrim(""))
        assertEquals("a", ConfigImplUtil.unicodeTrim("a"))
        assertEquals("abc", ConfigImplUtil.unicodeTrim("abc"))
        assertEquals("", ConfigImplUtil.unicodeTrim("   \n   \n  \u00A0 "))
        assertEquals(supplementaryChars, ConfigImplUtil.unicodeTrim(supplementaryChars))

        val s = " \u00A0 \n  " + supplementaryChars + "  \n  \u00A0 "
        val asciiTrimmed = s.trim()
        val unitrimmed = ConfigImplUtil.unicodeTrim(s)

        assertFalse(asciiTrimmed.equals(unitrimmed))
        assertEquals(supplementaryChars, unitrimmed)
    }

    @Test
    def definitionOfWhitespace() {
        assertTrue(ConfigImplUtil.isWhitespace(' '))
        assertTrue(ConfigImplUtil.isWhitespace('\n'))
        // these three are nonbreaking spaces
        assertTrue(ConfigImplUtil.isWhitespace('\u00A0'))
        assertTrue(ConfigImplUtil.isWhitespace('\u2007'))
        assertTrue(ConfigImplUtil.isWhitespace('\u202F'))
        // vertical tab, a weird one
        assertTrue(ConfigImplUtil.isWhitespace('\u000B'))
        // file separator, another weird one
        assertTrue(ConfigImplUtil.isWhitespace('\u001C'))
    }

    @Test
    def equalsThatHandlesNull() {
        assertTrue(ConfigImplUtil.equalsHandlingNull(null, null))
        assertFalse(ConfigImplUtil.equalsHandlingNull(new Object(), null))
        assertFalse(ConfigImplUtil.equalsHandlingNull(null, new Object()))
        assertTrue(ConfigImplUtil.equalsHandlingNull("", ""))
    }

    val lotsOfStrings = (invalidJson ++ validConf).map(_.test)

    private def roundtripJson(s: String) {
        val rendered = ConfigImplUtil.renderJsonString(s)
        val parsed = parseConfig("{ foo: " + rendered + "}").getString("foo")
        assertTrue("String round-tripped through maybe-unquoted escaping '" + s + "' " + s.length +
            " rendering '" + rendered + "' " + rendered.length +
            " parsed '" + parsed + "' " + parsed.length,
            s == parsed)
    }

    private def roundtripUnquoted(s: String) {
        val rendered = ConfigImplUtil.renderStringUnquotedIfPossible(s)
        val parsed = parseConfig("{ foo: " + rendered + "}").getString("foo")
        assertTrue("String round-tripped through maybe-unquoted escaping '" + s + "' " + s.length +
            " rendering '" + rendered + "' " + rendered.length +
            " parsed '" + parsed + "' " + parsed.length,
            s == parsed)
    }

    @Test
    def renderJsonString() {
        for (s <- lotsOfStrings) {
            roundtripJson(s)
        }
    }

    @Test
    def renderUnquotedIfPossible() {
        for (s <- lotsOfStrings) {
            roundtripUnquoted(s)
        }
    }
}
