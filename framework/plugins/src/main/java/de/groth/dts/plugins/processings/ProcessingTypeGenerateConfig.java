package de.groth.dts.plugins.processings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import de.groth.dts.api.core.dao.DynamicTemplateSystemExecutionContext;
import de.groth.dts.api.core.dao.PluginInstantiationContext;
import de.groth.dts.api.core.dto.IDynamicTemplateSystem;
import de.groth.dts.api.core.dto.plugins.PluginConstructor;
import de.groth.dts.api.core.dto.plugins.PluginToXml;
import de.groth.dts.api.core.dto.plugins.processings.AbstractProcessing;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;
import de.groth.dts.api.core.util.FileHelper;
import de.groth.dts.api.xml.IDtsDtoXmlHandler;
import de.groth.dts.api.xml.exception.XmlException;
import de.groth.dts.api.xml.util.XmlHelper;

/**
 * Generates the xml config file from current instance of
 * {@link IDynamicTemplateSystem}. The path to the directory is relative to
 * baseExportPath.
 * 
 * <pre>
 *      &lt;processing type=&quot;...&quot; path=&quot;&lt;pathToDirectory&gt;&quot; file=&quot;&lt;filename&gt;&quot; /&gt;
 * </pre>
 * 
 * @author Christian Groth
 */
public class ProcessingTypeGenerateConfig extends AbstractProcessing {
    private static final Logger LOGGER = Logger
            .getLogger(ProcessingTypeGenerateConfig.class);

    private static final String XML_ATT_PATH = "path";
    private static final String XML_ATT_FILE = "file";

    private final String path;
    private final String fileName;

    /**
     * Creates a new instance (annotated as {@link PluginConstructor})
     * 
     * @param config
     *                current {@link PluginInstantiationContext}
     * @throws PluginInitializationException
     */
    @PluginConstructor
    public ProcessingTypeGenerateConfig(final PluginInstantiationContext config)
            throws PluginInitializationException {
        super(config.getPluginKey(), config.getPluginClassName());

        ProcessingTypeGenerateConfig.LOGGER.debug(this.getProcessingName()
                + ": creating");
        final Node node = config.getNode();
        final String path = XmlHelper.nodeAttributeValue(node,
                ProcessingTypeGenerateConfig.XML_ATT_PATH);
        final String fileName = XmlHelper.nodeAttributeValue(node,
                ProcessingTypeGenerateConfig.XML_ATT_FILE);
        ProcessingTypeGenerateConfig.LOGGER.debug("path=" + path
                + ", fileName=" + fileName);

        if (path == null || path.trim().equals("")) {
            throw new PluginInitializationException(this.getProcessingName()
                    + ": path must not be empty!!");
        }

        if (fileName == null || fileName.trim().equals("")) {
            throw new PluginInitializationException(this.getProcessingName()
                    + ": fileName must not be empty!!");
        }

        this.path = path;
        this.fileName = fileName;
    }

    /**
     * Transforms the current instance to its xml representation.
     * 
     * @param processingNode
     *                {@link Element} for current instance
     * 
     * @see PluginToXml
     * 
     */
    @PluginToXml
    public void toXml(final Element processingNode) {
        ProcessingTypeGenerateConfig.LOGGER.debug(this.getProcessingName()
                + ": writing to xml");
        processingNode.addAttribute(ProcessingTypeGenerateConfig.XML_ATT_PATH,
                this.path);
        processingNode.addAttribute(ProcessingTypeGenerateConfig.XML_ATT_FILE,
                this.fileName);
    }

    /**
     * @see de.groth.dts.api.core.dto.processings.IProcessing#process(de.groth.dts.api.core.dao.DynamicTemplateSystemExecutionContext)
     */
    public boolean process(final DynamicTemplateSystemExecutionContext context) {
        XMLWriter writer = null;
        try {
            ProcessingTypeGenerateConfig.LOGGER.debug(this.getProcessingName()
                    + ": processing");

            final IDynamicTemplateSystem dts = context.getDts();
            final String dtsFilePath = dts.getFilePath();

            ProcessingTypeGenerateConfig.LOGGER.debug(this.getProcessingName()
                    + ": reading " + dtsFilePath);
            final SAXReader reader = new SAXReader();
            final Document sourceDocument = reader.read(dtsFilePath);

            ProcessingTypeGenerateConfig.LOGGER.debug(this.getProcessingName()
                    + ": creating xmlHandler");
            final IDtsDtoXmlHandler<IDynamicTemplateSystem> handler = context
                    .getDts().createXmlHandler(context);

            ProcessingTypeGenerateConfig.LOGGER
                    .debug(this.getProcessingName()
                            + ": creating basic xml document and invoking xmlHandler.toXml()");
            final Document document = DocumentHelper.createDocument();
            final Element rootElement = document
                    .addElement("dynamicTemplateSystem");
            handler.toXml(dts, rootElement);

            ProcessingTypeGenerateConfig.LOGGER.debug(this.getProcessingName()
                    + ": preparing output");
            final OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding(sourceDocument.getXMLEncoding());

            final String exportDirPath = FileHelper.combinePath(context
                    .getBaseExportPath(), this.path);
            final String exportPath = FileHelper.combinePath(exportDirPath,
                    this.fileName);
            ProcessingTypeGenerateConfig.LOGGER.debug(this.getProcessingName()
                    + ": output will be written to " + exportPath);
            boolean dirsCreated = new File(exportDirPath).mkdirs();
            if (!dirsCreated) {
                ProcessingTypeGenerateConfig.LOGGER.debug(this
                        .getProcessingName()
                        + ": directories already existent");
            }

            ProcessingTypeGenerateConfig.LOGGER.info(this.getProcessingName()
                    + ": writing xml configuration to " + exportPath);
            writer = new XMLWriter(new FileWriter(exportPath), format);
            writer.write(document);
            writer.flush();

            ProcessingTypeGenerateConfig.LOGGER.debug(this.getProcessingName()
                    + ": processing completed");
            return true;
        } catch (final XmlException ex) {
            ProcessingTypeGenerateConfig.LOGGER.error(this.getProcessingName()
                    + ": caught XmlException!!", ex);
        } catch (final DocumentException ex) {
            ProcessingTypeGenerateConfig.LOGGER.error(this.getProcessingName()
                    + ": caught DocumentException!!", ex);
        } catch (final IOException ex) {
            ProcessingTypeGenerateConfig.LOGGER.error(this.getProcessingName()
                    + ": caught IOException!!", ex);
        } finally {
            if (writer != null) {
                ProcessingTypeGenerateConfig.LOGGER.debug(this
                        .getProcessingName()
                        + ": closing XmlWriter");
                try {
                    writer.close();
                } catch (final IOException ex) {
                    ProcessingTypeGenerateConfig.LOGGER.error(this
                            .getProcessingName()
                            + ": failed to close XmlWriter!!");
                }
            }
        }

        ProcessingTypeGenerateConfig.LOGGER.debug(this.getProcessingName()
                + ": processing failed");
        return false;
    }
}
