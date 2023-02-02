<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<html>
<head>
    <title>Orders</title>
    <meta charset="utf-8">
</head>
<body>
<p><a href="#">Login</a></p>
<p>
<form action="${pageContext.servletContext.contextPath}/productList/1">
    <input type="text" name="query" value="${param.query}">
    <input type="submit" value="search">
</form>
</p>
<p>
<table border="1px">
    <thead>
    <tr>
        <td>
            Order number
        </td>
        <td>Customer</td>
        <td>Phone</td>
        <td>Address</td>
        <td>
           Date
        </td>
        <td>Total Price</td>
        <td>Status</td>
    </tr>
    </thead>

    <c:forEach var="order" items="${orders}">
        <tr>
            <td><a href="${pageContext.servletContext.contextPath}/admin/orders/${order.id}">${order.id}</a></td>
            <td>${order.firstName.concat(" ").concat(order.lastName)}</td>
            <td>
               ${order.contactPhoneNo}
            </td>
            <td>
                    ${order.deliveryAddress}
            </td>
            <td>${order.createdDate.format(formatter)}</td>
            <td>${order.totalPrice}$</td>
            <td>
                ${order.status}
            </td>
        </tr>
    </c:forEach>
</table>
</p>

</body>
</html>
