package io.github.overrun.mc2d.world;

import io.github.overrun.mc2d.block.Block;
import io.github.overrun.mc2d.block.BlockPos;
import io.github.overrun.mc2d.util.Identifier;
import io.github.overrun.mc2d.util.map.StorageBlock;

/**
 * @author squid233
 */
public interface IDimension {
    /**
     * return dimension identifier
     *
     * @return identifier
     */
    Identifier getId();

    /**
     * set storage block
     *
     * @param storageBlock storage blocks
     */
    void setStorageBlocks(StorageBlock storageBlock);

    /**
     * return storage blocks
     *
     * @return storage blocks
     */
    StorageBlock getStorageBlocks();

    /**
     * set block in world
     *
     * @param pos   block pos
     * @param block block
     * @return this
     */
    default IDimension setBlock(BlockPos pos, Block block) {
        getStorageBlocks().setBlock(pos.getX(), pos.getY(), block);
        return this;
    }
}
