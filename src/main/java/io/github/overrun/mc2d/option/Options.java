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

package io.github.overrun.mc2d.option;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * @author squid233
 * @since 2020/09/15
 */
public class Options {
    public static final Properties OPTIONS = new Properties(5);

    public static final String FPS_OPT = "fps";
    public static final String VIEW_DISTANCE = "view_distance";
    public static final String DEBUGGING = "debugging";
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String LANG = "lang";

    static {
        try {
            OPTIONS.load(new FileReader("options.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key, String def) {
        return OPTIONS.getProperty(key, def);
    }

    public static String get(String key) {
        return get(key, "null");
    }

    public static int getI(String key, int def) {
        return Integer.parseInt(get(key, String.valueOf(def)));
    }

    public static int getI(String key) {
        return getI(key, 0);
    }

    public static boolean getB(String key, boolean def) {
        return Boolean.parseBoolean(get(key, String.valueOf(def)));
    }

    public static boolean getB(String key) {
        return getB(key, false);
    }
}
