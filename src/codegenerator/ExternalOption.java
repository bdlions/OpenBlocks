package codegenerator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ExternalOption {
	public static String addCustombyUser(Document doc, String name, String value, String type)
	{
		String xmlString = null;
		Node blockGenuses = doc.getElementsByTagName("BlockGenuses").item(0);
		
		Element blockGenus = doc.createElement("BlockGenus");
		blockGenus.setAttribute("name", name);
		blockGenus.setAttribute("kind", "data");
		blockGenus.setAttribute("initlabel", value);
		blockGenus.setAttribute("editable-label", "yes");
		blockGenus.setAttribute("is-label-value", "yes");
		
		//blockGenus.setAttribute("label-prefix", "get ");		
		//blockGenus.setAttribute("label-unique", "yes");
				
		blockGenus.setAttribute("color", "230 0 255");
		
		Element blockConnectors = doc.createElement("BlockConnectors");
		Element blokConnector = doc.createElement("BlockConnector");
		blokConnector.setAttribute("connector-kind", "plug");
		blokConnector.setAttribute("connector-type", type);
		blokConnector.setAttribute("position-type", "mirror");
		
		blockConnectors.appendChild(blokConnector);
		blockGenus.appendChild(blockConnectors);
		blockGenuses.appendChild(blockGenus);
		
		NodeList blockDrawers = doc.getElementsByTagName("BlockDrawer");
		for (int i = 0; i < blockDrawers.getLength(); i ++) {
			Element blockDrawer = (Element)blockDrawers.item(i);
			if(blockDrawer.getAttribute("name").equals("ExternalOption"))
			{
				Element blockGenusDeclMember = doc.createElement("BlockGenusMember");
				
				blockGenusDeclMember.appendChild(doc.createTextNode(name));
				blockDrawer.appendChild(blockGenusDeclMember);				
			}			
		}		
		return xmlString;
	}

}
