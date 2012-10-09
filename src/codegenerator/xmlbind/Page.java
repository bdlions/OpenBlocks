package codegenerator.xmlbind;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class Page 
{
	private String pageName;
	PageBlock pageBlocks;
	
	public String getPageName() {
		return pageName;
	}
	@XmlAttribute(name="page-name")
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	
	public PageBlock getPageBlocks() {
		return pageBlocks;
	}
	@XmlElement(name="PageBlocks")
	public void setPageBlocks(PageBlock pageBlocks) {
		this.pageBlocks = pageBlocks;
	}
}
