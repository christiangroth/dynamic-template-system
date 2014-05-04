package de.groth.dts.impl.xml.plugins;

import de.groth.dts.api.core.dto.plugins.processings.PluginTypeProcessing;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;
import de.groth.dts.api.xml.structure.PluginXmlInformation;

/**
 * Handler for processing plugins.
 * 
 * @author Christian Groth
 */
public class ProcessingPluginXmlHandler extends
        AbstractPluginXmlHandler<PluginTypeProcessing> {

    /**
     * @see de.groth.dts.impl.xml.plugins.AbstractPluginXmlHandler#createPluginDto(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public PluginTypeProcessing createPluginDto(final String key,
            final String className) throws PluginInitializationException {
        return new PluginTypeProcessing(key, className);
    }

    /**
     * @see de.groth.dts.impl.xml.plugins.AbstractPluginXmlHandler#getXmlTagName()
     */
    @Override
    protected String getXmlTagName() {
        return PluginXmlInformation.PATH_PROCESSING.getValue();
    }
}
