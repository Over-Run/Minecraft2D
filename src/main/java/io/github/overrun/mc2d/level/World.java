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
import io.github.overrun.mc2d.client.Mc2dClient;
import io.github.overrun.mc2d.client.Window;
import io.github.overrun.mc2d.client.gui.Drawable;
import io.github.overrun.mc2d.client.gui.screen.ingame.InGameScreen;
import io.github.overrun.mc2d.event.KeyCallback;
import io.github.overrun.mc2d.mod.ModLoader;
import io.github.overrun.mc2d.text.IText;
import io.github.overrun.mc2d.text.TranslatableText;
import io.github.overrun.mc2d.util.GlUtils;
import io.github.overrun.mc2d.util.Identifier;
import io.github.overrun.mc2d.util.registry.Registry;
import io.github.overrun.mc2d.util.shape.VoxelSet;
import io.github.overrun.mc2d.util.shape.VoxelShape;
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
import static io.github.overrun.mc2d.client.Mouse.isMousePress;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 2021/01/09
 */
public final class World implements Serializable, Drawable {
    private static final Logger logger = LogManager.getLogger(World.class.getName());
    private static final long serialVersionUID = 2L;
    private static final IText MAX_MEMORY;
    public static byte z;
    private static boolean debug;
    private final Identifier[] blocks;
    private final Player player;
    public final transient int width;
    public final transient int height;
    public final transient byte depth;

    static {
        KeyCallback.EVENT.register((window, key, scancode, action, mods) -> {
            if (Mc2dClient.getInstance().screen instanceof InGameScreen) {
                if (action == GLFW_PRESS) {
                    if (key == GLFW_KEY_F3) {
                        debug = !debug;
                    }
                    // 逗号 || 句号
                    if (key == GLFW_KEY_COMMA || key == GLFW_KEY_PERIOD) {
                        --z;
                        if (z < 0) {
                            z = 1;
                        }
                        if (z > 1) {
                            z = 0;
                        }
                    }
                }
            }
        });
        double maxMemory = Runtime.getRuntime().maxMemory() / 1048576.0;
        MAX_MEMORY = new TranslatableText("Max.memory", maxMemory >= 1024 ? maxMemory / 1024.0 + " GB" : maxMemory + " MB");
    }

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

    @Override
    public void render(int mouseX, int mouseY) {
        Mc2dClient client = Mc2dClient.getInstance();
        int windowW = Window.width, windowH = Window.height;
        boolean inGame = client.screen instanceof InGameScreen;
        Block pointBlock = AIR;
        int pointBlockX = 0, pointBlockY = 0, pointBlockZ = 0;
        for (byte z = 1; z > -1; z--) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Block b = getBlock(x, y, z);
                    double ltX = (windowW >> 1) + (x - player.x) * 32,
                            ltY = (windowH >> 1) - (y - player.y) * 32 - 32,
                            rdX = ltX + 32,
                            rdY = ltY + 32;
                    b.x = (int) ltX;
                    b.y = (int) ltY;
                    b.z = z;
                    boolean dark = getBlock(x, y, 0) == AIR;
                    if (rdX >= 0 && rdY >= 0 && ltX < windowW && ltY < windowH) {
                        b.render(z == 0 || dark, dark);
                    }
                    if (inGame
                            && mouseX >= ltX
                            && mouseX < rdX
                            && mouseY >= ltY
                            && mouseY < rdY) {
                        pointBlock = getBlock(x, y, World.z);
                        pointBlockX = x;
                        pointBlockY = y;
                        pointBlockZ = World.z;
                        glDisable(GL_TEXTURE_2D);
                        if (z == 0 || dark) {
                            VoxelShape shape = b.getOutlineShape();
                            for (VoxelSet set : shape.getSets()) {
                                GlUtils.fillRect(ltX + (set.x << 1),
                                        ltY + (set.y << 1),
                                        ltX + (set.x << 1) + 2,
                                        ltY + (set.y << 1) + 2,
                                        0x80ffffff,
                                        true);
                            }
                        }
                        glEnable(GL_TEXTURE_2D);
                        if (pointBlock != AIR && isMousePress(GLFW_MOUSE_BUTTON_LEFT)) {
                            setBlock(x, y, World.z, AIR);
                        } else if (pointBlock == AIR && isMousePress(GLFW_MOUSE_BUTTON_RIGHT)) {
                            setBlock(x, y, World.z, Registry.BLOCK.getByRawId(player.handledBlock));
                        }
                    }
                }
            }
            if (z == 1) {
                player.render(mouseX, mouseY);
            }
        }
        glFinish();
        if (debug) {
            client.textRenderer.draw(0, 0, Main.VERSION_TEXT);
            client.textRenderer.draw(0, 17, new TranslatableText("Pos", player.x, player.y));
            client.textRenderer.draw(0, 34, new TranslatableText("Handled.block", Registry.BLOCK.getByRawId(player.handledBlock)));
            client.textRenderer.draw(0, 51, new TranslatableText("Point.block.pos", pointBlockX, pointBlockY, pointBlockZ));
            client.textRenderer.draw(0, 68, new TranslatableText("Point.block", pointBlock));
            client.textRenderer.draw(0, 85, MAX_MEMORY);
            if (ModLoader.getModCount() > 0) {
                client.textRenderer.draw(0, 102, new TranslatableText("Mods.count", ModLoader.getModCount()));
            }
        }
        if (inGame) {
            player.move();
        }
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
