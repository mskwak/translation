<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title><spring:message code="easy.tms"/></title>
    <meta name="viewport" content="width=device-width, user-scalable=yes">
    <link rel="stylesheet" type="text/css" href="http://spcoding.cafe24.com/assets/css/publishing_tree.css" >
</head>

<body>
    <h1><spring:message code="easy.tms"/></h1>
    <ul>
        <li>
            <a href="login.do" target="_blank"> 인트로 / 로그인</a>
        </li>
    	<li>
            <a href="search.do" target="_blank" >검색 </a>
        </li>
        <li>
        	<a href="search_result.do" target="_blank" >검색 결과</a>
        </li>
        <li>
        	<a href="download.do" target="_blank" >다운로드</a>
        </li>
        <li>
        	<a href="upload.do" target="_blank" >업로드</a>
        </li>
    </ul>
</body>
</html>
