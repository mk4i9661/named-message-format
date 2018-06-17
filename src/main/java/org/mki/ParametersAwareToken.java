package org.mki;

import java.util.Map;

/**
 * Serves as a dispatcher to a known toString method
 */
public class ParametersAwareToken implements Token {
    private final Token token;
    private final Map<String, Object> parameters;

    public ParametersAwareToken(Token token, Map<String, Object> parameters) {
        this.token = token;
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        if (token instanceof NamedToken) {
            NamedToken namedToken = (NamedToken) this.token;
            String identifier = namedToken.identifier().toString();
            if (parameters.containsKey(identifier)) {
                return namedToken.toString(parameters.get(identifier));
            }
        }
        return token.toString();
    }
}
