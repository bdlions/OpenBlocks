package codegenerator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import codeblocks.BlockGenus;
import codegenerator.xmlbind.Block;
import codegenerator.xmlbind.BlockConnector;
import codegenerator.xmlbind.Page;
import codegenerator.xmlbind.Pages;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class CodeGen2 {
	public HashMap<Integer, Block> blocks = new HashMap<Integer, Block>();
	public HashMap<String, Block> actionOrFunctionBlockMap = new HashMap<String, Block>();
	public List<Block> ifBlocks = new ArrayList<Block>();
	public List<Block> ifelseBlocks = new ArrayList<Block>();
	public List<Block> conditionBlocks = new ArrayList<Block>();
	public List<Block> actionBlocks = new ArrayList<Block>();
	public List<Block> functionBlocks = new ArrayList<Block>();
	public List<Variable> variableBlocks = new ArrayList<Variable>();


	public List<Block> getConditionBlocks() {
		return conditionBlocks;
	}

	public List<Block> getActionBlocks() {
		return actionBlocks;
	}

	public List<Block> getIfBlocks() {
		return ifBlocks;
	}

	public List<Block> getIfelseBlocks() {
		return ifelseBlocks;
	}

	private List<Block> blockList = new ArrayList<Block>();

	public static void main(String[] args) {

		CodeGen2 codeGen = new CodeGen2();
		codeGen.getCode();
	}
	
	
	/**
	 * Block parameters for command
	 * */
	
	public String getParameters(Block block)
	{
		String params = "";
		int count = 0;
		for (BlockConnector connector : block.getSockets().getBlockConnectors()) {
			Block bolck = blocks.get(connector.getConnectBlockId());
			if(count > 0 )
			{
				params += ", ";
			}
			count ++;
			params += bolck.getLabel();
		}
		
		return params; 
	}
	
	public List<Variable> getVariableBlocks()
	{
		return variableBlocks;
	}

	public String getCondition(Block block) throws Exception {
		if (block == null) {
			throw new NullPointerException("Block is null in getcondition");
		}

		String condition = null;

		/**
		 * this is a block which has a condition block or not
		 * 
		 * */
		if (block.getPlug() == null && block.getSockets() != null) {
			block = getConditionBlock(block);
		}

		/**
		 * If we found a condition block
		 * */
		if (block != null) {
			List<Block> conditionBlockList = new ArrayList<Block>();
			generateConditionBlock(conditionBlockList, block);
			condition = postfixToInfix(conditionBlockList);
		}
		return condition;
	}

	private Boolean isIfBlock(Block block)
	{
		if(block.getGenusName().equals("if")) return true;
		else return false;
	}
	private Boolean isVariable(Block block)
	{
		BlockGenus blockGenus = BlockGenus.getGenusWithName(block.getGenusName());
		return blockGenus.isVariableDeclBlock();
	}
	
	private Boolean isIfElseBlock(Block block)
	{
		if(block.getGenusName().equals("ifelse")) return true;
		else return false;
	}
	
	
	public List<Block> getActionsInIfBlock(Block block)
	{
		List<Block> actionBlocks = new ArrayList<Block>();
		if(isIfBlock(block) == false && isIfElseBlock(block) == false)
			return actionBlocks;
		if(block.getSockets() != null)
		{
			for (BlockConnector connector : block.getSockets().getBlockConnectors()) {
				if(connector.getLabel().equals("then"))
				{
					Block _block = blocks.get(connector.getConnectBlockId());
					actionBlocks.add(_block);
					
					while(_block.getAfterBlockId() > 0)
					{
						_block = blocks.get(_block.getAfterBlockId());
						actionBlocks.add(_block);
					}
					
				}
			}
		}
		return actionBlocks;
	}
	
	public List<Block> getActionsInElseBlock(Block block)
	{
		List<Block> actionBlocks = new ArrayList<Block>();
		if(isIfBlock(block) == false && isIfElseBlock(block) == false)
			return actionBlocks;
		if(block.getSockets() != null)
		{
			for (BlockConnector connector : block.getSockets().getBlockConnectors()) {
				if(connector.getLabel().equals("else"))
				{
					Block _block = blocks.get(connector.getConnectBlockId());
					actionBlocks.add(_block);
					
					while(_block.getAfterBlockId() > 0)
					{
						_block = blocks.get(_block.getAfterBlockId());
						actionBlocks.add(_block);
					}
					
				}
			}
		}
		return actionBlocks;
	}
	private void arrangeBlocks() {
		ifBlocks = new ArrayList<Block>();
		ifelseBlocks = new ArrayList<Block>();
		conditionBlocks = new ArrayList<Block>();
		actionBlocks = new ArrayList<Block>();
		
		
		for (Block block : blockList) {
			String blockName = block.getGenusName();
			BlockGenus blockGenus = BlockGenus.getGenusWithName(blockName);
			/**
			 * If there is no block name we cannot take decision what type of
			 * block it is so we discard the block
			 * */
			if (blockName == "") {
				continue;
			}

			/**
			 * If the bolck name is "if" then we add the block in ifblocks
			 * */
			if (isIfBlock(block)) {
				ifBlocks.add(block);
				for (Block action : getActionsInIfBlock(block)) {
					//actionBlocks.add(action);
				}
			}

			/**
			 * If the bolck name is "ifelse" then we add the block in
			 * ifelseblocks
			 * */
			else if (isIfElseBlock(block)) {
				ifelseBlocks.add(block);
				for (Block action : getActionsInIfBlock(block)) {
					//actionBlocks.add(action);
				}
				for (Block action : getActionsInElseBlock(block)) {
					//actionBlocks.add(action);
				}
			}

			/**
			 * Adding condition blocks
			 * */
			if (isIfBlock(block) || isIfElseBlock(block)) {
				Block conditionBlock = getConditionBlock(block);
				if (conditionBlock != null) {
					conditionBlocks.add(conditionBlock);
				}
			}
			
			/**
			 * Adding action blocks
			 * */
			if(blockGenus.isCommandBlock() && !isIfElseBlock(block) && !isIfBlock(block))
			{
				if(!actionOrFunctionBlockMap.containsKey(block.getGenusName()))
				{
					actionOrFunctionBlockMap.put(block.getGenusName(), block);
					actionBlocks.add(block);
				}
				
			}
			
			/**
			 * Adding function blocks
			 * */
			if(blockGenus.isFunctionBlock() && !isIfElseBlock(block) && !isIfBlock(block))
			{
				if(!actionOrFunctionBlockMap.containsKey(block.getGenusName()))
				{
					actionOrFunctionBlockMap.put(block.getGenusName(), block);
					functionBlocks.add(block);
				}
				
			}
			
			/**
			 * Adding variables
			 * 
			 */
			if(isVariable(block))
			{
				String pattern = "[^a-zA-Z_]+|[^a-zA-Z_0-9]+//g";
				Variable variable = new Variable();
				variable.setName(block.getLabel().replaceAll(pattern, ""));
				variable.setId(block.getId());
				if(block.getSockets().getBlockConnectors().size()> 0)
				{
					BlockConnector connector = block.getSockets().getBlockConnectors().get(0);
					variable.setType(connector.getType());
					Block valueBlock = blocks.get(connector.getConnectBlockId());
					//variable.setValue(valueBlock.getLabel());
					
					
				}
				variableBlocks.add(variable);
			}

		}
		
	}

	/**
	 * Give input a block who has a condition block
	 * */
	private Block getConditionBlock(Block block) {
		for (BlockConnector connector : block.getSockets().getBlockConnectors()) {
			Block connectorBlock = blocks.get(connector.getConnectBlockId());
			if (connectorBlock != null && connectorBlock.getPlug() != null) {
				return connectorBlock;
			}
		}
		return null;
	}

	/**
	 * Generation condiontion block storing all the blocks in postfix notaion
	 * */
	private void generateConditionBlock(List<Block> condiontBlocks, Block block) {

		/**
		 * If the block has no socket then we do not need to check it's barnch
		 * becasue this is the leaf node
		 * */
		if (block.getSockets() == null) {
			/**
			 * Added the leaf node
			 * */
			condiontBlocks.add(block);
			return;
		}

		/**
		 * if the block is a root node then we need to check the leaf node
		 * */
		for (BlockConnector connector : block.getSockets().getBlockConnectors()) {
			Block connectorBlock = blocks.get(connector.getConnectBlockId());

			/**
			 * if the node is connected and root then we call again to search
			 * the leaf node
			 * */
			if (connectorBlock.getPlug() != null) {
				generateConditionBlock(condiontBlocks, connectorBlock);
			}
		}
		/**
		 * Added the root node
		 * */
		condiontBlocks.add(block);
		return;
	}

	public final String postfixToInfix(final List<Block> postfix)
			throws Exception {

		/**
		 * Stack to hold values
		 * */
		final Stack<String> tokenStack = new Stack<String>();

		/**
		 * Start processing tokens
		 * */
		for (Block block : postfix) {
			/**
			 * Evaluate the current token and push it onto the stack
			 * */
			tokenStack.push(evaluateToken(block, tokenStack));
		}

		/**
		 * Last element of the stack is our result
		 * */
		String result = tokenStack.pop();
		/**
		 * If there is no brackets
		 * */
		if(result.indexOf("(") < 0)
		{
			return result;
		}
		/**
		 * delete first and last brackets
		 * */
		if (result != null) {
			result = result.substring(1, result.length() - 1);
		}
		return result;
	}

	/**
	 * If token is a value, return the token. If token is an operator, perform
	 * the operation and return the result.
	 * 
	 * @throws Exception
	 */
	private final String evaluateToken(final Block block,
			final Stack<String> tokenStack) throws Exception {

		/**
		 * if the sockets is not null then this is the root node then we need to
		 * search the left and righ leaf node
		 * */
		if (block.getSockets() != null) {
			/**
			 * We know each operator works on two numbers... Check for correct
			 * number of values on stack
			 * */

			if (tokenStack.size() < 2) {
				throw new Exception();
			}

			/**
			 * Grab values to evaluate from stack
			 * */
			final String value2 = tokenStack.pop();
			final String value1 = tokenStack.pop();

			/**
			 * Return evaluated expression
			 * */
			return "(" + value1 + " " + block.getGenusName() + " " + value2
					+ ")";

		} else {
			/**
			 * currentToken = leaf node If token is a number, it goes right onto
			 * the stack
			 * */

			return block.getGenusName();
		}
	}

	public void getCode() {
		try {

			InputStream inputStream = ClassLoader
					.getSystemResourceAsStream("resources/blocksample.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(Pages.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Pages pages = (Pages) jaxbUnmarshaller.unmarshal(inputStream);
			blocks = new HashMap<Integer, Block>();

			// get the block page
			Page blockPage = pages.getPage().get(0);
			blockList = blockPage.getPageBlocks().getBlocks();
			// put all the blocks in a hash table
			for (Block block : blockList) {
				blocks.put(block.getId(), block);
			}
			arrangeBlocks();

			for (Block block : ifelseBlocks) {
				getActionsInElseBlock(block);
			}
			Configuration cfg = new Configuration();
			String templateLocation = ((System.getProperty("application.home") != null) ? System
					.getProperty("application.home") : System
					.getProperty("user.dir"))
					+ "/templates/";

			cfg.setDirectoryForTemplateLoading(new File(templateLocation));
			cfg.setObjectWrapper(new DefaultObjectWrapper());

			Template temp = cfg.getTemplate("code.ftl");

			Map<String, CodeGen2> root = new HashMap<String, CodeGen2>();
			root.put("codeGen", this);

			final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			final Writer output = new OutputStreamWriter(outputStream);

			temp.process(root, output);
			output.flush();
			System.out.println(outputStream.toString());
		}

		catch (JAXBException exception) {
			exception.printStackTrace();
		} catch (IOException exception) {
			exception.printStackTrace();
		} catch (TemplateException exception) {
			exception.printStackTrace();
		}
	}

	public String getCode(String xmlString) {

		try {
			ByteArrayInputStream input = new ByteArrayInputStream(
					xmlString.getBytes());

			JAXBContext jaxbContext = JAXBContext.newInstance(Pages.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Pages pages = (Pages) jaxbUnmarshaller.unmarshal(input);
			blocks = new HashMap<Integer, Block>();

			// get the block page
			Page blockPage = pages.getPage().get(0);
			if(blockPage.getPageBlocks() != null)
			{
				blockList = blockPage.getPageBlocks().getBlocks();
			}
			// put all the blocks in a hash table
			for (Block block : blockList) {
				blocks.put(block.getId(), block);
			}
			arrangeBlocks();

			Configuration cfg = new Configuration();
			String templateLocation = ((System.getProperty("application.home") != null) ? System
					.getProperty("application.home") : System
					.getProperty("user.dir"))
					+ "/templates/";

			cfg.setDirectoryForTemplateLoading(new File(templateLocation));
			cfg.setObjectWrapper(new DefaultObjectWrapper());

			Template temp = cfg.getTemplate("code.ftl");

			Map<String, CodeGen2> root = new HashMap<String, CodeGen2>();
			root.put("codeGen", this);

			final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			final Writer output = new OutputStreamWriter(outputStream);

			temp.process(root, output);
			output.flush();
			return outputStream.toString();
		}

		catch (JAXBException exception) {
			exception.printStackTrace();
		} catch (IOException exception) {
			exception.printStackTrace();
		} catch (TemplateException exception) {
			exception.printStackTrace();
		}
		return null;
	}
}
