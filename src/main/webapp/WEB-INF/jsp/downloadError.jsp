<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title><spring:message code="easy.tms"/></title>
	<meta name="description" content="">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
	<link rel="stylesheet" type="text/css" href="css/layout.css">
	<link rel="stylesheet" type="text/css" href="css/style.css">
	<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:200,400,700">
	<link rel="stylesheet" href="//cdn.jsdelivr.net/xeicon/2/xeicon.min.css">
	<link rel="shortcut icon" href="images/favicon.ico">
	<script type="text/javascript" src="js/jquery-1.12.4.min.js"></script>
 	<script	type="text/javascript" src="js/custom.js"></script>
</head>

<body>
<div class="wrap">
	<div id="header" class="clearFix reset">
		<div class="setWidth">
			<h1><a href="#">InfraWare TMS</a></h1>
			<ul id="topMenu">
				<li><a href="login.do">LOGOUT</a></li>
				<li class="link"><a href="#">About EasyTMS</a></li>
			</ul>
		</div>	
		<div class="gnbWrap">
			<ul id="gnb" class="setWidth">
				<li><a href="search.do"><i class="xi-search"></i> Search Terminology</a></li>
				<li class="current"><a href="download.do"><i class="xi-file-download-o"></i> Downloads</a></li>
				<li><a href="upload.do"><i class="xi-file-upload-o"></i> Upload</a></li>
			</ul>
		</div>
	</div>
	<!-- Header END -->
	
 	<div id="subHeader" class="reset">
	 	<h2 class="locationTitle setWidth"><spring:message code="download.fail"/></h2>
 	</div>

	<div id="footer">
		<div class="footerContent setWidth">
			<p><spring:message code="copyright"/> <b><spring:message code="easy.tms"/></b></p>
		</div>
	</div>
</div>
</body>
</html>