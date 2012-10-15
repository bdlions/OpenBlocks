package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class StructureCode {
	public String spaceBlock = "     ";
	public StructureCode()
	{
		
	}
	
	/*public String getContents(String filePath) {
	    File aFile = new File(filePath);
		//...checks on aFile are elided
	    StringBuilder contents = new StringBuilder();
	    
	    try {
	      //use buffering, reading one line at a time
	      //FileReader always assumes default encoding is OK!
	      BufferedReader input =  new BufferedReader(new FileReader(aFile));
	      try {
	        String line = null; //not declared within while loop
	        while (( line = input.readLine()) != null){
	          contents.append(line.trim());
	          contents.append(System.getProperty("line.separator"));
	        }
	      }
	      finally {
	        input.close();
	      }
	    }
	    catch (IOException ex){
	      ex.printStackTrace();
	    }
	    
	    return contents.toString();
	  }
	public void writFile(String filePath, String fileContent) throws IOException
	{
		File aFile = new File(filePath);
		Writer output = new BufferedWriter(new FileWriter(aFile));
		try {
			//FileWriter always assumes default encoding is OK!
	      output.write( fileContent );
	    }
		catch(Exception exception)
		{
			
		}
	    finally {
	      output.close();
	    }
	}*/
	
	/*
	 * This method indents code
	 * */
	public String indentMyCode(String code)
	{
		String indentedCode = "";
		//String[] lines = code.split(System.getProperty("line.separator"));
		String[] lines = code.split("\n");
		System.out.println("total lines:"+lines.length);
		int tabCounter = 0;
		for(int i = 0 ; i < lines.length ; i++)
		{
			String singleLine = lines[i].trim();
			//}else{
			if(singleLine.contains("{") && singleLine.contains("}") && singleLine.indexOf("}") < singleLine.indexOf("{"))
			{
				tabCounter--;
				indentedCode = indentedCode +"\n"+ getIndentStartSpaces(tabCounter) + singleLine;
				tabCounter++;
			}
			else if(singleLine.contains("}"))
			{
				tabCounter--;
				indentedCode = indentedCode +"\n"+ getIndentStartSpaces(tabCounter) + singleLine;				
			}
			else if(singleLine.contains("{"))
			{
				indentedCode = indentedCode +"\n"+ getIndentStartSpaces(tabCounter) + singleLine;
				tabCounter++;
			}			
			else
			{
				indentedCode = indentedCode +"\n"+ getIndentStartSpaces(tabCounter) + singleLine;				
			}
		}

		return indentedCode;
	}
	
	/*
	 * This method creates starting indent of each line
	 * */
	public String getIndentStartSpaces(int tabCounter)
	{
		String spaces = "";
		for(int i = 0 ; i < tabCounter ; i++)
		{
			spaces = spaces + spaceBlock;
		}
		return spaces;
	}
}
