package de.groth.dts.api.core.util.plugins;

import org.apache.log4j.Logger;

import de.groth.dts.api.core.dto.IDynamicTemplateSystemBase;
import de.groth.dts.api.core.dto.parameters.IParameter;
import de.groth.dts.api.core.dto.plugins.parameter.PluginTypeParameter;

/**
 * Factory for instantiating {@link IParameter} from {@link PluginTypeParameter}
 * at runtime.
 * 
 * @author Christian Groth
 * 
 */
public class ParameterPluginFactory extends
        AbstractPluginFactory<PluginTypeParameter, IParameter> {
    private static final Logger LOGGER = Logger
            .getLogger(ParameterPluginFactory.class);

    /**
     * @see de.groth.dts.api.core.util.plugins.AbstractPluginFactory#getPlugin(de.groth.dts.api.core.dto.IDynamicTemplateSystemBase,
     *      java.lang.String)
     */
    @Override
    protected PluginTypeParameter getPlugin(
            final IDynamicTemplateSystemBase dts, final String pluginKey) {
        ParameterPluginFactory.LOGGER
                .debug("retrieving parameterPlugin with key " + pluginKey);
        return dts.getPluginParameter(pluginKey);
    }
}
