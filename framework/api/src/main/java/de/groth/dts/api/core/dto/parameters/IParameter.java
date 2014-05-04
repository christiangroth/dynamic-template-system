package de.groth.dts.api.core.dto.parameters;

import de.groth.dts.api.core.dao.ParameterContext;
import de.groth.dts.api.core.dto.plugins.IPlugin;
import de.groth.dts.api.core.exception.plugins.parameters.ParameterValueException;

/**
 * Basic interface for all parameter-types.
 * 
 * @author Christian Groth
 */
public interface IParameter extends IPlugin {
    /**
     * Returns the key (parameter-name)
     * 
     * @return the key
     */
    String getParameterName();

    /**
     * Return the type
     * 
     * @return the type
     */
    String getParameterType();

    /**
     * Prepares the parameter to be resolved in one or two steps. First it will
     * be lazy initialized if the concrete parameter implements
     * {@link ILazyInitializationCapable}. The second step resolves the value
     * via {@link #getValue()}.
     * 
     * @param context
     *                the current {@link ParameterContext}
     */
    void prepare(ParameterContext context);

    /**
     * Computes the value with given context.
     * 
     * @return the value
     * 
     * @throws ParameterValueException
     *                 if there is a problem resolving the value
     */
    String getValue() throws ParameterValueException;
}
