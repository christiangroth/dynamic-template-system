package de.groth.dts.api.core.dto;

import de.groth.dts.api.xml.IDtsDtoXmlHandlerProvider;

/**
 * An instance of {@link IInsertionPattern} represents the xml-declaration of
 * one insertionPattern in your dts-xml file.
 * 
 * @author Christian Groth
 * 
 */
public interface IInsertionPattern extends IDtsDto,
        IDtsDtoXmlHandlerProvider<IInsertionPattern> {

    /**
     * Returns the unique id of current instance.
     * 
     * @return the unique id
     */
    String getId();

    /**
     * Returns the default value for active insertion points (without cdata).
     * 
     * @return default value (without cdata)
     */
    String getDefaultActiveValue();

    /**
     * Returns the default value for inactive insertion points (without cdata).
     * 
     * @return default value (without cdata)
     */
    String getDefaultInactiveValue();

    /**
     * Returns the default value for unreplaced insertion points (without
     * cdata).
     * 
     * @return default value (without cdata)
     */
    String getDefaultUnreplacedValue();

    /**
     * Returns the unreplaced data section (without cdata).
     * 
     * @return data (without cdata)
     */
    String getData();

    /**
     * Returns the default value for active insertion points (as defined in
     * xml).
     * 
     * @return default value (as defined in xml)
     */
    String getDefaultActiveValueRaw();

    /**
     * Returns the default value for inactive insertion points (as defined in
     * xml).
     * 
     * @return default value (as defined in xml)
     */
    String getDefaultInactiveValueRaw();

    /**
     * Returns the default value for unreplaced insertion points (as defined in
     * xml).
     * 
     * @return default value (as defined in xml)
     */
    String getDefaultUnreplacedValueRaw();

    /**
     * Returns the unreplaced data section (as defined in xml).
     * 
     * @return data section (as defined in xml)
     */
    String getDataRaw();

    /**
     * Indicates if possible CData tags will be cropped on value resolving.
     * 
     * @return true if CData will be cropped
     */
    boolean isCropCData();
}
