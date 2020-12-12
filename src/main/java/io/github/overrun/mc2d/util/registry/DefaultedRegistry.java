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
import io.github.overrun.mc2d.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * @author squid233
 * @since 2020/10/06
 */
public class DefaultedRegistry<T> extends BaseRegistry<T> {
    private final T defaultValue;
    private final BiMap<Identifier, T> id2entry = HashBiMap.create(255);
    private final BiMap<Integer, T> rawId2entry = HashBiMap.create(255);
    private int nextId;

    public DefaultedRegistry(T def) {
        defaultValue = def;
    }

    @Override
    public T register(Identifier id, T entry) {
        if (nextId < rawId2entry.size())
            nextId = rawId2entry.size();
        rawId2entry.put(nextId++, entry);
        id2entry.put(id, entry);
        return entry;
    }

    @Override
    public T get(Identifier id) {
        return id2entry.get(id);
    }

    public T get(int rawId) {
        return rawId < 0 || rawId >= rawId2entry.size()
                ? defaultValue
                : rawId2entry.get(rawId);
    }

    @Override
    public Identifier getId(T entry) {
        return id2entry.inverse().get(entry);
    }

    public int getRawId(T entry) {
        return rawId2entry.inverse().get(entry);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return rawId2entry.values().iterator();
    }
}
