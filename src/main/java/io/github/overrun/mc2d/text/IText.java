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

package io.github.overrun.mc2d.text;

import java.awt.Graphics;

import static io.github.overrun.mc2d.util.DrawHelper.getSimsunMetrics;

/**
 * @author squid233
 * @since 2020/09/18
 */
public interface IText {
    /**
     * Convert the text to <b>formatted</b> string.
     *
     * @return The formatted string.
     * @apiNote Override this method to format string, override
     * {@link IText#toString()} to the <b>un-formatted</b> string.
     * @see IText#toString()
     */
    String asString();

    /**
     * Convert the text to <b>un-formatted</b> string.
     *
     * @return The un-formatted string.
     * @apiNote Override this method to un-formatted string, override
     * {@link IText#asString()} to the <b>formatted</b> string.
     * @see IText#asString()
     */
    @Override
    String toString();

    IText EMPTY = new LiteralText("");

    /**
     * Create a text without any string.
     *
     * @return The empty text.
     */
    static IText of() {
        return EMPTY;
    }

    default int getPrevWidth(Graphics g) {
        return getSimsunMetrics(g).stringWidth(asString());
    }

    default int getPrevHeight(Graphics g) {
        return getSimsunMetrics(g).getAscent();
    }
}
