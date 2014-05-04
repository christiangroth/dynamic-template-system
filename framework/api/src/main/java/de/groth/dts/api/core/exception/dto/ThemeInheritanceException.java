package de.groth.dts.api.core.exception.dto;

import de.groth.dts.api.core.dto.ITheme;

/**
 * Indicates problems with the inheritance of {@link ITheme} instances.
 * 
 * @author Christian Groth
 * 
 */
public class ThemeInheritanceException extends DtoInitializationException {
    private static final long serialVersionUID = -7254971241188343835L;

    /**
     * Creates a new instance
     * 
     * @param message
     */
    public ThemeInheritanceException(final String message) {
        super(message);
    }

    /**
     * Creates a new instance
     * 
     * @param cause
     */
    public ThemeInheritanceException(final Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new instance
     * 
     * @param message
     * @param cause
     */
    public ThemeInheritanceException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
