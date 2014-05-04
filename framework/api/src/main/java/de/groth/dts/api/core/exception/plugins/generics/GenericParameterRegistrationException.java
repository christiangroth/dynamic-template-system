package de.groth.dts.api.core.exception.plugins.generics;

import de.groth.dts.api.core.dto.generics.IGeneric;
import de.groth.dts.api.core.exception.plugins.PluginRegistrationException;

/**
 * Exception type is used if a given String representing a generic parameter
 * does not follow the correct syntax.
 * 
 * @see IGeneric for parameter syntax definition
 * 
 * @author Christian Groth
 * 
 */
public class GenericParameterRegistrationException extends
        PluginRegistrationException {
    private static final long serialVersionUID = 6261109293678335900L;

    /**
     * Creates a new instance
     * 
     * @param message
     */
    public GenericParameterRegistrationException(final String message) {
        super(message);
    }

    /**
     * Creates a new instance
     * 
     * @param cause
     */
    public GenericParameterRegistrationException(final Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new instance
     * 
     * @param message
     * @param cause
     */
    public GenericParameterRegistrationException(final String message,
            final Throwable cause) {
        super(message, cause);
    }
}
