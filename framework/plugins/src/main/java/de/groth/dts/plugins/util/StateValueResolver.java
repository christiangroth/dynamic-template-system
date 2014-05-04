package de.groth.dts.plugins.util;

import org.apache.log4j.Logger;

import de.groth.dts.api.core.dto.IDynamicTemplateSystem;
import de.groth.dts.api.core.dto.IState;
import de.groth.dts.api.core.exception.dto.UnknownStateValueException;
import de.groth.dts.plugins.exception.UnknownStateException;

/**
 * Helper class for resolving state values.
 * 
 * @author Christian Groth
 * 
 */
public final class StateValueResolver {
    private static final Logger LOGGER = Logger
            .getLogger(StateValueResolver.class);

    private StateValueResolver() {

    }

    /**
     * Resolves the state value for the given conditionValue. If given
     * conditionValue is null or empty states default value will be returned.
     * 
     * @param stateId
     *                the stateId
     * @param dts
     *                current {@link IDynamicTemplateSystem}
     * @param conditionValue
     *                states conditionValue
     * @return state value or default value if conditionValue null/empty
     * @throws UnknownStateValueException
     *                 if given state value is not present and no default is set
     * @throws UnknownStateException
     *                 if the state can't be resolved by given id
     */
    public static String resolveConditionValue(final String stateId,
            final IDynamicTemplateSystem dts, final String conditionValue)
            throws UnknownStateValueException, UnknownStateException {
        StateValueResolver.LOGGER.debug("resolving state by id " + stateId);
        final IState stateById = dts.getStateById(stateId);
        if (stateById == null) {
            throw new UnknownStateException("Unable to resolve state by id "
                    + stateId + " !!");
        }
        StateValueResolver.LOGGER.debug("got state " + stateById);

        return stateById.getValue(conditionValue);
    }
}
