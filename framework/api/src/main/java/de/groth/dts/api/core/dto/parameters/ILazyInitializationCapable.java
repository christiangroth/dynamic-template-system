package de.groth.dts.api.core.dto.parameters;

import de.groth.dts.api.core.dao.IGenericContextProvider;
import de.groth.dts.api.core.exception.plugins.LazyInitializationException;

/**
 * This interface defines an parameter as capable of lazy initialization. This
 * means, that parameter-attributes might contain values that must be
 * initialized at runtime (an attribute holds an generic value).
 * 
 * @author Christian Groth
 */
public interface ILazyInitializationCapable extends IGenericContextProvider {
    /**
     * return an id for logging purposes. To use your parameter plugin key is a
     * good idea.
     * 
     * @return an id
     */
    String getLazyInitializationId();

    /**
     * Gets the strings/attribute-values to search for generics to be
     * initialized
     * 
     * @return the strings to search in
     */
    String[] getLazyInitializationAttributeValues();

    /**
     * Sets the string to search after lazy initialization. Use the original
     * value to determine which attribute-value has to be replaced.
     * 
     * @param newValue
     *                the new value
     * @param originalValue
     *                the original value
     * @throws LazyInitializationException
     *                 in case of lazy initialization error
     */
    void setLazyInitializationAttributeValue(String newValue,
            String originalValue) throws LazyInitializationException;

    /**
     * Called after lazyInitialization and before resolveValue
     */
    void postLazyInitializationCallback();
}
