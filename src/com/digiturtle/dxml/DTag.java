package com.digiturtle.dxml;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Represents an XML opening tag
 * @author Jonathan
 */
public class DTag {
	
	/** Map of tag attributes */
	private HashMap<String, String> attributes = new HashMap<String, String>();
	
	/** Tag name */
	private String tag;
	
	/** Content of this tag and it's closer */
	private String textContent;
	
	/** Collection of nested nodes */
	private ArrayList<DTag> children = new ArrayList<DTag>();
	
	/** Encapsulates the DOM element */
	private Element element;
	
	/**
	 * Construct a DTag object
	 * @param element XML Element
	 */
	public DTag(Element element) {
		this.element = element;
		textContent = element.getTextContent();
		NamedNodeMap map = element.getAttributes();
		for (int index = 0; index < map.getLength(); index++) {
			Node node = map.item(index);
			attributes.put(node.getNodeName(), node.getNodeValue());
		}
		tag = element.getTagName();
	}
	
	/**
	 * Get the DOM element
	 * @return Underlying DOM element
	 */
	protected Element getElement() {
		return element;
	}
	
	/**
	 * See if this attribute exists
	 * @param name Attribute
	 * @return Whether it exists
	 */
	public boolean hasAttribute(String name) {
		return attributes.containsKey(name);
	}
	
	/**
	 * Get an attribute's value
	 * @param name Attribute name
	 * @return Attribute value
	 */
	public String getAttribute(String name) {
		return attributes.get(name);
	}
	
	/**
	 * Get this element's tag name
	 * @return Element tag name
	 */
	public String getTag() {
		return tag;
	}
	
	/**
	 * Get this element's text content
	 * @return Text content
	 */
	public String getContent() {
		return textContent;
	}
	
	/**
	 * Add a child tag
	 * @param tag Child tag
	 */
	public void addChild(DTag tag) {
		children.add(tag);
	}
	
	/**
	 * Get this node's children
	 * @return Node children
	 */
	public ArrayList<DTag> getChildren() {
		return children;
	}
	
	/**
	 * Get this node's children with the given tag name
	 * @param tag Tag name
	 * @return Node children
	 */
	public ArrayList<DTag> getChildren(String tag) {
		ArrayList<DTag> matches = new ArrayList<DTag>();
		for (DTag child : children) {
			if (child.getTag().equalsIgnoreCase(tag)) {
				matches.add(child);
			}
		}
		return matches;
	}
	
	/**
	 * Get this node's children with the given attribute
	 * @param attribute Attribute name
	 * @param value Attribute value
	 * @return Node children
	 */
	public ArrayList<DTag> getChildren(String attribute, String value) {
		ArrayList<DTag> matches = new ArrayList<DTag>();
		for (DTag child : children) {
			if (child.hasAttribute(attribute) && child.getAttribute(attribute).equalsIgnoreCase(value)) {
				matches.add(child);
			}
		}
		return matches;
	}

}
