package codegenerator.xmlbind.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


@XmlType
public class BlockConnectorShape {
	private String shapeType;
	private int shapeNumber;
	
	@XmlAttribute(name="shape-number")
	public void setShapeNumber(int shapeNumber) {
		this.shapeNumber = shapeNumber;
	}
	
	@XmlAttribute(name="shape-type")
	public void setShapeType(String shapeType) {
		this.shapeType = shapeType;
	}
	
	public int getShapeNumber() {
		return shapeNumber;
	}
	public String getShapeType() {
		return shapeType;
	}
	
}
