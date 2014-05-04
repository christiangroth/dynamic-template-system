package de.groth.dts.api.xml;

import de.groth.dts.api.core.dao.DynamicTemplateSystemExecutionContext;
import de.groth.dts.api.core.dto.IDtsDto;

/**
 * Provides an typed instance of {@link IDtsDtoXmlHandler} for the given type T,
 * which must be a subclass of {@link IDtsDto}.
 * 
 * @author Christian Groth
 * 
 * @param <T>
 *                the concrete type to be handled by {@link IDtsDtoXmlHandler}
 */
public interface IDtsDtoXmlHandlerProvider<T extends IDtsDto> {
    /**
     * Creates and returns an typed instance of {@link IDtsDtoXmlHandler} at
     * runtime.
     * 
     * @param context
     *                instance of {@link DynamicTemplateSystemExecutionContext}
     * @return the xml handler
     */
    IDtsDtoXmlHandler<T> createXmlHandler(
            DynamicTemplateSystemExecutionContext context);
}
