package codegenerator.xmlbind.config;

import java.util.List;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class BlockGenus {
	private String name;
	private String initLabel;
	private String kind;
	private String color;
	private String editabelLabel;
	private String labelUnique;
	private String isLabelValue;
	private String labelPrefix;
	private String labelSuffix;
	private String pageLabelEnabled;
	private String isStarter;
	private String isTerminator;
	
	@XmlAttribute(name="color")
	public void setColor(String color) {
		this.color = color;
	}
	@XmlAttribute(name="editable-label")
	public void setEditabelLabel(String editabelLabel) {
		this.editabelLabel = editabelLabel;
	}
	@XmlAttribute(name="initlabel")
	public void setInitLabel(String initLabel) {
		this.initLabel = initLabel;
	}
	@XmlAttribute(name="is-label-value")
	public void setIsLabelValue(String isLabelValue) {
		this.isLabelValue = isLabelValue;
	}
	@XmlAttribute(name="is-starter")
	public void setIsStarter(String isStarter) {
		this.isStarter = isStarter;
	}
	@XmlAttribute(name="is-terminator")
	public void setIsTerminator(String isTerminator) {
		this.isTerminator = isTerminator;
	}
	@XmlAttribute(name="kind")
	public void setKind(String kind) {
		this.kind = kind;
	}
	@XmlAttribute(name="label-prefix")
	public void setLabelPrefix(String labelPrefix) {
		this.labelPrefix = labelPrefix;
	}
	@XmlAttribute(name="label-suffix")
	public void setLabelSuffix(String labelSuffix) {
		this.labelSuffix = labelSuffix;
	}
	@XmlAttribute(name="label-unique")
	public void setLabelUnique(String labelUnique) {
		this.labelUnique = labelUnique;
	}
	@XmlAttribute(name="name")
	public void setName(String name) {
		this.name = name;
	}
	@XmlAttribute(name="page-label-enabled")
	public void setPageLabelEnabled(String pageLabelEnabled) {
		this.pageLabelEnabled = pageLabelEnabled;
	}
	
	public String getColor() {
		return color;
	}
	public String getEditabelLabel() {
		return editabelLabel;
	}
	public String getInitLabel() {
		return initLabel;
	}
	public String getIsLabelValue() {
		return isLabelValue;
	}
	public String getIsStarter() {
		return isStarter;
	}
	public String getIsTerminator() {
		return isTerminator;
	}
	public String getKind() {
		return kind;
	}
	public String getLabelPrefix() {
		return labelPrefix;
	}
	public String getLabelSuffix() {
		return labelSuffix;
	}
	public String getLabelUnique() {
		return labelUnique;
	}
	public String getName() {
		return name;
	}
	public String getPageLabelEnabled() {
		return pageLabelEnabled;
	}
	
	
	private List<BlockConnector> blockConnectors;
	
	@XmlElementWrapper(name="BlockConnectors")
	@XmlElement(name="BlockConnector")
	public void setBlockConnectors(List<BlockConnector> blockConnectors) {
		this.blockConnectors = blockConnectors;
	}
	
	public List<BlockConnector> getBlockConnectors() {
		return blockConnectors;
	}
	
	
}
