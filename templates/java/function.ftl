<#macro functionController functionName>
	<#if functionName = "userAge">
		<@userAge/>
	</#if>
	<#if functionName = "age">
		<@age/>
	</#if>
	<#if functionName = "born">
		<@advancedAge/>
	</#if>
	<#if functionName = "mail">
		<@mail/>
	</#if>
	<#if functionName = "line">
		<@line/>
	</#if>
</#macro>

<#macro age>
	int age(in){
		return in;
	}
</#macro>

<#macro userAge>
	int userAge(int in, float x){
		return in * 2/x;
	}
</#macro>

<#macro advancedAge>
	int advancedAge(tcountry, tcolor, dateborn){
		If (dateborn>10) {
		return 10;       
		} 
		else {
		return dateborn; 
		}
	}
</#macro>

<#macro mail>
	mail (String txt) {
	  mail(a@d.com,txt);
	}
</#macro>

<#macro line>
	line(style, width, color) {
		draw("line",style,width,color);
	}
</#macro>
	
<#assign mapping = {"and":"&&", "or":"||", "integer": "int", "string": "String", "tomorrow":"false", "now":"true", "yes":"1.0", "no":"0.0"}>

<#function getalternatename word>
	<#if mapping[word]??>
		<#return mapping[word]>
	</#if>
  	<#return word>
</#function>
