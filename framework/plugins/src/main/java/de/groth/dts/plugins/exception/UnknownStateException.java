package de.groth.dts.plugins.exception;

import de.groth.dts.api.core.exception.DynamicTemplateSystemException;
import de.groth.dts.plugins.util.StateValueResolver;

/**
 * Exception type is used by {@link StateValueResolver}.
 * 
 * @author Christian Groth
 * 
 */
public class UnknownStateException extends DynamicTemplateSystemException {
    private static final long serialVersionUID = 4128098489676507234L;

    /**
     * Creates a new instance
     * 
     * @param message
     */
    public UnknownStateException(final String message) {
        super(message);
    }

    /**
     * Creates a new instance
     * 
     * @param cause
     */
    public UnknownStateException(final Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new instance
     * 
     * @param message
     * @param cause
     */
    public UnknownStateException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
