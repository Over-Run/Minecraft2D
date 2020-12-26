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

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

import static io.github.overrun.mc2d.Minecraft2D.logger;

/**
 * @author squid233
 * @since 2020/09/15
 */
public final class Options {
    private static final Properties OPTIONS = new Properties(3);
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String LANG = "lang";
    public static final String FPS = "fps";

    public static final int DEF_WIDTH = 854;
    public static final int DEF_HEIGHT = 480;
    public static final String DEF_LANG = "en_us";
    public static final int DEF_FPS = 60;

    static {
        File f = new File("options.txt");
        if (!f.exists()) {
            try (FileWriter fw = new FileWriter(f)) {
                OPTIONS.put(WIDTH, DEF_WIDTH);
                OPTIONS.put(HEIGHT, DEF_HEIGHT);
                OPTIONS.put(LANG, DEF_LANG);
                OPTIONS.put(FPS, DEF_FPS);
                OPTIONS.store(fw, null);
            } catch (IOException e) {
                logger.error("Cannot write options!", e);
            }
        }
        try (FileReader fr = new FileReader(f)) {
            OPTIONS.load(fr);
        } catch (IOException e) {
            logger.error("Cannot read options!", e);
        }
    }

    public static void save() {
        try (Writer w = new FileWriter("options.txt")) {
            OPTIONS.store(w, null);
        } catch (IOException e) {
            logger.error("Cannot save options!", e);
        }
    }

    public static void set(String k, String v) {
        OPTIONS.setProperty(k, v);
    }

    public static void setAndSave(String k, String v) {
        set(k, v);
        save();
    }

    public static String get(String k) {
        return OPTIONS.getProperty(k);
    }

    public static String get(String k ,String def) {
        return OPTIONS.getProperty(k, def);
    }

    public static int getI(String k, int def) {
        return Integer.parseInt(get(k, String.valueOf(def)));
    }
}
