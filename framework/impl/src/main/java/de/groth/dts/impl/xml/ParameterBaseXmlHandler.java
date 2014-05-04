package de.groth.dts.impl.xml;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.dom4j.Node;

import de.groth.dts.api.core.dao.PluginInstantiationContext;
import de.groth.dts.api.core.dto.IDynamicTemplateSystemBase;
import de.groth.dts.api.core.dto.parameters.IParameter;
import de.groth.dts.api.core.exception.dto.DtoInitializationException;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;
import de.groth.dts.api.core.util.plugins.ParameterPluginFactory;
import de.groth.dts.api.xml.IDtsDtoXmlHandler;
import de.groth.dts.api.xml.IParameterXmlHandler;
import de.groth.dts.api.xml.exception.XmlException;
import de.groth.dts.api.xml.structure.ParameterXmlInformation;
import de.groth.dts.api.xml.util.XmlHelper;

/**
 * Default implementation of {@link IParameterXmlHandler}.
 * 
 * @author Christian Groth
 * 
 */
public class ParameterBaseXmlHandler implements IDtsDtoXmlHandler<IParameter> {
    private static final Logger LOGGER = Logger
            .getLogger(ParameterBaseXmlHandler.class);

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
    public ParameterBaseXmlHandler(final IDynamicTemplateSystemBase dts,
            final String dtsPath, final String exportPath) {
        this.dts = dts;
        this.dtsPath = dtsPath;
        this.exportPath = exportPath;
    }

    /**
     * @see de.groth.dts.api.xml.IDtsDtoXmlHandler#fromXml(org.dom4j.Node)
     */
    public IParameter fromXml(final Node node)
            throws DtoInitializationException {
        if (!node.getName()
                .equals(ParameterXmlInformation.NODE_NAME.getValue())) {
            throw new DtoInitializationException(
                    "Unable to create IParameter due to incorrect node. Node is "
                            + node.getName() + " but expected was "
                            + ParameterXmlInformation.NODE_NAME.getValue()
                            + "!!");
        }

        /*
         * Attributes
         */
        ParameterBaseXmlHandler.LOGGER.debug(node.getPath());
        ParameterBaseXmlHandler.LOGGER.debug("Reading parameters");
        final String name = XmlHelper.nodeAttributeValue(node,
                ParameterXmlInformation.ATT_NAME.getValue());
        final String type = XmlHelper.nodeAttributeValue(node,
                ParameterXmlInformation.ATT_TYPE.getValue());
        ParameterBaseXmlHandler.LOGGER.debug(ParameterXmlInformation.ATT_NAME
                .getValue()
                + "="
                + name
                + ", "
                + ParameterXmlInformation.ATT_TYPE.getValue() + "=" + type);

        if (this.dts.getPluginParameter(type) == null) {
            throw new DtoInitializationException(
                    "unable to resolve parameter with type " + type + "!!");
        }

        ParameterBaseXmlHandler.LOGGER.debug("plugin in dts is "
                + this.dts.getPluginParameter(type));

        final PluginInstantiationContext config = new PluginInstantiationContext(
                this.dtsPath, this.exportPath, this.dts, this.dts
                        .getConverter(), this.dts.getFilePath(), node, type,
                this.dts.getPluginParameter(type).getPluginClassName(),
                new HashMap<String, String>());
        config.addPluginAttribute(
                PluginInstantiationContext.PLUGIN_PARAMETER_NAME, name);

        try {
            return new ParameterPluginFactory().instantiate(config);
        } catch (final PluginInitializationException ex) {
            throw new DtoInitializationException("Unable to invoke factory!!",
                    ex);
        }
    }

    /**
     * @see de.groth.dts.api.xml.IDtsDtoXmlHandler#toXml(de.groth.dts.api.core.dto.IDtsDto,
     *      org.dom4j.Element)
     */
    public void toXml(final IParameter instance, final Element parent)
            throws XmlException {
        final Element parameterNode = parent
                .addElement(ParameterXmlInformation.NODE_NAME.getValue());

        /*
         * Attributes
         */
        parameterNode.addAttribute(ParameterXmlInformation.ATT_TYPE.getValue(),
                instance.getParameterType());
        parameterNode.addAttribute(ParameterXmlInformation.ATT_NAME.getValue(),
                instance.getParameterName());

        try {
            new ParameterPluginFactory().toXml(instance, parameterNode);
        } catch (final PluginInitializationException ex) {
            throw new XmlException("Unable to invoke factory!!", ex);
        }
    }
}
