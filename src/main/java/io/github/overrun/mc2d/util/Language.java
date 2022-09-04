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

package io.github.overrun.mc2d.util;

import io.github.overrun.mc2d.mod.ModLoader;
import org.overrun.swgl.core.util.LogFactory9;
import org.slf4j.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * @author squid233
 * @since 2021/01/29
 */
public final class Language {
    private static final Logger logger = LogFactory9.getLogger();
    public static final String EN_US = "en_us";
    public static final String ZH_CN = "zh_cn";
    public static String currentLang;
    private static final String[] LANGS_STR = {EN_US, ZH_CN};
    private static final Map<String, Map<String, String>> LANGS = new HashMap<>(2);

    static {
        for (String lang : LANGS_STR) {
            LANGS.put(lang, new HashMap<>());
        }
    }

    public static void init() {
        for (Map.Entry<String, Map<String, String>> entry : LANGS.entrySet()) {
            try (InputStream is = Objects.requireNonNull(
                ClassLoader.getSystemResourceAsStream(
                        "assets/" + Identifier.DEFAULT + "/lang/" + entry.getKey() + ".lang"
                ));
                 Reader r = new InputStreamReader(is, StandardCharsets.UTF_8)
            ) {
                Properties prop = new Properties();
                prop.load(r);
                putKv(entry.getKey(), prop);
            } catch (Throwable t) {
                logger.error("Catching loading language file", t);
            }
            for (String namespace : ModLoader.getMods().keySet()) {
                try (InputStream is = Objects.requireNonNull(
                    ModLoader.getLoader(namespace).getResourceAsStream(
                        "assets/" + namespace + "/lang/" + entry.getKey() + ".lang"
                    ));
                     Reader r = new InputStreamReader(is, StandardCharsets.UTF_8)
                ) {
                    Properties prop = new Properties();
                    prop.load(r);
                    putKv(entry.getKey(), prop);
                } catch (Throwable ignored) {
                }
            }
        }
    }

    private static void putKv(String lang, Properties prop) {
        for (Map.Entry<Object, Object> e : prop.entrySet()) {
            LANGS.get(lang).put(String.valueOf(e.getKey()), String.valueOf(e.getValue()));
        }
    }

    public static String getByKey(String key) {
        return LANGS.get(currentLang).getOrDefault(key, key);
    }
}
