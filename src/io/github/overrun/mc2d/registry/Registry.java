package io.github.overrun.mc2d.registry;

import io.github.overrun.mc2d.asset.AssetManager;
import io.github.overrun.mc2d.block.Block;
import io.github.overrun.mc2d.util.Identifier;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

/**
 * @author squid233
 */
public class Registry {
    public static final DeferredRegistry<Block> BLOCK = new DeferredRegistry<>(RegistryStorage.BLOCKS);

    public static <T extends IRegistrable> T register(DeferredRegistry<T> registry, Identifier id, T entry) {
        return registry.register(id, () -> {
            if (entry instanceof Block) {
                Properties m = new Properties();
                ((Block) entry).setModel(m);
                try (Reader reader = new FileReader(AssetManager.getAsString(id.getNamespace(), "models", "block", id.getPath() + ".properties"))) {
                    m.load(reader);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ((Block) entry).setRegistryName(id);
            }
            return entry;
        });
    }

    public static <T extends IRegistrable> T register(DeferredRegistry<T> registry, String path, T entry) {
        return register(registry, new Identifier(path), entry);
    }
}
