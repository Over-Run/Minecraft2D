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

import java.io.IOException;
import java.io.ObjectOutput;
import java.util.Map;

import static io.github.overrun.mc2d.world.ibt.IBTType.*;

/**
 * The interface of the binary tags.
 *
 * @author squid233
 * @since 0.6.0
 */
public interface IBinaryTag {
    /**
     * Serialize into bytes.
     * <p>
     * Byte order: serial flag, tag size, values: (serial flag, name, [array length], value[s])
     *
     * @param out the output
     * @throws IOException the exception
     */
    void serialize(ObjectOutput out) throws IOException;

    /**
     * Transform into {@link IBTValue}.
     *
     * @return the {@link IBTValue}
     */
    default IBTValue transform() {
        return new IBTValue(TAG, this);
    }

    /**
     * Gets the mappings.
     * <p>
     * Types of value: {@code byte}, {@code short}, {@code int}, {@code long},
     * {@code String}, {@code BinaryTag}, {@code Object[]}.
     *
     * @return the mappings
     */
    Map<String, IBTValue> getMappings();

    boolean contains(String path);

    IBTValue getGeneric(String path);

    IBTValue getGeneric(String path, int index);

    IBTValue getGeneric(String path, int index, IBTValue defaultValue);

    void setGeneric(String path, IBTValue value);

    void setGeneric(String path, int index, IBTValue value);

    default byte get(OfByte type, String path) {
        return (byte) getGeneric(path).value();
    }

    default short get(OfShort type, String path) {
        return (short) getGeneric(path).value();
    }

    default int get(OfInt type, String path) {
        return (int) getGeneric(path).value();
    }

    default long get(OfLong type, String path) {
        return (long) getGeneric(path).value();
    }

    default float get(OfFloat type, String path) {
        return (float) getGeneric(path).value();
    }

    default double get(OfDouble type, String path) {
        return (double) getGeneric(path).value();
    }

    default String get(OfString type, String path) {
        return (String) getGeneric(path).value();
    }

    default IBinaryTag get(OfTag type, String path) {
        return (IBinaryTag) getGeneric(path).value();
    }

    default IBTValue[] get(OfArray type, String path) {
        return (IBTValue[]) getGeneric(path).value();
    }

    default int[] get(OfIntArray type, String path) {
        return (int[]) getGeneric(path).value();
    }

    default double[] get(OfDoubleArray type, String path) {
        return (double[]) getGeneric(path).value();
    }

    default byte getAtIndex(OfByte type, String path, int index) {
        return (byte) getGeneric(path, index).value();
    }

    default short getAtIndex(OfShort type, String path, int index) {
        return (short) getGeneric(path, index).value();
    }

    default int getAtIndex(OfInt type, String path, int index) {
        return (int) getGeneric(path, index).value();
    }

    default long getAtIndex(OfLong type, String path, int index) {
        return (long) getGeneric(path, index).value();
    }

    default float getAtIndex(OfFloat type, String path, int index) {
        return (float) getGeneric(path, index).value();
    }

    default double getAtIndex(OfDouble type, String path, int index) {
        return (double) getGeneric(path, index).value();
    }

    default String getAtIndex(OfString type, String path, int index) {
        return (String) getGeneric(path, index).value();
    }

    default IBinaryTag getAtIndex(OfTag type, String path, int index) {
        return (IBinaryTag) getGeneric(path, index).value();
    }

    default byte get(OfByte type, String path, byte defaultValue) {
        var v = getGeneric(path);
        return v != null ? (byte) v.value() : defaultValue;
    }

    default short get(OfShort type, String path, short defaultValue) {
        var v = getGeneric(path);
        return v != null ? (short) v.value() : defaultValue;
    }

    default int get(OfInt type, String path, int defaultValue) {
        var v = getGeneric(path);
        return v != null ? (int) v.value() : defaultValue;
    }

    default long get(OfLong type, String path, long defaultValue) {
        var v = getGeneric(path);
        return v != null ? (long) v.value() : defaultValue;
    }

