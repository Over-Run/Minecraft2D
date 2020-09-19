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

package io.github.overrun.mc2d;

import io.github.overrun.mc2d.block.Blocks;
import io.github.overrun.mc2d.client.Mc2dClient;
import io.github.overrun.mc2d.world.Overworld;
import io.github.overrun.mc2d.world.Worlds;
import io.github.overrun.mc2d.world.chunk.Chunk;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * @author squid233
 * @date 2020/9/14
 */
public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        /////
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("saves/save.dat"))) {
            Worlds.OVERWORLD.setStorageBlock(((Overworld) ois.readObject()).getStorageBlock());
        } catch (IOException | ClassNotFoundException e) {
            for (Chunk chunk : Worlds.OVERWORLD.getStorageBlock().chunks) {
                for (int i = 0; i < 16; i++) {
                    chunk.setBlock(i, 0, Blocks.BEDROCK);
                    for (int j = 0; j < 4; j++) {
                        chunk.setBlock(i, j + 1, Blocks.STONE);
                    }
                    for (int j = 0; j < 4; j++) {
                        chunk.setBlock(i, j + 5, Blocks.COBBLESTONE);
                    }
                    for (int j = 0; j < 4; j++) {
                        chunk.setBlock(i, j + 9, Blocks.DIRT);
                    }
                    chunk.setBlock(i, 13, Blocks.GRASS_BLOCK);
                }
            }
        }
        /////
        Mc2dClient.getInstance();
    }
}
