package de.groth.dts.impl.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import de.groth.dts.api.core.IConverter;
import de.groth.dts.api.core.dao.DynamicTemplateSystemExecutionContext;
import de.groth.dts.api.core.dao.GenericContext;
import de.groth.dts.api.core.dao.ParameterContext;
import de.groth.dts.api.core.dto.IDynamicTemplateSystem;
import de.groth.dts.api.core.dto.IDynamicTemplateSystemBase;
import de.groth.dts.api.core.dto.IPage;
import de.groth.dts.api.core.dto.ITheme;
import de.groth.dts.api.core.dto.Replaceable;
import de.groth.dts.api.core.dto.parameters.ILazyInitializationCapable;
import de.groth.dts.api.core.dto.parameters.IParameter;
import de.groth.dts.api.core.dto.processings.IProcessing;
import de.groth.dts.api.core.exception.convert.ConverterException;
import de.groth.dts.api.core.exception.convert.ConverterPreparationException;
import de.groth.dts.api.core.exception.convert.PageNotExportableException;
import de.groth.dts.api.core.exception.dto.DtoInitializationException;
import de.groth.dts.api.core.exception.plugins.LazyInitializationException;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;
import de.groth.dts.api.core.exception.plugins.generics.GenericValueException;
import de.groth.dts.api.core.exception.plugins.parameters.ParameterValueException;
import de.groth.dts.api.core.util.DateHelper;
import de.groth.dts.api.core.util.GenericsHelper;
import de.groth.dts.api.core.util.IThemeHelper;
import de.groth.dts.api.core.util.LazyInitializationHelper;
import de.groth.dts.impl.core.dto.DynamicTemplateSystem;
import de.groth.dts.impl.core.security.DTSSecurityManager;
import de.groth.dts.impl.xml.DynamicTemplateSystemBaseXmlHandler;
import de.groth.dts.impl.xml.DynamicTemplateSystemXmlHandler;

/**
 * This is standard implementation of IConverter. Loading and parsing the
 * xml-document occurs automatically during constructor-call.
 * 
 * @author Christian Groth
 */
public class ConverterImpl implements IConverter {
    private static final Logger LOGGER = Logger.getLogger(ConverterImpl.class);

    private final File dtsFile;
    private final String dtsFileName;
    private final String baseDtsPath;

    private final String baseExportPath;

    private DynamicTemplateSystem dynamicTemplateSystem;

    private final Map<String, Date> lastConvertCallDateMap;
    private final Map<String, Date> lastExportCallDateMap;
    private Date lastExportAllCallDate;

    /**
     * Instantiates a new converter and reads the given configuration file to
     * initialize the dts. So a call to {@link #loadXml()} is not necessary
     * after constructor call.
     * 
     * @see #loadXml()
     * 
     * @param dtsConfigFile
     *                the dts configuration xml-file
     * @param exportBasePath
     *                the export base path (must be a directory)
     * @throws ConverterPreparationException
     *                 if loading of xml fails
     * 
     */
    public ConverterImpl(final File dtsConfigFile, final String exportBasePath)
            throws ConverterPreparationException {
        this.dtsFile = dtsConfigFile;
        this.dtsFileName = this.dtsFile.getName();
        this.baseDtsPath = dtsConfigFile.getParent();
        this.baseExportPath = exportBasePath;

        this.lastConvertCallDateMap = new HashMap<String, Date>();
        this.lastExportCallDateMap = new HashMap<String, Date>();

        ConverterImpl.LOGGER.info("initialized: " + this.baseDtsPath + ", "
                + this.baseExportPath + ", " + this.dtsFile.getPath());

        // TODO needs to be enabled/disabled somehow, also use mayDoXyz in IF?
        System.setSecurityManager(new DTSSecurityManager(this.baseDtsPath,
                this.baseExportPath));
        this.loadXml();
    }

    /**
     * @see de.groth.dts.api.core.IConverter#loadXml()
     */
    public void loadXml() throws ConverterPreparationException {
        ConverterImpl.LOGGER.debug("loading xml");
        try {
            this.dynamicTemplateSystem = null;

            ConverterImpl.LOGGER.debug("creating Parser");
            final SAXReader reader = new SAXReader();
            final Document document = reader.read(this.dtsFile);

            ConverterImpl.LOGGER.info("reading file "
                    + this.dtsFile.getAbsolutePath());
            final IDynamicTemplateSystemBase dtsBase = new DynamicTemplateSystemBaseXmlHandler(
                    this.dtsFile.getPath(), this).fromXml(document
                    .getRootElement());
            ConverterImpl.LOGGER
                    .debug("created DynamicTemplateSystemBase invoking xmlHandler for DynamicTemplateSystem");
            this.dynamicTemplateSystem = new DynamicTemplateSystemXmlHandler(
                    dtsBase, this.baseDtsPath, this.baseExportPath)
                    .fromXml(document.getRootElement());
            ConverterImpl.LOGGER.info("created DynamicTemplateSystem");
        } catch (final DocumentException ex) {
            throw new ConverterPreparationException("invalid xml!!", ex);
        } catch (final DtoInitializationException ex) {
            throw new ConverterPreparationException("invalid xml!!", ex);
        }
    }

