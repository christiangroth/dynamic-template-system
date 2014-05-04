package de.groth.dts.api.core.dto.plugins;

import de.groth.dts.api.core.exception.plugins.PluginInitializationException;

/**
 * An abstract base class for all types of plugins.
 * 
 * @author Christian Groth
 */
public abstract class AbstractPlugin implements IPlugin {
    private final String pluginKey;
    private final String pluginClassName;

    /**
     * Creates a new instance.
     * 
     * @param key
     *                plugins key
     * @param className
     *                fullqualified classname
     * @throws PluginInitializationException
     */
    public AbstractPlugin(final String key, final String className)
            throws PluginInitializationException {
        if (key == null || key.trim().equals("")) {
            throw new PluginInitializationException("key must not be empty!!");
        }

        if (className == null || className.trim().equals("")) {
            throw new PluginInitializationException(
                    "className must not be empty!!");
        }

        this.pluginKey = key;
        this.pluginClassName = className;
    }

    /**
     * @see de.groth.dts.api.core.dto.plugins.IPlugin#getPluginClassName()
     */
    public String getPluginClassName() {
        return this.pluginClassName;
    }

    /**
     * @see de.groth.dts.api.core.dto.plugins.IPlugin#getPluginKey()
     */
    public String getPluginKey() {
        return this.pluginKey;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof AbstractPlugin)) {
            return false;
        }

        final AbstractPlugin cast = (AbstractPlugin) obj;
        return this.pluginKey.equals(cast.getPluginKey())
                && this.pluginClassName.equals(cast.getPluginClassName());
    }

    private static final int HASH_CODE_BASE = 17;

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return AbstractPlugin.HASH_CODE_BASE
                * (this.pluginClassName.hashCode() + this.pluginKey.hashCode());
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AbstractPlugin: key=" + this.pluginKey + ", className="
                + this.pluginClassName;
    }
}
