<%-- 
    Document   : Pagination.jsp
    Created on : Feb 21, 2025, 12:25:04 PM
    Author     : Truong Van Khang _ CE181852
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
    <c:choose>
        <c:when test="${action eq 'search'}">
            <%-- Pagination for search mode --%>
            <div style="margin-top: 20px; margin-bottom: 20px;">
                <c:if test="${currentPage > 1}">
                    <a href="${forward}?action=search&page=${currentPage - 1}&search=${keyword}"
                       style="padding: 5px 10px; border: 1px solid #ccc; border-radius: 4px; text-decoration: none; color: #333; margin-right: 10px;">
                        Previous
                    </a>
                </c:if>

                <span style="font-weight: bold; color: #555;">
                    Page ${currentPage} of ${totalPages}
                </span>

                <c:if test="${currentPage < totalPages}">
                    <a href="${forward}?action=search&page=${currentPage + 1}&search=${keyword}"
                       style="padding: 5px 10px; border: 1px solid #ccc; border-radius: 4px; text-decoration: none; color: #333; margin-left: 10px;">
                        Next
                    </a>
                </c:if>
            </div>
        </c:when>
        <c:otherwise>
            <%-- Pagination for view mode --%>
            <div style="margin-top: 20px; margin-bottom: 20px;">
                <c:if test="${currentPage > 1}">
                    <a href="${forward}?&page=${currentPage - 1}"
                       style="padding: 5px 10px; border: 1px solid #ccc; border-radius: 4px; text-decoration: none; color: #333; margin-right: 10px;">
                        Previous
                    </a>
                </c:if>

                <span style="font-weight: bold; color: #555;">
                    Page ${currentPage} of ${totalPages}
                </span>

                <c:if test="${currentPage < totalPages}">
                    <a href="${forward}?&page=${currentPage + 1}"
                       style="padding: 5px 10px; border: 1px solid #ccc; border-radius: 4px; text-decoration: none; color: #333; margin-left: 10px;">
                        Next
                    </a>
                </c:if>
            </div>
        </c:otherwise>
    </c:choose>
</body>
</html>
