package de.groth.dts.plugins.generics;

import org.apache.log4j.Logger;

import de.groth.dts.api.core.dao.GenericContext;
import de.groth.dts.api.core.dao.PluginInstantiationContext;
import de.groth.dts.api.core.dto.plugins.PluginConstructor;
import de.groth.dts.api.core.dto.plugins.generics.AbstractGeneric;
import de.groth.dts.api.core.exception.DynamicTemplateSystemException;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;
import de.groth.dts.api.core.exception.plugins.generics.GenericValueException;
import de.groth.dts.api.core.util.FileHelper;
import de.groth.dts.plugins.util.PropertyReader;

/**
 * Reads a property-file and resolves the value for the given key.
 * ${...:properties<pathToPropertyFile>,key=<propertyKey>} The path to
 * properties file is relative to baseDtsPath.
 * 
 * @author Christian Groth
 */
public class GenericTypePropertyReader extends AbstractGeneric {
    private static final Logger LOGGER = Logger
            .getLogger(GenericTypePropertyReader.class);

    private static final String PARAMETER_PROPERTIES_FILE = "properties";
    private static final String PARAMETER_PROPERTIES_KEY = "key";

    /**
     * Creates a new instance (annotated as {@link PluginConstructor})
     * 
     * @param config
     *                current {@link PluginInstantiationContext}
     * @throws PluginInitializationException
     */
    @PluginConstructor
    public GenericTypePropertyReader(final PluginInstantiationContext config)
            throws PluginInitializationException {
        super(config.getPluginKey(), config.getPluginClassName());
    }

    /**
     * @see de.groth.dts.api.core.dto.generics.IGeneric#getValue(de.groth.dts.api.core.dao.GenericContext)
     */
    public String getValue(final GenericContext context)
            throws GenericValueException {
        final String propertiesFile = context
                .getGenericAttribute(GenericTypePropertyReader.PARAMETER_PROPERTIES_FILE);
        final String propertiesKey = context
                .getGenericAttribute(GenericTypePropertyReader.PARAMETER_PROPERTIES_KEY);
        final String basePath = context.getBaseDtsPath();
        GenericTypePropertyReader.LOGGER.debug(this.getGenericName()
                + ": propertiesFile=" + propertiesFile + ", propertiesKey="
                + propertiesKey);

        try {
            GenericTypePropertyReader.LOGGER.debug(this.getGenericName()
                    + ": invoking PropertyReader");
            return new PropertyReader().getValue(FileHelper.combinePath(
                    basePath, propertiesFile), propertiesKey);
        } catch (final DynamicTemplateSystemException ex) {
            throw new GenericValueException(
                    "Unable to invoke PropertyReader!!", ex);
        }
    }
}
