package de.groth.dts.impl.xml;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.dom4j.Node;

import de.groth.dts.api.core.dto.IDynamicTemplateSystemBase;
import de.groth.dts.api.core.dto.ITheme;
import de.groth.dts.api.core.dto.parameters.IParameter;
import de.groth.dts.api.core.exception.dto.DtoInitializationException;
import de.groth.dts.api.xml.IThemeXmlHandler;
import de.groth.dts.api.xml.exception.XmlException;
import de.groth.dts.api.xml.structure.ParameterXmlInformation;
import de.groth.dts.api.xml.structure.ThemeXmlInformation;
import de.groth.dts.api.xml.util.XmlHelper;
import de.groth.dts.impl.core.dto.Theme;

/**
 * Default implementation of {@link IThemeXmlHandler}.
 * 
 * @author Christian Groth
 */
public class ThemeXmlHandler implements IThemeXmlHandler {
    private static final Logger LOGGER = Logger
            .getLogger(ThemeXmlHandler.class);

    private final IDynamicTemplateSystemBase dts;
    private final String dtsPath;
    private final String exportPath;

    /**
     * Creates a new instance
     * 
     * @param dts
     *                current {@link IDynamicTemplateSystemBase}
     * @param dtsPath
     *                base dts path
     * @param exportPath
     *                base export path
     */
    public ThemeXmlHandler(final IDynamicTemplateSystemBase dts,
            final String dtsPath, final String exportPath) {
        this.dts = dts;
        this.dtsPath = dtsPath;
        this.exportPath = exportPath;
    }

    /**
     * @see de.groth.dts.api.xml.IDtsDtoXmlHandler#fromXml(org.dom4j.Node)
     */
    public ITheme fromXml(final Node node) throws DtoInitializationException {
        if (!node.getName().equals(ThemeXmlInformation.NODE_NAME.getValue())) {
            throw new DtoInitializationException(
                    "Unable to create Theme due to incorrect node. Node is "
                            + node.getName() + " but expected was "
                            + ThemeXmlInformation.NODE_NAME.getValue() + "!!");
        }

        /*
         * Attributes
         */
        ThemeXmlHandler.LOGGER.debug(node.getPath());
        ThemeXmlHandler.LOGGER.debug("Reading Parameters");
        final String id = XmlHelper.nodeAttributeValue(node,
                ThemeXmlInformation.ATT_ID.getValue());
        final String templateFile = XmlHelper.nodeAttributeValue(node,
                ThemeXmlInformation.ATT_TEMPLATE.getValue());
        final String superThemeId = XmlHelper.nodeAttributeValue(node,
                ThemeXmlInformation.ATT_SUPER_THEME_ID.getValue());
        ThemeXmlHandler.LOGGER.debug(ThemeXmlInformation.ATT_ID.getValue()
                + "=" + id + ", " + ThemeXmlInformation.ATT_TEMPLATE.getValue()
                + "=" + templateFile + ", "
                + ThemeXmlInformation.ATT_SUPER_THEME_ID + "=" + superThemeId);

        /*
         * Parameter
         */
        ThemeXmlHandler.LOGGER.debug("Reading Parameters");
        ArrayList<IParameter> parameterList;
        try {
            parameterList = new XmlHelper<IParameter>().nodesAsDtos(node,
                    new ParameterBaseXmlHandler(this.dts, this.dtsPath,
                            this.exportPath), ParameterXmlInformation.NODE_NAME
                            .getValue());
        } catch (final XmlException ex) {
            throw new DtoInitializationException("caught XmlException!!", ex);
        }

        final IParameter[] parameters = parameterList
                .toArray(new IParameter[parameterList.size()]);
        ThemeXmlHandler.LOGGER.debug("Created Parameter[]");

        // Create ThemeDto
        ThemeXmlHandler.LOGGER.debug("Creating Theme-DTO");
        return new Theme(id, templateFile, this.dtsPath, parameters,
                superThemeId);
    }

    /**
     * @see de.groth.dts.api.xml.IDtsDtoXmlHandler#toXml(de.groth.dts.api.core.dto.IDtsDto,
     *      org.dom4j.Element)
     */
    public void toXml(final ITheme instance, final Element parent)
            throws XmlException {
        final Element themeNode = parent
                .addElement(ThemeXmlInformation.NODE_NAME.getValue());
        themeNode.addAttribute(ThemeXmlInformation.ATT_ID.getValue(), instance
                .getId());

        if (instance.getTemplatePath() != null
                && !instance.getTemplatePath().trim().equals("")) {
            themeNode.addAttribute(ThemeXmlInformation.ATT_TEMPLATE.getValue(),
                    instance.getTemplatePath());
        }

        if (instance.getSuperThemeId() != null
                && !instance.getSuperThemeId().trim().equals("")) {
            themeNode.addAttribute(ThemeXmlInformation.ATT_SUPER_THEME_ID
                    .getValue(), instance.getSuperThemeId());
        }

        for (final IParameter parameter : instance.getParameter()) {
            new ParameterBaseXmlHandler(this.dts, this.dtsPath, this.exportPath)
                    .toXml(parameter, themeNode);
        }
    }
}
