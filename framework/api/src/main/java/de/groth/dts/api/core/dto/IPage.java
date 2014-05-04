package de.groth.dts.api.core.dto;

import de.groth.dts.api.core.dto.parameters.IParameter;
import de.groth.dts.api.core.exception.convert.PageNotExportableException;
import de.groth.dts.api.xml.IDtsDtoXmlHandlerProvider;

/**
 * An instance of {@link IPage} represents the xml-declaration of one page in
 * your dts-xml file.
 * 
 * @author Christian Groth
 */
public interface IPage extends IDtsDto, IDtsDtoXmlHandlerProvider<IPage> {
    /**
     * Returns the page id.
     * 
     * @return page id
     */
    String getId();

    /**
     * Returns the theme.
     * 
     * @return theme id
     */
    ITheme getTheme();

    /**
     * Returns all parameters which are defined for this page only.
     * 
     * @return all page parameters
     */
    IParameter[] getOwnParameter();

    /**
     * Returns all defined parameters for this page.
     * 
     * @return all page and theme parameters
     */
    IParameter[] getParameter();

    /**
     * Returns the defined output file path.
     * 
     * @return output file path
     */
    String getOutputFile();

    /**
     * Returns the depth of exported file in filesystem (0 for root directory).
     * 
     * @return depth of exported file in filesystem
     * @throws PageNotExportableException
     *                 if page is not exportable
     */
    int getDepth() throws PageNotExportableException;
}
