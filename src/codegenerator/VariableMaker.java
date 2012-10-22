package codegenerator;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;


public class VariableMaker {

	public static void addSetMethod(Document doc, String name, String type)
	{
		
		Node blockGenuses = doc.getElementsByTagName("BlockGenuses").item(0);
		
		Element blockGenus = doc.createElement("BlockGenus");
		blockGenus.setAttribute("name", "set_"+name);
		blockGenus.setAttribute("kind", "command");
		blockGenus.setAttribute("initlabel", name);
		blockGenus.setAttribute("label-unique", "yes");
		blockGenus.setAttribute("label-prefix", "set ");
		blockGenus.setAttribute("is-label-value", "yes");
		blockGenus.setAttribute("color", "230 0 255");
		
		Element blockConnectors = doc.createElement("BlockConnectors");
		Element blokConnector = doc.createElement("BlockConnector");
		blokConnector.setAttribute("connector-kind", "socket");
		blokConnector.setAttribute("connector-type", type);
		blokConnector.setAttribute("position-type", "single");
		
		blockConnectors.appendChild(blokConnector);
		blockGenus.appendChild(blockConnectors);
		blockGenuses.appendChild(blockGenus);
		
		NodeList nodeList = doc.getElementsByTagName("BlockFamily");
		for (int i = 0; i < nodeList.getLength(); i ++) {
			Element element = (Element) nodeList.item(i);
			if( element.getAttribute("type").equals("set_"+type))
			{
				Element familyMember = doc.createElement("FamilyMember");
				familyMember.appendChild(doc.createTextNode("set_"+name));
				element.appendChild(familyMember);
			}
		}
	}
	
	public static void addReSetMethod(Document doc, String name, String type)
	{
		
		Node blockGenuses = doc.getElementsByTagName("BlockGenuses").item(0);
		
		Element blockGenus = doc.createElement("BlockGenus");
		blockGenus.setAttribute("name", "reset_"+name);
		blockGenus.setAttribute("kind", "command");
		blockGenus.setAttribute("initlabel", name);
		blockGenus.setAttribute("label-unique", "yes");
		blockGenus.setAttribute("label-prefix", "reset ");
		blockGenus.setAttribute("is-label-value", "yes");
		blockGenus.setAttribute("color", "230 0 255");
		
		/*Element blockConnectors = doc.createElement("BlockConnectors");
		Element blokConnector = doc.createElement("BlockConnector");
		blokConnector.setAttribute("connector-kind", "socket");
		blokConnector.setAttribute("connector-type", type);
		blokConnector.setAttribute("position-type", "single");
		
		blockConnectors.appendChild(blokConnector);
		blockGenus.appendChild(blockConnectors);*/
		blockGenuses.appendChild(blockGenus);
		
		NodeList nodeList = doc.getElementsByTagName("BlockFamily");
		for (int i = 0; i < nodeList.getLength(); i ++) {
			Element element = (Element) nodeList.item(i);
			if( element.getAttribute("type").equals("reset_"+type))
			{
				Element familyMember = doc.createElement("FamilyMember");
				familyMember.appendChild(doc.createTextNode("reset_"+name));
				element.appendChild(familyMember);
			}
		}
	}
	
	
	public static String addVariable(Document doc, String name, String type)
	{
		//String pattern = "[^a-zA-Z_]+|[^a-zA-Z_0-9]+//g";
		//name = name.replaceAll(pattern, "");
		name = Variable.getValidVariableName(name);
		String xmlString = null;
		Node blockGenuses = doc.getElementsByTagName("BlockGenuses").item(0);
		
		Element blockGenus = doc.createElement("BlockGenus");
		blockGenus.setAttribute("name", name);
		blockGenus.setAttribute("kind", "data");
		blockGenus.setAttribute("initlabel", name);
		blockGenus.setAttribute("label-unique", "yes");
		blockGenus.setAttribute("is-label-value", "yes");
		blockGenus.setAttribute("color", "230 0 255");
		
		Element blockConnectors = doc.createElement("BlockConnectors");
		Element blokConnector = doc.createElement("BlockConnector");
		blokConnector.setAttribute("connector-kind", "plug");
		blokConnector.setAttribute("connector-type", type);
		blokConnector.setAttribute("position-type", "mirror");
		
		blockConnectors.appendChild(blokConnector);
		blockGenus.appendChild(blockConnectors);
		blockGenuses.appendChild(blockGenus);
		
		/*NodeList blockDrawers = doc.getElementsByTagName("BlockDrawer");
		for (int i = 0; i < blockDrawers.getLength(); i ++) {
			Element blockDrawer = (Element)blockDrawers.item(i);
			if(blockDrawer.getAttribute("name").equals("Variables"))
			{
				Element blockGenusMember = doc.createElement("BlockGenusMember");
				blockGenusMember.appendChild(doc.createTextNode(name));
				blockDrawer.appendChild(blockGenusMember);
			}
		}*/
		
		NodeList nodeList = doc.getElementsByTagName("BlockFamily");
		for (int i = 0; i < nodeList.getLength(); i ++) {
			Element element = (Element) nodeList.item(i);
			if( element.getAttribute("type").equals(type))
			{
				Element familyMember = doc.createElement("FamilyMember");
				familyMember.appendChild(doc.createTextNode(name));
				element.appendChild(familyMember);
			}
		}
		
		addSetMethod(doc, name, type);
		addReSetMethod(doc, name, type);
		if(type.equals("integer") || type.equals("double"))
		{
			addInc(doc, name, type);
			addDec(doc, name, type);
		}
		
		try
		{
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	
			//initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);
	
			xmlString = result.getWriter().toString();
			//System.out.println(xmlString);
		
		}
		catch(Exception ex)
		{
			
		}
		//System.out.println(xmlString);
		return xmlString;
	}
	
