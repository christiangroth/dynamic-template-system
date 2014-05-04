package de.groth.dts.impl.core.dto;

import java.util.HashMap;

import de.groth.dts.api.core.dao.DynamicTemplateSystemExecutionContext;
import de.groth.dts.api.core.dto.IState;
import de.groth.dts.api.core.exception.dto.DtoInitializationException;
import de.groth.dts.api.core.exception.dto.UnknownStateValueException;
import de.groth.dts.api.xml.IDtsDtoXmlHandler;
import de.groth.dts.impl.xml.StateXmlHandler;

/**
 * Default implementation of {@link IState}.
 * 
 * @author Christian Groth
 */
public class State implements IState {
    private final String id;
    private final boolean cropCData;

    private final String pre;
    private final String post;
    private final HashMap<String, String> conditionValues;
    private final String defaultValue;

    private final String rawPre;
    private final String rawPost;
    private final HashMap<String, String> rawConditionValues;
    private final String rawDefaultValue;

    /**
     * Creates a new instance
     * 
     * @param id
     *                unique id
     * @param pre
     *                pre-value
     * @param post
     *                post-value
     * @param values
     *                map of all values
     * @param defaultValue
     *                default value
     * @param rawPre
     *                raw pre-value (cdata not cropped)
     * @param rawPost
     *                raw post-value (cdata not cropped)
     * @param rawValues
     *                map of all raw values (cdata not cropped)
     * @param rawDefaultValue
     *                raw default value (cdata not cropped)
     * @param cropCData
     *                cropCData flag
     * @throws DtoInitializationException
     */
    public State(final String id, final String pre, final String post,
            final HashMap<String, String> values, final String defaultValue,
            final String rawPre, final String rawPost,
            final HashMap<String, String> rawValues,
            final String rawDefaultValue, final boolean cropCData)
            throws DtoInitializationException {
        if (id == null || id.trim().equals("")) {
            throw new DtoInitializationException("id must not be empty!!");
        }

        if (values == null || values.size() < 1) {
            throw new DtoInitializationException(
                    "there must be a minimum of one condition (values)!!");
        }

        if (rawValues == null || rawValues.size() < 1) {
            throw new DtoInitializationException(
                    "there must be a minimum of one condition (rawValues)!!");
        }

        this.id = id;
        this.cropCData = cropCData;

        this.pre = pre == null ? "" : pre;
        this.post = post == null ? "" : post;
        this.conditionValues = values;
        this.defaultValue = defaultValue != null
                && defaultValue.trim().equals("") ? null : defaultValue;

        this.rawPre = rawPre == null ? "" : rawPre;
        this.rawPost = rawPost == null ? "" : rawPost;
        this.rawConditionValues = rawValues;
        this.rawDefaultValue = rawDefaultValue != null
                && rawDefaultValue.trim().equals("") ? null : rawDefaultValue;
    }

    /**
     * @see de.groth.dts.api.core.dto.IState#getId()
     */
    public String getId() {
        return this.id;
    }

    /**
     * @see de.groth.dts.api.core.dto.IState#isCropCData()
     */
    public boolean isCropCData() {
        return this.cropCData;
    }

    /**
     * @see de.groth.dts.api.core.dto.IState#getValue(java.lang.String)
     */
    public String getValue(final String condition)
            throws UnknownStateValueException {
        if (!this.cropCData) {
            return this.getRawValue(condition);
        }

        try {
            final String conditionValue = this.getConditionValue(condition);
            return this.pre + conditionValue + this.post;
        } catch (final UnknownStateValueException ex) {
            if (this.defaultValue == null
                    || this.defaultValue.trim().equals("")) {
                throw ex;
            } else {
                return this.pre + this.defaultValue + this.post;
            }
        }
    }

    /**
     * @see de.groth.dts.api.core.dto.IState#getRawValue(java.lang.String)
     */
    public String getRawValue(final String condition)
            throws UnknownStateValueException {
        try {
            final String conditionValue = this.getRawConditionValue(condition);
            return this.rawPre + conditionValue + this.rawPost;
        } catch (final UnknownStateValueException ex) {
            if (this.rawDefaultValue == null
                    || this.rawDefaultValue.trim().equals("")) {
                throw ex;
            } else {
                return this.rawPre + this.rawDefaultValue + this.rawPost;
            }
        }
    }

    /**
     * @see de.groth.dts.api.core.dto.IState#getPre()
     */
    public String getPre() {
        return this.pre;
    }

    /**
     * @see de.groth.dts.api.core.dto.IState#getPost()
     */
    public String getPost() {
        return this.post;
    }

    /**
     * @see de.groth.dts.api.core.dto.IState#getDefaultValue()
     */
    public String getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * @see de.groth.dts.api.core.dto.IState#getConditionValue(java.lang.String)
     */
    public String getConditionValue(final String condition)
            throws UnknownStateValueException {
        final String value = this.conditionValues.get(condition);

        if (value == null) {
            throw new UnknownStateValueException("unknown condition "
                    + condition + "!!");
        }

        return value;
    }

    /**
     * @see de.groth.dts.api.core.dto.IState#getConditionValues()
     */
    public HashMap<String, String> getConditionValues() {
        return this.conditionValues;
    }

    /**
     * @see de.groth.dts.api.core.dto.IState#getRawPre()
     */
    public String getRawPre() {
        return this.rawPre;
    }

    /**
     * @see de.groth.dts.api.core.dto.IState#getRawPost()
     */
    public String getRawPost() {
        return this.rawPost;
    }

    /**
     * @see de.groth.dts.api.core.dto.IState#getRawDefaultValue()
     */
    public String getRawDefaultValue() {
        return this.rawDefaultValue;
    }

    /**
     * @see de.groth.dts.api.core.dto.IState#getRawConditionValue(java.lang.String)
     */
    public String getRawConditionValue(final String condition)
            throws UnknownStateValueException {
        final String value = this.rawConditionValues.get(condition);

        if (value == null) {
            throw new UnknownStateValueException("unknown condition "
                    + condition + "!!");
        }

        return value;
    }

    /**
     * @see de.groth.dts.api.core.dto.IState#getRawConditionValues()
     */
    public HashMap<String, String> getRawConditionValues() {
        return this.rawConditionValues;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof IState)) {
            return false;
        }

        final IState cast = (IState) obj;
        return this.id.equals(cast.getId());
    }

    private static final int HASH_CODE_BASE = 17;

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return State.HASH_CODE_BASE * this.id.hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "State: id=" + this.id;
    }

    /**
     * @see de.groth.dts.api.xml.IDtsDtoXmlHandlerProvider#createXmlHandler(de.groth.dts.api.core.dao.DynamicTemplateSystemExecutionContext)
     */
    public IDtsDtoXmlHandler<IState> createXmlHandler(
            final DynamicTemplateSystemExecutionContext context) {
        return new StateXmlHandler();
    }
}
