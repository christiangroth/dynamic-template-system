package de.groth.dts.api.core.exception.dto;

import de.groth.dts.api.core.exception.DynamicTemplateSystemException;

/**
 * Exception is thrown if an illegal state value is tried to resolve.
 * 
 * @author Christian Groth
 * 
 */
public class UnknownStateValueException extends DynamicTemplateSystemException {
    private static final long serialVersionUID = -4224724600590058407L;

    /**
     * Creates a new instance
     * 
     * @param message
     */
    public UnknownStateValueException(final String message) {
        super(message);
    }

    /**
     * Creates a new instance
     * 
     * @param cause
     */
    public UnknownStateValueException(final Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new instance
     * 
     * @param message
     * @param cause
     */
    public UnknownStateValueException(final String message,
            final Throwable cause) {
        super(message, cause);
    }
}
