<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Murach's Java Servlets and JSP</title>
    <link rel="stylesheet" href="styles/main.css" type="text/css"/>
</head>
<body>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page session="true" %>

<%
    // Xóa session c? khi nh?n F5 ho?c truy c?p m?i
    if (request.getParameter("reset") != null) {
        session.invalidate();
    }
%>

<!-- N?u ch?a có sqlStatement trong session thì gán m?c ??nh -->
<c:if test="${empty sqlStatement}">
    <c:set var="sqlStatement" value="SELECT * FROM user;" scope="session" />
</c:if>

<h1>The SQL Gateway</h1>
<p>Enter an SQL statement and click the Execute button.</p>

<p><b>SQL statement:</b></p>

<form action="sqlGateway" method="post">
    <textarea name="sqlStatement" cols="60" rows="8">${sqlStatement}</textarea><br>
    <input type="submit" value="Execute">
    <a href="index.jsp?reset=true" style="margin-left:10px;">Reset</a>
</form>

<p><b>SQL result:</b></p>
${sqlResult}


</body>
</html>