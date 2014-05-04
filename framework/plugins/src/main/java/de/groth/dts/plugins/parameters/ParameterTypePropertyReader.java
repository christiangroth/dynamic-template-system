package de.groth.dts.plugins.parameters;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.dom4j.Node;

import de.groth.dts.api.core.dao.PluginInstantiationContext;
import de.groth.dts.api.core.dto.plugins.PluginConstructor;
import de.groth.dts.api.core.dto.plugins.PluginToXml;
import de.groth.dts.api.core.dto.plugins.parameter.LazyInitializationParameter;
import de.groth.dts.api.core.exception.DynamicTemplateSystemException;
import de.groth.dts.api.core.exception.plugins.LazyInitializationException;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;
import de.groth.dts.api.core.exception.plugins.parameters.ParameterValueException;
import de.groth.dts.api.core.util.FileHelper;
import de.groth.dts.api.xml.util.XmlHelper;
import de.groth.dts.plugins.util.PropertyReader;

/**
 * Parameter for reading a properties file and printing out the value to the
 * given key. The path to properties file is relative to baseDtsPath.
 * 
 * <pre>
 * &lt;param type=&quot;...&quot; name=&quot;...&quot; properties=&quot;&lt;pathToPropertiesFile&gt;&quot; key=&quot;&lt;propertyKey&gt;&quot; /&gt;
 * </pre>
 * 
 * Attributes capable of lazy initialization:
 * <ul>
 * <li>properties</li>
 * <li>key</li>
 * </ul>
 * 
 * @author Christian Groth
 * 
 */
public class ParameterTypePropertyReader extends LazyInitializationParameter {
    private static final Logger LOGGER = Logger
            .getLogger(ParameterTypePropertyReader.class);

    private static final String XML_ATT_PROP_FILE = "properties";
    private static final String XML_ATT_PROP_KEY = "key";

    private String propertyFile;
    private String propertyKey;

    /**
     * Creates a new instance (annotated as {@link PluginConstructor})
     * 
     * @param config
     *                current {@link PluginInstantiationContext}
     * @throws PluginInitializationException
     */
    @PluginConstructor
    public ParameterTypePropertyReader(final PluginInstantiationContext config)
            throws PluginInitializationException {
        super(config.getPluginKey(), config.getPluginClassName(), config);

        ParameterTypePropertyReader.LOGGER.debug(this.getParameterName()
                + ": creating");

        final Node node = config.getNode();
        final String path = XmlHelper.nodeAttributeValue(node,
                ParameterTypePropertyReader.XML_ATT_PROP_FILE);
        final String key = XmlHelper.nodeAttributeValue(node,
                ParameterTypePropertyReader.XML_ATT_PROP_KEY);
        ParameterTypePropertyReader.LOGGER.debug("file=" + path + ", key="
                + key);

        this.propertyFile = path;
        this.propertyKey = key;

        if (this.propertyFile == null || this.propertyFile.trim().equals("")) {
            throw new PluginInitializationException(this.getParameterName()
                    + ": propertyFile must be set!!");
        }

        if (this.propertyKey == null || this.propertyKey.trim().equals("")) {
            throw new PluginInitializationException(this.getParameterName()
                    + ": propertyKey must be set!!");
        }
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
        ParameterTypePropertyReader.LOGGER.debug(this.getParameterName()
                + ": writing to xml");
        parameterElement.addAttribute(
                ParameterTypePropertyReader.XML_ATT_PROP_FILE,
                this.propertyFile);
        parameterElement.addAttribute(
                ParameterTypePropertyReader.XML_ATT_PROP_KEY, this.propertyKey);
    }

    /**
     * @see de.groth.dts.api.core.dto.parameters.IParameter#getValue()
     */
    public String getValue() throws ParameterValueException {
        try {
            ParameterTypePropertyReader.LOGGER.debug(this.getParameterName()
                    + ": invoking PropertyReader");
            return new PropertyReader().getValue(FileHelper.combinePath(
                    this.context.getBaseDtsPath(), this.propertyFile),
                    this.propertyKey);
        } catch (final DynamicTemplateSystemException ex) {
            throw new ParameterValueException(
                    "Unable to invoke PropertyReader!!", ex);
        }
    }

    /**
     * @see de.groth.dts.api.core.dto.parameters.ILazyInitializationCapable#getLazyInitializationAttributeValues()
     */
    public String[] getLazyInitializationAttributeValues() {
        return new String[] { this.propertyFile, this.propertyKey };
    }

    /**
     * @see de.groth.dts.api.core.dto.parameters.ILazyInitializationCapable#setLazyInitializationAttributeValue(java.lang.String,
     *      java.lang.String)
     */
    public void setLazyInitializationAttributeValue(final String newValue,
            final String originalValue) throws LazyInitializationException {
        ParameterTypePropertyReader.LOGGER.debug(this.getParameterName()
                + ": lazy initialization changes " + originalValue + " to "
                + newValue);
        if (this.propertyFile.equals(originalValue)) {
            this.propertyFile = newValue;
            return;
        } else if (this.propertyKey.equals(originalValue)) {
            this.propertyKey = newValue;
            return;
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
