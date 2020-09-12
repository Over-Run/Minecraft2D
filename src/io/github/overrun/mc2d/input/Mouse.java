package io.github.overrun.mc2d.input;

import io.github.overrun.mc2d.Mc2D;
import io.github.overrun.mc2d.block.Block;
import io.github.overrun.mc2d.block.BlockPos;
import io.github.overrun.mc2d.init.Blocks;
import io.github.overrun.mc2d.world.Dimensions;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author squid233
 */
public class Mouse extends MouseAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
        BlockPos clickedBlockPos = new BlockPos((e.getX() + 8 >> 4) - 1, Mc2D.getClient().getHeight() - 8 - e.getY() >> 4);
        if (e.getButton() == MouseEvent.BUTTON1) {
            Block block = Dimensions.OVERWORLD.getStorageBlocks().getBlock(clickedBlockPos);
            if (block != null && !block.equals(Blocks.AIR)) {
                Dimensions.OVERWORLD.setBlock(clickedBlockPos, Blocks.AIR);
            }
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (clickedBlockPos.getY() > 4) {
                Dimensions.OVERWORLD.setBlock(clickedBlockPos, Blocks.COBBLESTONE);
            } else if (clickedBlockPos.getY() < 4) {
                Dimensions.OVERWORLD.setBlock(clickedBlockPos, Blocks.DIRT);
            } else {
                Dimensions.OVERWORLD.setBlock(clickedBlockPos, Blocks.GRASS_BLOCK);
            }
        }
    }
}
