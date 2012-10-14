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
}
