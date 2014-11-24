/*
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
package org.spongepowered.config.impl;

import java.util.Stack;

import org.spongepowered.config.ConfigException;

final class PathBuilder {
    // the keys are kept "backward" (top of stack is end of path)
    final private Stack<String> keys;
    private Path result;

    PathBuilder() {
        keys = new Stack<String>();
    }

    private void checkCanAppend() {
        if (result != null)
            throw new ConfigException.BugOrBroken(
                    "Adding to PathBuilder after getting result");
    }

    void appendKey(String key) {
        checkCanAppend();

        keys.push(key);
    }

    void appendPath(Path path) {
        checkCanAppend();

        String first = path.first();
        Path remainder = path.remainder();
        while (true) {
            keys.push(first);
            if (remainder != null) {
                first = remainder.first();
                remainder = remainder.remainder();
            } else {
                break;
            }
        }
    }

    Path result() {
        // note: if keys is empty, we want to return null, which is a valid
        // empty path
        if (result == null) {
            Path remainder = null;
            while (!keys.isEmpty()) {
                String key = keys.pop();
                remainder = new Path(key, remainder);
            }
            result = remainder;
        }
        return result;
    }
}
