package org.task;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class NamedGroup implements Token {
    private final JavaIdentifier identifier;
    private final String source;
    private final String pattern;
//    private final int from;
//    private final int to;

    private static final String NAMED_GROUP_PATTERN =
            //matches the opening brace
            "\\{"
                    //matches the parameter name
                    + "(?<identifier>\\s*\\w+\\s*)"
                    //matches the format type
                    + "(?<type>\\s*\\,?\\s*\\w+\\s*)?"
                    //matches the format style
                    + "(?<style>\\s*\\,.+)?"
                    //matches the closing brace
                    + "}";

    private NamedGroup(JavaIdentifier identifier, String source, String pattern/*, int from, int to*/) {
        this.source = source;
        this.identifier = identifier;
        this.pattern = pattern;
//        this.from = from;
//        this.to = to;
    }

    public static NamedGroup of(String source, int from, int to) {
        String group = source.substring(from, to);
        Pattern pattern = Pattern.compile(NAMED_GROUP_PATTERN);
        Matcher matcher = pattern.matcher(group);
        if (matcher.matches()) {
            String identifier = matcher.group("identifier");
            JavaIdentifier javaIdentifier = JavaIdentifier.of(identifier);

            // matcher.appendReplacement does not accept StringBuilder in Java 8
            // use StringBuffer instead
            StringBuffer buffer = new StringBuffer();

            String replaced = matcher.group().replace(
                    identifier,
                    "0"
            );

            matcher.appendReplacement(
                    buffer,
                    Matcher.quoteReplacement(replaced) //treat $ as a non regexp character
            );
            matcher.appendTail(buffer);

            return new NamedGroup(
                    javaIdentifier,
                    buffer.toString(),
                    group//,
//                    from,
//                    to
            );
        } else {
            throw new RuntimeException(
                    String.format("'%s' is not a pattern.", group)
            );
        }
    }

//    public int from() {
//        return from;
//    }
//
//    public int to() {
//        return to;
//    }

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
