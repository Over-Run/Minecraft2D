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
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalTime;
import java.util.Map;

/**
 * @author squid233
 * @since 2020/10/24
 */
public class Logger {
    private static final Map<String, Logger> LOGGERS = new Object2ObjectArrayMap<>(2);
    private final String name;

    protected Logger(String name) {
        this.name = name;
    }

    public void logIfEnable(String msg, Level level, Object... params) {
        logIfEnable(msg, level, null, params);
    }

    public void logIfEnable(String msg, Level level, Throwable throwable, Object... params) {
        if (Options.getI(Options.LOG_LEVEL, Options.DEF_LOG_LEVEL) >= level.getLevel()) {
            for (Object o : params) {
                msg = msg.replaceFirst("\\{}", o.toString());
            }
            String thr = "";
            if (throwable != null) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                pw.println();
                throwable.printStackTrace(pw);
                pw.close();
                thr = sw.toString();
            }
            System.out.printf(
                    getFormat() + "\n",
                    LocalTime.now(),
                    Thread.currentThread().getName(),
                    level.getName(),
                    getName(),
                    msg,
                    thr
            );
        }
    }

    public void debug(String msg, Object... params) {
        logIfEnable(msg, Level.DEBUG, params);
    }

    public void info(String msg, Object... params) {
        logIfEnable(msg, Level.INFO, params);
    }

    public void warn(String msg, Object... params) {
        logIfEnable(msg, Level.WARN, params);
    }

    public void error(String msg, Object... params) {
        logIfEnable(msg, Level.ERROR, params);
    }

    public void fatal(String msg, Object... params) {
        logIfEnable(msg, Level.FATAL, params);
    }

    public void exception(String msg, Level level, Throwable throwable, Object... params) {
        logIfEnable(msg, level, params, throwable);
    }

    public void exception(String msg, Throwable throwable, Object... params) {
        exception(msg, Level.ERROR, throwable, params);
    }

    public String getName() {
        return name;
    }

    public static Logger getLogger(String name) {
        return LOGGERS.computeIfAbsent(name, Logger::new);
    }

    public static Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getSimpleName());
    }

    /**
     * Format of the logger.<br>
     * <ol>
     *     <li>Log time</li>
     *     <li>Current thread name</li>
     *     <li>Log level</li>
     *     <li>Logger name</li>
     *     <li>Log message</li>
     *     <li>Threw exception stack trace</li>
     * </ol>
     *
     * @return Format the logger.
     */
    public static String getFormat() {
        return Options.get(Options.LOGGER_FORMAT, Options.DEF_LOGGER_FORMAT);
    }
}
