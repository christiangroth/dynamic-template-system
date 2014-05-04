package de.groth.dts.api.core.dto.plugins.generics;

import de.groth.dts.api.core.dto.generics.IGeneric;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;

/**
 * Base class for all concrete generic-plugins defining all plugin and generic
 * capabilities.
 * 
 * @author Christian Groth
 * 
 */
public abstract class AbstractGeneric extends PluginTypeGeneric implements
        IGeneric {
    /**
     * Creates a new instance
     * 
     * @param key
     *                generics key
     * @param className
     *                fullqualified classname
     * @throws PluginInitializationException
     */
    public AbstractGeneric(final String key, final String className)
            throws PluginInitializationException {
        super(key, className);
    }

    /**
     * @see de.groth.dts.api.core.dto.generics.IGeneric#getGenericName()
     */
    public String getGenericName() {
        return this.getPluginKey();
    }
}
