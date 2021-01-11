/*
 * MIT License
 *
 * Copyright (c) 2020-2021 Over-Run
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

package io.github.overrun.mc2d.level;

import io.github.overrun.mc2d.Main;
import io.github.overrun.mc2d.Player;
import io.github.overrun.mc2d.block.Block;
import io.github.overrun.mc2d.util.GlUtils;
import io.github.overrun.mc2d.util.ModelManager;
import io.github.overrun.mc2d.util.TextureDrawer;
import io.github.overrun.mc2d.util.WindowUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

import static io.github.overrun.mc2d.block.Blocks.*;
import static io.github.overrun.mc2d.util.GlfwUtils.isMousePress;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
import static org.lwjgl.opengl.GL11.glFinish;

/**
 * @author squid233
 * @since 2021/01/09
 */
public final class World implements Serializable {
    private static final Logger logger = LogManager.getLogger();
    private static final long serialVersionUID = 1L;
    private final byte[] blocks;
    private final Player player;
    private final transient int width;
    private final transient int height;

    public World(Player player, int w, int h) {
        blocks = new byte[w * h];
        this.player = player;
        width = w;
        height = h;
        Arrays.fill(blocks, AIR.getRawId());
        if (new File("level.dat").exists()) {
            load();
        }
    }

    public void setBlock(int x, int y, int type) {
        blocks[x % width + y * width] = (byte) type;
    }

    public void setBlock(int x, int y, Block block) {
        setBlock(x, y, block.getRawId());
    }

    public Block getBlock(int x, int y) {
        try {
            return RAW_ID_BLOCKS.get(blocks[x % width + y * width]);
        } catch (Exception e) {
            return AIR;
        }
    }

    public void render(int mouseX, int mouseY) {
        Dimension wSize = WindowUtils.getWindowSize(Main.getInstance().getWindow());
        mouseY = wSize.height - mouseY;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Block b = getBlock(j, i);
                double ltX = (wSize.width >> 1) + (j * BLOCK_SIZE - player.x * BLOCK_SIZE),
                        ltY = (wSize.height >> 1) + ((i + 1) * BLOCK_SIZE - player.y * BLOCK_SIZE),
                        rdX = (wSize.width >> 1) + ((j + 1) * BLOCK_SIZE - player.x * BLOCK_SIZE),
                        rdY = (wSize.height >> 1) + (i * BLOCK_SIZE - player.y * BLOCK_SIZE);
                if (b != AIR) {
                    if (!ModelManager.BLOCK_MODELS.containsKey(b)) {
                        ModelManager.putModel(b);
                    }
                    TextureDrawer.begin(ModelManager.getModelId(b))
                            .color3f(1, 1, 1)
                            .tex2dVertex2d(0, 1,
                                    (wSize.width >> 1) + (j * BLOCK_SIZE - player.x * BLOCK_SIZE),
                                    (wSize.height >> 1) + (i * BLOCK_SIZE - player.y * BLOCK_SIZE))
                            .tex2dVertex2d(1, 1, rdX, rdY)
                            .tex2dVertex2d(1, 0,
                                    (wSize.width >> 1) + ((j + 1) * BLOCK_SIZE - player.x * BLOCK_SIZE),
                                    (wSize.height >> 1) + ((i + 1) * BLOCK_SIZE - player.y * BLOCK_SIZE))
                            .tex2dVertex2d(0, 0, ltX, ltY)
                            .end();
                }
                if (mouseX >= ltX && mouseX < rdX && mouseY <= ltY && mouseY > rdY) {
                    GlUtils.drawRect((int) ltX, (int) ltY, (int) rdX, (int) rdY, 0, false);
                    long window = Main.getInstance().getWindow();
                    if (getBlock(j, i) != AIR
                            && isMousePress(window, GLFW_MOUSE_BUTTON_LEFT)) {
                        setBlock(j, i, AIR);
                    } else if (getBlock(j, i) == AIR
                            && isMousePress(window, GLFW_MOUSE_BUTTON_RIGHT)) {
                        setBlock(j, i, player.handledBlock);
                    }
                }
            }
        }
        glFinish();
    }

    public void load() {
        try (ObjectInputStream is = new ObjectInputStream(
                new FileInputStream("level.dat")
        )) {
            World world = (World) is.readObject();
            System.arraycopy(world.blocks, 0, blocks, 0, blocks.length);
            player.x = world.player.x;
            player.y = world.player.y;
        } catch (IOException | ClassNotFoundException e) {
            logger.catching(e);
        }
    }

    public void save() {
        try (ObjectOutputStream os = new ObjectOutputStream(
                new FileOutputStream("level.dat"))) {
            os.writeObject(this);
        } catch (IOException e) {
            logger.catching(e);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
