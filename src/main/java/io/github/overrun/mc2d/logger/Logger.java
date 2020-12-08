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

package io.github.overrun.mc2d.logger;

import io.github.overrun.mc2d.option.Options;

import java.io.IOException;

import java.util.logging.FileHandler;
import java.util.logging.Level;

import static java.util.logging.Logger.getLogger;

/**
 * @author squid233
 * @since 2020/10/24
 */
public class Logger {
    private final java.util.logging.Logger logger;

    public Logger(String name) {
        this.logger = getLogger(name);
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.CONFIG);
        Formatter f = new Formatter();
        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(logDebugEnable() ? Level.CONFIG : Level.INFO);
        ch.setFormatter(f);
        logger.addHandler(ch);
        try {
            FileHandler fh = new FileHandler("latest.log");
            fh.setLevel(Level.INFO);
            fh.setFormatter(f);
            logger.addHandler(fh);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileHandler fh = new FileHandler("latest-debug.log");
            fh.setLevel(Level.CONFIG);
            fh.setFormatter(f);
            logger.addHandler(fh);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Logger(Class<?> clazz) {
        this(clazz.getSimpleName());
    }

    public void info(String msg) {
        getJavaLogger().info(msg);
    }

    public void info(String msg, Object... params) {
        getJavaLogger().log(Level.INFO, msg, params);
    }

    public void warn(String msg) {
        getJavaLogger().warning(msg);
    }

    public void warn(String msg, Object... params) {
        getJavaLogger().log(Level.WARNING, msg, params);
    }

    public void error(String msg) {
        getJavaLogger().severe(msg);
    }

    public void error(String msg, Object... params) {
        getJavaLogger().log(Level.SEVERE, msg, params);
    }

    public void debug(String msg) {
        if (logDebugEnable())
            getJavaLogger().config(msg);
    }

    public void debug(String msg, Object... params) {
        if (logDebugEnable())
            getJavaLogger().log(Level.CONFIG, msg, params);
    }

    public void exception(String msg, Throwable throwable) {
        getJavaLogger().log(Level.SEVERE, msg, throwable);
    }

    public java.util.logging.Logger getJavaLogger() {
        return logger;
    }

    public static boolean logDebugEnable() {
        return Options.getB(Options.DEBUG);
    }
}
