/*
 * MIT License
 *
 * Copyright (c) 2022 Overrun Organization
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

import org.jetbrains.annotations.NotNull;

import java.util.StringJoiner;

/**
 * A text with {@link Style}.
 *
 * @author squid233
 * @since 0.6.0
 */
public class StyledText extends BaseText {
    protected Style style;

    protected StyledText(String text, boolean translatable, boolean formatted, Style style) {
        super(text, translatable, formatted);
        this.style = style;
    }

    protected StyledText(String text, boolean translatable, boolean formatted) {
        this(text, translatable, formatted, null);
    }

    @Override
    public @NotNull Style getStyle() {
        return style == null ? Style.EMPTY : style;
    }

    @Override
    public IText setStyle(Style style) {
        var newText = new StyledText(text, translatable, formatted);
        newText.style = style;
        return newText;
    }

    @Override
    public IText copy() {
        return new StyledText(text, translatable, formatted, style);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", StyledText.class.getSimpleName() + "[", "]")
            .add("text='" + text + "'")
            .add("translatable=" + translatable)
            .add("style=" + style)
            .toString();
    }
}
