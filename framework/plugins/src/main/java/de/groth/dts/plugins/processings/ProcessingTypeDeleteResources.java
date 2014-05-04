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
 * Deletes the references file or directory. The path is relative to
 * baseExportPath.
 * 
 * <pre>
 *      &lt;processing type=&quot;...&quot; path=&quot;&lt;fileOrDirectory&gt;&quot; /&gt;
 * </pre>
 * 
 * @author Christian Groth
 */
public class ProcessingTypeDeleteResources extends AbstractProcessing {
    private static final Logger LOGGER = Logger
            .getLogger(ProcessingTypeDeleteResources.class);

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
    public ProcessingTypeDeleteResources(final PluginInstantiationContext config)
            throws PluginInitializationException {
        super(config.getPluginKey(), config.getPluginClassName());

        ProcessingTypeDeleteResources.LOGGER.debug(this.getProcessingName()
                + ": creating");
        final Node node = config.getNode();
        final String path = XmlHelper.nodeAttributeValue(node,
                ProcessingTypeDeleteResources.XML_ATT_PATH);
        ProcessingTypeDeleteResources.LOGGER.debug("path=" + path);

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
        ProcessingTypeDeleteResources.LOGGER.debug(this.getProcessingName()
                + ": writing to xml");
        processingNode.addAttribute(ProcessingTypeDeleteResources.XML_ATT_PATH,
                this.path);
    }

    /**
     * @see de.groth.dts.api.core.dto.processings.IProcessing#process(de.groth.dts.api.core.dao.DynamicTemplateSystemExecutionContext)
     */

    public boolean process(final DynamicTemplateSystemExecutionContext context) {
        ProcessingTypeDeleteResources.LOGGER.debug(this.getProcessingName()
                + ": processing " + this.path);
        final File ressource = new File(FileHelper.combinePath(context
                .getBaseExportPath(), this.path));

        ProcessingTypeDeleteResources.LOGGER.debug(this.getProcessingName()
                + ": checking ressource");
        if (!ressource.exists()) {
            ProcessingTypeDeleteResources.LOGGER.error(this.getProcessingName()
                    + ": error while deleting non existent file/dir: "
                    + ressource.getAbsolutePath() + "!!");
            ProcessingTypeDeleteResources.LOGGER.debug(this.getProcessingName()
                    + ": failed");
            return false;
        } // if

        ProcessingTypeDeleteResources.LOGGER.info(this.getProcessingName()
                + ": deleting " + ressource.getAbsolutePath());
        return this.processInternal(ressource);
    }

    private boolean processInternal(final File dir) {
        boolean success = true;

        if (dir.isDirectory()) {
            ProcessingTypeDeleteResources.LOGGER.debug(this.getProcessingName()
                    + ": is directory, deleting children");
            final String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                success = success
                        && this.processInternal(new File(dir, children[i]));
            }
        }

        ProcessingTypeDeleteResources.LOGGER.debug(this.getProcessingName()
                + ": deleting " + dir.getAbsolutePath());
        success = success && dir.delete();
        ProcessingTypeDeleteResources.LOGGER.debug(this.getProcessingName()
                + ": status " + (success ? "okay" : "fail"));
        return success;
    }
}
