package de.groth.dts.api.core.dto.plugins.processings;

import de.groth.dts.api.core.dto.processings.IProcessing;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;

/**
 * Base class for all concrete processing-plugins defining all plugin and
 * processing capabilities.
 * 
 * @author Christian Groth
 * 
 */
public abstract class AbstractProcessing extends PluginTypeProcessing implements
        IProcessing {
    /**
     * Creates a new instance
     * 
     * @param key
     *                processings key
     * @param className
     *                fullqualified classname
     * @throws PluginInitializationException
     */
    public AbstractProcessing(final String key, final String className)
            throws PluginInitializationException {
        super(key, className);
    }

    /**
     * @see de.groth.dts.api.core.dto.processings.IProcessing#getProcessingName()
     */
    public String getProcessingName() {
        return this.getPluginKey();
    }
}
