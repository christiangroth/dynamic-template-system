package de.groth.dts.impl.core.dto;

import de.groth.dts.api.core.dao.DynamicTemplateSystemExecutionContext;
import de.groth.dts.api.core.dto.IPage;
import de.groth.dts.api.core.dto.ITheme;
import de.groth.dts.api.core.dto.parameters.IParameter;
import de.groth.dts.api.core.exception.convert.PageNotExportableException;
import de.groth.dts.api.core.exception.dto.DtoInitializationException;
import de.groth.dts.api.core.util.FileHelper;
import de.groth.dts.api.xml.IDtsDtoXmlHandler;
import de.groth.dts.impl.xml.PageXmlHandler;

/**
 * Default implementation of {@link IPage}.
 * 
 * @author Christian Groth
 */
public class Page implements IPage {
    private final String id;
    private final ITheme theme;
    private final String outputFile;
    private final IParameter[] ownParameter;
    private final IParameter[] parameter;

    /**
     * Creates a new instance
     * 
     * @param id
     *                unique id
     * @param theme
     *                {@link ITheme} to be used
     * @param file
     *                output path
     * @param ownParameter
     *                all {@link IParameter}s (only own)
     * @param parameter
     *                all {@link IParameter}s
     * @throws DtoInitializationException
     */
    public Page(final String id, final ITheme theme, final String file,
            final IParameter[] ownParameter, final IParameter[] parameter)
            throws DtoInitializationException {
        if (id == null || id.trim().equals("")) {
            throw new DtoInitializationException("id must not be empty!!");
        }

        if (theme == null) {
            throw new DtoInitializationException("theme must not be empty!!");
        }

        this.id = id;
        this.theme = theme;
        this.ownParameter = ownParameter == null ? new IParameter[] {}
                : ownParameter;
        this.parameter = parameter == null ? new IParameter[] {} : parameter;
        this.outputFile = file;
    }

    /**
     * @see de.groth.dts.api.core.dto.IPage#getId()
     */
    public String getId() {
        return this.id;
    }

    /**
     * @see de.groth.dts.api.core.dto.IPage#getTheme()
     */
    public ITheme getTheme() {
        return this.theme;
    }

    /**
     * @see de.groth.dts.api.core.dto.IPage#getOwnParameter()
     */
    public IParameter[] getOwnParameter() {
        return this.ownParameter.clone();
    }

    /**
     * @see de.groth.dts.api.core.dto.IPage#getParameter()
     */
    public IParameter[] getParameter() {
        return this.parameter.clone();
    }

    /**
     * @see de.groth.dts.api.core.dto.IPage#getOutputFile()
     */
    public String getOutputFile() {
        return this.outputFile;
    }

    /**
     * @see de.groth.dts.api.core.dto.IPage#getDepth()
     */
    public int getDepth() throws PageNotExportableException {
        if (this.outputFile == null || this.outputFile.trim().equals("")) {
            throw new PageNotExportableException(
                    "page is not exportable, so it has no depth in filesystem!!");
        }

        return FileHelper.getFileDepth(this.outputFile);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof Page)) {
            return false;
        }

        final IPage cast = (IPage) obj;
        return cast.getId().equals(this.id);
    }

    private static final int HASH_CODE_BASE = 17;

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Page.HASH_CODE_BASE * this.id.hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Page: id=" + this.id + ", theme=" + this.theme.getId();
    }

    /**
     * @see de.groth.dts.api.xml.IDtsDtoXmlHandlerProvider#createXmlHandler(de.groth.dts.api.core.dao.DynamicTemplateSystemExecutionContext)
     */
    public IDtsDtoXmlHandler<IPage> createXmlHandler(
            final DynamicTemplateSystemExecutionContext context) {
        return new PageXmlHandler(context.getDts(), context.getBaseDtsPath(),
                context.getBaseExportPath(), context.getDts().getThemes());
    }
}
