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
import org.apache.commons.lang3.StringUtils;

import java.time.LocalTime;

/**
 * @author squid233
 * @since 2020/09/15
 */
public class Logger {
    private final String name;
    public static final String L_INFO = "INFO";
    public static final String L_ERROR = "ERROR";
    public static final String L_DEBUG = "DEBUG";

    public Logger() {
        String[] s = StringUtils.split(new Throwable().getStackTrace()[1].getClassName(), '.');
        this.name = s[s.length - 1];
    }

    public Logger(Class<?> clazz) {
        this.name = clazz.getSimpleName();
    }

    public void msg(String msg, String level) {
        System.out.println("[" + StringUtils.split(LocalTime.now().toString(), '.')[0]
                + "][" + Thread.currentThread().getName() + "/" + level + "](" + name + ") " + msg);
    }

    public void info(String msg) {
        msg(msg, L_INFO);
    }

    public void error(String msg) {
        msg(msg, L_ERROR);
    }

    public void debug(String msg) {
        if (Options.getB(Options.DEBUGGING)) {
            msg(msg, L_DEBUG);
        }
    }
}
