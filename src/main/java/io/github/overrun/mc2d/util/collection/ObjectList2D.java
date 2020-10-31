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

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * {@link List2D}'s implements.<br>
 * You can re-implements yourself.
 *
 * @author squid233
 * @since 2020/10/23
 */
@ApiStatus.Experimental
public class ObjectList2D<E> extends AbstractList2D<E> implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private final List<Entry<E>> elements;
    private E defEntry;
    private int modCount;
    private int size;

    public ObjectList2D(int initialCapacity) { elements = new ObjectArrayList<>(initialCapacity); }

    /**
     * Construct a 2d list by size 0.
     */
    public ObjectList2D() { this(0); }

    static final class Entry<E> implements Comparable<Entry<E>> {
        private final int x;
        private final int y;
        private final E entry;

        Entry(int x, int y, E entry) {
            this.x = x;
            this.y = y;
            this.entry = entry;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) { return true; }
            if (o == null || getClass() != o.getClass()) { return false; }
            Entry<?> entry1 = (Entry<?>) o;
            return x == entry1.x &&
                    y == entry1.y &&
                    Objects.equals(entry, entry1.entry);
        }

        @Override
        public int hashCode() { return Objects.hash(x, y, entry); }

        @Override
        public int compareTo(@NotNull ObjectList2D.Entry<E> o) {
            if (y > o.y) { return 1; }
            else if (y < o.y) { return -1; }
            else { return Integer.compare(x, o.x); }
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Entry.class.getSimpleName() + "[", "]")
                    .add("x=" + x)
                    .add("y=" + y)
                    .add("entry=" + entry)
                    .toString();
        }

        static class Comparator<E> implements java.util.Comparator<Entry<E>> { @Override public int compare(Entry<E> o1, Entry<E> o2) { return o1.compareTo(o2); }}
    }

    /**
     * Set the default entry when cannot find the entry.<br>
     * Recommend call first.
     *
     * @param defEntry The default entry.
     * @return This class to chain-call method.
     */
    public ObjectList2D<E> defaultEntry(E defEntry) { this.defEntry = defEntry; return this; }

    @Override
    public boolean isEmpty() { return size == 0; }

    /**
     * For example: <br>
<pre><code>Iterator it = list2d.iterator();
while (it.hasNext()) {
    System.out.println(it.next());
}</code></pre>
     *
     * @return The iterator.
     */
    @Override
    public @NotNull Iterator<E> iterator() {
        return new Iterator<>() {
            int nextId = 0;

            @Override
            public boolean hasNext() {
                return elements.size() > nextId && size > nextId;
            }

            @Override
            public E next() {
                nextId++;
                return elements.get(nextId).entry;
            }
        };
    }

    @Override
    public List2D<E> set(int x, int y, E e) {
        for (int i = 0; i < elements.size(); i++) {
            Entry<E> entry = new Entry<>(x, y, e);
            if (elements.get(i).equals(entry)) {
                elements.set(i, entry);
                break;
            }
        }
        modCount++;
        return this;
    }

    @Override
    public List2D<E> add(int y, E e) {
        if (modCount != 0) { size++; }
        elements.add(new Entry<>(size, y, e));
        modCount++;
        return this;
    }

    @Override
    public E get(int x, int y) {
        Entry<E> entry = new Entry<>(x, y, defEntry);
        if (!elements.contains(entry)) { return defEntry; }
        for (Entry<E> e : elements) { if (e.x == x && e.y == y) { entry = e; break; } }
        return entry.entry;
    }

    @Override
    public boolean contains(Object o) { return elements.contains(o); }

    public ObjectList2D<E> sort() { elements.sort(new Entry.Comparator<>()); return this; }

    @Override
    public int size() { return size; }

    @Override
    public String toString() {
        sort();
        StringBuilder sb = new StringBuilder("{\n    ");
        Entry<E> next = elements.get(0);
        for (int i = 0, len = elements.size() - 1; i < len; i++) {
            Entry<E> e = elements.get(i);
            if (e.y > next.y) {
                sb.append(e.entry).append(",\n    ");
            } else {
                sb.append(e.entry).append(", ");
            }
            next = elements.get(i);
        }
        sb.append(elements.get(elements.size() - 1).entry).append("\n}");
        return sb.toString();
    }
}
