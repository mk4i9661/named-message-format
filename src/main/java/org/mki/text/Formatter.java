package org.mki.text;

/**
 * Represents a Formatter that can turn an index-based pattern into a formatted string.
 */
public interface Formatter {
    String format(String pattern, Object... arguments);
}
