<!DOCTYPE html>
<html>
<head>
    <meta charset="utf‐8">
    <title>Hello World!</title>
</head>
<body>
Hello ${name}!


<ul>
    <#list stus as stu>
        <li>${stu.name} ${stu.age} ${stu.money} </li>
        <li>${stu.birthday?date}</li>
        <li>${stu.birthday?time}</li>
        <li>${stu.birthday?datetime}</li>
        <li>${stu.birthday?string("yyyy-MM-dd 24:mm:ss")}</li>
    </#list>
</ul><br/>

list大小:${stus?size}

<br/>
姓名：${stuMap['stu1'].name},&nbsp;&nbsp;年龄：${stuMap['stu1'].age}<br>
姓名：${stuMap.stu2.name},&nbsp;&nbsp;年龄：${stuMap.stu2.age}<br>

<br/>
map大小:${stuMap?size}<br/>
<ul>
    <#list stuMap?keys as k>
        <li <#if stuMap[k].name == '小明'>style="color:red;"</#if>>${stuMap[k].name},${stuMap[k].age}</li>
    </#list>
</ul><br/>

<#--空值处理-->
${(stuNull.name)!'kong'}<br/>

<#--空值判断-->
<#if stuNull??>
<#list stuNull as stu>
    <li>${stu.name} ${stu.age} ${stu.money} </li>
</#list>
</#if><br/>

<#-- 将数字型转字符串 -->
${money?c}
<#assign myJson="{'name':'java','score':'96'}" />
<#assign obj=myJson?eval />
${obj.name},${obj.score}

</body>
</html>