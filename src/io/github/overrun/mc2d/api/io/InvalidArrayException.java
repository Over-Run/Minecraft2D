package io.github.overrun.mc2d.api.io;

/**
 * @author squid233
 */
public class InvalidArrayException extends Exception {
    public InvalidArrayException(String invalidArray) {
        super("Invalid array: " + invalidArray + " ! This might be a typo error.");
    }
}
