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
/**
 * this is a class to make a variable
 * and add in the langdef.xml file
 * so that we can save and upload the variable
 * */

public class VariableMaker {

	public static void addSetMethod(Document doc, String name, String type)
	{
		/**
		 * adding set method in the langdef.xml file
		 * and it's properties
		 * */
		Node blockGenuses = doc.getElementsByTagName("BlockGenuses").item(0);
		
		Element blockGenus = doc.createElement("BlockGenus");
		blockGenus.setAttribute("name", "set_"+name);
		blockGenus.setAttribute("kind", "command");
		blockGenus.setAttribute("initlabel", name);
		blockGenus.setAttribute("label-unique", "yes");
		blockGenus.setAttribute("label-prefix", "set ");
		blockGenus.setAttribute("is-label-value", "yes");
		blockGenus.setAttribute("color", "230 0 255");
		blockGenus.setAttribute("editable-label", "no");
		
		Element blockConnectors = doc.createElement("BlockConnectors");
		Element blokConnector = doc.createElement("BlockConnector");
		blokConnector.setAttribute("connector-kind", "socket");
		blokConnector.setAttribute("connector-type", type);
		blokConnector.setAttribute("position-type", "single");
		blokConnector.setAttribute("is-expandable", "no");
		blokConnector.setAttribute("label-editable", "no");
		blokConnector.setAttribute("has-range", "no");
		blokConnector.setAttribute("has-length", "no");
		
		blockConnectors.appendChild(blokConnector);
		blockGenus.appendChild(blockConnectors);
		blockGenuses.appendChild(blockGenus);
		
		NodeList blockDrawers = doc.getElementsByTagName("BlockDrawer");
		for (int i = 0; i < blockDrawers.getLength(); i ++) {
			Element blockDrawer = (Element)blockDrawers.item(i);
			if(blockDrawer.getAttribute("name").equals("Variables"))
			{
				Element blockGenusMember = doc.createElement("BlockGenusMember");
				
				blockGenusMember.appendChild(doc.createTextNode("set_"+name));
				blockDrawer.appendChild(blockGenusMember);
				
				break;
			}
		}
	}
	
