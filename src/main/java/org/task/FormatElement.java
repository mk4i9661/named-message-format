package org.task;

class FormatElement {
    private final String source;
    private final int from;
    private final int to;

    public FormatElement(String source, int from, int to) {
        this.source = source;
        this.from = from;
        this.to = to;
    }

    public String element() {
        return source.substring(from, to);
    }


}
