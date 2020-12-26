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

package io.github.overrun.mc2d.util;

import io.github.overrun.mc2d.text.IText;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import static io.github.overrun.mc2d.Minecraft2D.getHeight;
import static io.github.overrun.mc2d.Minecraft2D.getWidth;
import static io.github.overrun.mc2d.util.Coordinator.*;
import static io.github.overrun.mc2d.util.collect.Arrays.notContains;
import static java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment;

/**
 * @author squid233
 * @since 2020/09/15
 */
public final class DrawHelper {
    public static final Color BG_COLOR = new Color(64, 64, 64, 64);
    public static final Color TOOLTIP_COLOR = new Color(64, 0, 128, 128);
    public static final Color TOOLTIP_BG_COLOR = new Color(0, 0, 0, 192);
    public static Font simsunb;
    public static final Font SIMSUN = new Font("新宋体", Font.PLAIN, 21);

    static {
        try {
            if (notContains(getLocalGraphicsEnvironment().getAvailableFontFamilyNames(),
                    "新宋体")) {
                simsunb = Font.createFont(Font.TRUETYPE_FONT,
                        new File("simsun.ttc"))
                        .deriveFont(21f);
            }
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void drawImage(Graphics g, Image image, Point point) {
        g.drawImage(image, point.x, point.y, null);
    }

    public static void drawImage(Graphics g, Image image, Point point, int width, int height) {
        g.drawImage(image, point.x, point.y, width, height, null);
    }

    public static void drawImage(Graphics g, Image image, int x, int y) {
        g.drawImage(image, x, y, null);
    }

    public static void drawImage(Graphics g, Image image, int x, int y, int width, int height) {
        g.drawImage(image, x, y, width, height, null);
    }

    public static void drawCenterImage(Graphics g, Image image, int y) {
        drawImage(g, image, transformation(-(image.getWidth(null) >> 1), y, U_M));
    }

    public static void drawCenterImage(Graphics g, Image image, int y, int width, int height) {
        drawImage(g, image, transformation(-(width >> 1), y, U_M), width, height);
    }

    public static void drawCenteredText(Graphics g, IText text, int y) {
        drawCenteredText(g, text, y, U_M);
    }

    public static void drawCenteredText(Graphics g, IText text, int y, int layout) {
        drawText(g, text, transformation(-(text.getPrevWidth(g) >> 1), y, isCenter(layout) ? layout : U_M));
    }

    public static void drawText(Graphics g, IText text, Point point) {
        drawText(g, text, point.x, point.y);
    }

    public static void drawText(Graphics g, IText text, int x, int y, int layout) {
        drawText(g, text, transformation(x, y, layout));
    }

    public static void drawText(Graphics g, IText text, int x, int y) {
        drawWithColor(g, Color.WHITE, gg -> {
            gg.setFont(simsunb == null ? SIMSUN : simsunb);
            gg.drawString(text.asString(), x, y + text.getPrevHeight(gg));
        });
    }

    public static void drawWithColor(Graphics g, Color color, Consumer<Graphics> consumer) {
        final Color c = g.getColor();
        g.setColor(color);
        consumer.accept(g);
        g.setColor(c);
    }

    public static void drawRect(Graphics g, Color color, int x, int y, int width, int height, int layout) {
        drawWithColor(g, color, gg -> {
            Point p = transformation(x, y, layout);
            gg.drawRect(p.x, p.y, width, height);
        });
    }

    public static void drawRect(Graphics g, Color color, int x, int y, int width, int height) {
        drawWithColor(g, color, gg -> gg.drawRect(x, y, width, height));
    }

    public static void fillRect(Graphics g, Color color, int x, int y, int width, int height, int layout) {
        drawWithColor(g, color, gg -> {
            Point p = transformation(x, y, layout);
            gg.fillRect(p.x, p.y, width, height);
        });
    }

    public static void fillRect(Graphics g, Color color, int x, int y, int width, int height) {
        drawWithColor(g, color, gg -> gg.fillRect(x, y, width, height));
    }

    public static FontMetrics getSimsunMetrics(Graphics g) {
        return g.getFontMetrics(simsunb == null ? SIMSUN : simsunb);
    }

    public static void renderTooltips(Graphics g, List<IText> tooltips, int x, int y) {
        if (!tooltips.isEmpty()) {
            int w = 0;
            int h = 0;
            for (IText text : tooltips) {
                if (text.getPrevWidth(g) > w) { w = text.getPrevWidth(g); }
                if (text.getPrevHeight(g) > h) { h = text.getPrevHeight(g); }
            }
            w += 12;
            h += 12;
            int sX;
            int sY;
            if (x + w > getWidth()) { sX = -10; } else { sX = 10; }
            if (y + h > getHeight()) { sY = -10; } else { sY = 10; }
            // Draw top
            fillRect(g, TOOLTIP_BG_COLOR, x + 2 + sX, y + sY, w - 4, 2);
            // Draw left
            fillRect(g, TOOLTIP_BG_COLOR, x + sX, y + 2 + sY, 2, h - 4);
            // Draw right
            fillRect(g, TOOLTIP_BG_COLOR, x + w - 2 + sX, y + 2 + sY, 2, h - 4);
            // Draw bottom
            fillRect(g, TOOLTIP_BG_COLOR, x + 2 + sX, y + h - 2 + sY, w - 4, 2);
            // Draw center
            fillRect(g, TOOLTIP_BG_COLOR, x + 3 + sX, y + 3 + sY, w - 6, h - 6);
            drawRect(g, TOOLTIP_COLOR, x + 2 + sX, y + 2 + sY, w - 5, h - 5);
            for (int i = 0; i < tooltips.size(); i++) {
                drawText(g, tooltips.get(i), x + 4 + sX, y + 2 + i * tooltips.get(i).getPrevHeight(g) + 2 + sY);
            }
        }
    }
}
