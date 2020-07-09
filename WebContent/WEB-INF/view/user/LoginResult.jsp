<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="poly.util.CmmUtil" %>
<%
//Controller에 저장된 세션으로 로그인할때 생성됨
String SS_USER_ID = CmmUtil.nvl((String)session.getAttribute("SS_USER_ID"));
String msg = CmmUtil.nvl((String)request.getAttribute("msg"));
String url = CmmUtil.nvl((String)request.getAttribute("url"));

%>      
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인되었습니다.</title>
<script>
window.onload = function(){
   alert('<%=msg%>');
   location.href='<%=url%>';
};
</script>
</head>
<body>
</body>
</html>