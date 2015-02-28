package com.digiturtle.dxml;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Encapsulates the reading and parsing of an XML file while hiding the core DOM objects
 * @author Jonathan
 */
public class DReader {
	
	/** Stores the active DOM Document */
	private Document document;
	
	/** Stores the parent tags and the tree structure of each */
	private ArrayList<DTag> tags = new ArrayList<DTag>();
	
	/** Input source */
	private InputStream stream;
	
	/**
	 * Construct the XML reader for a given file
	 * @param stream Access to file/input
	 * @throws Exception 
	 */
	public DReader(InputStream stream) throws Exception {
		this.stream = stream;
		reloadStructure();
	}
	
	/**
	 * Reload this XML file by regenerating its structure
	 * @throws Exception
	 */
	public void reloadStructure() throws Exception {
		tags.clear();
		// Load the file
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		document = builder.parse(stream);
		document.getDocumentElement().normalize();
		// Read the Tags
		NodeList nodes = document.getChildNodes();
		for (int index = 0; index < nodes.getLength(); index++) {
			Node node = nodes.item(index);
			if (node instanceof Element) {
				tags.add(parseTree((Element) node));
			}
		}
	}
	
	/**
	 * Save an XML file
	 * @param output Output access
	 * @throws Exception
	 */
	public void save(OutputStream output) throws Exception {
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(output);
		transformer.transform(source, result);
	}
	
	/**
	 * Add a child to the structure. Contamination is undone through reloadStructure()
	 * @param tag Child to append to
	 * @param tagName New tag name
	 * @param attributes Tag attributes. Null for no attributes
	 */
	public void addChild(DTag tag, String tagName, HashMap<String, String> attributes) {
		Element element = document.createElement(tagName);
		if (attributes != null) {
			for (Entry<String, String> entry : attributes.entrySet()) {
				element.setAttribute(entry.getKey(), entry.getValue());
			}
		}
		tag.getElement().appendChild(element);
	}
	
	/**
	 * Get all the parent tags
	 * @return Parent tags
	 */
	public ArrayList<DTag> getParents() {
		return tags;
	}
	
	private DTag parseTree(Element element) {
		DTag tag = new DTag(element);
		NodeList nodes = element.getChildNodes();
		for (int index = 0; index < nodes.getLength(); index++) {
			Node node = nodes.item(index);
			if (node instanceof Element) {
				DTag child = parseTree((Element) node);
				tag.addChild(child);
			}
		}
		return tag;
	}

}
