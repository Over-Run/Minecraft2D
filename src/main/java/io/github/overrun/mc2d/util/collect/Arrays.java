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

package io.github.overrun.mc2d.util.collect;

import java.util.List;
import java.util.function.ObjIntConsumer;
import java.util.function.Predicate;

/**
 * @author squid233
 * @since 2020/12/05
 */
public final class Arrays {
    public static <T> boolean contains(T[] array, T t) {
        return java.util.Arrays.stream(array).anyMatch(Predicate.isEqual(t));
    }

    public static <T> boolean notContains(T[] array, T t) {
        return !contains(array, t);
    }

    public static <T> void forEach(int start, List<T> c, ObjIntConsumer<T> oic) {
        for (int i = start; i < c.size(); i++) {
            oic.accept(c.get(i), i);
        }
    }

    public static <T> void forEach(List<T> c, ObjIntConsumer<T> oic) {
        forEach(0, c, oic);
    }
}
