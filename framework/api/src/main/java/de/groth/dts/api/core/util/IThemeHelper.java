package de.groth.dts.api.core.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.groth.dts.api.core.dto.ITheme;
import de.groth.dts.api.core.dto.parameters.IParameter;
import de.groth.dts.api.core.exception.dto.DtoInitializationException;
import de.groth.dts.api.core.exception.dto.ThemeInheritanceException;

/**
 * Helper class for {@link ITheme}.
 * 
 * @author Christian Groth
 * 
 */
public final class IThemeHelper {
    private static final Logger LOGGER = Logger.getLogger(IThemeHelper.class);

    private IThemeHelper() {

    }

    /**
     * Checks the theme inherticance for given themeId. If circular dependencies
     * are found an {@link DtoInitializationException} will be thrown.
     * 
     * @param themeId
     *                themeId of {@link ITheme} to be checked
     * @param allThemes
     *                all registered themes
     * @throws ThemeInheritanceException
     *                 if circular dependencies are detected
     */
    public static void checkThemeInheritance(final String themeId,
            final ITheme[] allThemes) throws ThemeInheritanceException {
        final ITheme theme = IThemeHelper.getThemeFromAll(themeId, allThemes);

        IThemeHelper.LOGGER
                .debug("checking theme inheritance for theme with id "
                        + themeId);

        if (theme.getSuperThemeId() == null
                || theme.getSuperThemeId().trim().equals("")) {
            IThemeHelper.LOGGER.debug("nothing to do");
            return;
        }

        ITheme currentTheme = theme;
        final List<String> themeIds = new ArrayList<String>();
        while (currentTheme.getSuperThemeId() != null
                && !currentTheme.getSuperThemeId().trim().equals("")) {
            IThemeHelper.LOGGER.debug("looking at " + currentTheme);
            if (themeIds.contains(currentTheme.getId())) {
                throw new ThemeInheritanceException(
                        "found super reference thats already used: "
                                + currentTheme.getId()
                                + ", starting theme was " + themeId + "!!");
            }
            themeIds.add(currentTheme.getId());

            currentTheme = IThemeHelper.getThemeFromAll(currentTheme
                    .getSuperThemeId(), allThemes);
            IThemeHelper.LOGGER.debug("super theme is " + currentTheme);
        }
    }

    /**
     * Collects all {@link IParameter} for given theme. Also all
     * {@link IParameter} from possible superThemes are collected.
     * 
     * @param themeId
     *                theme to start with
     * @param allThemes
     *                all registered themes
     * @return all found parameters for theme and possible superThemes
     */
    public static IParameter[] getAllParameters(final String themeId,
            final ITheme[] allThemes) {
        IThemeHelper.LOGGER
                .debug("collecting all parameters for theme with id " + themeId);
        final ITheme theme = IThemeHelper.getThemeFromAll(themeId, allThemes);

        final List<IParameter> allParameters = new ArrayList<IParameter>();
        for (final IParameter parameter : theme.getParameter()) {
            IThemeHelper.LOGGER.debug("adding direct: "
                    + parameter.getParameterName());
            allParameters.add(parameter);
        }

        String superThemeId = theme.getSuperThemeId();
        while (superThemeId != null && !superThemeId.trim().equals("")) {
            IThemeHelper.LOGGER
                    .debug("looking at superThemeId " + superThemeId);
            final ITheme superTheme = IThemeHelper.getThemeFromAll(
                    superThemeId, allThemes);
            for (final IParameter superParameter : superTheme.getParameter()) {
                if (!allParameters.contains(superParameter)) {
                    IThemeHelper.LOGGER.debug("adding super: "
                            + superParameter.getParameterName());
                    allParameters.add(superParameter);
                } else {
                    IThemeHelper.LOGGER.debug("already contained: "
                            + superParameter.getParameterName());
                }
            }

            superThemeId = superTheme.getSuperThemeId();
        }

        IThemeHelper.LOGGER.debug("found " + allParameters.size()
                + " parameters");
        return allParameters.toArray(new IParameter[allParameters.size()]);
    }

    /**
     * Returns the templateContent for given theme. If this theme is based on a
     * superTheme it has no own template, so the hierarchy has to be looked up
     * to find the template.
     * 
     * @param theme
     *                theme to start searching
     * @param allThemes
     *                all registered themes
     * @return template content for root theme
     * @throws IOException
     *                 thrown by {@link FileHelper#getFileContent(String)}
     */
    public static String getTemplateContentForTheme(final ITheme theme,
            final ITheme[] allThemes) throws IOException {
        IThemeHelper.LOGGER
                .debug("trying to find top level theme template for theme "
                        + theme);
        if (theme.getTemplate() != null) {
            IThemeHelper.LOGGER
                    .debug("no hierarchy, that's easy ... returning content of "
                            + theme.getTemplatePath());
            return FileHelper.getFileContent(theme.getTemplate()
                    .getAbsolutePath());
        }

        ITheme currentTheme = theme;
        while (currentTheme.getSuperThemeId() != null
                && !currentTheme.getSuperThemeId().trim().equals("")) {
            IThemeHelper.LOGGER.debug("looking at " + currentTheme);
            final ITheme superTheme = IThemeHelper.getThemeFromAll(currentTheme
                    .getSuperThemeId(), allThemes);
            IThemeHelper.LOGGER.debug("super theme is " + superTheme);

            if (superTheme.getTemplate() != null) {
                IThemeHelper.LOGGER.debug("found template: "
                        + superTheme.getTemplatePath());
                return FileHelper.getFileContent(superTheme.getTemplate()
                        .getAbsolutePath());
            }

            currentTheme = IThemeHelper.getThemeFromAll(superTheme
                    .getSuperThemeId(), allThemes);
        }

        throw new IllegalArgumentException(
                "unable to resolve top-level theme template!!");
    }

    private static ITheme getThemeFromAll(final String id,
            final ITheme[] allThemes) {
        for (final ITheme theme : allThemes) {
            if (theme.getId().equals(id)) {
                return theme;
            }
        }

        throw new IllegalArgumentException("unable to find theme with id " + id
                + "!!");
    }
}
