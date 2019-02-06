<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>UserList</title>
</head>
<body>

<table width="70%", border="1px solid black">
    <tr>
        <th>Username</th>
        <th>Password</th>
        <th>Email</th>
        <th>Resource</th>
        <th>Link</th>
    </tr>

        <c:forEach items="${userList}" var="c">
    <tr><td>${c.username}</td>
        <td>${c.password}</td>
        <td>${c.email}</td>
        <td>${c.resource}</td>
        <td>${c.link}</td> </tr>
        </c:forEach>

</table>
<br/>
<a href="<c:url value='/user/add'/>">Add</a>
</body>
</html>
