package de.groth.dts.impl.xml;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.dom4j.Node;

import de.groth.dts.api.core.dto.IState;
import de.groth.dts.api.core.exception.dto.DtoInitializationException;
import de.groth.dts.api.xml.IStateXmlHandler;
import de.groth.dts.api.xml.exception.XmlException;
import de.groth.dts.api.xml.structure.StateXmlInformation;
import de.groth.dts.api.xml.util.XmlHelper;
import de.groth.dts.impl.core.dto.State;

/**
 * Default implementation of {@link IStateXmlHandler}.
 * 
 * @author Christian Groth
 */
public class StateXmlHandler implements IStateXmlHandler {

    private static final Logger LOGGER = Logger
            .getLogger(StateXmlHandler.class);

    /**
     * @see de.groth.dts.api.xml.IDtsDtoXmlHandler#fromXml(org.dom4j.Node)
     */
    public IState fromXml(final Node node) throws DtoInitializationException {
        if (!node.getName().equals(StateXmlInformation.NODE_NAME.getValue())) {
            throw new DtoInitializationException(
                    "Unable to create Page due to incorrect node. Node is "
                            + node.getName() + " but expected was "
                            + StateXmlInformation.NODE_NAME.getValue() + "!!");
        }

        /*
         * Attributes
         */
        StateXmlHandler.LOGGER.debug(node.getPath());
        StateXmlHandler.LOGGER.debug("Reading Parameters");
        final String id = XmlHelper.nodeAttributeValue(node,
                StateXmlInformation.ATT_ID.getValue());
        final boolean cropCData = XmlHelper.stringToBoolean(XmlHelper
                .nodeAttributeValue(node, StateXmlInformation.ATT_CROP_CDATA
                        .getValue()));

        final String pre;
        final String post;
        final String defaultValue;
        final String rawPre;
        final String rawPost;
        final String rawDefaultValue;
        try {
            pre = XmlHelper.nodeContentAsStringAndCropCData(node,
                    StateXmlInformation.PATH_PRE.getValue());
            post = XmlHelper.nodeContentAsStringAndCropCData(node,
                    StateXmlInformation.PATH_POST.getValue());
            defaultValue = XmlHelper.nodeContentAsStringAndCropCData(node,
                    StateXmlInformation.PATH_DEFAULT.getValue());

            rawPre = XmlHelper.nodeContentAsString(node,
                    StateXmlInformation.PATH_PRE.getValue());
            rawPost = XmlHelper.nodeContentAsString(node,
                    StateXmlInformation.PATH_POST.getValue());
            rawDefaultValue = XmlHelper.nodeContentAsString(node,
                    StateXmlInformation.PATH_DEFAULT.getValue());
        } catch (final XmlException ex) {
            throw new DtoInitializationException("caught XmlException!!", ex);
        }

        StateXmlHandler.LOGGER.debug(StateXmlInformation.ATT_ID.getValue()
                + "=" + id + ", "
                + StateXmlInformation.ATT_CROP_CDATA.getValue() + "="
                + cropCData + ", " + StateXmlInformation.PATH_PRE.getValue()
                + "=" + rawPre + ", "
                + StateXmlInformation.PATH_POST.getValue() + "=" + rawPost
                + ", " + StateXmlInformation.PATH_DEFAULT.getValue() + "="
                + defaultValue);

        /*
         * raw conditions
         */
        StateXmlHandler.LOGGER.debug("reading raw conditions");
        final ArrayList<String> keys = XmlHelper.nodesCustomValueAsString(node,
                StateXmlInformation.PATH_CONDITION.getValue(), "@"
                        + StateXmlInformation.CONDITION_ATT_VALUE.getValue());

        ArrayList<String> rawValues;
        try {
            rawValues = XmlHelper.nodesContentAsString(node,
                    StateXmlInformation.PATH_CONDITION.getValue());
        } catch (final XmlException ex) {
            throw new DtoInitializationException("caught XmlException!!", ex);
        }

        final HashMap<String, String> rawConditionsSet = new HashMap<String, String>();
        if (keys != null && rawValues != null
                && keys.size() == rawValues.size()) {
            for (int i = 0; i < keys.size(); i++) {
                rawConditionsSet.put(keys.get(i), rawValues.get(i));
            }
        } else {
            throw new DtoInitializationException(
                    "error while creating conditions map. Check your XML please ("
                            + StateXmlInformation.ATT_ID.getValue() + "=" + id
                            + ")!!");
        }

        /*
         * conditions
         */
        ArrayList<String> values;
        try {
            values = XmlHelper.nodesContentAsString(node,
                    StateXmlInformation.PATH_CONDITION.getValue());
        } catch (final XmlException ex) {
            throw new DtoInitializationException("caught XmlException!!", ex);
        }

        for (int i = 0; i < values.size(); i++) {
            final String current = values.get(i);
            final String cropped = XmlHelper.cropCData(current);
            values.set(i, cropped);
        }

        final HashMap<String, String> conditionsSet = new HashMap<String, String>();
        if (values != null && keys.size() == values.size()) {
            for (int i = 0; i < keys.size(); i++) {
                conditionsSet.put(keys.get(i), values.get(i));
            }
        } else {
            throw new DtoInitializationException(
                    "error while creating conditions map. Check your XML please ("
                            + StateXmlInformation.ATT_ID.getValue() + "=" + id
                            + ")!!");
        }

        // Create StateDto
        StateXmlHandler.LOGGER.debug("Creating State-DTO");
        return new State(id, pre, post, conditionsSet, defaultValue, rawPre,
                rawPost, rawConditionsSet, rawDefaultValue, cropCData);
    }

    /**
     * @see de.groth.dts.api.xml.IDtsDtoXmlHandler#toXml(de.groth.dts.api.core.dto.IDtsDto,
     *      org.dom4j.Element)
     */
    public void toXml(final IState instance, final Element parent)
            throws XmlException {
        final Element stateNode = parent
                .addElement(StateXmlInformation.NODE_NAME.getValue());

        /*
         * Attributes
         */
        stateNode.addAttribute(StateXmlInformation.ATT_ID.getValue(), instance
                .getId());
        stateNode.addAttribute(StateXmlInformation.ATT_CROP_CDATA.getValue(),
                XmlHelper.booleanToString(instance.isCropCData()));

        stateNode.add(XmlHelper.createNodeFromString(
                StateXmlInformation.PATH_PRE.getValue(), instance.getRawPre()));
        stateNode.add(XmlHelper
                .createNodeFromString(StateXmlInformation.PATH_POST.getValue(),
                        instance.getRawPost()));
        if (instance.getRawDefaultValue() != null) {
            stateNode.add(XmlHelper.createNodeFromString(
                    StateXmlInformation.PATH_DEFAULT.getValue(), instance
                            .getRawDefaultValue()));
        }

        /*
         * Conditions
         */
        for (final String key : instance.getRawConditionValues().keySet()) {
            final String rawConditionValue = instance.getRawConditionValues()
                    .get(key);
            StateXmlHandler.LOGGER.debug("rawConditionValue="
                    + rawConditionValue);

            final Element conditionNode = XmlHelper.createNodeFromString(
                    StateXmlInformation.PATH_CONDITION.getValue(),
                    rawConditionValue);
            conditionNode.addAttribute(StateXmlInformation.CONDITION_ATT_VALUE
                    .getValue(), key);
            stateNode.add(conditionNode);
        }
    }
}
