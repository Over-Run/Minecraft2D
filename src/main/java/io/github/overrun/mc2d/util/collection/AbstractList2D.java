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

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author squid233
 * @since 2020/10/23
 */
public abstract class AbstractList2D<E> implements List2D<E> {
    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(int y, Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[][] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[][] toArray(T[][] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List2D<E> add(int y, E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List2D<E> add(int y, int x, E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(int y, Collection<?> c) {
        for (Object o : c) {
            if (!contains(y, o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List2D<E> addAll(int y, Collection<? extends E> c) {
        for (E e : c) {
            add(y, e);
        }
        return this;
    }

    @Override
    public List2D<E> addAll(int y, int x, Collection<? extends E> c) {
        for (E e : c) {
            add(y, x, e);
        }
        return this;
    }

    @Override
    public List2D<E> removeAll(int y, Collection<?> c) {
        for (Object o : c) {
            remove(y, o);
        }
        return this;
    }

    @Override
    public List2D<E> retainAll(int y, Collection<?> c) {
        for (Object o : c) {
            if (!contains(y, o)) {
                remove(y, o);
            }
        }
        return this;
    }

    @Override
    public List2D<E> remove(int y, Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List2D<E> set(int x, int y, E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E get(int x, int y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Spliterator<E> spliterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Stream<E> stream() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Stream<E> parallelStream() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        for (E e : c) {
            add(e);
        }
        return true;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        for (Object o : c) {
            remove(o);
        }
        return true;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                remove(o);
            }
        }
        return true;
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
