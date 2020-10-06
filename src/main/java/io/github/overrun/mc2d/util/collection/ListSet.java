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

package io.github.overrun.mc2d.util.collection;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author squid233
 * @since 2020/10/03
 */
public class ListSet<E> extends ArrayList<E> {
    private static final long serialVersionUID = 2616596627687114853L;

    public ListSet(int initialCapacity) {
        super(initialCapacity);
    }

    public ListSet() {
        super();
    }

    public ListSet(@NotNull Collection<? extends E> c) {
        super(c);
    }

    @Override
    public E set(int index, E element) {
        if (contains(element)) {
            return element;
        }
        return super.set(index, element);
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) {
            return false;
        }
        return super.add(e);
    }

    @Override
    public void add(int index, E element) {
        if (contains(element)) {
            return;
        }
        super.add(index, element);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        int modCount = 0;
        for (E e : c) {
            if (!contains(e)) {
                add(e);
                modCount++;
            }
        }
        return modCount != 0;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        int modCount = index;
        for (E e : c) {
            if (!contains(e)) {
                add(modCount, e);
                modCount++;
            }
        }
        return modCount != index;
    }
}
