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

import java.util.Collection;

/**
 * @author squid233
 * @since 2020/10/23
 */
public interface List2D<E> extends Collection<E> {
    boolean contains(int y, Object o);

    @Override
    Object[][] toArray();

    <T> T[][] toArray(T[][] a);

    List2D<E> add(int y, E e);

    List2D<E> add(int y, int x, E e);

    boolean containsAll(int y, Collection<?> c);

    List2D<E> addAll(int y, Collection<? extends E> c);

    List2D<E> addAll(int y, int x, Collection<? extends E> c);

    List2D<E> removeAll(int y, Collection<?> c);

    List2D<E> retainAll(int y, Collection<?> c);

    List2D<E> remove(int y, Object o);

    List2D<E> set(int x, int y, E e);

    E get(int x, int y);

    default int getSize() {
        return size();
    }
}
