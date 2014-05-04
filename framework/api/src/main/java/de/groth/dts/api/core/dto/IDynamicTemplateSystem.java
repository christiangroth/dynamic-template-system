package de.groth.dts.api.core.dto;

import java.util.Date;

import de.groth.dts.api.core.IConverter;
import de.groth.dts.api.core.dto.processings.IProcessing;
import de.groth.dts.api.xml.IDtsDtoXmlHandlerProvider;

/**
 * This class is the heart of the whole system. All pages, themes, states,
 * processings ... are held here. An instance of {@link IDynamicTemplateSystem}
 * represents an object of your xml-configuration.
 * 
 * @see IDynamicTemplateSystemProxy
 * @see IPage
 * @see ITheme
 * @see IState
 * @see IProcessing
 * 
 * @author Christian Groth
 */
public interface IDynamicTemplateSystem extends IDynamicTemplateSystemBase,
        IDtsDtoXmlHandlerProvider<IDynamicTemplateSystem> {
    /**
     * Creates a new {@link IDynamicTemplateSystemBase} instance.
     * 
     * @return the new base
     */
    IDynamicTemplateSystemBase getBase();

    /**
     * Retrieves a {@link ITheme} by given themeId
     * 
     * @param id
     *                the themeId
     * @return instance of {@link ITheme}
     */
    ITheme getThemeById(final String id);

    /**
     * Retrieves a {@link IPage} by given pageId
     * 
     * @param id
     *                the pageId
     * @return instance of {@link IPage}
     */
    IPage getPageById(final String id);

    /**
     * Retrieves a {@link IState} by given stateId
     * 
     * @param id
     *                the stateId
     * @return instance of {@link IState}
     */
    IState getStateById(final String id);

    /**
     * Retrieves a {@link IInsertionPattern} by given id.
     * 
     * @param id
     *                the unique id
     * @return instance of {@link IInsertionPattern}
     */
    IInsertionPattern getInsertionPatternById(final String id);

    /**
     * Retrieves all registered {@link ITheme}
     * 
     * @return all registered {@link ITheme}
     */
    ITheme[] getThemes();

    /**
     * Retrieves all registered {@link IPage}
     * 
     * @return all registered {@link IPage}
     */
    IPage[] getPages();

    /**
     * Retrieves all registered {@link IState}
     * 
     * @return all registered {@link IState}
     */
    IState[] getStates();

    /**
     * Retrieves all registered {@link IInsertionPattern}.
     * 
     * @return all registered {@link IInsertionPattern}
     */
    IInsertionPattern[] getInsertionPatterns();

    /**
     * Retrieves all registered {@link IProcessing} for pre-processing
     * 
     * @return all registered {@link IProcessing} for pre-processing
     */
    IProcessing[] getPreProcessings();

    /**
     * Retrieves all registered {@link IProcessing} for post-processing
     * 
     * @return all registered {@link IProcessing} for post-processing
     */
    IProcessing[] getPostProcessings();

    /**
     * Retrieves the date when the page with the given id was converted the last
     * time.
     * 
     * @param pageId
     *                the pageId
     * @return last convert date
     */
    Date getLastConvertCallDate(String pageId);

    /**
     * Retrieves the date when the page with the given id was exported the last
     * time.
     * 
     * @param pageId
     *                the pageId
     * @return last export date
     */
    Date getLastExportCallDate(String pageId);

    /**
     * Retrieves the date of last {@link IConverter#exportAll()} call
     * 
     * @return date of last {@link IConverter#exportAll()} call
     */
    Date getLastExportAllCallDate();
}
