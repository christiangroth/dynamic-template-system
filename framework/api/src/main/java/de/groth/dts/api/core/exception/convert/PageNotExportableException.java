package de.groth.dts.api.core.exception.convert;

/**
 * Exception is thrown if a page is meant to be exported which does not have the
 * file attribute set (useful for pages that might be included via
 * generic/parameter but not exported).
 * 
 * @author Christian Groth
 * 
 */
public class PageNotExportableException extends ConverterPreparationException {
    private static final long serialVersionUID = 7101224021525377715L;

    /**
     * Creates a new instance
     * 
     * @param message
     */
    public PageNotExportableException(final String message) {
        super(message);
    }

    /**
     * Creates a new instance
     * 
     * @param cause
     */
    public PageNotExportableException(final Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new instance
     * 
     * @param message
     * @param cause
     */
    public PageNotExportableException(final String message,
            final Throwable cause) {
        super(message, cause);
    }
}
