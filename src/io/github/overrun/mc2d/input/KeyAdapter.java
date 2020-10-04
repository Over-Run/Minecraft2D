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
import io.github.overrun.mc2d.block.Blocks;
import io.github.overrun.mc2d.client.Mc2dClient;
import io.github.overrun.mc2d.game.Camera;
import io.github.overrun.mc2d.screen.Screens;
import io.github.overrun.mc2d.util.MapFilter;
import io.github.overrun.mc2d.world.Worlds;
import io.github.overrun.mc2d.world.chunk.Chunk;

import javax.swing.JOptionPane;
import java.awt.event.KeyEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * @author squid233
 * @since 2020/09/14
 */
public class KeyAdapter extends java.awt.event.KeyAdapter {
    public static final char K_ESC = '\u001B';
    public static final char K_A = 'a';
    public static final char K_D = 'd';
    public static final char K_E = 'e';
    public static final int KEY_COUNT = 300;

    private static final HashMap<Integer, Boolean> KEY_DOWN = new HashMap<>(KEY_COUNT);

    public KeyAdapter() {
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
            Screens.setOpening(Screens.CREATIVE_TAB);
        }
        if (e.getKeyChar() == K_ESC) {
            int opt = JOptionPane.showConfirmDialog(Mc2dClient.getInstance(), "Are you sure want to save?", "Pausing", JOptionPane.YES_NO_CANCEL_OPTION);
            if (opt == JOptionPane.CANCEL_OPTION) {
                return;
            }
            if (opt == JOptionPane.YES_OPTION) {
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("saves/save.dat"))) {
                    for (Chunk c : Worlds.overworld.getStorageBlock().chunks) {
                        c.setBlocks((HashMap<String, AbstractBlock>) MapFilter.filterValue(c.getBlocks(), Blocks.AIR));
                    }
                    oos.writeObject(Worlds.overworld);
                } catch (IOException ee) {
                    ee.printStackTrace();
                }
            }
            System.exit(0);
        }
    }

    public static boolean getKeyDown(int keyCode) {
        return KEY_DOWN.get(keyCode);
    }
}
