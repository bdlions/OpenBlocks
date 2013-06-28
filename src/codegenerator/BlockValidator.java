package codegenerator;

import general.DisplayMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import codeblocks.BlockGenus;
import codegenerator.xmlbind.Block;
import codegenerator.xmlbind.BlockConnector;
import codegenerator.xmlbind.Plug;

/**
 * This is the class for validation
 * validate when code is generated
 * validated all blocks if a block has empty connectors or 
 * allowed to generate code
 * */

public class BlockValidator {
	private static HashMap<Integer, Block> blocksMap = new HashMap<Integer, Block>();
	private static List<String> errors = new ArrayList<String>();
	private static int missing_param_count = 0;
	private static List<String> variableNameList = new ArrayList<String>();
	
	/**
	 * This function validate a list of blocks
	 * and store error result
	 * */
	public static List<String> validateAll(List<Block> allBlocks)
	{
		variableNameList = new ArrayList<String>();
		blocksMap = new HashMap<Integer, Block>();
		errors = new ArrayList<String>();
		/**
		 * differentiate all variable list
		 * and others blocks
		 * */
		for (Block block : allBlocks) 
		{
			blocksMap.put(block.getId(), block);
			
			if(block.getGenusName().contains("decl"))
			{
				variableNameList.add(BlockGenus.getGenusWithName(block.getGenusName()).getInitialLabel());
			}
		}
		for (Block block : allBlocks) {
			/**
			 * validate a single block
			 * */
			validate(block);
		}
		
		return errors; 
	}
	
