package io.github.overrun.mc2d.api.io;

/**
 * @author squid233
 */
public class Arrays {
    public static final String ARRAY_PREFIX = "[";
    public static final String ARRAY_SEPARATOR = ", ";
    public static final String ARRAY_SUFFIX = "]";

    /**
     * Parse {@code arrString} to array. For example: {@code [Scope-Tech, squid233]} can be parse to array: {@code [Scope-Tech, squid233]}(use {@link java.util.Arrays#toString(Object[])})
     *
     * @param arrString The array's string type.
     * @return Object array after parse
     * @throws InvalidArrayException When param string isn't starts with "[" and isn't ends with "]", this exception will throw.
     */
    public static Object[] parseString(String arrString) throws InvalidArrayException {
        if (!arrString.startsWith(ARRAY_PREFIX) && !arrString.endsWith(ARRAY_SUFFIX)) {
            throw new InvalidArrayException(arrString);
        }
        return arrString.substring(1, arrString.length() - 1).split(ARRAY_SEPARATOR);
    }
}
