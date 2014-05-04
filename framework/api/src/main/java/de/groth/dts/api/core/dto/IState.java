package de.groth.dts.api.core.dto;

import java.util.HashMap;

import de.groth.dts.api.core.exception.dto.UnknownStateValueException;
import de.groth.dts.api.xml.IDtsDtoXmlHandlerProvider;

/**
 * An instance of {@link IState} represents the xml-declaration of one state in
 * your dts-xml file.
 * 
 * @author Christian Groth
 */
public interface IState extends IDtsDto, IDtsDtoXmlHandlerProvider<IState> {
    /**
     * Returns the stateId
     * 
     * @return the stateId
     */
    String getId();

    /**
     * Indicates if possible CData tags will be cropped on value resolving.
     * 
     * @return true if CData will be cropped
     */
    boolean isCropCData();

    /**
     * Returns the full state value for given condition. This means using pre
     * and post while considering cropCdata flag.
     * 
     * @param condition
     *                the given condition
     * @return the full value (without CData)
     * @throws UnknownStateValueException
     *                 if condition is unknown and no default is specified
     */
    String getValue(final String condition) throws UnknownStateValueException;

    /**
     * Returns the full state value for given condition. This means using pre
     * and post but not considering cropCdata flag (as defined in xml).
     * 
     * @param condition
     *                the given condition
     * @return the full value (as defined in xml)
     * @throws UnknownStateValueException
     *                 if condition is unknown and no default is specified
     */
    String getRawValue(final String condition)
            throws UnknownStateValueException;

    /**
     * Returns the pre-value (without CData)
     * 
     * @return pre-value (without CData)
     */
    String getPre();

    /**
     * Returns the post-value (without CData)
     * 
     * @return post-value (without CData)
     */
    String getPost();

    /**
     * Returns the default value (without CData)
     * 
     * @return default value (without CData)
     */
    String getDefaultValue();

    /**
     * Returns the value to the given given condition without pre or post
     * (without CData)
     * 
     * @param condition
     *                condition for value
     * @return value without pre or post (without CData)
     * @throws UnknownStateValueException
     */
    String getConditionValue(final String condition)
            throws UnknownStateValueException;

    /**
     * Returns all condition values (without CData).
     * 
     * @return a condition-value map (without CData)
     */
    HashMap<String, String> getConditionValues();

    /**
     * Returns the pre value (as defined in xml)
     * 
     * @return pre value (as defined in xml)
     */
    String getRawPre();

    /**
     * Returns the post value (as defined in xml)
     * 
     * @return post value (as defined in xml)
     */
    String getRawPost();

    /**
     * Returns the default value (as defined in xml)
     * 
     * @return default value (as defined in xml)
     */
    String getRawDefaultValue();

    /**
     * Returns the value to the given given condition without pre or post (as
     * defined in xml)
     * 
     * @param condition
     *                condition for value
     * @return value without pre or post (as defined in xml)
     * @throws UnknownStateValueException
     */
    String getRawConditionValue(final String condition)
            throws UnknownStateValueException;

    /**
     * Returns all condition values (as defined in xml)
     * 
     * @return a condition-value map (as defined in xml)
     */
    HashMap<String, String> getRawConditionValues();
}
