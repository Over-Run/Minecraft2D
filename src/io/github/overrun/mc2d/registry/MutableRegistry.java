/*
 * MIT License
 *
 * Copyright (c) 2020 Over-Run
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

package io.github.overrun.mc2d.registry;

import io.github.squid233.serialization.Lifecycle;

import java.util.OptionalInt;

/**
 * @author squid233
 * @since 2020/10/03
 */
public abstract class MutableRegistry<T> extends Registry<T> {
    public MutableRegistry(RegistryKey<? extends Registry<T>> registryKey, Lifecycle lifecycle) {
        super(registryKey, lifecycle);
    }

    public abstract <V extends T> V set(int rawId, RegistryKey<T> key, V entry, Lifecycle lifecycle);

    public abstract <V extends T> V add(RegistryKey<T> key, V entry, Lifecycle lifecycle);

    /**
     * If the given key is already present in the registry, replaces the entry associated with the given
     * key with the new entry. This method asserts that the raw ID is equal to the value already in
     * the registry. The raw ID not being present may lead to buggy behavior.
     *
     * <p>If the given key is not already present in the registry, adds the entry to the registry. If
     * {@code rawId} is present, then this method gives the entry this raw ID. Otherwise, uses the
     * next available ID.</p>
     */
    public abstract <V extends T> V replace(OptionalInt rawId, RegistryKey<T> key, V newEntry, Lifecycle lifecycle);
}
