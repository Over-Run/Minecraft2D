package io.github.overrun.mc2d.init;

import io.github.overrun.mc2d.block.Block;
import io.github.overrun.mc2d.registry.Registry;

/**
 * @author squid233
 */
public class Blocks {
    private static Block register(String name, Block block) {
        return Registry.register(Registry.BLOCK, name, block);
    }

    public static final Block DIRT = register("dirt", new Block(new Block.Settings()));
}
