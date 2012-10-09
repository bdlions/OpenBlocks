package codegenerator.xmlbind;
import javax.xml.bind.annotation.XmlElement;

public class Plug {
	BlockConnector blockConnectors;
	
	public BlockConnector getBlockConnectors() {
		return blockConnectors;
	}
	@XmlElement(name="BlockConnector")
	public void setBlockConnectors(BlockConnector blockConnectors) {
		this.blockConnectors = blockConnectors;
	}
}
