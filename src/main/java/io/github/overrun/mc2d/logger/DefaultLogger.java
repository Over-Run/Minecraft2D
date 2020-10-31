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

import static io.github.overrun.mc2d.logger.DefaultLogger.Level.*;

/**
 * @author squid233
 * @since 2020/09/15
 */
public class DefaultLogger implements Logger {
    private static final Object[] E_M = new Object[0];
    private final String name;

    public void msg(String msg, Level level, Object... params) {
        String dmp = msg;
        if (params != null && params.length > 0) {
            for (Object o : params) { dmp = dmp.replaceFirst("\\{}", o.toString()); }
        }
        System.out.println(Style.C_LIGHT_BLUE + "[" + StringUtils.split(LocalTime.now().toString(), '.')[0] + "]" + level.color
                + " [" + Thread.currentThread().getName() + "/" + level + "]" + Style.CLEAR + " (" + name + ") "
                + level.msgColor + dmp + Style.CLEAR
        );
    }

    public void msg(String msg, Level level) {
        msg(msg, level, E_M);
    }

    DefaultLogger(String name) {
        this.name = name;
    }

    @Override
    public void info(String msg) {
        msg(msg, INFO);
    }

    @Override
    public void info(String msg, Object... params) {
        msg(msg, INFO, params);
    }

    @Override
    public void warn(String msg) {
        msg(msg, WARN);
    }

    @Override
    public void warn(String msg, Object... params) {
        msg(msg, WARN, params);
    }

    @Override
    public void error(String msg) {
        msg(msg, ERROR);
    }

    @Override
    public void error(String msg, Object... params) {
        msg(msg, ERROR, params);
    }

    @Override
    public void fatal(String msg) {
        msg(msg, FATAL);
    }

    @Override
    public void fatal(String msg, Object... params) {
        msg(msg, FATAL, params);
    }

    @Override
    public void debug(String msg) {
        if (Options.getB(Options.DEBUGGING, false)) {
            msg(msg, DEBUG);
        }
    }

    @Override
    public void debug(String msg, Object... params) {
        if (Options.getB(Options.DEBUGGING, false)) {
            msg(msg, DEBUG, params);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    public interface Style {
        String NONE = "";
        String CLEAR = "\033[0m";
        String BOLD = "\033[1m";
        String UNDERLINE = "\033[4m";
        String INVERSE_COLOR = "\033[7m";
        String C_WHITE = "\033[30m";
        String C_RED = "\033[31m";
        String C_GREEN = "\033[32m";
        String C_YELLOW = "\033[33m";
        String C_BLUE = "\033[34m";
        String C_PURPLE = "\033[35m";
        String C_LIGHT_BLUE = "\033[36m";
        String C_GRAY = "\033[37m";
        String CF_RED = "\033[91m";
        String CF_GREEN = "\033[92m";
    }

    enum Level {
        /**
         * ?
         */
        INFO(Style.C_GREEN, Style.NONE),
        WARN(Style.C_YELLOW),
        ERROR(Style.CF_RED),
        FATAL(Style.C_RED),
        DEBUG(Style.CF_GREEN, Style.CF_GREEN);

        private final String color;
        private final String msgColor;

        Level(String color) {
            this(color, color);
        }

        Level(String color, String msgColor) {
            this.color = color;
            this.msgColor = msgColor;
        }
    }
}
