package de.groth.dts.api.core.dto.plugins.parameter;

import org.apache.log4j.Logger;

import de.groth.dts.api.core.dao.ParameterContext;
import de.groth.dts.api.core.dao.PluginInstantiationContext;
import de.groth.dts.api.core.dto.parameters.IParameter;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;

/**
 * Base class for all concrete parameter-plugins defining all plugin and
 * parameter capabilities.
 * 
 * @author Christian Groth
 * 
 */
public abstract class AbstractParameter extends PluginTypeParameter implements
        IParameter {
    private static final Logger LOGGER = Logger
            .getLogger(AbstractParameter.class);

    /**
     * Instance of {@link ParameterContext} which is set during
     * {@link #prepare(ParameterContext)}
     */
    protected ParameterContext context;
    private final String parameterName;

    /**
     * Creates a new instance
     * 
     * @param pluginKey
     *                parameters key
     * @param pluginClassName
     *                fullqualified classname
     * @param config
     *                current {@link PluginInstantiationContext}
     * @throws PluginInitializationException
     *                 in case of missing/incorrect parameters
     */
    public AbstractParameter(final String pluginKey,
            final String pluginClassName,
            final PluginInstantiationContext config)
            throws PluginInitializationException {
        super(pluginKey, pluginClassName);

        this.parameterName = config
                .getPluginAttribute(PluginInstantiationContext.PLUGIN_PARAMETER_NAME);

        if (this.parameterName == null || this.parameterName.trim().equals("")) {
            throw new PluginInitializationException(this.getClass().getName()
                    + ": parameterName must not be empty!!");
        }
    }

    /**
     * @see de.groth.dts.api.core.dto.parameters.IParameter#getParameterName()
     */
    public String getParameterName() {
        return this.parameterName;
    }

    /**
     * @see de.groth.dts.api.core.dto.parameters.IParameter#getParameterType()
     */
    public String getParameterType() {
        return this.getPluginKey();
    }

    /**
     * Sets the given instance of {@link ParameterContext} as local member
     * variable.
     * 
     * @param context
     *                the current {@link ParameterContext}
     */
    public void prepare(final ParameterContext context) {
        AbstractParameter.LOGGER.debug("setting ParameterContext for "
                + this.parameterName + "(" + this.getPluginKey() + ")");
        this.context = context;
    }

    /**
     * @see de.groth.dts.api.core.dto.plugins.AbstractPlugin#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof AbstractParameter)) {
            return false;
        }

        final AbstractParameter cast = (AbstractParameter) obj;

        return this.parameterName.equals(cast.getParameterName());
    }

    private static final int HASH_CODE_BASE = 17;

    /**
     * @see de.groth.dts.api.core.dto.plugins.AbstractPlugin#hashCode()
     */
    @Override
    public int hashCode() {
        return AbstractParameter.HASH_CODE_BASE
                * (super.hashCode() + this.parameterName.hashCode());
    }

    /**
     * @see de.groth.dts.api.core.dto.plugins.AbstractPlugin#toString()
     */
    @Override
    public String toString() {
        return "AbstractParameter: key=" + this.getPluginKey() + ", className="
                + this.getPluginClassName() + ", parameterName="
                + this.parameterName;
    }
}