	/**
	 * validate a single block
	 * */
	public static List<String> validate(Block block)
	{
		BlockGenus blockGenus = BlockGenus.getGenusWithName(block.getGenusName());
		Number number = block.getId();
		String customMessage = "";
		if(BlockGenus.getGenusWithName(block.getGenusName()).getLabelPrefix().contains("set") || BlockGenus.getGenusWithName(block.getGenusName()).getLabelPrefix().contains("reset") || BlockGenus.getGenusWithName(block.getGenusName()).getLabelPrefix().contains("get") || BlockGenus.getGenusWithName(block.getGenusName()).getLabelSuffix().contains("+=") || BlockGenus.getGenusWithName(block.getGenusName()).getLabelSuffix().contains("-="))
		{
			if(!variableNameList.contains(BlockGenus.getGenusWithName(block.getGenusName()).getInitialLabel()))
			{
				/**
				 * If a variable not declared but 
				 * trying to initiate tthe set command for that varialbe
				 * then generate an error
				 * */
				customMessage = DisplayMessage.convertedTextInSelectedLanguage("Please declare the variable named");
				errors.add(customMessage+" '"+block.getLabel()+"'");
				//errors.add("Please declare the variable named "+BlockGenus.getGenusWithName(block.getGenusName()).getInitialLabel());
			}
		}
		
		if(blockGenus.isDataBlock() && block.getPlug().getBlockConnectors().getConnectBlockId() <= 0)
		{ 
			/**
			 * data blocks are alone
			 * so we cannnot set that type of block without others connected block
			 * */
			customMessage = DisplayMessage.convertedTextInSelectedLanguage("can't be added as single data block");
			errors.add("'"+block.getLabel()+"' "+customMessage);			
			//errors.add("You can't add '"+block.getLabel()+"' as single data block ");
		}
		if(blockGenus.isDeclaration())
		{
			/**
			 * When a declaration block found
			 * declaration blocks should have only one connector if there is more than one connector thane we found an error
			 * if we found no connector then we have found another error becasue we have always declared varialbe with initialize
			 * if there has one connector but connected id is not valid then ther is another error
			 * */
			List<BlockConnector> blockConnectors = block.getSockets().getBlockConnectors();
			if (blockConnectors.size() > 1) 
			{
				customMessage = DisplayMessage.convertedTextInSelectedLanguage("Initialize correctly for variable named");
				errors.add(customMessage+" '"+block.getLabel()+"'");
				//errors.add("Declaration block " + block.getLabel() + "  has more than one parameters but declaration block should only one param.");
			} 
			else if (blockConnectors.size() < 1) 
			{
				customMessage = DisplayMessage.convertedTextInSelectedLanguage("Initialize correctly for variable named");
				errors.add(customMessage+" '"+block.getLabel()+"'");
				//errors.add("Declaration block " + block.getLabel() + "  has no parameter but  declaration block need at least one param.");
			} 
			else if (blockConnectors.get(0).getConnectBlockId() <= 0) 
			{
				customMessage = DisplayMessage.convertedTextInSelectedLanguage("Initialize variable named");
				errors.add(customMessage+" '"+block.getLabel()+"'");
				//errors.add("Declaration block " + block.getLabel() + "  has no parameter but need at least one param.");
			} 
			else if(!blocksMap.containsKey(blockConnectors.get(0).getConnectBlockId())){
				customMessage = DisplayMessage.convertedTextInSelectedLanguage("Initialize correctly for variable named");
				errors.add(customMessage+" '"+block.getLabel()+"'");
				//errors.add("Declaration block " + block.getLabel() + "  has an illigal parameter that doesn't really exist.");
			}			
		}
		else if(blockGenus.isCommandBlock() && codeblocks.Block.getBlock(number.longValue()).getLabelPrefix().equals("set "))
		{
			/**
			 * set method validation
			 * set block should atleast a socketso that we can set a block in the connector
			 * */
			if(block.getSockets() == null)
			{
				customMessage = DisplayMessage.convertedTextInSelectedLanguage("Socket isn't defined for block named");
				errors.add(customMessage+" "+block.getGenusName());
				//errors.add(block.getGenusName() + "  block has no socket defined.");
			}
			else
			{
				/**
				 * When a set block found
				 * set blocks should have only one connector if there is more than one connector thane we found an error
				 * if we found no connector then we have found another error becasue we have always a block for a set block
				 * if there has one connector but connected id is not valid then ther is another error
				 * */
				List<BlockConnector> blockConnectors = block.getSockets().getBlockConnectors();
				if (blockConnectors.size() > 1) 
				{
					customMessage = DisplayMessage.convertedTextInSelectedLanguage("Invalid set for variable named");
					errors.add(customMessage+" '"+block.getLabel()+"'");
					//errors.add(block.getGenusName() + "  block has more than one parameters but set method should only one param.");
				} 
				else if (blockConnectors.size() < 1) 
				{
					customMessage = DisplayMessage.convertedTextInSelectedLanguage("Invalid set for variable named");
					errors.add(customMessage+" '"+block.getLabel()+"'");
					//errors.add(block.getGenusName() + "  block has no parameter but set method need at least one param.");
				} 
				else if (blockConnectors.get(0).getConnectBlockId() <= 0) 
				{
					customMessage = DisplayMessage.convertedTextInSelectedLanguage("Set variable named");
					errors.add(customMessage+" '"+block.getLabel()+"'");
					//errors.add(block.getGenusName() + "  block has no parameter but set method need at least one param.");
				} 
				else if(!blocksMap.containsKey(blockConnectors.get(0).getConnectBlockId()))
				{
					customMessage = DisplayMessage.convertedTextInSelectedLanguage("Invalid set for variable named");
					errors.add(customMessage+" '"+block.getLabel()+"'");
					//errors.add(block.getGenusName() + "  block has an illigal parameter that doesn't really exist.");
				}
			}
		} 
		else if(blockGenus.isCommandBlock() && (block.getGenusName().equals("if") ||  block.getGenusName().equals("ifelse")))
		{
			
			//List<ExpressionData> conditionData = new ArrayList<ExpressionData>();
			/**
			 * if "if" or "ifelse" blocks blockconnectors are invalid id or has no connecoter 
			 * then add an erro
			 * */
			if(block.getSockets() != null){
				for (BlockConnector ifConnector : block.getSockets().getBlockConnectors()){
					
					if(ifConnector == null){
						continue;
					}
					if(ifConnector.getLabel().equals("then"))
					{
						if(ifConnector.getConnectBlockId() <= 0)
						{
							customMessage = DisplayMessage.convertedTextInSelectedLanguage("block is missing it's then block");
							errors.add(block.getGenusName()+" "+customMessage);
							//errors.add(block.getGenusName()+" block is missing it's then block");
						} 
						else
						{
							//conditionData.add(getExpressionData(ifConnector.getConnectBlockId(), Integer.toString(ifConnector.getConnectBlockId())));
							//while (isCompleteProcessFunction(conditionData));
						}
					}
					if(ifConnector.getLabel().equals("else"))
					{
						if(ifConnector.getConnectBlockId() <= 0)
						{
							customMessage = DisplayMessage.convertedTextInSelectedLanguage("block is missing it's else block");
							errors.add(block.getGenusName()+" "+customMessage);
							//errors.add(block.getGenusName()+" block is missing it's else block");
						} 
						else
						{
							//conditionData.add(getExpressionData(ifConnector.getConnectBlockId(), Integer.toString(ifConnector.getConnectBlockId())));
							//while (isCompleteProcessFunction(conditionData));
						}
					}
					if(ifConnector.getLabel().equals("condition"))
					{
						if(ifConnector.getConnectBlockId() <= 0)
						{
							customMessage = DisplayMessage.convertedTextInSelectedLanguage("block is missing it's condition block");
							errors.add(block.getGenusName()+" "+customMessage);
							//errors.add(block.getGenusName()+" block is missing it's condition block");
						} 
						else
						{
							//conditionData.add(getExpressionData(ifConnector.getConnectBlockId(), Integer.toString(ifConnector.getConnectBlockId())));
							//while (isCompleteProcessFunction(conditionData));
						}
					}
				}
			}			
		}  
		else if(blockGenus.isCommandBlock())
		{
			/**
			 * if the block is a command block
			 * */
			if(block.getSockets() == null)
			{
				/**
				 * No parameters in command block
				 * */
				//errors.add(block.getGenusName() + "  block has a null socket");
			}
			else
			{
				/**
				 * Calculate how many connectoer are missing and
				 * add error
				 * */
				int missing_param_count = 0;
				for (BlockConnector blockConnector : block.getSockets().getBlockConnectors()) 
				{
					//Block paramBlock = blocksMap.get(blockConnector.getConnectBlockId());
					if( blockConnector.getConnectBlockId() <= 0)
					{
						missing_param_count ++;
					}
				}
				if(missing_param_count == 1)
				{					
					customMessage = DisplayMessage.convertedTextInSelectedLanguage("parameter is missing in block named");
					errors.add(missing_param_count+" "+customMessage+" "+blockGenus.getInitialLabel());
					//errors.add(block.getGenusName() + " block is missing "+ missing_param_count+" params");
				}
				else if(missing_param_count > 1)
				{					
					customMessage = DisplayMessage.convertedTextInSelectedLanguage("parameters are missing in block named");
					errors.add(missing_param_count+" "+customMessage+" "+blockGenus.getInitialLabel());
					//errors.add(block.getGenusName() + " block is missing "+ missing_param_count+" params");
				}
			}
		}
		else if(blockGenus.isFunctionBlock())
		{

			/**
			 * adding function bloks error and counting how many params are missed
			 * */

			ArrayList<String> nameList = new ArrayList<String>();
			nameList.add("and");
			nameList.add("or");
			nameList.add("not");
			nameList.add("abs");
			nameList.add("sum");
			nameList.add("double_sum_integer");
			nameList.add("double_sum_double");
			nameList.add("difference");
			nameList.add("product");
			nameList.add("quotient");
			nameList.add("integernot-equalsinteger");
			nameList.add("integerequalsinteger");
			nameList.add("integerlessthaninteger");
			nameList.add("integerlessthanorequaltointeger");
			nameList.add("integergreaterthaninteger");
			nameList.add("integergreaterthanorequaltointeger");
			nameList.add("doublenot-equalsdouble");
			nameList.add("doubleequalsdouble");
			nameList.add("doublelessthandouble");
			nameList.add("doublelessthanorequaltodouble");
			nameList.add("doublegreaterthandouble");
			nameList.add("doublegreaterthanorequaltodouble");
			nameList.add("booleannot-equalsboolean");
			nameList.add("booleanequalsboolean");
			String genusName = blockGenus.getGenusName();
			//checking the validation of location of expression. A && B, A < B etc should be inside a condition
			if(nameList.contains(genusName) && block.getPlug() != null && block.getPlug().getBlockConnectors() != null && block.getPlug().getBlockConnectors().getConnectBlockId() == 0 )
			{
				customMessage = DisplayMessage.convertedTextInSelectedLanguage("Invalid block with");
				errors.add(customMessage+" '"+block.getLabel()+"'");
			}
			

			List<ExpressionData> listExpression = new ArrayList<ExpressionData>();
			listExpression.add(getExpressionData(block.getId(), Integer.toString(block.getId())));
			while (isCompleteProcessFunction(listExpression));
			if(missing_param_count > 0 )
			{
				if(missing_param_count == 1)
				{					
					customMessage = DisplayMessage.convertedTextInSelectedLanguage("parameter is missing in block named");
					errors.add(missing_param_count+" "+customMessage+" "+blockGenus.getInitialLabel());
				}
				else if(missing_param_count > 1)
				{					
					customMessage = DisplayMessage.convertedTextInSelectedLanguage("parameters are missing in block named");
					errors.add(missing_param_count+" "+customMessage+" "+blockGenus.getInitialLabel());
				}
				//errors.add(blockGenus.getGenusName() + " block is missing "+ missing_param_count+" params");
				missing_param_count = 0;
			}
		}
		return errors;
	}
	
	
	public static Boolean isCompleteProcessFunction(List<ExpressionData> list) {
		boolean found = false;
		List<ExpressionData> tempList = new ArrayList<ExpressionData>();
		String customMessage = "";
		for (int i = 0; i < list.size(); i++) {
			if (isNumber(list.get(i).getData())) {

				if(!blocksMap.containsKey(list.get(i).getId()))
				{
					customMessage = DisplayMessage.convertedTextInSelectedLanguage("An illegal block has been found which id is");
					errors.add(customMessage+" "+list.get(i).getId()+".");					
					//errors.add("An illigal block has been found whose id is "+list.get(i).getId()+".");
					return false;
				}
				
				Block block = blocksMap.get(list.get(i).getId());
				if(block == null)
				{
					customMessage = DisplayMessage.convertedTextInSelectedLanguage("An illegal block has been found which id is");
					errors.add(customMessage+" "+list.get(i).getId()+".");
					//errors.add("An illigal block has been found whose id is "+list.get(i).getId()+".");
					return false; 
				}
				
				BlockGenus blockGenus = BlockGenus.getGenusWithName(block.getGenusName());

				if (blockGenus.isDataBlock()) 
				{
					tempList.add(getExpressionData(block.getId(), block.getGenusName()));
					continue;
				} 
				else if (blockGenus.isFunctionBlock()) 
				{
					/**
					 * If there is a not a plug
					 * */
					if (block.getPlug() == null) 
					{
						customMessage = DisplayMessage.convertedTextInSelectedLanguage("block is missing the plug");
						errors.add(block.getGenusName()+" "+customMessage);						
						//errors.add(block.getGenusName()+ " block is missing the plug");
					} 
					else 
					{
						Plug blockPlug = block.getPlug();
						/**
						 * If the block plug has a no connector
						 * */
						if (blockPlug.getBlockConnectors() == null) 
						{
							customMessage = DisplayMessage.convertedTextInSelectedLanguage("block is missing the connector");
							errors.add(block.getGenusName()+" "+customMessage);
							//errors.add(block.getGenusName()+ " block is missing the connector");
						} 
						else
						{
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
								if (block.getSockets() == null) {
									
									tempList.add(getExpressionData(block.getId(), block.getGenusName()));

								} else {
									
									List<BlockConnector> connectorList = block.getSockets().getBlockConnectors();
									BlockConnector leftConnector = connectorList.get(0);
									BlockConnector rightConnector = connectorList.get(1);
									Block leftBlock = blocksMap.get(leftConnector.getConnectBlockId());
									Block rightBlock = blocksMap.get(rightConnector.getConnectBlockId());

									tempList.add(getExpressionData(0, "("));
									/**
									 * There will be no left block
									 * */
									if (leftBlock == null) 
									{
										customMessage = DisplayMessage.convertedTextInSelectedLanguage("block is missing the left part");
										errors.add(block.getGenusName()+" "+customMessage);
										//errors.add(block.getGenusName()+ " block is missing it's left part");
									} 
									else
									{
										tempList.add(getExpressionData(leftBlock.getId(), Integer.toString(leftBlock.getId())));
									}
									
									tempList.add(getExpressionData(block.getId(), block.getGenusName()));
									
									if (rightBlock == null) 
									{
										customMessage = DisplayMessage.convertedTextInSelectedLanguage("block is missing the right part");
										errors.add(block.getGenusName()+" "+customMessage);
										//errors.add(block.getGenusName()+ " block is missing it's right part");
									} 
									else
									{
										tempList.add(getExpressionData(rightBlock.getId(), Integer.toString(rightBlock.getId())));
									}
									tempList.add(getExpressionData(0, ")"));
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
								//int missing_param_count = 0;
								for (BlockConnector blockConnector : connectorList) {
									Block functionBlock = blocksMap.get(blockConnector.getConnectBlockId());
									if (paramCount > 0) {
										tempList.add(getExpressionData(0, ","));
									}
									if (functionBlock != null) {
										tempList.add(getExpressionData(functionBlock.getId(), Integer.toString(functionBlock.getId())));
									}else{
										missing_param_count ++;
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
	/**
	 * Checking a string is number
	 * */
	public static Boolean isNumber(String number) {
		try {
			Integer.parseInt(number);
			return true;
		} catch (NumberFormatException ex) {

		}
		return false;
	}
	
	/**
	 * getting expression data
	 * */
	private static ExpressionData getExpressionData(int id, String data) {
		ExpressionData expressionData = new ExpressionData();
		expressionData.setData(data);
		expressionData.setId(id);
		return expressionData;
	}
}
