package language;
import java.io.File;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;

@XmlType(propOrder = {"languages"}, namespace = "myform1")
@XmlRootElement(name="myform1")
public class LoadLanguage
{
	private String title;
	private Languages languages = new Languages();

		
	public String getTitle() {
		return title;
	}
	
	@XmlAttribute(name="title")
	public void setTitle(String title) {
		this.title = title;
	}

	
	public Languages getLanguages() {
		return languages;
	}
	public void setLanguages(Languages languages) {
		this.languages = languages;
	}
}