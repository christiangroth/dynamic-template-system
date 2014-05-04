package de.groth.dts.plugins.parameters;

import java.io.IOException;

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

/**
 * Includes the content of the referenced file. The path to the file is relative
 * to baseDtsPath.
 * 
 * <pre>
 * &lt;param type=&quot;...&quot; name=&quot;...&quot; file=&quot;&lt;pathToFile&gt;&quot; /&gt;
 * </pre>
 * 
 * Attributes capable of lazy initialization:
 * <ul>
 * <li>file</li>
 * </ul>
 * 
 * @author Christian Groth
 */
public class ParameterTypeFileContent extends LazyInitializationParameter {
    private static final Logger LOGGER = Logger
            .getLogger(ParameterTypeFileContent.class);

    private static final String XML_ATT_FILE = "file";

    private String file;
    private final String basePath;

    /**
     * Creates a new instance (annotated as {@link PluginConstructor})
     * 
     * @param config
     *                current {@link PluginInstantiationContext}
     * @throws PluginInitializationException
     */
    @PluginConstructor
    public ParameterTypeFileContent(final PluginInstantiationContext config)
            throws PluginInitializationException {
        super(config.getPluginKey(), config.getPluginClassName(), config);

        ParameterTypeFileContent.LOGGER.debug(this.getParameterName()
                + ": creating");

        final Node node = config.getNode();
        final String path = XmlHelper.nodeAttributeValue(node,
                ParameterTypeFileContent.XML_ATT_FILE);
        ParameterTypeFileContent.LOGGER.debug("file=" + path);

        this.file = path;
        this.basePath = config.getBaseDtsPath();

        if (this.file == null || this.file.trim().equals("")) {
            throw new PluginInitializationException(this.getParameterName()
                    + ": file must be set!!");
        }

        if (this.basePath == null || this.basePath.trim().equals("")) {
            throw new PluginInitializationException(this.getParameterName()
                    + ": basePath must be set!!");
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
        ParameterTypeFileContent.LOGGER.debug(this.getParameterName()
                + ": writing to xml");
        parameterElement.addAttribute(ParameterTypeFileContent.XML_ATT_FILE,
                this.file);
    }

    /**
     * @see de.groth.dts.api.core.dto.parameters.ILazyInitializationCapable#getLazyInitializationAttributeValues()
     */
    public String[] getLazyInitializationAttributeValues() {
        return new String[] { this.file };
    }

    /**
     * @see de.groth.dts.api.core.dto.parameters.ILazyInitializationCapable#postLazyInitializationCallback()
     */
    public void postLazyInitializationCallback() {
        /* nothing to do here */
    }

    /**
     * @see de.groth.dts.api.core.dto.parameters.ILazyInitializationCapable#setLazyInitializationAttributeValue(java.lang.String,
     *      java.lang.String)
     */
    public void setLazyInitializationAttributeValue(final String newValue,
            final String originalValue) throws LazyInitializationException {
        ParameterTypeFileContent.LOGGER.debug(this.getParameterName()
                + ": lazy initialization changes " + originalValue + " to "
                + newValue);
        if (originalValue.equals(this.file)) {
            this.file = newValue;
            return;
        }

        this.throwUnmatchedValueForLazyInitializationException(originalValue);
    }

    /**
     * @see de.groth.dts.api.core.dto.parameters.IParameter#getValue()
     */
    public String getValue() throws ParameterValueException {
        return this.initFileContent();
    }

    private String initFileContent() throws ParameterValueException {
        try {
            ParameterTypeFileContent.LOGGER.debug(this.getParameterName()
                    + ": reading content of file " + this.file);
            return FileHelper.getFileContent(FileHelper.combinePath(
                    this.basePath, this.file));
        } catch (final IOException ex) {
            throw new ParameterValueException(this.getParameterType()
                    + ": file does not exist or can't be read: "
                    + FileHelper.combinePath(this.basePath, this.file) + "!!");
        }
    }
}
