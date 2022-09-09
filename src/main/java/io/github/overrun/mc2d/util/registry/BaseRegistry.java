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

import java.util.Map.Entry;

/**
 * The base registry.
 *
 * @param <T> the registry entry
 * @author squid233
 * @since 2021/01/27
 */
public abstract class BaseRegistry<T> implements Iterable<Entry<Identifier, T>> {
    /**
     * Gets the identifier of the given entry.
     *
     * @param entry the entry
     * @return the identifier
     */
    public abstract Identifier getId(T entry);

    /**
     * Gets the entry by the given identifier.
     *
     * @param id the identifier
     * @return the entry
     */
    public abstract T getById(Identifier id);

    /**
     * Gets the raw identifier of the given entry.
     *
     * @param entry the entry
     * @return the raw id number
     */
    public abstract int getRawId(T entry);

    /**
     * Gets the entry by the given raw identifier.
     *
     * @param rawId the raw id number
     * @return the entry
     */
    public abstract T getByRawId(int rawId);

    /**
     * Register an entry.
     *
     * @param id    the identifier of the entry
     * @param entry the entry
     * @param <R>   the entry instance type
     * @return the entry
     */
    public abstract <R extends T> R register(Identifier id, R entry);
}
