package codegenerator.xmlbind.config;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="BlockLangDef")
public class BlockLangDef {
	private List<BlockConnectorShape> blockConnectorShapes;
	private List<BlockGenus> blockGenuses;
	private List<BlockFamily> blockFamilies;
	private List<BlockDrawerSet> blockDrawerSets;
	
	@XmlElementWrapper(name="BlockConnectorShapes")
	@XmlElement(name="BlockConnectorShape")
	public void setBlockConnectorShapes(
			List<BlockConnectorShape> blockConnectorShapes) {
		this.blockConnectorShapes = blockConnectorShapes;
	}
	
	@XmlElementWrapper(name="BlockGenuses")
	@XmlElement(name="BlockGenus")
	public void setBlockGenuses(List<BlockGenus> blockGenuses) {
		this.blockGenuses = blockGenuses;
	}
	
	@XmlElementWrapper(name="BlockFamilies")
	@XmlElement(name="BlockFamily")
	public void setBlockFamilies(List<BlockFamily> blockFamilies) {
		this.blockFamilies = blockFamilies;
	}
	
	@XmlElementWrapper(name="BlockDrawerSets")
	@XmlElement(name="BlockDrawerSet")
	public void setBlockDrawerSets(List<BlockDrawerSet> blockDrawerSets) {
		this.blockDrawerSets = blockDrawerSets;
	}
	
	public List<BlockConnectorShape> getBlockConnectorShapes() {
		return blockConnectorShapes;
	}
	public List<BlockGenus> getBlockGenuses() {
		return blockGenuses;
	}
	
	public List<BlockFamily> getBlockFamilies() {
		return blockFamilies;
	}
	public List<BlockDrawerSet> getBlockDrawerSets() {
		return blockDrawerSets;
	}
	
	public void toXML() {
		try {
			JAXBContext ctx = JAXBContext.newInstance(BlockLangDef.class);
			Marshaller marshaller = ctx.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
					Boolean.TRUE);
			marshaller.marshal(this, System.out);
		} catch (Exception e) {

			
			
		}
	}
}