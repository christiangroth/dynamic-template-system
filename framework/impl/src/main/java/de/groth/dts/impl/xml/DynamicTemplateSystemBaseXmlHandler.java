package de.groth.dts.impl.xml;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.dom4j.Node;

import de.groth.dts.api.core.IConverter;
import de.groth.dts.api.core.dto.IDynamicTemplateSystemBase;
import de.groth.dts.api.core.dto.IDynamicTemplateSystemProxy;
import de.groth.dts.api.core.dto.plugins.generics.PluginTypeGeneric;
import de.groth.dts.api.core.dto.plugins.parameter.PluginTypeParameter;
import de.groth.dts.api.core.dto.plugins.processings.PluginTypeProcessing;
import de.groth.dts.api.core.exception.dto.DtoInitializationException;
import de.groth.dts.api.core.exception.plugins.PluginRegistrationException;
import de.groth.dts.api.xml.IDynamicTemplateSystemBaseXmlHandler;
import de.groth.dts.api.xml.exception.XmlException;
import de.groth.dts.api.xml.structure.DynamicTemplateSystemXmlInformation;
import de.groth.dts.api.xml.structure.PluginXmlInformation;
import de.groth.dts.api.xml.util.XmlHelper;
import de.groth.dts.impl.core.dto.DynamicTemplateSystemProxy;
import de.groth.dts.impl.xml.plugins.GenericPluginXmlHandler;
import de.groth.dts.impl.xml.plugins.ParameterPluginXmlHandler;
import de.groth.dts.impl.xml.plugins.ProcessingPluginXmlHandler;

/**
 * Default implementation of {@link IDynamicTemplateSystemBaseXmlHandler}.
 * 
 * @author Christian Groth
 */
