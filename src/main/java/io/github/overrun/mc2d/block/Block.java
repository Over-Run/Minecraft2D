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

package io.github.overrun.mc2d.block;

import io.github.overrun.mc2d.client.Mc2dClient;
import io.github.overrun.mc2d.item.Item;
import io.github.overrun.mc2d.item.ItemConvertible;
import io.github.overrun.mc2d.item.Items;
import io.github.overrun.mc2d.util.GlUtils;
import io.github.overrun.mc2d.util.Identifier;
import io.github.overrun.mc2d.util.registry.Registry;
import io.github.overrun.mc2d.util.shape.VoxelShape;
import io.github.overrun.mc2d.util.shape.VoxelShapes;

import java.util.HashMap;
import java.util.Map;

import static io.github.overrun.mc2d.client.gui.DrawableHelper.drawTexture;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 2021/01/09
 */
public class Block implements ItemConvertible {
    public static final Map<Block, Item> BLOCK_ITEMS = new HashMap<>();

    public VoxelShape getOutlineShape() {
        return getCollisionShape();
    }

    public VoxelShape getCollisionShape() {
        return VoxelShapes.fullSquare();
    }

    public final int getRawId() {
        return Registry.BLOCK.getRawId(this);
    }

    public final Identifier getId() {
        return Registry.BLOCK.getId(this);
    }

    public void render(boolean dark, int x, int y, int z) {
        var id = Registry.BLOCK.getId(this);
        glColor4f(1, 1, 1, 1);
        Mc2dClient.getInstance().getTextureManager().bindTexture(
            new Identifier(id.getNamespace(), "textures/block/" + id.getPath() + ".png"));
        drawTexture(x, y, 32, 32);
        if (dark) {
            glDisable(GL_TEXTURE_2D);
            GlUtils.fillRect(x, y, x + 32, y + 32, 0x80000000, true);
            glEnable(GL_TEXTURE_2D);
        }
    }

    @Override
    public String toString() {
        return Registry.BLOCK.getId(this).toString();
    }

    @Override
    public Item asItem() {
        return BLOCK_ITEMS.getOrDefault(this, Items.AIR);
    }
}
