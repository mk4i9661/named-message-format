package org.mki.text;

import java.text.MessageFormat;
import java.util.Locale;

class UsFormatter implements Formatter {
    static final UsFormatter US_FORMATTER = new UsFormatter();

    @Override
    public String format(String pattern, Object... arguments) {
        return new MessageFormat(pattern, Locale.US).format(arguments);
    }
}
