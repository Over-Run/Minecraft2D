package io.github.overrun.mc2d.util;

/**
 * @author squid233
 */
public class JavaVersion {
    public static final String JAVA_CLASS_VERSION = "java.class.version";

    public enum Class {
        /**
         * Java 1.8 class version
         */
        V_1_8(52),
        V_9(53),
        V_10(54),
        V_11(55),
        ;
        private final int version;

        Class(int version) {
            this.version = version;
        }

        public int getVer() {
            return version;
        }
    }
}
