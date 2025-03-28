<%-- 
    Document   : PaginationReview
    Created on : Feb 22, 2025, 1:26:37 PM
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
                    <a href="${forward}?action=${action}&page=${currentPage - 1}&filterUserProduct=${filterUserProduct}&filterRating=${filterRating}&filterStatus=${filterStatus}" 
                       style="padding: 5px 10px; border: 1px solid #ccc; border-radius: 4px; text-decoration: none; color: #333; margin-right: 10px;">
                        Quay Lại
                    </a>
                </c:if>
                <span style="font-weight: bold; color: #555;">
                    Trang ${currentPage} Trên ${totalPages}
                </span>
                <c:if test="${currentPage < totalPages}">
                    <a href="${forward}?action=${action}&page=${currentPage + 1}&filterUserProduct=${filterUserProduct}&filterRating=${filterRating}&filterStatus=${filterStatus}" 
                       style="padding: 5px 10px; border: 1px solid #ccc; border-radius: 4px; text-decoration: none; color: #333; margin-left: 10px;">
                        Tiếp Theo
                    </a>
                </c:if>
            </div>
        </c:when>
        <c:otherwise>
            <%-- Pagination for view mode --%>
            <div style="margin-top: 20px; margin-bottom: 20px;">
                <c:if test="${currentPage > 1}">
                    <a href="${forward}?page=${currentPage - 1}"
                       style="padding: 5px 10px; border: 1px solid #ccc; border-radius: 4px; text-decoration: none; color: #333; margin-right: 10px;">
                        Quay Lại
                    </a>
                </c:if>

                <span style="font-weight: bold; color: #555;">
                    Trang ${currentPage} Trên ${totalPages}
                </span>

                <c:if test="${currentPage < totalPages}">
                    <a href="${forward}?page=${currentPage + 1}"
                       style="padding: 5px 10px; border: 1px solid #ccc; border-radius: 4px; text-decoration: none; color: #333; margin-left: 10px;">
                        Tiếp Theo
                    </a>
                </c:if>
            </div>
        </c:otherwise>
    </c:choose>
</body>
</html>
