package codegenerator.xmlbind.config;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


public class LangDefExecutor {
	public static void main(String[] args)
	{
		
		try {
			
	        String blockdocLocation = ((System.getProperty("application.home") != null) ?
	                System.getProperty("application.home") :
	                    System.getProperty("user.dir")) + "/support/lang_def.xml";
	        
			JAXBContext jaxbContext = JAXBContext.newInstance(BlockLangDef.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			BlockLangDef blockLangDef = (BlockLangDef) jaxbUnmarshaller.unmarshal(new File(blockdocLocation));
			
			System.out.println(blockLangDef.getBlockConnectorShapes().size());
			System.out.println(blockLangDef.getBlockGenuses().size());
			System.out.println(blockLangDef.getBlockFamilies().size());

		} catch (JAXBException exception) {
			exception.printStackTrace();
		} 
	}
	
}
