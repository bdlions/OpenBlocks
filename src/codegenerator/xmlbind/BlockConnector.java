package codegenerator.xmlbind;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class BlockConnector {
	private int connectBlockId;
	private String label;
	private String type;
	private String positionType;
	
	
	public String getLabel() {
		return label;
	}
	@XmlAttribute(name="label")
	public void setLabel(String label) {
		this.label = label;
	}
	
	public int getConnectBlockId() {
		return connectBlockId;
	}
	@XmlAttribute(name="con-block-id")
	public void setConnectBlockId(int connectBlockId) {
		this.connectBlockId = connectBlockId;
	}
	
	public String getType() {
		return type;
	}
	@XmlAttribute(name="init-type")
	public void setType(String type) {
		this.type = type;
	}
	
	public String getPositionType() {
		return positionType;
	}
	@XmlAttribute(name="position-type")
	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}
}
