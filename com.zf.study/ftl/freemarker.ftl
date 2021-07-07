<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
</head>
<body>
-----------------------------------------
//字符串
${world}
<br>
-----------------------------------------
//对象数据
${student}/${student.id}/${student.name}
<br>
-----------------------------------------
<!--        //list 集合数据
        List<String> persons=new ArrayList<String>();
        persons.add("小米1");
        persons.add("小米2");
        persons.add("小米3");
        root.put("persons", persons); -->
<#list persons as person>
    ${person}
</#list>
<br>
获取当前迭代的索引
<#list persons as person>
    ${person_index}
</#list>

<br>
-----------------------------------------
<!--
        //map 集合数据
        Map mx=new HashMap();
        mx.put("小米1", "小米1");
        mx.put("小米2", "小米2");
        mx.put("xm3", "小米3");
        root.put("mx", mx);
             -->
<#list mx ? keys as key>
    ${mx[key]}
</#list>

<br>
${mx.xm3} /${mx.小米2} /${mx.小米1}
<br>
-----------------------------------------
<!--        //list<map>数据
        List<Map> listMap=new ArrayList<Map>();
        Map mx1=new HashMap();
        Map mx2=new HashMap();
        mx1.put("小米1", "小米1");
        mx1.put("小米2", "小米2");
        mx2.put("xm3", "小米3");
        listMap.add(mx1);
        listMap.add(mx2); -->
<br>
-----------------------------------------
<#list listMap as map>
    <#list map ? keys as key>
        ${map[key]}
    </#list>
</#list>
<br>
在模板中赋值情况1
<#assign x=0/>
${x}
<br>
-----------------------------------------
在模板中赋值情况1
<#assign y='${world}'/>
${y}
<br>
-----------------------------------------
在模板中赋值情况3
<#assign y>世界太好了</#assign>
${y}
<br>
-----------------------------------------
在模板中赋值情况4
<#assign y>
    <#list persons as person>
        ${person}
    </#list>
</#assign>
${y}
<br>
-----------------------------------------
if语句
<#list persons as person>
    <#if person=='小米1'>
        ${person}
    </#if>
</#list>
-----------------------------------------
if语句 按索引
<#list persons as person>
    <#if person_index!=0>
        ${person}
    </#if>
</#list>
-----------------------------------------
|| && else语句 按索引
<#list persons as person>
    <#if person_index==0 ||person_index==1>
        ${person}
    <#else>
        ${person}
    </#if>
</#list>
-----------------------
时间格式
${curTime?date}
${curTime?time}
${curTime?datetime}

-----------------------
null格式
${testNull!"我是null"}
${testNull!""}
${testNull!}
-----------------------


宏定义 页面写逻辑判断
<#macro table pageNo>
    ${pageNo}
</#macro>
<@table pageNo=8 />


-----------------------
<#macro table u>
    ${u}
    <#nested/>
</#macro>
<@table u=8>this is 8</@table>
------------------------
include

</body>
</html>