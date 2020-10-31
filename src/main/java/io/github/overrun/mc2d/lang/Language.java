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
import io.github.overrun.mc2d.util.ResourceLocation;
import io.github.overrun.mc2d.util.YamlReader;

import static io.github.overrun.mc2d.Minecraft2D.LOGGER;

/**
 * @author squid233
 * @since 2020/10/13
 */
public class Language {
    public static final Language EN_US = new Language("en_us", "English (United States)");
    public static final Language ZH_CN = new Language("zh_cn", "中文（简体）");
    private static final YamlReader YAML_READER = new YamlReader();
    private static String curLang = Options.get("lang", EN_US.getCode());
    private final String code;
    private final String name;

    public Language(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static void reload() {
        YAML_READER.setPath(ResourceLocation.asString("lang/" + curLang + ".lang"));
        LOGGER.info("Setting language to: {}", curLang);
    }

    public static void setLang(Language lang) {
        Language.setCurrentLang(lang.getCode());
        Language.reload();
        Options.set(Options.LANG, lang.getCode());
    }

    public static String getValue(String translationKey) {
        return YAML_READER.get(translationKey, translationKey).toString();
    }

    public static void setCurrentLang(String currentLang) {
        curLang = currentLang;
    }

    public static String getCurrentLang() {
        return curLang;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getCode();
    }
}
