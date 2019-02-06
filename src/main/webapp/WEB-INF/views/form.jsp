<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User form</title>
</head>
<body>
<form action="<c:url value='/user/add'/>" method="post" modelAttribute="user">
    <input name="username" id="username" type="text" placeholder="Username..." value="kernel32" >
    <input name="password" id="password" type="text" placeholder="Password..." value="123123123">
    <input name="email" id="email" type="text" placeholder="Email..." value="email@mail.com">
    <input name="resource" id="resource" type="text" placeholder="Resource..." value="1">
    <input name="link" id="link" type="text" placeholder="Link..." value="http://linl.com">
    <input value="Save" type="submit">
</form>
</body>
</html>
