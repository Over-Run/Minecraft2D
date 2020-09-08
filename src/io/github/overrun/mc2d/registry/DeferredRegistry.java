package io.github.overrun.mc2d.registry;

import io.github.overrun.mc2d.util.Identifier;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author squid233
 */
public class DeferredRegistry<T extends IRegistrable> {
    private final Map<Identifier, T> map;

    public DeferredRegistry(Map<Identifier, T> map) {
        this.map = map;
    }

    public T register(Identifier id, Supplier<T> supplier) {
        map.put(id, supplier.get());
        return supplier.get();
    }
}
