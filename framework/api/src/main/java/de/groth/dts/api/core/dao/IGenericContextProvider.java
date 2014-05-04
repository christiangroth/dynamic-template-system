package de.groth.dts.api.core.dao;

import java.util.List;

import de.groth.dts.api.core.dto.plugins.generics.PluginTypeGeneric;

/**
 * Provider for current active {@link GenericContext}.
 * 
 * @see GenericContext
 * 
 * @author Christian Groth
 * 
 */
public interface IGenericContextProvider {

    /**
     * Provides an instance of {@link GenericContext}
     * 
     * @param generics
     *                list of {@link PluginTypeGeneric}
     * @param source
     *                source for {@link GenericContext}
     * 
     * @return instance of {@link GenericContext}
     */
    GenericContext createGenericContext(List<PluginTypeGeneric> generics,
            String source);
}
