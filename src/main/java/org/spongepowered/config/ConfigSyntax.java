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
 * The syntax of a character stream (<a href="http://json.org">JSON</a>, <a
 * href="https://github.com/typesafehub/config/blob/master/HOCON.md">HOCON</a>
 * aka ".conf", or <a href=
 * "http://download.oracle.com/javase/7/docs/api/java/util/Properties.html#load%28java.io.Reader%29"
 * >Java properties</a>).
 * 
 */
public enum ConfigSyntax {
    /**
     * Pedantically strict <a href="http://json.org">JSON</a> format; no
     * comments, no unexpected commas, no duplicate keys in the same object.
     * Associated with the <code>.json</code> file extension and
     * <code>application/json</code> Content-Type.
     */
    JSON,
    /**
     * The JSON-superset <a
     * href="https://github.com/typesafehub/config/blob/master/HOCON.md"
     * >HOCON</a> format. Associated with the <code>.conf</code> file extension
     * and <code>application/hocon</code> Content-Type.
     */
    CONF,
    /**
     * Standard <a href=
     * "http://download.oracle.com/javase/7/docs/api/java/util/Properties.html#load%28java.io.Reader%29"
     * >Java properties</a> format. Associated with the <code>.properties</code>
     * file extension and <code>text/x-java-properties</code> Content-Type.
     */
    PROPERTIES;
}
