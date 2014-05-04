package de.groth.dts.impl.xml;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.dom4j.Node;

import de.groth.dts.api.core.dao.PluginInstantiationContext;
import de.groth.dts.api.core.dto.IDynamicTemplateSystemBase;
import de.groth.dts.api.core.dto.processings.IProcessing;
import de.groth.dts.api.core.exception.dto.DtoInitializationException;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;
import de.groth.dts.api.core.util.plugins.ProcessingPluginFactory;
import de.groth.dts.api.xml.IDtsDtoXmlHandler;
import de.groth.dts.api.xml.IProcessigXmlHandler;
import de.groth.dts.api.xml.exception.XmlException;
import de.groth.dts.api.xml.structure.ProcessingXmlInformation;
import de.groth.dts.api.xml.util.XmlHelper;

/**
 * Default implementation of {@link IProcessigXmlHandler}.
 * 
 * @author Christian Groth
 * 
 */
public class ProcessingBaseXmlHandler implements IDtsDtoXmlHandler<IProcessing> {
    private static final Logger LOGGER = Logger
            .getLogger(ProcessingBaseXmlHandler.class);

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
    public ProcessingBaseXmlHandler(final IDynamicTemplateSystemBase dts,
            final String dtsPath, final String exportPath) {
        this.dts = dts;
        this.dtsPath = dtsPath;
        this.exportPath = exportPath;
    }

    /**
     * @see de.groth.dts.api.xml.IDtsDtoXmlHandler#fromXml(org.dom4j.Node)
     */
    public IProcessing fromXml(final Node node)
            throws DtoInitializationException {
        if (!node.getName().equals(
                ProcessingXmlInformation.NODE_NAME.getValue())) {
            throw new DtoInitializationException(
                    "Unable to create IProcessing due to incorrect node. Node is "
                            + node.getName() + " but expected was "
                            + ProcessingXmlInformation.NODE_NAME.getValue()
                            + "!!");
        }

        /*
         * Attributes
         */
        ProcessingBaseXmlHandler.LOGGER.debug(node.getPath());
        ProcessingBaseXmlHandler.LOGGER.debug("Reading parameters");
        final String type = XmlHelper.nodeAttributeValue(node,
                ProcessingXmlInformation.ATT_TYPE.getValue());
        ProcessingBaseXmlHandler.LOGGER.debug(ProcessingXmlInformation.ATT_TYPE
                .getValue()
                + "=" + type);

        if (this.dts.getPluginProcessing(type) == null) {
            throw new DtoInitializationException(
                    "unable to resolve parameter with type " + type + "!!");
        }

        ProcessingBaseXmlHandler.LOGGER.debug("plugin in dts is "
                + this.dts.getPluginProcessing(type));

        final PluginInstantiationContext config = new PluginInstantiationContext(
                this.dtsPath, this.exportPath, this.dts, this.dts
                        .getConverter(), this.dts.getFilePath(), node, type,
                this.dts.getPluginProcessing(type).getPluginClassName(),
                new HashMap<String, String>());
        try {
            return new ProcessingPluginFactory().instantiate(config);
        } catch (final PluginInitializationException ex) {
            throw new DtoInitializationException("Unable to invoke factory!!",
                    ex);
        }
    }

    /**
     * @see de.groth.dts.api.xml.IDtsDtoXmlHandler#toXml(de.groth.dts.api.core.dto.IDtsDto,
     *      org.dom4j.Element)
     */
    public void toXml(final IProcessing instance, final Element parent)
            throws XmlException {
        final Element processingNode = parent
                .addElement(ProcessingXmlInformation.NODE_NAME.getValue());

        /*
         * Attributes
         */
        processingNode.addAttribute(ProcessingXmlInformation.ATT_TYPE
                .getValue(), instance.getPluginKey());

        try {
            new ProcessingPluginFactory().toXml(instance, processingNode);
        } catch (final PluginInitializationException ex) {
            throw new XmlException("Unable to invoke factory!!", ex);
        }
    }
}
