package de.groth.dts.api.core.exception.convert;

import de.groth.dts.api.core.IConverter;
import de.groth.dts.api.core.IInvoker;
import de.groth.dts.api.core.exception.DynamicTemplateSystemException;

/**
 * Exception used by {@link IConverter} and {@link IInvoker}
 * 
 * @author Christian Groth
 * 
 */
public class ConverterException extends DynamicTemplateSystemException {
    private static final long serialVersionUID = -7822703747051992345L;

    /**
     * Creates a new instance
     * 
     * @param message
     */
    public ConverterException(final String message) {
        super(message);
    }

    /**
     * Creates a new instance
     * 
     * @param cause
     */
    public ConverterException(final Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new instance
     * 
     * @param message
     * @param cause
     */
    public ConverterException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
