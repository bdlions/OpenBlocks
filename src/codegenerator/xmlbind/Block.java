package codegenerator.xmlbind;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import codeblocks.BlockGenus;

@XmlType
public class Block 
{
	private String label;
	private int id;
	private String genusName;
	
	private int afterBlockId;
	private int beforeBlockId;
	private Sockets sockets;
	private Plug plug;
	
	public int getId() {
		return id;
	}
	@XmlAttribute(name = "id")
	public void setId(int id) {
		this.id = id;
	}
	
	public String getGenusName() {
		return genusName;
	}
	@XmlAttribute(name="genus-name")
	public void setGenusName(String genusName) {
		this.genusName = genusName;
	}

	public int getAfterBlockId() {
		return afterBlockId;
	}
	@XmlElement(name="AfterBlockId")
	public void setAfterBlockId(int afterBlockId) {
		this.afterBlockId = afterBlockId;
	}
	
	public int getBeforeBlockId() {
		return beforeBlockId;
	}
	@XmlElement(name="BeforeBlockId")
	public void setBeforeBlockId(int beforeBlockId) {
		this.beforeBlockId = beforeBlockId;
	}
	
	public Sockets getSockets() {
		return sockets;
	}
	@XmlElement(name="Sockets")
	public void setSockets(Sockets sockets) {
		this.sockets = sockets;
	}
	
	public Plug getPlug() {
		return plug;
	}
	@XmlElement(name="Plug")
	public void setPlug(Plug plug) {
		this.plug = plug;
	}
	
	public String getLabel() {
		if(label == null) return getGenusName();
		return label;
	}
	@XmlElement(name="Label")
	public void setLabel(String label) {
		this.label = label;
	}
	
}
