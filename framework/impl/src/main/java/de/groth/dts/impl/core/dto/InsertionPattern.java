package de.groth.dts.impl.core.dto;

import de.groth.dts.api.core.dao.DynamicTemplateSystemExecutionContext;
import de.groth.dts.api.core.dto.IInsertionPattern;
import de.groth.dts.api.core.exception.dto.DtoInitializationException;
import de.groth.dts.api.xml.IDtsDtoXmlHandler;
import de.groth.dts.impl.xml.InsertionPatternXmlHandler;

/**
 * Default implementation of {@link IInsertionPattern}.
 * 
 * @author Christian Groth
 * 
 */
public class InsertionPattern implements IInsertionPattern {
    private final String id;
    private final String defaultActiveValue;
    private final String defaultInactiveValue;
    private final String defaultUnreplacedValue;
    private final String data;
    private final String defaultActiveValueRaw;
    private final String defaultInactiveValueRaw;
    private final String defaultUnreplacedValueRaw;
    private final String dataRaw;
    private final boolean cropCData;

    /**
     * Creates a new instance
     * 
     * @param id
     *                unique id
     * @param defaultActiveValue
     *                default active value
     * @param defaultInactiveValue
     *                default inactive value
     * @param defaultUnreplacedValue
     *                default unreplaced value
     * @param data
     *                data
     * @param defaultActiveValueRaw
     *                default raw active value (cdata not cropped)
     * @param defaultInactiveValueRaw
     *                default raw inactive value (cdata not cropped)
     * @param defaultUnreplacedValueRaw
     *                default raw unreplaced value (cdata not cropped)
     * @param dataRaw
     *                raw data (cdata not cropped)
     * @param cropCData
     *                cropCData flag
     * @throws DtoInitializationException
     */
    public InsertionPattern(final String id, final String defaultActiveValue,
            final String defaultInactiveValue,
            final String defaultUnreplacedValue, final String data,
            final String defaultActiveValueRaw,
            final String defaultInactiveValueRaw,
            final String defaultUnreplacedValueRaw, final String dataRaw,
            final boolean cropCData) throws DtoInitializationException {

        if (id == null || id.trim().equals("")) {
            throw new DtoInitializationException("id must not be empty!!");
        }

        if (data == null || data.trim().equals("")) {
            throw new DtoInitializationException("data must not be empty!!");
        }

        this.id = id;
        this.defaultActiveValue = defaultActiveValue == null ? ""
                : defaultActiveValue;
        this.defaultInactiveValue = defaultInactiveValue == null ? ""
                : defaultInactiveValue;
        this.defaultUnreplacedValue = defaultUnreplacedValue == null ? ""
                : defaultUnreplacedValue;
        this.data = data;
        this.defaultActiveValueRaw = defaultActiveValueRaw == null ? ""
                : defaultActiveValueRaw;
        this.defaultInactiveValueRaw = defaultInactiveValueRaw == null ? ""
                : defaultInactiveValueRaw;
        this.defaultUnreplacedValueRaw = defaultUnreplacedValueRaw == null ? ""
                : defaultUnreplacedValueRaw;
        this.dataRaw = dataRaw;
        this.cropCData = cropCData;
    }

    /**
     * @see de.groth.dts.api.core.dto.IInsertionPattern#getId()
     */
    public String getId() {
        return this.id;
    }

    /**
     * @see de.groth.dts.api.core.dto.IInsertionPattern#getDefaultActiveValue()
     */
    public String getDefaultActiveValue() {
        return this.cropCData ? this.defaultActiveValue
                : this.defaultActiveValueRaw;
    }

    /**
     * @see de.groth.dts.api.core.dto.IInsertionPattern#getDefaultInactiveValue()
     */
    public String getDefaultInactiveValue() {
        return this.cropCData ? this.defaultInactiveValue
                : this.defaultInactiveValueRaw;
    }

    /**
     * @see de.groth.dts.api.core.dto.IInsertionPattern#getData()
     */
    public String getData() {
        return this.cropCData ? this.data : this.dataRaw;
    }

    /**
     * @see de.groth.dts.api.core.dto.IInsertionPattern#getDefaultActiveValueRaw()
     */
    public String getDefaultActiveValueRaw() {
        return this.defaultActiveValueRaw;
    }

    /**
     * @see de.groth.dts.api.core.dto.IInsertionPattern#getDefaultInactiveValueRaw()
     */
    public String getDefaultInactiveValueRaw() {
        return this.defaultInactiveValueRaw;
    }

    /**
     * @see de.groth.dts.api.core.dto.IInsertionPattern#getDataRaw()
     */
    public String getDataRaw() {
        return this.dataRaw;
    }

    /**
     * @see de.groth.dts.api.core.dto.IInsertionPattern#getDefaultUnreplacedValue()
     */
    public String getDefaultUnreplacedValue() {
        return this.cropCData ? this.defaultUnreplacedValue
                : this.defaultUnreplacedValueRaw;
    }

    /**
     * @see de.groth.dts.api.core.dto.IInsertionPattern#getDefaultUnreplacedValueRaw()
     */
    public String getDefaultUnreplacedValueRaw() {
        return this.defaultUnreplacedValueRaw;
    }

    /**
     * @see de.groth.dts.api.core.dto.IInsertionPattern#isCropCData()
     */
    public boolean isCropCData() {
        return this.cropCData;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof IInsertionPattern)) {
            return false;
        }

        final IInsertionPattern cast = (IInsertionPattern) obj;
        return this.id.equals(cast.getId());
    }

    private static final int HASH_CODE_BASE = 17;

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return InsertionPattern.HASH_CODE_BASE * this.id.hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "InsertionPattern: id=" + this.id + ", defaultActiveInsertion="
                + this.defaultActiveValue + ", defaultInactiveInsertion="
                + this.defaultInactiveValue + ", defaultUnreplacesInsertion="
                + this.defaultUnreplacedValue;
    }

    /**
     * @see de.groth.dts.api.xml.IDtsDtoXmlHandlerProvider#createXmlHandler(de.groth.dts.api.core.dao.DynamicTemplateSystemExecutionContext)
     */
    public IDtsDtoXmlHandler<IInsertionPattern> createXmlHandler(
            final DynamicTemplateSystemExecutionContext context) {
        return new InsertionPatternXmlHandler();
    }
}
