package de.groth.dts.plugins.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import de.groth.dts.api.core.dto.IInsertionPattern;
import de.groth.dts.api.core.dto.Replaceable;
import de.groth.dts.plugins.exception.InsertionPatternException;

/**
 * Helper class for processing instances of {@link IInsertionPattern}.
 * 
 * @author Christian Groth
 * 
 */
public final class InsertionPatternValueResolver {
    private static final Logger LOGGER = Logger
            .getLogger(InsertionPatternValueResolver.class);

    private InsertionPatternValueResolver() {

    }

    /**
     * Resolves the value of the given {@link IInsertionPattern} in three steps:
     * <ol>
     * <li>processes all insertion points of parameter activeInsertionPoints
     * with value of parameter activeValue</li>
     * <li>processes all insertion points of parameter inactiveInsertionPoints
     * with value of parameter inactiveValue</li>
     * <li>processes all unreplaced insertionPoints with value of parameter
     * unreplacedValue</li>
     * </ol>
     * 
     * @param insertionPattern
     *                instacne if {@link IInsertionPattern}
     * @param activeInsertionPoints
     *                all active insertionPoints
     * @param activeValue
     *                value to process active insertionPoint with
     * @param inactiveInsertionPoints
     *                all inactive insertionPoints
     * @param inactiveValue
     *                value to process inactive insertionPoint with
     * @param unreplacedValue
     *                value to process all unreplaced insertionPoints with
     *                (neither active nor inactive)
     * @return the replaced data
     * @throws InsertionPatternException
     *                 if unreplaced insertionPoints are found an no (or nor
     *                 empty) value for unreplaced insertionPoints is provided
     */
    public static String convert(final IInsertionPattern insertionPattern,
            final String[] activeInsertionPoints, final String activeValue,
            final String[] inactiveInsertionPoints, final String inactiveValue,
            final String unreplacedValue) throws InsertionPatternException {
        InsertionPatternValueResolver.LOGGER
                .debug("converting insertionPattern " + insertionPattern);
        String result = insertionPattern.getData();

        InsertionPatternValueResolver.LOGGER.debug("activeValue=" + activeValue
                + ", inactiveValue=" + inactiveValue + ", unreplacedValue="
                + unreplacedValue + ", data (dump follows)");
        InsertionPatternValueResolver.LOGGER.debug(result);

        /*
         * replace active values
         */
        if (activeInsertionPoints != null && activeInsertionPoints.length > 0) {
            InsertionPatternValueResolver.LOGGER
                    .debug("replacing activeInsertionPoints");
            for (final String insertionPoint : activeInsertionPoints) {
                result = InsertionPatternValueResolver.convertInsertionPoint(
                        result, insertionPoint, activeValue);

            }
        }

        /*
         * replace inactive values
         */
        if (inactiveInsertionPoints != null
                && inactiveInsertionPoints.length > 0) {
            InsertionPatternValueResolver.LOGGER
                    .debug("replacing inactiveInsertionPoints");
            for (final String insertionPoint : inactiveInsertionPoints) {
                result = InsertionPatternValueResolver.convertInsertionPoint(
                        result, insertionPoint, inactiveValue);

            }
        }

        // check for unreplaced insertionPoints
        InsertionPatternValueResolver.LOGGER
                .debug("checking for unreplaced insertionPoints");
        final Pattern p = Pattern.compile("("
                + Replaceable.INSERTION_PATTERN_PRE.getValueEscaped()
                // TODO define constant for this somewhere
                + "([^\\$\\{\\}]*)"
                + Replaceable.INSERTION_PATTERN_POST.getValueEscaped() + ")");
        final Matcher m = p.matcher(result);

        InsertionPatternValueResolver.LOGGER
                .debug("matcher created, starting so search");
        final List<String> unreplacedInsertionPoints = new ArrayList<String>();
        while (m.find()) {
            InsertionPatternValueResolver.LOGGER.debug("found " + m.group());
            unreplacedInsertionPoints.add(m.group(2));
        }

        if (!unreplacedInsertionPoints.isEmpty()
                && (unreplacedValue == null || unreplacedValue.trim()
                        .equals(""))) {
            if (unreplacedInsertionPoints.size() == 1) {
                throw new InsertionPatternException(
                        "unreplaced insertionPoint found, please check your xml: "
                                + unreplacedInsertionPoints.get(0) + "!!");
            } else {
                final StringBuffer sb = new StringBuffer();
                for (int i = 0; i < unreplacedInsertionPoints.size(); i++) {
                    final String insertionPoint = unreplacedInsertionPoints
                            .get(i);
                    sb.append(insertionPoint);

                    if (i < (unreplacedInsertionPoints.size() - 1)) {
                        sb.append(", ");
                    }
                }

                throw new InsertionPatternException(
                        "unreplaced insertionPoints found, please check your xml: "
                                + sb.toString() + "!!");
            }
        }

        InsertionPatternValueResolver.LOGGER
                .debug("iterating over unreplaced insertionPoints");
        for (final String insertionPoint : unreplacedInsertionPoints) {
            InsertionPatternValueResolver.LOGGER
                    .debug("converting unreplaced insertionPoint");
            result = InsertionPatternValueResolver.convertInsertionPoint(
                    result, insertionPoint, unreplacedValue);
        }

        InsertionPatternValueResolver.LOGGER.debug("returning result: "
                + result);
        return result;
    }

    private static String convertInsertionPoint(final String dataString,
            final String insertionPoint, final String insertionValue) {
        InsertionPatternValueResolver.LOGGER.debug("Replacing "
                + insertionPoint + " with value " + insertionValue);

        final String key = Replaceable.INSERTION_PATTERN_PRE.getValueEscaped()
                + insertionPoint
                + Replaceable.INSERTION_PATTERN_POST.getValueEscaped();

        InsertionPatternValueResolver.LOGGER
                .debug("creating pattern with key: " + key);
        final Pattern pattern = Pattern.compile(key);

        InsertionPatternValueResolver.LOGGER.debug("creating matcher on: "
                + dataString);
        final Matcher matcher = pattern.matcher(dataString);
        final String result = matcher.replaceAll(Matcher
                .quoteReplacement(insertionValue));
        InsertionPatternValueResolver.LOGGER
                .debug("returning data after replacement: " + result);
        return result;
    }
}
