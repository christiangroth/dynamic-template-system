package de.groth.dts.api.xml.structure;

import de.groth.dts.api.core.dto.processings.IProcessing;

/**
 * Enumeration holding information about the structure of the xml representation
 * of {@link IProcessing}.
 * 
 * @author Christian Groth
 * 
 */
public enum ProcessingXmlInformation {

    /**
     * Xml node name
     */
    NODE_NAME("processing"),

    /**
     * Xml type attribute
     */
    ATT_TYPE("type");

    private final String value;

    private ProcessingXmlInformation(final String value) {
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
