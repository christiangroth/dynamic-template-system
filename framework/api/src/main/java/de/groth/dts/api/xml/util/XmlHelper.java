package de.groth.dts.api.xml.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.dom.DOMText;
import org.dom4j.tree.DefaultAttribute;
import org.dom4j.tree.DefaultCDATA;
import org.dom4j.tree.DefaultComment;
import org.dom4j.tree.DefaultElement;
import org.dom4j.tree.DefaultText;

import de.groth.dts.api.core.dto.IDtsDto;
import de.groth.dts.api.core.exception.dto.DtoInitializationException;
import de.groth.dts.api.xml.IDtsDtoXmlHandler;
import de.groth.dts.api.xml.exception.XmlException;

/**
 * General Helper for XML purposes.
 * 
 * @param <T>
 *                the concrete {@link IDtsDto} to be handled
 * 
 * @author Christian Groth
 * 
 */
public class XmlHelper<T extends IDtsDto> {
    private static final Logger LOGGER = Logger.getLogger(XmlHelper.class);

    /**
     * Xml comment prefix
     */
    public static final String DEFINITION_COMMENT_PRE = "<!--";

    /**
     * Xml comment postfix
     */
    public static final String DEFINITION_COMMENT_POST = "-->";

    /**
     * Xml cdata prefix
     */
    public static final String DEFINITION_CDATA_PRE = "<![CDATA[";

    /**
     * Xml cdata postfix
     */
    public static final String DEFINITION_CDATA_POST = "]]>";

    /**
     * Xml cdata prefix (escaped for regex purposes)
     */
    public static final String DEFINITION_CDATA_PRE_ESCAPED = "<!\\[CDATA\\[";

    /**
     * Xml cdata postfix (escaped for regex purposes)
     */
    public static final String DEFINITION_CDATA_POST_ESCAPED = "\\]\\]>";

    /**
     * Xml representation of boolean value true (String)
     */
    public static final String BOOLEAN_AS_STRING_TRUE_SHORT = "y";

    /**
     * Xml representation of boolean value true (String)
     */
    public static final String BOOLEAN_AS_STRING_TRUE_WORD = "yes";

    /**
     * Xml representation of boolean value true (Integer)
     */
    public static final String BOOLEAN_AS_STRING_TRUE_NUMERIC = "1";

    /**
     * Xml representation of boolean value false (Integer)
     */
    public static final String BOOLEAN_AS_STRING_FALSE_NUMERIC = "0";

    /**
     * Gets the node content as string.
     * 
     * @param node
     *                the {@link Node}
     * @param nodePath
     *                the node path
     * 
     * @return the node content as string
     * 
     * @throws XmlException
     *                 the xml exception
     */
    public static String nodeContentAsString(final Node node,
            final String nodePath) throws XmlException {
        return XmlHelper.nodeContentAsStringInternal(node, nodePath);
    }

    /**
     * Gets the node content as string and crops CData.
     * 
     * @param node
     *                the {@link Node}
     * @param nodePath
     *                the node path
     * 
     * @return the node content as string and cropped cdata
     * 
     * @throws XmlException
     *                 the xml exception
     */
    public static String nodeContentAsStringAndCropCData(final Node node,
            final String nodePath) throws XmlException {
        return XmlHelper.cropCData(XmlHelper.nodeContentAsStringInternal(node,
                nodePath));
    }

    private static String nodeContentAsStringInternal(final Node node,
            final String nodePath) throws XmlException {
        XmlHelper.LOGGER.debug("retrieving nodeContent as string for node "
                + node.getPath());

        if (!node.hasContent()) {
            throw new XmlException("No content for node: " + node.getPath()
                    + " !!");
        }

        String content = "";

        try {
            final DefaultElement def = (DefaultElement) node
                    .selectObject(nodePath);
            final List<?> contentList = def.content();

            XmlHelper.LOGGER
                    .debug("iterating nodes childs and appending content");
            for (final Object o : contentList) {
                if (o instanceof DefaultElement) {
                    final DefaultElement defEl = (DefaultElement) o;
                    content += defEl.asXML();
                } else if (o instanceof DOMText) {
                    final DOMText domT = (DOMText) o;
                    content += domT.getText();
                } else if (o instanceof DefaultText) {
                    final DefaultText defT = (DefaultText) o;
                    content += defT.getText();
                } else if (o instanceof DefaultComment) {
                    final DefaultComment defC = (DefaultComment) o;
                    content += defC.asXML();
                } else if (o instanceof DefaultCDATA) {
                    final DefaultCDATA defCd = (DefaultCDATA) o;
                    content += defCd.asXML();
                } else {
                    XmlHelper.LOGGER.warn("Perhaps missing some information: "
                            + o);
                }
            }

            XmlHelper.LOGGER.debug("dumping content");
            XmlHelper.LOGGER.debug(content);
        } catch (final ClassCastException ex) {
            // if no node is found a ClassCastException is thrown
            // java.util.Collections$EmptyList
            final Pattern regex = Pattern
                    .compile("^(.*)java\\.util\\.Collections\\$EmptyList$");
            final Matcher matcher = regex.matcher(ex.getMessage());

            if (matcher.matches()) {
                XmlHelper.LOGGER
                        .debug("caught ClassCastException: no nodes available");
            } else {
                throw ex;
            }
        }

        return content.trim();
    }

