package de.groth.dts.api.core;

import de.groth.dts.api.core.exception.convert.ConverterException;
import de.groth.dts.api.core.exception.invoker.InvokerInitializationException;

/**
 * An instance of {@link IInvoker} deals with an instance of {@link IConverter}
 * and is the interface to the user. So an invoker is something the application
 * starts with (if it has a main method) or in other case the 'main class' in an
 * application.
 * 
 * @author Christian Groth
 * 
 */
public interface IInvoker {
    /**
     * Initializes the {@link IInvoker} and throws an
     * {@link InvokerInitializationException} if initialization fails.
     * 
     * @throws InvokerInitializationException
     *                 if initialization fails
     */
    void initialize() throws InvokerInitializationException;

    /**
     * Tells if the {@link IInvoker} is initialized properly.
     * 
     * @return true is initialized and ready to use
     */
    boolean isInitialized();

    /**
     * Tells if the {@link #convert(String)} method is supported.
     * 
     * @return true if supported
     */
    boolean supportsConvert();

    /**
     * Invokes {@link IConverter#convert(String)}
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
     * Tells if the {@link #export(String)} method is supported
     * 
     * @return true if supported
     */
    boolean supportsExport();

    /**
     * Invokes {@link IConverter#export(String)}
     * 
     * @param pageId
     *                the page id
     * 
     * @throws ConverterException
     *                 in case instance of {@link IConverter} fails
     */
    void export(String pageId) throws ConverterException;

    /**
     * Tells if the {@link #exportAll()} method is supported
     * 
     * @return true if supported
     */
    boolean supportsExportAll();

    /**
     * Invokes {@link IConverter#exportAll()}
     * 
     * @throws ConverterException
     *                 in case instance of {@link IConverter} fails
     */
    void exportAll() throws ConverterException;
}
