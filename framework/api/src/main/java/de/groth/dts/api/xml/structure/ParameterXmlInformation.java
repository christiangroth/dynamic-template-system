package de.groth.dts.api.xml.structure;

import de.groth.dts.api.core.dto.parameters.IParameter;

/**
 * Enumeration holding information about the structure of the xml representation
 * of {@link IParameter}.
 * 
 * @author Christian Groth
 * 
 */
public enum ParameterXmlInformation {
    /**
     * Xml node name
     */
    NODE_NAME("param"),

    /**
     * Xml name attribute
     */
    ATT_NAME("name"),

    /**
     * Xml type attribute
     */
    ATT_TYPE("type");

    private final String value;

    private ParameterXmlInformation(final String value) {
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
