package de.groth.dts.impl.xml;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.dom4j.Node;

import de.groth.dts.api.core.dto.IInsertionPattern;
import de.groth.dts.api.core.exception.dto.DtoInitializationException;
import de.groth.dts.api.xml.IInsertionPatternXmlHandler;
import de.groth.dts.api.xml.exception.XmlException;
import de.groth.dts.api.xml.structure.InsertionPatternXmlInformation;
import de.groth.dts.api.xml.util.XmlHelper;
import de.groth.dts.impl.core.dto.InsertionPattern;

/**
 * Default implementation of {@link IInsertionPatternXmlHandler}.
 * 
 * @author Christian Groth
 * 
 */
public class InsertionPatternXmlHandler implements IInsertionPatternXmlHandler {
    private static final Logger LOGGER = Logger
            .getLogger(InsertionPatternXmlHandler.class);

    /**
     * @see de.groth.dts.api.xml.IDtsDtoXmlHandler#fromXml(org.dom4j.Node)
     */
    public IInsertionPattern fromXml(final Node node)
            throws DtoInitializationException {

        if (!node.getName().equals(
                InsertionPatternXmlInformation.NODE_NAME.getValue())) {
            throw new DtoInitializationException(
                    "Unable to create InsertionPattern due to incorrect node. Node is "
                            + node.getName() + " but expected was "
                            + InsertionPatternXmlInformation.NODE_NAME + "!!");
        }

        /*
         * Attributes
         */
        try {
            InsertionPatternXmlHandler.LOGGER.debug(node.getPath());
            InsertionPatternXmlHandler.LOGGER.debug("Reading Attributes");
            final String id = XmlHelper.nodeAttributeValue(node,
                    InsertionPatternXmlInformation.ATT_ID.getValue());
            final boolean cropCData = XmlHelper.stringToBoolean(XmlHelper
                    .nodeAttributeValue(node,
                            InsertionPatternXmlInformation.ATT_CROP_CDATA
                                    .getValue()));

            final String defaultActiveInsertionRaw = XmlHelper
                    .nodeContentAsString(node,
                            InsertionPatternXmlInformation.PATH_DEFAULT_ACTIVE
                                    .getValue());
            final String defaultActiveInsertion = XmlHelper
                    .nodeContentAsStringAndCropCData(node,
                            InsertionPatternXmlInformation.PATH_DEFAULT_ACTIVE
                                    .getValue());

            final String defaultInactiveInsertionRaw = XmlHelper
                    .nodeContentAsString(
                            node,
                            InsertionPatternXmlInformation.PATH_DEFAULT_INACTIVE
                                    .getValue());
            final String defaultInactiveInsertion = XmlHelper
                    .nodeContentAsStringAndCropCData(
                            node,
                            InsertionPatternXmlInformation.PATH_DEFAULT_INACTIVE
                                    .getValue());

            final String defaultUnreplacedInsertionRaw = XmlHelper
                    .nodeContentAsString(
                            node,
                            InsertionPatternXmlInformation.PATH_DEFAULT_UNREPLACED
                                    .getValue());
            final String defaultUnreplacedInsertion = XmlHelper
                    .nodeContentAsStringAndCropCData(
                            node,
                            InsertionPatternXmlInformation.PATH_DEFAULT_UNREPLACED
                                    .getValue());

            final String dataRaw = XmlHelper.nodeContentAsString(node,
                    InsertionPatternXmlInformation.PATH_DATA.getValue());
            final String data = XmlHelper.nodeContentAsStringAndCropCData(node,
                    InsertionPatternXmlInformation.PATH_DATA.getValue());

            InsertionPatternXmlHandler.LOGGER
                    .debug(InsertionPatternXmlInformation.ATT_ID.getValue()
                            + "="
                            + id
                            + ", "
                            + InsertionPatternXmlInformation.PATH_DEFAULT_ACTIVE
                                    .getValue()
                            + "="
                            + defaultActiveInsertionRaw
                            + ", "
                            + InsertionPatternXmlInformation.PATH_DEFAULT_INACTIVE
                                    .getValue()
                            + "="
                            + defaultInactiveInsertionRaw
                            + ", "
                            + InsertionPatternXmlInformation.PATH_DATA
                                    .getValue() + "=" + dataRaw);

            return new InsertionPattern(id, defaultActiveInsertion,
                    defaultInactiveInsertion, defaultUnreplacedInsertion, data,
                    defaultActiveInsertionRaw, defaultInactiveInsertionRaw,
                    defaultUnreplacedInsertionRaw, dataRaw, cropCData);
        } catch (final XmlException ex) {
            throw new DtoInitializationException("caught XmlException!!", ex);
        }
    }

    /**
     * @see de.groth.dts.api.xml.IDtsDtoXmlHandler#toXml(de.groth.dts.api.core.dto.IDtsDto,
     *      org.dom4j.Element)
     */
    public void toXml(final IInsertionPattern instance, final Element parent)
            throws XmlException {
        final Element insertionPatternNode = parent
                .addElement(InsertionPatternXmlInformation.NODE_NAME.getValue());

        /*
         * Attributes
         */
        insertionPatternNode.addAttribute(InsertionPatternXmlInformation.ATT_ID
                .getValue(), instance.getId());

        insertionPatternNode.addAttribute(
                InsertionPatternXmlInformation.ATT_CROP_CDATA.getValue(),
                XmlHelper.booleanToString(instance.isCropCData()));

        insertionPatternNode.add(XmlHelper.createNodeFromString(
                InsertionPatternXmlInformation.PATH_DEFAULT_ACTIVE.getValue(),
                instance.getDefaultActiveValueRaw()));

        insertionPatternNode.add(XmlHelper
                .createNodeFromString(
                        InsertionPatternXmlInformation.PATH_DEFAULT_INACTIVE
                                .getValue(), instance
                                .getDefaultInactiveValueRaw()));

        insertionPatternNode.add(XmlHelper.createNodeFromString(
                InsertionPatternXmlInformation.PATH_DEFAULT_UNREPLACED
                        .getValue(), instance.getDefaultUnreplacedValueRaw()));

        insertionPatternNode.add(XmlHelper.createNodeFromString(
                InsertionPatternXmlInformation.PATH_DATA.getValue(), instance
                        .getDataRaw()));
    }
}
