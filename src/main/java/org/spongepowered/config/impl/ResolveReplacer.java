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

import org.spongepowered.config.impl.AbstractConfigValue.NotPossibleToResolve;

/** Callback that generates a replacement to use for resolving a substitution. */
abstract class ResolveReplacer {
    // this is a "lazy val" in essence (we only want to
    // make the replacement one time). Making it volatile
    // is good enough for thread safety as long as this
    // cache is only an optimization and making the replacement
    // twice has no side effects, which it should not...
    private volatile AbstractConfigValue replacement = null;

    final AbstractConfigValue replace(ResolveContext context) throws NotPossibleToResolve {
        if (replacement == null)
            replacement = makeReplacement(context);
        return replacement;
    }

    protected abstract AbstractConfigValue makeReplacement(ResolveContext context)
            throws NotPossibleToResolve;

    static final ResolveReplacer cycleResolveReplacer = new ResolveReplacer() {
        @Override
        protected AbstractConfigValue makeReplacement(ResolveContext context)
                throws NotPossibleToResolve {
            throw new NotPossibleToResolve(context);
        }
    };
}
