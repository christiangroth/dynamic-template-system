package de.groth.dts.api.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import de.groth.dts.api.core.dto.IDynamicTemplateSystem;
import de.groth.dts.api.core.dto.Replaceable;
import de.groth.dts.api.core.dto.parameters.ILazyInitializationCapable;
import de.groth.dts.api.core.dto.plugins.generics.PluginTypeGeneric;
import de.groth.dts.api.core.exception.plugins.LazyInitializationException;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;
import de.groth.dts.api.core.exception.plugins.generics.GenericValueException;

/**
 * Helper class to lazy initialize objects of type
 * {@link ILazyInitializationCapable}.
 * 
 * @author Christian Groth
 * 
 */
public final class LazyInitializationHelper {
    private static final Logger LOGGER = Logger
            .getLogger(LazyInitializationHelper.class);

    private LazyInitializationHelper() {
    }

    /**
     * Initializes the given instance of {@link ILazyInitializationCapable}.
     * 
     * @param lazyInitializationCapable
     *                the instance to be lazy initialized
     * @param dts
     *                current dts
     * @throws LazyInitializationException
     *                 if lazy initialization fails
     */
    public static void lazyInitialize(
            final ILazyInitializationCapable lazyInitializationCapable,
            final IDynamicTemplateSystem dts)
            throws LazyInitializationException {
        List<PluginTypeGeneric> generics = LazyInitializationHelper
                .getGenerics(lazyInitializationCapable, dts);
        if (generics == null || generics.size() < 1) {
            return;
        }

        LazyInitializationHelper.doLazyInitialization(
                lazyInitializationCapable, generics);

        LazyInitializationHelper.LOGGER
                .debug("checking for unreplaced generics after lazy initialization");
        generics = LazyInitializationHelper.getGenerics(
                lazyInitializationCapable, dts);

        LazyInitializationHelper.LOGGER.debug("found " + generics);
        if (generics != null && generics.size() > 0) {
            throw new LazyInitializationException(
                    "lazy parameter initialization failed for "
                            + lazyInitializationCapable
                                    .getLazyInitializationId() + " !!");
        }

        LazyInitializationHelper.LOGGER
                .debug("invoking postLazyInitializationCallback()");
        lazyInitializationCapable.postLazyInitializationCallback();
    }

    private static void doLazyInitialization(
            final ILazyInitializationCapable lazyInitializationCapable,
            final List<PluginTypeGeneric> generics)
            throws LazyInitializationException {
        if (generics == null || generics.size() < 1) {
            LazyInitializationHelper.LOGGER
                    .debug("skipping lazy initialization cause there no generics to process");
            return;
        }

        final String[] possibleMatches = lazyInitializationCapable
                .getLazyInitializationAttributeValues();
        for (final String originalValue : possibleMatches) {
            LazyInitializationHelper.LOGGER.debug("replacing possibleMatch: "
                    + originalValue);
            try {
                LazyInitializationHelper.LOGGER.debug("resolving new value");
                final String newValue = GenericsHelper
                        .applyGenerics(lazyInitializationCapable
                                .createGenericContext(generics, originalValue));
                LazyInitializationHelper.LOGGER.debug("the new value is "
                        + newValue
                        + ", setting it to lazyInitializationCapable");
                lazyInitializationCapable.setLazyInitializationAttributeValue(
                        newValue, originalValue);
            } catch (final PluginInitializationException ex) {
                throw new LazyInitializationException(
                        "caught PluginInitializationException!!", ex);
            } catch (final GenericValueException ex) {
                throw new LazyInitializationException(
                        "unable to retrieve generic value!!", ex);
            }
        }
    }

    private static List<PluginTypeGeneric> getGenerics(
            final ILazyInitializationCapable lazyInitializationCapable,
            final IDynamicTemplateSystem dts) {
        final ArrayList<PluginTypeGeneric> foundGenerics = new ArrayList<PluginTypeGeneric>();
        LazyInitializationHelper.LOGGER
                .debug("collecting generics for lazy initialization");

        for (final PluginTypeGeneric plugin : dts.getPluginsGeneric().values()) {
            LazyInitializationHelper.LOGGER.debug("checking generic "
                    + plugin.getPluginKey());

            final String genericString = Replaceable.GENERIC_PRE
                    .getValueEscaped()
                    + plugin.getPluginKey()
                    + Replaceable.GENERIC_POST.getValueEscaped();
            LazyInitializationHelper.LOGGER
                    .debug("compiling regex pattern with " + genericString);
            final Pattern pattern = Pattern
                    .compile(".*" + genericString + ".*");

            final String[] possibleMatches = lazyInitializationCapable
                    .getLazyInitializationAttributeValues();
            for (final String possibleMatch : possibleMatches) {
                LazyInitializationHelper.LOGGER
                        .debug("trying matcher with possibleMatch "
                                + possibleMatch);
                final Matcher matcher = pattern.matcher(possibleMatch);

                if (matcher.matches()) {
                    LazyInitializationHelper.LOGGER.debug("matched!");
                    foundGenerics.add(plugin);
                }
            }
        }

        return foundGenerics;
    }
}
