package de.groth.dts.plugins.exception;

import de.groth.dts.api.core.exception.DynamicTemplateSystemException;
import de.groth.dts.plugins.util.PropertyReader;

/**
 * Exception type is used by {@link PropertyReader}.
 * 
 * @author Christian Groth
 * 
 */
public class PropertyReaderException extends DynamicTemplateSystemException {
    private static final long serialVersionUID = -4413655043842160108L;

    /**
     * Creates a new instance
     * 
     * @param message
     */
    public PropertyReaderException(final String message) {
        super(message);
    }

    /**
     * Creates a new instance
     * 
     * @param cause
     */
    public PropertyReaderException(final Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new instance
     * 
     * @param message
     * @param cause
     */
    public PropertyReaderException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
