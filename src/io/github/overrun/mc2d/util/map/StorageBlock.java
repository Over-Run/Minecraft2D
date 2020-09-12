package io.github.overrun.mc2d.util.map;

import io.github.overrun.mc2d.block.Block;
import io.github.overrun.mc2d.block.BlockPos;
import io.github.overrun.mc2d.init.Blocks;

import java.awt.Graphics;

/**
 * @author squid233
 */
public class StorageBlock {
    public final Block[][] blocks = new Block[28][53];

    private StorageBlock() { }

    public static StorageBlock of() {
        return new StorageBlock();
    }

    public void setBlock(int x, int y, Block target) {
        blocks[y][x] = target;
    }

    public Block[][] getBlocks() {
        return blocks;
    }

    public Block[] getLayer(int y) {
        return getBlocks()[y];
    }

    public Block getBlock(int x, int y) {
        try {
            return getLayer(y)[x];
        } catch (Exception e) {
            return Blocks.AIR;
        }
    }

    public void drawBlock(Graphics g, int x, int y) {
        getBlock(x, y).draw(g, new BlockPos(x, y));
    }

    public void drawLayer(Graphics g, int y) {
        /* eat the exception because the exception prevented execution */
        try {
            for (int i = 0; i < getLayer(y).length; i++) {
                drawBlock(g, i, y);
            }
        } catch (Exception ignored) { }
    }

    public void drawBlocks(Graphics g) {
        for (int i = 0; i < blocks.length; i++) {
            drawLayer(g, i);
        }
    }
}
