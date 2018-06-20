package org.mki.text;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * A value class that represents a valid Java identifier.
 */
class JavaIdentifier {
    static final String JAVA_IDENTIFIER_PATTERN_VALUE =
            "\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*";

    private static final Pattern JAVA_IDENTIFIER_PATTERN =
            Pattern.compile(JAVA_IDENTIFIER_PATTERN_VALUE);

    private final String name;

    private JavaIdentifier(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static JavaIdentifier of(String name) throws InvalidJavaIdentifierException {
        //does not check whether the name is a reserved Java keyword, such as 'int', 'synchronized' etc.

        if (name == null || name.length() == 0)
            throw new InvalidJavaIdentifierException("Empty string is not a valid Java identifier.");

        if (!JAVA_IDENTIFIER_PATTERN.matcher(name).matches()) {
            throw new InvalidJavaIdentifierException(
                    String.format(
                            "'%s' is not a valid Java identifier.",
                            name
                    )
            );
        }

        return new JavaIdentifier(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JavaIdentifier that = (JavaIdentifier) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
