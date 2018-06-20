package org.mki.text;

/**
 * Represents a pattern with actual parameters bound to it.
 */
interface ParameterAwareFormat {
    String format(Formatter formatter);
}
