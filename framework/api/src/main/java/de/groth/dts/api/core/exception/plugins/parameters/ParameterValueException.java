package de.groth.dts.api.core.exception.plugins.parameters;

import de.groth.dts.api.core.dto.parameters.IParameter;
import de.groth.dts.api.core.exception.DynamicTemplateSystemException;

/**
 * Exception type used by {@link IParameter#getValue()}.
 * 
 * @author Christian Groth
 * 
 */
public class ParameterValueException extends DynamicTemplateSystemException {
    private static final long serialVersionUID = -1539991726753603748L;

    /**
     * Creates a new instance
     * 
     * @param message
     */
    public ParameterValueException(final String message) {
        super(message);
    }

    /**
     * Creates a new instance
     * 
     * @param cause
     */
    public ParameterValueException(final Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new instance
     * 
     * @param message
     * @param cause
     */
    public ParameterValueException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
