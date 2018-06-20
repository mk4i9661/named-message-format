package org.mki.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Ready to use pattern.
 */
class PreparedPattern {
    final List<ParameterBinding> binders;

    private PreparedPattern(List<ParameterBinding> binders) {
        this.binders = binders;
    }

    /**
     * Applies parameters to the pattern using specified formatter.
     * @param parameters
     * @param formatter
     * @return
     */
    public String apply(Map<String, Object> parameters, Formatter formatter) {
        return binders.stream()
                .map(binder -> binder.bindTo(parameters).format(formatter))
                .collect(Collectors.joining());
    }

    public String apply(String name, Object value, Formatter formatter) {
        return apply(Collections.singletonMap(name, value), formatter);
    }

    private static NamedParameterBinding namedBinder(String source, int from, int to) {
        return new NamedParameterBinding(
                NamedParameter.of(source.substring(from, to))
        );
    }

    private static RawParameterBinding rawBinder(String source, int from, int to) {
        return new RawParameterBinding(
                RawSequence.of(source.substring(from, to))
        );
    }

    private static class RawFormat implements ParameterAwareFormat {

        private final RawSequence token;

        public RawFormat(RawSequence token) {
            this.token = token;
        }

        @Override
        public String format(Formatter formatter) {
            return token.format(formatter);
        }
    }

    private static class NamedFormat implements ParameterAwareFormat {
        private final NamedParameter token;
        private final boolean valuePresent;
        private final Object value;

        public NamedFormat(NamedParameter token, boolean valuePresent, Object value) {
            this.token = token;
            this.valuePresent = valuePresent;
            this.value = value;
        }

        @Override
        public String format(Formatter formatter) {
            return valuePresent
                    ? token.format(formatter, value)
                    : token.toString();
        }
    }

    private static class RawParameterBinding implements ParameterBinding {
        private final RawSequence token;

        public RawParameterBinding(RawSequence token) {
            this.token = token;
        }

        @Override
        public ParameterAwareFormat bindTo(Map<String, Object> parameters) {
            return new RawFormat(token);
        }
    }

    private static class NamedParameterBinding implements ParameterBinding {
        private final NamedParameter token;

        public NamedParameterBinding(NamedParameter token) {
            this.token = token;
        }

        @Override
        public ParameterAwareFormat bindTo(Map<String, Object> parameters) {
            String name = token.identifier().toString();
            return parameters.containsKey(name)
                    ? new NamedFormat(token, true, parameters.get(name))
                    : new NamedFormat(token, false, null);
        }
    }

    /**
     * Splits pattern into a list of raw and named tokens.
     *
     * @param pattern
     * @return
     */
    private static List<ParameterBinding> split(String pattern) {
        List<ParameterBinding> binders = new ArrayList<>();
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
                        binders.add(rawBinder(pattern, from, i));
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
                        binders.add(namedBinder(pattern, from, i + 1));
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
            binders.add(new RawParameterBinding(RawSequence.of(pattern.substring(from))));
        }

        return Collections.unmodifiableList(binders);
    }

    public static PreparedPattern ofPattern(String pattern) {
        return new PreparedPattern(split(pattern));
    }
}
