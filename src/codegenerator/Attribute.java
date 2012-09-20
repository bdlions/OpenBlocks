package codegenerator;

public class Attribute
{
	private String accessModifier;
	private String name;
	
	public Attribute(String accessModifier, String name) 
	{
		// TODO Auto-generated constructor stub
		setAccessModifier(accessModifier);
		setName(name);
	}


	public void setAccessModifier(String accessModifier) {
		this.accessModifier = accessModifier;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAccessModifier() {
		return accessModifier;
	}
	public String getName() {
		return name;
	}
	
}
