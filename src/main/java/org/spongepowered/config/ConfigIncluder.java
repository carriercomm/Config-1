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
 * Implement this interface and provide an instance to
 * {@link ConfigParseOptions#setIncluder ConfigParseOptions.setIncluder()} to
 * customize handling of {@code include} statements in config files. You may
 * also want to implement {@link ConfigIncluderClasspath},
 * {@link ConfigIncluderFile}, and {@link ConfigIncluderURL}, or not.
 */
public interface ConfigIncluder {
    /**
     * Returns a new includer that falls back to the given includer. This is how
     * you can obtain the default includer; it will be provided as a fallback.
     * It's up to your includer to chain to it if you want to. You might want to
     * merge any files found by the fallback includer with any objects you load
     * yourself.
     *
     * It's important to handle the case where you already have the fallback
     * with a "return this", i.e. this method should not create a new object if
     * the fallback is the same one you already have. The same fallback may be
     * added repeatedly.
     *
     * @param fallback
     * @return a new includer
     */
    ConfigIncluder withFallback(ConfigIncluder fallback);

    /**
     * Parses another item to be included. The returned object typically would
     * not have substitutions resolved. You can throw a ConfigException here to
     * abort parsing, or return an empty object, but may not return null.
     * 
     * This method is used for a "heuristic" include statement that does not
     * specify file, URL, or classpath resource. If the include statement does
     * specify, then the same class implementing {@link ConfigIncluder} must
     * also implement {@link ConfigIncluderClasspath},
     * {@link ConfigIncluderFile}, or {@link ConfigIncluderURL} as needed, or a
     * default includer will be used.
     * 
     * @param context
     *            some info about the include context
     * @param what
     *            the include statement's argument
     * @return a non-null ConfigObject
     */
    ConfigObject include(ConfigIncludeContext context, String what);
}
