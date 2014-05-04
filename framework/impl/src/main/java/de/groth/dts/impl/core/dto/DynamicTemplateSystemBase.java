package de.groth.dts.impl.core.dto;

import java.util.HashMap;

import de.groth.dts.api.core.IConverter;
import de.groth.dts.api.core.dto.IDynamicTemplateSystemBase;
import de.groth.dts.api.core.dto.plugins.generics.PluginTypeGeneric;
import de.groth.dts.api.core.dto.plugins.parameter.PluginTypeParameter;
import de.groth.dts.api.core.dto.plugins.processings.PluginTypeProcessing;
import de.groth.dts.api.core.exception.dto.DtoInitializationException;
import de.groth.dts.api.core.exception.plugins.PluginRegistrationException;

/**
 * Default implementation of {@link IDynamicTemplateSystemBase}.
 * 
 * @author Christian Groth
 * 
 */
public class DynamicTemplateSystemBase implements IDynamicTemplateSystemBase {
    private final String filePath;
    private final String id;
    private final String name;
    private final IConverter converter;

    private final HashMap<String, PluginTypeProcessing> pluginsProcessing;
    private final HashMap<String, PluginTypeParameter> pluginsParameter;
    private final HashMap<String, PluginTypeGeneric> pluginsGeneric;

    /**
     * Creates a new instance
     * 
     * @param base
     *                instance of {@link IDynamicTemplateSystemBase} to retrieve
     *                values from
     */
    public DynamicTemplateSystemBase(final IDynamicTemplateSystemBase base) {
        this.filePath = base.getFilePath();
        this.id = base.getId();
        this.name = base.getName();
        this.converter = base.getConverter();

        this.pluginsProcessing = base.getPluginsProcessing();
        this.pluginsParameter = base.getPluginsParameter();
        this.pluginsGeneric = base.getPluginsGeneric();
    }

    /**
     * Creates a new instance
     * 
     * @param filePath
     *                the dts file path
     * @param id
     *                the dts id
     * @param name
     *                the dts name
     * @param converter
     *                instance of {@link IConverter}
     * @throws DtoInitializationException
     */
    public DynamicTemplateSystemBase(final String filePath, final String id,
            final String name, final IConverter converter)
            throws DtoInitializationException {
        this(filePath, id, name, new HashMap<String, PluginTypeProcessing>(),
                new HashMap<String, PluginTypeParameter>(),
                new HashMap<String, PluginTypeGeneric>(), converter);
    }

    /**
     * Creates a new instance
     * 
     * @param filePath
     *                the dts file path
     * @param id
     *                the dts id
     * @param name
     *                the dts name
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
    public DynamicTemplateSystemBase(final String filePath, final String id,
            final String name,
            final HashMap<String, PluginTypeProcessing> pluginsProcessing,
            final HashMap<String, PluginTypeParameter> pluginsParameter,
            final HashMap<String, PluginTypeGeneric> pluginsGeneric,
            final IConverter converter) throws DtoInitializationException {

        if (filePath == null || filePath.trim().equals("")) {
            throw new DtoInitializationException("filePath must not be empty!!");
        }

        if (id == null || id.trim().equals("")) {
            throw new DtoInitializationException("id must not be empty!!");
        }

        if (name == null || name.trim().equals("")) {
            throw new DtoInitializationException("name must not be empty!!");
        }

        if (converter == null) {
            throw new DtoInitializationException("converter must be set!!");
        }

        this.filePath = filePath;
        this.id = id;
        this.name = name;
        this.converter = converter;

        this.pluginsProcessing = pluginsProcessing;
        this.pluginsParameter = pluginsParameter;
        this.pluginsGeneric = pluginsGeneric;
    }

    /**
     * @see de.groth.dts.api.core.dto.IDynamicTemplateSystemBase#getFilePath()
     */
    public String getFilePath() {
        return this.filePath;
    }

    /**
     * @see de.groth.dts.api.core.dto.IDynamicTemplateSystemBase#getId()
     */
    public String getId() {
        return this.id;
    }

    /**
     * @see de.groth.dts.api.core.dto.IDynamicTemplateSystemBase#getName()
     */
    public String getName() {
        return this.name;
    }

    /**
     * @see de.groth.dts.api.core.dto.IDynamicTemplateSystemBase#getPluginProcessing(java.lang.String)
     */
    public PluginTypeProcessing getPluginProcessing(final String key) {
        return this.pluginsProcessing.get(key);
    }