	public static void addInc(Document doc, String name, String type)
	{
		Node blockGenuses = doc.getElementsByTagName("BlockGenuses").item(0);
		
		Element blockGenus = doc.createElement("BlockGenus");
		blockGenus.setAttribute("name", "inc_"+name);
		blockGenus.setAttribute("kind", "command");
		blockGenus.setAttribute("initlabel", name);
		blockGenus.setAttribute("label-suffix", "++");
		//blockGenus.setAttribute("label-unique", "yes");
		//blockGenus.setAttribute("is-label-value", "yes");
		blockGenus.setAttribute("color", "230 0 255");
		
		//Element blockConnectors = doc.createElement("BlockConnectors");
		//Element blokConnector = doc.createElement("BlockConnector");
		//blokConnector.setAttribute("connector-kind", "socket");
		//blokConnector.setAttribute("connector-type", type);
		//blokConnector.setAttribute("position-type", "single");
		
		//blockConnectors.appendChild(blokConnector);
		//blockGenus.appendChild(blockConnectors);
		blockGenuses.appendChild(blockGenus);
		
		NodeList nodeList = doc.getElementsByTagName("BlockFamily");
		for (int i = 0; i < nodeList.getLength(); i ++) {
			Element element = (Element) nodeList.item(i);
			if( element.getAttribute("type").equals("inc_"+type))
			{
				Element familyMember = doc.createElement("FamilyMember");
				familyMember.appendChild(doc.createTextNode("inc_"+name));
				element.appendChild(familyMember);
			}
		}
	}
	
	public static void addDec(Document doc, String name, String type)
	{
		Node blockGenuses = doc.getElementsByTagName("BlockGenuses").item(0);
		
		Element blockGenus = doc.createElement("BlockGenus");
		blockGenus.setAttribute("name", "dec_"+name);
		blockGenus.setAttribute("kind", "command");
		blockGenus.setAttribute("initlabel", name);
		blockGenus.setAttribute("label-suffix", "--");
		//blockGenus.setAttribute("label-unique", "yes");
		//blockGenus.setAttribute("is-label-value", "yes");
		blockGenus.setAttribute("color", "230 0 255");
		
		//Element blockConnectors = doc.createElement("BlockConnectors");
		//Element blokConnector = doc.createElement("BlockConnector");
		//blokConnector.setAttribute("connector-kind", "socket");
		//blokConnector.setAttribute("connector-type", type);
		//blokConnector.setAttribute("position-type", "single");
		
		//blockConnectors.appendChild(blokConnector);
		//blockGenus.appendChild(blockConnectors);
		blockGenuses.appendChild(blockGenus);
		
		NodeList nodeList = doc.getElementsByTagName("BlockFamily");
		for (int i = 0; i < nodeList.getLength(); i ++) {
			Element element = (Element) nodeList.item(i);
			if( element.getAttribute("type").equals("dec_"+type))
			{
				Element familyMember = doc.createElement("FamilyMember");
				familyMember.appendChild(doc.createTextNode("dec_"+name));
				element.appendChild(familyMember);
			}
		}
	}
	
	public static Document getXMLDocument()
	{
		Document doc = null;
		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.parse("support/lang_def.xml");
		}
		catch(Exception ex)
		{
			
		}
		return doc;
	}
}
