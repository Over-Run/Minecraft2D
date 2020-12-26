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

package io.github.overrun.mc2d.event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author squid233
 * @since 2020/12/20
 */
final class ArrayBackedEvent<T> extends Event<T> {
    private final Function<List<T>, T> invokerFactory;
    private final T dummyInvoker;
    private List<T> handlers;

    ArrayBackedEvent(T dummyInvoker, Function<List<T>, T> invokerFactory) {
        this.dummyInvoker = dummyInvoker;
        this.invokerFactory = invokerFactory;
        update();
    }

    void update() {
        if (handlers == null) {
            if (dummyInvoker != null) {
                invoker = dummyInvoker;
            } else {
                invoker = invokerFactory.apply(List.of());
            }
        } else if (handlers.size() == 1) {
            invoker = handlers.get(0);
        } else {
            invoker = invokerFactory.apply(handlers);
        }
    }

    @Override
    public void register(T listener) {
        if (listener == null) {
            throw new NullPointerException("Tried to register a null listener!");
        }
        if (handlers == null) {
            handlers = List.of(listener);
        } else {
            handlers = new ArrayList<>(handlers);
            handlers.set(handlers.size() - 1, listener);
        }
        update();
    }
}
