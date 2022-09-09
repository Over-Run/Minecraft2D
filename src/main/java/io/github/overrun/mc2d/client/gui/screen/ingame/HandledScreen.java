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

package io.github.overrun.mc2d.client.gui.screen.ingame;

import io.github.overrun.mc2d.client.Mc2dClient;
import io.github.overrun.mc2d.client.TextureManager;
import io.github.overrun.mc2d.client.gui.DrawableHelper;
import io.github.overrun.mc2d.client.gui.screen.Screen;
import io.github.overrun.mc2d.screen.ScreenHandler;
import io.github.overrun.mc2d.screen.inv.PlayerInventory;
import io.github.overrun.mc2d.screen.slot.Slot;
import io.github.overrun.mc2d.text.IText;
import io.github.overrun.mc2d.text.TextColor;
import io.github.overrun.mc2d.util.GlUtils;
import io.github.overrun.mc2d.util.Identifier;
import org.overrun.swgl.core.gl.GLStateMgr;

/**
 * The screen with a screen handler.
 *
 * @param <T> the screen handler type
 * @author squid233
 * @since 2021/01/25
 */
public abstract class HandledScreen<T extends ScreenHandler> extends Screen {
    private static final TextColor TITLE_COLOR = new TextColor(null, TextColor.WHITE.bgColor(), 0);
    protected final T handler;
    protected final PlayerInventory playerInventory;
    protected int backgroundWidth = 195;
    protected int backgroundHeight = 136;
    protected int titleX;
    protected int titleY;
    protected int x;
    protected int y;
    protected Slot hoveredSlot;

    protected HandledScreen(T handler, PlayerInventory playerInventory, IText title) {
        super(title.withColor(TITLE_COLOR));
        this.handler = handler;
        this.playerInventory = playerInventory;
        titleX = 8;
        titleY = 6;
    }

    protected void drawForeground(int mouseX, int mouseY) {
        textRenderer.draw(x + titleX, y + titleY, title);
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

    // TODO: ::(slotId, transferMask, clickType, player)
    protected void onClickSlot(Slot slot, int button) {
    }

    @Override
    public void init(Mc2dClient client, int width, int height) {
        super.init(client, width, height);
        x = (width - backgroundWidth) / 2;
        y = (height - backgroundHeight) / 2;
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        drawBackground(mouseX, mouseY);
        super.render(mouseX, mouseY, delta);
        Slot finalSlot = null;
        for (Slot slot : handler.slots.values()) {
            drawSlot(slot);
            boolean b = mouseOverSlotEffect(slot, mouseX, mouseY);
            if (b) finalSlot = slot;
        }
        hoveredSlot = finalSlot;
        drawForeground(mouseX, mouseY);
    }

    private boolean mouseOverSlotEffect(Slot slot, int mouseX, int mouseY) {
        int fx0 = x + slot.x();
        int fy0 = y + slot.y();
        int fx1 = fx0 + 18;
        int fy1 = fy0 + 18;
        if (mouseX >= fx0
            && mouseX < fx1
            && mouseY >= fy0
            && mouseY < fy1) {
            GLStateMgr.disableTexture2D();
            GlUtils.fillRect(fx0, fy0, fx1, fy1, 0x80ffffff, true);
            GLStateMgr.enableTexture2D();
            return true;
        }
        return false;
    }

    @Override
    public boolean mousePressed(int mouseX, int mouseY, int button) {
        boolean pressed = super.mousePressed(mouseX, mouseY, button);
        if (pressed) return true;
        if (hoveredSlot != null) {
            onClickSlot(hoveredSlot, button);
            return true;
        }
        return false;
    }

    private void drawSlot(Slot slot) {
        client.itemRenderer.renderItemStack(textRenderer, slot.getStack(),
            x + slot.x() + 1, y + slot.y() + 1);
    }

    @Override
    public void onClose() {
        super.onClose();
    }
}
