<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>ProductList</title>
    <meta charset="utf-8">
</head>
<body>

<h1>Order overview</h1>
<h2>Thank you for order</h2>
<h4>Order number:${order.id}</h4>
<h4>Order status:${order.status}</h4>
<p>
<table border="1px">
    <thead>
    <tr>
        <td>
            Brand
        </td>
        <td>Model</td>
        <td>Color</td>
        <td>Display size</td>
        <td>Quantity</td>
        <td>Prise</td>
    </tr>
    </thead>
    <c:forEach var="orderItem" items="${order.orderItems}">
        <tr>
            <c:set var="phone" value="${orderItem.phone}"/>
            <td>${phone.brand}</td>
            <td>${phone.model}</td>
            <td>
                <c:forEach var="color" items="${phone.colors}">
                    ${color.code},
                </c:forEach>
            </td>
            <td>${phone.displaySizeInches}</td>
            <td>${orderItem.quantity}</td>
            <td>${phone.price}</td>
        </tr>
    </c:forEach>

    <tr>
        <td style="border: none!important"></td>
        <td style="border: none!important"></td>
        <td style="border: none!important"></td>
        <td style="border: none!important"></td>
        <td>Subtotal</td>
        <td>${order.subtotal}</td>
    </tr>
    <tr>
        <td style="border: none!important"></td>
        <td style="border: none!important"></td>
        <td style="border: none!important"></td>
        <td style="border: none!important"></td>
        <td>Delivery</td>
        <td>${order.deliveryPrice}</td>
    </tr>
    <tr>
        <td style="border: none!important"></td>
        <td style="border: none!important"></td>
        <td style="border: none!important"></td>
        <td style="border: none!important"></td>
        <td>Total</td>
        <td>${order.totalPrice}</td>
    </tr>

</table>
</p>

<div>
    <div>
        <table style="border: none!important">
            <thead>
            <tr>
                <td> Delivery Info</td>
            </tr>
            </thead>
            <tr>
                <td>First name</td>
                <td>${order.firstName}</td>
            </tr>
            <tr>
                <td>Last name</td>
                <td>${order.lastName}</td>
            </tr>
            <tr>
                <td>Address</td>
                <td>${order.deliveryAddress}</td>
            </tr>
            <tr>
                <td>Contact phone</td>
                <td>${order.contactPhoneNo}</td>
            </tr>
            <tr>
                <td>Additional Information</td>
                <td>${order.additionalInformation}</td>
            </tr>
        </table>
    </div>
</div>

<form>
    <input type="button" value="Back" onClick='location.href="${pageContext.request.contextPath}/admin/orders"'>
</form>
<form method="post" action="${pageContext.request.contextPath}/admin/orders/${order.id}">
    <input type="hidden" value="DELIVERED" name="orderStatus">
    <input type="submit" value="Delivered">
</form>
<form method="post" action="${pageContext.request.contextPath}/admin/orders/${order.id}">
    <input type="hidden" value="REJECTED" name="orderStatus">
    <input type="submit" value="Rejected">
</form>
</body>
</html>
