package org.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class PreparedPattern {
    final List<Token> tokens;

    private PreparedPattern(List<Token> tokens) {
        this.tokens = tokens;
    }

    public String apply(Map<String, Object> parameters) {
        StringBuilder builder = new StringBuilder();
        for (Token token : tokens) {
            if (token instanceof NamedGroup) {
                NamedGroup namedGroup = (NamedGroup) token;
                if (parameters.containsKey(namedGroup.identifier().toString())) {
                    builder.append(
                            namedGroup.toString(
                                    parameters.get(
                                            namedGroup.identifier().toString()
                                    )
                            )
                    );
                    continue;
                }
            }
            builder.append(token);
        }

        return builder.toString();
    }

//    private static NamedGroup namedGroup(String pattern, int from, int to){
//        return NamedGroup.of(pattern.substring(from, to));
//    }

    //    private static List<NamedGroup> extractNamedGroups(String pattern) {
    private static List<Token> extractNamedGroups(String pattern) {
        List<NamedGroup> groups = new ArrayList<>();
        List<Token> tokens = new ArrayList<>();
        boolean raw = false;
        int quotes = 0;
        int from = 0;
        int to = 0;
        for (int i = 0; i < pattern.length(); i++) {
            char charAt = pattern.charAt(i);
            if (charAt == '\'') {
                raw = !raw;
            } else {
                if (raw)
                    continue;

                if (charAt == '{') {
                    tokens.add(RawGroup.of(pattern, from, i));
                    from = i;
                    quotes++;
                } else if (charAt == '}') {
                    to = i + 1;
                    quotes--;
                    if (quotes == 0) {
                        groups.add(NamedGroup.of(pattern, from, to));
                        tokens.add(NamedGroup.of(pattern, from, to));
                    }
                    from = i + 1;
                }
            }
        }

        if (quotes != 0)
            throw new RuntimeException("Unmatched braces in the s.");

//        if (tokens.isEmpty()) {
//            tokens.add(RawGroup.of(pattern));
//        }

        if (from < pattern.length()) {
            tokens.add(RawGroup.of(pattern, from, pattern.length()));
        }

//        return groups;
        return tokens;
    }

//    public static PreparedPattern from(String pattern, List<NamedGroup> groups) {
//        List<Token> tokens = new ArrayList<>();
//        if (!groups.isEmpty()) {
//            int from = 0;
//            for (NamedGroup group : groups) {
//                if (group.to() > 0) {
//                    tokens.add(RawGroup.of(pattern, from, group.from()));
//                }
//                tokens.add(group);
//                from = group.to();
//            }
//            if (from < pattern.length()) {
//                tokens.add(RawGroup.of(pattern, from, pattern.length()));
//            }
//        } else {
//            tokens.add(RawGroup.of(pattern, 0, pattern.length()));
//        }
//
//        return new PreparedPattern(tokens);
//    }

    public static PreparedPattern ofPattern(String pattern) {
//        return from(pattern, extractNamedGroups(pattern));
        return new PreparedPattern(extractNamedGroups(pattern));
    }
}
