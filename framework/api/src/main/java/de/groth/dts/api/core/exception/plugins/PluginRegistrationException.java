package de.groth.dts.api.core.exception.plugins;

import de.groth.dts.api.core.exception.DynamicTemplateSystemException;

/**
 * Exception is thrown if problems during registration of a plugin occur.
 * 
 * @author Christian Groth
 * 
 */
public class PluginRegistrationException extends DynamicTemplateSystemException {
    private static final long serialVersionUID = 6986226103573352263L;

    /**
     * Creates a new instance
     * 
     * @param message
     */
    public PluginRegistrationException(final String message) {
        super(message);
    }

    /**
     * Creates a new instance
     * 
     * @param cause
     */
    public PluginRegistrationException(final Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new instance
     * 
     * @param message
     * @param cause
     */
    public PluginRegistrationException(final String message,
            final Throwable cause) {
        super(message, cause);
    }
}
