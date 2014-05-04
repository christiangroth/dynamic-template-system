package de.groth.dts.plugins.parameters;

import org.apache.log4j.Logger;
import org.dom4j.Element;

import de.groth.dts.api.core.dao.PluginInstantiationContext;
import de.groth.dts.api.core.dto.plugins.PluginConstructor;
import de.groth.dts.api.core.dto.plugins.PluginToXml;
import de.groth.dts.api.core.dto.plugins.parameter.AbstractParameter;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;
import de.groth.dts.api.core.exception.plugins.parameters.ParameterValueException;
import de.groth.dts.api.xml.util.XmlHelper;

/**
 * Prints out the value from its xml-attribute "value".
 * 
 * <pre>
 * &lt;param type=&quot;...&quot; name=&quot;...&quot; value=&quot;&lt;someValue&gt;&quot; /&gt;
 * </pre>
 * 
 * @author Christian Groth
 */
public class ParameterTypeSimple extends AbstractParameter {
    private static final Logger LOGGER = Logger
            .getLogger(ParameterTypeSimple.class);

    private static final String XML_ATT_VALUE = "value";

    private final String value;

    /**
     * Creates a new instance (annotated as {@link PluginConstructor})
     * 
     * @param config
     *                current {@link PluginInstantiationContext}
     * @throws PluginInitializationException
     */
    @PluginConstructor
    public ParameterTypeSimple(final PluginInstantiationContext config)
            throws PluginInitializationException {
        super(config.getPluginKey(), config.getPluginClassName(), config);

        ParameterTypeSimple.LOGGER
                .debug(this.getParameterName() + ": creating");

        this.value = XmlHelper.nodeAttributeValue(config.getNode(),
                ParameterTypeSimple.XML_ATT_VALUE);

        if (this.value == null || this.value.trim().equals("")) {
            throw new PluginInitializationException(this.getParameterName()
                    + ": value must not be empty!!");
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
        ParameterTypeSimple.LOGGER.debug(this.getParameterName()
                + ": writing to xml");
        parameterElement.addAttribute(ParameterTypeSimple.XML_ATT_VALUE,
                this.value);
    }

    /**
     * @see de.groth.dts.api.core.dto.parameters.IParameter#getValue()
     */
    public String getValue() throws ParameterValueException {
        return this.value;
    }
}
