package codegenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import codeblocks.BlockGenus;
import codegenerator.xmlbind.Block;
import codegenerator.xmlbind.BlockConnector;
import codegenerator.xmlbind.Plug;

public class BlockValidator {
	private static HashMap<Integer, Block> blocksMap = new HashMap<Integer, Block>();
	private static List<String> errors = new ArrayList<String>();
	private static int missing_param_count = 0;
	
	public static List<String> validateAll(List<Block> allBlocks)
	{
		blocksMap = new HashMap<Integer, Block>();
		errors = new ArrayList<String>();
		
		for (Block block : allBlocks) 
		{
			blocksMap.put(block.getId(), block);
		}
		for (Block block : allBlocks) {
			validate(block);
		}
		
		return errors; 
	}
	
	public static List<String> validate(Block block)
	{
		BlockGenus blockGenus = BlockGenus.getGenusWithName(block.getGenusName());
		Number number = block.getId();
		
		/**
		 * set method validation
		 * */
		if(blockGenus.isCommandBlock() && codeblocks.Block.getBlock(number.longValue()).getLabelPrefix().equals("set "))
		{
			if(block.getSockets() == null)
			{
				errors.add(block.getGenusName() + "  block has no socket defined.");
			}
			else{
				List<BlockConnector> blockConnectors = block.getSockets().getBlockConnectors();
				if (blockConnectors.size() > 1) {
					errors.add(block.getGenusName() + "  block has more than one parameters but set method should only one param.");
				} else if (blockConnectors.size() < 1) {
					errors.add(block.getGenusName() + "  block has no parameter but set method need at least one param.");
				} else if (blockConnectors.get(0).getConnectBlockId() <= 0) {
					errors.add(block.getGenusName() + "  block has no parameter but set method need at least one param.");
				} else if(!blocksMap.containsKey(blockConnectors.get(0).getConnectBlockId())){
					errors.add(block.getGenusName() + "  block has an illigal parameter that doesn't really exist.");
				}
			}
		} else if(blockGenus.isCommandBlock() && (block.getGenusName().equals("if") ||  block.getGenusName().equals("ifelse"))){
			
			//List<ExpressionData> conditionData = new ArrayList<ExpressionData>();
			if(block.getSockets() != null){
				for (BlockConnector ifConnector : block.getSockets().getBlockConnectors()){
					
					if(ifConnector == null){
						continue;
					}
					if(ifConnector.getLabel().equals("condition")){
						if(ifConnector.getConnectBlockId() <= 0){
							errors.add(block.getGenusName()+" block is missing it's condition block");
						} else{
							//conditionData.add(getExpressionData(ifConnector.getConnectBlockId(), Integer.toString(ifConnector.getConnectBlockId())));
							//while (isCompleteProcessFunction(conditionData));
						}
					}
				}
			}
			
		}  else if(blockGenus.isCommandBlock()){
			/**
			 * if the block is a command block
			 * */
			if(block.getSockets() == null){
				/**
				 * No parameters in command block
				 * */
				errors.add(block.getGenusName() + "  block has a null socket");
			}else{
				int missing_param_count = 0;
				for (BlockConnector blockConnector : block.getSockets().getBlockConnectors()) {
					//Block paramBlock = blocksMap.get(blockConnector.getConnectBlockId());
					if( blockConnector.getConnectBlockId() <= 0){
						missing_param_count ++;
					}
				}
				if(missing_param_count > 0){
					errors.add(block.getGenusName() + "  block has missed "+missing_param_count+" parameter");
				}
			}
		}else if(blockGenus.isFunctionBlock()){
			List<ExpressionData> listExpression = new ArrayList<ExpressionData>();
			listExpression.add(getExpressionData(block.getId(), Integer.toString(block.getId())));
			while (isCompleteProcessFunction(listExpression));
			if(missing_param_count > 0 ){
				errors.add(blockGenus.getGenusName() + " block is missing "+ missing_param_count+" params");
				missing_param_count = 0;
			}
		}
		
		
		return errors;
	}
	
	
	public static Boolean isCompleteProcessFunction(List<ExpressionData> list) {
		boolean found = false;
		List<ExpressionData> tempList = new ArrayList<ExpressionData>();

		for (int i = 0; i < list.size(); i++) {
			if (isNumber(list.get(i).getData())) {

				if(!blocksMap.containsKey(list.get(i).getId()))
				{
					errors.add("An illigal block has been found whose id is "+list.get(i).getId()+".");
					return false;
				}
				
				Block block = blocksMap.get(list.get(i).getId());
				if(block == null)
				{
					errors.add("An illigal block has been found whose id is "+list.get(i).getId()+".");
					return false; 
				}
				
				BlockGenus blockGenus = BlockGenus.getGenusWithName(block.getGenusName());

				if (blockGenus.isDataBlock()) {
					tempList.add(getExpressionData(block.getId(), block.getGenusName()));
					continue;
				} else if (blockGenus.isFunctionBlock()) {
					/**
					 * If there is a not a plug
					 * */
					if (block.getPlug() == null) {
						errors.add(block.getGenusName()+ " block is missing the plug");
					} else {
						Plug blockPlug = block.getPlug();
						/**
						 * If the block plug has a no connector
						 * */
						if (blockPlug.getBlockConnectors() == null) {
							errors.add(block.getGenusName()+ " block is missing the connector");
						} else{
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
									if (leftBlock == null) {
										errors.add(block.getGenusName()+ " block is missing it's left part");
									} else{
										tempList.add(getExpressionData(leftBlock.getId(), Integer.toString(leftBlock.getId())));
									}
									
									tempList.add(getExpressionData(block.getId(), block.getGenusName()));
									
									if (rightBlock == null) {
										errors.add(block.getGenusName()+ " block is missing it's right part");
									} else{
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
	public static Boolean isNumber(String number) {
		try {
			Integer.parseInt(number);
			return true;
		} catch (NumberFormatException ex) {

		}
		return false;
	}
	private static ExpressionData getExpressionData(int id, String data) {
		ExpressionData expressionData = new ExpressionData();
		expressionData.setData(data);
		expressionData.setId(id);
		return expressionData;
	}
}
