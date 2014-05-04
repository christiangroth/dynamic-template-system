package de.groth.dts.api.core.dao;

import java.util.HashMap;

import org.dom4j.Node;

import de.groth.dts.api.core.IConverter;
import de.groth.dts.api.core.dto.IDynamicTemplateSystemBase;

/**
 * This class provides all information needed during instatiation of plugins.
 * 
 * @author Christian Groth
 */
public class PluginInstantiationContext extends PathContext {
    /** Constant name for parameter defining plugin name */
    public static final String PLUGIN_PARAMETER_NAME = "pluginAttribute.parameter.name";

    private final IDynamicTemplateSystemBase dtsBase;
    private final IConverter converter;

    private final Node node;
    private final String pluginKey;
    private final String pluginClassName;
    private final HashMap<String, String> pluginAttributes;

    /**
     * Creates a new instance
     * 
     * @param baseDtsPath
     *                the base dts path
     * @param baseExportPath
     *                the base export path
     * @param dtsBase
     *                current instance of {@link IDynamicTemplateSystemBase}
     * @param converter
     *                instance of {@link IConverter}
     * @param dtsFilePath
     *                the dts file path (relative to base dts path)
     * @param node
     *                current {@link Node} of plugin
     * @param pluginKey
     *                the plugin key
     * @param pluginClassName
     *                the plugin classname
     * @param pluginAttributes
     *                all plugin attributes
     */
    public PluginInstantiationContext(final String baseDtsPath,
            final String baseExportPath,
            final IDynamicTemplateSystemBase dtsBase,
            final IConverter converter, final String dtsFilePath,
            final Node node, final String pluginKey,
            final String pluginClassName,
            final HashMap<String, String> pluginAttributes) {
        super(baseDtsPath, baseExportPath, dtsFilePath);
        this.dtsBase = dtsBase;
        this.converter = converter;
        this.node = node;
        this.pluginKey = pluginKey;
        this.pluginClassName = pluginClassName;
        this.pluginAttributes = pluginAttributes;
    }

    /**
     * Gets the {@link IDynamicTemplateSystemBase} instance.
     * 
     * @return the dts
     */
    public IDynamicTemplateSystemBase getDtsBase() {
        return this.dtsBase;
    }

    /**
     * Gets the {@link IConverter}.
     * 
     * @return the converter
     */
    public IConverter getConverter() {
        return this.converter;
    }

    /**
     * Gets the corresponding {@link Node}.
     * 
     * @return the node
     */
    public Node getNode() {
        return this.node;
    }

    /**
     * Gets the plugin key.
     * 
     * @return the plugin key
     */
    public String getPluginKey() {
        return this.pluginKey;
    }

    /**
     * Gets the plugin class name.
     * 
     * @return the plugin class name
     */
    public String getPluginClassName() {
        return this.pluginClassName;
    }

    /**
     * Gets the plugin attributes.
     * 
     * @return the plugin attributes
     */
    public HashMap<String, String> getPluginAttributes() {
        return this.pluginAttributes;
    }

    /**
     * Gets the plugin attribute.
     * 
     * @param attributeName
     *                the attribute name
     * 
     * @return the plugin attribute
     */
    public String getPluginAttribute(final String attributeName) {
        return this.pluginAttributes.get(attributeName);
    }

    /**
     * Adds the plugin attribute.
     * 
     * @param attributeName
     *                the attribute name
     * @param attributeValue
     *                the attribute value
     */
    public void addPluginAttribute(final String attributeName,
            final String attributeValue) {
        this.pluginAttributes.put(attributeName, attributeValue);
    }
}
