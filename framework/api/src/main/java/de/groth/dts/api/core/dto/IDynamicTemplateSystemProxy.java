package de.groth.dts.api.core.dto;

import de.groth.dts.api.xml.IDtsDtoXmlHandlerProvider;

/**
 * Represents a proxy of {@link IDynamicTemplateSystem}, which is identical
 * with {@link IDynamicTemplateSystemBase}. Because of
 * {@link IDtsDtoXmlHandlerProvider} the {@link IDynamicTemplateSystemBase} had
 * to be created.
 * 
 * @author Christian Groth
 */
public interface IDynamicTemplateSystemProxy extends
        IDynamicTemplateSystemBase,
        IDtsDtoXmlHandlerProvider<IDynamicTemplateSystemBase> {

}
