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
import scala.collection.JavaConverters._
import org.spongepowered.config.ConfigException

class PathTest extends TestUtils {

    @Test
    def pathEquality() {
        // note: foo.bar is a single key here
        val a = Path.newKey("foo.bar")
        // check that newKey worked
        assertEquals(path("foo.bar"), a);
        val sameAsA = Path.newKey("foo.bar")
        val differentKey = Path.newKey("hello")
        // here foo.bar is two elements
        val twoElements = Path.newPath("foo.bar")
        // check that newPath worked
        assertEquals(path("foo", "bar"), twoElements);
        val sameAsTwoElements = Path.newPath("foo.bar")

        checkEqualObjects(a, a)
        checkEqualObjects(a, sameAsA)
        checkNotEqualObjects(a, differentKey)
        checkNotEqualObjects(a, twoElements)
        checkEqualObjects(twoElements, sameAsTwoElements)
    }

    @Test
    def pathToString() {
        assertEquals("Path(foo)", path("foo").toString())
        assertEquals("Path(foo.bar)", path("foo", "bar").toString())
        assertEquals("Path(foo.\"bar*\")", path("foo", "bar*").toString())
        assertEquals("Path(\"foo.bar\")", path("foo.bar").toString())
    }

    @Test
    def pathRender() {
        case class RenderTest(expected: String, path: Path)

        val tests = Seq(
            // simple one-element case
            RenderTest("foo", path("foo")),
            // simple two-element case
            RenderTest("foo.bar", path("foo", "bar")),
            // non-safe-char in an element
            RenderTest("foo.\"bar*\"", path("foo", "bar*")),
            // period in an element
            RenderTest("\"foo.bar\"", path("foo.bar")),
            // hyphen and underscore
            RenderTest("foo-bar", path("foo-bar")),
            RenderTest("foo_bar", path("foo_bar")),
            // starts with hyphen
            RenderTest("\"-foo\"", path("-foo")),
            // starts with number
            RenderTest("\"10foo\"", path("10foo")),
            // empty elements
            RenderTest("\"\".\"\"", path("", "")),
            // internal space
            RenderTest("\"foo bar\"", path("foo bar")),
            // leading and trailing spaces
            RenderTest("\" foo \"", path(" foo ")),
            // trailing space only
            RenderTest("\"foo \"", path("foo ")))
            // numbers with decimal points
            RenderTest("1.2", path("1", "2"))
            RenderTest("1.2.3.4", path("1", "2", "3", "4"))

        for (t <- tests) {
            assertEquals(t.expected, t.path.render())
            assertEquals(t.path, Parser.parsePath(t.expected))
            assertEquals(t.path, Parser.parsePath(t.path.render()))
        }
    }

    @Test
    def pathFromPathList() {
        assertEquals(path("foo"), new Path(List(path("foo")).asJava))
        assertEquals(path("foo", "bar", "baz", "boo"), new Path(List(path("foo", "bar"),
            path("baz", "boo")).asJava))
    }

    @Test
    def pathPrepend() {
        assertEquals(path("foo", "bar"), path("bar").prepend(path("foo")))
        assertEquals(path("a", "b", "c", "d"), path("c", "d").prepend(path("a", "b")))
    }

    @Test
    def pathLength() {
        assertEquals(1, path("foo").length())
        assertEquals(2, path("foo", "bar").length())
    }

    @Test
    def pathParent() {
        assertNull(path("a").parent())
        assertEquals(path("a"), path("a", "b").parent())
        assertEquals(path("a", "b"), path("a", "b", "c").parent())
    }

    @Test
    def pathLast() {
        assertEquals("a", path("a").last())
        assertEquals("b", path("a", "b").last())
    }

    @Test
    def pathsAreInvalid() {
        // this test is just of the Path.newPath() wrapper, the extensive
        // test of different paths is over in ConfParserTest
        intercept[ConfigException.BadPath] {
            Path.newPath("")
        }

        intercept[ConfigException.BadPath] {
            Path.newPath("..")
        }
    }
}