    /**
     * @see de.groth.dts.api.core.dto.IDynamicTemplateSystemBase#getPluginParameter(java.lang.String)
     */
    public PluginTypeParameter getPluginParameter(final String key) {
        return this.pluginsParameter.get(key);
    }

    /**
     * @see de.groth.dts.api.core.dto.IDynamicTemplateSystemBase#getPluginGeneric(java.lang.String)
     */
    public PluginTypeGeneric getPluginGeneric(final String key) {
        return this.pluginsGeneric.get(key);
    }

    /**
     * @see de.groth.dts.api.core.dto.IDynamicTemplateSystemBase#getPluginsProcessing()
     */
    public HashMap<String, PluginTypeProcessing> getPluginsProcessing() {
        return this.pluginsProcessing;
    }

    /**
     * @see de.groth.dts.api.core.dto.IDynamicTemplateSystemBase#getPluginsParameter()
     */
    public HashMap<String, PluginTypeParameter> getPluginsParameter() {
        return this.pluginsParameter;
    }

    /**
     * @see de.groth.dts.api.core.dto.IDynamicTemplateSystemBase#getPluginsGeneric()
     */
    public HashMap<String, PluginTypeGeneric> getPluginsGeneric() {
        return this.pluginsGeneric;
    }

    /**
     * @see de.groth.dts.api.core.dto.IDynamicTemplateSystemBase#registerProcessing(de.groth.dts.api.core.dto.plugins.processings.PluginTypeProcessing)
     */
    public void registerProcessing(final PluginTypeProcessing plugin)
            throws PluginRegistrationException {
        if (plugin.getPluginKey() == null
                || plugin.getPluginKey().trim().equals("")) {
            throw new PluginRegistrationException(
                    "pluginKey must not be empty!!");
        }

        if (this.pluginsProcessing.containsKey(plugin.getPluginKey())) {
            throw new PluginRegistrationException(
                    "duplicate processing registered: " + plugin.getPluginKey()
                            + "!!");
        } else {
            this.pluginsProcessing.put(plugin.getPluginKey(), plugin);
        }
    }

    /**
     * @see de.groth.dts.api.core.dto.IDynamicTemplateSystemBase#registerParameter(de.groth.dts.api.core.dto.plugins.parameter.PluginTypeParameter)
     */
    public void registerParameter(final PluginTypeParameter plugin)
            throws PluginRegistrationException {
        if (plugin.getPluginKey() == null
                || plugin.getPluginKey().trim().equals("")) {
            throw new PluginRegistrationException(
                    "pluginKey must not be empty!!");
        }

        if (this.pluginsParameter.containsKey(plugin.getPluginKey())) {
            throw new PluginRegistrationException(
                    "duplicate parameter registered: " + plugin.getPluginKey()
                            + "!!");
        } else {
            this.pluginsParameter.put(plugin.getPluginKey(), plugin);
        }
    }

    /**
     * @see de.groth.dts.api.core.dto.IDynamicTemplateSystemBase#registerGeneric(de.groth.dts.api.core.dto.plugins.generics.PluginTypeGeneric)
     */
    public void registerGeneric(final PluginTypeGeneric plugin)
            throws PluginRegistrationException {
        if (plugin.getPluginKey() == null
                || plugin.getPluginKey().trim().equals("")) {
            throw new PluginRegistrationException(
                    "pluginKey must not be empty!!");
        } // if

        if (this.pluginsGeneric.containsKey(plugin.getPluginKey())) {
            throw new PluginRegistrationException(
                    "duplicate generic registered: " + plugin.getPluginKey()
                            + "!!");
        } else {
            this.pluginsGeneric.put(plugin.getPluginKey(), plugin);
        }
    }

    /**
     * @see de.groth.dts.api.core.dto.IDynamicTemplateSystemBase#getConverter()
     */
    public IConverter getConverter() {
        return this.converter;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object other) {
        if (other instanceof DynamicTemplateSystemBase) {
            final IDynamicTemplateSystemBase o = (IDynamicTemplateSystemBase) other;
            return o.getId().equals(this.getId());
        } else {
            return false;
        }
    }

    private static final int HASH_CODE_BASE = 17;

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return DynamicTemplateSystemBase.HASH_CODE_BASE * this.id.hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "DynamicTemplateSystemBase: id=" + this.id;
    }
}
