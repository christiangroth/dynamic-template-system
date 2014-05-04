package de.groth.dts.impl.xml;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.dom4j.Node;

import de.groth.dts.api.core.dto.IDynamicTemplateSystem;
import de.groth.dts.api.core.dto.IDynamicTemplateSystemBase;
import de.groth.dts.api.core.dto.IInsertionPattern;
import de.groth.dts.api.core.dto.IPage;
import de.groth.dts.api.core.dto.IState;
import de.groth.dts.api.core.dto.ITheme;
import de.groth.dts.api.core.dto.processings.IProcessing;
import de.groth.dts.api.core.exception.dto.DtoInitializationException;
import de.groth.dts.api.xml.IDynamicTemplateSystemXmlHandler;
import de.groth.dts.api.xml.exception.XmlException;
import de.groth.dts.api.xml.structure.DynamicTemplateSystemXmlInformation;
import de.groth.dts.api.xml.structure.InsertionPatternXmlInformation;
import de.groth.dts.api.xml.structure.PageXmlInformation;
import de.groth.dts.api.xml.structure.ProcessingXmlInformation;
import de.groth.dts.api.xml.structure.StateXmlInformation;
import de.groth.dts.api.xml.structure.ThemeXmlInformation;
import de.groth.dts.api.xml.util.XmlHelper;
import de.groth.dts.impl.core.dto.DynamicTemplateSystem;

/**
 * Default implementation of {@link IDynamicTemplateSystemXmlHandler}.
 * 
 * @author Christian Groth
 */
