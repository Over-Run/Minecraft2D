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

import io.github.overrun.mc2d.client.Mouse;
import io.github.overrun.mc2d.client.TextureManager;
import io.github.overrun.mc2d.client.gui.DrawableHelper;
import io.github.overrun.mc2d.client.gui.screen.Screen;
import io.github.overrun.mc2d.client.model.BlockModelMgr;
import io.github.overrun.mc2d.screen.ScreenHandler;
import io.github.overrun.mc2d.screen.slot.Slot;
import io.github.overrun.mc2d.text.IText;
import io.github.overrun.mc2d.text.TextColor;
import io.github.overrun.mc2d.util.GlUtils;
import io.github.overrun.mc2d.util.Identifier;
import io.github.overrun.mc2d.world.item.BlockItemType;
import org.overrun.swgl.core.gl.GLStateMgr;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.opengl.GL11.glColor3f;

/**
 * @author squid233
 * @since 2021/01/25
 */
public abstract class HandledScreen<T extends ScreenHandler> extends Screen {
    private static final TextColor TITLE_COLOR = new TextColor(null, TextColor.WHITE.bgColor(), 0);
    protected final T handler;
    protected int backgroundWidth = 195;
    protected int backgroundHeight = 136;
    protected int titleX;
    protected int titleY;
    protected int x;
    protected int y;

    protected HandledScreen(T handler, IText title) {
        super(title.withColor(TITLE_COLOR));
        this.handler = handler;
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

    protected void onClickSlot(Slot slot) {
    }

    @Override
    public void init() {
        super.init();
        x = (width - backgroundWidth) / 2;
        y = (height - backgroundHeight) / 2;
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
            && mouseX < slot.x + 16
            && mouseY >= slot.y
            && mouseY < slot.y + 16) {
            GLStateMgr.disableTexture2D();
            GlUtils.fillRect(slot.x, slot.y, slot.x + 16, slot.y + 16, 0x80ffffff, true);
            GLStateMgr.enableTexture2D();
            if (Mouse.isMousePress(GLFW_MOUSE_BUTTON_LEFT)) {
                onClickSlot(slot);
            }
        }
    }

    private void drawSlot(Slot slot) {
        if (slot.itemStack.isEmpty()) return;
        glColor3f(1, 1, 1);
        // todo: give block and item model
        if (slot.itemStack.getItem().asItem() instanceof BlockItemType block) {
            client.getTextureManager().bindTexture(BlockModelMgr.BLOCK_ATLAS);
            var tex = BlockModelMgr.blockTexture(block.getBlock().getTexture());
            int u0 = BlockModelMgr.getBlockAtlas().getU0(tex);
            int v0 = BlockModelMgr.getBlockAtlas().getV0(tex);
            // todo: atlas::width(tex)
            drawTexture(slot.x, slot.y,
                u0, v0,
                BlockModelMgr.getBlockAtlas().getU1(tex) - u0,
                BlockModelMgr.getBlockAtlas().getV1(tex) - v0,
                16, 16,
                BlockModelMgr.getBlockAtlas().width(),
                BlockModelMgr.getBlockAtlas().height());
        }
    }

    @Override
    public void onClose() {
        super.onClose();
    }
}
