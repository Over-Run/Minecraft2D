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

package io.github.overrun.mc2d.screen;

import io.github.overrun.mc2d.Main;
import io.github.overrun.mc2d.Player;
import io.github.overrun.mc2d.block.Blocks;
import io.github.overrun.mc2d.util.GlUtils;
import io.github.overrun.mc2d.util.GlfwUtils;
import io.github.overrun.mc2d.util.ImageReader;
import io.github.overrun.mc2d.util.TextureDrawer;

import java.util.ArrayList;
import java.util.List;

import static io.github.overrun.mc2d.util.GlfwUtils.isMousePress;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 2021/01/23
 */
public final class CreativeTabScreen {
    private static final List<Slot> SLOTS = new ArrayList<>(4);
    public static int x;
    public static int y;

    public static void init(int width, int height) {
        x = (width >> 1) - 195;
        y = (height >> 1) + 136;
        SLOTS.clear();
        for (int i = 0; i < 4; i++) {
            Slot slot = new Slot(x + 18 + i * 36, y - 36);
            slot.item = (byte) (i + 1);
            SLOTS.add(slot);
        }
    }

    public static void render(int mouseX, int mouseY, int windowH, Player player) {
        mouseY = windowH - mouseY;
        TextureDrawer.begin(ImageReader.loadTexture("tab_items.png"))
                .color4f(1, 1, 1, 1)
                .tex2dVertex2d(0, 0, x, y)
                .tex2dVertex2d(0, 1, x, y - 272)
                .tex2dVertex2d(1, 1, x + 390, y - 272)
                .tex2dVertex2d(1, 0, x + 390, y)
                .end();
        GlUtils.drawText(x + 16, y, "Creative Tab");
        boolean showHand = false;
        for (Slot slot : SLOTS) {
            TextureDrawer.begin(ImageReader.loadTexture(Blocks.RAW_ID_BLOCKS.get(slot.item).toString() + ".png"))
                    .color4f(1, 1, 1, 1)
                    .tex2dVertex2d(0, 0, slot.x, slot.y)
                    .tex2dVertex2d(0, 1, slot.x, slot.y - 32)
                    .tex2dVertex2d(1, 1, slot.x + 32, slot.y - 32)
                    .tex2dVertex2d(1, 0, slot.x + 32, slot.y)
                    .end();
            if (mouseX >= slot.x
                    && mouseX < slot.x + 32
                    && mouseY <= slot.y
                    && mouseY > slot.y - 32) {
                showHand = true;
                glDisable(GL_TEXTURE_2D);
                GlUtils.fillRect(slot.x, slot.y, slot.x + 32, slot.y - 32, 0x7fffffff, true);
                glEnable(GL_TEXTURE_2D);
                if (isMousePress(GLFW_MOUSE_BUTTON_LEFT)) {
                    player.handledBlock = slot.item;
                    showHand = false;
                    Main.openingGroup = false;
                }
            }
        }
        if (showHand) {
            glfwSetCursor(glfwGetCurrentContext(), GlfwUtils.HAND_CURSOR);
        } else {
            GlfwUtils.setDefaultCursor();
        }
    }
}
