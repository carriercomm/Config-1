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

import java.net.URL;
import java.util.List;


/**
 * Represents the origin (such as filename and line number) of a
 * {@link ConfigValue} for use in error messages. Obtain the origin of a value
 * with {@link ConfigValue#origin}. Exceptions may have an origin, see
 * {@link ConfigException#origin}, but be careful because
 * <code>ConfigException.origin()</code> may return null.
 *
 * <p>
 * It's best to use this interface only for debugging; its accuracy is
 * "best effort" rather than guaranteed, and a potentially-noticeable amount of
 * memory could probably be saved if origins were not kept around, so in the
 * future there might be some option to discard origins.
 *
 * <p>
 * <em>Do not implement this interface</em>; it should only be implemented by
 * the config library. Arbitrary implementations will not work because the
 * library internals assume a specific concrete implementation. Also, this
 * interface is likely to grow new methods over time, so third-party
 * implementations will break.
 */
public interface ConfigOrigin {
    /**
     * Returns a string describing the origin of a value or exception. This will
     * never return null.
     *
     * @return string describing the origin
     */
    public String description();

    /**
     * Returns a filename describing the origin. This will return null if the
     * origin was not a file.
     *
     * @return filename of the origin or null
     */
    public String filename();

    /**
     * Returns a URL describing the origin. This will return null if the origin
     * has no meaningful URL.
     *
     * @return url of the origin or null
     */
    public URL url();

    /**
     * Returns a classpath resource name describing the origin. This will return
     * null if the origin was not a classpath resource.
     *
     * @return resource name of the origin or null
     */
    public String resource();

    /**
     * Returns a line number where the value or exception originated. This will
     * return -1 if there's no meaningful line number.
     *
     * @return line number or -1 if none is available
     */
    public int lineNumber();

    /**
     * Returns any comments that appeared to "go with" this place in the file.
     * Often an empty list, but never null. The details of this are subject to
     * change, but at the moment comments that are immediately before an array
     * element or object field, with no blank line after the comment, "go with"
     * that element or field.
     * 
     * @return any comments that seemed to "go with" this origin, empty list if
     *         none
     */
    public List<String> comments();
}
