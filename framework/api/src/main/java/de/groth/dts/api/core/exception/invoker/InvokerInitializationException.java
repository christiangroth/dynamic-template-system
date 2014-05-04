package de.groth.dts.api.core.exception.invoker;

import de.groth.dts.api.core.IInvoker;
import de.groth.dts.api.core.exception.DynamicTemplateSystemException;

/**
 * Exception is used by {@link IInvoker#initialize()}.
 * 
 * @author Christian Groth
 * 
 */
public class InvokerInitializationException extends
        DynamicTemplateSystemException {
    private static final long serialVersionUID = 5513172877101646263L;

    /**
     * Creates a new instance
     * 
     * @param message
     */
    public InvokerInitializationException(final String message) {
        super(message);
    }

    /**
     * Creates a new instance
     * 
     * @param cause
     */
    public InvokerInitializationException(final Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new instance
     * 
     * @param message
     * @param cause
     */
    public InvokerInitializationException(final String message,
            final Throwable cause) {
        super(message, cause);
    }
}
