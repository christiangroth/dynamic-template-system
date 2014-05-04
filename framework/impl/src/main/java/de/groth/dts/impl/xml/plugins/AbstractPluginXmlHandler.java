package de.groth.dts.impl.xml.plugins;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.dom4j.Node;

import de.groth.dts.api.core.dto.plugins.AbstractPlugin;
import de.groth.dts.api.core.exception.dto.DtoInitializationException;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;
import de.groth.dts.api.xml.plugins.IAbstractPluginXmlHandler;
import de.groth.dts.api.xml.structure.PluginXmlInformation;
import de.groth.dts.api.xml.util.XmlHelper;

/**
 * Default implementation of {@link IAbstractPluginXmlHandler}.
 * 
 * @param <T>
 *                the concrete {@link AbstractPlugin} to be handled
 * 
 * @author Christian Groth
 */
public abstract class AbstractPluginXmlHandler<T extends AbstractPlugin>
        implements IAbstractPluginXmlHandler<T> {

    private static final Logger LOGGER = Logger
            .getLogger(AbstractPluginXmlHandler.class);

    /**
     * @see de.groth.dts.api.xml.IDtsDtoXmlHandler#fromXml(org.dom4j.Node)
     */
    public T fromXml(final Node node) throws DtoInitializationException {
        /*
         * Attributes
         */
        AbstractPluginXmlHandler.LOGGER.debug(node.getPath());
        AbstractPluginXmlHandler.LOGGER.debug("Reading Attributes");

        final String key = XmlHelper.nodeAttributeValue(node,
                PluginXmlInformation.ATT_KEY.getValue());
        final String className = XmlHelper.nodeAttributeValue(node,
                PluginXmlInformation.ATT_CLASSNAME.getValue());

        AbstractPluginXmlHandler.LOGGER.debug(PluginXmlInformation.ATT_KEY
                .getValue()
                + "="
                + key
                + ", "
                + PluginXmlInformation.ATT_CLASSNAME.getValue()
                + "="
                + className);

        AbstractPluginXmlHandler.LOGGER.debug("Creating Plugin-DTO");

        /*
         * Create PluginDto
         */
        try {
            return this.createPluginDto(key, className);
        } catch (final PluginInitializationException ex) {
            throw new DtoInitializationException(
                    "Unable to create plugin from xml!!", ex);
        }
    }

    /**
     * Creates an instance of the concrete plugin type.
     * 
     * @param key
     *                the plugin key, defined in xml
     * @param className
     *                the plugin classname, defined in xml
     * @return the new plugin instance
     * @throws PluginInitializationException
     *                 in case of error during instantiation
     */
    public abstract T createPluginDto(String key, String className)
            throws PluginInitializationException;

    /**
     * @see de.groth.dts.api.xml.IDtsDtoXmlHandler#toXml(de.groth.dts.api.core.dto.IDtsDto,
     *      org.dom4j.Element)
     */
    public void toXml(final T instance, final Element parent) {
        final Element pluginNode = parent.addElement(this.getXmlTagName());

        pluginNode.addAttribute(PluginXmlInformation.ATT_KEY.getValue(),
                instance.getPluginKey());
        pluginNode.addAttribute(PluginXmlInformation.ATT_CLASSNAME.getValue(),
                instance.getPluginClassName());
    }

    /**
     * Returns the xml tag name for the concrete plugin type.
     * 
     * @return the tag name
     */
    protected abstract String getXmlTagName();
}
