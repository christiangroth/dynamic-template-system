package de.groth.dts.api.core.exception.plugins;

/**
 * Exception is thrown if problems during lazy initialization of parameters
 * occur.
 * 
 * @author Christian Groth
 * 
 */
public class LazyInitializationException extends PluginInitializationException {
    private static final long serialVersionUID = -464267564898279767L;

    /**
     * Creates a new instance
     * 
     * @param message
     */
    public LazyInitializationException(final String message) {
        super(message);
    }

    /**
     * Creates a new instance
     * 
     * @param cause
     */
    public LazyInitializationException(final Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new instance
     * 
     * @param message
     * @param cause
     */
    public LazyInitializationException(final String message,
            final Throwable cause) {
        super(message, cause);
    }
}
