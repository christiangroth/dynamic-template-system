package de.groth.dts.impl.xml;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.dom4j.Node;

import de.groth.dts.api.core.dto.IDynamicTemplateSystemBase;
import de.groth.dts.api.core.dto.IPage;
import de.groth.dts.api.core.dto.ITheme;
import de.groth.dts.api.core.dto.parameters.IParameter;
import de.groth.dts.api.core.exception.dto.DtoInitializationException;
import de.groth.dts.api.xml.IPageXmlHandler;
import de.groth.dts.api.xml.exception.XmlException;
import de.groth.dts.api.xml.structure.PageXmlInformation;
import de.groth.dts.api.xml.structure.ParameterXmlInformation;
import de.groth.dts.api.xml.util.XmlHelper;
import de.groth.dts.impl.core.dto.Page;

/**
 * Default implementation of {@link IPageXmlHandler}.
 * 
 * @author Christian Groth
 */
public class PageXmlHandler implements IPageXmlHandler {
    private static final Logger LOGGER = Logger.getLogger(PageXmlHandler.class);

    private final IDynamicTemplateSystemBase dts;
    private final String dtsPath;
    private final String exportPath;
    private final ITheme[] themes;

    /**
     * Creates a new instance
     * 
     * @param dts
     *                current {@link IDynamicTemplateSystemBase}
     * @param dtsPath
     *                base dts path
     * @param exportPath
     *                base export path
     * @param themes
     *                all {@link ITheme}s
     */
    public PageXmlHandler(final IDynamicTemplateSystemBase dts,
            final String dtsPath, final String exportPath, final ITheme[] themes) {
        this.dts = dts;
        this.dtsPath = dtsPath;
        this.exportPath = exportPath;
        this.themes = themes.clone();
    }

    /**
     * @see de.groth.dts.api.xml.IDtsDtoXmlHandler#fromXml(org.dom4j.Node)
     */
    public IPage fromXml(final Node node) throws DtoInitializationException {
        if (!node.getName().equals(PageXmlInformation.NODE_NAME.getValue())) {
            throw new DtoInitializationException(
                    "Unable to create Page due to incorrect node. Node is "
                            + node.getName() + " but expected was "
                            + PageXmlInformation.NODE_NAME + "!!");
        }

        /*
         * Attributes
         */
        PageXmlHandler.LOGGER.debug(node.getPath());
        PageXmlHandler.LOGGER.debug("Reading Parameters");
        final String id = XmlHelper.nodeAttributeValue(node,
                PageXmlInformation.ATT_ID.getValue());
        final String themeId = XmlHelper.nodeAttributeValue(node,
                PageXmlInformation.ATT_THEME.getValue());
        final String outputFile = XmlHelper.nodeAttributeValue(node,
                PageXmlInformation.ATT_FILE.getValue());
        PageXmlHandler.LOGGER.debug(PageXmlInformation.ATT_ID.getValue() + "="
                + id + ", " + PageXmlInformation.ATT_THEME.getValue() + "="
                + themeId + ", " + PageXmlInformation.ATT_FILE.getValue() + "="
                + outputFile);
        /*
         * Theme Parameters
         */
        final List<IParameter> allParameters = new ArrayList<IParameter>();
        final List<String> allThemeParameterNames = new ArrayList<String>();

        String searchedThemeId = themeId;
        while (searchedThemeId != null && !searchedThemeId.trim().equals("")) {
            final ITheme theme = new ThemeXmlHandler(this.dts, this.dtsPath,
                    this.exportPath).fromXml(this.getThemeNode(node,
                    searchedThemeId));
            for (final IParameter themeParameter : theme.getParameter()) {
                allParameters.add(themeParameter);
                allThemeParameterNames.add(themeParameter.getParameterName());
            }

            searchedThemeId = theme.getSuperThemeId();
        }

        /*
         * Parameters
         */
        PageXmlHandler.LOGGER.debug("Reading Attributes");
        List<IParameter> pageParameterList;
        try {
            pageParameterList = new XmlHelper<IParameter>().nodesAsDtos(node,
                    new ParameterBaseXmlHandler(this.dts, this.dtsPath,
                            this.exportPath), ParameterXmlInformation.NODE_NAME
                            .getValue());
        } catch (final XmlException ex) {
            throw new DtoInitializationException("caught XmlException!!", ex);
        }

        /*
         * Overwrite Theme Parameter
         */
        final HashSet<String> overridingParams = new HashSet<String>();
        for (final IParameter param : pageParameterList) {
            final String key = param.getParameterName();

            if (!allThemeParameterNames.contains(key)) {
                throw new DtoInitializationException(
                        "page ("
                                + id
                                + ") tried to overried parameter which is not defined in theme ("
                                + themeId + "): " + key + "!!");
            } // if

            overridingParams.add(key);
        } // for

        final IParameter[] ownParameters = pageParameterList
                .toArray(new IParameter[pageParameterList.size()]);
        PageXmlHandler.LOGGER.debug("Created Parameter[]");

        for (final IParameter pageParameter : ownParameters) {
            allParameters.add(pageParameter);
        }
        final IParameter[] allParameter = allParameters
                .toArray(new IParameter[allParameters.size()]);

        /*
         * Create Page
         */
        PageXmlHandler.LOGGER.debug("Creating Page-DTO");
        return new Page(id, this.findThemeById(themeId), outputFile,
                ownParameters, allParameter);
    }

    private Node getThemeNode(final Node node, final String themeId) {
        return node.selectSingleNode(PageXmlInformation.QUERY_THEME_PRE
                + themeId + PageXmlInformation.QUERY_THEME_POST);
    }

    private ITheme findThemeById(final String themeId)
            throws DtoInitializationException {
        for (final ITheme theme : this.themes) {
            if (theme.getId().equals(themeId)) {
                return theme;
            }
        }

        throw new DtoInitializationException("Unable to find theme with id "
                + themeId + "!!");
    }

    /**
     * @see de.groth.dts.api.xml.IDtsDtoXmlHandler#toXml(de.groth.dts.api.core.dto.IDtsDto,
     *      org.dom4j.Element)
     */
    public void toXml(final IPage instance, final Element parent)
            throws XmlException {
        final Element pageNode = parent.addElement(PageXmlInformation.NODE_NAME
                .getValue());

        /*
         * Attributes
         */
        pageNode.addAttribute(PageXmlInformation.ATT_ID.getValue(), instance
                .getId());
        pageNode.addAttribute(PageXmlInformation.ATT_THEME.getValue(), instance
                .getTheme().getId());
        pageNode.addAttribute(PageXmlInformation.ATT_FILE.getValue(), instance
                .getOutputFile());

        /*
         * Parameter
         */
        for (final IParameter parameter : instance.getOwnParameter()) {
            new ParameterBaseXmlHandler(this.dts, this.dtsPath, this.exportPath)
                    .toXml(parameter, pageNode);
        }
    }
}
