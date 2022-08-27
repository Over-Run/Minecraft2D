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

package io.github.overrun.mc2d.mod;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * @author squid233
 * @since 0.6.0
 */
public class ModInfoFile {
    private String namespace;
    private String name;
    private String version;
    private String main;

    public static final class Serializer extends TypeAdapter<ModInfoFile> {
        @Override
        public void write(JsonWriter out, ModInfoFile value) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public ModInfoFile read(JsonReader in) throws IOException {
            var file = new ModInfoFile();
            in.beginObject();
            while (in.hasNext()) {
                switch (in.nextName()) {
                    case "namespace" -> file.namespace = in.nextString();
                    case "name" -> file.name = in.nextString();
                    case "version" -> file.version = in.nextString();
                    case "main" -> file.main = in.nextString();
                }
            }
            in.endObject();
            return file;
        }
    }

    public String getNamespace() {
        return namespace;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getMain() {
        return main;
    }
}
