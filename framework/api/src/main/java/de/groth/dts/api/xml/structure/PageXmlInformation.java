package de.groth.dts.api.xml.structure;

import de.groth.dts.api.core.dto.IPage;

/**
 * Enumeration holding information about the structure of the xml representation
 * of {@link IPage}.
 * 
 * @author Christian Groth
 * 
 */
public enum PageXmlInformation {

    /**
     * Xml node name
     */
    NODE_NAME("page"),

    /**
     * Xml id attribute
     */
    ATT_ID("id"),

    /**
     * Xml theme attribute
     */
    ATT_THEME("theme"),

    /**
     * Xml file attribute
     */
    ATT_FILE("file"),

    /**
     * Xpath theme query prefix
     */
    QUERY_THEME_PRE("../../" + DynamicTemplateSystemXmlInformation.PATH_THEMES
            + "/" + ThemeXmlInformation.NODE_NAME.getValue() + "[@"
            + ThemeXmlInformation.ATT_ID.getValue() + "='"),

    /**
     * XPath theme query postfix
     */
    QUERY_THEME_POST("']");

    private final String value;

    private PageXmlInformation(final String value) {
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
