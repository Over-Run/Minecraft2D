/*
 * MIT License
 *
 * Copyright (c) 2020-2021 Over-Run
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Properties;

/**
 * @author squid233
 * @since 2021/01/23
 */
public final class Options {
    public static final Properties OPTIONS = new Properties(7);
    public static final String BLOCK_HIGHLIGHT = "block.highlight";
    public static final String KEY_CREATIVE_TAB = "key.creativeTab";
    private static final Logger logger = LogManager.getLogger();

    public static void init() {
        File file = new File("options.txt");
        if (!file.exists()) {
            try (OutputStream os = new FileOutputStream(file)) {
                OPTIONS.put(KEY_CREATIVE_TAB, "E");
                OPTIONS.put(BLOCK_HIGHLIGHT, "false");
                OPTIONS.store(os, null);
            } catch (IOException e) {
                logger.catching(e);
            }
        }
        try (Reader r = new FileReader(file)) {
            OPTIONS.load(r);
        } catch (IOException e) {
            logger.catching(e);
        }
    }

    public static boolean getB(String key, String def) {
        return OPTIONS.containsKey(key)
                ? Boolean.parseBoolean(OPTIONS.getProperty(key))
                : Boolean.parseBoolean(def);
    }

    public static String get(String key, String def) {
        return OPTIONS.getProperty(key, def);
    }
}
