package org.mki.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PreparedPattern {
    final List<Token> tokens;

    private PreparedPattern(List<Token> tokens) {
        this.tokens = tokens;
    }

    /**
     * Applies parameters to the pattern.
     *
     * @param parameters
     * @return
     */
    public String apply(Map<String, Object> parameters) {
        return tokens.stream()
                .map(token -> new ParametersAwareToken(token, parameters).toString())
                .collect(Collectors.joining());
    }

    public String apply(String name, Object value) {
        return apply(Collections.singletonMap(name, value));
    }

    private static NamedToken namedToken(String source, int from, int to) {
        return NamedToken.of(source.substring(from, to));
    }

    private static RawToken rawToken(String source, int from, int to) {
        return RawToken.of(source.substring(from, to));
    }

    /**
     * Splits pattern into a list of raw and named tokens.
     *
     * @param pattern
     * @return
     */
    private static List<Token> split(String pattern) {
        List<Token> tokens = new ArrayList<>();
        // marker for a raw sequence
        boolean raw = false;
        // serves as a stack to count opening and closing braces
        int braces = 0;
        // index in the pattern to start a token from
        int from = 0;
        for (int i = 0; i < pattern.length(); i++) {
            char charAt = pattern.charAt(i);
            if (charAt == '\'') {
                // mark a sequence as a raw one
                raw = !raw;
            } else {
                // if the sequence is a raw one then skip without checking its content
                if (raw)
                    continue;

                if (charAt == '{') {
                    // if the sequence starts with an opening brace do nothing
                    // as it would be handled when the closing brace would be found
                    if (from < i && braces == 0) {
                        tokens.add(rawToken(pattern, from, i));
                        from = i;
                    }
                    // as a brace was found, increase the stack value
                    braces++;
                } else if (charAt == '}') {
                    // a closing brace was found, decrease the stack value
                    braces--;

                    // if it was the last closing brace in the sequence so far
                    // i.e. it was a first level brace
                    // then add a new named token into the list
                    if (braces == 0) {
                        tokens.add(namedToken(pattern, from, i + 1));
                        from = i + 1;
                    }
                }
            }
        }

        // if the amount of closing braces exceeds the amount opening ones it is fine
        // for they will be treated as raw sequences
        if (braces > 0)
            throw new IllegalArgumentException("Unmatched braces in the pattern.");

        if (from < pattern.length()) {
            // add the rest of the pattern as a raw sequence
            tokens.add(RawToken.of(pattern.substring(from)));
        }

        return Collections.unmodifiableList(tokens);
    }

    public static PreparedPattern ofPattern(String pattern) {
        return new PreparedPattern(split(pattern));
    }
}
