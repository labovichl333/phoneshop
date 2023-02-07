<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>QuickAdd</title>
    <meta charset="utf-8">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/main.css">
</head>
<body>
<p>
    <a href="${pageContext.servletContext.contextPath}/productList/1">Back to product list</a>
</p>

<c:if test="${not empty allSuccessMessage}">
    <div class="success">${allSuccessMessage}</div>
</c:if>
<c:if test="${not empty productsWasAdd}">
    <c:forEach var="item" items="${productsWasAdd}">
        <p>
        <div class="success">${item}</div>
        </p>
    </c:forEach>

</c:if>


<c:if test="${not empty errorsMessage}">
    <div class="error">${errorsMessage}</div>
</c:if>
<div class="container">
    <div class="row">
        <div class="col">
            <p>
                Product code (Model)
            </p>
        </div>
        <div class="col">
            <p>
                QTY
            </p>
        </div>
    </div>
    <form:form method="post" modelAttribute="items" action="${pageContext.servletContext.contextPath}/quickAdd">
        <c:forEach begin="0"  end="7" varStatus="status">
            <c:set var="i" value="${status.index}"/>
            <div class="row">
                <div class="col">
                    <p>
                        <form:input path="items[${i}].model"/><br>
                        <form:errors path="items[${i}].model" cssClass="error"/>
                    </p>

                </div>
                <div class="col">
                    <p>
                        <form:input path="items[${i}].quantity"/><br>
                        <form:errors path="items[${i}].quantity" cssClass="error"/>
                    </p>

                </div>
            </div>
        </c:forEach>
        <p>
            <input type="submit" value="Add to cart">
        </p>
    </form:form>
</div>
</body>
</html>
