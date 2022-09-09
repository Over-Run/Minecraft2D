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

/**
 * The mutable registry.
 *
 * @param <T> the registry entry
 * @author squid233
 * @since 2021/01/27
 */
public abstract class MutableRegistry<T> extends BaseRegistry<T> {
    /**
     * Add an entry.
     *
     * @param id    the identifier of the entry
     * @param entry entry
     * @param <R>   the entry instance type
     * @return the entry
     * @implNote Default implementation used {@link #register(Identifier, T)}
     */
    public abstract <R extends T> R add(Identifier id, R entry);

    /**
     * Set an entry with the given raw identifier and identifier.
     *
     * @param rawId the raw id number
     * @param id    the identifier
     * @param entry the entry
     * @param <R>   the entry instance type
     * @return the entry
     */
    public abstract <R extends T> R set(int rawId, Identifier id, R entry);

    public abstract void remove(T entry);
}