    /**
     * Cropping cdata tags on given string.
     * 
     * @param content
     *                the content to be cropped
     * @return content without cdata
     */
    public static String cropCData(final String content) {
        XmlHelper.LOGGER.debug("cropping CData on following string ...");
        XmlHelper.LOGGER.debug(content);
        String result = content;

        Pattern regex = Pattern.compile(XmlHelper.DEFINITION_CDATA_PRE_ESCAPED);
        Matcher matcher = regex.matcher(result);
        result = matcher.replaceAll("");

        regex = Pattern.compile(XmlHelper.DEFINITION_CDATA_POST_ESCAPED);
        matcher = regex.matcher(result);
        result = matcher.replaceAll("");

        XmlHelper.LOGGER.debug("dumping content after cropCData");
        XmlHelper.LOGGER.debug(result);

        return result;
    }

    /**
     * Iterates over given nodes and collects the contents in an array.
     * 
     * @param node
     *                the {@link Node}
     * @param nodePath
     *                the node path
     * 
     * @return the nodes content as string
     * 
     * @throws XmlException
     *                 the xml exception
     */
    public static ArrayList<String> nodesContentAsString(final Node node,
            final String nodePath) throws XmlException {
        final ArrayList<String> contents = new ArrayList<String>();

        final List<?> nodeList = node.selectNodes(nodePath);
        for (final Object nodeObject : nodeList) {
            final Node aNode = (Node) nodeObject;
            contents.add(XmlHelper.nodeContentAsString(aNode, "."));
        }

        return contents;
    }

    /**
     * Evaluates the nodePath and returns the found value.
     * 
     * @param node
     *                the {@link Node}
     * @param nodePath
     *                the node path
     * @param value
     *                the value
     * 
     * @return the nodes custom value as string
     */
    public static ArrayList<String> nodesCustomValueAsString(final Node node,
            final String nodePath, final String value) {
        XmlHelper.LOGGER.debug("selecting custom values on " + nodePath
                + " currently at " + node.getPath());
        final ArrayList<String> contents = new ArrayList<String>();

        final List<?> nodeList = node.selectNodes(nodePath);
        for (final Object nodeObject : nodeList) {
            final Node aNode = (Node) nodeObject;

            XmlHelper.LOGGER.debug("retrieving value " + value + " on node "
                    + aNode.getPath() + ". dumping contetn afterwards ");
            String content = aNode.valueOf(value);
            if (content != null) {
                content = content.trim();
            }
            XmlHelper.LOGGER.debug(content);
            contents.add(content);
        }

        return contents;
    }

    /**
     * String to boolean.
     * 
     * @param value
     *                the value
     * 
     * @return true, if successful
     */
    public static boolean stringToBoolean(final String value) {
        if (value == null || value.trim().equals("")) {
            XmlHelper.LOGGER.debug(value + " is false");
            return false;
        }

        final String trimmedValue = value.trim();
        if (XmlHelper.BOOLEAN_AS_STRING_TRUE_SHORT
                .equalsIgnoreCase(trimmedValue)
                || XmlHelper.BOOLEAN_AS_STRING_TRUE_WORD
                        .equalsIgnoreCase(trimmedValue)
                || XmlHelper.BOOLEAN_AS_STRING_TRUE_NUMERIC
                        .equalsIgnoreCase(trimmedValue)) {
            XmlHelper.LOGGER.debug(value + " is false");
            return true;
        }

        XmlHelper.LOGGER.debug(value + " is false");
        return false;
    }

