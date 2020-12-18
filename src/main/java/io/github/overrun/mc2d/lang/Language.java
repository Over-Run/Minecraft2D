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

package io.github.overrun.mc2d.lang;

import io.github.overrun.mc2d.option.Options;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Properties;

import static io.github.overrun.mc2d.util.Utils.getResource;
import static java.lang.String.valueOf;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author squid233
 * @since 2020/10/13
 */
public final class Language {
    private static final Map<String, String> K2V_EN_US = new Object2ObjectArrayMap<>(14);
    private static final Map<String, String> K2V_ZH_CN = new Object2ObjectArrayMap<>(14);
    public static final Map<String, Map<String, String>> LANG
            = Map.ofEntries(
            new SimpleEntry<>("en_us", K2V_EN_US),
            new SimpleEntry<>("zh_cn", K2V_ZH_CN)
    );

    public static void load(String namespace) {
        for (String l : LANG.keySet()) {
            try (Reader r = new InputStreamReader(getResource(namespace + ":lang/" + l + ".lang"), UTF_8)) {
                Properties p = new Properties(6);
                p.load(r);
                p.forEach((key, value) -> LANG.get(l).put(valueOf(key), valueOf(value)));
            } catch (IOException ignored) {}
        }
    }

    public static String get(String key, String def) {
        return getByLocale(Options.get(Options.LANG, Options.DEF_LANG), key, def);
    }

    public static String get(String key) {
        return get(key, key);
    }

    public static String getByLocale(String locale, String key, String def) {
        return LANG.get(locale).getOrDefault(key, def);
    }

    public static String getByLocale(String locale, String key) {
        return getByLocale(locale, key, key);
    }
}
