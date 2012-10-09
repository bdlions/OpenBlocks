 <#import "java/function.ftl" as functions> 
   //all variable blocks
<#list codeGen.getVariableBlocks() as block>      
    private ${block.getType()} ${block.getName()} = ${block.getValue()};
</#list>   

<#list codeGen.getVariableBlocks() as block>
	public ${block.getType()} get${block.getName()?cap_first}(){
        return this.${block.getName()};
    }
    public void set${block.getType()?cap_first}(${block.getType()} ${block.getName()}){
        this.${block.getName()} = ${block.getName()};
    }
</#list>
   //all if else blocks
<#list codeGen.getIfelseBlocks() as block>
    if(${codeGen.getCondition(block)}){
   	<#list codeGen.getActionsInIfBlock(block) as actionBlock>
    	${actionBlock.getGenusName()}(${codeGen.getParameters(actionBlock)});
    </#list>
    }
    else{
    <#list codeGen.getActionsInElseBlock(block) as actionBlock>
    	${actionBlock.getGenusName()}(${codeGen.getParameters(actionBlock)});
    </#list>
    }
</#list>


    //all if blocks
<#list codeGen.getIfBlocks() as block>
    if(${codeGen.getCondition(block)}){
    <#list codeGen.getActionsInIfBlock(block) as actionBlock>
    	${actionBlock.getGenusName()}(${codeGen.getParameters(actionBlock)});
    </#list>
    }
</#list>

    //all condition block
<#list codeGen.getConditionBlocks() as block>
    ${codeGen.getCondition(block)}
</#list>

    //all action block
<#list codeGen.getActionBlocks() as block>
    ${block.getGenusName()}(${codeGen.getParameters(block)});
</#list>

    //all action definition block
<#list codeGen.getActionBlocks() as block>
	<#assign genusName = block.getGenusName() />
	<@functions.functionController genusName /> 
</#list>
