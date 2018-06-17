package org.mki.text;

import java.util.Map;

public class NamedMessageFormat {
    private final PreparedPattern pattern;

    public NamedMessageFormat(String pattern) {
        this.pattern = PreparedPattern.ofPattern(pattern);
    }

    public String format(Map<String, Object> parameters) {
        return pattern.apply(parameters);
    }

    public static String format(String pattern, Map<String, Object> parameters) {
        return new NamedMessageFormat(pattern).format(parameters);
    }
}
