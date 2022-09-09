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
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;

import static io.github.overrun.mc2d.world.ibt.IBTType.*;

/**
 * An IBT value holder.
 *
 * @param type  the type
 * @param value the value
 * @author squid233
 * @since 0.6.0
 */
public record IBTValue(IBTType type, Object value) {
    public void serialize(ObjectOutput out) throws IOException {
        out.writeByte(type.getSerialFlag());
        if (type == BYTE) {
            out.writeByte((byte) value);
        } else if (type == SHORT) {
            out.writeShort((short) value);
        } else if (type == INT) {
            out.writeInt((int) value);
        } else if (type == LONG) {
            out.writeLong((long) value);
        } else if (type == FLOAT) {
            out.writeFloat((float) value);
        } else if (type == DOUBLE) {
            out.writeDouble((double) value);
        } else if (type == STRING) {
            out.writeUTF((String) value);
        } else if (type == TAG) {
            ((IBinaryTag) value).serialize(out);
        } else if (type == ARRAY) {
            var arr = (IBTValue[]) value;
            out.writeInt(arr.length);
            for (var v : arr) {
                v.serialize(out);
            }
        } else if (type == INT_ARRAY) {
            var arr = (int[]) value;
            out.writeInt(arr.length);
            for (var v : arr) {
                out.writeInt(v);
            }
        } else if (type == DOUBLE_ARRAY) {
            var arr = (double[]) value;
            out.writeInt(arr.length);
            for (var v : arr) {
                out.writeDouble(v);
            }
        } else if (type == TAG_ARRAY) {
            var arr = (IBinaryTag[]) value;
            out.writeInt(arr.length);
            for (var v : arr) {
                v.serialize(out);
            }
        } else {
            out.writeObject(value);
        }
    }

    public static IBTValue deserialize(ObjectInput in) throws IOException, ClassNotFoundException {
        var type = fromFlag(in.readByte());
        if (type == BYTE) {
            return new IBTValue(type, in.readByte());
        }
        if (type == SHORT) {
            return new IBTValue(type, in.readShort());
        }
        if (type == INT) {
            return new IBTValue(type, in.readInt());
        }
        if (type == LONG) {
            return new IBTValue(type, in.readLong());
        }
        if (type == FLOAT) {
            return new IBTValue(type, in.readFloat());
        }
        if (type == DOUBLE) {
            return new IBTValue(type, in.readDouble());
        }
        if (type == STRING) {
            return new IBTValue(type, in.readUTF());
        }
        if (type == TAG) {
            return new IBTValue(type, BinaryTag.deserialize(in));
        }
        if (type == ARRAY) {
            int len = in.readInt();
            IBTValue[] arr = new IBTValue[len];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = IBTValue.deserialize(in);
            }
            return new IBTValue(type, arr);
        }
        if (type == INT_ARRAY) {
            int len = in.readInt();
            int[] arr = new int[len];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = in.readInt();
            }
            return new IBTValue(type, arr);
        }
        if (type == DOUBLE_ARRAY) {
            int len = in.readInt();
            double[] arr = new double[len];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = in.readDouble();
            }
            return new IBTValue(type, arr);
        }
        if (type == TAG_ARRAY) {
            int len = in.readInt();
            IBinaryTag[] arr = new IBinaryTag[len];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = BinaryTag.deserialize(in);
            }
            return new IBTValue(type, arr);
        }
        return new IBTValue(type, in.readObject());
    }

    @Override
    public String toString() {
        if (type == BYTE) {
            return value + "b";
        }
        if (type == SHORT) {
            return value + "s";
        }
        if (type == INT || type == DOUBLE || type == TAG) {
            return String.valueOf(value);
        }
        if (type == LONG) {
            return value + "L";
        }
        if (type == FLOAT) {
            return value + "f";
        }
        if (type == STRING) {
            return "'" + value + "'";
        }
        if (type == ARRAY) {
            var arr = (IBTValue[]) value;
            int iMax = arr.length - 1;
            if (iMax == -1) {
                return "[]";
            }
            var sb = new StringBuilder();
            sb.append('[');
            for (int i = 0; ; i++) {
                sb.append(arr[i].value);
                if (i == iMax) {
                    sb.append(']');
                    return sb.toString();
                }
                sb.append(", ");
            }
        }
        if (type == INT_ARRAY) {
            return Arrays.toString((int[]) value);
        }
        if (type == DOUBLE_ARRAY) {
            return Arrays.toString((double[]) value);
        }
        if (type == TAG_ARRAY) {
            return Arrays.toString((IBinaryTag[]) value);
        }
        return type + " -> " + value;
    }
}
