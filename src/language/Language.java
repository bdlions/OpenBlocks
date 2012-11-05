package language;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"entry" }, namespace = "language")
public class Language 
{
	private String name;
	
	private List<LanguageEntry> entry = new ArrayList<LanguageEntry>();
	
	
	@XmlAttribute(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public List<LanguageEntry> getEntry() {
		return entry;
	}
	public void setEntry(List<LanguageEntry> entry) {
		this.entry = entry;
	}
}
