package de.groth.dts.api.core.dto.generics;

import de.groth.dts.api.core.dao.GenericContext;
import de.groth.dts.api.core.dto.plugins.IPlugin;
import de.groth.dts.api.core.exception.plugins.generics.GenericValueException;

/**
 * Basic interface for all generic, which also defines the concrete syntax for
 * generic parameters.
 * 
 * @author Christian Groth
 */
public interface IGeneric extends IPlugin {
    /**
     * Delimiter between generics name and parameter section
     */
    String GENERIC_PARAMETER_SECTION_DELIMITER = ":";
    /**
     * Parameter key/value delimiter
     */
    String GENERIC_PARAMETER_KEY_VALUE_DELIMITER = "=";
    /**
     * Parameter key/value-pair delimiter
     */
    String GENERIC_PARAMETER_DELIMITER = ",";

    /**
     * Returns the key (generic-name without parameters)
     * 
     * @return the key
     */
    String getGenericName();

    /**
     * This method computes the generic value with the given context.
     * 
     * @param context
     *                the context
     * 
     * @return the value
     * 
     * @throws GenericValueException
     *                 the generic value exception
     */
    String getValue(final GenericContext context) throws GenericValueException;
}
