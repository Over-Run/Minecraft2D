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

import io.github.overrun.mc2d.lang.Language;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.Arrays;

/**
 * @author squid233
 * @since 2020/10/12
 */
public class TranslatableText implements IText {
    private static final Object2ObjectMap<String, TranslatableText> INSTANCES = new Object2ObjectOpenHashMap<>();
    private final String key;
    private final Object[] params;

    private TranslatableText(String key, Object... params) {
        this.key = key;
        this.params = params;
    }

    public static TranslatableText of(String key, Object... args) {
        String s = key + Arrays.deepToString(args);
        if (!INSTANCES.containsKey(s)) {
            INSTANCES.put(s, new TranslatableText(key, args));
        }
        return INSTANCES.get(s);
    }

    @Override
    public String asString() {
        return Language.getValue(key);
    }

    @Override
    public String asFormattedString() {
        return String.format(asString(), params);
    }
}
