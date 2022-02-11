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

import io.github.overrun.mc2d.block.Block;
import io.github.overrun.mc2d.client.Mc2dClient;
import io.github.overrun.mc2d.client.gui.Framebuffer;
import io.github.overrun.mc2d.util.GlUtils;
import io.github.overrun.mc2d.world.World;
import org.joml.Vector3d;

import static io.github.overrun.mc2d.block.Blocks.AIR;
import static io.github.overrun.mc2d.client.Mouse.isMousePress;
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

    public WorldRenderer(Mc2dClient client, World world) {
        this.client = client;
        this.world = world;
    }

    public void render(double delta, int z, int mouseX, int mouseY) {
        Block target;
        for (int y = 0; y < world.height; y++) {
            for (int x = 0; x < world.width; x++) {
                var b = world.getBlock(x, y, z);
                client.player.prevPos.lerp(client.player.position, delta, interpolation);
                double ltX = (Framebuffer.width >> 1) + (x - interpolation.x) * 32,
                    ltY = (Framebuffer.height >> 1) - (y - interpolation.y) * 32 - 32,
                    rdX = ltX + 32,
                    rdY = ltY + 32;
                var dark = world.getBlock(x, y, 0) == AIR;
                if (z == 0 || dark) {
                    b.render(dark, (int) ltX, (int) ltY, z);
                }
                if (mouseX >= ltX
                    && mouseX < rdX
                    && mouseY >= ltY
                    && mouseY < rdY) {
                    target = world.getBlock(x, y, World.z);
                    glDisable(GL_TEXTURE_2D);
                    if (z == 0 || dark) {
                        var shape = b.getOutlineShape();
                        if (shape.x0 > -1 && shape.y0 > -1 && shape.x1 > -1 && shape.y1 > -1) {
                            GlUtils.drawRect(ltX + shape.x0,
                                ltY + shape.y0,
                                ltX + (shape.x1 << 1),
                                ltY + (shape.y1 << 1),
                                0x80000000,
                                true);
                        }
                    }
                    glEnable(GL_TEXTURE_2D);
                    if (target == AIR) {
                        if (isMousePress(GLFW_MOUSE_BUTTON_RIGHT)) {
                            world.setBlock(x, y, World.z, client.player.handledBlock);
                        }
                    } else if (isMousePress(GLFW_MOUSE_BUTTON_LEFT)) {
                        world.setBlock(x, y, World.z, AIR);
                    }
                }
            }
        }
    }

    public void render(double delta, int mouseX, int mouseY) {
        render(delta, 1, mouseX, mouseY);
        glColor3f(1, 1, 1);
        client.player.render(delta, mouseX, mouseY);
        render(delta, 0, mouseX, mouseY);
    }
}