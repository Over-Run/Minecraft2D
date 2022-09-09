/*
 * MIT License
 *
 * Copyright (c) 2022 Overrun Organization
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

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The utils.
 *
 * @author squid233
 * @since 0.6.0
 */
public final class Utils {
    /**
     * Perform an action with the instance.
     *
     * @param t        the instance
     * @param function the function
     * @param <T>      the instance type
     * @param <R>      the return type
     * @return the value from the function
     */
    public static <T, R> R with(T t, Function<T, R> function) {
        return function.apply(t);
    }

    /**
     * Perform an action with the instance.
     *
     * @param t        the instance
     * @param consumer the consumer
     * @param <T>      the instance and return type
     * @return the value from the function
     */
    public static <T> T also(T t, Consumer<T> consumer) {
        consumer.accept(t);
        return t;
    }

    /**
     * Perform an action in a closure with the instance.
     *
     * @param t        the instance
     * @param consumer the consumer
     * @param <T>      the instance type
     */
    public static <T> void let(T t, Consumer<T> consumer) {
        consumer.accept(t);
    }
}
