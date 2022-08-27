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

import io.github.overrun.mc2d.util.Language;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author squid233
 * @since 2021/01/29
 */
public abstract class BaseText implements IText {
    protected static final Map<String, StyledText> FORMATTED_MAP = new HashMap<>();
    protected static final Map<String, StyledText> TRANSLATABLE_MAP = new HashMap<>();
    protected static final Map<String, StyledText> FORMAT_TRANSLATABLE_MAP = new HashMap<>();
    protected final String text;
    protected final boolean translatable;
    protected final boolean formatted;

    protected BaseText(String text, boolean translatable, boolean formatted) {
        this.text = text;
        this.translatable = translatable;
        this.formatted = formatted;
    }

    @Override
    public String asString(Object... args) {
        if (translatable) {
            if (formatted) return String.format(Language.getByKey(text), args);
            return Language.getByKey(text);
        }
        if (formatted) return String.format(text, args);
        return text;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BaseText.class.getSimpleName() + "[", "]")
            .add("text='" + text + "'")
            .add("translatable=" + translatable)
            .toString();
    }
}