    /**
     * @see de.groth.dts.api.core.IConverter#isInitialized()
     */
    public boolean isInitialized() {
        return this.dynamicTemplateSystem != null;
    }

    /**
     * @see de.groth.dts.api.core.IConverter#getDynamicTemplateSystem()
     */
    public IDynamicTemplateSystem getDynamicTemplateSystem() {
        return this.dynamicTemplateSystem;
    }

    /**
     * @see de.groth.dts.api.core.IConverter#getDtsFileName()
     */
    public String getDtsFileName() {
        return this.dtsFileName;
    }

    /**
     * @see de.groth.dts.api.core.IConverter#getDtsFile()
     */
    public File getDtsFile() {
        return this.dtsFile;
    }

    /**
     * @see de.groth.dts.api.core.IConverter#getBaseDtsPath()
     */
    public String getBaseDtsPath() {
        return this.baseDtsPath;
    }

    /**
     * @see de.groth.dts.api.core.IConverter#getBaseExportPath()
     */
    public String getBaseExportPath() {
        return this.baseExportPath;
    }

    /**
     * @see de.groth.dts.api.core.IConverter#getLastConvertCallDate(java.lang.String)
     */
    public Date getLastConvertCallDate(final String pageId) {
        return this.lastConvertCallDateMap.get(pageId);
    }

    /**
     * @see de.groth.dts.api.core.IConverter#getLastExportCallDate(java.lang.String)
     */
    public Date getLastExportCallDate(final String pageId) {
        return this.lastExportCallDateMap.get(pageId);
    }

    /**
     * @see de.groth.dts.api.core.IConverter#getLastExportAllCallDate()
     */
    public Date getLastExportAllCallDate() {
        return new Date(this.lastExportAllCallDate.getTime());
    }

    /**
     * @see de.groth.dts.api.core.IConverter#convert(java.lang.String)
     */
    public String convert(final String pageId) throws ConverterException {
        ConverterImpl.LOGGER.info("converting " + pageId);
        this.lastConvertCallDateMap.put(pageId, DateHelper.now());

        if (this.dynamicTemplateSystem != null) {
            try {
                // get page, theme and template
                final IPage page = this.getPageById(pageId);
                final ITheme theme = page.getTheme();
                final HashMap<String, IParameter> parameterMap = this
                        .buildParameterSet(page);
                String templateContent = IThemeHelper
                        .getTemplateContentForTheme(theme, this
                                .getDynamicTemplateSystem().getThemes());
                ConverterImpl.LOGGER.debug("page=" + page + ", theme=" + theme);
                ConverterImpl.LOGGER.debug("parameterMap=" + parameterMap
                        + ", templateContent(dumping values below)");
                ConverterImpl.LOGGER.debug(templateContent);

                // process parameters
                for (final Map.Entry<String, IParameter> entry : parameterMap
                        .entrySet()) {
                    ConverterImpl.LOGGER.debug("processing parameter "
                            + entry.getValue());
                    final String key = Replaceable.PARAM_PRE.getValueEscaped()
                            + entry.getKey()
                            + Replaceable.PARAM_POST.getValueEscaped();
                    ConverterImpl.LOGGER.debug("regex key is: " + key);

                    final IParameter parameter = entry.getValue();
                    final ParameterContext parameterContext = new ParameterContext(
                            this.baseDtsPath, this.baseExportPath,
                            this.dynamicTemplateSystem.getFilePath(),
                            this.dynamicTemplateSystem, page);

                    ConverterImpl.LOGGER.debug("preparing parameter");
                    parameter.prepare(parameterContext);
                    if (parameter instanceof ILazyInitializationCapable) {
                        ConverterImpl.LOGGER
                                .debug("lazy initializing parameter");
                        LazyInitializationHelper.lazyInitialize(
                                (ILazyInitializationCapable) parameter,
                                this.dynamicTemplateSystem);
                    }
                    final String value = parameter.getValue();
                    ConverterImpl.LOGGER.debug("parameter value is " + value);

                    ConverterImpl.LOGGER.debug("creating Pattern and Matcher");
                    final Pattern pattern = Pattern.compile(key);
                    final Matcher matcher = pattern.matcher(templateContent);
                    ConverterImpl.LOGGER.debug("starting regex replacement");
                    templateContent = matcher.replaceAll(Matcher
                            .quoteReplacement(value));
                    ConverterImpl.LOGGER.debug("content after replacement: "
                            + templateContent);
                }

                // apply generics
                ConverterImpl.LOGGER.debug("applying generics");
                final GenericContext context = new GenericContext(
                        this.baseDtsPath, this.baseExportPath,
                        this.dynamicTemplateSystem.getFilePath(),
                        this.dynamicTemplateSystem, page, templateContent,
                        this.dynamicTemplateSystem.getPluginsGeneric().values());
                ConverterImpl.LOGGER.debug("invoking GenericsHelper");
                templateContent = GenericsHelper.applyGenerics(context);

                // check not replaced parameter/generics
                ConverterImpl.LOGGER.debug("checking unreplaced values");
                final Pattern p = Pattern.compile("("
                        + Replaceable.GENERIC_PRE.getValueEscaped()
                        // TODO define constant for this somewhere
                        + "[^\\$\\{\\}]*"
                        + Replaceable.GENERIC_POST.getValueEscaped() + ")");
                final Matcher m = p.matcher(templateContent);

                while (m.find()) {
                    ConverterImpl.LOGGER
                            .warn("unreplaced parameter/generic value found. Please check your configuration: "
                                    + m.group(1) + "!!");
                }

                // done
                ConverterImpl.LOGGER.debug("converting page finished");
                return templateContent;
            } catch (final IOException ex) {
                throw new ConverterException(
                        "Unable to read ThemeFile, caught IOException!!", ex);
            } catch (final LazyInitializationException ex) {
                throw new ConverterException("Lazy initialization failed!!", ex);
            } catch (final PluginInitializationException ex) {
                throw new ConverterException(
                        "Caught PluginInitializationException!!", ex);
            } catch (final ParameterValueException ex) {
                throw new ConverterException(
                        "Unable to retrieve parameter value!!", ex);
            } catch (final GenericValueException ex) {
                throw new ConverterException(
                        "Unable to retrieve generic value!!", ex);
            }
        } else {
            throw new ConverterException("got no DTS!!");
        }
    }

