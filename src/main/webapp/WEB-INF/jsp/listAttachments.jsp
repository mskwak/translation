<!-- 아래 JSTL을 사용하기 위해 pom.xml에 jstl, tablib 설정을 해주어야 했다. -->  
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="/js/jquery.fileDownload.js"></script>
<script type="text/javascript" src="/js/upload/vendor/jquery.ui.widget.js"></script>
<script type="text/javascript" src="/js/upload/jquery.iframe-transport.js"></script>
<script type="text/javascript" src="/js/upload/jquery.fileupload.js"></script>
<link rel="stylesheet" href="/css/jquery-ui.css">
<link rel="stylesheet" href="/css/base.css">

</head>
<body>
	<table class="table">
		<thead>
			<tr>
				<td colspan="7">
				<input type="button" id="deletion" value="<spring:message code="deletion"/>"/>
				<input type="button" id="upload-button" value="<spring:message code="upload"/>"/>
				</td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td><input id="check-all" type="checkbox"></td>
				<td><spring:message code="number"/></td>
				<td><spring:message code="language"/></td>
				<td><spring:message code="file.name"/></td>	
				<!-- <td><spring:message code="extension"/></td>  -->
				<td><spring:message code="size"/></td>
				<td><spring:message code="created.time"/></td>
				<td><spring:message code="download"/></td>
				<!-- <td><spring:message code="download"/></td>  -->
				<!-- <td><spring:message code="deletion"/></td>  -->
			</tr>
			<c:forEach var="attachment" varStatus="attachmentStatus" items="${listAttachments}">
			<tr>
				<td><input id="<c:out value="${attachment['id']}"/>" name="tbx-list" type="checkbox"></td>
				<!-- <td><c:out value="${attachment['id']}"/></td>  -->
				<!-- <td><c:out value="${fn:length(listAttachments)}"/></td>  -->
				<td><c:out value="${fn:length(listAttachments) - attachmentStatus.index}"/></td>
				
				
				<!-- <td><img src="<c:out value="/image/flag/${attachment['country']}.png"/>" /></td>  -->
				
				<td>
				<c:set var="langsetList" value="${fn:split(attachment['langset'], '|')}" />
				<c:forEach var="langset" items="${langsetList}">
					<c:out value="${langset}"></c:out>
					<!-- <img src="<c:out value="/image/flag/${country}.png"/>" title="${country}" /> -->
				</c:forEach>
				</td>
				
				
				<td><c:out value="${attachment['name']}"/></td>
				<!-- <td><c:out value="${attachment['extension']}"/></td>  -->
				<!-- <td><c:out value="${attachment['size']}"/></td>  -->
				<td>
				<c:choose>
					<c:when test="${attachment['size'] lt 1024}">
						<!-- <c:out value="${attachment['size']} B" /> -->
						<fmt:formatNumber value="${attachment['size']}" type="number" pattern="#.#"/>B
					</c:when>
					<c:when test="${attachment['size'] le (1024 * 1024)}">
						<!-- <c:out value="${attachment['size'] / 1024} KB" /> -->
						<fmt:formatNumber value="${attachment['size'] / 1024}" type="number" pattern="#.#"/>KB
					</c:when>
					<c:when test="${attachment['size'] le (1024 * 1024 * 1024)}">
						<!-- <c:out value="${attachment['size'] / (1024 * 1024)} MB" /> -->
						<fmt:formatNumber value="${attachment['size'] / (1024 * 1024)}" type="number" pattern="#.#"/>MB
					</c:when>
					<c:otherwise>
						<c:out value="${attachment['size']}" />
					</c:otherwise>
				</c:choose>
				</td>
				<!-- <td><c:out value="${attachment['created_time']}"/></td>  -->
				<td><fmt:formatDate value="${attachment['created_time']}" pattern="yyyy-MM-dd HH:mm"/></td>
				<td>
					<!-- <div id="${attachment['hash_name']}">download</div>  -->
					<a href="javascript:downloadFile(${attachment['id']}, 'xlsx')"><img src="/image/excel.png" /></a>
					<a href="javascript:downloadFile(${attachment['id']}, 'tbx')"><img src="/image/tbx.png" /></a>
				</td>
				<!-- <td>download excel</td>  -->
				<!-- <td>삭제</td> -->
			</tr>
			</c:forEach>
		</tbody>
	</table>
	
<div id="upload-dialog" title="<spring:message code="file.upload"/>">
	<div>
		<span>
			<label for="fileupload"><spring:message code="file.upload"/></label>
  			<input id="fileupload" type="file" name="upload" multiple>
  		</span>
  		<span id="progress-count"></span>
  	</div>
  	<div id="blank" style="height: 30px;"></div>
  	<div id="progress">
		<div class="bar" style="width:0%;"></div>
	</div>
	
	<table id="upload-list-table"></table>
</div>

</body>
<script type="text/javascript">

