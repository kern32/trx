<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User form</title>
</head>
<body>

<form action="<c:url value='/users/add'/>" method="post">
    <input name="username" type="text" placeholder="Username...">
    <input value="Save" type="submit">
</form>

</body>
</html>
