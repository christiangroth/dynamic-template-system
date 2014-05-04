package de.groth.dts.api.xml.exception;

import de.groth.dts.api.core.exception.DynamicTemplateSystemException;
import de.groth.dts.api.xml.util.XmlHelper;

/**
 * Exception type is used by {@link XmlHelper}.
 * 
 * @author Christian Groth
 */
public class XmlException extends DynamicTemplateSystemException {
    private static final long serialVersionUID = 7283745659840635817L;

    /**
     * Creates a new instance
     * 
     * @param message
     */
    public XmlException(final String message) {
        super(message);
    }

    /**
     * Creates a new instance
     * 
     * @param cause
     */
    public XmlException(final Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new instance
     * 
     * @param message
     * @param cause
     */
    public XmlException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
