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

package io.github.overrun.mc2d.registry;

import io.github.overrun.mc2d.util.Identifier;
import org.intellij.lang.annotations.MagicConstant;

import java.util.Hashtable;
import java.util.function.Supplier;

/**
 * @author squid233
 * @since 2020/09/14
 */
public class DeferredRegistry<T extends IRegistrable> {
    private final Hashtable<String, T> registries;

    public DeferredRegistry(@MagicConstant(flagsFromClass = Mc2dRegistries.class) Hashtable<String, T> registries) {
        this.registries = registries;
    }

    public T register(Identifier id, Supplier<T> supplier) {
        T t = supplier.get();
        registries.put(t.setRegistryName(id).getRegistryName().toString(), t);
        return t;
    }
}