	public static void addReSetMethod(Document doc, String name, String type)
	{
		/**
		 * adding reset method in the langdef.xml file
		 * and it's properties
		 * */
		Node blockGenuses = doc.getElementsByTagName("BlockGenuses").item(0);
		
		Element blockGenus = doc.createElement("BlockGenus");
		blockGenus.setAttribute("name", "reset_"+name);
		blockGenus.setAttribute("kind", "command");
		blockGenus.setAttribute("initlabel", name);
		blockGenus.setAttribute("label-unique", "yes");
		blockGenus.setAttribute("label-prefix", "reset ");
		blockGenus.setAttribute("is-label-value", "yes");
		blockGenus.setAttribute("color", "230 0 255");
		blockGenus.setAttribute("editable-label", "no");
		
		/*Element blockConnectors = doc.createElement("BlockConnectors");
		Element blokConnector = doc.createElement("BlockConnector");
		blokConnector.setAttribute("connector-kind", "socket");
		blokConnector.setAttribute("connector-type", type);
		blokConnector.setAttribute("position-type", "single");
		
		blockConnectors.appendChild(blokConnector);
		blockGenus.appendChild(blockConnectors);*/
		blockGenuses.appendChild(blockGenus);
		
		/*NodeList nodeList = doc.getElementsByTagName("BlockFamily");
		for (int i = 0; i < nodeList.getLength(); i ++) {
			Element element = (Element) nodeList.item(i);
			if( element.getAttribute("type").equals("reset_"+type))
			{
				Element familyMember = doc.createElement("FamilyMember");
				familyMember.appendChild(doc.createTextNode("reset_"+name));
				element.appendChild(familyMember);
			}
		}*/
		
		/*NodeList nodeList = doc.getElementsByTagName("BlockFamily");
		for (int i = 0; i < nodeList.getLength(); i ++) {
			Element element = (Element) nodeList.item(i);
			if( element.getAttribute("type").equals("reset_"+type))
			{
				Element familyMember = doc.createElement("FamilyMember");
				familyMember.appendChild(doc.createTextNode("reset_"+name));
				element.appendChild(familyMember);
			}
		}*/
		
		NodeList blockDrawers = doc.getElementsByTagName("BlockDrawer");
		for (int i = 0; i < blockDrawers.getLength(); i ++) {
			Element blockDrawer = (Element)blockDrawers.item(i);
			if(blockDrawer.getAttribute("name").equals("Variables"))
			{
				Element blockGenusMember = doc.createElement("BlockGenusMember");
				
				blockGenusMember.appendChild(doc.createTextNode("reset_"+name));
				blockDrawer.appendChild(blockGenusMember);
				
				break;
			}
		}
		
	}
	
	
	public static String addVariable(Document doc, String name, String type)
	{
		/**
		 * adding variable in the langdef.xml file
		 * and it's properties
		 * */
		//String pattern = "[^a-zA-Z_]+|[^a-zA-Z_0-9]+//g";
		//name = name.replaceAll(pattern, "");
		name = Variable.getValidVariableName(name);
		String xmlString = null;
		Node blockGenuses = doc.getElementsByTagName("BlockGenuses").item(0);
		
		
		Element blockGenus = doc.createElement("BlockGenus");
		blockGenus.setAttribute("name", name);
		blockGenus.setAttribute("label-prefix", "get ");
		blockGenus.setAttribute("kind", "data");
		blockGenus.setAttribute("initlabel", name);
		blockGenus.setAttribute("label-unique", "yes");
		blockGenus.setAttribute("is-label-value", "yes");
		blockGenus.setAttribute("color", "230 0 255");
		blockGenus.setAttribute("editable-label", "no");
		
		Element blockConnectors = doc.createElement("BlockConnectors");
		Element blokConnector = doc.createElement("BlockConnector");
		blokConnector.setAttribute("connector-kind", "plug");
		blokConnector.setAttribute("connector-type", type);
		blokConnector.setAttribute("position-type", "mirror");
		blokConnector.setAttribute("is-expandable", "no");
		blokConnector.setAttribute("label-editable", "no");
		blokConnector.setAttribute("has-range", "no");
		blokConnector.setAttribute("has-length", "no");
		
		blockConnectors.appendChild(blokConnector);
		blockGenus.appendChild(blockConnectors);
		blockGenuses.appendChild(blockGenus);
		
		Element blockGenusDecl = doc.createElement("BlockGenus");
		blockGenusDecl.setAttribute("name", name+"_decl");
		blockGenusDecl.setAttribute("kind", "variable");
		blockGenusDecl.setAttribute("label-prefix", type +" ");
		blockGenusDecl.setAttribute("initlabel",  name);
		blockGenusDecl.setAttribute("label-unique", "no");
		blockGenusDecl.setAttribute("is-label-value", "yes");
		blockGenusDecl.setAttribute("color", "65 170 225");
		blockGenusDecl.setAttribute("editable-label", "no");
		
		Element defaultArgument = doc.createElement("DefaultArg");
		defaultArgument.setAttribute("genus-name", type);
		if(type.equals("string"))
		{
			defaultArgument.setAttribute("label", "please add some text here");
		}
		else if(type.equals("boolean"))
		{
			defaultArgument.setAttribute("label", "true");
			defaultArgument.setAttribute("genus-name", "true");
		}
		else if(type.equals("integer"))
		{
			defaultArgument.setAttribute("label", "0");
		}
		else if(type.equals("double"))
		{
			defaultArgument.setAttribute("label", "0.0");
		}
		
		
		Element blockDeclConnectors = doc.createElement("BlockConnectors");
		Element blokDeclConnector = doc.createElement("BlockConnector");
		blokDeclConnector.setAttribute("connector-kind", "socket");
		blokDeclConnector.appendChild(defaultArgument);
		blokDeclConnector.setAttribute("connector-type", type);
		blokDeclConnector.setAttribute("position-type", "single");
		blokDeclConnector.setAttribute("is-expandable", "no");
		blokDeclConnector.setAttribute("label-editable", "no");
		blokDeclConnector.setAttribute("has-range", "no");
		blokDeclConnector.setAttribute("has-length", "no");
				
		blockDeclConnectors.appendChild(blokDeclConnector);
		blockGenusDecl.appendChild(blockDeclConnectors);
		blockGenuses.appendChild(blockGenusDecl);

		/**
		 * adding variable into the blockdrawer
		 * */
		NodeList blockDrawers = doc.getElementsByTagName("BlockDrawer");
		for (int i = 0; i < blockDrawers.getLength(); i ++) {
			Element blockDrawer = (Element)blockDrawers.item(i);
			if(blockDrawer.getAttribute("name").equals("Variables"))
			{
				Element blockGenusDeclMember = doc.createElement("BlockGenusMember");
				
				blockGenusDeclMember.appendChild(doc.createTextNode(name+"_decl"));
				blockDrawer.appendChild(blockGenusDeclMember);
				
				Element blockGenusMemberGet = doc.createElement("BlockGenusMember");				
				blockGenusMemberGet.appendChild(doc.createTextNode(name));
				blockDrawer.appendChild(blockGenusMemberGet);
				
			}
			else if(blockDrawer.getAttribute("name").equals("Logic"))
			{
				Element blockGenusMember = doc.createElement("BlockGenusMember");
				
				blockGenusMember.appendChild(doc.createTextNode(name));
				blockDrawer.appendChild(blockGenusMember);
				
				
			}
		}
		/**
		 * add set and reset methond
		 * */
		
		addSetMethod(doc, name, type);
		addReSetMethod(doc, name, type);
		if(type.equals("integer") || type.equals("double"))
		{
			addInc(doc, name, type);
			addDec(doc, name, type);
		}
		
		try
		{
			/**
			 * generating xml
			 * */
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
	
	/**
	 * adding increment methond
	 * and add properties in the xml
	 * */
	public static void addInc(Document doc, String name, String type)
	{
		Node blockGenuses = doc.getElementsByTagName("BlockGenuses").item(0);
		
		Element blockGenus = doc.createElement("BlockGenus");
		blockGenus.setAttribute("name", "inc_"+name);
		blockGenus.setAttribute("kind", "command");
		blockGenus.setAttribute("initlabel", name);
		blockGenus.setAttribute("label-suffix", "+=");
		blockGenus.setAttribute("label-unique", "no");
		blockGenus.setAttribute("is-label-value", "no");
		blockGenus.setAttribute("color", "230 0 255");
		blockGenus.setAttribute("editable-label", "no");
		
		Element blockConnectors = doc.createElement("BlockConnectors");
		Element blockConnector = doc.createElement("BlockConnector");
		blockConnector.setAttribute("connector-kind", "socket");
		blockConnector.setAttribute("connector-type", type);
		blockConnector.setAttribute("position-type", "single");
		blockConnector.setAttribute("is-expandable", "no");
		blockConnector.setAttribute("label-editable", "no");
		blockConnector.setAttribute("has-range", "no");
		blockConnector.setAttribute("has-length", "no");
		
		Element defaultArgument = doc.createElement("DefaultArg");
		defaultArgument.setAttribute("genus-name", type);
		defaultArgument.setAttribute("label", "1");
		blockConnector.appendChild(defaultArgument);
		
		blockConnectors.appendChild(blockConnector);
		blockGenus.appendChild(blockConnectors);
		blockGenuses.appendChild(blockGenus);
		
		/*NodeList nodeList = doc.getElementsByTagName("BlockFamily");
		for (int i = 0; i < nodeList.getLength(); i ++) {
			Element element = (Element) nodeList.item(i);
			if( element.getAttribute("type").equals("inc_"+type))
			{
				Element familyMember = doc.createElement("FamilyMember");
				familyMember.appendChild(doc.createTextNode("inc_"+name));
				element.appendChild(familyMember);
			}
		}*/
		
		NodeList blockDrawers = doc.getElementsByTagName("BlockDrawer");
		for (int i = 0; i < blockDrawers.getLength(); i ++) {
			Element blockDrawer = (Element)blockDrawers.item(i);
			if(blockDrawer.getAttribute("name").equals("Variables"))
			{
				Element blockGenusMember = doc.createElement("BlockGenusMember");
				
				blockGenusMember.appendChild(doc.createTextNode("inc_"+name));
				blockDrawer.appendChild(blockGenusMember);
				
				break;
			}
		}
	}
	
	/**
	 * adding decrement methond
	 * and add properties in the xml
	 * */
	public static void addDec(Document doc, String name, String type)
	{
		Node blockGenuses = doc.getElementsByTagName("BlockGenuses").item(0);
		
		Element blockGenus = doc.createElement("BlockGenus");
		blockGenus.setAttribute("name", "dec_"+name);
		blockGenus.setAttribute("kind", "command");
		blockGenus.setAttribute("initlabel", name);
		blockGenus.setAttribute("label-suffix", "-=");
		blockGenus.setAttribute("label-unique", "no");
		blockGenus.setAttribute("is-label-value", "no");
		blockGenus.setAttribute("color", "230 0 255");
		blockGenus.setAttribute("editable-label", "no");
		
		Element blockConnectors = doc.createElement("BlockConnectors");
		Element blockConnector = doc.createElement("BlockConnector");
		blockConnector.setAttribute("connector-kind", "socket");
		blockConnector.setAttribute("connector-type", type);
		blockConnector.setAttribute("position-type", "single");
		blockConnector.setAttribute("is-expandable", "no");
		blockConnector.setAttribute("label-editable", "no");
		blockConnector.setAttribute("has-range", "no");
		blockConnector.setAttribute("has-length", "no");
		
		Element defaultArgument = doc.createElement("DefaultArg");
		defaultArgument.setAttribute("genus-name", type);
		defaultArgument.setAttribute("label", "1");
		blockConnector.appendChild(defaultArgument);
		
		blockConnectors.appendChild(blockConnector);
		blockGenus.appendChild(blockConnectors);
		blockGenuses.appendChild(blockGenus);
		
		/*NodeList nodeList = doc.getElementsByTagName("BlockFamily");
		for (int i = 0; i < nodeList.getLength(); i ++) {
			Element element = (Element) nodeList.item(i);
			if( element.getAttribute("type").equals("inc_"+type))
			{
				Element familyMember = doc.createElement("FamilyMember");
				familyMember.appendChild(doc.createTextNode("inc_"+name));
				element.appendChild(familyMember);
			}
		}*/
		
		NodeList blockDrawers = doc.getElementsByTagName("BlockDrawer");
		for (int i = 0; i < blockDrawers.getLength(); i ++) {
			Element blockDrawer = (Element)blockDrawers.item(i);
			if(blockDrawer.getAttribute("name").equals("Variables"))
			{
				Element blockGenusMember = doc.createElement("BlockGenusMember");
				
				blockGenusMember.appendChild(doc.createTextNode("dec_"+name));
				blockDrawer.appendChild(blockGenusMember);
				
				break;
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
