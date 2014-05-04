package de.groth.dts.impl.core.dto;

import java.io.File;

import de.groth.dts.api.core.dao.DynamicTemplateSystemExecutionContext;
import de.groth.dts.api.core.dto.ITheme;
import de.groth.dts.api.core.dto.parameters.IParameter;
import de.groth.dts.api.core.exception.dto.DtoInitializationException;
import de.groth.dts.api.core.util.FileHelper;
import de.groth.dts.api.xml.IDtsDtoXmlHandler;
import de.groth.dts.impl.xml.ThemeXmlHandler;

/**
 * Default implementation of {@link ITheme}.
 * 
 * @author Christian Groth
 */
public class Theme implements ITheme {

    private final String id;
    private final String templatePath;
    private final String basePath;
    private final File template;
    private final IParameter[] parameter;
    private final String superThemeId;

    /**
     * Creates a new instance
     * 
     * @param id
     *                unique id
     * @param templatePath
     *                path to template file (relative to dts base path)
     * @param basePath
     *                dts base path
     * @param parameter
     *                list of all {@link IParameter} (only own, no inherited)
     * @param superThemeId
     *                id of super theme
     * @throws DtoInitializationException
     */
    public Theme(final String id, final String templatePath,
            final String basePath, final IParameter[] parameter,
            final String superThemeId) throws DtoInitializationException {
        if (id == null || id.trim().equals("")) {
            throw new DtoInitializationException("id must not be empty!!");
        }

        if (basePath == null || basePath.trim().equals("")) {
            throw new DtoInitializationException("basePath must not be empty!!");
        }

        if (templatePath == null || templatePath.trim().equals("")) {
            if (superThemeId == null || superThemeId.trim().equals("")) {
                throw new DtoInitializationException(
                        "templatePath must not be empty or superTheme must be set!!");
            }
        } else if (superThemeId != null && !superThemeId.trim().equals("")) {
            throw new DtoInitializationException(
                    "either templatePath or superTheme must be set, but not both!!");
        }

        this.basePath = basePath;
        this.templatePath = templatePath;
        this.template = templatePath != null && !templatePath.trim().equals("") ? new File(
                FileHelper.combinePath(this.basePath, this.templatePath))
                : null;

        if (this.template != null
                && (!this.template.exists() || !this.template.isFile() || !this.template
                        .canRead())) {
            throw new DtoInitializationException(
                    "template no file or not readable: "
                            + this.template.getAbsolutePath() + "!!");
        }

        this.id = id;
        this.parameter = parameter == null ? new IParameter[] {} : parameter;
        this.superThemeId = superThemeId;
    }

    /**
     * @see de.groth.dts.api.core.dto.ITheme#getId()
     */
    public String getId() {
        return this.id;
    }

    /**
     * @see de.groth.dts.api.core.dto.ITheme#getTemplatePath()
     */
    public String getTemplatePath() {
        return this.templatePath;
    }

    /**
     * @see de.groth.dts.api.core.dto.ITheme#getTemplate()
     */
    public File getTemplate() {
        return this.template;
    }

    /**
     * @see de.groth.dts.api.core.dto.ITheme#getParameter()
     */
    public IParameter[] getParameter() {
        return this.parameter.clone();
    }

    /**
     * @see de.groth.dts.api.core.dto.ITheme#getSuperThemeId()
     */
    public String getSuperThemeId() {
        return this.superThemeId;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof Theme)) {
            return false;
        }

        final ITheme cast = (ITheme) obj;
        return cast.getId().equals(this.getId());
    }

    private static final int HASH_CODE_BASE = 17;

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Theme.HASH_CODE_BASE * this.id.hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Theme: id=" + this.id + ", template=" + this.templatePath
                + ", superThemeId=" + this.superThemeId;
    }

    /**
     * @see de.groth.dts.api.xml.IDtsDtoXmlHandlerProvider#createXmlHandler(de.groth.dts.api.core.dao.DynamicTemplateSystemExecutionContext)
     */
    public IDtsDtoXmlHandler<ITheme> createXmlHandler(
            final DynamicTemplateSystemExecutionContext context) {
        return new ThemeXmlHandler(context.getDts(), context.getBaseDtsPath(),
                context.getBaseExportPath());
    }
}
