package io.github.overrun.mc2d.world;

import io.github.overrun.mc2d.util.Identifier;
import io.github.overrun.mc2d.util.map.StorageBlock;

/**
 * @author squid233
 */
public class Overworld implements IDimension {
    private static final StorageBlock STORAGE_BLOCKS = StorageBlock.of();

    @Override
    public Identifier getId() {
        return new Identifier("overworld");
    }

    @Override
    public StorageBlock getStorageBlocks() {
        return STORAGE_BLOCKS;
    }
}
