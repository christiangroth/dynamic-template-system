package de.groth.dts.plugins.generics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import de.groth.dts.api.core.dao.GenericContext;
import de.groth.dts.api.core.dao.PluginInstantiationContext;
import de.groth.dts.api.core.dto.IInsertionPattern;
import de.groth.dts.api.core.dto.plugins.PluginConstructor;
import de.groth.dts.api.core.dto.plugins.generics.AbstractGeneric;
import de.groth.dts.api.core.exception.plugins.PluginInitializationException;
import de.groth.dts.api.core.exception.plugins.generics.GenericValueException;
import de.groth.dts.api.xml.util.XmlHelper;
import de.groth.dts.plugins.exception.InsertionPatternException;
import de.groth.dts.plugins.util.InsertionPatternValueResolver;

/**
 * Includes the processed value fo the referenced {@link IInsertionPattern}:
 * ${...:insertionPattern=<insertionPatternId>,activeValue=<activeValue>,inactiveValue=<inactiveValue>,unreplacedValue=<unreplacedValue>,(<insertionPoint>=<1|0>)*}.
 * 
 * Define all insertionPoints as parameter-key and a boolean-representation as
 * value to declare it as active or inactive.
 * 
 * @see InsertionPatternValueResolver for process of resolving the referenced
 *      {@link IInsertionPattern}
 * @see XmlHelper for process of converting a String to a boolean (for
 *      insertionPoints attribute-value)
 * 
 * @author Christian Groth
 * 
 */
public class GenericTypeInsertionPattern extends AbstractGeneric {
    private static final Logger LOGGER = Logger
            .getLogger(GenericTypeInsertionPattern.class);

    private static final String PARAMETER_ID = "insertionPattern";
    private static final String PARAMETER_ACTIVE_VALUE = "activeValue";
    private static final String PARAMETER_INACTIVE_VALUE = "inactiveValue";
    private static final String PARAMETER_UNREPLACED_VALUE = "unreplacedValue";

    /**
     * Creates a new instance (annotated as {@link PluginConstructor})
     * 
     * @param config
     *                current {@link PluginInstantiationContext}
     * @throws PluginInitializationException
     */
    @PluginConstructor
    public GenericTypeInsertionPattern(final PluginInstantiationContext config)
            throws PluginInitializationException {
        super(config.getPluginKey(), config.getPluginClassName());
    }

    /**
     * @see de.groth.dts.api.core.dto.generics.IGeneric#getValue(de.groth.dts.api.core.dao.GenericContext)
     */
    public String getValue(final GenericContext context)
            throws GenericValueException {
        String id = null;
        String activeValue = null;
        String inactiveValue = null;
        String unreplacedValue = null;

        final List<String> activeInsertionPointList = new ArrayList<String>();
        final List<String> inactiveInsertionPointList = new ArrayList<String>();

        GenericTypeInsertionPattern.LOGGER.debug(this.getGenericName()
                + ": querying parameters from content ...");
        for (final Map.Entry<String, String> entry : context
                .getGenericAttributes().entrySet()) {
            final String key = entry.getKey();
            GenericTypeInsertionPattern.LOGGER.debug(this.getGenericName()
                    + ": got key=" + key + ", value=" + entry.getValue());

            if (key.equals(GenericTypeInsertionPattern.PARAMETER_ID)) {

                id = entry.getValue();
            } else if (key
                    .equals(GenericTypeInsertionPattern.PARAMETER_ACTIVE_VALUE)) {
                activeValue = entry.getValue();
            } else if (key
                    .equals(GenericTypeInsertionPattern.PARAMETER_INACTIVE_VALUE)) {
                inactiveValue = entry.getValue();
            } else if (key
                    .equals(GenericTypeInsertionPattern.PARAMETER_UNREPLACED_VALUE)) {
                unreplacedValue = entry.getValue();
            } else {
                if (XmlHelper.stringToBoolean(entry.getValue())) {
                    GenericTypeInsertionPattern.LOGGER.debug(this
                            .getGenericName()
                            + ": " + key + " is active");
                    activeInsertionPointList.add(key);
                } else {
                    GenericTypeInsertionPattern.LOGGER.debug(this
                            .getGenericName()
                            + ": " + key + " is inactive");
                    inactiveInsertionPointList.add(key);
                }
            }
        }

        if (id == null) {
            throw new GenericValueException(this.getGenericName()
                    + ": id must be set!!");
        }

        final IInsertionPattern insertionPattern = context.getDts()
                .getInsertionPatternById(id);
        if (insertionPattern == null) {
            throw new GenericValueException(this.getGenericName()
                    + ": unable to resolve InsertionPattern with id " + id
                    + "!!");
        }

        if (activeValue == null || activeValue.trim().equals("")) {
            GenericTypeInsertionPattern.LOGGER.debug(this.getGenericName()
                    + ": using default for activeValue due to null or empty: '"
                    + activeValue + "' !!");
            activeValue = insertionPattern.getDefaultActiveValue();
        }

        if (inactiveValue == null || inactiveValue.trim().equals("")) {
            GenericTypeInsertionPattern.LOGGER
                    .debug(this.getGenericName()
                            + ": using default for inactiveValue due to null or empty: '"
                            + inactiveValue + "' !!");
            inactiveValue = insertionPattern.getDefaultInactiveValue();
        }

        if (unreplacedValue == null || unreplacedValue.trim().equals("")) {
            GenericTypeInsertionPattern.LOGGER
                    .debug(this.getGenericName()
                            + ": using default for unreplacedValue due to null or empty: '"
                            + unreplacedValue + "' !!");
            unreplacedValue = insertionPattern.getDefaultUnreplacedValue();
        }

        final String[] activeInsertionPoints = activeInsertionPointList
                .isEmpty() ? null : activeInsertionPointList
                .toArray(new String[activeInsertionPointList.size()]);
        final String[] inactiveInsertionPoints = inactiveInsertionPointList
                .isEmpty() ? null : inactiveInsertionPointList
                .toArray(new String[inactiveInsertionPointList.size()]);

        GenericTypeInsertionPattern.LOGGER
                .debug(this.getGenericName()
                        + ": calling InsertionPatternValueResolver with args: insertionPattern="
                        + insertionPattern + ", activeInsertionPoints="
                        + activeInsertionPoints + ", activeValue="
                        + activeValue + ", inactiveInsertionPoints="
                        + inactiveInsertionPoints + ", inactiveValue="
                        + inactiveValue + ", unreplacedValue="
                        + unreplacedValue);

        try {
            return InsertionPatternValueResolver.convert(insertionPattern,
                    activeInsertionPoints, activeValue,
                    inactiveInsertionPoints, inactiveValue, unreplacedValue);
        } catch (final InsertionPatternException ex) {
            throw new GenericValueException(this.getGenericName()
                    + ": caught InsertionPatternException!!", ex);
        }
    }
}
