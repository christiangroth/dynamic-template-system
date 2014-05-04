package de.groth.dts.api.core.dto;

import java.io.File;

import de.groth.dts.api.core.dto.parameters.IParameter;
import de.groth.dts.api.xml.IDtsDtoXmlHandlerProvider;

/**
 * An instance of {@link ITheme} represents the xml-declaration of one theme in
 * your dts-xml file.
 * 
 * @author Christian Groth
 */
public interface ITheme extends IDtsDto, IDtsDtoXmlHandlerProvider<ITheme> {
    /**
     * Returns the theme id
     * 
     * @return id
     */
    String getId();

    /**
     * Returns the path to template file.
     * 
     * @return path to template file or null if super theme exists
     */
    String getTemplatePath();

    /**
     * Returns the corresponding {@link File} object to the template.
     * 
     * @return file object for template file or null if super theme exists
     */
    File getTemplate();

    /**
     * Returns all defined parameters for this theme.
     * 
     * @return defined parameters
     */
    IParameter[] getParameter();

    /**
     * Returns the {@link ITheme} id this theme is baed on. If a super
     * {@link ITheme} exists there must not be an own File via
     * {@link #getTemplate()}. Both {@link #getTemplate()} and
     * {@link #getTemplatePath()} will return null!!
     * 
     * @return super-theme or null if no inheritance is defined for this
     *         instance
     */
    String getSuperThemeId();
}
