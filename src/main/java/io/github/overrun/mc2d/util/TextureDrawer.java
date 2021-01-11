/*
 * MIT License
 *
 * Copyright (c) 2020-2021 Over-Run
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

package io.github.overrun.mc2d.util;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author squid233
 * @since 2021/01/11
 */
public final class TextureDrawer {
    private static TextureDrawer instance;
    private TextureDrawer() {}

    /**
     * Starting draw texture.
     * <p>Call {@link TextureDrawer#end()} when end.</p>
     *
     * @param texture The texture id.
     * @return This
     */
    public static TextureDrawer begin(int texture) {
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        ImageReader.bindTexture(texture);
        glBegin(GL_QUADS);
        return instance != null
                ? instance
                : (instance = new TextureDrawer());
    }

    public TextureDrawer color3f(float r, float g, float b) {
        glColor3f(r, g, b);
        return this;
    }

    public TextureDrawer tex2dVertex2d(double s, double t, double x, double y) {
        glTexCoord2d(s, t);
        glVertex2d(x, y);
        return this;
    }

    public TextureDrawer bind(int texture) {
        end();
        return begin(texture);
    }

    public void end() {
        glEnd();
    }
}
