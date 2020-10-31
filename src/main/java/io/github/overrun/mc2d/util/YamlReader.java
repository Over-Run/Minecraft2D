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

package io.github.overrun.mc2d.util;

import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.util.LinkedHashMap;

/**
 * @author squid233
 * @since 2020/10/24
 */
public class YamlReader {
    private String path;

    public YamlReader(String path) { this.path = path; }

    public YamlReader() {}

    public Object get(String key) {
        Yaml yaml = new Yaml();
        LinkedHashMap<String, Object> map = yaml.load(ClassLoader.getSystemResourceAsStream(path));
        String[] keys;
        if (key.contains(".")) { keys = StringUtils.split(key, '.'); }
        else { return map.get(key); }
        LinkedHashMap<String, Object> finalV = new LinkedHashMap<>(keys.length);
        for (int i = 0; i < keys.length; i++) {
            if (i == 0) { finalV = (LinkedHashMap<String, Object>) map.get(keys[i]); }
            else {
                if (finalV.get(keys[i]) instanceof String) { return finalV.get(keys[i]); }
                else { finalV = (LinkedHashMap<String, Object>) finalV.get(keys[i]); }
            }
        } return finalV == null ? null : finalV.get(keys[keys.length - 1]);
    }

    public Object get(String key, Object def) { return get(key) == null ? def : get(key); }

    public String getPath() { return path; }

    public YamlReader setPath(String path) { this.path = path; return this; }
}
