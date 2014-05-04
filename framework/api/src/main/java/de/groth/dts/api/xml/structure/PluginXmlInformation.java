package de.groth.dts.api.xml.structure;

import de.groth.dts.api.core.dto.plugins.IPlugin;

/**
 * Enumeration holding information about the structure of the xml representation
 * of {@link IPlugin}.
 * 
 * @author Christian Groth
 * 
 */
public enum PluginXmlInformation {

    /**
     * Xml key attribute
     */
    ATT_KEY("key"),

    /**
     * Xml class attribute
     */
    ATT_CLASSNAME("class"),

    /**
     * Xml path to parameter plugins
     */
    PATH_PARAMETER("param"),

    /**
     * Xml path to generic plugins
     */
    PATH_GENERIC("generic"),

    /**
     * Xml path to processing plugins
     */
    PATH_PROCESSING("processing");

    private final String value;

    private PluginXmlInformation(final String value) {
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
