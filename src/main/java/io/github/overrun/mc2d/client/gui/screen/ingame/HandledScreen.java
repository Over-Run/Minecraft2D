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

package io.github.overrun.mc2d.client.gui.screen.ingame;

import io.github.overrun.mc2d.block.Blocks;
import io.github.overrun.mc2d.client.gui.DrawableHelper;
import io.github.overrun.mc2d.client.gui.screen.Screen;
import io.github.overrun.mc2d.client.texture.TextureManager;
import io.github.overrun.mc2d.screen.ScreenHandler;
import io.github.overrun.mc2d.screen.slot.Slot;
import io.github.overrun.mc2d.util.GlUtils;
import io.github.overrun.mc2d.util.GlfwUtils;
import io.github.overrun.mc2d.util.Identifier;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 2021/01/25
 */
public abstract class HandledScreen<T extends ScreenHandler> extends Screen {
    protected final T handler;
    protected int backgroundWidth = 195;
    protected int backgroundHeight = 136;
    protected int titleX;
    protected int titleY;
    protected int x;
    protected int y;

    protected HandledScreen(T handler, String title) {
        super(title);
        this.handler = handler;
    }

    protected void drawForeground(int mouseX, int mouseY) {
        textRenderer.draw(titleX, titleY, title);
    }

    /**
     * Draw background likes GUI texture.
     * <h1>How to draw texture</h1>
     * <p>Steps:</p>
     * <ol>
     *     <li>{@link TextureManager#bindTexture(Identifier) Bind texture}</li>
     *     <li>{@link DrawableHelper#drawTexture(int, int, int, int, int, int) Draw texture}</li>
     * </ol>
     *
     * @param mouseX X of mouse pos.
     * @param mouseY Y of mouse pos.
     */
    protected abstract void drawBackground(int mouseX, int mouseY);

    protected void onClickSlot(Slot slot) { }

    @Override
    protected void init() {
        super.init();
        x = width - (backgroundWidth << 1) >> 1;
        y = height - (backgroundHeight << 1) >> 1;
        titleX = x + 16;
        titleY = y + 6;
        handler.slots.clear();
        handler.init();
    }

    @Override
    public void render(int mouseX, int mouseY) {
        drawBackground(mouseX, mouseY);
        super.render(mouseX, mouseY);
        for (Slot slot : handler.slots) {
            drawSlot(slot);
            mouseOverSlotEffect(slot, mouseX, mouseY);
        }
        drawForeground(mouseX, mouseY);
    }

    private void mouseOverSlotEffect(Slot slot, int mouseX, int mouseY) {
        int slotX = x + slot.x, slotY = y + slot.y;
        if (mouseX >= slotX
                && mouseX < slotX + 32
                && mouseY >= slotY
                && mouseY < slotY + 32) {
            glDisable(GL_TEXTURE_2D);
            GlUtils.fillRect(slotX, slotY, slotX + 32, slotY + 32, 0x80ffffff, true);
            glEnable(GL_TEXTURE_2D);
            if (GlfwUtils.isMousePress(GLFW_MOUSE_BUTTON_LEFT)) {
                onClickSlot(slot);
            }
        }
    }

    private void drawSlot(Slot slot) {
        int slotX = x + slot.x, slotY = y + slot.y;
        client.getTextureManager().bindTexture(new Identifier("textures/block/" + Blocks.RAW_ID_BLOCKS.get(slot.item).toString() + ".png"));
        drawTexture(slotX, slotY, 32, 32);
    }

    @Override
    public void onClose() {
        super.onClose();
    }
}
