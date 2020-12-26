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

/**
 * The event object.
 * <p>The standard event callback should like this below:<br>
 * <pre><code>Event&lt;[event name]CallBack&gt; EVENT = EventFactory.createArrayBacked(
        listeners -> button -> {
            for ([event name]CallBack callback : listeners) {
                callback.on[event name]([params]);
            }
        }
);

static void post([params]) {
    EVENT.invoker().on[event name]([params]);
}

void on[event name]([params]);</code></pre>
 * </p>
 *
 * @author FabricMC
 * @author squid233
 * @since 2020/12/16
 * @see EventFactory
 */
public abstract class Event<T> {
    /**
     * The invoker field. This should be updated by the implementation to
     * always refer to an instance containing all code that should be
     * executed upon event emission.
     */
    protected T invoker;

    /**
     * Returns the invoker instance.
     *
     * <p>An "invoker" is an object which hides multiple registered
     * listeners of type T under one instance of type T, executing
     * them and leaving early as necessary.</p>
     *
     * @return The invoker instance.
     */
    public final T invoker() {
        return invoker;
    }

    /**
     * Register a listener to the event.
     *
     * @param listener The desired listener.
     */
    public abstract void register(T listener);
}
