/*
 * MIT License
 *
 * Copyright (c) 2020-2021 Over-Run
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

package io.github.overrun.mc2d.client.gui;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * @author squid233
 * @since 2021/01/25
 */
public interface ParentElement extends Element {
    List<? extends Element> children();

    default Optional<Element> hoveredElement(int mouseX, int mouseY) {
        Iterator<? extends Element> iterator = children().iterator();
        Element element;
        do {
            if (!iterator.hasNext()) {
                return Optional.empty();
            }
            element = iterator.next();
        } while (!element.isMouseOver(mouseX, mouseY));
        return Optional.of(element);
    }

    @Override
    default boolean mousePressed(int mouseX, int mouseY, int button) {
        Iterator<? extends Element> iterator = children().iterator();
        Element element;
        do {
            if (!iterator.hasNext()) {
                return false;
            }
            element = iterator.next();
        } while (!element.mousePressed(mouseX, mouseY, button));
        return true;
    }
}
