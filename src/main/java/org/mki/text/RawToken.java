package org.mki.text;

import java.text.MessageFormat;

/**
 * Represents a string 'as is' without any format transformations (has no curly braces in it)
 */
public class RawToken implements Token {
    private final String source;

    private RawToken(String source) {
        this.source = source;
    }

    static RawToken of(String patter) {
        return new RawToken(patter);
    }

    @Override
    public String toString() {
        //MessageFormat is not thread safe, so create a new one
        return MessageFormat.format(source, new Object[0]);
    }
}
