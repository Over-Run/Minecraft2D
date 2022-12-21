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

package io.github.overrun.mc2d.world.ibt;

import io.github.overrun.mc2d.util.Utils;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static io.github.overrun.mc2d.world.ibt.IBTType.*;

/**
 * The default implementation of IBT.
 *
 * @author squid233
 * @since 0.6.0
 */
public class BinaryTag implements IBinaryTag {
    private static final Pattern INDEX_REGEX = Pattern.compile("(.*)\\[(\\d+)]");
    private final Map<String, IBTValue> mappings = new HashMap<>();

    @Override
    public void serialize(ObjectOutput out) throws IOException {
        out.writeByte(TAG.getSerialFlag());
        out.writeInt(getMappings().size());
        for (var e : getMappings().entrySet()) {
            var k = e.getKey();
            var v = e.getValue();
            if (k != null && v != null) {
                out.writeUTF(k);
                v.serialize(out);
            }
        }
    }

    /**
     * Deserialize from bytes.
     *
     * @param in the input
     * @return the binary tag
     * @throws IOException            the IOException
     * @throws ClassNotFoundException if class not found
     */
    public static BinaryTag deserialize(ObjectInput in) throws IOException, ClassNotFoundException {
        var tag = new BinaryTag();
        var map = tag.getMappings();
        in.readByte();
        for (int i = 0, sz = in.readInt(); i < sz; i++) {
            var k = in.readUTF();
            map.put(k, IBTValue.deserialize(in));
        }
        return tag;
    }

    @Override
    public Map<String, IBTValue> getMappings() {
        return mappings;
    }

    @Override
    public boolean contains(String path) {
        if (path.contains(".")) {
            var map = getMappings();
            var split = path.split("\\.");
            int maxI = split.length - 1;
            for (int i = 0, len = split.length; i < len; i++) {
                var s = split[i];
                if (i == maxI) {
                    return map.containsKey(s);
                }
                map = ((BinaryTag) map.get(s).value()).getMappings();
            }
        }
        return getMappings().containsKey(path);
    }

    @Override
    public IBTValue getGeneric(String path) {
        if (path.contains(".")) {
            var map = getMappings();
            var split = path.split("\\.");
            int maxI = split.length - 1;
            for (int i = 0, len = split.length; i < len; i++) {
                var s = split[i];
                var matcher = INDEX_REGEX.matcher(s);
                boolean matches = matcher.matches();
                if (i == maxI) {
                    if (matches) {
                        return ((IBTValue[]) map.get(matcher.group(1)).value())[Integer.parseInt(matcher.group(2))];
                    }
                    return map.get(s);
                }
                map = ((BinaryTag) map.get(s).value()).getMappings();
            }
        }
        var matcher = INDEX_REGEX.matcher(path);
        boolean matches = matcher.matches();
        if (matches) {
            return ((IBTValue[]) getMappings().get(matcher.group(1)).value())[Integer.parseInt(matcher.group(2))];
        }
        return getMappings().get(path);
    }

    @Override
    public IBTValue getGeneric(String path, int index) {
        if (path.contains(".")) {
            var map = getMappings();
            var split = path.split("\\.");
            int maxI = split.length - 1;
            for (int i = 0, len = split.length; i < len; i++) {
                var s = split[i];
                if (i == maxI) {
                    return ((IBTValue[]) map.get(path).value())[index];
                }
                map = ((BinaryTag) map.get(s).value()).getMappings();
            }
        }
        return ((IBTValue[]) getMappings().get(path).value())[index];
    }

    @Override
    public IBTValue getGeneric(String path, int index, IBTValue defaultValue) {
        if (path.contains(".")) {
            var map = getMappings();
            var split = path.split("\\.");
            int maxI = split.length - 1;
            for (int i = 0, len = split.length; i < len; i++) {
                var s = split[i];
                if (i == maxI) {
                    var result = map.get(path);
                    if (result != null)
                        return ((IBTValue[]) result.value())[index];
                    return defaultValue;
                }
                var result = map.get(s);
                if (result != null)
                    map = ((BinaryTag) result.value()).getMappings();
                else
                    return defaultValue;
            }
        }
        var result = getMappings().get(path);
        if (result != null)
            return ((IBTValue[]) result.value())[index];
        return defaultValue;
    }

    @Override
    public void setGeneric(String path, IBTValue value) {
        if (path.contains(".")) {
            var map = getMappings();
            var split = path.split("\\.");
            int maxI = split.length - 1;
            for (int i = 0, len = split.length; i < len; i++) {
                var s = split[i];
                var matcher = INDEX_REGEX.matcher(s);
                boolean matches = matcher.matches();
                final String name, index;
                if (matches) {
                    name = matcher.group(1);
                    index = matcher.group(2);
                } else {
                    name = null;
                    index = null;
                }
                if (i == maxI) {
                    if (matches) {
                        ((IBTValue[]) map.get(name).value())[Integer.parseInt(index)] = value;
                    } else {
                        map.put(s, value);
                    }
                    break;
                }
                map = Utils.with(map, finalMap -> Utils.with(new BinaryTag(), tag -> {
                    var v = tag.transform();
                    if (matches) {
                        ((IBTValue[]) finalMap.get(name).value())[Integer.parseInt(index)] = v;
                    } else {
                        finalMap.put(s, v);
                    }
                    return tag.getMappings();
                }));
            }
        } else {
            var matcher = INDEX_REGEX.matcher(path);
            boolean matches = matcher.matches();
            if (matches) {
                ((IBTValue[]) getMappings().get(matcher.group(1)).value())[Integer.parseInt(matcher.group(2))] = value;
            } else {
                getMappings().put(path, value);
            }
        }
    }

    @Override
    public void setGeneric(String path, int index, IBTValue value) {
        if (path.contains(".")) {
            var map = getMappings();
            var split = path.split("\\.");
            int maxI = split.length - 1;
            for (int i = 0, len = split.length; i < len; i++) {
                var s = split[i];
                if (i == maxI) {
                    ((IBTValue[]) map.get(path).value())[index] = value;
                    break;
                }
                map = Utils.with(map, finalMap -> Utils.with(new BinaryTag(), tag -> {
                    finalMap.put(s, tag.transform());
                    return tag.getMappings();
                }));
            }
        } else {
            ((IBTValue[]) getMappings().get(path).value())[index] = value;
        }
    }

    private void append(StringBuilder sb, Map.Entry<String, IBTValue> e) {
        sb.append(e.getKey()).append(": ").append(e.getValue());
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append('{');
        var it = getMappings().entrySet().iterator();
        if (it.hasNext()) {
            append(sb, it.next());
            while (it.hasNext()) {
                append(sb.append(", "), it.next());
            }
        }
        sb.append('}');
        return sb.toString();
    }
}
