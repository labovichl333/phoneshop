<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<sec:authorize access="isAnonymous()">
    <a href="${pageContext.servletContext.contextPath}/login">Login</a>
</sec:authorize>
<sec:authorize access="isAuthenticated()">
    <sec:authentication property="principal.username"/>
    <a href="${pageContext.servletContext.contextPath}/logout">Logout</a>
</sec:authorize>
<sec:authorize access="hasRole('ROLE_ADMIN')">
    <a href="${pageContext.servletContext.contextPath}/admin/orders">Orders</a>
</sec:authorize>