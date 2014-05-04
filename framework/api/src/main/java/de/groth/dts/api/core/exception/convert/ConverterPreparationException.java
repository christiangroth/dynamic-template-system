package de.groth.dts.api.core.exception.convert;

/**
 * Exception is thrown if problems during preparation of converter occur.
 * 
 * @author Christian Groth
 * 
 */
public class ConverterPreparationException extends ConverterException {
    private static final long serialVersionUID = 811320987581926077L;

    /**
     * Creates a new instance
     * 
     * @param message
     */
    public ConverterPreparationException(final String message) {
        super(message);
    }

    /**
     * Creates a new instance
     * 
     * @param cause
     */
    public ConverterPreparationException(final Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new instance
     * 
     * @param message
     * @param cause
     */
    public ConverterPreparationException(final String message,
            final Throwable cause) {
        super(message, cause);
    }
}
