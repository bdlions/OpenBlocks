package codegenerator;

import java.util.List;


public class Variable {
	private int id;
	private String name;
	private String type;
	private List<ExpressionData> value;
	
	public String getName() {
		return name;
	}
	public String getType() {
		return type;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public void setValue(List<ExpressionData> value) {
		this.value = value;
	}
	public List<ExpressionData> getValue() {
		return value;
	}
	
	public static String getValidVariableName(String name)
	{
		StringBuilder sb = new StringBuilder();
	    if(!Character.isJavaIdentifierStart(name.charAt(0))) {
	        sb.append("_");
	    }
	    for (char c : name.toCharArray()) {
	        if(!Character.isJavaIdentifierPart(c)) {
	            sb.append("_");
	        } else {
	            sb.append(c);
	        }
	    }
	    
	    return sb.toString();
	}
	
	public static boolean isValidVariableName(String name)
	{
		
		if(name == null || name.equals("")) return false;
	    if(!Character.isJavaIdentifierStart(name.charAt(0))) return false;
	    
	    for (char c : name.toCharArray()) {
	        if(!Character.isJavaIdentifierPart(c))  return false;
	    }
	    
	    return true;
	}
}
