<%-- 
    Document   : PaginationProduct
    Created on : Mar 7, 2025, 7:05:02 PM
    Author     : admin1
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Phân trang sản phẩm</title>
    </head>
    <body>
    <c:choose>
        <c:when test="true">
            <div style="margin-top: 20px; margin-bottom: 20px;">
                <c:if test="${currentPage > 1}">
                    <a href="${forward}&page=${currentPage - 1}"
                       style="padding: 5px 10px; border: 1px solid #ccc; border-radius: 4px; text-decoration: none; color: #333; margin-right: 10px;">
                        Quay Lại
                    </a>
                </c:if>

                <span style="font-weight: bold; color: #555;">
                    Trang ${currentPage} Trên ${totalPages}
                </span>

                <c:if test="${currentPage < totalPages}">
                    <a href="${forward}&page=${currentPage + 1}"
                       style="padding: 5px 10px; border: 1px solid #ccc; border-radius: 4px; text-decoration: none; color: #333; margin-left: 10px;">
                       Tiếp Theo
                    </a>
                </c:if>
            </div>
        </c:when>
       
    </c:choose>
</body>
</html>
