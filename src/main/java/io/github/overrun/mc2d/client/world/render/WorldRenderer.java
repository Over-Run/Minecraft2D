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

package io.github.overrun.mc2d.client.world.render;

import io.github.overrun.mc2d.client.Mc2dClient;
import io.github.overrun.mc2d.client.model.BlockModelMgr;
import io.github.overrun.mc2d.client.world.ClientChunk;
import io.github.overrun.mc2d.util.GlUtils;
import io.github.overrun.mc2d.world.HitResult;
import io.github.overrun.mc2d.world.IWorldListener;
import io.github.overrun.mc2d.world.World;
import io.github.overrun.mc2d.world.block.BlockType;
import io.github.overrun.mc2d.world.block.Blocks;
import io.github.overrun.mc2d.world.entity.HumanEntity;
import io.github.overrun.mc2d.world.item.BlockItemType;
import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import org.joml.Intersectiond;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import static io.github.overrun.mc2d.world.Chunk.CHUNK_SIZE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.overrun.swgl.core.gl.GLStateMgr.*;

/**
 * @author squid233
 * @since 0.6.0
 */
public class WorldRenderer implements IWorldListener, AutoCloseable {
    public static final int MAX_REBUILDS_PER_FRAME = 8;
    private final Mc2dClient client;
    private final World world;
    public final HitResult hitResult = new HitResult(null, 0, 0, 0, true);
    private final int xChunks, yChunks;
    private final Long2ObjectMap<ClientChunk> chunkMap = new Long2ObjectArrayMap<>();
    private final List<ClientChunk> dirtyChunks = new ArrayList<>();

    public WorldRenderer(Mc2dClient client, World world) {
        this.client = client;
        this.world = world;
        xChunks = world.width / CHUNK_SIZE;
        yChunks = world.height / CHUNK_SIZE;
        for (int y = 0; y < yChunks; y++) {
            for (int x = 0; x < xChunks; x++) {
                getOrCreateChunk(x, y);
            }
        }
    }

    public ClientChunk getOrCreateChunk(int x, int y) {
        return chunkMap.computeIfAbsent(ClientChunk.pos2Long(x, y),
            pos -> {
                int x0 = x * CHUNK_SIZE;
                int y0 = y * CHUNK_SIZE;
                int x1 = (x + 1) * CHUNK_SIZE;
                int y1 = (y + 1) * CHUNK_SIZE;

                if (x1 > world.width) {
                    x1 = world.width;
                }
                if (y1 > world.height) {
                    y1 = world.height;
                }

                return new ClientChunk(world,
                    x0,
                    y0,
                    x1,
                    y1);
            });
    }

    public void forVisibleChunk(Consumer<ClientChunk> consumer) {
        final double inv32 = 1. / 32.;
        int rx = (int) Math.ceil(client.window.getWidth() * .5 * inv32);
        int ry = (int) Math.ceil(client.window.getHeight() * .5 * inv32);
        int ox = (int) Math.floor(client.player.lerpPos.x);
        int oy = (int) Math.floor(client.player.lerpPos.y);
        for (int y = Math.max(world.getMinY(), oy - ry) / CHUNK_SIZE, my = Math.min(world.getMaxY(), oy + ry) / CHUNK_SIZE;
             y <= my; y++) {
            for (int x = Math.max(world.getMinX(), ox - rx) / CHUNK_SIZE, mx = Math.min(world.getMaxX(), ox + rx) / CHUNK_SIZE;
                 x <= mx; x++) {
                consumer.accept(getOrCreateChunk(x, y));
            }
        }
    }

    private void getDirtyChunks() {
        forVisibleChunk(c -> {
            if (!dirtyChunks.contains(c) && c.isDirty()) {
                dirtyChunks.add(c);
            }
        });
    }

    public void updateDirtyChunks() {
        getDirtyChunks();
        if (dirtyChunks.size() == 0) {
            return;
        }
        dirtyChunks.sort(Comparator.comparingDouble(value -> Math.abs(value.distanceSqr(client.player))));
        for (int i = 0; i < dirtyChunks.size() && i < MAX_REBUILDS_PER_FRAME; i++) {
            dirtyChunks.get(i).rebuild();
            dirtyChunks.remove(i--);
        }
    }

    public void render(int z) {
        client.getTextureManager().bindTexture(BlockModelMgr.BLOCK_ATLAS);
        forVisibleChunk(c -> c.render(z));
        client.getTextureManager().bindTexture(0);
    }

