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

import io.github.overrun.mc2d.lang.Language;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;

import static io.github.overrun.mc2d.util.Constants.FALSE;

/**
 * @author squid233
 * @since 2020/09/15
 */
public class Options {
    public static final Properties OPTIONS = new Properties(6);

    public static final String FPS = "fps";
    public static final String VIEW_DISTANCE = "view_distance";
    public static final String DEBUGGING = "debugging";
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String LANG = "lang";

    public static final String FPS_DEF = "60";
    public static final String VIEW_DISTANCE_DEF = "4";
    public static final String DEBUGGING_DEF = FALSE;
    public static final String WIDTH_DEF = "1040";
    public static final String HEIGHT_DEF = "486";
    public static final String LANG_DEF = Language.EN_US.getCode();

    static {
        try (Reader r = new FileReader("options.properties")) {
            OPTIONS.load(r);
        } catch (IOException e) {
            OPTIONS.putAll(Map.of(
                    FPS, FPS_DEF,
                    WIDTH, WIDTH_DEF,
                    VIEW_DISTANCE, VIEW_DISTANCE_DEF,
                    DEBUGGING, FALSE,
                    LANG, LANG_DEF,
                    HEIGHT, HEIGHT_DEF));
            try {
                Writer w = new FileWriter("options.properties");
                OPTIONS.store(w, null);
            } catch (IOException ee) {
                ee.printStackTrace();
            }
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

    public static void set(String k, String v) {
        OPTIONS.setProperty(k, v);
        Reader r = null;
        try (Writer w = new FileWriter("options.properties")) {
            r = new FileReader("options.properties");
            OPTIONS.load(r);
            OPTIONS.store(w, null);
            OPTIONS.load(r);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (r != null) {
                    r.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
