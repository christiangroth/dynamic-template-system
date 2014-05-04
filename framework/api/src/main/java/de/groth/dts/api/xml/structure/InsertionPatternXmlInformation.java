package de.groth.dts.api.xml.structure;

import de.groth.dts.api.core.dto.IInsertionPattern;

/**
 * Enumeration holding information about the structure of the xml representation
 * of {@link IInsertionPattern}.
 * 
 * @author Christian Groth
 * 
 */
public enum InsertionPatternXmlInformation {

    /**
     * Xml node name
     */
    NODE_NAME("insertionPattern"),

    /**
     * Xml id attribute
     */
    ATT_ID("id"),

    /**
     * Xml cropCData attribute
     */
    ATT_CROP_CDATA("cropCData"),

    /**
     * Xml path to default active insertion value
     */
    PATH_DEFAULT_ACTIVE("defaultActiveInsertion"),

    /**
     * Xml path to default inactive insertion value
     */
    PATH_DEFAULT_INACTIVE("defaultInactiveInsertion"),

    /**
     * Xml path to default unreplaced insertion value
     */
    PATH_DEFAULT_UNREPLACED("defaultUnreplacedInsertion"),

    /**
     * Xml path to data
     */
    PATH_DATA("data");

    private final String value;

    private InsertionPatternXmlInformation(final String value) {
        this.value = value;
    }

    /**
     * Returns the value to be used by e. g. xpath, dom
     * 
     * @return value
     */
    public String getValue() {
        return this.value;
    }

    /**
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return this.getValue();
    }
}
