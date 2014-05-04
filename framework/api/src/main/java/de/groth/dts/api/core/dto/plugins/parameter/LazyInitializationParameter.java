package de.groth.dts.api.core.dto.plugins.parameter;

import java.util.List;

import de.groth.dts.api.core.dao.GenericContext;
import de.groth.dts.api.core.dao.PluginInstantiationContext;
import de.groth.dts.api.core.dto.parameters.ILazyInitializationCapable;
import de.groth.dts.api.core.dto.plugins.generics.PluginTypeGeneric;
import de.groth.dts.api.core.exception.plugins.LazyInitializationException;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;

/**
 * Base class for all concrete parameters with all plugin and parameter
 * capabilities plus basic implementation for lazy initialization.
 * 
 * @author Christian Groth
 * 
 */
public abstract class LazyInitializationParameter extends AbstractParameter
        implements ILazyInitializationCapable {

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
    public LazyInitializationParameter(final String pluginKey,
            final String pluginClassName,
            final PluginInstantiationContext config)
            throws PluginInitializationException {
        super(pluginKey, pluginClassName, config);
    }

    /**
     * @see de.groth.dts.api.core.dto.parameters.ILazyInitializationCapable#getLazyInitializationId()
     */
    public String getLazyInitializationId() {
        return this.getPluginKey();
    }

    /**
     * @see de.groth.dts.api.core.dao.IGenericContextProvider#createGenericContext(java.util.List,
     *      java.lang.String)
     */
    public GenericContext createGenericContext(
            final List<PluginTypeGeneric> generics, final String source) {
        return this.context.createGenericContext(generics, source);
    }

    /**
     * Helper method for throwing an exception if the given original value in
     * {@link ILazyInitializationCapable#setLazyInitializationAttributeValue(String, String)}
     * does not match any current value.
     * 
     * @param originalValue
     *                the unmatched value
     * @throws LazyInitializationException
     *                 the exception which will be thrown
     */
    protected void throwUnmatchedValueForLazyInitializationException(
            final String originalValue) throws LazyInitializationException {
        final StringBuffer sb = new StringBuffer("");
        final String[] initializationAttributeValues = this
                .getLazyInitializationAttributeValues();

        for (int i = 0; i < initializationAttributeValues.length; i++) {
            sb.append(initializationAttributeValues[i]);

            if ((i + 1) < initializationAttributeValues.length) {
                sb.append(", ");
            }
        }

        final StringBuffer possibelValueStringBuffer = new StringBuffer();
        for (int i = 0; i < this.getLazyInitializationAttributeValues().length; i++) {
            possibelValueStringBuffer.append(this
                    .getLazyInitializationAttributeValues()[i]);

            if (i < (this.getLazyInitializationAttributeValues().length - 1)) {
                possibelValueStringBuffer.append(", ");
            }
        }

        throw new LazyInitializationException("Unmatched original value '"
                + originalValue + "' for possible values: "
                + possibelValueStringBuffer.toString());
    }
}
