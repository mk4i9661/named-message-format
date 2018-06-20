package org.mki.text;

import java.util.Map;

/**
 * A factory that produces ready to use format with parameters bound to the underlying pattern.
 */
interface ParameterBinding {
    /**
     * Produces a ready to use format with parameters bound to the actual pattern.
     * @param parameters
     * @return
     */
    ParameterAwareFormat bindTo(Map<String, Object> parameters);

    default String format(Map<String, Object> parameters, Formatter formatter) {
        return bindTo(parameters).format(formatter);
    }
}