var downloadFile = function(id, extension) {
	window.location = "/downloadFile.do?id=" + id + "&" + "extension=" + extension;
};

/* 
$("td img").on("click", function() {
	var idWithExtension = $(this).attr("id");
	//console.log(id);
	//var pattern = /^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$/;
	// http://icoon22.tistory.com/220 : pattern.exec 에 대한 설명
	//if(pattern.exec(id) != null) {
		$.ajax({
			type: "POST",
			url: "downloadFile.do",
			data: {id: idWithExtension},
//			contentType: "application/octet-stream",
			success: function(result, textStatus, jqXHR) {
				//console.log(result);
			}
		});
	//}
	
//	console.log("xxx:" + r);
});
 */

// http://fruitdev.tistory.com/143
/* $("#check-all").change(function() {
	$("input:checkbox").prop("checked", $(this).prop("checked"));
}); */

$("#check-all").on("change", function() {
	$("input:checkbox").prop("checked", $(this).prop("checked"));
});


$("#deletion").on("click", function() {
	var listIds = [];
	
	// name 속성을 지정하지 않을 경우, $("input:checkbox:checked") 의 표현도 가능하다.
	$("input:checkbox[name='tbx-list']:checked").each(function() {
		//var val = $(this).attr("id");
		//list.push(val);
		//console.log(val);
		//console.log(list.toString());
		
		listIds.push($(this).attr("id"));
	});
	
	// 자바스크립트 배열 function: http://www.w3schools.com/jsref/jsref_obj_array.asp
	if(listIds.length != 0) {
		// The local variable result is never read. function 안에 있는 변수들을 로컬 변수로 사용하려면 어떻게 해야하지?
		// var result, textStatus, jqXHR;
		// 배열을 넘기는 것에 대해 서버단 에서는 ArrayList 혹은 String[] 로 받으면 됨.
		// 배열을 넘길 때에는 traditional: true, 이렇게 하는 방법밖에는 없나? 배열을 직렬화해서 넘기는 방법은???
		$.ajax({
			type: "POST",
			url: "deleteLists.do",
			traditional: true,
			data: {list: listIds},
			success: function(data, textStatus, jqXHR) {
				// 컨트롤러로 부터 리턴되는 페이지를 reload 한다. 컨트롤러에서 void를 리턴할 경우 아무런 액션을 취하지 않는다.
				//window.location.reload();
				location.reload();
				
				console.log(data);
				console.log(textStatus);
				console.log(jqXHR);
			},
			complete: function(jqXHR, textStatus) {
				// window.location.reload()를 이용해 페이지를 갱신하더라도 check-all 체크박스에 체크가 해제되지 않는다. 해제를 위해 아래 코드 삽입
				$("input:checkbox[name='tbx-list']").prop("checked", false);
			}
		});
	}
});

$("#upload-dialog").dialog({
	autoOpen: false,
	width: 600,
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
			location.reload();
		}
	},]
});

/* $(window).on('scroll', function() {
    $("#upload-dialog").dialog('option', 'position', 'center');
}); */


$("#upload-button").on("click", function() {
	var dialog = $("#upload-dialog").dialog("open");
/* 	
	console.log(dialog.offset().top);
	console.log(dialog.offset().left);
	
	console.log($( window ).width());
	console.log($( window ).height());
	 
	// Returns width of HTML document
	console.log($(document).width());
	console.log($(document).height());
*/
});

$('#fileupload').fileupload({
	url: "uploadByAjaxP.do",
	dataType: 'json',
	sequentialUploads: true,
	add: function(e, data) {
		$.each(data.files, function(index, file) {
			console.log("add: " + file.name);
			console.log("add: " + file.lastModified);
			console.log("add: " + file.lastModifiedDate);
			console.log("add: " + file.size);
		});
		data.submit();
	},
	done: function(e, data) {
		//$("tr:has(td)").remove();
		$.each(data.files, function(index, file) {
			console.log("done: " + file.name);
			console.log("done: " + file.lastModified);
			console.log("done: " + file.lastModifiedDate);
			console.log("done: " + file.size);
			
			//$("#progress-count").empty().text(files.index);
			
			$("#upload-list-table").append(
					$("<tr/>")
					.append($("<td/>").text(file.name))
					.append($("<td/>").text(file.size))
			);
		});
	},
	progress: function(e, data) {
		//$.each(data.files, function(index, file) {
			//console.log("progress: " + file.name);
			var progress = parseInt(data.loaded / data.total * 100, 10);
			$("#progress .bar").css("width", progress + "%");
			data.context.text(progress + '%');
			console.log("progress: " + data);
			//$("#filename").html(file.name);
			

		//});
		

	},
	process: function(e, data) {
		console.log('Processing ' + data.files[data.index].name + '...');
	},
	fileuploadprogress: function (e, data) {
	    // Log the current bitrate for this upload:
	    console.log(data.bitrate);
	},
});
</script>
</html>


		
