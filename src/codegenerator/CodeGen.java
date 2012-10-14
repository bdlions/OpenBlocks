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
	public String getGenerateCode(String xmlString) {
		// TODO Auto-generated method stub
		allBlocks = XMLToBlockGenerator.generateBlocks(xmlString);
		HashMap<Integer, Block> functionsMap = new HashMap<Integer, Block>();

		// XMLToBlockGenerator.generateBlocks("<Pages><Page page-name=\"Blocks\" page-color=\"30 30 30\" page-width=\"797\" page-infullview=\"yes\" page-drawer=\"Blocks\" ><PageBlocks><Block id=\"125\" genus-name=\"product\" ><Label>x</Label><Location><X>427</X><Y>108</Y></Location><Plug><BlockConnector connector-kind=\"plug\" connector-type=\"integer\" init-type=\"integer\" label=\"\" position-type=\"mirror\" con-block-id=\"103\" ></BlockConnector></Plug><Sockets num-sockets=\"2\" ><BlockConnector connector-kind=\"socket\" connector-type=\"integer\" init-type=\"integer\" label=\"\" position-type=\"bottom\" con-block-id=\"127\" ></BlockConnector><BlockConnector connector-kind=\"socket\" connector-type=\"integer\" init-type=\"integer\" label=\"\" position-type=\"bottom\" con-block-id=\"115\" ></BlockConnector></Sockets></Block><Block id=\"115\" genus-name=\"userAge\" ><Label>Age</Label><Location><X>516</X><Y>111</Y></Location><Plug><BlockConnector connector-kind=\"plug\" connector-type=\"integer\" init-type=\"integer\" label=\"\" position-type=\"single\" con-block-id=\"125\" ></BlockConnector></Plug><Sockets num-sockets=\"2\" ><BlockConnector connector-kind=\"socket\" connector-type=\"integer\" init-type=\"integer\" label=\"p\" position-type=\"single\" con-block-id=\"117\" ></BlockConnector><BlockConnector connector-kind=\"socket\" connector-type=\"integer\" init-type=\"integer\" label=\"x\" position-type=\"single\" con-block-id=\"119\" ></BlockConnector></Sockets></Block><Block id=\"119\" genus-name=\"integer\" ><Label>1.2</Label><Location><X>580</X><Y>135</Y></Location><Plug><BlockConnector connector-kind=\"plug\" connector-type=\"integer\" init-type=\"integer\" label=\"\" position-type=\"mirror\" con-block-id=\"115\" ></BlockConnector></Plug></Block><Block id=\"117\" genus-name=\"random-color\" ><Label>0</Label><Location><X>580</X><Y>111</Y></Location><Plug><BlockConnector connector-kind=\"plug\" connector-type=\"integer\" init-type=\"integer\" label=\"\" position-type=\"mirror\" con-block-id=\"115\" ></BlockConnector></Plug></Block><Block id=\"127\" genus-name=\"integer\" ><Label>1</Label><Location><X>437</X><Y>135</Y></Location><Plug><BlockConnector connector-kind=\"plug\" connector-type=\"integer\" init-type=\"integer\" label=\"\" position-type=\"mirror\" con-block-id=\"125\" ></BlockConnector></Plug></Block><Block id=\"129\" genus-name=\"and\" ><Label>and</Label><Location><X>35</X><Y>58</Y></Location><Plug><BlockConnector connector-kind=\"plug\" connector-type=\"boolean\" init-type=\"boolean\" label=\"\" position-type=\"mirror\" ></BlockConnector></Plug><Sockets num-sockets=\"2\" ><BlockConnector connector-kind=\"socket\" connector-type=\"boolean\" init-type=\"boolean\" label=\"\" position-type=\"bottom\" con-block-id=\"133\" ></BlockConnector><BlockConnector connector-kind=\"socket\" connector-type=\"boolean\" init-type=\"boolean\" label=\"\" position-type=\"bottom\" con-block-id=\"103\" ></BlockConnector></Sockets></Block><Block id=\"103\" genus-name=\"greaterthan\" ><Label>&gt;</Label><Location><X>158</X><Y>61</Y></Location><Plug><BlockConnector connector-kind=\"plug\" connector-type=\"boolean\" init-type=\"boolean\" label=\"\" position-type=\"mirror\" con-block-id=\"129\" ></BlockConnector></Plug><Sockets num-sockets=\"2\" ><BlockConnector connector-kind=\"socket\" connector-type=\"integer\" init-type=\"integer\" label=\"\" position-type=\"bottom\" con-block-id=\"105\" ></BlockConnector><BlockConnector connector-kind=\"socket\" connector-type=\"integer\" init-type=\"integer\" label=\"\" position-type=\"bottom\" con-block-id=\"125\" ></BlockConnector></Sockets></Block><Block id=\"105\" genus-name=\"born\" ><Label>AdvancedAge</Label><Location><X>168</X><Y>64</Y></Location><Plug><BlockConnector connector-kind=\"plug\" connector-type=\"integer\" init-type=\"integer\" label=\"\" position-type=\"single\" con-block-id=\"103\" ></BlockConnector></Plug><Sockets num-sockets=\"4\" ><BlockConnector connector-kind=\"socket\" connector-type=\"string\" init-type=\"string\" label=\"tcountry\" position-type=\"single\" con-block-id=\"107\" ></BlockConnector><BlockConnector connector-kind=\"socket\" connector-type=\"integer\" init-type=\"integer\" label=\"tcolor\" position-type=\"single\" con-block-id=\"109\" ></BlockConnector><BlockConnector connector-kind=\"socket\" connector-type=\"integer\" init-type=\"integer\" label=\"dateborn\" position-type=\"single\" con-block-id=\"111\" ></BlockConnector><BlockConnector connector-kind=\"socket\" connector-type=\"integer\" init-type=\"integer\" label=\"A\" position-type=\"single\" con-block-id=\"113\" ></BlockConnector></Sockets></Block><Block id=\"113\" genus-name=\"integer\" ><Label>0</Label><Location><X>326</X><Y>136</Y></Location><Plug><BlockConnector connector-kind=\"plug\" connector-type=\"integer\" init-type=\"integer\" label=\"\" position-type=\"mirror\" con-block-id=\"105\" ></BlockConnector></Plug></Block><Block id=\"111\" genus-name=\"integer\" ><Label>1960</Label><Location><X>326</X><Y>112</Y></Location><Plug><BlockConnector connector-kind=\"plug\" connector-type=\"integer\" init-type=\"integer\" label=\"\" position-type=\"mirror\" con-block-id=\"105\" ></BlockConnector></Plug></Block><Block id=\"109\" genus-name=\"black\" ><Label>1</Label><Location><X>326</X><Y>88</Y></Location><Plug><BlockConnector connector-kind=\"plug\" connector-type=\"integer\" init-type=\"integer\" label=\"\" position-type=\"mirror\" con-block-id=\"105\" ></BlockConnector></Plug></Block><Block id=\"107\" genus-name=\"USA\" ><Label>USA</Label><Location><X>326</X><Y>64</Y></Location><Plug><BlockConnector connector-kind=\"plug\" connector-type=\"string\" init-type=\"string\" label=\"\" position-type=\"mirror\" con-block-id=\"105\" ></BlockConnector></Plug></Block><Block id=\"133\" genus-name=\"true\" ><Label>true</Label><Location><X>45</X><Y>137</Y></Location><Plug><BlockConnector connector-kind=\"plug\" connector-type=\"boolean\" init-type=\"boolean\" label=\"\" position-type=\"mirror\" con-block-id=\"129\" ></BlockConnector></Plug></Block></PageBlocks></Page><Page page-name=\"Code\" page-color=\"30 30 30\" page-width=\"966\" page-infullview=\"yes\" page-drawer=\"Code\" ></Page></Pages>");

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
			/**
			 * Adding variables
			 * 
			 */
			else if(genus.isVariableDeclBlock())
			{
				String pattern = "[^a-zA-Z_]+|[^a-zA-Z_0-9]+//g";
				Variable variable = new Variable();
				variable.setName(block.getLabel().replaceAll(pattern, ""));
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
							listExpressionBlocks.add(getExpressionData(valueBlock.getId(), Integer.toString(valueBlock.getId())));
							while (isCompleteProcessFunction(listExpressionBlocks)) ;
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
		
		try
		{
			Configuration cfg = new Configuration();
			String templateLocation = ((System.getProperty("application.home") != null) ? System
					.getProperty("application.home") : System
					.getProperty("user.dir"))
					+ "/templates/";
	
			cfg.setDirectoryForTemplateLoading(new File(templateLocation));
			cfg.setObjectWrapper(new DefaultObjectWrapper());
	
			Template temp = cfg.getTemplate("code.ftl");
	
			Map<String, CodeGen> root = new HashMap<String, CodeGen>();
			root.put("codeGen", this);
	
			final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			final Writer output = new OutputStreamWriter(outputStream);
	
			temp.process(root, output);
			output.flush();
			return outputStream.toString();
		}
		catch (IOException exception) {
			exception.printStackTrace();
		} catch (TemplateException exception) {
			exception.printStackTrace();
		}
		return null;
	}

	private void printBlocks(List<ExpressionData> functionBlocks) {
		for (ExpressionData expressionData : functionBlocks) {
			if (blocksMap.containsKey(expressionData.getId())) {
				Block functionBlock = blocksMap.get(expressionData.getId());
				System.out.print(functionBlock.getLabel());
			} else {
				System.out.print(expressionData.getData());
			}
		}
		System.out.println();
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
				
				if(ifConnector.getLabel().equals("test"))
				{
					conditionData.add(getExpressionData(ifConnector.getConnectBlockId(), Integer.toString(ifConnector.getConnectBlockId())));
					while (isCompleteProcessFunction(conditionData));
				}
			}
		}
		
		while(isCompleteProcessFunction(conditionData));
		return conditionData;
	}
	public List<ExpressionData> getFullFunction(Block functionBlock)
	{
		List<ExpressionData> functionData = new ArrayList<ExpressionData>();

		functionData.add(getExpressionData(functionBlock.getId(),Integer.toString(functionBlock.getId())));
		
		while(isCompleteProcessFunction(functionData));
		return functionData;
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
