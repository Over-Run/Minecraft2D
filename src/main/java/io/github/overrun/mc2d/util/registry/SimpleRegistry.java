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

package io.github.overrun.mc2d.util.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Iterators;
import io.github.overrun.mc2d.logger.Logger;
import io.github.overrun.mc2d.util.Identifier;
import io.github.overrun.mc2d.util.Util;
import io.github.overrun.mc2d.util.collection.IndexedIterable;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Objects;

/**
 * @author squid233
 * @since 2020/10/06
 */
public class SimpleRegistry<T> implements IndexedIterable<T> {
    protected static final Logger LOGGER = new Logger();
    private final ObjectList<T> rawId2entry = new ObjectArrayList<>(256);
    private final Object2IntMap<T> entry2rawId = new Object2IntOpenCustomHashMap<>(Util.identityHashStrategy());
    private final BiMap<Identifier, T> id2entry;
    private int nextId;

    public SimpleRegistry() {
        entry2rawId.defaultReturnValue(-1);
        id2entry = HashBiMap.create();
    }

    public T add(Identifier id, T entry) {
        return set(nextId, id, entry);
    }

    public T set(int rawId, Identifier id, T entry) {
        Validate.notNull(entry);
        rawId2entry.size(Math.max(rawId2entry.size(), rawId + 1));
        rawId2entry.set(rawId, entry);
        entry2rawId.put(entry, rawId);
        if (id2entry.containsValue(entry)) {
            LOGGER.error("Adding duplicate value '" + entry + "' to registry");
        }
        this.id2entry.put(id, entry);
        if (nextId <= rawId) {
            nextId = rawId + 1;
        }
        return entry;
    }

    public int size() {
        return getSize();
    }

    public int getSize() {
        return rawId2entry.size();
    }

    public Identifier getId(T entry) {
        return id2entry.inverse().get(entry);
    }

    public T get(Identifier id) {
        return id2entry.get(id);
    }

    @Override
    public int getRawId(T entry) {
        return entry2rawId.getInt(entry);
    }

    @Override
    public @Nullable T get(int index) {
        return index >= 0 && index < rawId2entry.size() ? rawId2entry.get(index) : null;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return Iterators.filter(rawId2entry.iterator(), Objects::nonNull);
    }
}
