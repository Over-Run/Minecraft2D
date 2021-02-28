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

import io.github.overrun.mc2d.Player;
import io.github.overrun.mc2d.client.gui.screen.Screen;
import io.github.overrun.mc2d.item.BlockItem;
import io.github.overrun.mc2d.screen.CreativeTabScreenHandler;
import io.github.overrun.mc2d.screen.slot.Slot;
import io.github.overrun.mc2d.text.TranslatableText;
import io.github.overrun.mc2d.util.Identifier;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;

/**
 * @author squid233
 * @since 2021/01/23
 */
public final class CreativeTabScreen extends HandledScreen<CreativeTabScreenHandler> {
    private static final Identifier TEXTURE = new Identifier("textures/gui/tab_items.png");
    private final Screen parent;
    private final Player player;

    public CreativeTabScreen(Player player, Screen parent) {
        super(new CreativeTabScreenHandler(), new TranslatableText("Creative.Tab"));
        this.player = player;
        this.parent = parent;
    }

    @Override
    protected void onClickSlot(Slot slot) {
        super.onClickSlot(slot);
        if (slot.item instanceof BlockItem) {
            player.handledBlock = ((BlockItem) slot.item).getBlock();
        }
    }

    @Override
    public void drawBackground(int mouseX, int mouseY) {
        client.getTextureManager().bindTexture(TEXTURE);
        drawTexture(x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        renderBackground();
        super.render(mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int key, int scancode, int mods) {
        if (key == GLFW_KEY_E) {
            onClose();
        }
        return super.keyPressed(key, scancode, mods);
    }

    @Override
    public void onClose() {
        super.onClose();
        client.openScreen(parent);
    }
}
