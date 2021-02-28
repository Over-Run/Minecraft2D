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

import io.github.overrun.mc2d.client.Mouse;
import io.github.overrun.mc2d.client.TextureManager;
import io.github.overrun.mc2d.client.gui.DrawableHelper;
import io.github.overrun.mc2d.client.gui.screen.Screen;
import io.github.overrun.mc2d.item.BlockItem;
import io.github.overrun.mc2d.item.Item;
import io.github.overrun.mc2d.screen.ScreenHandler;
import io.github.overrun.mc2d.screen.slot.Slot;
import io.github.overrun.mc2d.text.IText;
import io.github.overrun.mc2d.text.TextColor;
import io.github.overrun.mc2d.util.GlUtils;
import io.github.overrun.mc2d.util.Identifier;
import io.github.overrun.mc2d.util.registry.Registry;

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

    protected HandledScreen(T handler, IText title) {
        super(title);
        this.handler = handler;
        titleX = 16;
        titleY = 12;
    }

    protected void drawForeground(int mouseX, int mouseY) {
        textRenderer.draw(x + titleX, y + titleY, title.withColor(new TextColor("", TextColor.WHITE.getBgColor(), 0)));
    }

    /**
     * Draw background likes GUI texture.
     * <h1>How to draw texture</h1>
     * <p>Steps:</p>
     * <ol>
     *     <li>{@link org.lwjgl.opengl.GL11#glColor4f(float, float, float, float) Set color}</li>
     *     <li>{@link TextureManager#bindTexture(Identifier) Bind texture}</li>
     *     <li>{@link DrawableHelper#drawTexture(double, double, int, int, int, int) Draw texture}</li>
     * </ol>
     *
     * @param mouseX Mouse pos x.
     * @param mouseY Mouse pos y.
     */
    protected abstract void drawBackground(int mouseX, int mouseY);

    protected void onClickSlot(Slot slot) { }

    @Override
    protected void init() {
        super.init();
        x = width - (backgroundWidth << 1) >> 1;
        y = height - (backgroundHeight << 1) >> 1;
        handler.slots.clear();
        handler.init(x, y);
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        drawBackground(mouseX, mouseY);
        super.render(mouseX, mouseY, delta);
        for (Slot slot : handler.slots) {
            drawSlot(slot);
            mouseOverSlotEffect(slot, mouseX, mouseY);
        }
        drawForeground(mouseX, mouseY);
    }

    private void mouseOverSlotEffect(Slot slot, int mouseX, int mouseY) {
        if (mouseX >= slot.x
                && mouseX < slot.x + 32
                && mouseY >= slot.y
                && mouseY < slot.y + 32) {
            glDisable(GL_TEXTURE_2D);
            GlUtils.fillRect(slot.x, slot.y, slot.x + 32, slot.y + 32, 0x80ffffff, true);
            glEnable(GL_TEXTURE_2D);
            if (Mouse.isMousePress(GLFW_MOUSE_BUTTON_LEFT)) {
                onClickSlot(slot);
            }
        }
    }

    private void drawSlot(Slot slot) {
        Item item = slot.item;
        glColor3f(1, 1, 1);
        if (item instanceof BlockItem) {
            Identifier id = Registry.BLOCK.getId(((BlockItem) item).getBlock());
            client.getTextureManager().bindTexture(new Identifier(id.getNamespace(), "textures/block/" + id.getPath() + ".png"));
        } else {
            Identifier id = Registry.ITEM.getId(item.asItem());
            client.getTextureManager().bindTexture(new Identifier(id.getNamespace(), "textures/item/" + id.getPath() + ".png"));
        }
        drawTexture(slot.x, slot.y, 32, 32);
    }

    @Override
    public void onClose() {
        super.onClose();
    }
}
