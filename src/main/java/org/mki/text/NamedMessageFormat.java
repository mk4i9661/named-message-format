package org.mki.text;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;

/**
 * Provides a way to concatenate strings in a way similar to {@link MessageFormat}.
 * Unlike {@link MessageFormat}, this class uses named parameters to specify elements of the pattern.
 */
public class NamedMessageFormat {
    private final PreparedPattern pattern;
    private final Formatter formatter;

    /**
     * Constructs a {@link NamedMessageFormat} using specified pattern and formatter.
     *
     * @param pattern
     * @param formatter
     */
    public NamedMessageFormat(String pattern, Formatter formatter) {
        this.pattern = PreparedPattern.ofPattern(pattern);
        this.formatter = formatter;
    }

    /**
     * Constructs a {@link NamedMessageFormat} using specified pattern a locale default Formatter.
     *
     * @param pattern
     */
    public NamedMessageFormat(String pattern) {
        this(pattern, MessageFormat::format);
    }

    public NamedMessageFormat(String pattern, Locale locale) {
        this(pattern, (p, args) -> new MessageFormat(p, locale).format(args));
    }

    public String format(Map<String, Object> parameters) {
        return pattern.apply(parameters, formatter);
    }

    public static String format(String pattern, Map<String, Object> parameters, Formatter formatter) {
        return new NamedMessageFormat(pattern, formatter).format(parameters);
    }

    public static String format(String pattern, Map<String, Object> parameters) {
        return new NamedMessageFormat(pattern).format(parameters);
    }

    public static String format(String pattern, Map<String, Object> parameters, Locale locale) {
        return new NamedMessageFormat(pattern, locale).format(parameters);
    }
}
