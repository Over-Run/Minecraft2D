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
import io.github.overrun.mc2d.util.Identifier;
import io.github.overrun.mc2d.util.registry.Registry;
import io.github.overrun.mc2d.util.shape.VoxelShapes;
import io.github.overrun.mc2d.world.World;
import io.github.overrun.mc2d.world.item.ItemType;
import io.github.overrun.mc2d.world.item.ItemConvertible;
import io.github.overrun.mc2d.world.item.Items;
import org.jetbrains.annotations.Nullable;
import org.overrun.swgl.core.phys.p2d.AABRect2f;

import java.util.HashMap;
import java.util.Map;

/**
 * @author squid233
 * @since 2021/01/09
 */
public class BlockType implements ItemConvertible {
    public static final Map<BlockType, ItemType> BLOCK_ITEMS = new HashMap<>();
    final BlockSettings settings;

    public BlockType(BlockSettings settings) {
        this.settings = settings;
    }

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

    public boolean isAir() {
        return settings.isAir;
    }

    public boolean isTexTransparency() {
        return false;
    }

    public boolean shouldRender(World world, int x, int y, int z) {
        return z == 1 || (world.getBlockStates(x, y, 1).isTexTransparency());
    }

    public void render(Tesselator t, int x, int y, int z) {
        var path = BlockModelMgr.blockTexture(getTexture());
        var atlas = BlockModelMgr.getBlockAtlas();
        float u0 = atlas.getU0n(path);
        float v0 = atlas.getV0n(path);
        float u1 = atlas.getU1n(path);
        float v1 = atlas.getV1n(path);
        if (z == 0) {
            t.color(0.5f, 0.5f, 0.5f);
        } else {
            t.color(1.0f, 1.0f, 1.0f);
        }
        t.tex(u0, v0).vertex(x, y + 1, z);
        t.tex(u0, v1).vertex(x, y, z);
        t.tex(u1, v1).vertex(x + 1, y, z);
        t.tex(u1, v0).vertex(x + 1, y + 1, z);
    }

    public Identifier getTexture() {
        return getId();
    }

    public final int getRawId() {
        return Registry.BLOCK.getRawId(this);
    }

    public final Identifier getId() {
        return Registry.BLOCK.getId(this);
    }

    @Override
    public String toString() {
        return getId().toString();
    }

    @Override
    public ItemType asItem() {
        return BLOCK_ITEMS.getOrDefault(this, Items.AIR);
    }
}
