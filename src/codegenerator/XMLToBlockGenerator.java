package codegenerator;

import java.io.ByteArrayInputStream;
import java.rmi.UnmarshalException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import codegenerator.xmlbind.Block;
import codegenerator.xmlbind.Page;
import codegenerator.xmlbind.PageBlock;
import codegenerator.xmlbind.Pages;

public class XMLToBlockGenerator {
	public static List< Block> generateBlocks(String xml) {
		List<Block> blocks = new ArrayList<Block>();

		try {
			ByteArrayInputStream input = new ByteArrayInputStream(xml.getBytes());
			JAXBContext jaxbContext = JAXBContext.newInstance(Pages.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Pages pages = (Pages) jaxbUnmarshaller.unmarshal(input);

			for (Page page : pages.getPage()) {
				PageBlock pageBlock = page.getPageBlocks();
				if (pageBlock != null) {
					blocks.addAll(pageBlock.getBlocks());
				}
			}

		} catch (JAXBException exception) {
			exception.printStackTrace();
		}
			
		return blocks;
	}
}
