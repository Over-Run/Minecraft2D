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

import java.io.*;
import java.util.Properties;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;

/**
 * @author squid233
 * @since 2021/01/23
 */
public final class Options {
    private static final Logger logger = LogFactory9.getLogger();
    private static final File FILE = new File("options.txt");

    ///////////////////////////////////////////////////////////////////////////
    // Keybindings
    ///////////////////////////////////////////////////////////////////////////

    public static final String KEY_ITEM_GROUP = "key.itemGroup";

    public static final String LANG = "lang";
    public static final String GUI_SCALE = "guiScale";
    public static final String VSYNC = "vsync";

    public final Properties options = new Properties();

    public Options() {
        if (!FILE.exists()) {
            init();
            save();
        }
        try (var r = new BufferedInputStream(new FileInputStream(FILE))) {
            options.load(r);
            init();
            save();
        } catch (IOException e) {
            logger.error("Catching loading options", e);
        }
    }

    private void init() {
        putIfAbsent(KEY_ITEM_GROUP, GLFW_KEY_E);
        putIfAbsent(LANG, "en_us");
        putIfAbsent(GUI_SCALE, 2.0);
        putIfAbsent(VSYNC, true);
    }

    private void putIfAbsent(String key, Object value) {
        options.putIfAbsent(key, String.valueOf(value));
    }

    public void save() {
        try (var os = new BufferedOutputStream(new FileOutputStream(FILE))) {
            options.store(os, null);
        } catch (IOException e) {
            logger.error("Catching saving options", e);
        }
    }

    public boolean getB(String key, boolean def) {
        var value = options.getProperty(key);
        return value == null ? def : Boolean.parseBoolean(value);
    }

    public int getI(String key, int def) {
        var value = options.getProperty(key);
        return value == null ? def : Integer.parseInt(value);
    }

    public double getD(String key, double def) {
        var value = options.getProperty(key);
        return value == null ? def : Double.parseDouble(value);
    }

    public String get(String key, String def) {
        return options.getProperty(key, def);
    }
}
