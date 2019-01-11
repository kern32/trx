<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>User</title>
</head>
<body>
<c:forEach items="${userList}" var="u">
    <p>${u.username}</p>
</c:forEach>

<br/>
<a href="<c:url value='/users/add'/>">Add</a>
</body>
</html>
