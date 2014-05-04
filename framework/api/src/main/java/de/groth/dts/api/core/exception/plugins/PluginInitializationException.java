package de.groth.dts.api.core.exception.plugins;

import de.groth.dts.api.core.exception.DynamicTemplateSystemException;

/**
 * Exception is thrown if problems during initialization of a plugin occur.
 * 
 * @author Christian Groth
 * 
 */
public class PluginInitializationException extends
        DynamicTemplateSystemException {
    private static final long serialVersionUID = 3143927214052789520L;

    /**
     * Creates a new instance
     * 
     * @param message
     */
    public PluginInitializationException(final String message) {
        super(message);
    }

    /**
     * Creates a new instance
     * 
     * @param cause
     */
    public PluginInitializationException(final Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new instance
     * 
     * @param message
     * @param cause
     */
    public PluginInitializationException(final String message,
            final Throwable cause) {
        super(message, cause);
    }
}
