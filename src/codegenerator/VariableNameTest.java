package codegenerator;

public class VariableNameTest {
	public static void main(String[] args) {
		String varName = " a bc_12&%ab";
		//String pattern = "s/^[^a-zA-Z_]+|[^a-zA-Z_0-9]+//g";
		String pattern = "[^a-zA-Z_]+|[^a-zA-Z_0-9]+//g";
		String repl = "";
		System.out.println(varName.replaceAll(pattern, repl)); 
		System.out.println(repl);
	}

}
