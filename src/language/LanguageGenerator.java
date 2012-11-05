package language;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import java.util.*;
public class LanguageGenerator
{
	private String language = "English";
	private Hashtable syntaxMapLanguage = new Hashtable();
	private LoadLanguage loadLanguage = new LoadLanguage();
	
	public LoadLanguage getLoadLanguage() {
		return loadLanguage;
	}
	
	public Hashtable getSyntaxMapLanguage() {
		return syntaxMapLanguage;
	}
	
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	
	/*
	 * This method loads form1 xml file and stores into object
	 * */
	public void generateLoadLanguage()
	{
		try 
		{
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("resources/language.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(LoadLanguage.class);
			 
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			loadLanguage = (LoadLanguage) jaxbUnmarshaller.unmarshal(inputStream);
			syntaxMapLanguage = new Hashtable();
			List<Language> languages =  loadLanguage.getLanguages().getLanguage();
			for ( Language language : languages) 
			{
				if(getLanguage().equalsIgnoreCase(language.getName()))
				{
					for(LanguageEntry entry: language.getEntry())
					{
						syntaxMapLanguage.put(entry.getName(), entry);
					}
				}
			}
		} 
		catch (JAXBException exception) 
		{
			
		}
	}
	/*
	 * This method updates from1 language
	 * */
	public void updateLanguageMapLoadLanguage()
	{
		syntaxMapLanguage = new Hashtable();
		List<Language> languages =  loadLanguage.getLanguages().getLanguage();
		for ( Language language : languages) 
		{
			if(getLanguage().equalsIgnoreCase(language.getName()))
			{
				for(LanguageEntry entry: language.getEntry())
				{
					syntaxMapLanguage.put(entry.getName(), entry);
				}
			}
		}
	}
	
}
