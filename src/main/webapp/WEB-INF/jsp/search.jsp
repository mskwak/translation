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
				<li class="current"><a href="search.do"><i class="xi-search"></i> Search Terminology</a></li>
				<li><a href="download.do"><i class="xi-file-download-o"></i> Downloads</a></li>
				<li><a href="upload.do"><i class="xi-file-upload-o"></i> Upload</a></li>
			</ul>
		</div>
	</div>
	<!-- Header END -->

 	<div id="subHeader" class="reset"><h2 class="locationTitle setWidth">Search Terminology</h2></div>
	
	<div class="contentWrap clearFix setWidth reset">
		<div class="contentBody">
			<table class="searchTable"> 
				<tbody>
				<tr class="blank">
					<td colspan="2"></td>
				</tr>
				<tr class="search">
					<th>Search For</th>
					<td><input id="searchText" type="text" class="textInput large"/></td>
				</tr> 
				<tr class="search">
					<th>From</th>
					<td>
						<div class="selectWrap">
							<select>
								<option>Any</option>
							</select>
						</div>
						<span>to</span> 
						<div id="charset" class="selectWrap">
							<select>
								<option value="all">All Languages</option>
								<c:forEach var="charset" varStatus="charsetStatus" items="${charsetMap}">
									<option value="<c:out value="${charset.key}"/>"><c:out value="${charset.value}"/></option>
								</c:forEach>
							</select>
						</div>
					</td>
				</tr> 
				<tr class="blank">
					<td colspan="2"></td>
				</tr>
				<tr class="product">
					<th>Product</th>
					<td>
						<div class="selectWrap">
							<select>
								<!-- <option>All products</option> -->
								<option>N/A</option>
							</select>
						</div>
						<a id="searchAnchor" class="button blue large">SEARCH</a>
						<!-- <input type="button" class="button blue large"/>SEARCH -->
						
					</td>
				</tr>
				</tbody>	 
			</table>
			
			<!--검색결과 시작-->
			<div id="searchResult">
			<div class="divider"></div>
				<h3>InfraWare Terminology Collection</h3>
				<table class="searchListTable"> 
					<thead>
						<tr>
							<th class="langFrom">English</th>
							<th class="langTo">Translation</th>
							<th class="definition">Definition</th> 
						</tr>
					</thead>
					<tbody id="searchList">	 
					</tbody>
				</table> 
				<div class="boardListBottom">			
					<ul class="pagination">
						<li><a href=""><i class="xi-angle-left"></i></a></li>	
						<li class="current"><a href="">1</a></li>
						<li><a href="">2</a></li>
						<li><a href="">3</a></li>
						<li><a href=""><i class="xi-angle-right"></i></a></li>	
					</ul>
				</div>
			</div>
			<!--검색결과 끝 -->
		</div>
	</div>

	<div id="footer">
		<div class="footerContent setWidth">
			<p><spring:message code="copyright"/> <b><spring:message code="easy.tms"/></b></p>
		</div>
	</div>
</div>

<div id="searchDialog" title="" align="center">
	<p class="large"><spring:message code="search.fail"/></p>
</div>
</body>
<script>
$(".boardListBottom").hide();
$("#searchResult").hide();

$("#searchText").on("keyup", function(e) {
	if (e.which == 13) {
		e.preventDefault();
		searchTerminology();
	}
});

$("#searchAnchor").on("click", function(e) {
	e.preventDefault();
	searchTerminology();
});

var searchTerminology = function() {
	var word = $("#searchText").val();
	var charset = $("#charset select").val();
	
	if(! word || ! charset) {
		$("#searchDialog").html("<spring:message code="search.input"/>").dialog("open");
		return false;
	}
	
 	$.ajax({
 		url: "search.do",
 		data: {
 			word: word,
 			charset: charset,
 			handler: "tbx",
 		},
 		dataType: "json",
 		type: "POST",
 		success: function(data, textStatus, jqXHR) {
 			var response = data.response;
 			
 			if(response.code == "500") {
 				$("#searchDialog").dialog("open");
 				return false;
 			}
 			
 			$("#searchList").empty();
 			
			$.each(data.data, function(termEntryId, termEntryIdList) {
				$.each(termEntryIdList, function(i, obj) {
					var searchTerminology = "";
					
					if(i == 0) {
						//$("#search-result-table").append($("<td rowspan=" + termEntryIdList.length + ">" + obj.termEntryId + "</td>"));
						searchTerminology = "<td rowspan=" + termEntryIdList.length + ">" + $("#searchText").val() + "</td>";
					}
					
					$("#searchList").append($("<tr/>").
							append(searchTerminology).
							// append($("<td/>").html(getCountryFlag(obj.country))).
							//append($("<td/>").text(obj.langSet)).
							append($("<td/>").attr("title", obj.country).text(obj.term)).
							append($("<td/>").attr({"style": "width:60%"}).text(obj.descrip))
					);
				});
			});
			
			$("#searchResult").show();
 		}
 	});
};

// TODO 중복 코드
$("#searchDialog").dialog({
	autoOpen: false,
	width: 300,
	resizable: false,
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
			$("#uploadText").val("");
			location.reload();
		}
	},]
});
</script>
</html>