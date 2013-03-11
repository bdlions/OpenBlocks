package codegenerator.xmlbind.config;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class BlockFamily {
	
	private List<String> familyMembers;
	@XmlElement(name="FamilyMember")
	public void setFamilyMembers(List<String> familyMembers) {
		this.familyMembers = familyMembers;
	}
	
	public List<String> getFamilyMembers() {
		return familyMembers;
	}
}
