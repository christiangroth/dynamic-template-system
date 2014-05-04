package de.groth.dts.impl.core.dto;

import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import de.groth.dts.api.core.dao.DynamicTemplateSystemExecutionContext;
import de.groth.dts.api.core.dto.IDynamicTemplateSystem;
import de.groth.dts.api.core.dto.IDynamicTemplateSystemBase;
import de.groth.dts.api.core.dto.IInsertionPattern;
import de.groth.dts.api.core.dto.IPage;
import de.groth.dts.api.core.dto.IState;
import de.groth.dts.api.core.dto.ITheme;
import de.groth.dts.api.core.dto.processings.IProcessing;
import de.groth.dts.api.core.exception.dto.DtoInitializationException;
import de.groth.dts.api.core.util.IThemeHelper;
import de.groth.dts.api.xml.IDtsDtoXmlHandler;
import de.groth.dts.impl.xml.DynamicTemplateSystemXmlHandler;

/**
 * Default implementation if {@link IDynamicTemplateSystem}.
 * 
 * @author Christian Groth
 */
public class DynamicTemplateSystem extends DynamicTemplateSystemBase implements
        IDynamicTemplateSystem {
    private static final Logger LOGGER = Logger
            .getLogger(DynamicTemplateSystem.class);

    private final ITheme[] themes;
    private final IPage[] pages;
    private final IState[] states;
    private final IInsertionPattern[] insertionPatterns;

    private final IProcessing[] preProcessings;
    private final IProcessing[] postProcessings;

    private final HashMap<String, ITheme> cacheThemeById;
    private final HashMap<String, IPage> cachePageById;
    private final HashMap<String, IState> cacheStateById;
    private final HashMap<String, IInsertionPattern> cacheInsertionPatternById;

    /**
     * Creates a new instance
     * 
     * @param base
     *                an {@link IDynamicTemplateSystemBase}
     * @param themes
     *                all {@link ITheme}s
     * @param pages
     *                all {@link IPage}s
     * @param states
     *                all {@link IState}s
     * @param insertionPatterns
     *                all {@link IInsertionPattern}s
     * @param preProcessings
     *                all {@link IProcessing}s for pre-processing
     * @param postProcessings
     *                all {@link IProcessing}s for post-processing
     * @throws DtoInitializationException
     */
    public DynamicTemplateSystem(final IDynamicTemplateSystemBase base,
            final ITheme[] themes, final IPage[] pages, final IState[] states,
            final IInsertionPattern[] insertionPatterns,
            final IProcessing[] preProcessings,
            final IProcessing[] postProcessings)
            throws DtoInitializationException {
        super(base);

        if (themes == null || themes.length < 0) {
            throw new DtoInitializationException(
                    "there must be a minimum of one theme!!");
        }

        if (pages == null || pages.length < 0) {
            throw new DtoInitializationException(
                    "there must be a minimum of one page!!");
        }

        this.themes = themes.clone();
        this.pages = pages.clone();
        this.states = states == null ? new IState[0] : states.clone();
        this.insertionPatterns = insertionPatterns == null ? new IInsertionPattern[0]
                : insertionPatterns.clone();

        this.preProcessings = preProcessings == null ? new IProcessing[0]
                : preProcessings.clone();
        this.postProcessings = postProcessings == null ? new IProcessing[0]
                : postProcessings.clone();

        for (final ITheme theme : this.themes) {
            IThemeHelper.checkThemeInheritance(theme.getId(), this.themes);
        }

        this.cacheThemeById = new HashMap<String, ITheme>();
        for (final ITheme theme : this.themes) {
            if (this.cacheThemeById.containsKey(theme.getId())) {
                DynamicTemplateSystem.LOGGER.warn("ignoring duplicate themeId "
                        + theme.getId() + "!!");
            } else {
                this.cacheThemeById.put(theme.getId(), theme);
            }
        }

        this.cachePageById = new HashMap<String, IPage>();
        for (final IPage page : this.pages) {
            if (this.cachePageById.containsKey(page.getId())) {
                DynamicTemplateSystem.LOGGER.warn("ignoring duplicate pageId "
                        + page.getId() + "!!");
            } else {
                this.cachePageById.put(page.getId(), page);
            }
        }

        this.cacheStateById = new HashMap<String, IState>();
        for (final IState state : this.states) {
            if (this.cacheStateById.containsKey(state.getId())) {
                DynamicTemplateSystem.LOGGER.warn("ignoring duplicate stateId "
                        + state.getId() + "!!");
            } else {
                this.cacheStateById.put(state.getId(), state);
            }
        }

        this.cacheInsertionPatternById = new HashMap<String, IInsertionPattern>();
        for (final IInsertionPattern insertionPattern : this.insertionPatterns) {
            if (this.cacheInsertionPatternById.containsKey(insertionPattern
                    .getId())) {
                DynamicTemplateSystem.LOGGER
                        .warn("ignoring duplicate insertionPatternId "
                                + insertionPattern.getId() + "!!");
            } else {
                this.cacheInsertionPatternById.put(insertionPattern.getId(),
                        insertionPattern);
            }
        }
    }

    /**
     * @see de.groth.dts.api.core.dto.IDynamicTemplateSystem#getBase()
     */
    public DynamicTemplateSystemBase getBase() {
        return new DynamicTemplateSystemBase(this);
    }

    /**
     * @see de.groth.dts.api.core.dto.IDynamicTemplateSystem#getThemeById(java.lang.String)
     */
    public ITheme getThemeById(final String id) {
        return this.cacheThemeById.get(id);
    }

    /**
     * @see de.groth.dts.api.core.dto.IDynamicTemplateSystem#getPageById(java.lang.String)
     */
    public IPage getPageById(final String id) {
        return this.cachePageById.get(id);
    }

    /**
     * @see de.groth.dts.api.core.dto.IDynamicTemplateSystem#getStateById(java.lang.String)
     */
    public IState getStateById(final String id) {
        return this.cacheStateById.get(id);
    }

    /**
     * @see de.groth.dts.api.core.dto.IDynamicTemplateSystem#getInsertionPatternById(java.lang.String)
     */
    public IInsertionPattern getInsertionPatternById(final String id) {
        return this.cacheInsertionPatternById.get(id);
    }

    /**
     * @see de.groth.dts.api.core.dto.IDynamicTemplateSystem#getThemes()
     */
    public ITheme[] getThemes() {
        return this.themes.clone();
    }

    /**
     * @see de.groth.dts.api.core.dto.IDynamicTemplateSystem#getPages()
     */
    public IPage[] getPages() {
        return this.pages.clone();
    }

    /**
     * @see de.groth.dts.api.core.dto.IDynamicTemplateSystem#getStates()
     */
    public IState[] getStates() {
        return this.states.clone();
    }

    /**
     * @see de.groth.dts.api.core.dto.IDynamicTemplateSystem#getInsertionPatterns()
     */
    public IInsertionPattern[] getInsertionPatterns() {
        return this.insertionPatterns.clone();
    }

    /**
     * @see de.groth.dts.api.core.dto.IDynamicTemplateSystem#getPreProcessings()
     */
    public IProcessing[] getPreProcessings() {
        return this.preProcessings.clone();
    }

    /**
     * @see de.groth.dts.api.core.dto.IDynamicTemplateSystem#getPostProcessings()
     */
    public IProcessing[] getPostProcessings() {
        return this.postProcessings.clone();

    }

    /**
     * @see de.groth.dts.api.core.dto.IDynamicTemplateSystem#getLastConvertCallDate(java.lang.String)
     */
    public Date getLastConvertCallDate(final String pageId) {
        return this.getConverter().getLastConvertCallDate(pageId);
    }

    /**
     * @see de.groth.dts.api.core.dto.IDynamicTemplateSystem#getLastExportCallDate(java.lang.String)
     */
    public Date getLastExportCallDate(final String pageId) {
        return this.getConverter().getLastExportCallDate(pageId);
    }

    /**
     * @see de.groth.dts.api.core.dto.IDynamicTemplateSystem#getLastExportAllCallDate()
     */
    public Date getLastExportAllCallDate() {
        return this.getConverter().getLastExportAllCallDate();
    }

    /**
     * @see de.groth.dts.api.xml.IDtsDtoXmlHandlerProvider#createXmlHandler(de.groth.dts.api.core.dao.DynamicTemplateSystemExecutionContext)
     */
    public IDtsDtoXmlHandler<IDynamicTemplateSystem> createXmlHandler(
            final DynamicTemplateSystemExecutionContext context) {
        return new DynamicTemplateSystemXmlHandler(context.getDts(), context
                .getBaseDtsPath(), context.getBaseExportPath());
    }

    /**
     * @see de.groth.dts.impl.core.dto.DynamicTemplateSystemBase#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object other) {
        return super.equals(other);
    }

    /**
     * @see de.groth.dts.impl.core.dto.DynamicTemplateSystemBase#hashCode()
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * @see de.groth.dts.impl.core.dto.DynamicTemplateSystemBase#toString()
     */
    @Override
    public String toString() {
        return "DynamicTemplateSystem: id=" + this.getFilePath();
    }
}
