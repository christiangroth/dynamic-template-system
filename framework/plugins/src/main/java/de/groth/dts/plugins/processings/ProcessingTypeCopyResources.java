package de.groth.dts.plugins.processings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.dom4j.Node;

import de.groth.dts.api.core.dao.DynamicTemplateSystemExecutionContext;
import de.groth.dts.api.core.dao.PluginInstantiationContext;
import de.groth.dts.api.core.dto.plugins.PluginConstructor;
import de.groth.dts.api.core.dto.plugins.PluginToXml;
import de.groth.dts.api.core.dto.plugins.processings.AbstractProcessing;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;
import de.groth.dts.api.core.util.FileHelper;
import de.groth.dts.api.xml.util.XmlHelper;

/**
 * Copies the referenced file or directory from the from attribute to to
 * attribute. The path to the from attribute is relative to baseDtsPath but the
 * path to the to attribute is relative to baseExportPath.
 * 
 * <pre>
 *      &lt;processing type=&quot;...&quot; from=&quot;&lt;source&gt;&quot; to=&quot;&lt;destination&gt;&quot; /&gt;
 * </pre>
 * 
 * @author Christian Groth
 */
public class ProcessingTypeCopyResources extends AbstractProcessing {
    private static final Logger LOGGER = Logger
            .getLogger(ProcessingTypeCopyResources.class);

    private static final String XML_ATT_PATH_FROM = "from";
    private static final String XML_ATT_PATH_TO = "to";

    private final String pathFrom;
    private final String pathTo;

    /**
     * Creates a new instance (annotated as {@link PluginConstructor})
     * 
     * @param config
     *                current {@link PluginInstantiationContext}
     * @throws PluginInitializationException
     */
    @PluginConstructor
    public ProcessingTypeCopyResources(final PluginInstantiationContext config)
            throws PluginInitializationException {
        super(config.getPluginKey(), config.getPluginClassName());

        ProcessingTypeCopyResources.LOGGER.debug(this.getProcessingName()
                + ": creating");
        final Node node = config.getNode();
        final String pathFrom = XmlHelper.nodeAttributeValue(node,
                ProcessingTypeCopyResources.XML_ATT_PATH_FROM);
        final String pathTo = XmlHelper.nodeAttributeValue(node,
                ProcessingTypeCopyResources.XML_ATT_PATH_TO);
        ProcessingTypeCopyResources.LOGGER.debug("pathFrom=" + pathFrom
                + ", pathTo=" + pathTo);

        if (pathFrom == null || pathFrom.trim().equals("")) {
            throw new PluginInitializationException(this.getProcessingName()
                    + ": pathFrom must not be empty!!");
        }

        if (pathTo == null || pathTo.trim().equals("")) {
            throw new PluginInitializationException(this.getProcessingName()
                    + ": pathTo must not be empty!!");
        }

        this.pathFrom = pathFrom;
        this.pathTo = pathTo;
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
        ProcessingTypeCopyResources.LOGGER.debug(this.getProcessingName()
                + ": writing to xml");
        processingNode.addAttribute(
                ProcessingTypeCopyResources.XML_ATT_PATH_FROM, this.pathFrom);
        processingNode.addAttribute(
                ProcessingTypeCopyResources.XML_ATT_PATH_TO, this.pathTo);
    }

    /**
     * @see de.groth.dts.api.core.dto.processings.IProcessing#process(de.groth.dts.api.core.dao.DynamicTemplateSystemExecutionContext)
     */

    public boolean process(final DynamicTemplateSystemExecutionContext context) {
        ProcessingTypeCopyResources.LOGGER.debug(this.getProcessingName()
                + ": processing from " + this.pathFrom + " to " + this.pathTo);
        final File from = new File(FileHelper.combinePath(context
                .getBaseDtsPath(), this.pathFrom));
        final File to = new File(FileHelper.combinePath(context
                .getBaseExportPath(), this.pathTo));

        ProcessingTypeCopyResources.LOGGER.info(this.getProcessingName()
                + ": copying from " + from.getPath() + " to " + to.getPath());
        return this.processInternal(from, to);
    }

    private boolean processInternal(final File from, final File to) {
        if (from.isDirectory()) {
            ProcessingTypeCopyResources.LOGGER.debug(this.getProcessingName()
                    + ": from is a directory");
            if (!from.exists()) {
                ProcessingTypeCopyResources.LOGGER.error(this
                        .getProcessingName()
                        + ": pathFrom does not exist: "
                        + from.getAbsolutePath() + "!!");
                return false;
            }

            ProcessingTypeCopyResources.LOGGER.debug(this.getProcessingName()
                    + ": processing children");
            final String[] files = from.list();
            boolean result = true;
            for (int i = 0; i < files.length; i++) {
                result = result
                        && this.processInternal(new File(from, files[i]),
                                new File(to, files[i]));
            }

            return result;
        } else {
            FileInputStream fis = null;
            FileOutputStream fos = null;

            try {
                ProcessingTypeCopyResources.LOGGER.debug(this
                        .getProcessingName()
                        + ": from is a file");
                if (!from.exists()) {
                    ProcessingTypeCopyResources.LOGGER.error(this
                            .getProcessingName()
                            + ": pathFrom does not exist: "
                            + from.getAbsolutePath() + "!!");
                    return false;
                }

                final File toParent = to.getParentFile();
                if (!toParent.exists()) {
                    ProcessingTypeCopyResources.LOGGER.debug(this
                            .getProcessingName()
                            + ": creating destination dirs");
                    boolean dirsCreated = toParent.mkdirs();
                    if (!dirsCreated) {
                        ProcessingTypeCopyResources.LOGGER
                                .debug("directories already existent  ...");
                    }
                }

                fis = new FileInputStream(from);
                fos = new FileOutputStream(to);

                final int bufferSize = 1024;
                final byte[] buf = new byte[bufferSize];
                int i = 0;

                ProcessingTypeCopyResources.LOGGER.debug(this
                        .getProcessingName()
                        + ": copy data");
                while ((i = fis.read(buf)) != -1) {
                    fos.write(buf, 0, i);
                }
            } catch (final IOException ex) {
                ProcessingTypeCopyResources.LOGGER.error(this
                        .getProcessingName()
                        + ": caught IOException!!", ex);
                return false;
            } finally {
                if (fis != null) {
                    try {
                        ProcessingTypeCopyResources.LOGGER.debug(this
                                .getProcessingName()
                                + ": closing FileInputStream");
                        fis.close();
                    } catch (final IOException ex) {
                        ProcessingTypeCopyResources.LOGGER.error(this
                                .getProcessingName()
                                + ": failed to close FileInputStream!!", ex);
                    }
                }

                if (fos != null) {
                    try {
                        ProcessingTypeCopyResources.LOGGER.debug(this
                                .getProcessingName()
                                + ": closing FileOutputStream");
                        fos.close();
                    } catch (final IOException ex) {
                        ProcessingTypeCopyResources.LOGGER.error(this
                                .getProcessingName()
                                + ": failed to close FileOutputStream!!", ex);
                    }
                }
            }

            return true;
        }
    }
}
