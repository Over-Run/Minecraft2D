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

package io.github.overrun.mc2d;

import io.github.overrun.mc2d.block.Blocks;
import io.github.overrun.mc2d.level.World;
import io.github.overrun.mc2d.util.ImageReader;
import io.github.overrun.mc2d.util.TextureDrawer;

import java.io.Serializable;

import static io.github.overrun.mc2d.util.GlfwUtils.isKeyPress;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_NEAREST;

/**
 * @author squid233
 * @since 2021/01/09
 */
public final class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    public transient byte handledBlock = 1;
    public double x = .5;
    public double y = 6;
    private transient double headCoord;
    private transient double bodyCoord;

    public void render(int w, int height) {
        int a = (w >> 1) - 16;
        int b = (height >> 1) + 128;
        int c = (height >> 1) + 96;
        double d = headCoord + .5;
        int e = (w >> 1) + 16;
        int f = (w >> 1) - 8;
        int g = (height >> 1) + 48;
        double h = bodyCoord + .5;
        double i = bodyCoord + .75;
        int j = (w >> 1) + 8;
        int k = height >> 1;
        double l = bodyCoord + .25;
        TextureDrawer.begin(ImageReader.loadTexture("char_head.png", GL_NEAREST))
                .tex2dVertex2d(headCoord, 0, a, b)
                .tex2dVertex2d(headCoord, 1, a, c)
                .tex2dVertex2d(d, 1, e, c)
                .tex2dVertex2d(d, 0, e, b)
                .end();
        TextureDrawer.begin(ImageReader.loadTexture("char_body.png", GL_NEAREST))
                .color3f(1, 1, 1)
                .tex2dVertex2d(h, 0, f, c)
                .tex2dVertex2d(h, 1, f, g)
                .tex2dVertex2d(i, 1, j, g)
                .tex2dVertex2d(i, 0, j, c)
                .tex2dVertex2d(bodyCoord, 0, f, g)
                .tex2dVertex2d(bodyCoord, 1, f, k)
                .tex2dVertex2d(l, 1, j, k)
                .tex2dVertex2d(l, 0, j, g)
                .end();
    }

    public void move(long window, World world) {
        if (world.getBlock((int) (x - .5), (int) (y + .125)) == Blocks.AIR
                && (isKeyPress(window, GLFW_KEY_A)
                || isKeyPress(window, GLFW_KEY_LEFT))) {
            x -= x > 0 ? .0625 : 0;
            headCoord = .5;
            bodyCoord = .25;
        }
        if (world.getBlock((int) (x + .5), (int) (y + .125)) == Blocks.AIR
                && (isKeyPress(window, GLFW_KEY_D)
                || isKeyPress(window, GLFW_KEY_RIGHT))) {
            x += x < world.getWidth() ? .0625 : 0;
            headCoord = 0;
            bodyCoord = 0;
        }
        if (onGround(world)
                && world.getBlock((int) x, (int) (y + 1)) == Blocks.AIR
                && (isKeyPress(window, GLFW_KEY_SPACE)
                || isKeyPress(window, GLFW_KEY_W)
                || isKeyPress(window, GLFW_KEY_UP))
        ) {
            for (int i = 8; i > 0; i--) {
                y += y < world.getHeight() ? i * .0625 : 0;
            }
        }
        if (!onGround(world) && y > 0) {
            y -= .0625;
            if ((isKeyPress(window, GLFW_KEY_LEFT_SHIFT)
                    || isKeyPress(window, GLFW_KEY_S)
                    || isKeyPress(window, GLFW_KEY_DOWN))
            ) {
                y -= y > 0 ? .0625 : 0;
            }
        }
    }

    public boolean onGround(World world) {
        return world.getBlock((int) x, (int) (y + .125)) != Blocks.AIR;
    }
}
