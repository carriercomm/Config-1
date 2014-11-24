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

import java.io.ObjectStreamException;
import java.io.Serializable;

import org.spongepowered.config.ConfigException;
import org.spongepowered.config.ConfigOrigin;

abstract class ConfigNumber extends AbstractConfigValue implements Serializable {

    private static final long serialVersionUID = 2L;

    // This is so when we concatenate a number into a string (say it appears in
    // a sentence) we always have it exactly as the person typed it into the
    // config file. It's purely cosmetic; equals/hashCode don't consider this
    // for example.
    final protected String originalText;

    protected ConfigNumber(ConfigOrigin origin, String originalText) {
        super(origin);
        this.originalText = originalText;
    }

    @Override
    public abstract Number unwrapped();

    @Override
    String transformToString() {
        return originalText;
    }

    int intValueRangeChecked(String path) {
        long l = longValue();
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new ConfigException.WrongType(origin(), path, "32-bit integer",
                    "out-of-range value " + l);
        }
        return (int) l;
    }

    protected abstract long longValue();

    protected abstract double doubleValue();

    private boolean isWhole() {
        long asLong = longValue();
        return asLong == doubleValue();
    }

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof ConfigNumber;
    }

    @Override
    public boolean equals(Object other) {
        // note that "origin" is deliberately NOT part of equality
        if (other instanceof ConfigNumber && canEqual(other)) {
            ConfigNumber n = (ConfigNumber) other;
            if (isWhole()) {
                return n.isWhole() && this.longValue() == n.longValue();
            } else {
                return (!n.isWhole()) && this.doubleValue() == n.doubleValue();
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        // note that "origin" is deliberately NOT part of equality

        // this matches what standard Long.hashCode and Double.hashCode
        // do, though I don't think it really matters.
        long asLong;
        if (isWhole()) {
            asLong = longValue();
        } else {
            asLong = Double.doubleToLongBits(doubleValue());
        }
        return (int) (asLong ^ (asLong >>> 32));
    }

    static ConfigNumber newNumber(ConfigOrigin origin, long number,
            String originalText) {
        if (number <= Integer.MAX_VALUE && number >= Integer.MIN_VALUE)
            return new ConfigInt(origin, (int) number, originalText);
        else
            return new ConfigLong(origin, number, originalText);
    }

    static ConfigNumber newNumber(ConfigOrigin origin, double number,
            String originalText) {
        long asLong = (long) number;
        if (asLong == number) {
            return newNumber(origin, asLong, originalText);
        } else {
            return new ConfigDouble(origin, number, originalText);
        }
    }

    // serialization all goes through SerializedConfigValue
    private Object writeReplace() throws ObjectStreamException {
        return new SerializedConfigValue(this);
    }
}
