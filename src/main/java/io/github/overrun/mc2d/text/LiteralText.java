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

import java.util.Objects;

/**
 * @author squid233
 * @since 2020/09/18
 */
public class LiteralText implements IText {
    public static final LiteralText EMPTY = LiteralText.of("");
    private final String text;
    private final Object[] args;
    private Style style;

    /**
     * Construct a text.<br>
     * Use {@link LiteralText#of(String, Object...)}.
     *
     * @param text The text.
     * @param args Args for format.
     */
    private LiteralText(String text, Style style, Object... args) {
        this.text = text;
        this.style = style;
        this.args = Objects.requireNonNullElse(args, new Object[0]);
    }

    public static LiteralText of(String text, Style style, Object... args) {
        return new LiteralText(text, style, args);
    }

    public static LiteralText of(String text, Object... args) {
        return of(text, Style.EMPTY, args);
    }

    @Override
    public String toString() {
        return asString();
    }

    @Override
    public String asString() {
        return text;
    }

    @Override
    public String asFormattedString() {
        return String.format(text, args);
    }

    @Override
    public Style getStyle() {
        return style;
    }

    @Override
    public LiteralText setStyle(Style style) {
        this.style = style;
        return this;
    }
}
