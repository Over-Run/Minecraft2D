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

package io.github.squid233.serialization;

/**
 * @author squid233
 * @since 2020/10/03
 */
public class Lifecycle {
    private static final Lifecycle STABLE = new Lifecycle() {
        @Override
        public String toString() {
            return "Stable";
        }
    };
    private static final Lifecycle EXPERIMENTAL = new Lifecycle() {
        @Override
        public String toString() {
            return "Experimental";
        }
    };

    private Lifecycle() {
    }

    public static final class Deprecated extends Lifecycle {
        private final int since;

        public Deprecated(final int since) {
            this.since = since;
        }

        public int since() {
            return since;
        }
    }

    public static Lifecycle experimental() {
        return EXPERIMENTAL;
    }

    public static Lifecycle stable() {
        return STABLE;
    }

    public static Lifecycle deprecated(final int since) {
        return new Deprecated(since);
    }

    public Lifecycle add(final Lifecycle other) {
        if (this == EXPERIMENTAL || other == EXPERIMENTAL) {
            return EXPERIMENTAL;
        }
        if (this instanceof Deprecated) {
            if (other instanceof Deprecated && ((Deprecated) other).since < ((Deprecated) this).since) {
                return other;
            }
            return this;
        }
        if (other instanceof Deprecated) {
            return other;
        }
        return STABLE;
    }
}
