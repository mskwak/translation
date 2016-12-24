<!-- 아래 JSTL을 사용하기 위해 pom.xml에 jstl, tablib 설정을 해주어야 했다. -->  
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<script type="text/javascript" src="/js/jquery-1.12.4.min.js"></script>
<body>
	<div id="xxx">
		<input id="search-id" type="text" placeholder="<spring:message code="search"/>"/>
	</div>
	<div id="search-result">
		<table id="search-result-table" class="table"></table>
	</div>
</body>
<script type="text/javascript">

$("#search-id").on('keyup', function(e) {
    if (e.which == 13) {
        e.preventDefault();
        
        var word = $("#search-id").val();
        
 	 	$.ajax({
 	 		url: "searchGlossary.do",
 	 		//processData: false,
 	 		//contentType: false,
 	 		data: {word: word},
 	 		dataType: "json",
 	 		type: "POST",
 	 		success: function(data, textStatus, jqXHR) {
				//console.log(textStatus);
				//console.log(jqXHR);
				//$("#search-result").text(data);
				//$("#status").html(textStatus);
				//$("#jqxhr").html(jqXHR);
				//console.log(data.searchResults);
				$("#search-result-table").empty().append("<tr><th>termEntryId</th><th>langSet</th><th>term</th><th>descrip</th></tr>");
				// $("#search-result-table").append("<tr><th>termEntryId</th><th>langSet</th><th>term</th><th>descrip</th></tr>");
				//$("#search-result-table").html();
				
				$.each(data.searchResults, function(termEntryId, termEntryIdList) {
					//console.log(termEntryId);
					//console.log(termEntryIdList);
					//console.log(map.size);
					//var colspann = arrayList.length;
					//console.log(data.searchResults);
					$.each(termEntryIdList, function(i, obj) {
						//console.log(termEntryIdList.length);
						//var rowspan = Object.keys(langSetMap).length;
						//console.log(obj.id);
						//console.log(langSet + ":" + langSetMap);
						//$("#search-result").text(k);
						//$("#search-result").text(v);
						//$("#search-result").append(k);
						//$("#search-result").append(v);
						//console.log(value[i].id);
						//console.log(value[i].langSet);
						//console.log(value[i].term);
						//console.log(value[i].descrip);
						//console.log(i);
						//console.log("================");
						
						//if(i == 0) {
						//	result.append("<td colspan=" + colspann + ">" +  + "</td>");		
						//} 
						//result.append("<tr>");
						//result.append("<td>" + jsonList[i].id + "</td>");
						//if(i ==0) {
						//	result.append("<td colspan=" + jsonList.length + ">" + i + "</td>");
						//}
						//result.append("<td>" + jsonList[i].langSet + "</td>");
						//result.append("<td>" + jsonList[i].term + "</td>");
						//if(jsonList[i].descrip) {
						//	result.append("<td>" + jsonList[i].descrip + "</td>");
						//}
						
						//console.log(i);

						
						var xxx = "";
						
						if(i == 0) {
							//$("#search-result-table").append($("<td rowspan=" + termEntryIdList.length + ">" + obj.termEntryId + "</td>"));
							xxx = "<td rowspan=" + termEntryIdList.length + ">" + obj.termEntryId + "</td>";
						}
						
						$("#search-result-table").append($("<tr/>").
								append(xxx).
								// append($("<td/>").html(getCountryFlag(obj.country))).
								append($("<td/>").text(obj.langSet)).
								append($("<td/>").text(obj.term)).
								append($("<td/>").attr({"style": "width:70%"}).text(obj.descrip))
						);
						
						
						//$("#search-result-table").append($("<td/>").text(getCountryFlag(obj.country)));
						//$("#search-result-table").append($("<td/>").text(obj.term));
						
/* 						if(obj.descrip) {
							$("#search-result-table").append($("<td/>").css("width", "60%").text(obj.descrip));
						} else {
							$("#search-result-table").append($("<td/>").css("width", "60%").text(N/A));
						} */
						
/* 						t += "<tr>";
						
						if(i == 0) {
							t += "<td rowspan=" + termEntryIdList.length + ">" + obj.termEntryId + "</td>";
						}
						
						t += "<td>" + getCountryFlag(obj.country) + "</td>";
						t += "<td>" + obj.term + "</td>";					
						
						if(obj.descrip) {
							t += "<td style='width:60%'>" + obj.descrip + "</td>";
						} else {
							t += "<td style='width:60%'>N/A</td>";
						}
						
						t += "</tr>"; */
					});
				});
				
				//result.append(t);
 	 		}
 	 	});
    }
});

var getCountryFlag = function(args) {
	return "<img src='/image/flag/" + args + ".png' title='" + args + "'/>";
};
</script>
		
