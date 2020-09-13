package io.github.overrun.mc2d.world;

import io.github.overrun.mc2d.util.Identifier;
import io.github.overrun.mc2d.util.map.StorageBlock;

/**
 * @author squid233
 */
public class Overworld implements IDimension {
    private static StorageBlock storageBlock = StorageBlock.of();

    @Override
    public Identifier getId() {
        return new Identifier("overworld");
    }

    @Override
    public void setStorageBlocks(StorageBlock storageBlock) {
        Overworld.storageBlock = storageBlock;
    }

    @Override
    public StorageBlock getStorageBlocks() {
        return storageBlock;
    }
}
