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

import io.github.overrun.mc2d.block.Block;
import io.github.overrun.mc2d.block.Blocks;
import io.github.overrun.mc2d.client.Mc2dClient;
import io.github.overrun.mc2d.client.gui.DrawableHelper;
import io.github.overrun.mc2d.client.gui.Framebuffer;
import io.github.overrun.mc2d.util.Identifier;

import java.io.Serializable;

import static io.github.overrun.mc2d.client.Keyboard.isKeyPress;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 2021/01/09
 */
public final class Player extends DrawableHelper implements Serializable {
    public static final Identifier HEAD_TEXTURE = new Identifier("char_head.png");
    public static final Identifier BODY_TEXTURE = new Identifier("char_body.png");
    private static final long serialVersionUID = 2L;
    public transient Block handledBlock = Blocks.GRASS_BLOCK;
    public transient int headTexCoord;
    public transient int bodyTexCoord;
    public double x = .5;
    public double y = 6;
    public final int z = 0;

    public void tick() {
        if (isKeyPress(GLFW_KEY_A)
                || isKeyPress(GLFW_KEY_LEFT)) {
            x -= .0625;
            headTexCoord = 8;
            bodyTexCoord = 4;
        }
        if (isKeyPress(GLFW_KEY_D)
                || isKeyPress(GLFW_KEY_RIGHT)) {
            x += .0625;
            headTexCoord = 0;
            bodyTexCoord = 0;
        }
        if (isKeyPress(GLFW_KEY_SPACE)
                || isKeyPress(GLFW_KEY_W)
                || isKeyPress(GLFW_KEY_UP)) {
            y += .0625;
        }
        if (isKeyPress(GLFW_KEY_LEFT_SHIFT)
                || isKeyPress(GLFW_KEY_S)
                || isKeyPress(GLFW_KEY_DOWN)
        ) {
            y -= .0625;
        }
    }

    public void render(int mouseX, int mouseY) {
        Mc2dClient client = Mc2dClient.getInstance();
        double x = Framebuffer.width >> 1;
        double y = Framebuffer.height >> 1;
        client.getTextureManager().bindTexture(BODY_TEXTURE);
        glPushMatrix();
        glTranslatef(0, -48, 0);
        drawTexture(x - 8, y, bodyTexCoord, 0, 4, 12, 8, 24, 16, 12);
        glTranslatef(0, -48, 0);
        drawTexture(x - 8, y, bodyTexCoord + 8, 0, 4, 12, 8, 24, 16, 12);
        client.getTextureManager().bindTexture(HEAD_TEXTURE);
        glTranslatef(0, -32, 0);
        drawTexture(x - 16, y, headTexCoord, 0, 8, 8, 16, 16, 16, 8);
        glPopMatrix();
    }
}
