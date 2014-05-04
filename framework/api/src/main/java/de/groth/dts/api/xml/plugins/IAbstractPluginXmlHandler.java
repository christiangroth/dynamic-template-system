package de.groth.dts.api.xml.plugins;

import de.groth.dts.api.core.dto.plugins.AbstractPlugin;
import de.groth.dts.api.xml.IDtsDtoXmlHandler;

/**
 * An abstract and generic handler for plugin-definition purposes.
 * 
 * @param <T>
 *                concrete {@link AbstractPlugin} to be handled
 * 
 * @author Christian Groth
 * 
 */
public interface IAbstractPluginXmlHandler<T extends AbstractPlugin> extends
        IDtsDtoXmlHandler<T> {

}
