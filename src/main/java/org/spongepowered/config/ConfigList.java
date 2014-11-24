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

/**
 * Subtype of {@link ConfigValue} representing a list value, as in JSON's
 * {@code [1,2,3]} syntax.
 *
 * <p>
 * {@code ConfigList} implements {@code java.util.List<ConfigValue>} so you can
 * use it like a regular Java list. Or call {@link #unwrapped()} to unwrap the
 * list elements into plain Java values.
 *
 * <p>
 * Like all {@link ConfigValue} subtypes, {@code ConfigList} is immutable. This
 * makes it threadsafe and you never have to create "defensive copies." The
 * mutator methods from {@link java.util.List} all throw
 * {@link java.lang.UnsupportedOperationException}.
 *
 * <p>
 * The {@link ConfigValue#valueType} method on a list returns
 * {@link ConfigValueType#LIST}.
 * 
 * <p>
 * <em>Do not implement {@code ConfigList}</em>; it should only be implemented
 * by the config library. Arbitrary implementations will not work because the
 * library internals assume a specific concrete implementation. Also, this
 * interface is likely to grow new methods over time, so third-party
 * implementations will break.
 * 
 */
public interface ConfigList extends List<ConfigValue>, ConfigValue {

    /**
     * Recursively unwraps the list, returning a list of plain Java values such
     * as Integer or String or whatever is in the list.
     */
    @Override
    List<Object> unwrapped();

}
