package de.groth.dts.api.core.dto.plugins;

import de.groth.dts.api.core.dto.IDtsDto;
import de.groth.dts.api.core.dto.IDynamicTemplateSystemBase;

/**
 * Basic interface for all types of plugin to the
 * {@link IDynamicTemplateSystemBase}.
 * 
 * @author Christian Groth
 */
public interface IPlugin extends IDtsDto {
    /**
     * Returns the key of the plugin.
     * 
     * @return the key
     */
    String getPluginKey();

    /**
     * Returns the className of the plugin.
     * 
     * @return the class name
     */
    String getPluginClassName();
}
