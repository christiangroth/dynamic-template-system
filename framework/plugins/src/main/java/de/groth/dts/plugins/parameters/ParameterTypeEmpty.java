package de.groth.dts.plugins.parameters;

import org.dom4j.Element;

import de.groth.dts.api.core.dao.PluginInstantiationContext;
import de.groth.dts.api.core.dto.plugins.PluginConstructor;
import de.groth.dts.api.core.dto.plugins.PluginToXml;
import de.groth.dts.api.core.dto.plugins.parameter.AbstractParameter;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;
import de.groth.dts.api.core.exception.plugins.parameters.ParameterValueException;

/**
 * Defines an empty value "" and can be used as placeholder.
 * 
 * <pre>
 * &lt;param type=&quot;...&quot; name=&quot;...&quot; /&gt;
 * </pre>
 * 
 * @author Christian Groth
 */
public class ParameterTypeEmpty extends AbstractParameter {

    /**
     * Creates a new instance (annotated as {@link PluginConstructor})
     * 
     * @param config
     *                current {@link PluginInstantiationContext}
     * @throws PluginInitializationException
     */
    @PluginConstructor
    public ParameterTypeEmpty(final PluginInstantiationContext config)
            throws PluginInitializationException {
        super(config.getPluginKey(), config.getPluginClassName(), config);
    }

    /**
     * Transforms the current instance to its xml representation.
     * 
     * @param parameterElement
     *                {@link Element} for current instance
     * 
     * @see PluginToXml
     * 
     */
    @PluginToXml
    public void toXml(final Element parameterElement) {
        /* nothing to do here */
    }

    /**
     * @see de.groth.dts.api.core.dto.parameters.IParameter#getValue()
     */
    public String getValue() throws ParameterValueException {
        return "";
    }
}
