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

import java.time.LocalTime;
import java.util.logging.LogRecord;

import static io.github.overrun.mc2d.util.Utils.newLine;

/**
 * @author squid233
 * @since 2020/11/22
 */
public class Formatter extends java.util.logging.Formatter {
    @Override
    public String format(LogRecord record) {
        String level;
        switch (record.getLevel().getName()) {
            case "SEVERE":
                level = "ERROR";
                break;
            case "WARNING":
                level = "WARN";
                break;
            case "CONFIG":
                level = "DEBUG";
                break;
            default:
                level = record.getLevel().getName();
        }
        String msg = record.getMessage();
        if (record.getParameters() != null) {
            for (Object o : record.getParameters()) {
                msg = msg.replaceFirst("\\{}", String.valueOf(o));
            }
        }
        StringBuilder sb = new StringBuilder("[")
                .append(LocalTime.now().toString().split("\\.")[0])
                .append("] [")
                .append(Thread.currentThread().getName())
                .append("/")
                .append(level)
                .append("] (")
                .append(record.getLoggerName())
                .append(") ")
                .append(msg);
        if (record.getThrown() != null) {
            Throwable t = record.getThrown();
            sb.append(" Caused by: ")
                    .append(t)
                    .append(newLine());
            for (StackTraceElement ste : t.getStackTrace()) {
                sb.append(newLine())
                        .append("\tat ")
                        .append(ste);
            }
        }
        sb.append(newLine());
        return sb.toString();
    }
}
