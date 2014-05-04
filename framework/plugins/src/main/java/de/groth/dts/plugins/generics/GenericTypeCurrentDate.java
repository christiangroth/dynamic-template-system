package de.groth.dts.plugins.generics;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import de.groth.dts.api.core.dao.GenericContext;
import de.groth.dts.api.core.dao.PluginInstantiationContext;
import de.groth.dts.api.core.dto.NonDeterministic;
import de.groth.dts.api.core.dto.plugins.PluginConstructor;
import de.groth.dts.api.core.dto.plugins.generics.AbstractGeneric;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;
import de.groth.dts.api.core.exception.plugins.generics.GenericValueException;
import de.groth.dts.api.core.util.DateHelper;

/**
 * Creates a date as output. Format (java.text.DateFormat) and type of date have
 * to be specified by parameters format and type. <br/><br/>
 * 
 * You can specify the following types of date:
 * <ul>
 * <li>current (default, if no type is given)</li>
 * <li>convert: date when convert in IConverter was called for current page</li>
 * <li>export: date when export in IConverter was called for current page</li>
 * <li>exportAll: date when exportAll in IConverter was called</li>
 * </ul>
 * 
 * The format is described as general java date-format like in
 * {@link DateFormat}.<br/><br/>
 * 
 * Note: This one is non deterministic!!
 * 
 * @see DateFormat
 * 
 * @author Christian Groth
 */
@NonDeterministic
public class GenericTypeCurrentDate extends AbstractGeneric {
    private static final Logger LOGGER = Logger
            .getLogger(GenericTypeCurrentDate.class);

    private static final String PARAMETER_DATE_FORMAT = "format";
    private static final String PARAMETER_DATE_TYPE = "type";

    /**
     * This Enum defines all allowed types of date that can be processed.
     * 
     * @author Christian Groth
     */
    private static enum ParameterDateTypes {
        /**
         * Current time during execution.
         */
        CURRENT("current"),

        /**
         * Time when convert was calles the last time
         */
        CONVERT("convert") {
            @Override
            public Date getDate(final GenericContext context,
                    final String pageId) {
                return context.getDts().getConverter().getLastConvertCallDate(
                        pageId);
            }
        },

        /**
         * Time when export was called the last time.
         */
        EXPORT("export") {
            @Override
            public Date getDate(final GenericContext context,
                    final String pageId) {
                return context.getDts().getConverter().getLastExportCallDate(
                        pageId);
            }
        },

        /**
         * Time when exportAll was called the last time.
         */
        EXPORT_ALL("exportAll") {
            @Override
            public Date getDate(final GenericContext context,
                    final String pageId) {
                return context.getDts().getConverter()
                        .getLastExportAllCallDate();
            }
        };

        private String type;

        private ParameterDateTypes(final String type) {
            this.type = type;
        }

        /**
         * Gets the typeString, that has to match the given parameter value.
         * 
         * @return the type
         */
        public String getType() {
            return this.type;
        }

        /**
         * Gets the date.
         * 
         * @param context
         *                the {@link GenericContext}
         * @param pageId
         *                the pageId
         * 
         * @return the date
         */
        public Date getDate(final GenericContext context, final String pageId) {
            return DateHelper.now();
        }
    }

    /**
     * Creates a new instance (annotated as {@link PluginConstructor})
     * 
     * @param config
     *                current {@link PluginInstantiationContext}
     * @throws PluginInitializationException
     */
    @PluginConstructor
    public GenericTypeCurrentDate(final PluginInstantiationContext config)
            throws PluginInitializationException {
        super(config.getPluginKey(), config.getPluginClassName());
    }

    /**
     * @see de.groth.dts.api.core.dto.generics.IGeneric#getValue(de.groth.dts.api.core.dao.GenericContext)
     */
    public String getValue(final GenericContext context)
            throws GenericValueException {
        // get dateFormat parameter value
        final String dateFormatString = context
                .getGenericAttribute(GenericTypeCurrentDate.PARAMETER_DATE_FORMAT);
        GenericTypeCurrentDate.LOGGER.debug(this.getGenericName()
                + ": dateFormat=" + dateFormatString);

        if (dateFormatString == null || dateFormatString.trim().equals("")) {
            throw new GenericValueException(this.getGenericName()
                    + ": parameter 'dateFormat' must be set!!");
        }

        // determine type of date to format
        final String typeOfDate = context
                .getGenericAttribute(GenericTypeCurrentDate.PARAMETER_DATE_TYPE);
        GenericTypeCurrentDate.LOGGER.debug(this.getGenericName() + ": type="
                + typeOfDate);

        Date theDateToFormat = DateHelper.now();
        if (typeOfDate != null && !typeOfDate.trim().equals("")) {
            for (final ParameterDateTypes dateType : ParameterDateTypes
                    .values()) {
                if (dateType.getType().equalsIgnoreCase(typeOfDate)) {
                    theDateToFormat = dateType.getDate(context, context
                            .getCurrentPage().getId());
                    break;
                }
            }
        }

        // format
        GenericTypeCurrentDate.LOGGER.debug(this.getGenericName()
                + ": formatting date");
        final DateFormat dateFormat = new SimpleDateFormat(dateFormatString);
        return dateFormat.format(theDateToFormat);
    }
}
