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

package io.github.overrun.mc2d.world.block;

import io.github.overrun.mc2d.client.model.BlockModelMgr;
import io.github.overrun.mc2d.client.render.Tesselator;
import io.github.overrun.mc2d.world.item.Item;
import io.github.overrun.mc2d.world.item.ItemConvertible;
import io.github.overrun.mc2d.world.item.Items;
import io.github.overrun.mc2d.util.Identifier;
import io.github.overrun.mc2d.util.registry.Registry;
import io.github.overrun.mc2d.util.shape.VoxelShapes;
import io.github.overrun.mc2d.world.World;
import org.jetbrains.annotations.Nullable;
import org.overrun.swgl.core.phys.p2d.AABRect2f;

import java.util.HashMap;
import java.util.Map;

import static io.github.overrun.mc2d.world.block.Blocks.AIR;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 2021/01/09
 */
public class Block implements ItemConvertible {
    public static final Map<Block, Item> BLOCK_ITEMS = new HashMap<>();

    @Nullable
    public AABRect2f getOutlineShape() {
        return getCollisionShape();
    }

    @Nullable
    public AABRect2f getRayCastingShape() {
        return getCollisionShape();
    }

    @Nullable
    public AABRect2f getCollisionShape() {
        return VoxelShapes.fullSquare();
    }

    public final int getRawId() {
        return Registry.BLOCK.getRawId(this);
    }

    public final Identifier getId() {
        return Registry.BLOCK.getId(this);
    }

    public boolean shouldRender(World world, int x, int y, int z) {
        return z == 1 || (world.getBlock(x, y, 1) == AIR);
    }

    public void render(Tesselator t, int x, int y, int z) {
        var path = BlockModelMgr.blockTexture(getTexture());
        var atlas = BlockModelMgr.getBlockAtlas();
        float u0 = atlas.getU0n(path);
        float v0 = atlas.getV0n(path);
        float u1 = atlas.getU1n(path);
        float v1 = atlas.getV1n(path);
        if (z == 0) {
            glColor3f(0.5f, 0.5f, 0.5f);
        } else {
            glColor3f(1.0f, 1.0f, 1.0f);
        }
        glTexCoord2f(u0, v0);
        glVertex2f(x, y + 1);
        glTexCoord2f(u0, v1);
        glVertex2f(x, y);
        glTexCoord2f(u1, v1);
        glVertex2f(x + 1, y);
        glTexCoord2f(u1, v0);
        glVertex2f(x + 1, y + 1);
    }

    public Identifier getTexture() {
        return getId();
    }

    @Override
    public String toString() {
        return getId().toString();
    }

    @Override
    public Item asItem() {
        return BLOCK_ITEMS.getOrDefault(this, Items.AIR);
    }
}
