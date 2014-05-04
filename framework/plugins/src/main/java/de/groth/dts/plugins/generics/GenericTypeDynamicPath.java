package de.groth.dts.plugins.generics;

import org.apache.log4j.Logger;

import de.groth.dts.api.core.dao.GenericContext;
import de.groth.dts.api.core.dao.PluginInstantiationContext;
import de.groth.dts.api.core.dto.IPage;
import de.groth.dts.api.core.dto.plugins.PluginConstructor;
import de.groth.dts.api.core.dto.plugins.generics.AbstractGeneric;
import de.groth.dts.api.core.exception.convert.PageNotExportableException;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;
import de.groth.dts.api.core.exception.plugins.generics.GenericValueException;

/**
 * Creates a path for current page to the root of export directory. ../../../
 * and so on ...<br/><br/>
 * 
 * You can specify a attribute defining another pageId, then the path points to
 * the other page.<br/><br/>
 * 
 * ../../../../<root>/path/to/other/pageFile.extenesion<br/><br/>
 * 
 * ${...:page=<optionalPageId>}<br/><br/>
 * 
 * @author Christian Groth
 */
public class GenericTypeDynamicPath extends AbstractGeneric {
    private static final Logger LOGGER = Logger
            .getLogger(GenericTypeDynamicPath.class);

    private static final String PARAMETER_OTHER_PAGE_ID = "page";

    /**
     * Creates a new instance (annotated as {@link PluginConstructor})
     * 
     * @param config
     *                current {@link PluginInstantiationContext}
     * @throws PluginInitializationException
     */
    @PluginConstructor
    public GenericTypeDynamicPath(final PluginInstantiationContext config)
            throws PluginInitializationException {
        super(config.getPluginKey(), config.getPluginClassName());
    }

    /**
     * @see de.groth.dts.api.core.dto.generics.IGeneric#getValue(de.groth.dts.api.core.dao.GenericContext)
     */
    public String getValue(final GenericContext context)
            throws GenericValueException {
        final StringBuffer sb = new StringBuffer("");

        GenericTypeDynamicPath.LOGGER.debug(this.getGenericName()
                + ": calculating depth for page " + context.getCurrentPage());
        final int depth;
        try {
            depth = context.getCurrentPage().getDepth();
        } catch (final PageNotExportableException ex) {
            throw new GenericValueException(this.getGenericName()
                    + ": page with id " + context.getCurrentPage().getId()
                    + "is not exportable!!", ex);
        }

        GenericTypeDynamicPath.LOGGER.debug(this.getGenericName()
                + ": creating path to root");
        if (depth > 0) {
            for (int i = 0; i < depth; i++) {
                sb.append("../");
            }
        }
        GenericTypeDynamicPath.LOGGER.debug(this.getGenericName()
                + ": pathToRoot=" + sb.toString());

        final String otherPageId = context
                .getGenericAttribute(GenericTypeDynamicPath.PARAMETER_OTHER_PAGE_ID);
        if (otherPageId != null && !otherPageId.trim().equals("")) {
            GenericTypeDynamicPath.LOGGER.debug(this.getGenericName()
                    + ": appending path to referenced page "
                    + otherPageId.trim());

            final IPage otherPage = context.getDts().getPageById(
                    otherPageId.trim());
            if (otherPage != null) {
                sb.append(otherPage.getOutputFile());
            } else {
                throw new GenericValueException(this.getGenericName()
                        + ": page '" + otherPageId.trim()
                        + "' can't be referenced!!");
            }
        }

        GenericTypeDynamicPath.LOGGER.debug(this.getGenericName()
                + ": dynamicPath=" + sb.toString());
        return sb.toString();
    }
}
