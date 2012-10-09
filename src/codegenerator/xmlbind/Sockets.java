package codegenerator.xmlbind;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlType
public class Sockets {

	private int numSockets;
	private List<BlockConnector> blockConnectors;
	
	public int getNumSockets() {
		return numSockets;
	}
	@XmlAttribute(name="num-sockets")
	public void setNumSockets(int numSockets) {
		this.numSockets = numSockets;
	}
	
	public List<BlockConnector> getBlockConnectors() {
		return blockConnectors;
	}
	@XmlElement(name="BlockConnector")
	public void setBlockConnectors(List<BlockConnector> blockConnectors) {
		this.blockConnectors = blockConnectors;
	}
}
