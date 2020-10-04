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

import io.github.overrun.mc2d.block.Block;
import io.github.overrun.mc2d.block.Blocks;
import io.github.overrun.mc2d.util.Identifier;
import io.github.overrun.mc2d.util.collection.IndexedIterable;
import io.github.squid233.serialization.Lifecycle;

import java.util.function.Supplier;

/**
 * @author squid233
 * @since 2020/10/03
 */
public abstract class Registry<T> implements IndexedIterable<T> {
    public static final Identifier ROOT_KEY = new Identifier("root");
    protected static final MutableRegistry<MutableRegistry<?>> ROOT = new SimpleRegistry(createRegistryKey("root"), Lifecycle.experimental())
    public static final RegistryKey<Block> BLOCK_KEY = createRegistryKey("block");
    public static final DefaultedRegistry<Block> BLOCK = create(BLOCK_KEY, "air", () -> Blocks.AIR);

    private final RegistryKey<? extends Registry<T>> registryKey;
    private final Lifecycle lifecycle;

    protected Registry(RegistryKey<? extends Registry<T>> key, Lifecycle lifecycle) {
        this.registryKey = key;
        this.lifecycle = lifecycle;
    }

    private static <T> RegistryKey<T> createRegistryKey(String registryId) {
        return RegistryKey.ofRegistry(new Identifier(registryId));
    }

    private static <T, R extends MutableRegistry<T>> R create(RegistryKey<? extends Registry<T>> registryKey, R registry, Supplier<T> defaultEntry, Lifecycle lifecycle) {
        Identifier identifier = registryKey.getValue();
        DEFAULT_ENTRIES.put(identifier, defaultEntry);
        MutableRegistry<R> mutableRegistry = ROOT;
        return (MutableRegistry)mutableRegistry.add(registryKey, registry, lifecycle);
    }

    private static <T> DefaultedRegistry<T> create(RegistryKey<? extends Registry<T>> registryKey, String defaultId, Lifecycle lifecycle, Supplier<T> defaultEntry) {
        return (DefaultedRegistry)create(registryKey, (MutableRegistry)(new DefaultedRegistry(defaultId, registryKey, lifecycle)), (Supplier)defaultEntry, (Lifecycle)lifecycle);
    }

    private static <T> DefaultedRegistry<T> create(RegistryKey<? extends Registry<T>> registryKey, String defaultId, Supplier<T> defaultEntry) {
        return create(registryKey, defaultId, Lifecycle.experimental(), defaultEntry);
    }

    public RegistryKey<? extends Registry<T>> getKey() {
        return registryKey;
    }

    @Override
    public String toString() {
        return "Registry[" + registryKey + " (" + lifecycle + ")]";
    }
}
