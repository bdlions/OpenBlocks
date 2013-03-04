 <#-- 
 	import function file 
 	-->
 <#import "function.ftl" as functions> 
 <#-- 
 	Java Style class declaration 
 	--> 
class JavaCodeSample{
 	<#-- 
 		variable declaration code generation 
 		
 		--> 
	<#list codeGen.getVariableDeclBlocks() as variable>
	 	${functions.getalternatename(variable.getType())} ${functions.getalternatename(variable.getName())} = <#list variable.getValue() as expressionData><#if expressionData.getId()== 0>${functions.getalternatename(expressionData.getData())}<#elseif variable.getType()?matches('string') >"${functions.getalternatename(codeGen.getBlock(expressionData.getId()).getLabel())}"<#else>${functions.getalternatename(codeGen.getBlock(expressionData.getId()).getLabel())}</#if></#list>;
	 </#list>
	 
	<#-- 
 		main function declaration code generation 
 		--> 
	public static void main(String[] args){
	<#list codeGen.getDifferentCommand() as command>
		<#assign commandBlock = command />
		<@commandMacro commandBlock/>
	</#list>
	}
	<#-- 
 		all function declaration code generation 
 		
 		--> 
	<#list codeGen.getAllFunction() as block>
		<#assign genusName = block.getGenusName() />
		<@functions.functionController genusName /> 
	</#list>
}
	<#-- 
 		complete sequential code generation
 		--> 

//all variabes blocks
<#list codeGen.getVariableDeclBlocks() as variable>
	private ${functions.getalternatename(variable.getType())} ${functions.getalternatename(variable.getName())} = <#list variable.getValue() as expressionData><#if expressionData.getId()== 0>${functions.getalternatename(expressionData.getData())}<#elseif variable.getType()?matches('string') >"${functions.getalternatename(codeGen.getBlock(expressionData.getId()).getLabel())}"<#else>${functions.getalternatename(codeGen.getBlock(expressionData.getId()).getLabel())}</#if></#list>;
</#list>


//all if blocks
<#list codeGen.getAllBlocks() as block>
	<#if block.getGenusName() = "if">
		<@singleIfElse block/>
	</#if> 
</#list>

//all if else blocks
<#list codeGen.getAllBlocks() as block>
	<#if block.getGenusName() = "ifelse">
		<@singleIfElse block/>
	</#if> 
</#list>

//all action blocks
<#list codeGen.getAllBlocks() as block>
	<#if block.getGenusName() != "if" && block.getGenusName() != "ifelse">
		<#assign blockGenus = codeGen.getGenusWithName(block.getGenusName()) />
		<#if blockGenus.isCommandBlock() >
			<@singleIfElse block/>
		</#if>
	</#if> 
</#list>

//all option blocks
<#list codeGen.getAllBlocks() as block>
	<#if block.getGenusName() != "if" && block.getGenusName() != "ifelse">
		<#assign blockGenus = codeGen.getGenusWithName(block.getGenusName()) />
		<#if blockGenus.isFunctionBlock() >
			<#list codeGen.getFullFunction(block) as functionBlock><#if functionBlock.getId() = 0> ${functions.getalternatename(functionBlock.getData())}<#else> ${functions.getalternatename(codeGen.getBlock(functionBlock.getId()).getLabel())}</#if></#list>
		</#if>
	</#if> 
</#list>



<#-- 
	some macro defined
 --> 

<#macro commandMacro command>
	<#if command.getLabel() = "if">
 		if( <#list codeGen.getIfCondition(command) as conditionBlocks><#if conditionBlocks.getId() = 0> ${functions.getalternatename(conditionBlocks.getData())}<#else> ${functions.getalternatename(codeGen.getBlock(conditionBlocks.getId()).getLabel())}</#if></#list>){
		<#list codeGen.getThenStatements(command) as commandStatement>
 			<@commandMacro command=commandStatement/>
		</#list> 
		}
	<#elseif command.getLabel() = "ifelse">
		if( <#list codeGen.getIfCondition(command) as conditionBlocks><#if conditionBlocks.getId() = 0> ${functions.getalternatename(conditionBlocks.getData())}<#else> ${functions.getalternatename(codeGen.getBlock(conditionBlocks.getId()).getLabel())}</#if></#list>){
			<#list codeGen.getThenStatements(command) as commandStatement>
	 			<@commandMacro command=commandStatement/>
			</#list> 
		}else{
			<#list codeGen.getElseStatements(command) as commandStatement>
	 			<@commandMacro command=commandStatement/>
			</#list> 
		}
	<#else>
 		<#list codeGen.getCommandExpression(command) as commandStatement>${functions.getalternatename(commandStatement.getData())}</#list>;
 	</#if>
	<#if command.getAfterBlockId() != 0>
		<#assign nextBlock = codeGen.getBlock(command.getAfterBlockId())/>
		<@commandMacro command=nextBlock/>
	</#if>
</#macro>

<#macro singleIfElse command>
	<#if command.getLabel() = "if">
 		if( <#list codeGen.getIfCondition(command) as conditionBlocks><#if conditionBlocks.getId() = 0> ${functions.getalternatename(conditionBlocks.getData())}<#else> ${functions.getalternatename(codeGen.getBlock(conditionBlocks.getId()).getLabel())}</#if></#list>){
		<#list codeGen.getThenStatements(command) as commandStatement>
 			<@commandMacro command=commandStatement/>
		</#list> 
		}
		<#elseif command.getLabel() = "ifelse">
		if( <#list codeGen.getIfCondition(command) as conditionBlocks><#if conditionBlocks.getId() = 0> ${functions.getalternatename(conditionBlocks.getData())}<#else> ${functions.getalternatename(codeGen.getBlock(conditionBlocks.getId()).getLabel())}</#if></#list>){
			<#list codeGen.getThenStatements(command) as commandStatement>
	 			<@commandMacro command=commandStatement/>
			</#list> 
		}else{
			<#list codeGen.getElseStatements(command) as commandStatement>
	 			<@commandMacro command=commandStatement/>
			</#list> 
		}
 		<#else>
 			<#list codeGen.getCommandExpression(command) as commandStatement>${functions.getalternatename(commandStatement.getData())}</#list>;
 		<#if command.getAfterBlockId() != 0>
			<#assign nextBlock = codeGen.getBlock(command.getAfterBlockId())/>
			<@commandMacro command=nextBlock/>
		</#if>
 	</#if>
</#macro>
