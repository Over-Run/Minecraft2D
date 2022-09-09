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

/**
 * The IBT types.
 *
 * @author squid233
 * @since 0.6.0
 */
public sealed class IBTType {
    private final byte serialFlag;

    private IBTType(int serialFlag) {
        this.serialFlag = (byte) serialFlag;
    }

    /**
     * Gets the type from the serial flag.
     *
     * @param serialFlag the flag
     * @return the type
     */
    public static IBTType fromFlag(int serialFlag) {
        return switch (serialFlag) {
            case 1 -> BYTE;
            case 2 -> SHORT;
            case 3 -> INT;
            case 4 -> LONG;
            case 5 -> FLOAT;
            case 6 -> DOUBLE;
            case 7 -> STRING;
            case 8 -> TAG;
            case 9 -> ARRAY;
            case 10 -> INT_ARRAY;
            case 11 -> DOUBLE_ARRAY;
            case 12 -> TAG_ARRAY;
            default ->
                throw new IllegalArgumentException("serialFlag is an invalid number; got: " + serialFlag + ", expected 1 to 11");
        };
    }

    /**
     * Gets the serialization header.
     *
     * @return the flag
     */
    public byte getSerialFlag() {
        return serialFlag;
    }

    /**
     * Byte type.
     *
     * @author squid233
     * @since 0.6.0
     */
    public static final class OfByte extends IBTType {
        private OfByte() {
            super(1);
        }
    }

    /**
     * Short type.
     *
     * @author squid233
     * @since 0.6.0
     */
    public static final class OfShort extends IBTType {
        private OfShort() {
            super(2);
        }
    }

    /**
     * Int type.
     *
     * @author squid233
     * @since 0.6.0
     */
    public static final class OfInt extends IBTType {
        private OfInt() {
            super(3);
        }
    }

    /**
     * Long type.
     *
     * @author squid233
     * @since 0.6.0
     */
    public static final class OfLong extends IBTType {
        private OfLong() {
            super(4);
        }
    }

    /**
     * Float type.
     *
     * @author squid233
     * @since 0.6.0
     */
    public static final class OfFloat extends IBTType {
        private OfFloat() {
            super(5);
        }
    }

    /**
     * Double type.
     *
     * @author squid233
     * @since 0.6.0
     */
    public static final class OfDouble extends IBTType {
        private OfDouble() {
            super(6);
        }
    }

    /**
     * String type.
     *
     * @author squid233
     * @since 0.6.0
     */
    public static final class OfString extends IBTType {
        private OfString() {
            super(7);
        }
    }

    /**
     * Tag type.
     *
     * @author squid233
     * @since 0.6.0
     */
    public static final class OfTag extends IBTType {
        private OfTag() {
            super(8);
        }
    }

    /**
     * Array type.
     *
     * @author squid233
     * @since 0.6.0
     */
    public static final class OfArray extends IBTType {
        private OfArray() {
            super(9);
        }
    }

    /**
     * Int array type.
     *
     * @author squid233
     * @since 0.6.0
     */
    public static final class OfIntArray extends IBTType {
        private OfIntArray() {
            super(10);
        }
    }

    /**
     * Double array type.
     *
     * @author squid233
     * @since 0.6.0
     */
    public static final class OfDoubleArray extends IBTType {
        private OfDoubleArray() {
            super(11);
        }
    }

    /**
     * Tag array type.
     *
     * @author squid233
     * @since 0.6.0
     */
    public static final class OfTagArray extends IBTType {
        private OfTagArray() {
            super(12);
        }
    }

    public static final OfByte BYTE = new OfByte();
    public static final OfShort SHORT = new OfShort();
    public static final OfInt INT = new OfInt();
    public static final OfLong LONG = new OfLong();
    public static final OfFloat FLOAT = new OfFloat();
    public static final OfDouble DOUBLE = new OfDouble();
    public static final OfString STRING = new OfString();
    public static final OfTag TAG = new OfTag();
    public static final OfArray ARRAY = new OfArray();
    public static final OfIntArray INT_ARRAY = new OfIntArray();
    public static final OfDoubleArray DOUBLE_ARRAY = new OfDoubleArray();
    public static final OfTagArray TAG_ARRAY = new OfTagArray();
}
