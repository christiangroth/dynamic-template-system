package de.groth.dts.api.core.dao;

import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import de.groth.dts.api.core.dto.IDynamicTemplateSystem;
import de.groth.dts.api.core.dto.IPage;
import de.groth.dts.api.core.dto.generics.IGeneric;
import de.groth.dts.api.core.dto.plugins.generics.PluginTypeGeneric;
import de.groth.dts.api.core.exception.plugins.generics.GenericParameterRegistrationException;

/**
 * This class provides all information needed during evaluation of generics.
 * 
 * @author Christian Groth
 */
public class GenericContext extends DynamicTemplateSystemExecutionContext {
    private static final Logger LOGGER = Logger.getLogger(GenericContext.class);

    private final String source;
    private final Collection<PluginTypeGeneric> genericPlugins;
    private final HashMap<String, String> genericAttributes;

    /**
     * Creates an new instance.
     * 
     * @param baseDtsPath
     *                the dts base path
     * @param baseExportPath
     *                the dts export path
     * @param dtsFilePath
     *                the dts file path (relative to dts base path)
     * @param dts
     *                current {@link IDynamicTemplateSystem}
     * @param currentPage
     *                current {@link IPage}
     * @param source
     *                source (generics meant to be applied on this source)
     * @param genericPlugins
     *                list of {@link PluginTypeGeneric}
     */
    public GenericContext(final String baseDtsPath,
            final String baseExportPath, final String dtsFilePath,
            final IDynamicTemplateSystem dts, final IPage currentPage,
            final String source,
            final Collection<PluginTypeGeneric> genericPlugins) {
        super(baseDtsPath, baseExportPath, dtsFilePath, dts, currentPage);
        this.source = source;
        this.genericPlugins = genericPlugins;
        this.genericAttributes = new HashMap<String, String>();
    }

    /**
     * Creates a copy of given context.
     * 
     * @param other
     *                the copied context
     * 
     * @return copy of the context
     */
    public static GenericContext copy(final GenericContext other) {
        final GenericContext genericContext = new GenericContext(other
                .getBaseDtsPath(), other.getBaseExportPath(), other
                .getDtsFilePath(), other.getDts(), other.getCurrentPage(),
                other.getSource(), other.getGenericPlugins());

        for (final String originalKey : other.getGenericAttributes().keySet()) {
            genericContext.addGenericAttribute(originalKey, other
                    .getGenericAttribute(originalKey));
        } // for

        return genericContext;
    }

    /**
     * Gets the source.
     * 
     * @return the source
     */
    public String getSource() {
        return this.source;
    }

    /**
     * Gets the generic plugins.
     * 
     * @return the generic plugins
     */
    public Collection<PluginTypeGeneric> getGenericPlugins() {
        return this.genericPlugins;
    }

    /**
     * Gets the generic attributes.
     * 
     * @return the generic attributes
     */
    public HashMap<String, String> getGenericAttributes() {
        return this.genericAttributes;
    }

    /**
     * Gets the generic attribute.
     * 
     * @param attributeName
     *                the attribute name
     * 
     * @return the generic attribute
     */
    public String getGenericAttribute(final String attributeName) {
        return this.genericAttributes.get(attributeName);
    }

    /**
     * Adds a specific generic attribute.
     * 
     * @param attributeName
     *                the attribute name
     * @param attributeValue
     *                the attribute value
     */
    public void addGenericAttribute(final String attributeName,
            final String attributeValue) {
        this.genericAttributes.put(attributeName, attributeValue);
    }

    /**
     * Registers a new generic parameter. The given string must follow the
     * syntax defined in {@link IGeneric}.
     * 
     * @param attributeString
     *                string representing the parameter
     * @throws GenericParameterRegistrationException
     *                 in case of bad syntax
     */
    public void registerGenericParameters(final String attributeString)
            throws GenericParameterRegistrationException {
        GenericContext.LOGGER
                .debug("adding generic parameters from string representation: "
                        + attributeString);
        if (attributeString == null || attributeString.trim().equals("")) {
            throw new GenericParameterRegistrationException(
                    "no attributes given!!");
        }

        final String[] pairs = attributeString
                .split(IGeneric.GENERIC_PARAMETER_DELIMITER);
        if (pairs == null || pairs.length < 1) {
            throw new GenericParameterRegistrationException(
                    "not parameters found after split wirh "
                            + IGeneric.GENERIC_PARAMETER_DELIMITER + "!!");
        }

        for (final String pair : pairs) {
            final String[] keyValue = pair
                    .split(IGeneric.GENERIC_PARAMETER_KEY_VALUE_DELIMITER);
            if (keyValue.length != 2) {
                throw new GenericParameterRegistrationException(
                        "delimiter must be "
                                + IGeneric.GENERIC_PARAMETER_KEY_VALUE_DELIMITER
                                + "!!");
            }

            GenericContext.LOGGER
                    .debug("creating parameter from key/value-pair: "
                            + keyValue[0] + "/" + keyValue[1]);
            if (keyValue[0] != null && !keyValue[0].trim().equals("")
                    && keyValue[1] != null && !keyValue[1].trim().equals("")) {
                this
                        .addGenericAttribute(keyValue[0].trim(), keyValue[1]
                                .trim());
            } else {
                throw new GenericParameterRegistrationException(
                        "key or value is empty/null: " + keyValue[0] + "="
                                + keyValue[1] + "!!");
            }
        }
    }
}
