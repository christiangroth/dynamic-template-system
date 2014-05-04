package de.groth.dts.plugins.parameters;

import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.dom4j.Node;

import de.groth.dts.api.core.dao.PluginInstantiationContext;
import de.groth.dts.api.core.dto.plugins.PluginConstructor;
import de.groth.dts.api.core.dto.plugins.PluginToXml;
import de.groth.dts.api.core.dto.plugins.parameter.LazyInitializationParameter;
import de.groth.dts.api.core.exception.plugins.LazyInitializationException;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;
import de.groth.dts.api.core.exception.plugins.parameters.ParameterValueException;
import de.groth.dts.api.core.util.FileHelper;
import de.groth.dts.api.xml.util.XmlHelper;
import de.groth.dts.plugins.util.XSLTTransformator;

/**
 * Parameter for processing xsl-tranformations on given xml source. The path to
 * xml and xsl file is relative to baseDtsPath.
 * 
 * <pre>
 * &lt;param type=&quot;...&quot; name=&quot;...&quot; xml=&quot;&lt;pathToXmlFile&gt;&quot; xsl=&quot;&lt;pathToXslFile&gt;&quot; /&gt;
 * </pre>
 * 
 * Attributes capable of lazy initialization:
 * <ul>
 * <li>xml</li>
 * <li>xsl</li>
 * </ul>
 * 
 * @author Christian Groth
 * 
 */
public class ParameterTypeXslt extends LazyInitializationParameter {
    private static final Logger LOGGER = Logger
            .getLogger(ParameterTypeXslt.class);

    private static final String XML_ATT_XML = "xml";
    private static final String XML_ATT_XSL = "xsl";

    private String xml;
    private String xsl;
    private final String baseDtsPath;

    /**
     * Creates a new instance (annotated as {@link PluginConstructor})
     * 
     * @param config
     *                current {@link PluginInstantiationContext}
     * @throws PluginInitializationException
     */
    @PluginConstructor
    public ParameterTypeXslt(final PluginInstantiationContext config)
            throws PluginInitializationException {
        super(config.getPluginKey(), config.getPluginClassName(), config);

        ParameterTypeXslt.LOGGER.debug(this.getParameterName()
                + ": creating to xml");

        final Node node = config.getNode();
        final String xml = XmlHelper.nodeAttributeValue(node,
                ParameterTypeXslt.XML_ATT_XML);
        final String xsl = XmlHelper.nodeAttributeValue(node,
                ParameterTypeXslt.XML_ATT_XSL);
        ParameterTypeXslt.LOGGER.debug("xml=" + xml + ", xsl=" + xsl);

        if (xml == null || xml.trim().equals("")) {
            throw new PluginInitializationException(this.getParameterName()
                    + ": xml must be set!!");
        }

        if (xsl == null || xsl.trim().equals("")) {
            throw new PluginInitializationException(this.getParameterName()
                    + ": xslKey must be set!!");
        }

        this.xml = xml;
        this.xsl = xsl;
        this.baseDtsPath = config.getBaseDtsPath();
    }

    /**
     * Transforms the current instance to its xml representation.
     * 
     * @param parameterElement
     *                {@link Element} for current instance
     * 
     * @see PluginToXml
     * 
     */
    @PluginToXml
    public void toXml(final Element parameterElement) {
        ParameterTypeXslt.LOGGER.debug(this.getParameterName()
                + ": writing to xml");
        parameterElement.addAttribute(ParameterTypeXslt.XML_ATT_XML, this.xml);
        parameterElement.addAttribute(ParameterTypeXslt.XML_ATT_XSL, this.xsl);
    }

    /**
     * @see de.groth.dts.api.core.dto.parameters.IParameter#getValue()
     */
    public String getValue() throws ParameterValueException {
        try {
            ParameterTypeXslt.LOGGER.debug(this.getParameterName()
                    + ": invoking XSLTTransformator");
            return XSLTTransformator.transform(FileHelper.combinePath(
                    this.baseDtsPath, this.xml), FileHelper.combinePath(
                    this.baseDtsPath, this.xsl));
        } catch (final TransformerException ex) {
            throw new ParameterValueException(
                    "Call to XSTLTransformator failed!!", ex);
        }
    }

    /**
     * @see de.groth.dts.api.core.dto.parameters.ILazyInitializationCapable#getLazyInitializationAttributeValues()
     */
    public String[] getLazyInitializationAttributeValues() {
        return new String[] { this.xml, this.xsl };
    }

    /**
     * @see de.groth.dts.api.core.dto.parameters.ILazyInitializationCapable#setLazyInitializationAttributeValue(java.lang.String,
     *      java.lang.String)
     */
    public void setLazyInitializationAttributeValue(final String newValue,
            final String originalValue) throws LazyInitializationException {
        ParameterTypeXslt.LOGGER.debug(this.getParameterName()
                + ": lazy initialization changes " + originalValue + " to "
                + newValue);
        if (this.xml.equals(originalValue)) {
            this.xml = newValue;
        } else if (this.xsl.equals(originalValue)) {
            this.xsl = newValue;
        }

        this.throwUnmatchedValueForLazyInitializationException(originalValue);
    }

    /**
     * @see de.groth.dts.api.core.dto.parameters.ILazyInitializationCapable#postLazyInitializationCallback()
     */
    public void postLazyInitializationCallback() {
        /* nothing to do here */
    }
}
