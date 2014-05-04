package de.groth.dts.api.core.util.plugins;

import org.apache.log4j.Logger;

import de.groth.dts.api.core.dto.IDynamicTemplateSystemBase;
import de.groth.dts.api.core.dto.generics.IGeneric;
import de.groth.dts.api.core.dto.plugins.generics.PluginTypeGeneric;

/**
 * Factory for instantiating {@link IGeneric} from {@link PluginTypeGeneric} at
 * runtime.
 * 
 * @author Christian Groth
 * 
 */
public class GenericPluginFactory extends
        AbstractPluginFactory<PluginTypeGeneric, IGeneric> {
    private static final Logger LOGGER = Logger
            .getLogger(GenericPluginFactory.class);

    /**
     * @see de.groth.dts.api.core.util.plugins.AbstractPluginFactory#getPlugin(de.groth.dts.api.core.dto.IDynamicTemplateSystemBase,
     *      java.lang.String)
     */
    @Override
    protected PluginTypeGeneric getPlugin(final IDynamicTemplateSystemBase dts,
            final String pluginKey) {
        GenericPluginFactory.LOGGER.debug("retrieving genericPlugin with key "
                + pluginKey);
        return dts.getPluginGeneric(pluginKey);
    }
}
