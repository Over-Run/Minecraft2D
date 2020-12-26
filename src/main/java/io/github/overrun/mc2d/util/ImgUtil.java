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

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static io.github.overrun.mc2d.Minecraft2D.logger;

/**
 * @author squid233
 * @since 2020/09/15
 */
public final class ImgUtil {
    public static BufferedImage readImage(String name) {
        try {
            return ImageIO.read(ClassLoader.getSystemResource(name));
        } catch (IOException e) {
            logger.error("Cannot read image!", e);
            return null;
        }
    }

    public static BufferedImage readImage(ResourceLocation location) {
        return readImage(location.toString());
    }

    public static Image getSubImage(Image img, int x, int y, int w, int h) {
        return img instanceof BufferedImage ? ((BufferedImage) img).getSubimage(x, y, w, h) : Images.EMPTY;
    }
}
