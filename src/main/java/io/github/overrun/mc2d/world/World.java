/*
 * MIT License
 *
 * Copyright (c) 2020-2022 Overrun Organization
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

package io.github.overrun.mc2d.world;

import io.github.overrun.mc2d.Player;
import io.github.overrun.mc2d.block.Block;
import io.github.overrun.mc2d.client.gui.Framebuffer;
import io.github.overrun.mc2d.util.GlUtils;
import io.github.overrun.mc2d.util.Identifier;
import io.github.overrun.mc2d.util.registry.Registry;
import io.github.overrun.mc2d.util.shape.VoxelShape;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Arrays;

import static io.github.overrun.mc2d.block.Blocks.*;
import static io.github.overrun.mc2d.client.Mouse.isMousePress;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 2021/01/09
 */
public class World implements Serializable {
    private static final Logger logger = LogManager.getLogger(World.class.getName());
    private static final long serialVersionUID = 3L;
    public static byte z;
    private final Identifier[] blocks;
    private final Player player;
    public final transient int width;
    public final transient int height;
    public final transient byte depth;

    public World(Player player, int w, int h) {
        this.player = player;
        width = w;
        height = h;
        depth = 2;
        blocks = new Identifier[w * h * depth];
        Arrays.fill(blocks, AIR.getId());
        // generate the terrain
        for (int i = 0; i < w; i++) {
            for (byte j = 0; j < 2; j++) {
                setBlock(i, 0, j, BEDROCK);
                setBlock(i, 0, j, BEDROCK);
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < w; j++) {
                for (byte k = 0; k < 2; k++) {
                    setBlock(j, i + 1, k, COBBLESTONE);
                    setBlock(j, i + 1, k, COBBLESTONE);
                }
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < w; j++) {
                for (byte k = 0; k < 2; k++) {
                    setBlock(j, i + 3, k, DIRT);
                    setBlock(j, i + 3, k, DIRT);
                }
            }
        }
        for (int i = 0; i < w; i++) {
            for (byte j = 0; j < 2; j++) {
                setBlock(i, 5, j, GRASS_BLOCK);
                setBlock(i, 5, j, GRASS_BLOCK);
            }
        }
        load();
    }

    public void setBlock(int x, int y, int z, Block block) {
        if (x >= 0 && x < width && y >= 0 && y < height && z >= 0 && z < depth) {
            blocks[getIndex(x, y, (byte) z)] = block.getId();
        }
    }

    public Block getBlock(int x, int y, int z) {
        if (x >= 0 && x < width && y >= 0 && y < height && z >= 0 && z < depth) {
            return Registry.BLOCK.getById(blocks[getIndex(x, y, (byte) z)]);
        } else {
            return AIR;
        }
    }

    public int getIndex(int x, int y, byte z) {
        return x % width + y * width + z * width * height;
    }

    public void render(int z, int mouseX, int mouseY) {
        Block target;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Block b = getBlock(x, y, z);
                double ltX = (Framebuffer.width >> 1) + (x - player.x) * 32,
                        ltY = (Framebuffer.height >> 1) - (y - player.y) * 32 - 32,
                        rdX = ltX + 32,
                        rdY = ltY + 32;
                b.x = (int) ltX;
                b.y = (int) ltY;
                b.z = (byte) z;
                boolean dark = getBlock(x, y, 0) == AIR;
                b.render(z == 0 || dark, dark);
                if (mouseX >= ltX
                        && mouseX < rdX
                        && mouseY >= ltY
                        && mouseY < rdY) {
                    target = getBlock(x, y, World.z);
                    glDisable(GL_TEXTURE_2D);
                    if (z == 0 || dark) {
                        VoxelShape shape = b.getOutlineShape();
                        if (shape.x0 > -1 && shape.y0 > -1 && shape.x1 > -1 && shape.y1 > -1) {
                            GlUtils.drawRect(ltX + shape.x0,
                                    ltY + shape.y0,
                                    ltX + (shape.x1 << 1),
                                    ltY + (shape.y1 << 1),
                                    0x80000000,
                                    true);
                        }
                    }
                    glEnable(GL_TEXTURE_2D);
                    if (target == AIR) {
                        if (isMousePress(GLFW_MOUSE_BUTTON_RIGHT)) {
                            setBlock(x, y, World.z, player.handledBlock);
                        }
                    } else if (isMousePress(GLFW_MOUSE_BUTTON_LEFT)) {
                        setBlock(x, y, World.z, AIR);
                    }
                }
            }
        }
    }

    public void render(int mouseX, int mouseY) {
        render(1, mouseX, mouseY);
        player.render(mouseX, mouseY);
        render(0, mouseX, mouseY);
    }

    public void load() {
        logger.info("Loading world");
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
        logger.info("Saving world");
        try (OutputStream os = new FileOutputStream("level.dat");
             ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(this);
        } catch (IOException e) {
            logger.catching(e);
        }
    }
}
