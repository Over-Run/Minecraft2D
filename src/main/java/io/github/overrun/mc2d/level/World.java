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
import io.github.overrun.mc2d.option.Options;
import io.github.overrun.mc2d.util.GlUtils;
import io.github.overrun.mc2d.util.ImageReader;
import io.github.overrun.mc2d.util.TextureDrawer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Arrays;

import static io.github.overrun.mc2d.block.Blocks.*;
import static io.github.overrun.mc2d.util.GlfwUtils.isMousePress;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 2021/01/09
 */
public final class World implements Serializable {
    private static final Logger logger = LogManager.getLogger();
    private static final long serialVersionUID = 1L;
    private final byte[] blocks;
    private final Player player;
    public final transient int width;
    public final transient int height;

    public World(Player player, int w, int h) {
        this.player = player;
        blocks = new byte[w * h];
        width = w;
        height = h;
        Arrays.fill(blocks, AIR.getRawId());
        // generate the terrain
        for (int i = 0; i < w; i++) {
            setBlock(i, 0, BEDROCK);
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < w; j++) {
                setBlock(j, i + 1, COBBLESTONE);
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < w; j++) {
                setBlock(j, i + 3, DIRT);
            }
        }
        for (int i = 0; i < w; i++) {
            setBlock(i, 5, GRASS_BLOCK);
        }
        load();
    }

    public void setBlock(int x, int y, byte type) {
        blocks[x % width + y * width] = type;
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

    public void render(int mouseX, int mouseY, int windowW, int windowH) {
        mouseY = windowH - mouseY;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Block b = getBlock(j, i);
                double ltX = ((windowW >> 1) - 1) + (j * BLOCK_SIZE - player.x * BLOCK_SIZE),
                        ltY = ((windowH >> 1) - 1) + ((i + 1) * BLOCK_SIZE - player.y * BLOCK_SIZE),
                        rdX = ((windowW >> 1) - 1) + ((j + 1) * BLOCK_SIZE - player.x * BLOCK_SIZE),
                        rdY = ((windowH >> 1) - 1) + (i * BLOCK_SIZE - player.y * BLOCK_SIZE);
                if (b != AIR) {
                    TextureDrawer.begin(ImageReader.loadTexture(BLOCKS.inverse().get(b) + ".png"))
                            .color4f(1, 1, 1, 1)
                            .tex2dVertex2d(0, 1,
                                    ((windowW >> 1) - 1) + (j * BLOCK_SIZE - player.x * BLOCK_SIZE),
                                    ((windowH >> 1) - 1) + (i * BLOCK_SIZE - player.y * BLOCK_SIZE))
                            .tex2dVertex2d(1, 1, rdX, rdY)
                            .tex2dVertex2d(1, 0,
                                    ((windowW >> 1) - 1) + ((j + 1) * BLOCK_SIZE - player.x * BLOCK_SIZE),
                                    ((windowH >> 1) - 1) + ((i + 1) * BLOCK_SIZE - player.y * BLOCK_SIZE))
                            .tex2dVertex2d(0, 0, ltX, ltY)
                            .end();
                }
                if (!Main.openingGroup
                        && mouseX >= ltX
                        && mouseX < rdX
                        && mouseY <= ltY
                        && mouseY > rdY) {
                    if (
                            Options.getB(Options.BLOCK_HIGHLIGHT,
                                    System.getProperty("mc2d.block.highlight",
                                            "false"))
                    ) {
                        glDisable(GL_TEXTURE_2D);
                        GlUtils.fillRect(ltX, ltY, rdX, rdY, 0x7fffffff, true);
                        glEnable(GL_TEXTURE_2D);
                    }
                    else {
                        GlUtils.drawRect(ltX, ltY, rdX, rdY, 0, false);
                    }
                    if (getBlock(j, i) != AIR
                            && isMousePress(GLFW_MOUSE_BUTTON_LEFT)) {
                        setBlock(j, i, AIR);
                    } else if (getBlock(j, i) == AIR
                            && isMousePress(GLFW_MOUSE_BUTTON_RIGHT)) {
                        setBlock(j, i, player.handledBlock);
                    }
                }
            }
        }
        glFinish();
    }

    public void load() {
        File file = new File("level.dat");
        if (file.exists()) {
            try (InputStream is = new FileInputStream(file);
                 ObjectInputStream ois = new ObjectInputStream(is)) {
                World world = (World) ois.readObject();
                System.arraycopy(world.blocks, 0, blocks, 0, blocks.length);
                player.x = world.player.x;
                player.y = world.player.y;
            } catch (IOException | ClassNotFoundException e) {
                logger.catching(e);
            }
        }
    }

    public void save() {
        try (OutputStream os = new FileOutputStream("level.dat");
             ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(this);
        } catch (IOException e) {
            logger.catching(e);
        }
    }
}
