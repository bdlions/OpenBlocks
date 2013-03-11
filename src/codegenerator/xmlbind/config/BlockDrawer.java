package codegenerator.xmlbind.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class BlockDrawer {

	private String type;
	private String name;
	private String isOpen;
	private String buttonColor;
	
	@XmlAttribute(name="name")
	public void setName(String name) {
		this.name = name;
	}
	@XmlAttribute(name="type")
	public void setType(String type) {
		this.type = type;
	}
	@XmlAttribute(name="is-open")
	public void setButtonColor(String buttonColor) {
		this.buttonColor = buttonColor;
	}
	@XmlAttribute(name="button-color")
	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}
	
	public String getName() {
		return name;
	}
	public String getType() {
		return type;
	}
	public String getButtonColor() {
		return buttonColor;
	}
	public String getIsOpen() {
		return isOpen;
	}
	
}