    /**
     * @see de.groth.dts.api.core.IConverter#exportAll()
     */
    public void exportAll() throws ConverterException {
        ConverterImpl.LOGGER.debug("exporting all pages");

        if (this.dynamicTemplateSystem != null) {
            this.lastExportAllCallDate = DateHelper.now();

            // pre-processing
            ConverterImpl.LOGGER.info("pre processing");
            for (int i = 0; i < this.dynamicTemplateSystem.getPreProcessings().length; i++) {
                final IProcessing processing = this.dynamicTemplateSystem
                        .getPreProcessings()[i];
                ConverterImpl.LOGGER.debug("processing " + processing);

                // TODO null for page is not that fine!!
                final DynamicTemplateSystemExecutionContext context = new DynamicTemplateSystemExecutionContext(
                        this.baseDtsPath, this.baseExportPath,
                        this.dynamicTemplateSystem.getFilePath(),
                        this.dynamicTemplateSystem, null);
                if (!processing.process(context)) {
                    ConverterImpl.LOGGER.error("processing failed: "
                            + processing);
                }
            }

            // exporting pages
            final IPage[] pages = this.dynamicTemplateSystem.getPages();
            for (final IPage page : pages) {
                try {
                    this.export(page.getId());
                } catch (final PageNotExportableException ex) {
                    ConverterImpl.LOGGER.info("skipping not exportable page '"
                            + page.getId() + "'");
                    ConverterImpl.LOGGER
                            .debug("ignoring PageNotExportableException: "
                                    + ex.getMessage());
                }
            }

            // post-processing
            ConverterImpl.LOGGER.info("post processing");
            for (int i = 0; i < this.dynamicTemplateSystem.getPostProcessings().length; i++) {
                final IProcessing processing = this.dynamicTemplateSystem
                        .getPostProcessings()[i];
                ConverterImpl.LOGGER.debug("processing " + processing);

                // TODO null for page is not that fine!!
                final DynamicTemplateSystemExecutionContext context = new DynamicTemplateSystemExecutionContext(
                        this.baseDtsPath, this.baseExportPath,
                        this.dynamicTemplateSystem.getFilePath(),
                        this.dynamicTemplateSystem, null);
                if (!processing.process(context)) {
                    ConverterImpl.LOGGER.error("processing failed: "
                            + processing);
                }
            }
        } else {
            throw new ConverterException("Got no DTS!!");
        }
    }

