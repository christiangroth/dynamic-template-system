package de.groth.dts.plugins.generics;

import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;

import de.groth.dts.api.core.dao.GenericContext;
import de.groth.dts.api.core.dao.PluginInstantiationContext;
import de.groth.dts.api.core.dto.plugins.PluginConstructor;
import de.groth.dts.api.core.dto.plugins.generics.AbstractGeneric;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;
import de.groth.dts.api.core.exception.plugins.generics.GenericValueException;
import de.groth.dts.api.core.util.FileHelper;
import de.groth.dts.plugins.util.XSLTTransformator;

/**
 * Generic for processing xsl-tranformations on given xml source. ${...:xml=<pathToXmlFile>,xsl=<pathToXslFile>}
 * The path to xml and xsl file is relative to baseDtsPath.
 * 
 * @author Christian Groth
 * 
 */
public class GenericTypeXslt extends AbstractGeneric {
    private static final Logger LOGGER = Logger
            .getLogger(GenericTypeXslt.class);

    private static final String PARAMETER_XML = "xml";
    private static final String PARAMETER_XSL = "xsl";

    /**
     * Creates a new instance (annotated as {@link PluginConstructor})
     * 
     * @param config
     *                current {@link PluginInstantiationContext}
     * @throws PluginInitializationException
     */
    @PluginConstructor
    public GenericTypeXslt(final PluginInstantiationContext config)
            throws PluginInitializationException {
        super(config.getPluginKey(), config.getPluginClassName());
    }

    /**
     * @see de.groth.dts.api.core.dto.generics.IGeneric#getValue(de.groth.dts.api.core.dao.GenericContext)
     */
    public String getValue(final GenericContext context)
            throws GenericValueException {
        final String xml = context
                .getGenericAttribute(GenericTypeXslt.PARAMETER_XML);
        GenericTypeXslt.LOGGER.debug(this.getGenericName() + ": xml=" + xml);
        if (xml == null || xml.trim().equals("")) {
            throw new GenericValueException(
                    "Unable to start xsl-transformation: xml must not be null or empty!!");
        }

        final String xsl = context
                .getGenericAttribute(GenericTypeXslt.PARAMETER_XSL);
        GenericTypeXslt.LOGGER.debug(this.getGenericName() + ": xsl=" + xsl);
        if (xsl == null || xsl.trim().equals("")) {
            throw new GenericValueException(
                    "Unable to start xsl-transformation: xsl must not be null or empty!!");
        }

        final String xmlPath = FileHelper.combinePath(context.getBaseDtsPath(),
                xml);
        final String xsltPath = FileHelper.combinePath(
                context.getBaseDtsPath(), xsl);
        try {
            GenericTypeXslt.LOGGER.debug(this.getGenericName()
                    + ": invoking XSLTTransformator");
            return XSLTTransformator.transform(xmlPath, xsltPath);
        } catch (final TransformerException ex) {
            throw new GenericValueException(
                    "Call to XSTLTransformator failed!!", ex);
        }
    }
}
