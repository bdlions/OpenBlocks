package codegenerator.xmlbind.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

public class DefaultSettings 
{
	public static BlockLangDef getSettings()
	{
		BlockLangDef blockLangDef = new BlockLangDef();
		
		List<BlockConnectorShape> connectorShapes = new ArrayList<BlockConnectorShape>();
		
		/**
		 * Integer shape
		 * */
		BlockConnectorShape integerShape = new BlockConnectorShape();
		integerShape.setShapeNumber(1);
		integerShape.setShapeType("integer");
		
		/**
		 * double shape
		 * */
		BlockConnectorShape doubleShape = new BlockConnectorShape();
		doubleShape.setShapeNumber(2);
		doubleShape.setShapeType("double");
		
		/**
		 * string shape
		 * */
		BlockConnectorShape stringShape = new BlockConnectorShape();
		stringShape.setShapeNumber(7);
		stringShape.setShapeType("string");
		
		
		/**
		 * boolean shape
		 * */
		BlockConnectorShape booleanShape = new BlockConnectorShape();
		booleanShape.setShapeNumber(4);
		booleanShape.setShapeType("boolean");
		
		/**
		 * cmd shape
		 * */
		BlockConnectorShape cmdShape = new BlockConnectorShape();
		cmdShape.setShapeNumber(14);
		cmdShape.setShapeType("cmd");
		
		
		/**
		 * Adding all shapes
		 * */
		connectorShapes.add(integerShape);
		connectorShapes.add(doubleShape);
		connectorShapes.add(booleanShape);
		connectorShapes.add(stringShape);
		connectorShapes.add(cmdShape);
		
		blockLangDef.setBlockConnectorShapes(connectorShapes);
		
		/**
		 * add block genuses
		 * */
		List<BlockGenus> blockGenuses = new ArrayList<BlockGenus>();
		
		/**
		 * If block creation
		 * */
		BlockGenus ifBlock = new BlockGenus();
		ifBlock.setKind("command");
		ifBlock.setInitLabel("if");
		ifBlock.setName("if");
		ifBlock.setColor("255 80 0");
		
		List<BlockConnector> ifConnectors = new ArrayList<BlockConnector>();
		
		BlockConnector conditionConnector = new BlockConnector();
		conditionConnector.setLabel("condition");
		conditionConnector.setConnectorKind("socket");
		conditionConnector.setConnectorType("boolean");
		ifConnectors.add(conditionConnector);
		
		BlockConnector thenConnector = new BlockConnector();
		thenConnector.setLabel("then");
		thenConnector.setConnectorKind("socket");
		thenConnector.setConnectorType("cmd");
		ifConnectors.add(thenConnector);
		
		ifBlock.setBlockConnectors(ifConnectors);
		blockGenuses.add(ifBlock);
		/*********if block generation complete**************/
		
		/*********ifelse block generation start**************/
		BlockGenus ifelseBlock = new BlockGenus();
		ifelseBlock.setKind("command");
		ifelseBlock.setInitLabel("ifelse");
		ifelseBlock.setName("ifelse");
		ifelseBlock.setColor("255 80 0");
		
		
		BlockConnector elseConnector = new BlockConnector();
		elseConnector.setLabel("else");
		elseConnector.setConnectorKind("socket");
		elseConnector.setConnectorType("cmd");
		ifConnectors.add(elseConnector);
		
		List<BlockConnector> ifElseConnectors = new ArrayList<BlockConnector>();
		ifElseConnectors.add(conditionConnector);
		ifElseConnectors.add(thenConnector);
		ifElseConnectors.add(elseConnector);
		
		ifelseBlock.setBlockConnectors(ifElseConnectors);
		blockGenuses.add(ifelseBlock);
		/*********ifelse block generation complete**************/
		
		blockLangDef.setBlockGenuses(blockGenuses);
		
		return blockLangDef;
	}
}
