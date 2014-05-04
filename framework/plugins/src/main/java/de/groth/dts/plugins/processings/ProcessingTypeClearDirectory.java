package de.groth.dts.plugins.processings;

import java.io.File;

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
 * Clears the referenced directory (all content will be deleted, but not the
 * directory itself). The path to the directory is relative to baseExportPath.
 * 
 * <pre>
 *      &lt;processing type=&quot;...&quot; path=&quot;&lt;pathToDirectory&gt;&quot; /&gt;
 * </pre>
 * 
 * @author Christian Groth
 */
public class ProcessingTypeClearDirectory extends AbstractProcessing {
    private static final Logger LOGGER = Logger
            .getLogger(ProcessingTypeClearDirectory.class);

    private static final String XML_ATT_PATH = "path";

    private final String path;

    /**
     * Creates a new instance (annotated as {@link PluginConstructor})
     * 
     * @param config
     *                current {@link PluginInstantiationContext}
     * @throws PluginInitializationException
     */
    @PluginConstructor
    public ProcessingTypeClearDirectory(final PluginInstantiationContext config)
            throws PluginInitializationException {
        super(config.getPluginKey(), config.getPluginClassName());

        ProcessingTypeClearDirectory.LOGGER.debug(this.getProcessingName()
                + ": creating");
        final Node node = config.getNode();
        final String path = XmlHelper.nodeAttributeValue(node,
                ProcessingTypeClearDirectory.XML_ATT_PATH);
        ProcessingTypeClearDirectory.LOGGER.debug("path=" + path);

        if (path == null || path.trim().equals("")) {
            throw new PluginInitializationException(this.getProcessingName()
                    + ": path must not be empty!!");
        }

        this.path = path;
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
        ProcessingTypeClearDirectory.LOGGER.debug(this.getProcessingName()
                + ": writing to xml");
        processingNode.addAttribute(ProcessingTypeClearDirectory.XML_ATT_PATH,
                this.path);
    }

    /**
     * @see de.groth.dts.api.core.dto.processings.IProcessing#process(de.groth.dts.api.core.dao.DynamicTemplateSystemExecutionContext)
     */
    public boolean process(final DynamicTemplateSystemExecutionContext context) {
        ProcessingTypeClearDirectory.LOGGER.debug(this.getProcessingName()
                + ": processing " + this.path);

        final File file = new File(FileHelper.combinePath(context
                .getBaseExportPath(), this.path));

        ProcessingTypeClearDirectory.LOGGER.debug(this.getProcessingName()
                + ": checking directory");
        if (!file.exists()) {
            ProcessingTypeClearDirectory.LOGGER.warn(this.getProcessingName()
                    + ": directory does not exist and can't be cleared: "
                    + file.getAbsolutePath() + "!!");
            return false;
        }

        if (!file.isDirectory()) {
            ProcessingTypeClearDirectory.LOGGER.error(this.getProcessingName()
                    + ": this is no directory: " + file.getAbsolutePath()
                    + "!!");
            return false;
        }

        ProcessingTypeClearDirectory.LOGGER.debug(this.getProcessingName()
                + ": clearing directory " + file.getAbsolutePath());
        return this.processInternal(file, true);
    }

    private boolean processInternal(final File dir, final boolean initial) {
        ProcessingTypeClearDirectory.LOGGER.debug(this.getProcessingName()
                + ": processing " + dir.getAbsolutePath());
        boolean success = true;

        if (dir.isDirectory()) {
            final String[] children = dir.list();
            ProcessingTypeClearDirectory.LOGGER.debug(this.getProcessingName()
                    + ": calling for child elements");
            for (int i = 0; i < children.length; i++) {
                success = success
                        && this.processInternal(new File(dir, children[i]),
                                false);
            }
        }

        if (!initial) {
            ProcessingTypeClearDirectory.LOGGER.debug(this.getProcessingName()
                    + ": deleting " + dir.getAbsolutePath());
            success = success && dir.delete();
        }

        ProcessingTypeClearDirectory.LOGGER.debug(this.getProcessingName()
                + ": status " + (success ? "okay" : "fail"));
        return success;
    }
}
