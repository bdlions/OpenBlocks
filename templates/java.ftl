 <#-- 
 	import function file 
 	-->
 <#import "java/function.ftl" as functions> 

<#-- 
	variable declaration code generation
	--> 
<#list codeGen.getVariableDeclBlocks() as variable>
 	private ${variable.getType()} ${variable.getName()} = <#list variable.getValue() as expressionData><#if expressionData.getId()== 0>${expressionData.getData()}<#elseif variable.getType()?matches('string') >"${codeGen.getBlock(expressionData.getId()).getLabel()}"<#else>${codeGen.getBlock(expressionData.getId()).getLabel()}</#if></#list>;
 </#list>
<#-- 
	main function declaration code generation 
	--> 
<#list codeGen.getDifferentCommand() as command>
	<#assign commandBlock = command />
	<@commandMacro commandBlock/>
</#list>

<#-- 
	some macro defined
 --> 

<#macro commandMacro command>
	<#if command.getLabel() = "if">
 		if( <#list codeGen.getIfCondition(command) as conditionBlocks><#if conditionBlocks.getId() = 0> ${conditionBlocks.getData()}<#else> ${codeGen.getBlock(conditionBlocks.getId()).getLabel()}</#if></#list>){
		<#list codeGen.getThenStatements(command) as commandStatement>
 			<@commandMacro command=commandStatement/>
		</#list> 
		}
		<#elseif command.getLabel() = "ifelse">
		if( <#list codeGen.getIfCondition(command) as conditionBlocks><#if conditionBlocks.getId() = 0> ${conditionBlocks.getData()}<#else> ${codeGen.getBlock(conditionBlocks.getId()).getLabel()}</#if></#list>){
			<#list codeGen.getThenStatements(command) as commandStatement>
	 			<@commandMacro command=commandStatement/>
			</#list> 
		}else{
			<#list codeGen.getElseStatements(command) as commandStatement>
	 			<@commandMacro command=commandStatement/>
			</#list> 
		}
 		<#else>
 		<#list codeGen.getCommandExpression(command) as commandStatement>${commandStatement.getData()}</#list>;
 	</#if>
	<#if command.getAfterBlockId() != 0>
		<#assign nextBlock = codeGen.getBlock(command.getAfterBlockId())/>
		<@commandMacro command=nextBlock/>
	</#if>
</#macro>

<#macro singleIfElse command>
	<#if command.getLabel() = "if">
 		if( <#list codeGen.getIfCondition(command) as conditionBlocks><#if conditionBlocks.getId() = 0> ${conditionBlocks.getData()}<#else> ${codeGen.getBlock(conditionBlocks.getId()).getLabel()}</#if></#list>){
		<#list codeGen.getThenStatements(command) as commandStatement>
 			<@commandMacro command=commandStatement/>
		</#list> 
		}
		<#elseif command.getLabel() = "ifelse">
		if( <#list codeGen.getIfCondition(command) as conditionBlocks><#if conditionBlocks.getId() = 0> ${conditionBlocks.getData()}<#else> ${codeGen.getBlock(conditionBlocks.getId()).getLabel()}</#if></#list>){
			<#list codeGen.getThenStatements(command) as commandStatement>
	 			<@commandMacro command=commandStatement/>
			</#list> 
		}else{
			<#list codeGen.getElseStatements(command) as commandStatement>
	 			<@commandMacro command=commandStatement/>
			</#list> 
		}
 		<#else>
 		<#list codeGen.getCommandExpression(command) as commandStatement> ${commandStatement.getData()} </#list>;
 		<#if command.getAfterBlockId() != 0>
			<#assign nextBlock = codeGen.getBlock(command.getAfterBlockId())/>
			<@commandMacro command=nextBlock/>
		</#if>
 	</#if>
</#macro>
