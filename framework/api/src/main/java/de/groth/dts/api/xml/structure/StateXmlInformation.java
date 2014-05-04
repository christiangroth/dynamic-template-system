package de.groth.dts.api.xml.structure;

import de.groth.dts.api.core.dto.IState;

/**
 * Enumeration holding information about the structure of the xml representation
 * of {@link IState}.
 * 
 * @author Christian Groth
 * 
 */
public enum StateXmlInformation {

    /**
     * Xml node name
     */
    NODE_NAME("state"),

    /**
     * Xml id attribute
     */
    ATT_ID("id"),

    /**
     * Xml cropCData attribute
     */
    ATT_CROP_CDATA("cropCData"),

    /**
     * Xml path to pre data
     */
    PATH_PRE("pre"),

    /**
     * Xml path to post data
     */
    PATH_POST("post"),

    /**
     * Xml path to default data
     */
    PATH_DEFAULT("default"),

    /**
     * Xml path to conditions
     */
    PATH_CONDITION("condition"),

    /**
     * Xml value attribute for condition
     */
    CONDITION_ATT_VALUE("value");

    private final String value;

    private StateXmlInformation(final String value) {
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
