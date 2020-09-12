package io.github.overrun.mc2d.init;

import io.github.overrun.mc2d.block.Block;
import io.github.overrun.mc2d.block.BlockAir;
import io.github.overrun.mc2d.registry.Registry;
import io.github.overrun.mc2d.registry.RegistryStorage;
import io.github.overrun.mc2d.util.Identifier;

import java.util.ArrayList;

/**
 * @author squid233
 */
public class Blocks {
    private static final ArrayList<Block> BLOCKS = new ArrayList<>();

    private static Block register(int rawId, String name, Block block) {
        BLOCKS.add(rawId, block);
        return Registry.register(Registry.BLOCK, name, block);
    }

    public static final BlockAir AIR;
    public static final Block DIRT;
    public static final Block GRASS_BLOCK;

    static {
        AIR = (BlockAir) register(0, "air", new BlockAir(new Block.Settings()));
        DIRT = register(1, "dirt", new Block(new Block.Settings()));
        GRASS_BLOCK = register(2, "grass_block", new Block(new Block.Settings()));
    }

    public static Block getByRawId(int rawId) {
        return BLOCKS.get(rawId);
    }

    public static Block getById(Identifier id) {
        return RegistryStorage.BLOCKS.get(id);
    }
}
