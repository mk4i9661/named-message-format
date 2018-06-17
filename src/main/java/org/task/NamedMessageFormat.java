package org.task;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NamedMessageFormat {
    private final String pattern;

    public NamedMessageFormat(String pattern) {
        this.pattern = pattern;
    }


    private static final String NAMED_GROUP_PATTERN =
            //should not be in quoted area (start)
            "(?<!')"
                    //matches the opening brace
                    + "\\{"
                    //matches the parameter name
                    + "(?<identifier>\\s*%s\\s*)"
                    //matches the format type
                    + "(?<type>\\s*\\,?\\s*\\w+\\s*)?"
                    //matches the format style
                    + "(?<style>\\s*\\,.+)?"
                    //matches the closing brace
                    + "}"
                    //should not be in quoted area (end)
                    + "(?!')";

    private Pattern patternOf(String name) {
        return Pattern.compile(
                String.format(
                        NAMED_GROUP_PATTERN,
                        name
                )
        );
    }

    public static String format(String pattern, Map<String, Object> parameters) {
        return new NamedMessageFormat(pattern).format(parameters);
    }

    public String format(Map<String, Object> parameters) {
        List<JavaIdentifier> identifiers = new ArrayList<>();
        int position = 0;

        // matcher.appendReplacement does not accept StringBuilder in Java 8
        // use StringBuffer instead
        StringBuffer buffer = new StringBuffer(pattern);

        List<Object> arguments = new ArrayList<>(parameters.size());


        for (Map.Entry<String, Object> parameter : parameters.entrySet()) {

            for (UnquotedText text : UnquotedTextArea.of(buffer.toString()).tokens()) {
                String name = parameter.getKey();
                identifiers.add(JavaIdentifier.of(name));

                Pattern pattern = patternOf(name);
                Matcher matcher = pattern.matcher(
                        buffer.toString()
                );

                buffer.setLength(0);

                matcher.region(text.from(), text.to());

                while (matcher.find()) {
                    String identifier = matcher.group("identifier");
                    String replaced = matcher.group().replace(
                            identifier,
                            String.valueOf(position)
                    );
                    matcher.appendReplacement(buffer, replaced);
                }


                position++;

                matcher.appendTail(buffer);

                arguments.add(parameter.getValue());
            }
        }

        return MessageFormat.format(buffer.toString(), arguments.toArray());
    }
}
