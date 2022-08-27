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

import org.overrun.swgl.core.util.LogFactory9;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;

/**
 * @author squid233
 * @since 2021/01/23
 */
public final class Options {
    public static final Properties OPTIONS = new Properties();
    private static final File FILE = new File("options.txt");
    public static final String KEY_CREATIVE_TAB = "key.creativeTab";
    public static final String LANG = "lang";
    public static final String GUI_SCALE = "guiScale";
    private static final Logger logger = LogFactory9.getLogger();

    private static void put(String key, int value) {
        OPTIONS.put(key, String.valueOf(value));
    }

    private static void put(String key, double value) {
        OPTIONS.put(key, String.valueOf(value));
    }

    public static void save() {
        try (var os = new FileOutputStream(FILE)) {
            OPTIONS.store(os, null);
        } catch (IOException e) {
            logger.error("Catching", e);
        }
    }

    public static void init() {
        if (!FILE.exists()) {
            put(KEY_CREATIVE_TAB, GLFW_KEY_E);
            OPTIONS.put(LANG, "en_us");
            put(GUI_SCALE, 2.0);
            save();
        }
        try (var r = new FileReader(FILE)) {
            OPTIONS.load(r);
            save();
        } catch (IOException e) {
            logger.error("Catching", e);
        }
    }

    public static boolean getB(String key, String def) {
        return OPTIONS.containsKey(key)
            ? Boolean.parseBoolean(OPTIONS.getProperty(key))
            : Boolean.parseBoolean(def);
    }

    public static int getI(String key, int def) {
        var value = OPTIONS.getProperty(key);
        if (value == null) {
            return def;
        }
        return Integer.parseInt(value);
    }

    public static double getD(String key, double def) {
        var value = OPTIONS.getProperty(key);
        if (value == null) {
            return def;
        }
        return Double.parseDouble(value);
    }

    public static String get(String key, String def) {
        return OPTIONS.getProperty(key, def);
    }
}
