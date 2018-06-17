package org.task;

import java.text.MessageFormat;

class RawGroup implements Token {
    private final String source;

    private RawGroup(String source) {
        this.source = source;
    }

    static RawGroup of(String patter, int from, int to) {
        return new RawGroup(patter.substring(from, to));
    }

    static RawGroup of(String patter) {
        return new RawGroup(patter);
    }

    @Override
    public String toString() {
        //MessageFormat is not thread safe, so create a new one
        return MessageFormat.format(source, new Object[0]);
    }
}
