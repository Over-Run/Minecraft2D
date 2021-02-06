/*
 * MIT License
 *
 * Copyright (c) 2020-2021 Over-Run
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
import io.github.overrun.mc2d.util.collect.DefaultedList;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author squid233
 * @since 2021/01/27
 */
public class DefaultedRegistry<T> extends MutableRegistry<T> {
    private final Map<T, Identifier> entry2id = new HashMap<>();
    private final Map<Identifier, T> id2entry = new HashMap<>();
    private final Object2IntMap<T> entry2rawId = new Object2IntArrayMap<>();
    private final List<T> entries;
    private final T defaultEntry;

    public DefaultedRegistry(T defaultEntry) {
        this.defaultEntry = defaultEntry;
        entries = new DefaultedList<>(defaultEntry);
    }

    public int size() {
        return entries.size();
    }

    public List<T> entries() {
        return List.copyOf(entries);
    }

    @Override
    public Identifier getId(T entry) {
        return entry2id.get(entry);
    }

    @Override
    public T getById(Identifier id) {
        return id2entry.getOrDefault(id, defaultEntry);
    }

    @Override
    public int getRawId(T entry) {
        return entry2rawId.getInt(entry);
    }

    @Override
    public T getByRawId(int rawId) {
        return entries.get(rawId);
    }

    @Override
    public T register(Identifier id, T entry) {
        return add(id, entry);
    }

    @Override
    public T add(Identifier id, T entry) {
        if (id2entry.containsKey(id)) {
            throw new IllegalArgumentException("Registry entry is present!");
        }
        id2entry.put(id, entry);
        entry2id.put(entry, id);
        entries.add(entry);
        entry2rawId.put(entry, entries.size() - 1);
        return entry;
    }

    @Override
    public void remove(T entry) {
        id2entry.remove(entry2id.get(entry));
        entry2id.remove(entry);
        entries.remove(entry);
        entry2rawId.removeInt(entry);
    }
}
