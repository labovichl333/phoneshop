<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="pagePath" required="true" %>
<%@ attribute name="sort" required="true" %>
<%@ attribute name="order" required="true" %>
<%@ attribute name="value" required="true" %>
<a href="${pageContext.request.contextPath}${pagePath}?sortField=${sort}&sortOrder=${order}&query=${param.query}
" style="${sort eq param.sort and order eq param.order ? 'font-weight: bold' : ''}">${value}
</a>