public class DynamicTemplateSystemBaseXmlHandler implements
        IDynamicTemplateSystemBaseXmlHandler {
    private static final Logger LOGGER = Logger
            .getLogger(DynamicTemplateSystemBaseXmlHandler.class);

    private final String dtsFilePath;
    private final IConverter converter;

    /**
     * Creates a new instance
     * 
     * @param dtsFilePath
     *                dts file path
     * @param converter
     *                an {@link IConverter}
     */
    public DynamicTemplateSystemBaseXmlHandler(final String dtsFilePath,
            final IConverter converter) {
        this.dtsFilePath = dtsFilePath;
        this.converter = converter;
    }

    /**
     * @see de.groth.dts.api.xml.IDtsDtoXmlHandler#fromXml(org.dom4j.Node)
     */
    public IDynamicTemplateSystemBase fromXml(final Node node)
            throws DtoInitializationException {
        if (!node.getName().equals(
                DynamicTemplateSystemXmlInformation.NODE_NAME.getValue())) {
            throw new DtoInitializationException(
                    "Unable to create DynamicTemplateSystemProxy due to incorrect node. Node is "
                            + node.getName()
                            + " but expected was "
                            + DynamicTemplateSystemXmlInformation.NODE_NAME
                                    .getValue() + "!!");
        }

        /*
         * Attributes
         */
        DynamicTemplateSystemBaseXmlHandler.LOGGER.debug(node.getPath());
        DynamicTemplateSystemBaseXmlHandler.LOGGER.debug("Reading Attributes");
        final String id = XmlHelper.nodeAttributeValue(node,
                DynamicTemplateSystemXmlInformation.ATT_ID.getValue());
        final String name = XmlHelper.nodeAttributeValue(node,
                DynamicTemplateSystemXmlInformation.ATT_NAME.getValue());
        DynamicTemplateSystemBaseXmlHandler.LOGGER
                .debug(DynamicTemplateSystemXmlInformation.ATT_ID.getValue()
                        + "="
                        + id
                        + ", "
                        + DynamicTemplateSystemXmlInformation.ATT_NAME
                                .getValue() + "=" + name);

        /*
         * Create DynamicTemplateSystemProxy
         */
        DynamicTemplateSystemBaseXmlHandler.LOGGER.debug("Creating DTS-Proxy");
        final IDynamicTemplateSystemProxy proxy = new DynamicTemplateSystemProxy(
                this.dtsFilePath, id, name, this.converter);

        /*
         * Processing Plugins
         */
        DynamicTemplateSystemBaseXmlHandler.LOGGER.debug("Registering Plugins");

        ArrayList<PluginTypeProcessing> processingPlugins;
        try {
            processingPlugins = new XmlHelper<PluginTypeProcessing>()
                    .nodesAsDtos(node, new ProcessingPluginXmlHandler(),
                            DynamicTemplateSystemXmlInformation.PATH_PLUGINS
                                    .getValue()
                                    + "/"
                                    + PluginXmlInformation.PATH_PROCESSING
                                            .getValue());
        } catch (final XmlException ex) {
            throw new DtoInitializationException("caught XmlException!!", ex);
        }

        for (final PluginTypeProcessing processingPlugin : processingPlugins) {
            try {
                Class.forName(processingPlugin.getPluginClassName());
                DynamicTemplateSystemBaseXmlHandler.LOGGER
                        .info("Registering Processing-Plugin: "
                                + processingPlugin.getPluginKey() + " ("
                                + processingPlugin.getPluginClassName() + ")");
                proxy.registerProcessing(processingPlugin);
            } catch (final ClassNotFoundException ex) {
                throw new DtoInitializationException(
                        "unknown class, check classpath: "
                                + processingPlugin.getPluginClassName() + "!!",
                        ex);
            } catch (final PluginRegistrationException ex) {
                throw new DtoInitializationException(
                        "Unable to register processing-plugin!!", ex);
            }
        }

        /*
         * Parameter Plugins
         */
        final ArrayList<PluginTypeParameter> parameterPlugins;
        try {
            parameterPlugins = new XmlHelper<PluginTypeParameter>()
                    .nodesAsDtos(node, new ParameterPluginXmlHandler(),
                            DynamicTemplateSystemXmlInformation.PATH_PLUGINS
                                    .getValue()
                                    + "/"
                                    + PluginXmlInformation.PATH_PARAMETER
                                            .getValue());
        } catch (final XmlException ex) {
            throw new DtoInitializationException("caught XmlException!!", ex);
        }

        for (final PluginTypeParameter parameterPlugin : parameterPlugins) {
            try {
                Class.forName(parameterPlugin.getPluginClassName());
                DynamicTemplateSystemBaseXmlHandler.LOGGER
                        .info("Registering Parameter-Plugin: "
                                + parameterPlugin.getPluginKey() + " ("
                                + parameterPlugin.getPluginClassName() + ")");
                proxy.registerParameter(parameterPlugin);
            } catch (final ClassNotFoundException ex) {
                throw new DtoInitializationException(
                        "unknown class, check classpath: "
                                + parameterPlugin.getPluginClassName() + "!!",
                        ex);
            } catch (final PluginRegistrationException ex) {
                throw new DtoInitializationException(
                        "Unable to register parameter-plugin!!", ex);
            }
        }

        /*
         * Generic Plugins
         */
        final ArrayList<PluginTypeGeneric> genericPlugins;
        try {
            genericPlugins = new XmlHelper<PluginTypeGeneric>().nodesAsDtos(
                    node, new GenericPluginXmlHandler(),
                    DynamicTemplateSystemXmlInformation.PATH_PLUGINS.getValue()
                            + "/"
                            + PluginXmlInformation.PATH_GENERIC.getValue());
        } catch (final XmlException ex) {
            throw new DtoInitializationException("caught XmlException!!", ex);
        }

        for (final PluginTypeGeneric genericPlugin : genericPlugins) {
            try {
                Class.forName(genericPlugin.getPluginClassName());
                DynamicTemplateSystemBaseXmlHandler.LOGGER
                        .info("Registering Generic-Plugin: "
                                + genericPlugin.getPluginKey() + " ("
                                + genericPlugin.getPluginClassName() + ")");
                proxy.registerGeneric(genericPlugin);
            } catch (final ClassNotFoundException ex) {
                throw new DtoInitializationException(
                        "unknown class, check classpath: "
                                + genericPlugin.getPluginClassName() + "!!", ex);
            } catch (final PluginRegistrationException ex) {
                throw new DtoInitializationException(
                        "Unable to register generic-plugin!!", ex);
            }
        }

        return proxy;
    }

    /**
     * @see de.groth.dts.api.xml.IDtsDtoXmlHandler#toXml(de.groth.dts.api.core.dto.IDtsDto,
     *      org.dom4j.Element)
     */
    public void toXml(final IDynamicTemplateSystemBase instance,
            final Element parent) {
        /*
         * in this case parent is not really a parent because an instance of
         * type DynamicTemplateSystemProxy maps to the root element. So we get
         * the hopefully correctly named xml root element without any
         * attributes.
         * 
         * so ... in standard case we would add a new node - which represents
         * the given instance - to the given parent element so we build up our
         * document step by step. this time we just take the given parent (which
         * is root and the representation of our instance) and start building
         * ...
         */
        parent
                .setName(DynamicTemplateSystemXmlInformation.NODE_NAME
                        .getValue());

        /*
         * Attributes
         */
        final Element dynamicTemplateSystemNode = parent;
        dynamicTemplateSystemNode.addAttribute(
                DynamicTemplateSystemXmlInformation.ATT_ID.getValue(), instance
                        .getId());
        dynamicTemplateSystemNode.addAttribute(
                DynamicTemplateSystemXmlInformation.ATT_NAME.getValue(),
                instance.getName());

        /*
         * Plugins
         */
        final Element pluginsNode = dynamicTemplateSystemNode
                .addElement(DynamicTemplateSystemXmlInformation.PATH_PLUGINS
                        .getValue());
        for (final PluginTypeProcessing processingPlugin : instance
                .getPluginsProcessing().values()) {

            new ProcessingPluginXmlHandler().toXml(processingPlugin,
                    pluginsNode);
        }

        for (final PluginTypeParameter parameterPlugin : instance
                .getPluginsParameter().values()) {
            new ParameterPluginXmlHandler().toXml(parameterPlugin, pluginsNode);
        }

        for (final PluginTypeGeneric parameterGeneric : instance
                .getPluginsGeneric().values()) {
            new GenericPluginXmlHandler().toXml(parameterGeneric, pluginsNode);
        }
    }
}
