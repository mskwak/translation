<!-- 아래 인코딩 설정을 해주어야만 브라우저에서 한글이 깨지지 않고 정상 출력된다. -->
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!-- 아래 JSTL을 사용하기 위해 pom.xml에 jstl, tablib 설정을 해주어야 했다. -->
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

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
<div id="header-menu">
	<span><a href="/searchGlossary.do"><img src="/image/search.png" /></a></span>
	<span style="display:inline-block; width:10px;"></span>
	<span><a href="/listAttachments.do"><img src="image/upload.png"></a></span>
</div>
</body>
</html>
		
