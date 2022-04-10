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
import io.github.overrun.mc2d.world.block.Block;
import io.github.overrun.mc2d.world.block.Blocks;
import io.github.overrun.mc2d.client.Mc2dClient;
import io.github.overrun.mc2d.client.gui.DrawableHelper;
import io.github.overrun.mc2d.client.gui.Framebuffer;
import io.github.overrun.mc2d.util.Identifier;
import org.joml.Vector3d;
import org.overrun.swgl.core.phys.p2d.AABBox2f;
import org.overrun.swgl.core.util.math.Numbers;

import java.io.IOException;

import static io.github.overrun.mc2d.client.Keyboard.isKeyPress;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 2021/01/09
 */
public class Player extends DrawableHelper {
    public static final Identifier TEXTURE = new Identifier("textures/player.png");
    private static final long serialVersionUID = 2L;
    public Block handledBlock = Blocks.GRASS_BLOCK;
    public int headTexCoord;
    public int bodyTexCoord;
    public final Vector3d prevPos = new Vector3d();
    public final Vector3d position = new Vector3d();
    public boolean facingRight = true;
    public final Vector3d velocity = new Vector3d();
    public AABBox2f box;
    public boolean onGround = false;
    protected float bbWidth = 0.6f;
    protected float bbHeight = 1.8f;
    public World world;

    public Player(World world) {
        this.world = world;
        setPos(Math.random() * world.width, world.height + 10, 0.5);
    }

    public void setPos(double x, double y, double z) {
        position.set(x, y, z);
        float hw = bbWidth * 0.5f;
        box = new AABBox2f((float) (x - hw), (float) y, (float) (x + hw), (float) (y + bbHeight));
    }

    public void tick() {
        prevPos.set(position);
        double xa = 0;
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
            velocity.y = 0.5f;
        }
        if (isKeyPress(GLFW_KEY_LEFT_SHIFT)
            || isKeyPress(GLFW_KEY_S)
            || isKeyPress(GLFW_KEY_DOWN)
        ) {
            velocity.y = -0.5f;
        }
        moveRelative(xa, onGround ? 0.1f : 0.02f);
        velocity.y -= 0.08;
        move((float) velocity.x(), (float) velocity.y());
        velocity.mul(0.91, 0.98, 0.91);
        if (onGround) {
            velocity.mul(0.7, 1.0, 0.7);
        }
    }

    public void moveRelative(double x, float speed) {
        if ((x * x) >= 0.01f) {
            velocity.x += x * speed / Math.abs(x);
        }
    }

    public void move(float x, float y) {
        float xaOrg = x;
        float yaOrg = y;
        var cubes = world.getCubes(0, box.expand(x, y, new AABBox2f()));
        for (var cube : cubes) {
            y = box.clipYCollide(y, cube);
        }
        box.move(0.0f, y);
        for (var cube : cubes) {
            x = box.clipXCollide(x, cube);
        }
        box.move(x, 0.0f);
        onGround = yaOrg != y && yaOrg < 0.0f;
        if (Numbers.isNonEqual(xaOrg, x))
            velocity.x = 0.0f;
        if (Numbers.isNonEqual(yaOrg, y))
            velocity.y = 0.0f;
        position.set(
            (box.getMinX() + box.getMaxX()) * 0.5f,
            box.getMinY(),
            position.z()
        );
    }

    public void render(double delta, int mouseX, int mouseY) {
        var client = Mc2dClient.getInstance();
        double x = Framebuffer.width >> 1;
        double y = Framebuffer.height >> 1;

        client.getTextureManager().bindTexture(TEXTURE);
        glPushMatrix();
        // Move to center
        glTranslated(x, y, 0);
        final float scalar = 1.8f / 2.0f;
        glScalef(scalar, scalar, scalar);
        glBegin(GL_QUADS);

        // Draw head
        glTexCoord2f(facingRight ? 0 : 0.5f, 0);
        glVertex2f(-8, 64);
        glTexCoord2f(facingRight ? 0 : 0.5f, 0.4f);
        glVertex2f(-8, 48);
        glTexCoord2f(facingRight ? 0.5f : 1, 0.4f);
        glVertex2f(8, 48);
        glTexCoord2f(facingRight ? 0.5f : 1, 0);
        glVertex2f(8, 64);

        // Draw body
        glTexCoord2f(facingRight ? 0.5f : 0.75f, 0.4f);
        glVertex2f(-4, 48);
        glTexCoord2f(facingRight ? 0.5f : 0.75f, 1);
        glVertex2f(-4, 24);
        glTexCoord2f(facingRight ? 0.75f : 1, 1);
        glVertex2f(4, 24);
        glTexCoord2f(facingRight ? 0.75f : 1, 0.4f);
        glVertex2f(4, 48);

        // Draw legs
        glTexCoord2f(facingRight ? 0 : 0.25f, 0.4f);
        glVertex2f(-4, 24);
        glTexCoord2f(facingRight ? 0 : 0.25f, 1);
        glVertex2f(-4, 0);
        glTexCoord2f(facingRight ? 0.25f : 0.5f, 1);
        glVertex2f(4, 0);
        glTexCoord2f(facingRight ? 0.25f : 0.5f, 0.4f);
        glVertex2f(4, 24);

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
        double x = position.x, y = position.y;
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "version" -> {
                    var v = reader.nextLong();
                    if (serialVersionUID != v) {
                        throw new RuntimeException("Doesn't compatible with version " + v + ". Current is " + serialVersionUID);
                    }
                }
                case "x" -> x = reader.nextDouble();
                case "y" -> y = reader.nextDouble();
                case "z" -> reader.nextDouble();
            }
        }
        reader.endObject();
        setPos(x, y, position.z);
    }
}
