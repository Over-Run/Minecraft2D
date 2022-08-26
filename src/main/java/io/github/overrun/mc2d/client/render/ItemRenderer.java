/*
 * MIT License
 *
 * Copyright (c) 2022 Overrun Organization
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

package io.github.overrun.mc2d.client.render;

import io.github.overrun.mc2d.client.Mc2dClient;
import io.github.overrun.mc2d.client.TextRenderer;
import io.github.overrun.mc2d.client.model.BlockModelMgr;
import io.github.overrun.mc2d.text.TextColor;
import io.github.overrun.mc2d.world.item.ItemStack;

import static io.github.overrun.mc2d.client.gui.DrawableHelper.drawTexture;
import static org.lwjgl.opengl.GL11.glColor3f;

/**
 * @author squid233
 * @since 0.6.0
 */
public class ItemRenderer {
    private final Mc2dClient client;

    public ItemRenderer(Mc2dClient client) {
        this.client = client;
    }

    public void renderItemStack(TextRenderer textRenderer,
                                ItemStack stack,
                                double x, double y) {
        if (stack.isEmpty()) return;
        glColor3f(1, 1, 1);
        // todo: give block and item model
        client.getTextureManager().bindTexture(BlockModelMgr.BLOCK_ATLAS);
        var tex = BlockModelMgr.blockTexture(stack.getItem().getTexture());
        final var atlas = BlockModelMgr.getBlockAtlas();
        drawTexture(x, y,
            atlas.getU0(tex),
            atlas.getV0(tex),
            atlas.getWidth(tex),
            atlas.getHeight(tex),
            16, 16,
            atlas.width(),
            atlas.height());
        if (stack.getCount() > 1) {
            var text = String.valueOf(stack.getCount());
            textRenderer.draw((int) (x + 16 - textRenderer.drawWidth(text)),
                (int) (y + textRenderer.drawHeight()),
                text,
                TextColor.WHITE.bgColor(),
                TextColor.WHITE.fgColor(),
                true);
        }
    }
}
