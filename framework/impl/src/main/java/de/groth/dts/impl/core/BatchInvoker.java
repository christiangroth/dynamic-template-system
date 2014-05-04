package de.groth.dts.impl.core;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import de.groth.dts.api.core.IConverter;
import de.groth.dts.api.core.IInvoker;
import de.groth.dts.api.core.exception.convert.ConverterException;
import de.groth.dts.api.core.exception.invoker.InvokerInitializationException;

/**
 * The {@link BatchInvoker} is designed to be invoked from command-line. It is
 * only capable of exporting all pages invokes exporting all pages. Additionally
 * the log4j system will be initialized, because the {@link BatchInvoker} is
 * meant to be run from an unmanaged environment. Be sure to have a file named
 * &quot;log4j.xml&quot; in current directory.
 * 
 * @author Christian Groth
 */
public final class BatchInvoker implements IInvoker {
    /*
     * initialize log4J
     */
    private static final String LOG4J_FILE = "log4j.xml";
    private static final int LOG4J_REFRESH = 120000;

    static {
        DOMConfigurator.configureAndWatch(BatchInvoker.LOG4J_FILE,
                BatchInvoker.LOG4J_REFRESH);
    }

    private static final Logger LOGGER = Logger.getLogger(BatchInvoker.class);
    private static final int EXPECTED_ARG_COUNT = 3;

    /**
     * Use he following parameters on the command-line:
     * 
     * <ul>
     * <li>basePath (absolute or relative to current)</li>
     * <li>dtsFile (relative to basePath)</li>
     * <li>exportPath (absolute or relative to current)</li>
     * </ul>
     * 
     * @param args
     *                the arguments
     */
    public static void main(final String[] args) {
        if (args.length != BatchInvoker.EXPECTED_ARG_COUNT) {
            BatchInvoker.usage("number of arguments is " + args.length);
        }

        String basePath = args[0].trim();
        final String dtsFile = args[1].trim();
        final String exportPath = args[2].trim();

        BatchInvoker.LOGGER.debug("basePath=" + basePath + ", dtsFile="
                + dtsFile + ", exportPath=" + exportPath);
        if (basePath == null || basePath.equals("")) {
            BatchInvoker.LOGGER.debug("setting basePath to '.'");
            basePath = ".";
        }

        if (basePath.endsWith("\\") || basePath.endsWith("/")) {
            basePath = basePath.substring(0, basePath.length() - 1);
            BatchInvoker.LOGGER.debug("stripped basePath: " + basePath);
        }

        final File basePathFile = new File(basePath);
        final File dtsFileFile = new File(basePath
                + System.getProperty("file.separator") + dtsFile);
        BatchInvoker.LOGGER.debug("checking directoried and paths");

        if (!basePathFile.isDirectory() || !basePathFile.canRead()) {
            BatchInvoker.usage("basePath not existent or readable: "
                    + basePathFile.getPath() + ", "
                    + basePathFile.getAbsolutePath());
        }

        if (!dtsFileFile.isFile() || !dtsFileFile.canRead()) {
            BatchInvoker.usage("dtsFile not existent or readable: "
                    + dtsFileFile.getPath() + ", "
                    + dtsFileFile.getAbsolutePath());
        }

        try {
            BatchInvoker.LOGGER
                    .debug("paths seem to be okay, invoking exportAll");
            final BatchInvoker batchInvoker = new BatchInvoker(dtsFileFile,
                    exportPath);
            batchInvoker.initialize();
            if (batchInvoker.isInitialized()) {
                batchInvoker.exportAll();
            }
        } catch (final InvokerInitializationException ex) {
            throw new IllegalStateException("call to BatchInvoker failed!!", ex);
        } catch (final ConverterException ex) {
            throw new IllegalStateException("call to BatchInvoker failed!!", ex);
        }
    }

    private static void usage(final String error) {
        BatchInvoker.LOGGER.error("call me with three arguments:");
        BatchInvoker.LOGGER
                .error(" - basePath (absolute or relative to current)");
        BatchInvoker.LOGGER.error(" - dtsFile (relative to basePath");
        BatchInvoker.LOGGER
                .error(" - exportPath (absolute or relative to current)");
        BatchInvoker.LOGGER
                .error("be sure that basePath exists. if basePath is current directory then set '.'");

        if (error != null) {
            BatchInvoker.LOGGER.error("Error: " + error);
        }

        throw new IllegalStateException(
                "Unable to initialize BatchInvoker. See output on stderr for details!!");
    }

    private final File dtsFile;
    private final String baseExportPath;
    private IConverter converter;

    /**
     * Creates a new instance
     * 
     * @param dtsFile
     *                dts file
     * @param baseExportPath
     *                base dts export path
     */
    public BatchInvoker(final File dtsFile, final String baseExportPath) {
        this.dtsFile = dtsFile;
        this.baseExportPath = baseExportPath;
    }

    /**
     * @see de.groth.dts.api.core.IInvoker#initialize()
     */
    public void initialize() throws InvokerInitializationException {
        try {
            BatchInvoker.LOGGER.debug("initializing ConverterImpl with "
                    + this.dtsFile.getAbsolutePath() + " and "
                    + this.baseExportPath);
            this.converter = new ConverterImpl(this.dtsFile,
                    this.baseExportPath);
        } catch (final ConverterException ex) {
            throw new InvokerInitializationException(
                    "failed to create IConverter!!", ex);
        }
    }

    /**
     * @see de.groth.dts.api.core.IInvoker#isInitialized()
     */
    public boolean isInitialized() {
        return this.converter != null && this.converter.isInitialized();
    }

    /**
     * Will throw an {@link UnsupportedOperationException} because this method
     * is not supported for {@link BatchInvoker}.
     * 
     * @see de.groth.dts.api.core.IInvoker#convert(String)
     * 
     * @param pageId
     *                the page id
     * 
     * @throws ConverterException
     * 
     */
    public String convert(final String pageId) throws ConverterException {
        throw new UnsupportedOperationException(
                "the method convert(String) is not supported by BatchInvoker!!");
    }

    /**
     * Will throw an {@link UnsupportedOperationException} because this method
     * is not supported for {@link BatchInvoker}.
     * 
     * @see de.groth.dts.api.core.IInvoker#export(String)
     * 
     * @param pageId
     *                the page id
     * 
     * @throws ConverterException
     * 
     */
    public void export(final String pageId) throws ConverterException {
        throw new UnsupportedOperationException(
                "the method export(String) is not supported by BatchInvoker!!");
    }

    /**
     * @see de.groth.dts.api.core.IInvoker#exportAll()
     */
    public void exportAll() throws ConverterException {
        BatchInvoker.LOGGER.debug("invoking exportAll on ConverterImpl");
        this.converter.exportAll();
    }

    /**
     * @see de.groth.dts.api.core.IInvoker#supportsConvert()
     */
    public boolean supportsConvert() {
        return false;
    }

    /**
     * @see de.groth.dts.api.core.IInvoker#supportsExport()
     */
    public boolean supportsExport() {
        return false;
    }

    /**
     * @see de.groth.dts.api.core.IInvoker#supportsExportAll()
     */
    public boolean supportsExportAll() {
        return true;
    }
}