public class DynamicTemplateSystemXmlHandler implements
        IDynamicTemplateSystemXmlHandler {
    private static final Logger LOGGER = Logger
            .getLogger(DynamicTemplateSystemXmlHandler.class);

    private final IDynamicTemplateSystemBase base;
    private final String dtsPath;
    private final String exportPath;

    /**
     * Creates a new instance
     * 
     * @param base
     *                current {@link IDynamicTemplateSystemBase}
     * @param dtsPath
     *                base dts path
     * @param exportPath
     *                base export path
     */
    public DynamicTemplateSystemXmlHandler(
            final IDynamicTemplateSystemBase base, final String dtsPath,
            final String exportPath) {
        this.base = base;
        this.dtsPath = dtsPath;
        this.exportPath = exportPath;
    }

    /**
     * @see de.groth.dts.api.xml.IDtsDtoXmlHandler#fromXml(org.dom4j.Node)
     */
    public DynamicTemplateSystem fromXml(final Node node)
            throws DtoInitializationException {
        if (!node.getName().equals(
                DynamicTemplateSystemXmlInformation.NODE_NAME.getValue())) {
            throw new DtoInitializationException(
                    "Unable to create DynamicTemplateSystem due to incorrect node. Node is "
                            + node.getName()
                            + " but expected was "
                            + DynamicTemplateSystemXmlInformation.NODE_NAME
                                    .getValue() + "!!");
        }

        /*
         * Processings
         */
        DynamicTemplateSystemXmlHandler.LOGGER.debug("Reading Pre-Processings");
        ArrayList<IProcessing> preProcessingList;
        try {
            preProcessingList = new XmlHelper<IProcessing>()
                    .nodesAsDtos(
                            node,
                            new ProcessingBaseXmlHandler(this.base,
                                    this.dtsPath, this.exportPath),
                            DynamicTemplateSystemXmlInformation.PATH_PROCESSINGS
                                    .getValue()
                                    + "/"
                                    + DynamicTemplateSystemXmlInformation.PATH_PROCESSINGS_PRE
                                            .getValue()
                                    + "/"
                                    + ProcessingXmlInformation.NODE_NAME
                                            .getValue());
        } catch (final XmlException ex) {
            throw new DtoInitializationException("caught XmlException!!", ex);
        }

        final IProcessing[] preProcessings = preProcessingList
                .toArray(new IProcessing[preProcessingList.size()]);
        DynamicTemplateSystemXmlHandler.LOGGER.debug("Created IProcessing[]");

        DynamicTemplateSystemXmlHandler.LOGGER
                .debug("Reading Post-Processings");
        ArrayList<IProcessing> postProcessingList;
        try {
            postProcessingList = new XmlHelper<IProcessing>()
                    .nodesAsDtos(
                            node,
                            new ProcessingBaseXmlHandler(this.base,
                                    this.dtsPath, this.exportPath),
                            DynamicTemplateSystemXmlInformation.PATH_PROCESSINGS
                                    .getValue()
                                    + "/"
                                    + DynamicTemplateSystemXmlInformation.PATH_PROCESSINGS_POST
                                            .getValue()
                                    + "/"
                                    + ProcessingXmlInformation.NODE_NAME
                                            .getValue());
        } catch (final XmlException ex) {
            throw new DtoInitializationException("caught XmlException!!", ex);
        }

        final IProcessing[] postProcessings = postProcessingList
                .toArray(new IProcessing[postProcessingList.size()]);
        DynamicTemplateSystemXmlHandler.LOGGER.debug("Created IProcessing[]");

        /*
         * Themes
         */
        DynamicTemplateSystemXmlHandler.LOGGER.debug("Reading Themes");
        ArrayList<ITheme> themeList;
        try {
            themeList = new XmlHelper<ITheme>().nodesAsDtos(node,
                    new ThemeXmlHandler(this.base, this.dtsPath,
                            this.exportPath),
                    DynamicTemplateSystemXmlInformation.PATH_THEMES.getValue()
                            + "/" + ThemeXmlInformation.NODE_NAME.getValue());
        } catch (final XmlException ex) {
            throw new DtoInitializationException("caught XmlException!!", ex);
        }

        final ITheme[] themes = themeList.toArray(new ITheme[themeList.size()]);
        DynamicTemplateSystemXmlHandler.LOGGER.debug("Created Theme[]");

        /*
         * Pages
         */
        DynamicTemplateSystemXmlHandler.LOGGER.debug("Reading Pages");
        ArrayList<IPage> pageList;
        try {
            pageList = new XmlHelper<IPage>().nodesAsDtos(node,
                    new PageXmlHandler(this.base, this.dtsPath,
                            this.exportPath, themes),
                    DynamicTemplateSystemXmlInformation.PATH_PAGES.getValue()
                            + "/" + PageXmlInformation.NODE_NAME.getValue());
        } catch (final XmlException ex) {
            throw new DtoInitializationException("caught XmlException!!", ex);
        }

        final IPage[] pages = pageList.toArray(new IPage[pageList.size()]);
        DynamicTemplateSystemXmlHandler.LOGGER.debug("Created Page[]");

        /*
         * States
         */
        DynamicTemplateSystemXmlHandler.LOGGER.debug("Reading States");
        ArrayList<IState> stateList;
        try {
            stateList = new XmlHelper<IState>().nodesAsDtos(node,
                    new StateXmlHandler(),
                    DynamicTemplateSystemXmlInformation.PATH_STATES.getValue()
                            + "/" + StateXmlInformation.NODE_NAME.getValue());
        } catch (final XmlException ex) {
            throw new DtoInitializationException("caught XmlException!!", ex);
        }

        final IState[] states = stateList.toArray(new IState[stateList.size()]);
        DynamicTemplateSystemXmlHandler.LOGGER.debug("Created State[]");

        /*
         * InsertionPatterns
         */
        DynamicTemplateSystemXmlHandler.LOGGER
                .debug("Reading InsertionPatterns");
        ArrayList<IInsertionPattern> insertionPatternList;
        try {
            insertionPatternList = new XmlHelper<IInsertionPattern>()
                    .nodesAsDtos(
                            node,
                            new InsertionPatternXmlHandler(),
                            DynamicTemplateSystemXmlInformation.PATH_INSERTION_PATTERNS
                                    .getValue()
                                    + "/"
                                    + InsertionPatternXmlInformation.NODE_NAME
                                            .getValue());
        } catch (final XmlException ex) {
            throw new DtoInitializationException("caught XmlException!!", ex);
        }

        final IInsertionPattern[] insertionPatterns = insertionPatternList
                .toArray(new IInsertionPattern[insertionPatternList.size()]);
        DynamicTemplateSystemXmlHandler.LOGGER
                .debug("created IIndertionPattern[]");

        /*
         * Create DynamicTemplateSystem
         */
        DynamicTemplateSystemXmlHandler.LOGGER.debug("Creating DTS-DTO");
        return new DynamicTemplateSystem(this.base, themes, pages, states,
                insertionPatterns, preProcessings, postProcessings);
    }

    /**
     * @see de.groth.dts.api.xml.IDtsDtoXmlHandler#toXml(de.groth.dts.api.core.dto.IDtsDto,
     *      org.dom4j.Element)
     */
    public void toXml(final IDynamicTemplateSystem instance,
            final Element parent) throws XmlException {
        /*
         * in this case parent is not really a parent because an instance of
         * type DynamicTemplateSystem maps to the root element. So we get the
         * hopefully correctly named xml root element without any attributes.
         * 
         * so ... in standard case we would add a new node - which represents
         * the given instance - to the given parent element so we build up our
         * document step by step. this time we just take the given parent (which
         * is root and the representation of our instance) and start building
         * ...
         */
        final Element dynamicTemplateSystemNode = parent;
        new DynamicTemplateSystemBaseXmlHandler(instance.getFilePath(),
                instance.getConverter()).toXml(instance,
                dynamicTemplateSystemNode);

        /*
         * Processings
         */
        final Element processingsNode = dynamicTemplateSystemNode
                .addElement(DynamicTemplateSystemXmlInformation.PATH_PROCESSINGS
                        .getValue());
        final Element preProcessingsNode = processingsNode
                .addElement(DynamicTemplateSystemXmlInformation.PATH_PROCESSINGS_PRE
                        .getValue());
        for (final IProcessing preProcessing : instance.getPreProcessings()) {
            new ProcessingBaseXmlHandler(instance, this.dtsPath,
                    this.exportPath).toXml(preProcessing, preProcessingsNode);
        }

        final Element postProcessingsNode = processingsNode
                .addElement(DynamicTemplateSystemXmlInformation.PATH_PROCESSINGS_POST
                        .getValue());
        for (final IProcessing postProcessing : instance.getPostProcessings()) {
            new ProcessingBaseXmlHandler(instance, this.dtsPath,
                    this.exportPath).toXml(postProcessing, postProcessingsNode);
        }

        /*
         * Themes
         */
        final Element themesNode = dynamicTemplateSystemNode
                .addElement(DynamicTemplateSystemXmlInformation.PATH_THEMES
                        .getValue());
        for (final ITheme theme : instance.getThemes()) {
            new ThemeXmlHandler(instance, this.dtsPath, this.exportPath).toXml(
                    theme, themesNode);
        }

        /*
         * Pages
         */
        final Element pagesNode = dynamicTemplateSystemNode
                .addElement(DynamicTemplateSystemXmlInformation.PATH_PAGES
                        .getValue());
        for (final IPage page : instance.getPages()) {
            new PageXmlHandler(instance, this.dtsPath, this.exportPath,
                    instance.getThemes()).toXml(page, pagesNode);
        }

        /*
         * States
         */
        final Element statesNode = dynamicTemplateSystemNode
                .addElement(DynamicTemplateSystemXmlInformation.PATH_STATES
                        .getValue());
        for (final IState state : instance.getStates()) {
            new StateXmlHandler().toXml(state, statesNode);
        }

        /*
         * InsertionPatterns
         */
        final Element insertionPatternsNode = dynamicTemplateSystemNode
                .addElement(DynamicTemplateSystemXmlInformation.PATH_INSERTION_PATTERNS
                        .getValue());
        for (final IInsertionPattern insertionPattern : instance
                .getInsertionPatterns()) {
            new InsertionPatternXmlHandler().toXml(insertionPattern,
                    insertionPatternsNode);
        }
    }
}