    public void pick(int mouseX, int mouseY) {
        BlockType target = null;
        int targetX = 0, targetY = 0;
        final double inv32 = 1. / 32.;
        int rx = (int) Math.ceil(client.window.getWidth() * .5 * inv32);
        int ry = (int) Math.ceil(client.window.getHeight() * .5 * inv32);
        int ox = (int) Math.floor(client.player.lerpPos.x);
        int oy = (int) Math.floor(client.player.lerpPos.y);
        for (int y = Math.max(world.getMinY(), oy - ry), my = Math.min(world.getMaxY(), oy + ry);
             y <= my; y++) {
            for (int x = Math.max(world.getMinX(), ox - rx), mx = Math.min(world.getMaxX(), ox + rx);
                 x <= mx; x++) {
                var block = world.getBlockStates(x, y, world.pickZ);
                var shape = block.getRayCastingShape();
                if (shape == null) {
                    continue;
                }
                double bx0 = x + shape.minX();
                double by0 = y + shape.minY();
                double bx1 = x + shape.maxX();
                double by1 = y + shape.maxY();
                if (Intersectiond.testPointAar(
                    ((mouseX - (client.window.getWidth() * .5f)) * inv32 + client.player.lerpPos.x()),
                    ((mouseY - (client.window.getHeight() * .5f)) * inv32 + client.player.lerpPos.y()),
                    bx0, by0, bx1, by1
                )) {
                    target = block;
                    targetX = x;
                    targetY = y;
                }
            }
        }
        hitResult.block = target;
        hitResult.x = targetX;
        hitResult.y = targetY;
        hitResult.z = world.pickZ;
        hitResult.miss = (target == null);
    }

    public void renderHit() {
        if (!hitResult.miss &&
            world.isInBorder(hitResult.x, hitResult.y, hitResult.z)) {
            disableTexture2D();
            var shape = hitResult.block.getOutlineShape();
            if (shape != null) {
                float w = getLineWidth();
                lineWidth(2.0f);
                GlUtils.drawRect(hitResult.x + shape.minX(),
                    hitResult.y + shape.minY(),
                    hitResult.x + shape.maxX(),
                    hitResult.y + shape.maxY(),
                    hitResult.z == 0 ? 0x800000FF : 0x80000000,
                    true);
                lineWidth(w);
            }
            enableTexture2D();
        }
    }

    public void processHit() {
        if (!hitResult.miss) {
            var target = hitResult.block;
            int x = hitResult.x;
            int y = hitResult.y;
            int z = hitResult.z;
            if (target.isAir()) {
                if (client.mouse.isBtnDown(GLFW_MOUSE_BUTTON_RIGHT)) {
                    var stack = client.player.getItemMainHand();
                    if (!stack.isEmpty() && stack.getItem() instanceof BlockItemType blockItemType) {
                        world.setBlockStates(x, y, z, blockItemType.getBlock());
                    }
                }
            } else if (client.mouse.isBtnDown(GLFW_MOUSE_BUTTON_LEFT)) {
                world.setBlockStates(x, y, z, Blocks.AIR);
            }
        }
    }

    public void renderEntities(float delta) {
        final double inv32 = 1. / 32.;
        double rx = client.window.getWidth() * .5 * inv32;
        double ry = client.window.getHeight() * .5 * inv32;
        double ox = client.player.lerpPos.x;
        double oy = client.player.lerpPos.y;
        for (var entity : world.entities) {
            if (entity instanceof HumanEntity human) {
                if (entity.box.minX() > ox + rx ||
                    entity.box.minY() > oy + ry ||
                    entity.box.maxX() < ox - rx ||
                    entity.box.maxY() < oy - ry) {
                    continue;
                }
                human.render(delta);
            }
        }
    }

    public void render(float delta, int mouseX, int mouseY) {
        pick(mouseX, mouseY);
        updateDirtyChunks();
        render(0);
        glColor3f(1, 1, 1);
        renderEntities(delta);
        client.player.render(delta, mouseX, mouseY);
        render(1);
        if (client.screen == null) {
            renderHit();
        }
    }

    public void markDirty(int x0, int y0, int x1, int y1) {
        if (x0 < world.getMinX()) {
            x0 = world.getMinX();
        }
        if (y0 < world.getMinY()) {
            y0 = world.getMinY();
        }
        if (x1 > world.getMaxX()) {
            x1 = world.getMaxX();
        }
        if (y1 > world.getMaxY()) {
            y1 = world.getMaxY();
        }
        x0 /= CHUNK_SIZE;
        y0 /= CHUNK_SIZE;
        x1 /= CHUNK_SIZE;
        y1 /= CHUNK_SIZE;
        for (int y = y0; y <= y1; y++) {
            for (int x = x0; x <= x1; x++) {
                getOrCreateChunk(x, y).markDirty();
            }
        }
    }

    @Override
    public void allChanged() {
        markDirty(world.getMinX(), world.getMinY(), world.getMaxX(), world.getMaxY());
    }

    @Override
    public void blockChanged(int x, int y, int z) {
        markDirty(x - 1, y - 1, x + 1, y + 1);
    }

    @Override
    public void close() {
        chunkMap.values().forEach(ClientChunk::free);
    }
}
