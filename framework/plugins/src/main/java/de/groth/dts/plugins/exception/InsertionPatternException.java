package de.groth.dts.plugins.exception;

import de.groth.dts.api.core.exception.DynamicTemplateSystemException;
import de.groth.dts.plugins.util.InsertionPatternValueResolver;

/**
 * Exception type used by {@link InsertionPatternValueResolver}.
 * 
 * @author Christian Groth
 * 
 */
public class InsertionPatternException extends DynamicTemplateSystemException {
    private static final long serialVersionUID = -6248757455333232961L;

    /**
     * Creates a new instance
     * 
     * @param message
     */
    public InsertionPatternException(final String message) {
        super(message);
    }

    /**
     * Creates a new instance
     * 
     * @param cause
     */
    public InsertionPatternException(final Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new instance
     * 
     * @param message
     * @param cause
     */
    public InsertionPatternException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
