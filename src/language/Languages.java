package language;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"language"}, namespace = "languages")
public class Languages 
{
	private List<Language> language = new ArrayList<Language>() ;
	public List<Language> getLanguage() {
		return language;
	}
	public void setLanguage(List<Language> language) {
		this.language = language;
	}
}
