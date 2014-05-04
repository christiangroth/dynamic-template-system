package de.groth.dts.plugins.parameters;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.dom4j.Node;

import de.groth.dts.api.core.dao.PluginInstantiationContext;
import de.groth.dts.api.core.dto.plugins.PluginConstructor;
import de.groth.dts.api.core.dto.plugins.PluginToXml;
import de.groth.dts.api.core.dto.plugins.parameter.LazyInitializationParameter;
import de.groth.dts.api.core.exception.convert.ConverterException;
import de.groth.dts.api.core.exception.plugins.LazyInitializationException;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;
import de.groth.dts.api.core.exception.plugins.parameters.ParameterValueException;
import de.groth.dts.api.xml.util.XmlHelper;

/**
 * Parameter to include a converted page as content. The referenced page will be
 * converted and the output is set as paramater-value. Be sure not to create
 * circular dependencies!!
 * 
 * <pre>
 * &lt;param type=&quot;...&quot; name=&quot;...&quot; page=&quot;&lt;somePageId&gt;&quot;  /&gt;
 * </pre>
 * 
 * Attributes capable of lazy initialization:
 * <ul>
 * <li>page</li>
 * </ul>
 * 
 * @author Christian Groth
 */
public class ParameterTypePageInclude extends LazyInitializationParameter {
    private static final Logger LOGGER = Logger
            .getLogger(ParameterTypePageInclude.class);

    private static final String XML_ATT_PAGE_ID = "page";

    private String pageId;

    /**
     * Creates a new instance (annotated as {@link PluginConstructor})
     * 
     * @param config
     *                current {@link PluginInstantiationContext}
     * @throws PluginInitializationException
     */
    @PluginConstructor
    public ParameterTypePageInclude(final PluginInstantiationContext config)
            throws PluginInitializationException {
        super(config.getPluginKey(), config.getPluginClassName(), config);

        ParameterTypePageInclude.LOGGER.debug(this.getParameterName()
                + ": creating");

        final Node node = config.getNode();
        this.pageId = XmlHelper.nodeAttributeValue(node,
                ParameterTypePageInclude.XML_ATT_PAGE_ID);

        if (this.pageId == null || this.pageId.trim().equals("")) {
            throw new PluginInitializationException(this.getParameterName()
                    + ": pageId must be set!!");
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
        ParameterTypePageInclude.LOGGER.debug(this.getParameterName()
                + ": writing to xml");
        parameterElement.addAttribute(ParameterTypePageInclude.XML_ATT_PAGE_ID,
                this.pageId);
    }

    /**
     * @see de.groth.dts.api.core.dto.parameters.IParameter#getValue()
     */
    public String getValue() throws ParameterValueException {
        try {
            ParameterTypePageInclude.LOGGER.debug(this.getParameterName()
                    + ": converting page with id " + this.pageId);
            return this.context.getDts().getConverter().convert(this.pageId);
        } catch (final ConverterException ex) {
            throw new ParameterValueException("Unable to convert nested page "
                    + this.pageId + " for "
                    + this.context.getCurrentPage().getId() + "!!", ex);
        }
    }

    /**
     * @see de.groth.dts.api.core.dto.parameters.ILazyInitializationCapable#getLazyInitializationAttributeValues()
     */
    public String[] getLazyInitializationAttributeValues() {
        return new String[] { this.pageId };
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
        ParameterTypePageInclude.LOGGER.debug(this.getParameterName()
                + ": lazy initialization changes " + originalValue + " to "
                + newValue);
        if (originalValue.equals(this.pageId)) {
            this.pageId = newValue;
            return;
        }

        this.throwUnmatchedValueForLazyInitializationException(originalValue);
    }
}
