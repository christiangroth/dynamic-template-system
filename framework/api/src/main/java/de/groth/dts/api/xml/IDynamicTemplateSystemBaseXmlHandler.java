package de.groth.dts.api.xml;

import de.groth.dts.api.core.dto.IDynamicTemplateSystem;
import de.groth.dts.api.core.dto.IDynamicTemplateSystemBase;
import de.groth.dts.api.core.dto.IDynamicTemplateSystemProxy;

/**
 * Handler for constructing an instance of {@link IDynamicTemplateSystemProxy}
 * which is needed during instantiation of {@link IDynamicTemplateSystem} and
 * writing it back to xml.
 * 
 * @author Christian Groth
 */
public interface IDynamicTemplateSystemBaseXmlHandler extends
        IDtsDtoXmlHandler<IDynamicTemplateSystemBase> {

}
