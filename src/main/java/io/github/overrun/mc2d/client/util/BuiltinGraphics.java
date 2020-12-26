/*
 * MIT License
 *
 * Copyright (c) 2020 Over-Run
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

package io.github.overrun.mc2d.client.util;

import io.github.overrun.mc2d.text.IText;
import io.github.overrun.mc2d.util.DrawHelper;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author squid233
 * @since 2020/12/26
 */
public final class BuiltinGraphics {
    private final Graphics g;

    public BuiltinGraphics(Graphics g) {
        this.g = g;
    }

    public void drawImage(Image image, Point point) {
        DrawHelper.drawImage(g, image, point);
    }

    public void drawImage(Image image, Point point, int width, int height) {
        DrawHelper.drawImage(g, image, point, width, height);
    }

    public void drawImage(Image image, int x, int y) {
        DrawHelper.drawImage(g, image, x, y);
    }

    public void drawImage(Image image, int x, int y, int width, int height) {
        DrawHelper.drawImage(g, image, x, y, width, height);
    }

    public void drawCenterImage(Image image, int y) {
        DrawHelper.drawCenterImage(g, image, y);
    }

    public void drawCenterImage(Image image, int y, int width, int height) {
        DrawHelper.drawCenterImage(g, image, y, width, height);
    }

    public void drawCenteredText(IText text, int y) {
        DrawHelper.drawCenteredText(g, text, y);
    }

    public void drawCenteredText(IText text, int y, int layout) {
        DrawHelper.drawCenteredText(g, text, y, layout);
    }

    public void drawText(IText text, Point point) {
        DrawHelper.drawText(g, text, point);
    }

    public void drawText(IText text, int x, int y, int layout) {
        DrawHelper.drawText(g, text, x, y, layout);
    }

    public void drawText(IText text, int x, int y) {
        DrawHelper.drawText(g, text, x, y);
    }

    public void drawWithColor(Color color, Consumer<Graphics> consumer) {
        DrawHelper.drawWithColor(g, color, consumer);
    }

    public void drawRect(Color color, int x, int y, int width, int height, int layout) {
        DrawHelper.drawRect(g, color, x, y, width, height, layout);
    }

    public void drawRect(Color color, int x, int y, int width, int height) {
        DrawHelper.drawRect(g, color, x, y, width, height);
    }

    public void fillRect(Color color, int x, int y, int width, int height, int layout) {
        DrawHelper.fillRect(g, color, x, y, width, height, layout);
    }

    public void fillRect(Color color, int x, int y, int width, int height) {
        DrawHelper.fillRect(g, color, x, y, width, height);
    }

    public FontMetrics getSimsunMetrics() {
        return DrawHelper.getSimsunMetrics(g);
    }

    public void renderTooltips(List<IText> tooltips, int x, int y) {
        DrawHelper.renderTooltips(g, tooltips, x, y);
    }

    public Graphics getGraphics() {
        return g;
    }
}
