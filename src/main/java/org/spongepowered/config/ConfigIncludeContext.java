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


/**
 * Context provided to a {@link ConfigIncluder}; this interface is only useful
 * inside a {@code ConfigIncluder} implementation, and is not intended for apps
 * to implement.
 *
 * <p>
 * <em>Do not implement this interface</em>; it should only be implemented by
 * the config library. Arbitrary implementations will not work because the
 * library internals assume a specific concrete implementation. Also, this
 * interface is likely to grow new methods over time, so third-party
 * implementations will break.
 */
public interface ConfigIncludeContext {
    /**
     * Tries to find a name relative to whatever is doing the including, for
     * example in the same directory as the file doing the including. Returns
     * null if it can't meaningfully create a relative name. The returned
     * parseable may not exist; this function is not required to do any IO, just
     * compute what the name would be.
     *
     * The passed-in filename has to be a complete name (with extension), not
     * just a basename. (Include statements in config files are allowed to give
     * just a basename.)
     *
     * @param filename
     *            the name to make relative to the resource doing the including
     * @return parseable item relative to the resource doing the including, or
     *         null
     */
    ConfigParseable relativeTo(String filename);

    /**
     * Parse options to use (if you use another method to get a
     * {@link ConfigParseable} then use {@link ConfigParseable#options()}
     * instead though).
     *
     * @return the parse options
     */
    ConfigParseOptions parseOptions();
}
