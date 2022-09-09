/*
 * MIT License
 *
 * Copyright (c) 2022 Overrun Organization
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
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The mutable registry with entry mapping.
 *
 * @param <T> the registry entry
 * @author squid233
 * @since 0.6.0
 */
public class MappedRegistry<T> extends MutableRegistry<T> {
    protected final Map<T, Identifier> entry2id = new LinkedHashMap<>();
    protected final Map<Identifier, T> id2entry = new LinkedHashMap<>();
    protected final Object2IntMap<T> entry2rawId = new Object2IntLinkedOpenHashMap<>();
    protected final Int2ObjectMap<T> entries = new Int2ObjectLinkedOpenHashMap<>();
    protected int nextId = 0;

    public int size() {
        return entries.size();
    }

    @Override
    public Identifier getId(T entry) {
        return entry2id.get(entry);
    }

    @Override
    public T getById(Identifier id) {
        return id2entry.get(id);
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
    public <R extends T> R register(Identifier id, R entry) {
        return add(id, entry);
    }

    @Override
    public <R extends T> R add(Identifier id, R entry) {
        return set(nextId++, id, entry);
    }

    @Override
    public <R extends T> R set(int rawId, Identifier id, R entry) {
        if (id2entry.containsKey(id)) {
            throw new IllegalArgumentException("Registry entry is present!");
        }
        id2entry.put(id, entry);
        entry2id.put(entry, id);
        entries.put(rawId, entry);
        entry2rawId.put(entry, rawId);
        if (rawId > nextId) {
            nextId = rawId + 1;
        }
        return entry;
    }

    @Override
    public void remove(T entry) {
        id2entry.remove(entry2id.get(entry));
        entry2id.remove(entry);
        entries.remove(entry2rawId.getInt(entry));
        entry2rawId.removeInt(entry);
    }

    @NotNull
    @Override
    public Iterator<Map.Entry<Identifier, T>> iterator() {
        return id2entry.entrySet().iterator();
    }
}
