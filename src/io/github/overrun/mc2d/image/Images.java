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

package io.github.overrun.mc2d.image;

import io.github.overrun.mc2d.Minecraft2D;
import io.github.overrun.mc2d.asset.AssetManager;
import io.github.overrun.mc2d.block.AbstractBlock;
import io.github.overrun.mc2d.client.Mc2dClient;
import io.github.overrun.mc2d.util.Identifier;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.util.HashMap;

/**
 * @author squid233
 * @since 2020/09/15
 */
public class Images {
    public static final HashMap<Character, Image> ASCII_IMAGE_MAP = new HashMap<>(95);
    public static final HashMap<Character, Integer> ASCII_IMAGE_WIDTH = new HashMap<>(95);
    public static final HashMap<String, Image> BLOCK_IMG_MAP = new HashMap<>(5);

    static {
        putsAscii();
    }

    public static Image getBlockTexture(AbstractBlock block) {
        var id = new Identifier(block.getModel().getProperty("texture"));
        if (!BLOCK_IMG_MAP.containsKey(block.getRegistryName().toString())) {
            BLOCK_IMG_MAP.put(block.getRegistryName().toString(),
                    new ImageIcon(AssetManager.getAsString(id.getNamespace(), "textures", id.getPath() + ".png")).getImage());
        }
        return BLOCK_IMG_MAP.get(block.getRegistryName().toString());
    }

    public static Image getImagePart(Image img, int x, int y, int width, int height) {
        return Mc2dClient.getInstance().createImage(new FilteredImageSource(img.getSource(), new CropImageFilter(x, y, width, height)));
    }

    public static Image getAsciiPart(int x, int y, int width) {
        return getImagePart(new ImageIcon(AssetManager.getAsString(Minecraft2D.NAMESPACE, "textures", "font", "ascii.png")).getImage(), x, y, width, 8);
    }

    public static Image getAsciiInMap(char c) {
        try {
            return ASCII_IMAGE_MAP.get(c);
        } catch (Exception e) {
            return ASCII_IMAGE_MAP.get(' ');
        }
    }

    private static void putAscii(char c, int x, int y, int width) {
        ASCII_IMAGE_MAP.put(c, getAsciiPart(x, y, width));
        ASCII_IMAGE_WIDTH.put(c, width);
    }

    private static void putAscii(int c, int x, int y, int width) {
        putAscii((char) c, x, y, width);
    }

    private static void putsAscii() {
        put128asciiU();
    }

    private static void put128asciiU() {
        putAscii(' ', 0, 16, 6);
        putAscii('!', 8, 16, 2);
        putAscii('"', 16, 16, 4);
        // ascii #, $, %, &
        for (int i = 0; i < 4; i++) {
            putAscii(i + 35, i + 3 << 3, 16, 6);
        }
        putAscii('\'', 56, 16, 2);
        putAscii('(', 64, 16, 4);
        putAscii(')', 72, 16, 4);
        putAscii('*', 80, 16, 4);
        putAscii('+', 88, 16, 6);
        putAscii(',', 96, 16, 2);
        putAscii('-', 104, 16, 6);
        putAscii('.', 112, 16, 2);
        putAscii('/', 120, 16, 6);
        // ascii 0~10
        for (int i = 0; i < 10; i++) {
            putAscii(i + 48, i << 3, 24, 6);
        }
        putAscii(':', 80, 24, 2);
        putAscii(';', 88, 24, 2);
        putAscii('<', 96, 24, 5);
        putAscii('=', 104, 24, 6);
        putAscii('>', 112, 24, 5);
        putAscii('?', 120, 24, 6);
        putAscii('@', 0, 32, 7);
        // ascii A~H
        for (int i = 0; i < 8; i++) {
            putAscii(i + 65, i + 1 << 3, 32, 6);
        }
        putAscii('I', 72, 32, 4);
        // ascii J~O
        for (int i = 0; i < 6; i++) {
            putAscii(i + 74, i + 10 << 3, 32, 6);
        }
        // ascii P~Z
        for (int i = 0; i < 11; i++) {
            putAscii(i + 80, i << 3, 40, 6);
        }
        putAscii('[', 88, 40, 4);
        putAscii('\\', 96, 40, 6);
        putAscii(']', 104, 40, 4);
        putAscii('^', 112, 40, 6);
        putAscii('_', 120, 40, 6);
        putAscii('`', 0, 48, 3);
        // ascii a~e
        for (int i = 0; i < 5; i++) {
            putAscii(i + 97, i + 1 << 3, 48, 6);
        }
        putAscii('f', 48, 48, 5);
        putAscii('g', 56, 48, 6);
        putAscii('h', 64, 48, 6);
        putAscii('i', 72, 48, 2);
        putAscii('j', 80, 48, 6);
        putAscii('k', 88, 48, 5);
        putAscii('l', 96, 48, 3);
        putAscii('m', 104, 48, 6);
        putAscii('n', 112, 48, 6);
        putAscii('o', 120, 48, 6);
        // ascii p~s
        for (int i = 0; i < 4; i++) {
            putAscii(i + 112, i << 3, 56, 6);
        }
        putAscii('t', 32, 56, 4);
        // ascii u~z
        for (int i = 0; i < 6; i++) {
            putAscii(i + 117, i + 5 << 3, 56, 6);
        }
        putAscii('{', 88, 56, 4);
        putAscii('|', 96, 56, 4);
        putAscii('}', 104, 56, 4);
        putAscii('~', 112, 56, 4);
    }
}
