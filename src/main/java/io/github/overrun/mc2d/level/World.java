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
import io.github.overrun.mc2d.client.gui.screen.ingame.InGameScreen;
import io.github.overrun.mc2d.event.KeyCallback;
import io.github.overrun.mc2d.option.Options;
import io.github.overrun.mc2d.util.GlUtils;
import io.github.overrun.mc2d.util.Identifier;
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
import static io.github.overrun.mc2d.client.gui.DrawableHelper.drawTexture;
import static io.github.overrun.mc2d.util.GlfwUtils.*;
import static org.lwjgl.glfw.GLFW.*;
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
    private static boolean debug;

    static {
        KeyCallback.EVENT.register(context -> {
            if (context.key == GLFW_KEY_F3 && context.action == GLFW_PRESS) {
                debug = !debug;
            }
        });
    }

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
        if (x >= 0 && x < width && y >= 0 && y < height) {
            blocks[x % width + y * width] = type;
        }
    }

    public void setBlock(int x, int y, Block block) {
        setBlock(x, y, block.getRawId());
    }

    public Block getBlock(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return RAW_ID_BLOCKS.get(blocks[x % width + y * width]);
        } else {
            return AIR;
        }
    }

    public void render(Mc2dClient client, int mouseX, int mouseY, int windowW, int windowH) {
        byte pointBlock = 0;
        int pointBlockX = 0, pointBlockY = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Block b = getBlock(j, i);
                double ltX = (windowW >> 1) - 1 + (j - player.x) * BLOCK_SIZE,
                        ltY = (windowH >> 1) - 1 - (i - player.y) * BLOCK_SIZE,
                        rdX = ltX + BLOCK_SIZE,
                        rdY = ltY + BLOCK_SIZE;
                if (b != AIR && rdX >= 0 && rdY >= 0 && ltX < windowW && ltY < windowH) {
                    client.getTextureManager().bindTexture(new Identifier("textures/block/" + BLOCK2ID.get(b) + ".png"));
                    drawTexture(ltX, ltY, BLOCK_SIZE, BLOCK_SIZE);
                }
                if (client.screen instanceof InGameScreen
                        && mouseX >= ltX
                        && mouseX < rdX
                        && mouseY >= ltY
                        && mouseY < rdY) {
                    pointBlock = b.getRawId();
                    pointBlockX = j;
                    pointBlockY = i;
                    if (
                            Options.getB(Options.BLOCK_HIGHLIGHT,
                                    System.getProperty("mc2d.block.highlight",
                                            "false"))
                    ) {
                        glDisable(GL_TEXTURE_2D);
                        GlUtils.fillRect(ltX, ltY, rdX, rdY, 0x80ffffff, true);
                        glEnable(GL_TEXTURE_2D);
                    } else {
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
        if (debug) {
            client.textRenderer.draw(0, 0, "Minecraft2D " + Main.VERSION);
            client.textRenderer.draw(0, 30, String.format("Pos: %.4f, %.4f", player.x, player.y));
            client.textRenderer.draw(0, 60, "Handled block: " + RAW_ID_BLOCKS.get(player.handledBlock));
            client.textRenderer.draw(0, 90, "Point block pos: " + pointBlockX + ", " + pointBlockY);
            client.textRenderer.draw(0, 120, "Point block: " + RAW_ID_BLOCKS.get(pointBlock));
        }
        glFinish();
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
