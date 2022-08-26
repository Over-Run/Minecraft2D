/*
 * MIT License
 *
 * Copyright (c) 2020-2022 Overrun Organization
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

package io.github.overrun.mc2d;

import io.github.overrun.mc2d.client.Mc2dClient;
import io.github.overrun.mc2d.util.Options;
import org.overrun.swgl.core.util.LogFactory9;
import org.slf4j.Logger;

/**
 * @author squid233
 * @since 2021/01/07
 */
public final class Main {
    // todo: mods and options screen

    private static final Logger logger = LogFactory9.getLogger();

    public static void main(String[] args) {
        Options.init();
        logger.info("Loading for game Minecraft2D {}", Mc2dClient.VERSION);
        try (var client = Mc2dClient.getInstance()) {
            client.init();
            client.run();
        }
    }
}
