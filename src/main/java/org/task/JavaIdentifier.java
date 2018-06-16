package org.task;


public class JavaIdentifier {
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

        if (!Character.isJavaIdentifierStart(name.charAt(0))) {
            throw new InvalidJavaIdentifierException(
                    String.format(
                            "'%s' is not a valid Java identifier.",
                            name
                    )
            );
        }

        if (name.chars()
                .skip(1)
                .anyMatch(c -> !Character.isJavaIdentifierPart(c))) {
            throw new InvalidJavaIdentifierException(
                    String.format(
                            "'%s' is not a valid Java identifier.",
                            name
                    )
            );
        }

        return new JavaIdentifier(name);
    }
}
