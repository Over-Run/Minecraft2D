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

import io.github.overrun.mc2d.block.Block;
import io.github.overrun.mc2d.client.Mc2dClient;
import io.github.overrun.mc2d.item.Item;
import io.github.overrun.mc2d.util.ResourceLocation;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author squid233
 * @since 2020/09/15
 */
public class Images {
    public static Image FONT_ASCII_IMG;
    public static final Image WIDGETS = ImageIcons.getGameImage("textures/gui/widgets.png");
    public static final Image MISSINGO = ImageIcons.getGameImage("textures/gui/missingo.png");
    public static final ResourceLocation OPTIONS_BG_ID = new ResourceLocation("textures/gui/options_background.png");
    public static final Image OPTIONS_BG = ImageIcons.getGameImage("textures/gui/options_background.png");

    public static final HashMap<Character, Image> CHAR_IMAGE_MAP = new HashMap<>(95);
    public static final HashMap<Character, Integer> CHAR_IMAGE_WIDTH = new HashMap<>(95);
    public static final HashMap<String, Image> BLOCK_IMG_MAP = new HashMap<>(6);
    public static final HashMap<String, Image> ITEM_IMG_MAP = new HashMap<>(6);

    static {
        try {
            FONT_ASCII_IMG = ImageIO.read(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream(ResourceLocation.asString("textures/font/ascii.png"))));
        } catch (IOException | NullPointerException e) {
            FONT_ASCII_IMG = ImageIcons.getGameImage(ResourceLocation.asString("textures/font/ascii.png"));
        }
        putsAscii();
    }

    public static Image getBlockTexture(Block block) {
        if (!BLOCK_IMG_MAP.containsKey(block.getRegistryName().toString())) {
            try {
                BLOCK_IMG_MAP.put(block.getRegistryName().toString(),
                        ImageIO.read(ClassLoader.getSystemResource(ResourceLocation.asString("textures/" + block.getModel().getProperty("texture") + ".png"))));
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
                BLOCK_IMG_MAP.put(block.getRegistryName().toString(), MISSINGO);
            }
        }
        return BLOCK_IMG_MAP.get(block.getRegistryName().toString());
    }

    public static Image getItemTexture(Item item) {
        if (!ITEM_IMG_MAP.containsKey(item.toString())) {
            try {
                ITEM_IMG_MAP.put(item.toString(),
                        ImageIO.read(ClassLoader.getSystemResource(ResourceLocation.asString("textures/item/" + item.getRegistryName().getPath() + ".png"))));
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
                ITEM_IMG_MAP.put(item.toString(), MISSINGO);
            }
        }
        return ITEM_IMG_MAP.get(item.getRegistryName().toString());
    }

    public static Image getImagePart(Image img, int x, int y, int width, int height) {
        return Mc2dClient.getInstance().createImage(new FilteredImageSource(img.getSource(), new CropImageFilter(x, y, width, height)));
    }

    private static Image getAsciiPart(int x, int y, int width) {
        return getImagePart(new ImageIcon(FONT_ASCII_IMG).getImage(), x, y, width, 8);
    }

    public static Image getCharInMap(char c) {
        try {
            return CHAR_IMAGE_MAP.get(c);
        } catch (Exception e) {
            return CHAR_IMAGE_MAP.get(' ');
        }
    }

    private static void putAscii(char c, int x, int y, int width) {
        CHAR_IMAGE_MAP.put(c, getAsciiPart(x, y, width));
        CHAR_IMAGE_WIDTH.put(c, width);
    }

    private static void putAscii(int c, int x, int y, int width) {
        putAscii((char) c, x, y, width);
    }

    private static void putsAscii() {
        put128asciiU();
    }

    private static void put128asciiU() {
        putAscii(' ', 0, 16, 5);
        putAscii('!', 8, 16, 1);
        putAscii('"', 16, 16, 3);
        // ascii #, $, %, &
        for (int i = 0; i < 4; i++) {
            putAscii(i + 35, i + 3 << 3, 16, 5);
        }
        putAscii('\'', 56, 16, 1);
        putAscii('(', 64, 16, 3);
        putAscii(')', 72, 16, 3);
        putAscii('*', 80, 16, 3);
        putAscii('+', 88, 16, 5);
        putAscii(',', 96, 16, 1);
        putAscii('-', 104, 16, 5);
        putAscii('.', 112, 16, 1);
        putAscii('/', 120, 16, 5);
        // ascii 0~10
        for (int i = 0; i < 10; i++) {
            putAscii(i + 48, i << 3, 24, 5);
        }
        putAscii(':', 80, 24, 1);
        putAscii(';', 88, 24, 1);
        putAscii('<', 96, 24, 4);
        putAscii('=', 104, 24, 5);
        putAscii('>', 112, 24, 4);
        putAscii('?', 120, 24, 5);
        putAscii('@', 0, 32, 6);
        // ascii A~H
        for (int i = 0; i < 8; i++) {
            putAscii(i + 65, i + 1 << 3, 32, 5);
        }
        putAscii('I', 72, 32, 3);
        // ascii J~O
        for (int i = 0; i < 6; i++) {
            putAscii(i + 74, i + 10 << 3, 32, 5);
        }
        // ascii P~Z
        for (int i = 0; i < 11; i++) {
            putAscii(i + 80, i << 3, 40, 5);
        }
        putAscii('[', 88, 40, 3);
        putAscii('\\', 96, 40, 5);
        putAscii(']', 104, 40, 2);
        putAscii('^', 112, 40, 5);
        putAscii('_', 120, 40, 5);
        putAscii('`', 0, 48, 2);
        // ascii a~e
        for (int i = 0; i < 5; i++) {
            putAscii(i + 97, i + 1 << 3, 48, 5);
        }
        putAscii('f', 48, 48, 4);
        putAscii('g', 56, 48, 5);
        putAscii('h', 64, 48, 5);
        putAscii('i', 72, 48, 1);
        putAscii('j', 80, 48, 5);
        putAscii('k', 88, 48, 4);
        putAscii('l', 96, 48, 2);
        putAscii('m', 104, 48, 5);
        putAscii('n', 112, 48, 5);
        putAscii('o', 120, 48, 5);
        // ascii p~s
        for (int i = 0; i < 4; i++) {
            putAscii(i + 112, i << 3, 56, 5);
        }
        putAscii('t', 32, 56, 3);
        // ascii u~z
        for (int i = 0; i < 6; i++) {
            putAscii(i + 117, i + 5 << 3, 56, 5);
        }
        putAscii('{', 88, 56, 3);
        putAscii('|', 96, 56, 3);
        putAscii('}', 104, 56, 3);
        putAscii('~', 112, 56, 3);
    }
}
