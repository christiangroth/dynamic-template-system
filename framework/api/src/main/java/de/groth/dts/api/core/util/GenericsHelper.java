package de.groth.dts.api.core.util;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import de.groth.dts.api.core.dao.GenericContext;
import de.groth.dts.api.core.dao.PluginInstantiationContext;
import de.groth.dts.api.core.dto.Replaceable;
import de.groth.dts.api.core.dto.generics.IGeneric;
import de.groth.dts.api.core.dto.plugins.generics.PluginTypeGeneric;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;
import de.groth.dts.api.core.exception.plugins.generics.GenericParameterRegistrationException;
import de.groth.dts.api.core.exception.plugins.generics.GenericValueException;
import de.groth.dts.api.core.util.plugins.GenericPluginFactory;

/**
 * Helper for handling generics.
 * 
 * @author Christian Groth
 */
public final class GenericsHelper {
    private static final Logger LOGGER = Logger.getLogger(GenericsHelper.class);

    private GenericsHelper() {

    }

    /**
     * Applies all listed generics in given content and returns the result.
     * 
     * @param context
     *                the context
     * 
     * @return the result as string
     * @throws PluginInitializationException
     *                 if generic can't be instantiated
     * @throws GenericValueException
     *                 if there's a problem retrievng the generic value
     */
    public static String applyGenerics(final GenericContext context)
            throws PluginInitializationException, GenericValueException {
        String result = context.getSource();
        GenericsHelper.LOGGER.debug("applying generics on source " + result);

        // check all registered plugins
        for (final PluginTypeGeneric plugin : context.getGenericPlugins()) {
            final PluginInstantiationContext pluginInstantiationContext = new PluginInstantiationContext(
                    context.getBaseDtsPath(), context.getBaseExportPath(),
                    context.getDts(), context.getDts().getConverter(), context
                            .getDtsFilePath(), null, plugin.getPluginKey(),
                    plugin.getPluginClassName(), new HashMap<String, String>());
            final IGeneric generic = new GenericPluginFactory()
                    .instantiate(pluginInstantiationContext);
            GenericsHelper.LOGGER.debug("trying generic with key="
                    + plugin.getPluginKey());

            final String regex = Replaceable.GENERIC_PRE.getValueEscaped()
                    + plugin.getPluginKey() + "("
                    + IGeneric.GENERIC_PARAMETER_SECTION_DELIMITER
                    + "[^\\{\\}]*)?"
                    + Replaceable.GENERIC_POST.getValueEscaped();
            GenericsHelper.LOGGER.debug("regex=" + regex);
            final Pattern pattern = Pattern.compile(regex);
            final Matcher matcher = pattern.matcher(result);
            GenericsHelper.LOGGER.debug("compiled pattern and created matcher");

            // get all matches and define replacements
            GenericsHelper.LOGGER.debug("collecting replacements");
            final StringBuffer replacementBuffer = new StringBuffer();
            while (matcher.find()) {
                GenericsHelper.LOGGER.debug("found match: " + matcher.group(1));
                final GenericContext specificContext = GenericContext
                        .copy(context);

                String foundAttributeString = matcher.group(1);
                if (foundAttributeString != null) {
                    foundAttributeString = foundAttributeString
                            .substring(IGeneric.GENERIC_PARAMETER_SECTION_DELIMITER
                                    .length());
                    try {
                        specificContext
                                .registerGenericParameters(foundAttributeString);
                        GenericsHelper.LOGGER
                                .debug("registered generic parameters are "
                                        + specificContext
                                                .getGenericAttributes());
                    } catch (final GenericParameterRegistrationException ex) {
                        GenericsHelper.LOGGER
                                .error(
                                        "ignoring generic parameter ("
                                                + foundAttributeString
                                                + ") due to syntax problem during parameter registration!!",
                                        ex);
                    }
                }

                final String genericValue = generic.getValue(specificContext);
                GenericsHelper.LOGGER
                        .debug("appending replacement with replacementBuffer="
                                + replacementBuffer.toString()
                                + ", genericValue=" + genericValue);
                matcher.appendReplacement(replacementBuffer, Matcher
                        .quoteReplacement(genericValue));
            }

            GenericsHelper.LOGGER.debug("replacing values, result before="
                    + result);
            result = matcher.appendTail(replacementBuffer).toString();
            GenericsHelper.LOGGER.debug("replacing values, result after="
                    + result);
        }

        return result;
    }
}
