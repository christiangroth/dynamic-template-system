package de.groth.dts.impl.core.dto;

import java.util.HashMap;

import de.groth.dts.api.core.IConverter;
import de.groth.dts.api.core.dao.DynamicTemplateSystemExecutionContext;
import de.groth.dts.api.core.dto.IDynamicTemplateSystemBase;
import de.groth.dts.api.core.dto.IDynamicTemplateSystemProxy;
import de.groth.dts.api.core.dto.plugins.generics.PluginTypeGeneric;
import de.groth.dts.api.core.dto.plugins.parameter.PluginTypeParameter;
import de.groth.dts.api.core.dto.plugins.processings.PluginTypeProcessing;
import de.groth.dts.api.core.exception.dto.DtoInitializationException;
import de.groth.dts.api.xml.IDtsDtoXmlHandler;
import de.groth.dts.impl.xml.DynamicTemplateSystemBaseXmlHandler;

/**
 * Default implementation of {@link IDynamicTemplateSystemProxy}.
 * 
 * @author Christian Groth
 */
public class DynamicTemplateSystemProxy extends DynamicTemplateSystemBase
        implements IDynamicTemplateSystemProxy {

    /**
     * Creates a new instance
     * 
     * @param filePath
     *                dts file path
     * @param id
     *                dts id
     * @param name
     *                dts name
     * @param pluginsProcessing
     *                map of processing plugins to be registered
     * @param pluginsParameter
     *                map of parameter plugins to be registered
     * @param pluginsGeneric
     *                map of generic plugins to be registered
     * @param converter
     *                an {@link IConverter}
     * @throws DtoInitializationException
     */
    public DynamicTemplateSystemProxy(final String filePath, final String id,
            final String name,
            final HashMap<String, PluginTypeProcessing> pluginsProcessing,
            final HashMap<String, PluginTypeParameter> pluginsParameter,
            final HashMap<String, PluginTypeGeneric> pluginsGeneric,
            final IConverter converter) throws DtoInitializationException {
        super(filePath, id, name, pluginsProcessing, pluginsParameter,
                pluginsGeneric, converter);
    }

    /**
     * Creates a new instance
     * 
     * @param filePath
     *                dts file path
     * @param id
     *                dts id
     * @param name
     *                dts name
     * @param converter
     *                an {@link IConverter}
     * @throws DtoInitializationException
     */
    public DynamicTemplateSystemProxy(final String filePath, final String id,
            final String name, final IConverter converter)
            throws DtoInitializationException {
        super(filePath, id, name, converter);
    }

    /**
     * @see de.groth.dts.api.xml.IDtsDtoXmlHandlerProvider#createXmlHandler(de.groth.dts.api.core.dao.DynamicTemplateSystemExecutionContext)
     */
    public IDtsDtoXmlHandler<IDynamicTemplateSystemBase> createXmlHandler(
            final DynamicTemplateSystemExecutionContext context) {
        return new DynamicTemplateSystemBaseXmlHandler(
                context.getDtsFilePath(), this.getConverter());
    }

    /**
     * @see de.groth.dts.impl.core.dto.DynamicTemplateSystemBase#toString()
     */
    @Override
    public String toString() {
        return "DynamicTemplateSystemProxy: id=" + this.getId();
    }
}
