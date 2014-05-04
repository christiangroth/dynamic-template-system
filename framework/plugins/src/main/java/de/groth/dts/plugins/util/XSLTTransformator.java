package de.groth.dts.plugins.util;

import java.io.File;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

/**
 * Utility class handling xslt transformation using dom4j.
 * 
 * @author Christian Groth
 * 
 */
public final class XSLTTransformator {
    private static final Logger LOGGER = Logger
            .getLogger(XSLTTransformator.class);

    private XSLTTransformator() {

    }

    /**
     * Transforms the given source-document using the referenced xsl-document
     * and returns the result string.
     * 
     * @param xmlPath
     *                path to referenced xml-document
     * @param xsltPath
     *                path to referenced xsl-document
     * @return strign representing the transformed document
     * @throws TransformerException
     *                 in case of transformation error
     */
    public static String transform(final String xmlPath, final String xsltPath)
            throws TransformerException {
        XSLTTransformator.LOGGER.debug("testing for xml file " + xmlPath);
        final File testXmlFile = new File(xmlPath);
        XSLTTransformator.LOGGER.debug("xml: " + testXmlFile.getAbsolutePath());
        if (testXmlFile == null || !testXmlFile.exists()
                || !testXmlFile.isFile() || !testXmlFile.canRead()) {
            throw new TransformerException(
                    "Unable to resolve xml-document with given path: "
                            + xmlPath + "!!");
        }

        XSLTTransformator.LOGGER.debug("testing for xsl file " + xsltPath);
        final File testXsltFile = new File(xsltPath);
        XSLTTransformator.LOGGER.debug(testXsltFile.getAbsolutePath());
        if (testXsltFile == null || !testXsltFile.exists()
                || !testXsltFile.isFile() || !testXsltFile.canRead()) {
            throw new TransformerException(
                    "Unable to resolve xslt-document with given path: "
                            + xsltPath + "!!");
        }

        XSLTTransformator.LOGGER
                .debug("creating Sources, TransformerFactory and Trasnformer");
        final Source xml = new StreamSource(xmlPath);
        final Source xsl = new StreamSource(xsltPath);

        final TransformerFactory factory = TransformerFactory.newInstance();
        final Transformer transformer = factory.newTransformer(xsl);

        XSLTTransformator.LOGGER
                .debug("creating and transforming result document");
        final StreamResult result = new StreamResult(new StringWriter());

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
        transformer.transform(xml, result);

        return result.getWriter().toString();
    }
}
