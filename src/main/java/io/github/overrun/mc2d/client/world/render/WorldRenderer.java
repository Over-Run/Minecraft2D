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
import org.joml.Vector3d;
import org.overrun.swgl.core.gl.GLStateMgr;

import static io.github.overrun.mc2d.client.Mouse.isMousePress;
import static io.github.overrun.mc2d.world.block.Blocks.AIR;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 0.6.0
 */
public class WorldRenderer {
    private final Mc2dClient client;
    private final World world;
    /**
     * Linear interpolation storage. Let it don't create more objects.
     */
    private final Vector3d interpolation = new Vector3d();
    private HitResult hitResult = new HitResult(null, 0, 0, 0, true);
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
        Block target = null;
        client.getTextureManager().bindTexture(BlockModelMgr.BLOCK_ATLAS);
        glBegin(GL_QUADS);
        for (int y = 0; y < world.height; y++) {
            for (int x = 0; x < world.width; x++) {
                var b = world.getBlock(x, y, z);
                double ldX = (Framebuffer.width >> 1) + (x - interpolation.x) * 32,
                    ldY = (Framebuffer.height >> 1) + (y - interpolation.y) * 32,
                    rtX = ldX + 32,
                    rtY = ldY + 32;
                if (ldX > Framebuffer.width || ldY > Framebuffer.height || rtX < 0 || rtY < 0) {
                    continue;
                }
                var upAir = world.getBlock(x, y, 1) == AIR;
                if (z == 1 || upAir) {
                    b.render(null, (int) ldX, (int) ldY, z);
                }
                if (mouseX >= ldX
                    && mouseX < rtX
                    && mouseY >= ldY
                    && mouseY < rtY) {
                    target = world.getBlock(x, y, world.pickZ);
                    hitResult.block = target;
                    hitResult.x = x;
                    hitResult.y = y;
                    hitResult.z = world.pickZ;
                }
            }
        }
        glEnd();
        hitResult.miss = (target == null);
        client.getTextureManager().bindTexture(0);
    }

    public void renderHit() {
        if (!hitResult.miss) {
            double ldX = (Framebuffer.width >> 1) + (hitResult.x - interpolation.x) * 32,
                ldY = (Framebuffer.height >> 1) + (hitResult.y - interpolation.y) * 32;
            boolean upAir = world.getBlock(hitResult.x, hitResult.y, 1) == AIR;
            GLStateMgr.disableTexture2D();
            if (hitResult.z == 1 || upAir) {
                var shape = hitResult.block.getOutlineShape();
                if (shape != null) {
                    GlUtils.drawRect(ldX + ((int) shape.minX() << 4),
                        ldY + ((int) shape.minY() << 4),
                        ldX + ((int) shape.maxX() << 5),
                        ldY + ((int) shape.maxY() << 5),
                        0x80000000,
                        true);
                }
            }
            GLStateMgr.enableTexture2D();
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
                    world.setBlock(x, y, z, client.player.handledBlock);
                }
            } else if (isMousePress(GLFW_MOUSE_BUTTON_LEFT)) {
                world.setBlock(x, y, z, AIR);
            }
        }
    }

    public void render(float delta, int mouseX, int mouseY) {
        client.player.prevPos.lerp(client.player.position, delta, interpolation);
        glEnable(GL_LIGHTING);
        glEnable(GL_COLOR_MATERIAL);
        glLightModelfv(GL_LIGHT_MODEL_AMBIENT, new float[]{0.5f, 0.5f, 0.5f, 1.0f});
        render(0, mouseX, mouseY);
        glDisable(GL_LIGHTING);
        glColor3f(1, 1, 1);
        client.player.render(delta, mouseX, mouseY);
        render(1, mouseX, mouseY);
        renderHit();
    }
}