    /**
     * @see de.groth.dts.api.core.IConverter#export(java.lang.String)
     */
    public void export(final String pageId) throws ConverterException {
        ConverterImpl.LOGGER.debug("exporting page " + pageId);

        if (this.dynamicTemplateSystem != null) {
            // check if page is exportable
            ConverterImpl.LOGGER.debug("getting page from dts");
            final IPage page = this.dynamicTemplateSystem.getPageById(pageId);
            if (page == null) {
                throw new ConverterPreparationException("Page with id '"
                        + pageId + "' not found!!");
            }
            ConverterImpl.LOGGER.debug("page is " + page);

            if (page.getOutputFile() == null
                    || page.getOutputFile().trim().equals("")) {
                throw new PageNotExportableException(
                        "page with id '"
                                + pageId
                                + "' not exportable due to missing or empty attribute 'file'!!");
            }

            // convert
            this.lastExportCallDateMap.put(pageId, DateHelper.now());
            final String output = this.convert(pageId);

            // create output file
            ConverterImpl.LOGGER.debug("creating output directory "
                    + this.baseExportPath);
            final File outputDir = new File(this.baseExportPath);
            if (!outputDir.exists()) {
                ConverterImpl.LOGGER.debug("creating dirs");
                boolean dirsCreated = outputDir.mkdirs();
                if (!dirsCreated) {
                    ConverterImpl.LOGGER
                            .debug("directories already existent ...");
                }
            }

            // write file
            ConverterImpl.LOGGER.debug("writing file");
            FileWriter out = null;
            try {
                final File outputFile = new File(this.baseExportPath
                        + System.getProperty("file.separator")
                        + page.getOutputFile());
                ConverterImpl.LOGGER.debug("writing to "
                        + outputFile.getAbsolutePath());

                final File parent = outputFile.getParentFile();
                if (parent != null && !parent.exists()) {
                    ConverterImpl.LOGGER.debug("output directory is: "
                            + parent.getAbsolutePath());
                    boolean dirsCreated = parent.mkdirs();
                    if (!dirsCreated) {
                        ConverterImpl.LOGGER
                                .debug("directories already existent ...");
                    }
                }

                ConverterImpl.LOGGER.debug("writing to file");
                out = new FileWriter(outputFile);
                out.write(output);
            } catch (final IOException ex) {
                ConverterImpl.LOGGER.error(
                        "IOException during fileWrite occured!!", ex);
            } finally {
                if (out != null) {
                    try {
                        ConverterImpl.LOGGER.debug("closing FileWriter");
                        out.close();
                    } catch (final IOException ex) {
                        ConverterImpl.LOGGER
                                .error(
                                        "IOException while closing FileWriter occured!!",
                                        ex);
                    }
                }
            }
        } else {
            throw new ConverterException("Got no DTS!!");
        }
    }

    /*
     * Helper
     */
    private IPage getPageById(final String pageId) throws ConverterException {
        final IPage page = this.dynamicTemplateSystem.getPageById(pageId);
        if (page == null) {
            throw new ConverterException("unable to resolve pageId=" + pageId
                    + ", page not existent!!");
        }

        return page;
    }

    private HashMap<String, IParameter> buildParameterSet(final IPage page)
            throws ConverterPreparationException {
        ConverterImpl.LOGGER.debug("building parameter-map to convert page");
        final HashMap<String, IParameter> parameterMap = new HashMap<String, IParameter>();

        ConverterImpl.LOGGER.debug("adding themeParameter");
        final IParameter[] themeParameter = IThemeHelper.getAllParameters(page
                .getTheme().getId(), this.getDynamicTemplateSystem()
                .getThemes());
        for (final IParameter parameter : themeParameter) {
            if (!parameterMap.containsKey(parameter.getParameterName())) {
                ConverterImpl.LOGGER.debug("adding "
                        + parameter.getParameterName());
                parameterMap.put(parameter.getParameterName(), parameter);
            } else {
                throw new ConverterPreparationException(
                        "duplicate parameter name found in theme-hierarchy="
                                + page.getId() + ", key="
                                + parameter.getParameterName() + "!!");
            }
        }

        ConverterImpl.LOGGER.debug("adding page parameter");
        final IParameter[] pageParameter = page.getParameter();
        for (final IParameter parameter : pageParameter) {
            ConverterImpl.LOGGER
                    .debug("adding " + parameter.getParameterName());
            if (parameterMap.containsKey(parameter.getParameterName())) {
                ConverterImpl.LOGGER
                        .debug("overwriting parameter from theme-hierarchy: "
                                + parameter.getParameterName());
            }
            parameterMap.put(parameter.getParameterName(), parameter);
        }

        ConverterImpl.LOGGER.debug("built parameter-map: " + parameterMap);
        return parameterMap;
    }
}
