package de.groth.dts.plugins.generics;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import de.groth.dts.api.core.dao.GenericContext;
import de.groth.dts.api.core.dao.PluginInstantiationContext;
import de.groth.dts.api.core.dto.plugins.PluginConstructor;
import de.groth.dts.api.core.dto.plugins.generics.AbstractGeneric;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;
import de.groth.dts.api.core.exception.plugins.generics.GenericValueException;
import de.groth.dts.api.core.util.FileHelper;

/**
 * Includes the content of the referenced file: ${...:file=<pathToFile>}. The
 * file path is relative to baseDtsPath.
 * 
 * @author Christian Groth
 * 
 */
public class GenericTypeFileContent extends AbstractGeneric {
    private static final Logger LOGGER = Logger
            .getLogger(GenericTypeFileContent.class);

    private static final String PARAMETER_FILE = "file";

    /**
     * Creates a new instance (annotated as {@link PluginConstructor})
     * 
     * @param config
     *                current {@link PluginInstantiationContext}
     * @throws PluginInitializationException
     */
    @PluginConstructor
    public GenericTypeFileContent(final PluginInstantiationContext config)
            throws PluginInitializationException {
        super(config.getPluginKey(), config.getPluginClassName());
    }

    /**
     * @see de.groth.dts.api.core.dto.generics.IGeneric#getValue(de.groth.dts.api.core.dao.GenericContext)
     */
    public String getValue(final GenericContext context)
            throws GenericValueException {
        final String file = context
                .getGenericAttribute(GenericTypeFileContent.PARAMETER_FILE);
        GenericTypeFileContent.LOGGER.debug(this.getGenericName()
                + ": retrieving fileContent for " + file);
        if (file == null || file.trim().equals("")) {
            throw new GenericValueException(
                    this.getGenericName()
                            + ": unable to include content: file must not be null or empty!!");
        }

        final String filePath = FileHelper.combinePath(
                context.getBaseDtsPath(), file);
        final File testFile = new File(filePath);
        GenericTypeFileContent.LOGGER.debug(this.getGenericName()
                + ": filePath is " + testFile.getAbsolutePath());
        if (testFile == null || !testFile.exists() || !testFile.isFile()
                || !testFile.canRead()) {
            throw new GenericValueException(this.getGenericName()
                    + ": unable to read file " + filePath + "!!");
        }

        try {
            GenericTypeFileContent.LOGGER.debug(this.getGenericName()
                    + ": invoking FileHelper");
            return FileHelper.getFileContent(filePath);
        } catch (final IOException ex) {
            throw new GenericValueException(this.getGenericName()
                    + ": call to FileHelper.getFileContent failed!!", ex);
        }
    }
}
