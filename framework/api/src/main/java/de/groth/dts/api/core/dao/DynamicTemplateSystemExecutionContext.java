package de.groth.dts.api.core.dao;

import de.groth.dts.api.core.dto.IDynamicTemplateSystem;
import de.groth.dts.api.core.dto.IPage;

/**
 * Context holding information about the current {@link IDynamicTemplateSystem}
 * instance while executing convervions, so the the current instance
 * {@link IPage} is known.
 * 
 * @author Christian Groth
 * 
 */
public class DynamicTemplateSystemExecutionContext extends ProcessingContext {
    private final IPage currentPage;

    /**
     * Creates an new instance.
     * 
     * @param baseDtsPath
     *                the base dts path
     * @param baseExportPath
     *                the base export path
     * @param dtsFilePath
     *                the dts file path (relative to base dts path)
     * @param dts
     *                current {@link IDynamicTemplateSystem}
     * @param currentPage
     *                current {@link IPage}
     */
    public DynamicTemplateSystemExecutionContext(final String baseDtsPath,
            final String baseExportPath, final String dtsFilePath,
            final IDynamicTemplateSystem dts, final IPage currentPage) {
        super(baseDtsPath, baseExportPath, dtsFilePath, dts);
        this.currentPage = currentPage;
    }

    /**
     * Gets the current {@link IPage}.
     * 
     * @return the current {@link IPage}
     */
    public IPage getCurrentPage() {
        return this.currentPage;
    }
}
