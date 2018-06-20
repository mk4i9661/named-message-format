package org.mki.text;

/**
 * Represents a string 'as is' without any format transformations (has no curly braces in it)
 */
class RawSequence {
    private final String pattern;

    private RawSequence(String pattern) {
        this.pattern = pattern;
    }

    static RawSequence of(String patter) {
        return new RawSequence(patter);
    }

    public String format(Formatter formatter) {
        return formatter.format(pattern);
    }
}
