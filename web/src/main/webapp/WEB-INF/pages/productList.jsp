<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<html>
<head>
    <title>ProductList</title>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/pagination.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/main.css">
</head>
<body>
<p><a href="#">Login</a></p>
<p>
    My cart: <span id="cartState">${cart.totalQuantity} items ${cart.totalCost}</span>$
</p>
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
        <td>Image</td>
        <td>
            Brand
            <tags:sortLink pagePath="/productList/1" sort="brand" order="asc" value="&#9650;"></tags:sortLink>
            <tags:sortLink pagePath="/productList/1" sort="brand" order="desc" value="&#9660;"></tags:sortLink>
        </td>
        <td>Model</td>
        <td>Color</td>
        <td>Display size</td>
        <td>
            Price
            <tags:sortLink pagePath="/productList/1" sort="price" order="asc" value="&#9650;"></tags:sortLink>
            <tags:sortLink pagePath="/productList/1" sort="price" order="desc" value="&#9660;"></tags:sortLink>
        </td>
        <td>Quantity</td>
        <td>Action</td>
    </tr>
    </thead>
    <c:forEach var="phone" items="${phones}">
        <tr>
            <td>
                <img src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}">
            </td>
            <td>${phone.brand}</td>
            <td>${phone.model}</td>
            <td>
                <c:set var="colors" value=""></c:set>
                <c:forEach  var="color" items="${phone.colors}">
                    ${color.code},
                </c:forEach>
            </td>
            <td>${phone.displaySizeInches}</td>
            <td>${phone.price}$</td>
            <td>
                <form id="${phone.id}">
                    <input type="text" id="${phone.id}quantity" name="quantity" value="1">
                    <input type="hidden" name="phoneId" value="${phone.id}">
                </form>
                <div class="error" id="${phone.id}error"></div>
            </td>
            <td>
                <button type="button" onclick="addToCartForm(${phone.id},'${pageContext.servletContext.contextPath}/ajaxCart')">
                    Add to cart
                </button>
                </td>
        </tr>
    </c:forEach>
</table>
</p>

<div class="pagination">
    <c:set var="prevPage" value="${pageNumber == 1 ? pageNumber : pageNumber - 1}"/>

    <a href="${pageContext.request.contextPath}/productList/${prevPage}?query=${param.query}&sortField=${param.sortField}&sortOrder=${param.orderField}">&laquo;</a>

    <c:forEach var="page" items="${pages}">
        <a class="${pageNumber==page? 'active':''}"
           href="${pageContext.request.contextPath}/productList/${page}?query=${param.query}&sortField=${param.sortField}&sortOrder=${param.orderField}">${page}</a>
    </c:forEach>

    <c:set var="nextPage" value="${pageNumber == lastPage ? pageNumber : pageNumber + 1}"/>
    <a href="${pageContext.request.contextPath}/productList/${nextPage}?query=${param.query}&sortField=${param.sortField}&sortOrder=${param.orderField}">&raquo;</a>

</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="${pageContext.servletContext.contextPath}/js/main.js"></script>
</body>
</html>