    default float get(OfFloat type, String path, float defaultValue) {
        var v = getGeneric(path);
        return v != null ? (float) v.value() : defaultValue;
    }

    default double get(OfDouble type, String path, double defaultValue) {
        var v = getGeneric(path);
        return v != null ? (double) v.value() : defaultValue;
    }

    default String get(OfString type, String path, String defaultValue) {
        var v = getGeneric(path);
        return v != null ? (String) v.value() : defaultValue;
    }

    default byte getAtIndex(OfByte type, String path, int index, byte defaultValue) {
        return (byte) getGeneric(path, index, new IBTValue(BYTE, defaultValue)).value();
    }

    default short getAtIndex(OfShort type, String path, int index, short defaultValue) {
        return (short) getGeneric(path, index, new IBTValue(SHORT, defaultValue)).value();
    }

    default int getAtIndex(OfInt type, String path, int index, int defaultValue) {
        return (int) getGeneric(path, index, new IBTValue(INT, defaultValue)).value();
    }

    default long getAtIndex(OfLong type, String path, int index, long defaultValue) {
        return (long) getGeneric(path, index, new IBTValue(LONG, defaultValue)).value();
    }

    default float getAtIndex(OfFloat type, String path, int index, float defaultValue) {
        return (float) getGeneric(path, index, new IBTValue(FLOAT, defaultValue)).value();
    }

    default double getAtIndex(OfDouble type, String path, int index, double defaultValue) {
        return (double) getGeneric(path, index, new IBTValue(DOUBLE, defaultValue)).value();
    }

    default String getAtIndex(OfString type, String path, int index, String defaultValue) {
        return (String) getGeneric(path, index, new IBTValue(STRING, defaultValue)).value();
    }

    default void set(String path, byte value) {
        setGeneric(path, new IBTValue(BYTE, value));
    }

    default void set(String path, short value) {
        setGeneric(path, new IBTValue(SHORT, value));
    }

    default void set(String path, int value) {
        setGeneric(path, new IBTValue(INT, value));
    }

    default void set(String path, long value) {
        setGeneric(path, new IBTValue(LONG, value));
    }

    default void set(String path, float value) {
        setGeneric(path, new IBTValue(FLOAT, value));
    }

    default void set(String path, double value) {
        setGeneric(path, new IBTValue(DOUBLE, value));
    }

    default void set(String path, String value) {
        setGeneric(path, new IBTValue(STRING, value));
    }

    default void set(String path, IBinaryTag value) {
        setGeneric(path, value.transform());
    }

    default void set(String path, IBTValue[] value) {
        setGeneric(path, new IBTValue(ARRAY, value));
    }

    default void set(String path, int[] value) {
        setGeneric(path, new IBTValue(INT_ARRAY, value));
    }

    default void set(String path, double[] value) {
        setGeneric(path, new IBTValue(DOUBLE_ARRAY, value));
    }

    default void setAtIndex(String path, int index, byte value) {
        setGeneric(path, index, new IBTValue(BYTE, value));
    }

    default void setAtIndex(String path, int index, short value) {
        setGeneric(path, index, new IBTValue(SHORT, value));
    }

    default void setAtIndex(String path, int index, int value) {
        setGeneric(path, index, new IBTValue(INT, value));
    }

    default void setAtIndex(String path, int index, long value) {
        setGeneric(path, index, new IBTValue(LONG, value));
    }

    default void setAtIndex(String path, int index, float value) {
        setGeneric(path, index, new IBTValue(FLOAT, value));
    }

    default void setAtIndex(String path, int index, double value) {
        setGeneric(path, index, new IBTValue(DOUBLE, value));
    }

    default void setAtIndex(String path, int index, String value) {
        setGeneric(path, index, new IBTValue(STRING, value));
    }

    default void setAtIndex(String path, int index, IBinaryTag value) {
        setGeneric(path, index, value.transform());
    }
}
