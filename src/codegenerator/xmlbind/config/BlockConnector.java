package codegenerator.xmlbind.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class BlockConnector {

	private String label;
	private String labelEditable;
	private String connectorType;
	private String connectorKind;
	private String positionType;
	private String isExpandable;
	private String hasRange;
	private String low;
	private String high;
	
	@XmlAttribute(name="connector-kind")
	public void setConnectorKind(String connectorKind) {
		this.connectorKind = connectorKind;
	}
	@XmlAttribute(name="connector-type")
	public void setConnectorType(String connectorType) {
		this.connectorType = connectorType;
	}
	@XmlAttribute(name="has-range")
	public void setHasRange(String hasRange) {
		this.hasRange = hasRange;
	}
	@XmlAttribute(name="high")
	public void setHigh(String high) {
		this.high = high;
	}
	@XmlAttribute(name="is-expandable")
	public void setIsExpandable(String isExpandable) {
		this.isExpandable = isExpandable;
	}
	@XmlAttribute(name="label")
	public void setLabel(String label) {
		this.label = label;
	}
	@XmlAttribute(name="label-editable")
	public void setLabelEditable(String labelEditable) {
		this.labelEditable = labelEditable;
	}
	@XmlAttribute(name="low")
	public void setLow(String low) {
		this.low = low;
	}
	@XmlAttribute(name="position-type")
	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}
	
	
	public String getConnectorKind() {
		return connectorKind;
	}
	
	public String getConnectorType() {
		return connectorType;
	}
	public String getHasRange() {
		return hasRange;
	}
	public String getHigh() {
		return high;
	}
	
	public String getIsExpandable() {
		return isExpandable;
	}
	public String getLabel() {
		return label;
	}
	public String getLabelEditable() {
		return labelEditable;
	}
	public String getLow() {
		return low;
	}
	public String getPositionType() {
		return positionType;
	}
	
	
}
