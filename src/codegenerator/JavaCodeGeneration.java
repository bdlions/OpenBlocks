package codegenerator;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;


public class JavaCodeGeneration {
	
	public static void main(String args[]) throws IOException, TemplateException 
	{

		
        /* ------------------------------------------------------------------- */    
        /* You should do this ONLY ONCE in the whole application life-cycle:   */    
    
        /* Create and adjust the configuration */
        Configuration cfg = new Configuration();
        cfg.setDirectoryForTemplateLoading(
                new File("H:\\WorkspaceRAC\\OpenBlocks"));
        cfg.setObjectWrapper(new DefaultObjectWrapper());

        /* ------------------------------------------------------------------- */    
        /* You usually do these for many times in the application life-cycle:  */    
                
        /* Get or create a template */
        Template temp = cfg.getTemplate("java.ftl");

        /* Create a data-model */
        Map root = new HashMap();
        
        Map classMap = new HashMap();
        classMap.put("name", "Person");
        classMap.put("packageName", "person");
        root.put("class", classMap);
        
        List<Attribute> aKeys = new ArrayList<Attribute>();
        aKeys.add(new Attribute("String", "name"));
        aKeys.add(new Attribute("int", "age"));
        aKeys.add(new Attribute("int",  "passingYear"));
        aKeys.add(new Attribute("float", "value"));
        
        root.put("aKeys", aKeys);
        
        
        /* Merge data-model with template */
        Writer out = new OutputStreamWriter(System.out);
        temp.process(root, out);
        out.flush();
	}

}
