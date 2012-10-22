package codegenerator.xmlbind;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class Pages 
{
	private List<Page> page = new ArrayList<Page>() ;
	
	@XmlElement(name="Page")
	public List<Page> getPage() {
		return page;
	}
	public void setPage(List<Page> page) {
		this.page = page;
	}
}
