<!-- 아래 인코딩 설정을 해주어야만 브라우저에서 한글이 깨지지 않고 정상 출력된다. -->
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!-- 아래 JSTL을 사용하기 위해 pom.xml에 jstl, tablib 설정을 해주어야 했다. -->
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<html>
<head>
<link rel="stylesheet" href="/css/base.css">  
</head>

<body>
 	<div id="header"><tiles:insertAttribute name="header" /></div>
 	<div id="blank" style="height:70px"><tiles:insertAttribute name="white" /></div>
 	<div id="middle">
 		<span id="left" style="display:inline-block; width:200px; background-color:#CDCDCD"><tiles:insertAttribute name="white" /> </span>
 		<span id="center"><tiles:insertAttribute name="body" /></span>
 		<span id="right" style="display:inline-block; width:200px;"><tiles:insertAttribute name="white" /> </span>
 	</div>
 	
<%--  	
 	<table class="table">
 		<tr>
 			<td><tiles:insertAttribute name="white" /></td>
 			<td><tiles:insertAttribute name="header" /></td>
 			<td><tiles:insertAttribute name="white" /></td>
 		</tr>
 		<tr>
 			<td style="width:200px"><tiles:insertAttribute name="white" /></td>
 			<td><tiles:insertAttribute name="body" /></td>
 			<td style="width:200px"><tiles:insertAttribute name="white" /></td>
 		</tr>
 	
		<tr>
 			<td><tiles:insertAttribute name="footer" /></td>
 		</tr>

 	</table>
--%>
</body>
</html>