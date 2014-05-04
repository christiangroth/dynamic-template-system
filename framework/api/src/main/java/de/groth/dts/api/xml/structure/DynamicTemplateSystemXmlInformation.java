package de.groth.dts.api.xml.structure;

import de.groth.dts.api.core.dto.IDynamicTemplateSystem;

/**
 * Enumeration holding information about the structure of the xml representation
 * of {@link IDynamicTemplateSystem}.
 * 
 * @author Christian Groth
 * 
 */
public enum DynamicTemplateSystemXmlInformation {

    /**
     * Xml node name
     */
    NODE_NAME("dynamicTemplateSystem"),

    /**
     * Xml id attribute
     */
    ATT_ID("id"),

    /**
     * Xml name attribute
     */
    ATT_NAME("name"),

    /**
     * Xml path to plugin nodes
     */
    PATH_PLUGINS("plugins"),

    /**
     * Xml path to processings
     */
    PATH_PROCESSINGS("processings"),

    /**
     * Xml path to pre-processings (relative to processings path)
     */
    PATH_PROCESSINGS_PRE("pre"),

    /**
     * Xml path to post-processings (relative to processings path)
     */
    PATH_PROCESSINGS_POST("post"),

    /**
     * Xml path to themes
     */
    PATH_THEMES("themes"),

    /**
     * Xml path to pages
     */
    PATH_PAGES("pages"),

    /**
     * Xml path to states
     */
    PATH_STATES("states"),

    /**
     * Xml path to insertion patterns
     */
    PATH_INSERTION_PATTERNS("insertionPatterns");

    private final String value;

    private DynamicTemplateSystemXmlInformation(final String value) {
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
