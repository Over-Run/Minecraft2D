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

package io.github.overrun.mc2d.util.collect;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

/**
 * @author squid233
 * @since 2021/01/27
 */
public class DefaultedList<E> extends ArrayList<E> {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Supplier<E> defaultSupplier;
    private E defaultEntry;

    public DefaultedList(Supplier<E> defaultEntry, int initialCapacity) {
        super(initialCapacity);
        defaultSupplier = defaultEntry;
    }

    public DefaultedList(Supplier<E> defaultEntry) {
        defaultSupplier = defaultEntry;
    }

    public DefaultedList(Supplier<E> defaultEntry, @NotNull Collection<? extends E> c) {
        super(c);
        defaultSupplier = defaultEntry;
    }

    @Override
    public E get(int index) {
        return index < 0 || index >= size() ?
            (defaultEntry == null ? defaultEntry = defaultSupplier.get() : defaultEntry) :
            super.get(index);
    }
}
