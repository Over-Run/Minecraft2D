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

import java.util.HashMap;
import java.util.List;

/**
 * @author squid233
 * @since 2020/10/15
 */
public class Map<K, V> {
    private final List<K> keys;
    private final List<V> values;

    public static <K, V> Map<K, V> of() {
        return new Map<>();
    }

    public static <K, V> Map<K, V> of(List<K> keys, List<V> values) {
        return new Map<>(keys, values);
    }

    private Map() {
        this(new ObjectArrayList<>(), new ObjectArrayList<>());
    }

    private Map(List<K> keys, List<V> values) {
        this.keys = keys;
        this.values = values;
    }

    public Map<K, V> put(K k, V v) {
        keys.add(k);
        values.add(v);
        return this;
    }

    public java.util.Map<K, V> build() {
        java.util.Map<K, V> m = new HashMap<>(16);
        for (int i = 0; i < keys.size(); i++) {
            m.put(keys.get(i), values.get(i));
        }
        return m;
    }
}
