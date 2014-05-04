package de.groth.dts.plugins.generics;

import org.apache.log4j.Logger;

import de.groth.dts.api.core.dao.GenericContext;
import de.groth.dts.api.core.dao.PluginInstantiationContext;
import de.groth.dts.api.core.dto.plugins.PluginConstructor;
import de.groth.dts.api.core.dto.plugins.generics.AbstractGeneric;
import de.groth.dts.api.core.exception.dto.UnknownStateValueException;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;
import de.groth.dts.api.core.exception.plugins.generics.GenericValueException;
import de.groth.dts.plugins.exception.UnknownStateException;
import de.groth.dts.plugins.util.StateValueResolver;

/**
 * Invokes the referenced state with the given condition and prints out the
 * value. ${...:state=<someStateId>,value=<someConditionValue>}<br/><br/>
 * 
 * If the value parameter is not given, the state default value will be printed
 * out.
 * 
 * @author Christian Groth
 * 
 */
public class GenericTypeState extends AbstractGeneric {
    private static final Logger LOGGER = Logger
            .getLogger(GenericTypeState.class);

    private static final String PARAMETER_STATE_ID = "state";
    private static final String PARAMETER_STATE_VALUE = "value";

    /**
     * Creates a new instance (annotated as {@link PluginConstructor})
     * 
     * @param config
     *                current {@link PluginInstantiationContext}
     * @throws PluginInitializationException
     */
    @PluginConstructor
    public GenericTypeState(final PluginInstantiationContext config)
            throws PluginInitializationException {
        super(config.getPluginKey(), config.getPluginClassName());
    }

    /**
     * @see de.groth.dts.api.core.dto.generics.IGeneric#getValue(de.groth.dts.api.core.dao.GenericContext)
     */
    public String getValue(final GenericContext context)
            throws GenericValueException {
        final String stateId = context
                .getGenericAttribute(GenericTypeState.PARAMETER_STATE_ID);
        GenericTypeState.LOGGER.debug(this.getGenericName() + ": stateId="
                + stateId);
        if (stateId == null || stateId.trim().equals("")) {
            throw new GenericValueException(
                    "Unable to use state: stateId must not be null or empty!!");
        }

        final String stateValue = context
                .getGenericAttribute(GenericTypeState.PARAMETER_STATE_VALUE);
        GenericTypeState.LOGGER.debug(this.getGenericName() + ": stateValue="
                + stateValue);
        try {
            GenericTypeState.LOGGER.debug(this.getGenericName()
                    + ": invoking StateValueResolver");
            return StateValueResolver.resolveConditionValue(stateId, context
                    .getDts(), stateValue);
        } catch (final UnknownStateValueException ex) {
            throw new GenericValueException(
                    "caught UnknownStateValueException!!", ex);
        } catch (final UnknownStateException ex) {
            throw new GenericValueException(
                    "caught UnknownStateValueException!!", ex);
        }
    }
}
