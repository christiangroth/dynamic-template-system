package de.groth.dts.api.core;

import java.io.File;
import java.util.Date;

import de.groth.dts.api.core.dto.IDynamicTemplateSystem;
import de.groth.dts.api.core.exception.convert.ConverterException;
import de.groth.dts.api.core.exception.convert.ConverterPreparationException;

/**
 * The Converter provides all access needed to work with an instance of
 * DynamicTemplateSystem. It defines methods to convert and export a page or
 * export all defined pages with included pre- and post-processing.
 * 
 * TODO document how security manager is set
 * 
 * @author Christian Groth
 * 
 */
public interface IConverter {
    /**
     * Returns the DynamicTemplateSystem which is created by constructor or
     * loadXml-method. The dts might be null if loading/parsing the xml failed.
     * In this case an exception was thrown!!
     * 
     * @see #loadXml()
     * 
     * @return current instance of {@link IDynamicTemplateSystem}
     */
    IDynamicTemplateSystem getDynamicTemplateSystem();

    /**
     * Returns the filename (without path) of configuration xml-file.
     * 
     * @return the file name
     */
    String getDtsFileName();

    /**
     * Returns the corresponding File to the configuration.
     * 
     * @see java.io.File
     * 
     * @return the dts file
     */
    File getDtsFile();

    /**
     * Returns the path without the filename.
     * 
     * @return the dts path
     */
    String getBaseDtsPath();

    /**
     * Returns the export path, which must be a directory.
     * 
     * @return the export path
     */
    String getBaseExportPath();

    /**
     * The Date on which the convert-method for given pageId was called the last
     * time.
     * 
     * @param pageId
     * 
     * @see #convert(String)
     * @see java.util.Date
     * 
     * @return the last convert call date
     */
    Date getLastConvertCallDate(String pageId);

    /**
     * The Date on which the export-method for given pageId was called the last
     * time.
     * 
     * @param pageId
     * 
     * @see #export(String)
     * @see java.util.Date
     * 
     * @return the last export call date
     */
    Date getLastExportCallDate(String pageId);

    /**
     * The Date on which the exportAll-method was called the last time.
     * 
     * @see #exportAll()
     * @see java.util.Date
     * 
     * @return the last export all call date
     */
    Date getLastExportAllCallDate();

    /**
     * Loads the configuration to initialize the dts. This method can be invoked
     * at any time to reload the dts from scratch.
     * 
     * @throws ConverterPreparationException
     *                 if loading of xml fails
     */
    void loadXml() throws ConverterPreparationException;

    /**
     * Convert a single page and returns the result as String. Null will be
     * returned in case the dts is null!
     * 
     * @param pageId
     *                id of the page to be converted
     * 
     * @return the result string
     * 
     * @throws ConverterException
     *                 in case instance of {@link IConverter} fails
     */
    String convert(String pageId) throws ConverterException;

    /**
     * Exports all pages and does pre/post-processing.
     * 
     * @see #export(String) for exporting preconditions
     * @see #convert(String) for exception causes
     * 
     * @throws ConverterException
     *                 in case instance of {@link IConverter} fails
     */
    void exportAll() throws ConverterException;

    /**
     * Exports a single page (no pre/post processing). Be sure that the page has
     * set the file-attribute, otherwise an ConverterPreparationException will
     * be thrown! The lastExportDate is only set if the Converter tries to
     * export the page.
     * 
     * @param pageId
     *                the page id
     * 
     * @throws ConverterException
     *                 in case instance of {@link IConverter} fails
     */
    void export(String pageId) throws ConverterException;

    /**
     * Tells if the {@link IConverter} is initialized properly.
     * 
     * @return true if initialized properly
     */
    boolean isInitialized();
}
