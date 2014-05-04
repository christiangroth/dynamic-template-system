package de.groth.dts.api.core.dao;

import de.groth.dts.api.core.dto.IDynamicTemplateSystem;
import de.groth.dts.api.core.dto.IDynamicTemplateSystemBase;

/**
 * ProcessingContext additionally holding the current instance of
 * {@link IDynamicTemplateSystemBase}.
 * 
 * @author Christian Groth
 * 
 */
public class ProcessingContext extends PathContext {
    private final IDynamicTemplateSystem dts;

    /**
     * Creates an new instance
     * 
     * @param baseDtsPath
     *                the base dts path
     * @param baseExportPath
     *                the base export path
     * @param dtsFilePath
     *                the dts file path (relative to base dts path)
     * @param dts
     *                the current {@link IDynamicTemplateSystem}
     */
    public ProcessingContext(final String baseDtsPath,
            final String baseExportPath, final String dtsFilePath,
            final IDynamicTemplateSystem dts) {
        super(baseDtsPath, baseExportPath, dtsFilePath);
        this.dts = dts;
    }

    /**
     * Returns the current instance of {@link IDynamicTemplateSystem}.
     * 
     * @return instance of {@link IDynamicTemplateSystem}
     */
    public IDynamicTemplateSystem getDts() {
        return this.dts;
    }
}
