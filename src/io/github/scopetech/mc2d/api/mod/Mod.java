package io.github.scopetech.mc2d.api.mod;

/**
 * @author squid233
 */
public @interface Mod {
    String modid();

    String name() default "";

    String version() default "";

    String[] authors() default {};
}
