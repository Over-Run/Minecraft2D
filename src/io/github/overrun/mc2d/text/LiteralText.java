package io.github.overrun.mc2d.text;

/**
 * @author squid233
 */
public class LiteralText implements IText {
    private final String text;

    public LiteralText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String asString() {
        return getText();
    }

    @Override
    public int hashCode() {
        return getText().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof LiteralText && ((LiteralText) obj).getText().equals(text);
    }
}
