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

package io.github.overrun.mc2d.input;

import it.unimi.dsi.fastutil.chars.Char2ObjectArrayMap;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Consumer;

/**
 * @author squid233
 * @since 2020/11/22
 */
public final class KeyBinding {
    public static final Char2ObjectArrayMap<KeyBinding> KEY_BINDINGS = new Char2ObjectArrayMap<>(2);
    public static final int PRESS = 0;
    public static final int TYPED = 1;
    public static final int RELEASE = 2;

    private final char key;
    private final int type;
    private final Consumer<KeyBinding> operation;

    public KeyBinding(char key, int type, Consumer<KeyBinding> onTyping) {
        this.key = key;
        this.type = type;
        operation = onTyping;
    }

    public KeyBinding(char key, Consumer<KeyBinding> onTyping) {
        this(key, TYPED, onTyping);
    }

    public char getKey() {
        return key;
    }

    public int getType() {
        return type;
    }

    public Consumer<KeyBinding> onTyping() {
        return operation;
    }

    public static void bindKey(KeyBinding keyBinding) {
        KEY_BINDINGS.put(keyBinding.key, keyBinding);
    }

    public static KeyBinding getKeyBinding(char keyChar) {
        return KEY_BINDINGS.get(keyChar);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        KeyBinding that = (KeyBinding) o;
        return getKey() == that.getKey() && getType() == that.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), getType());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", KeyBinding.class.getSimpleName() + "[", "]")
                .add("key=" + key)
                .add("type=" + type)
                .add("operation=" + operation)
                .toString();
    }
}
