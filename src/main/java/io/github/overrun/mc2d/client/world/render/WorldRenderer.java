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
import io.github.overrun.mc2d.client.gui.Framebuffer;
import io.github.overrun.mc2d.client.model.BlockModelMgr;
import io.github.overrun.mc2d.client.world.ClientChunk;
import io.github.overrun.mc2d.util.GlUtils;
import io.github.overrun.mc2d.world.HitResult;
import io.github.overrun.mc2d.world.World;
import io.github.overrun.mc2d.world.block.Block;
import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import org.joml.Intersectiond;

import static io.github.overrun.mc2d.client.Mouse.isMousePress;
import static io.github.overrun.mc2d.world.block.Blocks.AIR;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
import static org.lwjgl.opengl.GL11.*;
import static org.overrun.swgl.core.gl.GLStateMgr.*;

/**
 * @author squid233
 * @since 0.6.0
 */
public class WorldRenderer {
    private final Mc2dClient client;
    private final World world;
    private final HitResult hitResult = new HitResult(null, 0, 0, 0, true);
    private final Long2ObjectMap<ClientChunk> chunkMap = new Long2ObjectArrayMap<>();

    public WorldRenderer(Mc2dClient client, World world) {
        this.client = client;
        this.world = world;
    }

    public ClientChunk getOrCreateChunk(int x, int y) {
        return chunkMap.computeIfAbsent(ClientChunk.pos2Long(x, y),
            pos -> {
                int x0 = x * ClientChunk.CHUNK_SIZE;
                int y0 = y * ClientChunk.CHUNK_SIZE;
                int x1 = (x + 1) * ClientChunk.CHUNK_SIZE;
                int y1 = (y + 1) * ClientChunk.CHUNK_SIZE;

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

    public void render(int z, int mouseX, int mouseY) {
        client.getTextureManager().bindTexture(BlockModelMgr.BLOCK_ATLAS);
        glBegin(GL_QUADS);
        for (int y = 0; y < world.height; y++) {
            for (int x = 0; x < world.width; x++) {
                var b = world.getBlock(x, y, z);
                var upAir = world.getBlock(x, y, 1) == AIR;
                if (z == 1 || upAir) {
                    b.render(null, x, y, z);
                }
            }
        }
        glEnd();
        client.getTextureManager().bindTexture(0);
    }

    public void pick(int mouseX, int mouseY) {
        Block target = null;
        int targetX = 0, targetY = 0;
        final double inv32 = 1. / 32.;
        int rx = (int) Math.ceil(Framebuffer.width * .5 * inv32);
        int ry = (int) Math.ceil(Framebuffer.height * .5 * inv32);
        int ox = (int) Math.floor(client.player.lerpPos.x);
        int oy = (int) Math.floor(client.player.lerpPos.y);
        for (int y = Math.min(world.getMinY(), oy - ry), my = Math.min(world.getMaxY(), oy + ry);
             y <= my; y++) {
            for (int x = Math.min(world.getMinX(), ox - rx), mx = Math.min(world.getMaxX(), ox + rx);
                 x <= mx; x++) {
                var block = world.getBlock(x, y, world.pickZ);
                var shape = block.getRayCastingShape();
                if (shape == null) {
                    continue;
                }
                double bx0 = x + shape.minX();
                double by0 = y + shape.minY();
                double bx1 = x + shape.maxX();
                double by1 = y + shape.maxY();
                if (Intersectiond.testPointAar(
                    ((mouseX - (Framebuffer.width * .5f)) * inv32 + client.player.lerpPos.x()),
                    ((mouseY - (Framebuffer.height * .5f)) * inv32 + client.player.lerpPos.y()),
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
        if (!hitResult.miss) {
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
            if (target == AIR) {
                if (isMousePress(GLFW_MOUSE_BUTTON_RIGHT)) {
                    world.setBlock(x, y, z, client.player.mainHand);
                }
            } else if (isMousePress(GLFW_MOUSE_BUTTON_LEFT)) {
                world.setBlock(x, y, z, AIR);
            }
        }
    }

    public void render(float delta, int mouseX, int mouseY) {
        pick(mouseX, mouseY);
        render(0, mouseX, mouseY);
        glColor3f(1, 1, 1);
        client.player.render(delta, mouseX, mouseY);
        render(1, mouseX, mouseY);
        if (client.screen == null) {
            renderHit();
        }
    }
}
