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

import io.github.overrun.mc2d.client.Mc2dClient;
import io.github.overrun.mc2d.game.Camera;
import io.github.overrun.mc2d.screen.Screens;

import javax.swing.JOptionPane;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;

/**
 * @author squid233
 * @since 2020/10/10
 */
public class MultiAdapter implements KeyListener, MouseListener, MouseWheelListener, MouseMotionListener {
    public static final char K_ESC = '\u001B';
    public static final char K_A = 'a';
    public static final char K_D = 'd';
    public static final char K_E = 'e';
    public static final int KEY_COUNT = 300;

    private static final HashMap<Integer, Boolean> KEY_DOWN = new HashMap<>(KEY_COUNT);

    public MultiAdapter() {
        for (int i = 0; i < KEY_COUNT; i++) {
            KEY_DOWN.put(i, false);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        KEY_DOWN.put(e.getKeyCode(), true);
        /////
        if (getKeyDown(KeyEvent.VK_A)) {
            Camera.reduce();
        }
        if (getKeyDown(KeyEvent.VK_D)) {
            Camera.plus();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        KEY_DOWN.put(e.getKeyCode(), false);
        /////
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == K_E) {
            ///Screens.setOpening(Screens.CREATIVE_TAB);
            ///Screens.CREATIVE_TAB.handledStack = ItemStack.EMPTY;
        }
        if (e.getKeyChar() == K_ESC) {
            int opt = JOptionPane.showConfirmDialog(Mc2dClient.getInstance(), "Are you sure want to save?", "Pausing", JOptionPane.YES_NO_CANCEL_OPTION);
            if (opt == JOptionPane.CANCEL_OPTION) {
                return;
            }
            if (opt == JOptionPane.YES_OPTION) {
                /*try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("saves/save.dat"))) {
                    for (Chunk c : Worlds.overworld.getFrontStorageBlock().chunks) {
                        c.setBlocks((HashMap<String, Block>) MapFilter.filterValue(c.getBlocks(), Blocks.AIR));
                    }
                    oos.writeObject(Worlds.overworld);
                } catch (IOException ee) {
                    ee.printStackTrace();
                }*/
            }
            System.exit(0);
        }
    }

    public static boolean getKeyDown(int keyCode) {
        return KEY_DOWN.get(keyCode);
    }


    // Below is mouse


    public static final int W_P_UP = 1;
    public static final int W_P_DOWN = -1;

    @Override
    public void mouseReleased(MouseEvent e) {
        /*if (!Screens.isOpeningAnyScreen) {
            int clickedX = ((e.getX() + 8) >> 4) - 1;
            BlockPos clickedBlockPos = BlockPos.of(clickedX % 16, (Mc2dClient.getInstance().getHeight() - 8 - e.getY() - 1) >> 4);
            Chunk chunk = Worlds.overworld.getFrontStorageBlock().getChunk((clickedX >> 4) + Camera.view);
            Block block = chunk.getBlock(clickedBlockPos);
            if (e.getButton() == MouseEvent.BUTTON1) {
                chunk.setBlock(clickedBlockPos, Blocks.AIR);
            }
            if (e.getButton() == MouseEvent.BUTTON3) {
                if (block.equals(Blocks.AIR)) {
                    chunk.setBlock(clickedBlockPos, Registry.BLOCK.get(Player.handledBlock));
                }
            }
        }*/
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        Screens.getOpening().onMousePressed(e);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        /*if (!Screens.isOpeningAnyScreen) {
            if (e.getWheelRotation() == W_P_UP) {
                Player.plusHandledBlock();
            }
            if (e.getWheelRotation() == W_P_DOWN) {
                Player.reduceHandledBlock();
            }
        }*/
    }

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}
}
