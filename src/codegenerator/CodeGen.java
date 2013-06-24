package codegenerator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import renderable.BlockUtilities;

import codeblocks.BlockGenus;
import codegenerator.xmlbind.Block;
import codegenerator.xmlbind.BlockConnector;
import codegenerator.xmlbind.Plug;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class CodeGen {

	private HashMap<Integer, Block> blocksMap = new HashMap<Integer, Block>();
	private List<Variable> variableDecl = new ArrayList<Variable>();
	private List<Block> allBlocks = new ArrayList<Block>();
	private List<Block> differentCommand = new ArrayList<Block>();
	private List<Block> allFunction = new ArrayList<Block>();
	public String getGenerateCode(String xmlString, String languageName) {
		languageName = languageName+"/"+languageName;
		// TODO Auto-generated method stub
		allBlocks = XMLToBlockGenerator.generateBlocks(xmlString);
		HashMap<Integer, Block> functionsMap = new HashMap<Integer, Block>();

		for (Block block : allBlocks) 
		{
			blocksMap.put(block.getId(), block);
		}
		
		for (Block block : allBlocks) 
		{
			BlockGenus genus = getGenusWithName(block.getGenusName());
			
			if(genus.isCommandBlock() &&  block.getBeforeBlockId() <= 0)
			{
				differentCommand.add(block);
			}
			if (genus.isFunctionBlock() || genus.isCommandBlock()) 
			{
				if(!functionsMap.containsKey(block.getId()))
				{
					functionsMap.put(block.getId(), block);
					allFunction.add(block);
				}
			}
			else if(genus.isVariableDeclBlock())
			{
				/**
				 * Adding variables
				 * 
				 */
				
				//String pattern = "^[a-zA-Z][a-zA-Z0-9]*?$";
				Variable variable = new Variable();
				variable.setName(Variable.getValidVariableName(block.getLabel()));
				variable.setId(block.getId());
				
				if(block.getSockets() != null && block.getSockets().getBlockConnectors().size()> 0)
				{
					BlockConnector connector = block.getSockets().getBlockConnectors().get(0);
					
					variable.setType(connector.getType());
					
					Block valueBlock = blocksMap.get(connector.getConnectBlockId());
					/**
					 * If there is really a value block
					 * */
					if(valueBlock != null)
					{
						BlockGenus connectorGenus = getGenusWithName(valueBlock.getGenusName());
						List<ExpressionData> listExpressionBlocks = new ArrayList<ExpressionData>();
						
						
						if(connectorGenus.isFunctionBlock())
						{
							//listExpressionBlocks.add(getExpressionData(valueBlock.getId(), Integer.toString(valueBlock.getId())));
							//while (isCompleteProcessFunction(listExpressionBlocks)) ;
							listExpressionBlocks = getFullFunction(valueBlock.getId());
							variable.setValue(listExpressionBlocks);
						}
						else
						{
							listExpressionBlocks.add(getExpressionData(valueBlock.getId(), valueBlock.getGenusName()));
							variable.setValue(listExpressionBlocks);
						}
					}
				}
				variableDecl.add(variable);
			}
		}
		return getCodeFromTemplate(languageName);
	}
	
	public String getCodeFromTemplate(String languageName)
	{
		try
		{
			Configuration cfg = new Configuration();
			String templateLocation = ((System.getProperty("application.home") != null) ? System
					.getProperty("application.home") : System
					.getProperty("user.dir"))
					+ "/templates/";
	
			cfg.setDirectoryForTemplateLoading(new File(templateLocation));
			cfg.setObjectWrapper(new DefaultObjectWrapper());
	
			Template temp = cfg.getTemplate(languageName+".ftl");
	
			Map<String, CodeGen> root = new HashMap<String, CodeGen>();
			root.put("codeGen", this);
	
			final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			final Writer output = new OutputStreamWriter(outputStream);
	
			temp.process(root, output);
			output.flush();
			//System.out.println(outputStream.toString());
			return outputStream.toString();
		}
		catch (IOException exception) {
			exception.printStackTrace();
		} catch (TemplateException exception) {
			exception.printStackTrace();
		}
		
		return null;
	}
	
	public String getPartialGenerateCode(String xmlString, String languageName, int blockID) {
		languageName = languageName+"/"+languageName+"-partial";
		// TODO Auto-generated method stub
		allBlocks = XMLToBlockGenerator.generateBlocks(xmlString);
		HashMap<Integer, Block> functionsMap = new HashMap<Integer, Block>();

		for (Block block : allBlocks) 
		{
			blocksMap.put(block.getId(), block);
		}
		
		for (Block block : allBlocks) 
		{
			if(block.getId() != blockID) continue;
			BlockGenus genus = getGenusWithName(block.getGenusName());
			
			if(genus.isCommandBlock())
			{
				differentCommand.add(block);
			}
			if (genus.isFunctionBlock() || genus.isCommandBlock()) 
			{
				if(!functionsMap.containsKey(block.getId()))
				{
					functionsMap.put(block.getId(), block);
					allFunction.add(block);
				}
			}
			/**
			 * Adding variables
			 * 
			 */
			else if(genus.isVariableDeclBlock())
			{
				
				//String pattern = "^[a-zA-Z][a-zA-Z0-9]*?$";
				Variable variable = new Variable();
				variable.setName(Variable.getValidVariableName(block.getLabel()));
				variable.setId(block.getId());
				
				if(block.getSockets() != null && block.getSockets().getBlockConnectors().size()> 0)
				{
					BlockConnector connector = block.getSockets().getBlockConnectors().get(0);
					
					variable.setType(connector.getType());
					
					Block valueBlock = blocksMap.get(connector.getConnectBlockId());
					/**
					 * If there is really a value block
					 * */
					if(valueBlock != null)
					{
						BlockGenus connectorGenus = getGenusWithName(valueBlock.getGenusName());
						List<ExpressionData> listExpressionBlocks = new ArrayList<ExpressionData>();
						
						
						if(connectorGenus.isFunctionBlock())
						{
							//listExpressionBlocks.add(getExpressionData(valueBlock.getId(), Integer.toString(valueBlock.getId())));
							//while (isCompleteProcessFunction(listExpressionBlocks)) ;
							listExpressionBlocks = getFullFunction(valueBlock.getId());
							variable.setValue(listExpressionBlocks);
						}
						else
						{
							listExpressionBlocks.add(getExpressionData(valueBlock.getId(), valueBlock.getGenusName()));
							variable.setValue(listExpressionBlocks);
						}
					}
				}
				variableDecl.add(variable);
			}
		}
		
		return getCodeFromTemplate(languageName);
	}

	public Boolean isCompleteProcessFunction(List<ExpressionData> list) {
		boolean found = false;
		List<ExpressionData> tempList = new ArrayList<ExpressionData>();

		for (int i = 0; i < list.size(); i++) {
			if (isNumber(list.get(i).getData())) {

				Block block = blocksMap.get(list.get(i).getId());
				if(block == null)
				{
					return false; 
				}
				BlockGenus blockGenus = getGenusWithName(block.getGenusName());

				if (blockGenus.isDataBlock()) {
					tempList.add(getExpressionData(block.getId(), block.getGenusName()));
					continue;
				} else if (blockGenus.isFunctionBlock()) {
					/**
					 * If there is a plug
					 * */
					if (block.getPlug() != null) {
						Plug blockPlug = block.getPlug();
						/**
						 * If the block plug has a connector
						 * */
						if (blockPlug.getBlockConnectors() != null) {
							BlockConnector connector = blockPlug.getBlockConnectors();
							/**
							 * if the connector is a mirror type that means and,
							 * or, <, >, == or the connector type is a single
							 * that means it's a function
							 * */
							if (connector.getPositionType().equals("mirror")) {

								/**
								 * this is a mirrored connector so it has two
								 * sockets or not
								 * */
								if (block.getSockets() != null) {
									List<BlockConnector> connectorList = block.getSockets().getBlockConnectors();
									BlockConnector leftConnector = connectorList.get(0);
									BlockConnector rightConnector = connectorList.get(1);
									Block leftBlock = blocksMap.get(leftConnector.getConnectBlockId());
									Block rightBlock = blocksMap.get(rightConnector.getConnectBlockId());

									tempList.add(getExpressionData(0, "("));
									/**
									 * There will be no left block
									 * */
									if (leftBlock != null) {
										tempList.add(getExpressionData(leftBlock.getId(), Integer.toString(leftBlock.getId())));
									}
									tempList.add(getExpressionData(block.getId(), block.getGenusName()));
									if (rightBlock != null) {
										tempList.add(getExpressionData(rightBlock.getId(), Integer.toString(rightBlock.getId())));
									}
									tempList.add(getExpressionData(0, ")"));

								} else {
									tempList.add(getExpressionData(block.getId(), block.getGenusName()));

								}
								found = true;
							} else if (connector.getPositionType().equals("single")) {

								/**
								 * this is a function type connector so it has
								 * lots of sockets or not
								 * */
								List<BlockConnector> connectorList = block.getSockets().getBlockConnectors();

								tempList.add(getExpressionData(block.getId(),block.getGenusName()));
								int paramCount = 0;

								tempList.add(getExpressionData(0, "("));

								for (BlockConnector blockConnector : connectorList) {
									Block functionBlock = blocksMap.get(blockConnector.getConnectBlockId());
									if (paramCount > 0) {
										tempList.add(getExpressionData(0, ","));
									}
									if (functionBlock != null) {
										tempList.add(getExpressionData(functionBlock.getId(), Integer.toString(functionBlock.getId())));
									}

									paramCount++;
								}

								tempList.add(getExpressionData(0, ")"));
								found = true;
							}

						}
					}

				}
			} else {
				tempList.add(list.get(i));
			}

		}

		list.clear();
		list.addAll(tempList);
		return found;
	}
	
	public List<ExpressionData> getCommandExpression(Block block)
	{
		List<ExpressionData> expression = new ArrayList<ExpressionData>();
		
		BlockGenus blockGenus = getGenusWithName(block.getGenusName());
		Number number = block.getId();
		if(blockGenus.isCommandBlock() && block.getGenusName().equals("run"))
		{
			expression.add(getExpressionData(0, "run("));
			
			if(block.getSockets() != null)
			{
				int count = 0;
				for (BlockConnector blockConnector : block.getSockets().getBlockConnectors()) {
					Block paramBlock = blocksMap.get(blockConnector.getConnectBlockId());
					if(count > 0)
					{
						expression.add(getExpressionData(0, ","));
					}
					if(paramBlock != null)
					{
						Number paramBlockId = paramBlock.getId();
						if(count == 3){
							expression.add(getExpressionData(paramBlock.getId(), 2*Integer.parseInt(paramBlock.getLabel()) + ""));
						}else if(blockConnector.getType().equals("string") && !codeblocks.Block.getBlock(paramBlockId.longValue()).getLabelPrefix().equals("get "))
							expression.add(getExpressionData(paramBlock.getId(), "\""+paramBlock.getLabel()+"\""));
						else
							expression.add(getExpressionData(paramBlock.getId(), paramBlock.getLabel()));
					}
					count ++;
				}
			}
			expression.add(getExpressionData(0, ")"));
			return expression;
		}
		else if(blockGenus.isCommandBlock() && block.getGenusName().equals("play"))
		{
			expression.add(getExpressionData(0, "play(100)"));
			return expression;
		}
		else if(blockGenus.isCommandBlock() && codeblocks.Block.getBlock(number.longValue()).getLabelPrefix().equals("set "))
		{
			expression.add(getExpressionData(0, block.getLabel()));
			expression.add(getExpressionData(0, "="));
			if(block.getSockets() != null)
			{
				BlockConnector blockConnector = block.getSockets().getBlockConnectors().get(0);
				if(blockConnector != null && blocksMap.get(blockConnector.getConnectBlockId()) != null)
				{
					Block paramBlock = blocksMap.get(blockConnector.getConnectBlockId());
					
					if(getGenusWithName(paramBlock.getGenusName()).isFunctionBlock())
						expression.addAll(getFullFunction(paramBlock));
					else if(blockConnector.getType().equals("string") && !codeblocks.Block.getBlock(number.longValue()).getLabelPrefix().equals("get "))
						expression.add(getExpressionData(paramBlock.getId(), "\""+paramBlock.getLabel()+"\""));
					else
						expression.add(getExpressionData(paramBlock.getId(), paramBlock.getLabel()));
				}
			}
			return expression;
		}
		else if(blockGenus.isCommandBlock() && codeblocks.Block.getBlock(number.longValue()).getLabelPrefix().equals("reset "))
		{
			expression.add(getExpressionData(0, block.getLabel()));
			expression.add(getExpressionData(0, "="));
			for (Variable variable : variableDecl) {
				if(variable.getName().equals(block.getLabel()))
				{
					if(variable.getType().equals("string"))
						expression.add(getExpressionData(0, "\""+getBlock(variable.getValue().get(0).getId()).getLabel()+"\""));
					else expression.add(getExpressionData(0, getBlock(variable.getValue().get(0).getId()).getLabel()));
					break;
				}
			}
			return expression;
		}
		else if(blockGenus.isCommandBlock() && codeblocks.Block.getBlock(number.longValue()).getLabelSuffix().equals("+="))
		{
			expression.add(getExpressionData(0, block.getLabel()));
			
			BlockConnector blockConnector = block.getSockets().getBlockConnectors().get(0);
			if(blockConnector != null && blocksMap.get(blockConnector.getConnectBlockId()) != null)
			{
				Block paramBlock = blocksMap.get(blockConnector.getConnectBlockId());
				expression.add(getExpressionData(0, "="+block.getLabel()+"+"+paramBlock.getLabel()));
			}
			return expression;
		}
		else if(blockGenus.isCommandBlock() && codeblocks.Block.getBlock(number.longValue()).getLabelSuffix().equals("-="))
		{
			expression.add(getExpressionData(0, block.getLabel()));
			BlockConnector blockConnector = block.getSockets().getBlockConnectors().get(0);
			if(blockConnector != null && blocksMap.get(blockConnector.getConnectBlockId()) != null)
			{
				Block paramBlock = blocksMap.get(blockConnector.getConnectBlockId());
				expression.add(getExpressionData(0, "="+block.getLabel()+"-"+paramBlock.getLabel()));
			}
			return expression;
			
		}
		
		expression.add(getExpressionData(block.getId(), block.getGenusName()));
		
		if(blockGenus.isCommandBlock())
		{
			expression.add(getExpressionData(0, "("));
			
			if(block.getSockets() != null)
			{
				int count = 0;
				for (BlockConnector blockConnector : block.getSockets().getBlockConnectors()) {
					Block paramBlock = blocksMap.get(blockConnector.getConnectBlockId());
					if(count > 0)
					{
						expression.add(getExpressionData(0, ","));
					}
					if(paramBlock != null)
					{
						Number paramBlockId = paramBlock.getId();
						if(getGenusWithName(paramBlock.getGenusName()).isFunctionBlock())
							expression.addAll(getFullFunction(paramBlock));
						else if((blockConnector.getType().equals("string") || blockConnector.getType().equals("style") || blockConnector.getType().equals("tomorrow-now") || blockConnector.getType().equals("color")) && !codeblocks.Block.getBlock(paramBlockId.longValue()).getLabelPrefix().equals("get "))
							expression.add(getExpressionData(paramBlock.getId(), "\""+paramBlock.getLabel()+"\""));
						else
							expression.add(getExpressionData(paramBlock.getId(), paramBlock.getLabel()));
					}
					count ++;
				}
			}
			expression.add(getExpressionData(0, ")"));
			
		}
		
		return expression;
	}

	private ExpressionData getExpressionData(int id, String data) {
		ExpressionData expressionData = new ExpressionData();
		expressionData.setData(data);
		expressionData.setId(id);
		return expressionData;
	}

	public Boolean isNumber(String number) {
		try {
			Integer.parseInt(number);
			return true;
		} catch (NumberFormatException ex) {

		}
		return false;
	}

	public Block getBlock(int id)
	{
		return blocksMap.get(id);
	}
	
	public String getDataBlockType(int id)
	{
		Block block = blocksMap.get(id);
		if(BlockGenus.getGenusWithName(block.getGenusName()).isDataBlock()){
			if(block.getPlug() != null )
			{
				return block.getPlug().getBlockConnectors().getType();
			}
			else
			{
				return "";
			}
		}
		else{
			return "";
		}
	}
	
	public BlockGenus getGenusWithName(String blockName)
	{
		return BlockGenus.getGenusWithName(blockName);
	}
	public List<Variable> getVariableDeclBlocks()
	{
		return variableDecl;
	}
	public List<Block> getDifferentCommand() {
		return differentCommand;
	}
	
	public List<ExpressionData> getIfCondition(Block commandBlock)
	{
		List<ExpressionData> conditionData = new ArrayList<ExpressionData>();
		if(commandBlock.getSockets() != null)
		{
			for (BlockConnector ifConnector : commandBlock.getSockets().getBlockConnectors())
			{
				if(ifConnector == null)
				{
					continue;
				}
				
				if(ifConnector.getLabel().equals("condition"))
				{
					conditionData.add(getExpressionData(ifConnector.getConnectBlockId(), Integer.toString(ifConnector.getConnectBlockId())));
					//while (isCompleteProcessFunction(conditionData));
					conditionData = getFullFunction(ifConnector.getConnectBlockId());
				}
			}
		}
		
		//while(isCompleteProcessFunction(conditionData));
		return conditionData;
	}
	public List<ExpressionData> getFullFunction(Block functionBlock)
	{
		List<ExpressionData> functionData = new ArrayList<ExpressionData>();

		functionData.add(getExpressionData(functionBlock.getId(),Integer.toString(functionBlock.getId())));
		
		while(isCompleteProcessFunction(functionData));
		return functionData;
	}
	public List<ExpressionData> getFullFunction(int blockId)
	{
		return getFullFunction(getBlock(blockId));
	}
	public List<Block> getThenStatements(Block block)
	{
		List<Block> thenStatements = new ArrayList<Block>();
		if(block.getSockets() != null)
		{
			for (BlockConnector ifConnector : block.getSockets().getBlockConnectors())
			{
				if(ifConnector == null)
				{
					continue;
				}
				else if(ifConnector.getLabel().equals("then"))
				{
					do
					{
						if(getBlock(ifConnector.getConnectBlockId()) != null)
						{
							thenStatements.add(getBlock(ifConnector.getConnectBlockId()));
							block = getBlock(block.getAfterBlockId());
						}
					}
					while( block != null && block.getAfterBlockId() > 0 );
					
				}
				
			}
		}
		return thenStatements;
	}
	
	public List<Block> getElseStatements(Block block)
	{
		List<Block> elseStatements = new ArrayList<Block>();
		if(block.getSockets() != null)
		{
			for (BlockConnector ifConnector : block.getSockets().getBlockConnectors())
			{
				if(ifConnector == null)
				{
					continue;
				}
				else if(ifConnector.getLabel().equals("else"))
				{
					do
					{
						if(getBlock(ifConnector.getConnectBlockId()) != null)
						{
							elseStatements.add(getBlock(ifConnector.getConnectBlockId()));
							block = getBlock(block.getAfterBlockId());
						}
					}
					while( block != null && block.getAfterBlockId() > 0 );
					
				}
				
			}
		}
		return elseStatements;
	}
	
	public List<Block> getAllFunction() {
		return allFunction;
	}
	public List<Block> getAllBlocks() {
		return allBlocks;
	} 
}
