package de.groth.dts.api.core.dto;

import de.groth.dts.api.core.dto.generics.IGeneric;
import de.groth.dts.api.core.dto.parameters.IParameter;

/**
 * Basic interface for all types objects, which will be replaced in the
 * processed files.
 * 
 * @see IParameter
 * @see IGeneric
 * @see IInsertionPattern
 * 
 * @author Christian Groth
 * 
 */
public enum Replaceable {
    /**
     * Prefix for {@link IParameter}
     */
    PARAM_PRE(Replaceable.VALUE_PARAM_PRE, Replaceable.VALUE_PARAM_PRE_ESCAPED),
    /**
     * Postfix for {@link IParameter}
     */
    PARAM_POST(Replaceable.VALUE_PARAM_POST,
            Replaceable.VALUE_PARAM_POST_ESCAPED),

    /**
     * Prefix for {@link IGeneric}
     */
    GENERIC_PRE(Replaceable.VALUE_GENERIC_PRE,
            Replaceable.VALUE_GENERIC_PRE_ESCAPED),
    /**
     * Postfix for {@link IGeneric}
     */
    GENERIC_POST(Replaceable.VALUE_GENERIC_POST,
            Replaceable.VALUE_GENERIC_POST_ESCAPED),

    /**
     * Prefix for {@link IInsertionPattern}
     */
    INSERTION_PATTERN_PRE(Replaceable.VALUE_INSERTION_PATTERN_PRE,
            Replaceable.VALUE_INSERTION_PATTERN_PRE_ESCAPED),
    /**
     * Postfix for {@link IInsertionPattern}
     */
    INSERTION_PATTERN_POST(Replaceable.VALUE_INSERTION_PATTERN_POST,
            Replaceable.VALUE_INSERTION_PATTERN_POST_ESCAPED);

    private static final String DOLLAR = "$";
    private static final String DOLLAR_ESCAPED = "\\$";
    private static final String HASH = "#";
    private static final String HASH_ESCAPED = "\\#";

    private static final String VALUE_PRE = "{";
    private static final String VALUE_PRE_ESCAPED = "\\{";
    private static final String VALUE_POST = "}";
    private static final String VALUE_POST_ESCAPED = "\\}";

    private static final String VALUE_PARAM_PRE = Replaceable.DOLLAR
            + Replaceable.VALUE_PRE;
    private static final String VALUE_PARAM_PRE_ESCAPED = Replaceable.DOLLAR_ESCAPED
            + Replaceable.VALUE_PRE_ESCAPED;
    private static final String VALUE_PARAM_POST = Replaceable.VALUE_POST;
    private static final String VALUE_PARAM_POST_ESCAPED = Replaceable.VALUE_POST_ESCAPED;

    private static final String VALUE_GENERIC_PRE = Replaceable.DOLLAR
            + Replaceable.VALUE_PRE;
    private static final String VALUE_GENERIC_PRE_ESCAPED = Replaceable.DOLLAR_ESCAPED
            + Replaceable.VALUE_PRE_ESCAPED;
    private static final String VALUE_GENERIC_POST = Replaceable.VALUE_POST;
    private static final String VALUE_GENERIC_POST_ESCAPED = Replaceable.VALUE_POST_ESCAPED;

    private static final String VALUE_INSERTION_PATTERN_PRE = Replaceable.HASH
            + Replaceable.VALUE_PRE;
    private static final String VALUE_INSERTION_PATTERN_PRE_ESCAPED = Replaceable.HASH_ESCAPED
            + Replaceable.VALUE_PRE_ESCAPED;
    private static final String VALUE_INSERTION_PATTERN_POST = Replaceable.VALUE_POST;
    private static final String VALUE_INSERTION_PATTERN_POST_ESCAPED = Replaceable.VALUE_POST_ESCAPED;

    private final String value;
    private final String valueEscaped;

    private Replaceable(final String value, final String valueEscaped) {
        this.value = value;
        this.valueEscaped = valueEscaped;
    }

    /**
     * Returns the value
     * 
     * @return value
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Returns the escaped value for regex purposes
     * 
     * @return escaped value
     */
    public String getValueEscaped() {
        return this.valueEscaped;
    }

    /**
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return this.getValue() + ":" + this.getValueEscaped();
    }
}
