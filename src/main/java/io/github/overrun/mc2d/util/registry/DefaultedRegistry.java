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

import io.github.overrun.mc2d.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * @author squid233
 * @since 2020/10/06
 */
public class DefaultedRegistry<T> extends SimpleRegistry<T> {
    private final Identifier defaultId;
    private T defaultValue;

    public DefaultedRegistry(String defaultId, T defaultValue) {
        this(defaultId);
        this.defaultValue = defaultValue;
    }

    public DefaultedRegistry(String defaultId) {
        this.defaultId = new Identifier(defaultId);
    }

    public Identifier getDefaultId() {
        return this.defaultId;
    }

    public Optional<T> getOrEmpty(@javax.annotation.Nullable Identifier id) {
        return Optional.ofNullable(super.get(id));
    }

    @Override
    public T set(int rawId, Identifier id, T entry) {
        if (defaultId.equals(id)) {
            defaultValue = entry;
        }
        return super.set(rawId, id, entry);
    }

    @Override
    public Identifier getId(T entry) {
        Identifier id = super.getId(entry);
        return id == null ? defaultId : id;
    }

    @Override
    public T get(Identifier id) {
        T t = super.get(id);
        return t == null ? defaultValue : t;
    }

    @Override
    public int getRawId(T entry) {
        int i = super.getRawId(entry);
        return i == -1 ? super.getRawId(defaultValue) : i;
    }

    @Override
    public @Nullable T get(int index) {
        T t = super.get(index);
        return t == null ? defaultValue : t;
    }
}
