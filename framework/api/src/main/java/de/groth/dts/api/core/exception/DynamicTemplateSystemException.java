package de.groth.dts.api.core.exception;

/**
 * Basic exception type for this project.
 * 
 * @author Christian Groth
 * 
 */
public abstract class DynamicTemplateSystemException extends Exception {
    /**
     * Creates a new instance
     * 
     * @param message
     */
    public DynamicTemplateSystemException(final String message) {
        super(message);
    }

    /**
     * Creates a new instance
     * 
     * @param cause
     */
    public DynamicTemplateSystemException(final Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new instance
     * 
     * @param message
     * @param cause
     */
    public DynamicTemplateSystemException(final String message,
            final Throwable cause) {
        super(message, cause);
    }
}
