<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<html>
<head>
    <title>ProductList</title>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/pagination.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/main.css">
</head>
<body>
<tags:login/>
<p>
    My cart: <span id="cartState">${cart.totalQuantity} items ${cart.totalCost}</span>$
</p>
<p><a href="${pageContext.request.contextPath}/productList/1">Back to product list</a></p>
<p>

</p>
<p>
</p>
<p>
<p class="error">
    <c:if test="${error_message!=null}">
        ${error_message}
    </c:if>
</p>
<table border="1px">
    <thead>
    <tr>
        <td>Image</td>
        <td>
            Brand
        </td>
        <td>Model</td>
        <td>Color</td>
        <td>Display size</td>
        <td>
            Price
        </td>
        <td>Quantity</td>
        <td>Action</td>
    </tr>
    </thead>

    <form:form method="post" modelAttribute="cartItems" id="update-form">
        <c:forEach var="cartItem" items="${cartItems.cartItems}" varStatus="status">
            <c:set var="phone" value="${cart.items[status.index].phone}"/>
            <tr>
                <td>
                    <img src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}">
                </td>
                <td>${phone.brand}</td>
                <td>${phone.model}</td>
                <td>
                    <c:forEach var="color" items="${phone.colors}">
                        ${color.code},
                    </c:forEach>
                </td>
                <td>${phone.displaySizeInches}</td>
                <td>${phone.price}$</td>
                <td>
                    <c:set var="i" value="${status.index}"/>
                    <form:hidden path="cartItems[${i}].phoneId"/>
                    <form:input path="cartItems[${i}].quantity"/><br>
                    <form:errors path="cartItems[${i}].quantity" cssClass="error"/>
                </td>
                <td>
                    <button form="delete" formaction="<c:url value = "/cart/delete/${phone.id}" />">
                        Delete
                    </button>
                </td>
            </tr>

        </c:forEach>
        <p>
            <input type="submit" value="Update">
        </p>
    </form:form>

    <form:form id="delete" method="delete">
    </form:form>
</table>
</p>
<form action="${pageContext.request.contextPath}/order" method="post">
    <button type="submit" >Order</button>
</form>
</body>
</html>
