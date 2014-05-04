package de.groth.dts.plugins.parameters;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.dom4j.Node;

import de.groth.dts.api.core.dao.PluginInstantiationContext;
import de.groth.dts.api.core.dto.IInsertionPattern;
import de.groth.dts.api.core.dto.plugins.PluginConstructor;
import de.groth.dts.api.core.dto.plugins.PluginToXml;
import de.groth.dts.api.core.dto.plugins.parameter.LazyInitializationParameter;
import de.groth.dts.api.core.exception.plugins.LazyInitializationException;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;
import de.groth.dts.api.core.exception.plugins.parameters.ParameterValueException;
import de.groth.dts.api.xml.util.XmlHelper;
import de.groth.dts.plugins.exception.InsertionPatternException;
import de.groth.dts.plugins.util.InsertionPatternValueResolver;

/**
 * Includes the processed value fo the referenced {@link IInsertionPattern}:
 * 
 * <pre>
 * &lt;param type=&quot;...&quot; name=&quot;...&quot; insertionPattern=&quot;&lt;insertionPatternId&gt;&quot; activeValue=&quot;&lt;activeValue&gt;&quot; inactiveValue=&quot;&lt;inactiveValue&gt;&quot; unreplacedValue=&quot;&lt;unreplacedValue&gt;&quot; active=&quot;&lt;commaSeperatedActiveInsertionPoints&gt;&quot; inactive=&quot;&lt;commaSeperatedInactiveInsertionPoints&gt;&quot; /&gt;
 * </pre>
 * 
 * Attributes capable of lazy initialization:
 * <ul>
 * <li>insertionPattern</li>
 * <li>activeValue</li>
 * <li>inactiveValue</li>
 * <li>unreplacedValue</li>
 * <li>active</li>
 * <li>inactive</li>
 * </ul>
 * 
 * @see InsertionPatternValueResolver for process of resolving the referenced
 *      {@link IInsertionPattern}
 * 
 * @author Christian Groth
 * 
 */
public class ParameterTypeInsertionPattern extends LazyInitializationParameter {
    private static final Logger LOGGER = Logger
            .getLogger(ParameterTypeInsertionPattern.class);

    private static final String XML_ATT_ID = "insertionPattern";
    private static final String XML_ATT_ACTIVE_VALUE = "activeValue";
    private static final String XML_ATT_INACTIVE_VALUE = "inactiveValue";
    private static final String XML_ATT_UNREPLACED_VALUE = "unreplacedValue";
    private static final String XML_ATT_ACTIVE_INSERTION_POINTS = "active";
    private static final String XML_ATT_INACTIVE_INSERTION_POINTS = "inactive";

    private String insertionPatternId;
    private String activeValue;
    private String inactiveValue;
    private String unreplacedValue;
    private String active;
    private String inactive;

