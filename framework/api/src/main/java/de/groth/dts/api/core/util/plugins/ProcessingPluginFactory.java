package de.groth.dts.api.core.util.plugins;

import org.apache.log4j.Logger;

import de.groth.dts.api.core.dto.IDynamicTemplateSystemBase;
import de.groth.dts.api.core.dto.plugins.processings.PluginTypeProcessing;
import de.groth.dts.api.core.dto.processings.IProcessing;

/**
 * Factory for instantiating {@link IProcessing} from
 * {@link PluginTypeProcessing} at runtime.
 * 
 * @author Christian Groth
 * 
 */
public class ProcessingPluginFactory extends
        AbstractPluginFactory<PluginTypeProcessing, IProcessing> {
    private static final Logger LOGGER = Logger
            .getLogger(ProcessingPluginFactory.class);

    /**
     * @see de.groth.dts.api.core.util.plugins.AbstractPluginFactory#getPlugin(de.groth.dts.api.core.dto.IDynamicTemplateSystemBase,
     *      java.lang.String)
     */
    @Override
    protected PluginTypeProcessing getPlugin(
            final IDynamicTemplateSystemBase dts, final String pluginKey) {
        ProcessingPluginFactory.LOGGER
                .debug("retrieving processingPlugin with key " + pluginKey);
        return dts.getPluginProcessing(pluginKey);
    }
}
