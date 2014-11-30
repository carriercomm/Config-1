/*
 * This file is part of Sponge Config, licensed under the MIT License (Apache2).
 *
 * Copyright (C) 2011-2012 Typesafe Inc. <http://typesafe.com>
 * Adaptations Copyright (c) SpongePowered.org <http://www.spongepowered.org>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
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
