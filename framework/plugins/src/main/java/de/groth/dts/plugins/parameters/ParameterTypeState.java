package de.groth.dts.plugins.parameters;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.dom4j.Node;

import de.groth.dts.api.core.dao.PluginInstantiationContext;
import de.groth.dts.api.core.dto.plugins.PluginConstructor;
import de.groth.dts.api.core.dto.plugins.PluginToXml;
import de.groth.dts.api.core.dto.plugins.parameter.LazyInitializationParameter;
import de.groth.dts.api.core.exception.dto.UnknownStateValueException;
import de.groth.dts.api.core.exception.plugins.LazyInitializationException;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;
import de.groth.dts.api.core.exception.plugins.parameters.ParameterValueException;
import de.groth.dts.api.xml.util.XmlHelper;
import de.groth.dts.plugins.exception.UnknownStateException;
import de.groth.dts.plugins.util.StateValueResolver;

/**
 * Reads its value from a defined state.
 * 
 * <pre>
 * &lt;param type=&quot;...&quot; name=&quot;...&quot; state=&quot;&lt;someStateId&gt;&quot; value=&quot;&lt;stateConditionValue&gt;&quot; /&gt;
 * </pre>
 * 
 * Attributes capable of lazy initialization:
 * <ul>
 * <li>state</li>
 * <li>value</li>
 * </ul>
 * 
 * @author Christian Groth
 */
public class ParameterTypeState extends LazyInitializationParameter {
    private static final Logger LOGGER = Logger
            .getLogger(ParameterTypeState.class);

    private static final String XML_ATT_VALUE = "value";
    private static final String XML_ATT_STATE = "state";

    private String stateId;
    private String stateCondition;

    /**
     * Creates a new instance (annotated as {@link PluginConstructor})
     * 
     * @param config
     *                current {@link PluginInstantiationContext}
     * @throws PluginInitializationException
     */
    @PluginConstructor
    public ParameterTypeState(final PluginInstantiationContext config)
            throws PluginInitializationException {
        super(config.getPluginKey(), config.getPluginClassName(), config);

        ParameterTypeState.LOGGER.debug(this.getParameterName() + ": creating");

        final Node node = config.getNode();
        final String stateCondition = XmlHelper.nodeAttributeValue(node,
                ParameterTypeState.XML_ATT_VALUE);
        final String stateId = XmlHelper.nodeAttributeValue(node,
                ParameterTypeState.XML_ATT_STATE);
        ParameterTypeState.LOGGER.debug("state=" + stateId + ", condition="
                + stateCondition);

        if (stateId == null || stateId.trim().equals("")) {
            throw new PluginInitializationException(this.getParameterName()
                    + ": stateId must be set!!");
        }

        this.stateId = stateId;
        this.stateCondition = stateCondition;
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
        ParameterTypeState.LOGGER.debug(this.getParameterName()
                + ": writing to xml");
        parameterElement.addAttribute(ParameterTypeState.XML_ATT_STATE,
                this.stateId);
        parameterElement.addAttribute(ParameterTypeState.XML_ATT_VALUE,
                this.stateCondition);
    }

    /**
     * @see de.groth.dts.api.core.dto.parameters.IParameter#getValue()
     */
    public String getValue() throws ParameterValueException {
        try {
            ParameterTypeState.LOGGER.debug(this.getParameterName()
                    + ": invoking StateValueResolver");
            return StateValueResolver.resolveConditionValue(this.stateId,
                    this.context.getDts(), this.stateCondition);
        } catch (final UnknownStateValueException ex) {
            throw new ParameterValueException(
                    "caught UnknownStateValueException!!", ex);
        } catch (final UnknownStateException ex) {
            throw new ParameterValueException(
                    "caught UnknownStateValueException!!", ex);
        }
    }

    /**
     * @see de.groth.dts.api.core.dto.parameters.ILazyInitializationCapable#getLazyInitializationAttributeValues()
     */
    public String[] getLazyInitializationAttributeValues() {
        return new String[] { this.stateId, this.stateCondition };
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
        ParameterTypeState.LOGGER.debug(this.getParameterName()
                + ": lazy initialization changes " + originalValue + " to "
                + newValue);
        if (originalValue.equals(this.stateId)) {
            this.stateId = newValue;
            return;
        } else if (originalValue.equals(this.stateCondition)) {
            this.stateCondition = newValue;
            return;
        }

        this.throwUnmatchedValueForLazyInitializationException(originalValue);
    }
}
