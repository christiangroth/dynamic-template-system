package de.groth.dts.api.core.dao;

import java.util.List;

import de.groth.dts.api.core.dto.IDynamicTemplateSystem;
import de.groth.dts.api.core.dto.IPage;
import de.groth.dts.api.core.dto.parameters.ILazyInitializationCapable;
import de.groth.dts.api.core.dto.plugins.generics.PluginTypeGeneric;
import de.groth.dts.api.core.dto.plugins.parameter.LazyInitializationParameter;

/**
 * Context used during lazy initialization of parameters.
 * 
 * @see ILazyInitializationCapable
 * @see LazyInitializationParameter
 * 
 * @author Christian Groth
 * 
 */
public class ParameterContext extends DynamicTemplateSystemExecutionContext
        implements IGenericContextProvider {

    /**
     * Creates a new instance
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
    public ParameterContext(final String baseDtsPath,
            final String baseExportPath, final String dtsFilePath,
            final IDynamicTemplateSystem dts, final IPage currentPage) {
        super(baseDtsPath, baseExportPath, dtsFilePath, dts, currentPage);
    }

    /**
     * Creates a {@link GenericContext} for lazy initialization purposes.
     * 
     * @param generics
     *                generics to be in the context
     * @param source
     *                the source string
     * 
     * @return created {@link GenericContext}
     */
    public GenericContext createGenericContext(
            final List<PluginTypeGeneric> generics, final String source) {
        return new GenericContext(this.getBaseDtsPath(), this
                .getBaseExportPath(), this.getDtsFilePath(), this.getDts(),
                this.getCurrentPage(), source, generics);
    }
}
