package de.groth.dts.api.core.dto.plugins.generics;

import de.groth.dts.api.core.dto.plugins.AbstractPlugin;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;

/**
 * Class for genericPlugins, extending {@link AbstractPlugin} with generic
 * specifics.
 * 
 * @author Christian Groth
 */
public class PluginTypeGeneric extends AbstractPlugin {
    /**
     * Creates a new instance
     * 
     * @param key
     *                generics key
     * @param className
     *                fullqualified classname
     * @throws PluginInitializationException
     */
    public PluginTypeGeneric(final String key, final String className)
            throws PluginInitializationException {
        super(key, className);
    }
}
