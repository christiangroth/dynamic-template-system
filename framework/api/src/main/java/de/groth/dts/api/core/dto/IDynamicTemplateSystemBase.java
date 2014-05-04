package de.groth.dts.api.core.dto;

import java.util.HashMap;

import de.groth.dts.api.core.IConverter;
import de.groth.dts.api.core.dto.plugins.generics.PluginTypeGeneric;
import de.groth.dts.api.core.dto.plugins.parameter.PluginTypeParameter;
import de.groth.dts.api.core.dto.plugins.processings.PluginTypeProcessing;
import de.groth.dts.api.core.exception.plugins.PluginRegistrationException;

/**
 * Represents the base of a {@link IDynamicTemplateSystem} and
 * {@link IDynamicTemplateSystemProxy}. From here you get the xml-file, name
 * and id of your project plus all registered plugins.
 * 
 * @author Christian Groth
 */
public interface IDynamicTemplateSystemBase extends IDtsDto {
    /**
     * Gets the path of xml file.
     * 
     * @return the file path
     */
    String getFilePath();

    /**
     * Gets the project-id defined in xml file.
     * 
     * @return the id
     */
    String getId();

    /**
     * Gets the project-name defined in xml file.
     * 
     * @return the name
     */
    String getName();

    /**
     * Get processing-plugin by key (as defined in xml-file).
     * 
     * @param key
     *                the key
     * 
     * @return the plugin processing
     */
    PluginTypeProcessing getPluginProcessing(final String key);

    /**
     * Get parameter-plugin by key (as defined in xml-file)
     * 
     * @param key
     *                the key
     * 
     * @return the plugin parameter
     */
    PluginTypeParameter getPluginParameter(final String key);

    /**
     * Get generic-plugin by key (as defined in xml-file)
     * 
     * @param key
     *                the key
     * 
     * @return the plugin generic
     */
    PluginTypeGeneric getPluginGeneric(final String key);

    /**
     * Get map of all processing-plugins with their key as map-key.
     * 
     * @return the plugins processing
     */
    HashMap<String, PluginTypeProcessing> getPluginsProcessing();

    /**
     * Get map of all parameter-plugins with their key as map-key.
     * 
     * @return the plugins parameter
     */
    HashMap<String, PluginTypeParameter> getPluginsParameter();

    /**
     * Get map of all generic-plugins with their key as map-key.
     * 
     * @return the plugins generic
     */
    HashMap<String, PluginTypeGeneric> getPluginsGeneric();

    /**
     * Register new processing-plugin. Throws
     * {@link PluginRegistrationException} if key is null or already registered.
     * 
     * @param plugin
     *                the plugin
     * 
     * @throws PluginRegistrationException
     *                 the {@link PluginRegistrationException}
     */
    void registerProcessing(final PluginTypeProcessing plugin)
            throws PluginRegistrationException;

    /**
     * Register new parameter-plugin. Throws {@link PluginRegistrationException}
     * if key is null or already registered.
     * 
     * @param plugin
     *                the plugin
     * 
     * @throws PluginRegistrationException
     *                 the {@link PluginRegistrationException}
     */
    void registerParameter(final PluginTypeParameter plugin)
            throws PluginRegistrationException;

    /**
     * Register new generic-plugin. Throws {@link PluginRegistrationException}
     * if key is null or already registered.
     * 
     * @param plugin
     *                the plugin
     * 
     * @throws PluginRegistrationException
     *                 the {@link PluginRegistrationException}
     */
    void registerGeneric(final PluginTypeGeneric plugin)
            throws PluginRegistrationException;

    /**
     * Retrieves the current {@link IConverter}
     * 
     * @return current {@link IConverter}
     */
    IConverter getConverter();
}
