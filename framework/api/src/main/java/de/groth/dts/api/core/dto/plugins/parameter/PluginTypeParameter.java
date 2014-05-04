package de.groth.dts.api.core.dto.plugins.parameter;

import de.groth.dts.api.core.dto.plugins.AbstractPlugin;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;

/**
 * Class for parameterPlugins, extending {@link AbstractPlugin} with parameter
 * specifics.
 * 
 * @author Christian Groth
 */
public class PluginTypeParameter extends AbstractPlugin {
    /**
     * Creates a new instance
     * 
     * @param key
     *                parameters key
     * @param className
     *                fullqualified classname
     * @throws PluginInitializationException
     */
    public PluginTypeParameter(final String key, final String className)
            throws PluginInitializationException {
        super(key, className);
    }
}
