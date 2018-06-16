package org.task;

import java.util.ArrayList;
import java.util.List;

class NamedMessageFormatPattern {

    private static NamedMessageFormatPattern of(String pattern) {
        boolean raw = false;
        int from = 0;
        int to = 0;

        List<FormatElement> elements = new ArrayList<>();

        for (int i = 0, length = pattern.length(); i < length; i++) {
            char charAt = pattern.charAt(i);
            if (charAt != '\'') {
                to = i;
            } else {
                if (!raw) {
                    if (to != 0) {
                        elements.add(new FormatElement(pattern, from, to + 1));
                    }
                } else {
                    from = i + 1;
                }
                raw = !raw;
            }
        }
        if (!raw) {
            elements.add(new FormatElement(pattern, from, to + 1));
        }

        return null;
    }

}
