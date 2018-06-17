package org.task;

import java.util.*;

class UnquotedTextArea {

    private final List<UnquotedText> tokens;

    private UnquotedTextArea(List<UnquotedText> tokens) {
        this.tokens = tokens;
    }

    /**
     * Splits pattern into a list of unquoted areas
     * @param pattern
     * @return
     */
    public static UnquotedTextArea of(String pattern) {
        boolean raw = false;
        int from = 0;
        int to = 0;

        List<UnquotedText> elements = new ArrayList<>();
        Deque<Integer> quotes = new ArrayDeque<>();
        for (int i = 0, length = pattern.length(); i < length; i++) {
            char charAt = pattern.charAt(i);
            to = i;
            if (charAt == '\'') {
                if (quotes.isEmpty()) {
                    //the first quote occurrence
                    if (to != 0) {
                        elements.add(new UnquotedText(pattern, from, i));
                    }
                    quotes.push(i);
                } else {
                    quotes.pop();
                    from = i + 1;
                }
            }
        }

        if (quotes.isEmpty() && from < to) {
            elements.add(new UnquotedText(pattern, from, to + 1));
        }


        return new UnquotedTextArea(
                Collections.unmodifiableList(elements)
        );
    }


    public List<UnquotedText> tokens() {
        return tokens;
    }
}
