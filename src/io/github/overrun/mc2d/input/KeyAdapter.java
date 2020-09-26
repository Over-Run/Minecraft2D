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

import io.github.overrun.mc2d.block.Blocks;
import io.github.overrun.mc2d.client.Mc2dClient;
import io.github.overrun.mc2d.game.Camera;
import io.github.overrun.mc2d.util.MapFilter;
import io.github.overrun.mc2d.world.Worlds;
import io.github.overrun.mc2d.world.chunk.Chunk;

import javax.swing.JOptionPane;
import java.awt.event.KeyEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @author squid233
 * @since 2020/09/14
 */
public class KeyAdapter extends java.awt.event.KeyAdapter {
    public static final char K_ESC = '\u001B';
    public static final char K_A = 'a';
    public static final char K_D = 'd';

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == K_A) {
            Camera.reduce();
        }
        if (e.getKeyChar() == K_D) {
            Camera.plus();
        }
        if (e.getKeyChar() == K_ESC) {
            int opt = JOptionPane.showConfirmDialog(Mc2dClient.getInstance(), "Are you sure want to save?", "Pausing", JOptionPane.YES_NO_CANCEL_OPTION);
            if (opt == JOptionPane.CANCEL_OPTION) {
                return;
            }
            if (opt == JOptionPane.YES_OPTION) {
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("saves/save.dat"))) {
                    for (Chunk c : Worlds.overworld.getStorageBlock().chunks) {
                        MapFilter.filterValue(c.blocks, Blocks.AIR);
                    }
                    oos.writeObject(Worlds.overworld);
                } catch (IOException ee) {
                    ee.printStackTrace();
                }
            }
            System.exit(0);
        }
    }
}
