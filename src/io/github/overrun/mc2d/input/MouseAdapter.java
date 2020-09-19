/*
 * MIT License
 *
 * Copyright (c) 2020 Over-Run
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.overrun.mc2d.input;

import io.github.overrun.mc2d.block.AbstractBlock;
import io.github.overrun.mc2d.block.BlockPos;
import io.github.overrun.mc2d.block.Blocks;
import io.github.overrun.mc2d.client.Mc2dClient;
import io.github.overrun.mc2d.game.Player;
import io.github.overrun.mc2d.util.factory.Mc2dFactories;
import io.github.overrun.mc2d.world.Worlds;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Objects;

/**
 * @author squid233
 * @date 2020/9/16
 */
public class MouseAdapter extends java.awt.event.MouseAdapter {
    public static final int W_P_UP = 1;
    public static final int W_P_DOWN = -1;

    @Override
    public void mouseClicked(MouseEvent e) {
        int clickedX = ((e.getX() + 8) >> 4) - 1;
        BlockPos clickedBlockPos = Mc2dFactories.getBlockPos().get(clickedX % 16,
                (Mc2dClient.getInstance().getHeight() - 8 - e.getY() - 1) >> 4);
        AbstractBlock block = Worlds.OVERWORLD.getStorageBlock().getChunk(clickedX >> 4).getBlock(clickedBlockPos);
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (!Objects.equals(block, Blocks.AIR)) {
                Worlds.OVERWORLD.getStorageBlock().getChunk(clickedX >> 4).setBlock(clickedBlockPos, Blocks.AIR);
            }
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (block.equals(Blocks.AIR)) {
                Worlds.OVERWORLD.getStorageBlock().getChunk(clickedX >> 4).setBlock(clickedBlockPos, Blocks.getByRawId(Player.handledBlock));
            }
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() == W_P_UP) {
            Player.plusHandledBlock();
        }
        if (e.getWheelRotation() == W_P_DOWN) {
            Player.reduceHandledBlock();
        }
    }
}