    /**
     * Boolean to string.
     * 
     * @param value
     *                the value
     * 
     * @return the string
     */
    public static String booleanToString(final boolean value) {
        final String result = value ? XmlHelper.BOOLEAN_AS_STRING_TRUE_NUMERIC
                : XmlHelper.BOOLEAN_AS_STRING_FALSE_NUMERIC;

        XmlHelper.LOGGER.debug(value + " becomes " + result);
        return result;
    }

    /**
     * Returns the value of given attribute name. You can leave away the '@'
     * which is needed for xpath expression.
     * 
     * @param node
     *                the {@link Node} to be queried
     * @param attributeName
     *                name of the attribute (if you like, without '@')
     * @return the trimmed attribute value
     */
    public static String nodeAttributeValue(final Node node,
            final String attributeName) {
        final String prefix = attributeName.indexOf('@') >= 0 ? "" : "@";
        XmlHelper.LOGGER.debug("resolving node attribute value for attribute '"
                + attributeName + "' with extra prefix '" + prefix
                + "' and node " + node.asXML());
        return node.valueOf(prefix + attributeName).trim();
    }

    /**
     * Invokes the given reader on all found nodes at given nodePath.
     * 
     * @param node
     *                the {@link Node}
     * @param reader
     *                an {@link IDtsDtoXmlHandler}
     * @param nodePath
     *                the node path
     * 
     * @return the nodes as {@link IDtsDto} (concrete type depends on given
     *         {@link IDtsDtoXmlHandler}
     * 
     * @throws XmlException
     *                 the xml exception
     */
    public ArrayList<T> nodesAsDtos(final Node node,
            final IDtsDtoXmlHandler<T> reader, final String nodePath)
            throws XmlException {
        XmlHelper.LOGGER.debug("creating dtos from nodes " + nodePath
                + " currently at " + node.getPath());
        final ArrayList<T> dtoList = new ArrayList<T>();
        final List<?> nodeList = node.selectNodes(nodePath);

        XmlHelper.LOGGER.debug("created nodeList");
        for (final Object obj : nodeList) {
            try {
                final Node dtoNode = (Node) obj;
                XmlHelper.LOGGER.debug("invoking xmlHandler for node "
                        + node.getPath());
                final T dto = reader.fromXml(dtoNode);
                dtoList.add(dto);
            } catch (final DtoInitializationException ex) {
                throw new XmlException("caught DtoInitializationException!!",
                        ex);
            }
        }

        XmlHelper.LOGGER.debug("created " + dtoList.size() + " dtos");
        return dtoList;
    }

    /**
     * Creates a new node with the given nodeContent as body-content. The String
     * must be valid xml!!
     * 
     * @param nodeName
     *                name of the node to be created
     * @param nodeContent
     *                valid xml-content of the node to be created
     * @return the created node
     * @throws XmlException
     *                 in case the nodeContent is not valid xml
     */
    public static Element createNodeFromString(final String nodeName,
            final String nodeContent) throws XmlException {
        XmlHelper.LOGGER.debug("creating new node with name " + nodeName
                + " and content " + nodeContent);
        try {
            return DocumentHelper.parseText(
                    "<" + nodeName + ">" + nodeContent + "</" + nodeName + ">")
                    .getRootElement();
        } catch (final DocumentException ex) {
            throw new XmlException("unable to create node with name "
                    + nodeName + " and given content " + nodeContent + "!!", ex);
        }
    }

    /**
     * Removes the given oldElement from its parent and adds the given new
     * element. Additionally all attributes from oldElement are copied to
     * newElement.
     * 
     * @param oldElement
     *                the old element to be removed from parent
     * @param newElement
     *                the new element to be added to parent
     * @return the new element
     */
    @SuppressWarnings("unchecked")
    public static Element changeNode(final Element oldElement,
            final Element newElement) {
        XmlHelper.LOGGER.debug("replacing element "
                + oldElement.getStringValue() + " with "
                + newElement.getStringValue());
        final List<DefaultAttribute> originalAttributes = oldElement
                .attributes();
        final Element parent = oldElement.getParent();

        for (final DefaultAttribute attribute : originalAttributes) {
            XmlHelper.LOGGER.debug("copying attribute " + attribute.getName()
                    + "=" + attribute.getValue());
            newElement.addAttribute(attribute.getName(), attribute.getValue());
        }

        parent.remove(oldElement);
        parent.add(newElement);

        return newElement;
    }
}
