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

import java.io.ObjectStreamException;
import java.io.Serializable;

import org.spongepowered.config.ConfigOrigin;
import org.spongepowered.config.ConfigValueType;

final class ConfigInt extends ConfigNumber implements Serializable {

    private static final long serialVersionUID = 2L;

    final private int value;

    ConfigInt(ConfigOrigin origin, int value, String originalText) {
        super(origin, originalText);
        this.value = value;
    }

    @Override
    public ConfigValueType valueType() {
        return ConfigValueType.NUMBER;
    }

    @Override
    public Integer unwrapped() {
        return value;
    }

    @Override
    String transformToString() {
        String s = super.transformToString();
        if (s == null)
            return Integer.toString(value);
        else
            return s;
    }

    @Override
    protected long longValue() {
        return value;
    }

    @Override
    protected double doubleValue() {
        return value;
    }

    @Override
    protected ConfigInt newCopy(ConfigOrigin origin) {
        return new ConfigInt(origin, value, originalText);
    }

    // serialization all goes through SerializedConfigValue
    private Object writeReplace() throws ObjectStreamException {
        return new SerializedConfigValue(this);
    }
}
