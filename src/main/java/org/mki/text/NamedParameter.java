package org.mki.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a ready to use pattern with named attributes in it ({name}, {age} etc.)
 */
class NamedParameter {
    private static final String NAMED_TOKEN_PATTERN_VALUE =
            //matches the opening brace
            "\\{"
                    // matches the parameter name
                    // (allows valid java identifiers only)
                    + "(?<identifier>" + JavaIdentifier.JAVA_IDENTIFIER_PATTERN_VALUE + ")"
                    //matches the format type
                    + "(?<type>\\s*\\,?\\s*\\w+\\s*)?"
                    //matches the format style
                    + "(?<style>\\s*\\,\\p{all}+\\s*)?"
                    //matches the closing brace
                    + "}";

    private static final Pattern NAMED_TOKEN_PATTERN = Pattern.compile(NAMED_TOKEN_PATTERN_VALUE);

    private final JavaIdentifier identifier;
    private final String namedPattern;
    private final String indexedPattern;

    private NamedParameter(JavaIdentifier identifier, String indexedPattern, String namedPattern) {
        this.identifier = identifier;
        this.namedPattern = namedPattern;
        this.indexedPattern = indexedPattern;
    }

    public static NamedParameter of(String pattern) throws InvalidJavaIdentifierException, InvalidNamedGroupException {
        Matcher matcher = NAMED_TOKEN_PATTERN.matcher(pattern);
        if (matcher.matches()) {
            String identifier = matcher.group("identifier");
            JavaIdentifier javaIdentifier = JavaIdentifier.of(identifier);

            // matcher.appendReplacement does not accept StringBuilder in Java 8
            // use StringBuffer instead
            StringBuffer buffer = new StringBuffer();

            // replace the name of the group with positional index (always 0)
            String positionalIndexGroup = matcher.group().replace(
                    identifier,
                    "0"
            );

            // replace the old group with a new one
            matcher.appendReplacement(
                    buffer,
                    Matcher.quoteReplacement(positionalIndexGroup) //treat $ as a non regexp character
            );

            // append the end of the match to the buffer
            matcher.appendTail(buffer);

            return new NamedParameter(
                    javaIdentifier,
                    buffer.toString(),
                    pattern
            );
        }

        throw new InvalidNamedGroupException(
                String.format("'%s' is not a pattern.", pattern)
        );
    }

    public JavaIdentifier identifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return namedPattern;
    }

    public String format(Formatter formatter, Object value) {
        return formatter.format(indexedPattern, value);
    }
}
