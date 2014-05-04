package de.groth.dts.api.core.dto.processings;

import de.groth.dts.api.core.dao.DynamicTemplateSystemExecutionContext;
import de.groth.dts.api.core.dto.IDtsDto;
import de.groth.dts.api.core.dto.plugins.IPlugin;

/**
 * Basic interface for all types of processing.
 * 
 * @author Christian Groth.
 */
public interface IProcessing extends IDtsDto, IPlugin {
    /**
     * Returns the key.
     * 
     * @return the key
     */
    String getProcessingName();

    /**
     * Starts processing.
     * 
     * @param context
     *                an instance of
     *                {@link DynamicTemplateSystemExecutionContext}
     * 
     * @return true, if successful
     */
    boolean process(DynamicTemplateSystemExecutionContext context);
}
