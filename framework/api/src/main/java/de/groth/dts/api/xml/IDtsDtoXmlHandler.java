package de.groth.dts.api.xml;

import org.dom4j.Element;
import org.dom4j.Node;

import de.groth.dts.api.core.dto.IDtsDto;
import de.groth.dts.api.core.exception.dto.DtoInitializationException;
import de.groth.dts.api.xml.exception.XmlException;

/**
 * Defines a general interface for writing instances of type {@link IDtsDto} to
 * xml and construct them from given xml node using dom4j.
 * 
 * @param <T>
 *                the concrete {@link IDtsDto} to be handled
 * 
 * @author Christian Groth
 * 
 */
public interface IDtsDtoXmlHandler<T extends IDtsDto> {
    /**
     * Constructs an instance from the given xml node
     * 
     * @param node
     *                base xml node to be used to for new instance
     * @return instantiated object
     * @throws DtoInitializationException
     */
    T fromXml(Node node) throws DtoInitializationException;

    /**
     * Converts the given instance into the xml representation by creating and
     * returning a new xml node
     * 
     * @param instance
     *                the instance to be converted to xml node
     * @param parent
     *                the parent xml element to add your new xml data
     * @throws XmlException
     */
    void toXml(T instance, Element parent) throws XmlException;
}
