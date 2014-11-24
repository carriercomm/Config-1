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

class TokenTest extends TestUtils {

    @Test
    def tokenEquality() {
        // syntax tokens
        checkEqualObjects(Tokens.START, Tokens.START)
        checkNotEqualObjects(Tokens.START, Tokens.OPEN_CURLY)

        // int
        checkEqualObjects(tokenInt(42), tokenInt(42))
        checkNotEqualObjects(tokenInt(42), tokenInt(43))

        // long
        checkEqualObjects(tokenLong(42), tokenLong(42))
        checkNotEqualObjects(tokenLong(42), tokenLong(43))

        // int and long mixed
        checkEqualObjects(tokenInt(42), tokenLong(42))
        checkNotEqualObjects(tokenInt(42), tokenLong(43))

        // boolean
        checkEqualObjects(tokenTrue, tokenTrue)
        checkNotEqualObjects(tokenTrue, tokenFalse)

        // double
        checkEqualObjects(tokenDouble(3.14), tokenDouble(3.14))
        checkNotEqualObjects(tokenDouble(3.14), tokenDouble(4.14))

        // string
        checkEqualObjects(tokenString("foo"), tokenString("foo"))
        checkNotEqualObjects(tokenString("foo"), tokenString("bar"))

        // unquoted
        checkEqualObjects(tokenUnquoted("foo"), tokenUnquoted("foo"))
        checkNotEqualObjects(tokenUnquoted("foo"), tokenUnquoted("bar"))

        // key subst
        checkEqualObjects(tokenKeySubstitution("foo"), tokenKeySubstitution("foo"))
        checkNotEqualObjects(tokenKeySubstitution("foo"), tokenKeySubstitution("bar"))

        // null
        checkEqualObjects(tokenNull, tokenNull)

        // newline
        checkEqualObjects(tokenLine(10), tokenLine(10))
        checkNotEqualObjects(tokenLine(10), tokenLine(11))

        // different types are not equal
        checkNotEqualObjects(tokenTrue, tokenInt(1))
        checkNotEqualObjects(tokenString("true"), tokenTrue)
    }

    @Test
    def tokenToString() {
        // just be sure toString() doesn't throw, it's for debugging
        // so its exact output doesn't matter a lot
        tokenTrue.toString()
        tokenFalse.toString()
        tokenInt(42).toString()
        tokenLong(43).toString()
        tokenDouble(3.14).toString()
        tokenNull.toString()
        tokenUnquoted("foo").toString()
        tokenString("bar").toString()
        tokenKeySubstitution("a").toString()
        tokenLine(10).toString()
        Tokens.START.toString()
        Tokens.END.toString()
        Tokens.COLON.toString()
    }
}
