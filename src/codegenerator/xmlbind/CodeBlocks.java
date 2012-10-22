package codegenerator.xmlbind;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="CODEBLOCKS")
public class CodeBlocks {
	private Pages pages;
	@XmlElement(name="Pages")
	public Pages getPages() {
		return pages;
	}
	public void setPages(Pages pages) {
		this.pages = pages;
	}
}
