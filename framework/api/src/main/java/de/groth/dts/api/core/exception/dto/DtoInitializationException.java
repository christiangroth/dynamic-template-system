package de.groth.dts.api.core.exception.dto;

import de.groth.dts.api.core.exception.DynamicTemplateSystemException;

/**
 * Exception is thrown if problems during initialization of any data transfer
 * object occur.
 * 
 * @author Christian Groth
 * 
 */
public class DtoInitializationException extends DynamicTemplateSystemException {
    private static final long serialVersionUID = 6783226050866869668L;

    /**
     * Creates a new instance
     * 
     * @param message
     */
    public DtoInitializationException(final String message) {
        super(message);
    }

    /**
     * Creates a new instance
     * 
     * @param cause
     */
    public DtoInitializationException(final Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new instance
     * 
     * @param message
     * @param cause
     */
    public DtoInitializationException(final String message,
            final Throwable cause) {
        super(message, cause);
    }
}
