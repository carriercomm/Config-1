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
import org.spongepowered.config.ConfigRenderOptions;
import org.spongepowered.config.ConfigValueType;

/**
 * This exists because sometimes null is not the same as missing. Specifically,
 * if a value is set to null we can give a better error message (indicating
 * where it was set to null) in case someone asks for the value. Also, null
 * overrides values set "earlier" in the search path, while missing values do
 * not.
 *
 */
final class ConfigNull extends AbstractConfigValue implements Serializable {

    private static final long serialVersionUID = 2L;

    ConfigNull(ConfigOrigin origin) {
        super(origin);
    }

    @Override
    public ConfigValueType valueType() {
        return ConfigValueType.NULL;
    }

    @Override
    public Object unwrapped() {
        return null;
    }

    @Override
    String transformToString() {
        return "null";
    }

    @Override
    protected void render(StringBuilder sb, int indent, boolean atRoot, ConfigRenderOptions options) {
        sb.append("null");
    }

    @Override
    protected ConfigNull newCopy(ConfigOrigin origin) {
        return new ConfigNull(origin);
    }

    // serialization all goes through SerializedConfigValue
    private Object writeReplace() throws ObjectStreamException {
        return new SerializedConfigValue(this);
    }
}
