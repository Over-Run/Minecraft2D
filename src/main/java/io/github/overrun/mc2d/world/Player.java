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

package io.github.overrun.mc2d.world;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.github.overrun.mc2d.block.Block;
import io.github.overrun.mc2d.block.Blocks;
import io.github.overrun.mc2d.client.Mc2dClient;
import io.github.overrun.mc2d.client.gui.DrawableHelper;
import io.github.overrun.mc2d.client.gui.Framebuffer;
import io.github.overrun.mc2d.util.Identifier;
import org.joml.Vector3d;

import java.io.IOException;

import static io.github.overrun.mc2d.client.Keyboard.isKeyPress;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 2021/01/09
 */
public final class Player extends DrawableHelper {
    public static final Identifier TEXTURE = new Identifier("textures/player.png");
    private static final long serialVersionUID = 2L;
    public Block handledBlock = Blocks.GRASS_BLOCK;
    public int headTexCoord;
    public int bodyTexCoord;
    public final Vector3d prevPos = new Vector3d();
    public final Vector3d position = new Vector3d();
    public boolean facingRight = true;
    public final Vector3d velocity = new Vector3d();

    public Player(int ww, int wh, int wd) {
        position.x = Math.random() * ww;
        position.y = wh + 10;
        position.z = 1.5;
    }

    public void tick() {
        prevPos.set(position);
        double xa = 0, ya = 0;
        if (isKeyPress(GLFW_KEY_A)
            || isKeyPress(GLFW_KEY_LEFT)) {
            --xa;
            facingRight = false;
            headTexCoord = 8;
            bodyTexCoord = 4;
        }
        if (isKeyPress(GLFW_KEY_D)
            || isKeyPress(GLFW_KEY_RIGHT)) {
            ++xa;
            facingRight = true;
            headTexCoord = 0;
            bodyTexCoord = 0;
        }
        if (isKeyPress(GLFW_KEY_SPACE)
            || isKeyPress(GLFW_KEY_W)
            || isKeyPress(GLFW_KEY_UP)) {
            ++ya;
        }
        if (isKeyPress(GLFW_KEY_LEFT_SHIFT)
            || isKeyPress(GLFW_KEY_S)
            || isKeyPress(GLFW_KEY_DOWN)
        ) {
            --ya;
        }
        velocity.add(xa * 0.1, ya * 0.1, 0);
        position.add(velocity);
        velocity.mul(0.91, 0.91, 0.91);
        velocity.mul(0.7, 0.7, 0.7);
    }

    public void render(double delta, int mouseX, int mouseY) {
        var client = Mc2dClient.getInstance();
        double x = Framebuffer.width >> 1;
        double y = Framebuffer.height >> 1;

        client.getTextureManager().bindTexture(TEXTURE);
        glPushMatrix();
        // Move to center
        glTranslated(x, y, 0);
        glBegin(GL_QUADS);

        // Draw head
        glTexCoord2f(facingRight ? 0 : 0.5f, 0);
        glVertex2f(-8, -64);
        glTexCoord2f(facingRight ? 0 : 0.5f, 0.4f);
        glVertex2f(-8, -48);
        glTexCoord2f(facingRight ? 0.5f : 1, 0.4f);
        glVertex2f(8, -48);
        glTexCoord2f(facingRight ? 0.5f : 1, 0);
        glVertex2f(8, -64);

        // Draw body
        glTexCoord2f(facingRight ? 0.5f : 0.75f, 0.4f);
        glVertex2f(-4, -48);
        glTexCoord2f(facingRight ? 0.5f : 0.75f, 1);
        glVertex2f(-4, -24);
        glTexCoord2f(facingRight ? 0.75f : 1, 1);
        glVertex2f(4, -24);
        glTexCoord2f(facingRight ? 0.75f : 1, 0.4f);
        glVertex2f(4, -48);

        // Draw legs
        glTexCoord2f(facingRight ? 0 : 0.25f, 0.4f);
        glVertex2f(-4, -24);
        glTexCoord2f(facingRight ? 0 : 0.25f, 1);
        glVertex2f(-4, 0);
        glTexCoord2f(facingRight ? 0.25f : 0.5f, 1);
        glVertex2f(4, 0);
        glTexCoord2f(facingRight ? 0.25f : 0.5f, 0.4f);
        glVertex2f(4, -24);

        glEnd();
        glPopMatrix();
    }

    public void serialize(JsonWriter writer) throws IOException {
        writer.beginObject()
            .name("version").value(serialVersionUID)
            .name("x").value(position.x)
            .name("y").value(position.y)
            .name("z").value(position.z)
            .endObject();
    }

    public void deserialize(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "version":
                    var v = reader.nextLong();
                    if (serialVersionUID != v) {
                        throw new RuntimeException("Doesn't compatible with version " + v + ". Current is " + serialVersionUID);
                    }
                    break;
                case "x":
                    position.x = reader.nextDouble();
                    break;
                case "y":
                    position.y = reader.nextDouble();
                    break;
                case "z":
                    reader.nextDouble();
            }
        }
        reader.endObject();
    }
}
