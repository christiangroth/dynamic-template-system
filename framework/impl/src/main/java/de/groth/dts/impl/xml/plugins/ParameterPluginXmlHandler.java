package de.groth.dts.impl.xml.plugins;

import de.groth.dts.api.core.dto.plugins.parameter.PluginTypeParameter;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;
import de.groth.dts.api.xml.structure.PluginXmlInformation;

/**
 * Handler for parameter plugins.
 * 
 * @author Christian Groth
 */
public class ParameterPluginXmlHandler extends
        AbstractPluginXmlHandler<PluginTypeParameter> {

    /**
     * @see de.groth.dts.impl.xml.plugins.AbstractPluginXmlHandler#createPluginDto(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public PluginTypeParameter createPluginDto(final String key,
            final String className) throws PluginInitializationException {
        return new PluginTypeParameter(key, className);
    }

    /**
     * @see de.groth.dts.impl.xml.plugins.AbstractPluginXmlHandler#getXmlTagName()
     */
    @Override
    protected String getXmlTagName() {
        return PluginXmlInformation.PATH_PARAMETER.getValue();
    }
}
