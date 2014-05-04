package de.groth.dts.plugins.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import de.groth.dts.plugins.exception.PropertyReaderException;

/**
 * Helper class for reading properties files.
 * 
 * @author Christian Groth
 * 
 */
public class PropertyReader {
    private static final Logger LOGGER = Logger.getLogger(PropertyReader.class);

    /**
     * Retrieves the property value from given properties file with the given
     * property key.
     * 
     * @param propertiesFile
     *                path to properties file
     * @param propertiesKey
     *                the property key
     * @return the property value
     * @throws PropertyReaderException
     *                 if the file is not readable
     */
    public String getValue(final String propertiesFile,
            final String propertiesKey) throws PropertyReaderException {
        PropertyReader.LOGGER
                .debug("checking propertiesFile " + propertiesFile);
        if (propertiesFile == null || propertiesFile.trim().equals("")) {
            throw new PropertyReaderException(
                    "PropertyReader: parameter 'properties' must be set!!");
        }

        PropertyReader.LOGGER.debug("checking propertiesKey " + propertiesKey);
        if (propertiesKey == null || propertiesKey.trim().equals("")) {
            throw new PropertyReaderException(
                    "PropertyReader: parameter 'key' must be set!!");
        }

        PropertyReader.LOGGER.debug("creating Properties");
        FileInputStream fileInputStream = null;
        try {
            final Properties properties = new Properties();
            fileInputStream = new FileInputStream(propertiesFile);
            PropertyReader.LOGGER.debug("loading file " + propertiesFile);
            properties.load(fileInputStream);

            PropertyReader.LOGGER.debug("returning value for key "
                    + propertiesKey);
            return properties.getProperty(propertiesKey);
        } catch (final FileNotFoundException ex) {
            throw new PropertyReaderException(
                    "PropertyReader: Unable to load properties-file ("
                            + propertiesFile
                            + "), caught FileNotFoundException!!", ex);
        } catch (final IOException ex) {
            throw new PropertyReaderException(
                    "PropertyReader: Unable to load properties-file ("
                            + propertiesFile + "), caught IOException!!", ex);
        } finally {
            if (fileInputStream != null) {
                try {
                    PropertyReader.LOGGER.debug("closing FileInputStream for "
                            + propertiesFile);
                    fileInputStream.close();
                } catch (final IOException ex) {
                    throw new PropertyReaderException(
                            "PropertyReader: Failed to close FileInputStream!!",
                            ex);
                }
            }
        }
    }
}