    /**
     * Creates a new instance (annotated as {@link PluginConstructor})
     * 
     * @param config
     *                current {@link PluginInstantiationContext}
     * @throws PluginInitializationException
     */
    @PluginConstructor
    public ParameterTypeInsertionPattern(final PluginInstantiationContext config)
            throws PluginInitializationException {
        super(config.getPluginKey(), config.getPluginClassName(), config);

        ParameterTypeInsertionPattern.LOGGER.debug(this.getParameterName()
                + ": creating");

        final Node node = config.getNode();
        this.insertionPatternId = XmlHelper.nodeAttributeValue(node,
                ParameterTypeInsertionPattern.XML_ATT_ID);
        this.activeValue = XmlHelper.nodeAttributeValue(node,
                ParameterTypeInsertionPattern.XML_ATT_ACTIVE_VALUE);
        this.inactiveValue = XmlHelper.nodeAttributeValue(node,
                ParameterTypeInsertionPattern.XML_ATT_INACTIVE_VALUE);
        this.unreplacedValue = XmlHelper.nodeAttributeValue(node,
                ParameterTypeInsertionPattern.XML_ATT_UNREPLACED_VALUE);
        this.active = XmlHelper.nodeAttributeValue(node,
                ParameterTypeInsertionPattern.XML_ATT_ACTIVE_INSERTION_POINTS);
        this.inactive = XmlHelper
                .nodeAttributeValue(
                        node,
                        ParameterTypeInsertionPattern.XML_ATT_INACTIVE_INSERTION_POINTS);
        ParameterTypeInsertionPattern.LOGGER.debug(this.getParameterName()
                + ": insertionPatternId=" + this.insertionPatternId
                + ", activeValue=" + this.activeValue + ", inactiveValue="
                + this.inactiveValue + ", unreplacedValue="
                + this.unreplacedValue + ", active=" + this.active
                + ", inactive=" + this.inactive);

        if (this.insertionPatternId == null
                || this.insertionPatternId.trim().equals("")) {
            throw new PluginInitializationException(this.getParameterName()
                    + ": insertionPattern must be set!!");
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
        ParameterTypeInsertionPattern.LOGGER.debug(this.getParameterName()
                + ": writing to xml");

        parameterElement.addAttribute(ParameterTypeInsertionPattern.XML_ATT_ID,
                this.insertionPatternId);
        parameterElement.addAttribute(
                ParameterTypeInsertionPattern.XML_ATT_ACTIVE_VALUE,
                this.activeValue);
        parameterElement.addAttribute(
                ParameterTypeInsertionPattern.XML_ATT_INACTIVE_VALUE,
                this.inactiveValue);
        parameterElement.addAttribute(
                ParameterTypeInsertionPattern.XML_ATT_UNREPLACED_VALUE,
                this.unreplacedValue);
    }

    /**
     * @see de.groth.dts.api.core.dto.parameters.IParameter#getValue()
     */
    public String getValue() throws ParameterValueException {
        ParameterTypeInsertionPattern.LOGGER.debug(this.getParameterName()
                + ": getting IInsertionPattern by id "
                + this.insertionPatternId);
        final IInsertionPattern insertionPattern = this.context.getDts()
                .getInsertionPatternById(this.insertionPatternId);
        if (insertionPattern == null) {
            throw new ParameterValueException(this.getParameterName()
                    + ": unable to resolve InsertionPattern with id"
                    + this.insertionPatternId + "!!");
        }

        final String[] activeInsertionPoints = this.active == null
                || this.active.trim().equals("") ? null : this.active
                .split(",");
        final String[] inactiveInsertionPoints = this.inactive == null
                || this.inactive.trim().equals("") ? null : this.inactive
                .split(",");
        final String activeValue = this.activeValue == null
                || this.activeValue.trim().equals("") ? insertionPattern
                .getDefaultActiveValue() : this.activeValue;
        final String inactiveValue = this.inactiveValue == null
                || this.inactiveValue.trim().equals("") ? insertionPattern
                .getDefaultInactiveValue() : this.inactiveValue;
        final String unreplacedValue = this.unreplacedValue == null
                || this.unreplacedValue.trim().equals("") ? insertionPattern
                .getDefaultUnreplacedValue() : this.unreplacedValue;

        ParameterTypeInsertionPattern.LOGGER
                .debug(this.getParameterName()
                        + ": calling InsertionPatternValueResolver with args: insertionPattern="
                        + insertionPattern + ", activeInsertionPoints="
                        + this.active + ", activeValue=" + activeValue
                        + ", inactiveInsertionPoints=" + this.inactive
                        + ", inactiveValue=" + inactiveValue
                        + ", unreplacedValue=" + unreplacedValue);

        try {
            return InsertionPatternValueResolver.convert(insertionPattern,
                    activeInsertionPoints, activeValue,
                    inactiveInsertionPoints, inactiveValue, unreplacedValue);
        } catch (final InsertionPatternException ex) {
            throw new ParameterValueException(
                    "caught ParameterValueException!!", ex);
        }
    }

    /**
     * @see de.groth.dts.api.core.dto.parameters.ILazyInitializationCapable#getLazyInitializationAttributeValues()
     */
    public String[] getLazyInitializationAttributeValues() {
        return new String[] { this.insertionPatternId, this.activeValue,
                this.inactiveValue, this.unreplacedValue, this.active,
                this.inactive };
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
        ParameterTypeInsertionPattern.LOGGER.debug(this.getParameterName()
                + ": lazy initialization changes " + originalValue + " to "
                + newValue);
        if (originalValue.equals(this.insertionPatternId)) {
            this.insertionPatternId = newValue;
            return;
        } else if (originalValue.equals(this.activeValue)) {
            this.activeValue = newValue;
            return;
        } else if (originalValue.equals(this.inactiveValue)) {
            this.inactiveValue = newValue;
            return;
        } else if (originalValue.equals(this.unreplacedValue)) {
            this.unreplacedValue = newValue;
            return;
        } else if (originalValue.equals(this.active)) {
            this.active = newValue;
            return;
        } else if (originalValue.equals(this.inactive)) {
            this.inactive = newValue;
            return;
        }

        this.throwUnmatchedValueForLazyInitializationException(originalValue);
    }

}
