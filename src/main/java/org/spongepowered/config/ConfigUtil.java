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
package org.spongepowered.config;

import java.util.List;

import org.spongepowered.config.impl.ConfigImplUtil;

/**
 * Contains static utility methods.
 * 
 */
public final class ConfigUtil {
    private ConfigUtil() {

    }

    /**
     * Quotes and escapes a string, as in the JSON specification.
     *
     * @param s
     *            a string
     * @return the string quoted and escaped
     */
    public static String quoteString(String s) {
        return ConfigImplUtil.renderJsonString(s);
    }

    /**
     * Converts a list of keys to a path expression, by quoting the path
     * elements as needed and then joining them separated by a period. A path
     * expression is usable with a {@link Config}, while individual path
     * elements are usable with a {@link ConfigObject}.
     * <p>
     * See the overview documentation for {@link Config} for more detail on path
     * expressions vs. keys.
     * 
     * @param elements
     *            the keys in the path
     * @return a path expression
     * @throws ConfigException
     *             if there are no elements
     */
    public static String joinPath(String... elements) {
        return ConfigImplUtil.joinPath(elements);
    }

    /**
     * Converts a list of strings to a path expression, by quoting the path
     * elements as needed and then joining them separated by a period. A path
     * expression is usable with a {@link Config}, while individual path
     * elements are usable with a {@link ConfigObject}.
     * <p>
     * See the overview documentation for {@link Config} for more detail on path
     * expressions vs. keys.
     * 
     * @param elements
     *            the keys in the path
     * @return a path expression
     * @throws ConfigException
     *             if the list is empty
     */
    public static String joinPath(List<String> elements) {
        return ConfigImplUtil.joinPath(elements);
    }

    /**
     * Converts a path expression into a list of keys, by splitting on period
     * and unquoting the individual path elements. A path expression is usable
     * with a {@link Config}, while individual path elements are usable with a
     * {@link ConfigObject}.
     * <p>
     * See the overview documentation for {@link Config} for more detail on path
     * expressions vs. keys.
     * 
     * @param path
     *            a path expression
     * @return the individual keys in the path
     * @throws ConfigException
     *             if the path expression is invalid
     */
    public static List<String> splitPath(String path) {
        return ConfigImplUtil.splitPath(path);
    }
}
