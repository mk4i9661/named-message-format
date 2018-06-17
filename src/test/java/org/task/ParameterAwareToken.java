package org.task;

class ParameterAwareToken {
    private final Token token;
    private final Object value;

    public ParameterAwareToken(Token token, Object value) {
        this.token = token;
        this.value = value;
    }

    @Override
    public String toString() {
        if (token instanceof NamedGroup) {
            return ((NamedGroup) token).toString(value);
        }
        return token.toString();
    }
}
