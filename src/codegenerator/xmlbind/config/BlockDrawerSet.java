package codegenerator.xmlbind.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class BlockDrawerSet {

	private String type;
	private String name;
	private String location;
	private String windowPerDrawer;
	private String drawerDraggable;
	private int width;

	@XmlAttribute(name="drawer-draggable")
	public void setDrawerDraggable(String drawerDraggable) {
		this.drawerDraggable = drawerDraggable;
	}
	@XmlAttribute(name="location")
	public void setLocation(String location) {
		this.location = location;
	}
	@XmlAttribute(name="name")
	public void setName(String name) {
		this.name = name;
	}
	@XmlAttribute(name="type")
	public void setType(String type) {
		this.type = type;
	}
	@XmlAttribute(name="width")
	public void setWidth(int width) {
		this.width = width;
	}
	@XmlAttribute(name="window-per-drawer")
	public void setWindowPerDrawer(String windowPerDrawer) {
		this.windowPerDrawer = windowPerDrawer;
	}
	
	public String getDrawerDraggable() {
		return drawerDraggable;
	}
	public String getLocation() {
		return location;
	}
	public String getName() {
		return name;
	}
	public String getType() {
		return type;
	}
	public int getWidth() {
		return width;
	}
	public String getWindowPerDrawer() {
		return windowPerDrawer;
	}
}
