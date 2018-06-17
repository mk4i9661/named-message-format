package org.task;

public class UnquotedText {
    private final String source;
    private final int from;
    private final int to;

    public UnquotedText(String source, int from, int to) {
        this.source = source;
        this.from = from;
        this.to = to;
    }

    public int from() {
        return from;
    }

    public int to() {
        return to;
    }

    public String value() {
        return source.substring(from, to);
    }
}
