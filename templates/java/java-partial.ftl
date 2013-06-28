 <#-- 
 	import function file 
 	-->
 <#import "function.ftl" as functions> 

<#-- 
	variable declaration code generation
	--> 
	<#list codeGen.getVariableDeclBlocks() as variable>
	 	${functions.getalternatename(variable.getType())} ${functions.getalternatename(variable.getName())} = <#list variable.getValue() as expressionData><#if expressionData.getId()== 0>${functions.getalternatename(expressionData.getData())}<#elseif variable.getType()?matches('string') >"${functions.getalternatename(codeGen.getBlock(expressionData.getId()).getLabel())}"<#else>${functions.getalternatename(codeGen.getBlock(expressionData.getId()).getLabel())}</#if></#list>;
	 </#list>
 
<#list codeGen.getDifferentCommand() as command>
	<#assign commandBlock = command />
	<@commandMacro commandBlock/>
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
 		<#list codeGen.getCommandExpression(command) as commandStatement><#if commandStatement.getId() = 0>${functions.getalternatename(commandStatement.getData())}<#else><#if codeGen.getDataBlockType(commandStatement.getId()) = "string">"${functions.getalternatename(codeGen.getBlock(commandStatement.getId()).getLabel())}"<#else>${functions.getalternatename(codeGen.getBlock(commandStatement.getId()).getLabel())}</#if></#if></#list>;
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
 			<#list codeGen.getCommandExpression(command) as commandStatement><#if commandStatement.getId() = 0>${functions.getalternatename(commandStatement.getData())}<#else><#if codeGen.getDataBlockType(commandStatement.getId()) = "string">"${functions.getalternatename(codeGen.getBlock(commandStatement.getId()).getLabel())}"<#else>${functions.getalternatename(codeGen.getBlock(commandStatement.getId()).getLabel())}</#if></#if></#list>;
 		<#if command.getAfterBlockId() != 0>
			<#assign nextBlock = codeGen.getBlock(command.getAfterBlockId())/>
			<@commandMacro command=nextBlock/>
		</#if>
 	</#if>
</#macro>

