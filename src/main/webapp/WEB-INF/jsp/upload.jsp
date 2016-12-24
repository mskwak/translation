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
	<link rel="stylesheet" href="/css/jquery-ui.css">
	<link rel="shortcut icon" href="images/favicon.ico">
	<script type="text/javascript" src="/js/jquery-1.12.4.min.js"></script>
	<script type="text/javascript" src="/js/jquery-ui.min.js"></script>
 	<script	type="text/javascript" src="/js/custom.js"></script>
 	<script type="text/javascript" src="/js/jquery.fileDownload.js"></script>
 	<script type="text/javascript" src="/js/upload/vendor/jquery.ui.widget.js"></script>
	<script type="text/javascript" src="/js/upload/jquery.iframe-transport.js"></script>
	<script type="text/javascript" src="/js/upload/jquery.fileupload.js"></script>
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
				<li><a href="download.do"><i class="xi-file-download-o"></i> Downloads</a></li>
				<li class="current"><a href="upload.do"><i class="xi-file-upload-o"></i> Upload</a></li>
			</ul>
		</div>
	</div>
	
 	<div id="subHeader" class="reset">
	 	<h2 class="locationTitle setWidth">Upload</h2>
 	</div>
	
	<div class="contentWrap clearFix setWidth reset">
		<div class="contentBody" style="padding: 0 0 0px;">
			<table class="downloadTable"> 
				<tbody>
				<tr>
					<th style="width: 70%;">Choose File</th>
					<td></td>
				</tr> 
				<tr>
					<td>
						<input id="uploadText" class="textInput large" style="width: 400px;" placeholder="No file chosen" disabled="disabled" />
						<div class="fileUpload button black large btn-primary" style="width:180px;">
						    <span>CHOOSE</span>
						    <input id="uploadFile" name="uploadFile" type="file" class="upload" multiple />
						</div>
					</td>
					<td>
						<a id="uploadAction" class="button blue large" style="width: 100%;">UPLOAD</a>
					</td>
				</tr>
				</tbody>	 
			</table>	
		</div>
	</div>

	<!--검색결과 시작-->
	<div id="searchResult" class="setWidth">
	<div class="divider"></div>
		<h3>File List To Upload</h3>
		<table class="uploadListTable"> 
			<thead>
				<tr>
					<th class="definition">Filename</th>
					<th class="langFrom">Size</th>
				</tr>
			</thead>
			<tbody id="uploadList">	 
			</tbody>
		</table> 
	</div>
	<!--검색결과 끝 -->

	<div id="footer">
		<div class="footerContent setWidth">
			<p><spring:message code="copyright"/> <b><spring:message code="easy.tms"/></b></p>
		</div>
	</div>
</div>

<div id="upload-dialog" title="Upload complete" align="center">
	<p class="large"><spring:message code="upload.complete"/></p>
</div>

<script>

document.getElementById("uploadFile").onchange = function () {
    document.getElementById("uploadText").value = this.value;
};

//$("#searchResult").hide();

$("#uploadFile").fileupload({
	url: "uploadByAjax.do",
	dataType: 'json',
	sequentialUploads: true,
	add: function(e, data) {
		$.each(data.files, function(index, file) {
			$("#uploadList").append(
					$("<tr/>")
					.append($("<td/>").text(file.name))
					.append($("<td/>").text((file.size / (1024 * 1024)).toFixed(2) + " MB"))
			);
			
			console.log("add: " + file.name);
			console.log("add: " + file.lastModified);
			console.log("add: " + file.lastModifiedDate);			
			console.log("add: " + file.size);
		});
		
		$("#searchResult").show();
		
		$("#uploadAction").on("click", function() {
			data.submit();	
		});
	},
	done: function(e, data) {
		$.each(data.files, function(index, file) {

		});
		
		$("#upload-dialog").dialog("open");
	},
	progress: function(e, data) {
			var progress = parseInt(data.loaded / data.total * 100, 10);
			$("#progress .bar").css("width", progress + "%");
	},
});

//TODO 중복 코드
$("#upload-dialog").dialog({
	autoOpen: false,
	width: 300,
	//height: 300,
	modal: true,
	height: "auto",
	position: {
		my: "center top+25%",
		at: "center top+25%",
		of: $(document)
	},
	buttons: [{
		text: "OK",
		click: function() {
			$(this).dialog("close");
			//$("#uploadText").val("");
			//location.reload();
			window.location="upload.do";
		}
	},]
});
</script>
</body>
</html>