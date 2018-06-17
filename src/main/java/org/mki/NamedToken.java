package org.mki;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NamedToken implements Token {
    private static final String NAMED_TOKEN_PATTERN_VALUE =
            //matches the opening brace
            "\\{"
                    // matches the parameter name
                    // (allows valid java identifiers only)
                    + "(?<identifier>\\s*\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}+\\s*)"
                    //matches the format type
                    + "(?<type>\\s*\\,?\\s*\\w+\\s*)?"
                    //matches the format style
                    + "(?<style>\\s*\\,\\p{all}+\\s*)?"
                    //matches the closing brace
                    + "}";

    private static final Pattern NAMED_TOKEN_PATTERN = Pattern.compile(NAMED_TOKEN_PATTERN_VALUE);

    private final JavaIdentifier identifier;
    private final String source;
    private final String pattern;

    private NamedToken(JavaIdentifier identifier, String source, String pattern) {
        this.source = source;
        this.identifier = identifier;
        this.pattern = pattern;
    }

    public static NamedToken of(String group) throws InvalidJavaIdentifierException, InvalidNamedGroupException {
        Matcher matcher = NAMED_TOKEN_PATTERN.matcher(group);
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

            return new NamedToken(
                    javaIdentifier,
                    buffer.toString(),
                    group
            );
        }

        throw new InvalidNamedGroupException(
                String.format("'%s' is not a pattern.", group)
        );
    }

    public JavaIdentifier identifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return pattern;
    }

    public String toString(Object value) {
        //MessageFormat is not thread safe, so create a new one
        return MessageFormat.format(source, value);
    }
}
