package de.groth.dts.plugins.generics;

import org.apache.log4j.Logger;

import de.groth.dts.api.core.dao.GenericContext;
import de.groth.dts.api.core.dao.PluginInstantiationContext;
import de.groth.dts.api.core.dto.plugins.PluginConstructor;
import de.groth.dts.api.core.dto.plugins.generics.AbstractGeneric;
import de.groth.dts.api.core.exception.convert.ConverterException;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;
import de.groth.dts.api.core.exception.plugins.generics.GenericValueException;

/**
 * Includes the content of the converted page, referenced by given pageId.
 * ${...:page=<somePageId>}
 * 
 * @author Christian Groth
 * 
 */
public class GenericTypePageInclude extends AbstractGeneric {
    private static final Logger LOGGER = Logger
            .getLogger(GenericTypePageInclude.class);

    private static final String PARAMETER_PAGE_ID = "page";

    /**
     * Creates a new instance (annotated as {@link PluginConstructor})
     * 
     * @param config
     *                current {@link PluginInstantiationContext}
     * @throws PluginInitializationException
     */
    @PluginConstructor
    public GenericTypePageInclude(final PluginInstantiationContext config)
            throws PluginInitializationException {
        super(config.getPluginKey(), config.getPluginClassName());
    }

    /**
     * @see de.groth.dts.api.core.dto.generics.IGeneric#getValue(de.groth.dts.api.core.dao.GenericContext)
     */
    public String getValue(final GenericContext context)
            throws GenericValueException {
        final String pageId = context
                .getGenericAttribute(GenericTypePageInclude.PARAMETER_PAGE_ID);
        GenericTypePageInclude.LOGGER.debug(this.getGenericName() + ": pageId="
                + pageId);
        if (pageId == null || pageId.trim().equals("")) {
            throw new GenericValueException(
                    "Unable to include content: pagemust not be null or empty!!");
        }

        try {
            GenericTypePageInclude.LOGGER.debug(this.getGenericName()
                    + ": converting page");
            return context.getDts().getConverter().convert(pageId);
        } catch (final ConverterException ex) {
            throw new GenericValueException("Unable to convert nested page "
                    + pageId + " for " + context.getCurrentPage().getId()
                    + "!!", ex);
        }
    }
}
