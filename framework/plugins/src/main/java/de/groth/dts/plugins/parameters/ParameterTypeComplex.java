package de.groth.dts.plugins.parameters;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.dom4j.Node;

import de.groth.dts.api.core.dao.PluginInstantiationContext;
import de.groth.dts.api.core.dto.plugins.PluginConstructor;
import de.groth.dts.api.core.dto.plugins.PluginToXml;
import de.groth.dts.api.core.dto.plugins.parameter.AbstractParameter;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;
import de.groth.dts.api.core.exception.plugins.parameters.ParameterValueException;
import de.groth.dts.api.xml.exception.XmlException;
import de.groth.dts.api.xml.structure.ParameterXmlInformation;
import de.groth.dts.api.xml.util.XmlHelper;

/**
 * Prints out the xml node content as value and may crop possible cdata flags if
 * activated (default: false).
 * 
 * <pre>
 * &lt;param type=&quot;...&quot; name=&quot;...&quot; cropCData=&quot;&lt;1|0&gt;&quot; &gt;
 *      &lt;someValue&gt;
 * &lt;/param&gt;
 * </pre>
 * 
 * @author Christian Groth
 */
public class ParameterTypeComplex extends AbstractParameter {
    private static final Logger LOGGER = Logger
            .getLogger(ParameterTypeComplex.class);

    private static final String XML_ATT_CROP_CDATA = "cropCData";

    private final String croppedValue;
    private final String rawValue;
    private final boolean cropCData;

    /**
     * Creates a new instance (annotated as {@link PluginConstructor})
     * 
     * @param config
     *                current {@link PluginInstantiationContext}
     * @throws PluginInitializationException
     */
    @PluginConstructor
    public ParameterTypeComplex(final PluginInstantiationContext config)
            throws PluginInitializationException {
        super(config.getPluginKey(), config.getPluginClassName(), config);

        ParameterTypeComplex.LOGGER.debug(this.getParameterName()
                + ": creating");

        final Node node = config.getNode();
        this.cropCData = XmlHelper.stringToBoolean(XmlHelper
                .nodeAttributeValue(node,
                        ParameterTypeComplex.XML_ATT_CROP_CDATA));

        try {
            this.croppedValue = XmlHelper.nodeContentAsStringAndCropCData(node,
                    ".");
            this.rawValue = XmlHelper.nodeContentAsString(node, ".");
        } catch (final XmlException ex) {
            throw new PluginInitializationException(this.getParameterName()
                    + ": caught XmlException during xml-processing!!", ex);
        }

        try {
            final String value = this.getValue();
            if (value == null || value.trim().equals("")) {
                throw new PluginInitializationException(this.getParameterName()
                        + ": value must be set!!");
            }
        } catch (final ParameterValueException ex) {
            throw new PluginInitializationException(this.getParameterName()
                    + ": unable to check value!!", ex);
        }
    }

    /**
     * Transforms the current instance to its xml representation.
     * 
     * @param parameterElement
     * @throws XmlException
     * 
     * @see PluginToXml
     * 
     */
    @SuppressWarnings("unchecked")
    @PluginToXml
    public void toXml(final Element parameterElement) throws XmlException {
        ParameterTypeComplex.LOGGER.debug(this.getParameterName()
                + ": writing to xml");

        final Element newParameterElement = XmlHelper.changeNode(
                parameterElement, XmlHelper.createNodeFromString(
                        ParameterXmlInformation.NODE_NAME.getValue(),
                        this.rawValue));

        newParameterElement.addAttribute(
                ParameterTypeComplex.XML_ATT_CROP_CDATA, XmlHelper
                        .booleanToString(this.cropCData));
    }

    /**
     * @see de.groth.dts.api.core.dto.parameters.IParameter#getValue()
     */
    public String getValue() throws ParameterValueException {
        return this.cropCData ? this.croppedValue : this.rawValue;
    }
}
