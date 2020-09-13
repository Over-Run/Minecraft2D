package io.github.overrun.mc2d.input;

import io.github.overrun.mc2d.Mc2D;
import io.github.overrun.mc2d.block.Block;
import io.github.overrun.mc2d.block.BlockPos;
import io.github.overrun.mc2d.entity.Player;
import io.github.overrun.mc2d.init.Blocks;
import io.github.overrun.mc2d.world.Dimensions;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * @author squid233
 */
public class Mouse extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
        BlockPos clickedBlockPos = new BlockPos((e.getX() + 8 >> 4) - 1, Mc2D.getClient().getHeight() - 8 - e.getY() - 1 >> 4);
        Block block = Dimensions.OVERWORLD.getStorageBlocks().getBlock(clickedBlockPos);
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (block != null && !block.equals(Blocks.AIR)) {
                Dimensions.OVERWORLD.setBlock(clickedBlockPos, Blocks.AIR);
            }
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (block != null && block.equals(Blocks.AIR)) {
                Dimensions.OVERWORLD.setBlock(clickedBlockPos, Blocks.getByRawId(Player.handledBlock));
            }
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() == WheelRotation.SCROLL_UP) {
            Player.handledBlock++;
        } else if (e.getWheelRotation() == WheelRotation.SCROLL_DOWN) {
            Player.handledBlock--;
        }
        if (Player.handledBlock >= Blocks.size()) {
            Player.handledBlock = 1;
        } else if (Player.handledBlock < 1) {
            Player.handledBlock = Blocks.size() - 1;
        }
    }

    public interface WheelRotation {
        int SCROLL_UP = 1;
        int SCROLL_DOWN = -1;
    }
}
