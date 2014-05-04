package de.groth.dts.api.core.dto.plugins.processings;

import de.groth.dts.api.core.dto.plugins.AbstractPlugin;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;

/**
 * Class for processingPlugins, extending {@link AbstractPlugin} with processing
 * specifics.
 * 
 * @author Christian Groth
 */
public class PluginTypeProcessing extends AbstractPlugin {
    /**
     * Creates a new instance
     * 
     * @param key
     *                processings key
     * @param className
     *                fullqualified classname
     * @throws PluginInitializationException
     */
    public PluginTypeProcessing(final String key, final String className)
            throws PluginInitializationException {
        super(key, className);
    }
}
