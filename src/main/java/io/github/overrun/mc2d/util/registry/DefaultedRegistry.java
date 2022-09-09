/*
 * MIT License
 *
 * Copyright (c) 2020-2022 Overrun Organization
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.overrun.mc2d.util.registry;

import io.github.overrun.mc2d.util.Identifier;

import java.util.function.Supplier;

/**
 * The mapped registry with defaulted entry.
 *
 * @param <T> the registry entry
 * @author squid233
 * @since 2021/01/27
 */
public class DefaultedRegistry<T> extends MappedRegistry<T> {
    private final Supplier<T> defaultSupplier;
    private T defaultEntry;

    public DefaultedRegistry(Supplier<T> defaultEntry) {
        defaultSupplier = defaultEntry;
    }

    public T getDefaultEntry() {
        if (defaultEntry == null) {
            defaultEntry = defaultSupplier.get();
        }
        return defaultEntry;
    }

    @Override
    public T getById(Identifier id) {
        T e;
        return ((e = super.getById(id)) != null || id2entry.containsKey(id)) ? e : getDefaultEntry();
    }

    @Override
    public T getByRawId(int rawId) {
        T e;
        return ((e = super.getByRawId(rawId)) != null || entries.containsKey(rawId)) ? e : getDefaultEntry();
    }
}
