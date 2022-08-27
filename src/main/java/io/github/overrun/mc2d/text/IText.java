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

package io.github.overrun.mc2d.text;

import org.jetbrains.annotations.NotNull;

/**
 * @author squid233
 * @since 2021/01/29
 */
public interface IText {
    IText EMPTY = literal("");

    static IText literal(String text) {
        return new StyledText(text, false, false);
    }

    static IText formatted(String text) {
        return BaseText.FORMATTED_MAP.computeIfAbsent(text, s -> new StyledText(s, false, true));
    }

    static IText translatable(String text) {
        return BaseText.TRANSLATABLE_MAP.computeIfAbsent(text, s -> new StyledText(s, true, false));
    }

    static IText formatTranslatable(String text) {
        return BaseText.FORMAT_TRANSLATABLE_MAP.computeIfAbsent(text, s -> new StyledText(s, true, true));
    }

    /**
     * Cast to string.
     * <p>May be a translated text.</p>
     *
     * @param args the arguments for formatting
     * @return The text.
     */
    String asString(Object... args);

    default @NotNull Style getStyle() {
        return Style.EMPTY;
    }

    IText setStyle(Style style);

    IText copy();

    default IText withColor(TextColor color) {
        return setStyle(getStyle().withColor(color));
    }
}
