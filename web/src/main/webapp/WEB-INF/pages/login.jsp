<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/main.css">
</head>
<body>
<div class="error">
    <c:if test="${not empty error}">
        ${error}
    </c:if>
</div>

<form method="post" action="${pageContext.servletContext.contextPath}/login">
    <p>
        <label>
            Login
            <input type="text" name="username">
        </label>
    </p>
    <p>
        <label>
            Password
            <input type="password" name="password">
        </label>
    </p>

    <input type="submit">
</form>

</body>
</html>
