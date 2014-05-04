package de.groth.dts.api.core.exception.plugins.generics;

import de.groth.dts.api.core.dao.GenericContext;
import de.groth.dts.api.core.dto.generics.IGeneric;
import de.groth.dts.api.core.exception.DynamicTemplateSystemException;

/**
 * Exception type used by {@link IGeneric#getValue(GenericContext)}
 * 
 * @author Christian Groth
 * 
 */
public class GenericValueException extends DynamicTemplateSystemException {
    private static final long serialVersionUID = -3522163402853882413L;

    /**
     * Creates a new instance
     * 
     * @param message
     */
    public GenericValueException(final String message) {
        super(message);
    }

    /**
     * Creates a new instance
     * 
     * @param cause
     */
    public GenericValueException(final Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new instance
     * 
     * @param message
     * @param cause
     */
    public GenericValueException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
