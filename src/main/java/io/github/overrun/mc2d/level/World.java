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
import io.github.overrun.mc2d.util.ImageReader;
import io.github.overrun.mc2d.util.ModelManager;
import io.github.overrun.mc2d.util.WindowUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static io.github.overrun.mc2d.block.Blocks.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 2021/01/09
 */
public final class World {
    private static final Logger logger = LogManager.getLogger();
    private final byte[] blocks;
    private final int width;
    private final int height;

    public World(int w, int h) {
        blocks = new byte[w * h];
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

    public byte getBlock(int x, int y) {
        return blocks[x % width + y * width];
    }

    public void render(int mouseX, int mouseY) {
        int wH = WindowUtils.getWindowSize(Main.getInstance().getWindow()).height;
        mouseY = wH - mouseY;
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                byte b = getBlock(j, i);
                if (b != AIR.getRawId()) {
                    Block block = RAW_ID_BLOCKS.get(b);
                    if (!ModelManager.BLOCK_MODELS.containsKey(block)) {
                        ModelManager.putModel(block);
                    }
                    ImageReader.bindTexture(ModelManager.getModelId(b));
                    int ltX = j * BLOCK_SIZE, ltY = (i + 1) * BLOCK_SIZE,
                            rdX = (j + 1) * BLOCK_SIZE, rdY = i * BLOCK_SIZE;
                    glBegin(GL_QUADS);
                    glColor3f(1, 1, 1);
                    glTexCoord2f(0, 1);
                    glVertex2f(j * BLOCK_SIZE, i * BLOCK_SIZE);
                    glTexCoord2f(1, 1);
                    glVertex2f(rdX, rdY);
                    glTexCoord2f(1, 0);
                    glVertex2f((j + 1) * BLOCK_SIZE, (i + 1) * BLOCK_SIZE);
                    glTexCoord2f(0, 0);
                    glVertex2f(ltX, ltY);
                    glEnd();
                    if (mouseX >= ltX && mouseX < rdX && mouseY <= ltY && mouseY > rdY) {
                        GlUtils.drawRect(ltX, ltY, rdX, rdY, 0, false);
                    }
                }
            }
        }
        glDisable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glFinish();
    }

    public void mousePressed(int button, int mouseX, int mouseY) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int wH = WindowUtils.getWindowSize(Main.getInstance().getWindow()).height;
                int x = mouseX / BLOCK_SIZE;
                int y = (wH - mouseY) / BLOCK_SIZE;
                if (getBlock(x, y) != AIR.getRawId()) {
                    if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                        setBlock(x, y, AIR.getRawId());
                    }
                } else {
                    if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
                        setBlock(x, y, Player.handledBlock);
                    }
                }
            }
        }
    }

    public void load() {
        try (InputStream is = new DataInputStream(
                new GZIPInputStream(
                        new FileInputStream("level.dat")
                )
        )) {
            System.arraycopy(is.readAllBytes(), 0, blocks, 0, blocks.length);
        } catch (IOException e) {
            logger.catching(e);
        }
    }

    public void save() {
        try (OutputStream os = new DataOutputStream(
                new GZIPOutputStream(
                        new FileOutputStream("level.dat")))) {
            os.write(blocks);
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
