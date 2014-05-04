package de.groth.dts.impl.xml.plugins;

import de.groth.dts.api.core.dto.plugins.generics.PluginTypeGeneric;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;
import de.groth.dts.api.xml.structure.PluginXmlInformation;

/**
 * Handler for generic plugins.
 * 
 * @author Christian Groth
 */
public class GenericPluginXmlHandler extends
        AbstractPluginXmlHandler<PluginTypeGeneric> {

    /**
     * @see de.groth.dts.impl.xml.plugins.AbstractPluginXmlHandler#createPluginDto(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public PluginTypeGeneric createPluginDto(final String key,
            final String className) throws PluginInitializationException {
        return new PluginTypeGeneric(key, className);
    }

    /**
     * @see de.groth.dts.impl.xml.plugins.AbstractPluginXmlHandler#getXmlTagName()
     */
    @Override
    protected String getXmlTagName() {
        return PluginXmlInformation.PATH_GENERIC.getValue();
    }
}
