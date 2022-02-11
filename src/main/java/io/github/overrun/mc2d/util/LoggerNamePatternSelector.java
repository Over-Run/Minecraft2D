/*
 * MIT License
 *
 * Copyright (c) 2017-2022 Overrun Organization
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

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.layout.PatternMatch;
import org.apache.logging.log4j.core.layout.PatternSelector;
import org.apache.logging.log4j.core.pattern.PatternFormatter;
import org.apache.logging.log4j.core.pattern.PatternParser;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link PatternSelector} that selects patterns based on the logger name.
 * Can be used to log messages from different loggers using different patterns.
 *
 * <p>Multiple logger names may be separated using comma in the
 * {@link PatternMatch#getKey() PatternMatch "key"}. The pattern will be applied
 * if the logger name matches at least one of them.</p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code  <PatternLayout>
 *     <LoggerNamePatternSelector defaultPattern="[%d{HH:mm:ss} %level] [%logger]: %msg%n">
 *         <!-- Log root (empty logger name), "Main", and net.minecrell.* without logger prefix -->
 *         <PatternMatch key=",Main,net.minecrell." pattern="[%d{HH:mm:ss} %level]: %msg%n"/>
 *         <PatternMatch key="com.example.Logger" pattern="EXAMPLE: %msg%n"/>
 *     </LoggerNamePatternSelector>
 * </PatternLayout>}</pre>
 *
 * @author Minecrell
 * @since 2020/12/21
 */
@SuppressWarnings("unused")
@Plugin(name = "LoggerNamePatternSelector", category = Node.CATEGORY, elementType = PatternSelector.ELEMENT_TYPE)
public final class LoggerNamePatternSelector implements PatternSelector {
    private static final class LoggerNameSelector {
        private final String name;
        private final boolean isPackage;
        private final PatternFormatter[] formatters;

        private LoggerNameSelector(String name, PatternFormatter[] formatters) {
            this.name = name;
            isPackage = name.endsWith(".");
            this.formatters = formatters;
        }

        private boolean test(String s) {
            return isPackage ? s.startsWith(name) : s.equals(name);
        }
    }

    private final PatternFormatter[] defaultFormatters;
    private final List<LoggerNameSelector> formatters = new ArrayList<>();

    /**
     * Constructs a new {@link LoggerNamePatternSelector}.
     *
     * @param defaultPattern The default pattern to use if no logger name matches
     * @param properties The pattern match rules to use
     * @param alwaysWriteExceptions Write exceptions even if pattern does not
     *     include exception conversion
     * @param disableAnsi If true, disable all ANSI escape codes
     * @param noConsoleNoAnsi If true and {@link System#console()} is null,
     *     disable ANSI escape codes
     * @param config The configuration
     */
    protected LoggerNamePatternSelector(String defaultPattern,
                                        PatternMatch[] properties,
                                        boolean alwaysWriteExceptions,
                                        boolean disableAnsi,
                                        boolean noConsoleNoAnsi,
                                        Configuration config) {
        PatternParser parser = PatternLayout.createPatternParser(config);
        PatternFormatter[] emptyFormatters = new PatternFormatter[0];
        defaultFormatters = parser.parse(defaultPattern, alwaysWriteExceptions, disableAnsi, noConsoleNoAnsi)
                .toArray(emptyFormatters);
        for (PatternMatch property : properties) {
            PatternFormatter[] formatters = parser.parse(property.getPattern(), alwaysWriteExceptions, disableAnsi, noConsoleNoAnsi)
                    .toArray(emptyFormatters);
            for (String name : property.getKey().split(",")) {
                this.formatters.add(new LoggerNameSelector(name, formatters));
            }
        }
    }

    @Override
    public PatternFormatter[] getFormatters(LogEvent event) {
        final @Nullable String loggerName = event.getLoggerName();
        if (loggerName != null) {
            for (LoggerNameSelector selector : formatters) {
                if (selector.test(loggerName)) {
                    return selector.formatters;
                }
            }
        }
        return defaultFormatters;
    }

    /**
     * Creates a new {@link LoggerNamePatternSelector}.
     *
     * @param defaultPattern The default pattern to use if no logger name matches
     * @param properties The pattern match rules to use
     * @param alwaysWriteExceptions Write exceptions even if pattern does not
     *     include exception conversion
     * @param disableAnsi If true, disable all ANSI escape codes
     * @param noConsoleNoAnsi If true and {@link System#console()} is null,
     *     disable ANSI escape codes
     * @param config The configuration
     * @return The new pattern selector
     */
    @PluginFactory
    public static LoggerNamePatternSelector createSelector(
            @Required(message = "Default pattern is required") @PluginAttribute(value = "defaultPattern") String defaultPattern,
            @PluginElement("PatternMatch") PatternMatch[] properties,
            @PluginAttribute(value = "alwaysWriteExceptions", defaultBoolean = true) boolean alwaysWriteExceptions,
            @PluginAttribute("disableAnsi") boolean disableAnsi,
            @PluginAttribute("noConsoleNoAnsi") boolean noConsoleNoAnsi,
            @PluginConfiguration Configuration config) {
        return new LoggerNamePatternSelector(defaultPattern, properties, alwaysWriteExceptions, disableAnsi, noConsoleNoAnsi, config);
    }
}
