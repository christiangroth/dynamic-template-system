package de.groth.dts.api.xml.structure;

import de.groth.dts.api.core.dto.ITheme;

/**
 * Enumeration holding information about the structure of the xml representation
 * of {@link ITheme}.
 * 
 * @author Christian Groth
 * 
 */
public enum ThemeXmlInformation {

    /**
     * Xml node name
     */
    NODE_NAME("theme"),

    /**
     * Xml id attribute
     */
    ATT_ID("id"),

    /**
     * Xml template attriute
     */
    ATT_TEMPLATE("template"),

    /**
     * Xml superThemeId attribute
     */
    ATT_SUPER_THEME_ID("superThemeId");

    private final String value;

    private ThemeXmlInformation(final String value) {
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
