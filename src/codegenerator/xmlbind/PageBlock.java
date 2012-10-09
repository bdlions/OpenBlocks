package codegenerator.xmlbind;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class PageBlock 
{
	private List<Block> blocks;
	
	public List<Block> getBlocks() {
		return blocks;
	}
	@XmlElement(name="Block")
	public void setBlocks(List<Block> blocks) {
		this.blocks = blocks;
	}
}
