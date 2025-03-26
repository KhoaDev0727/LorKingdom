<%-- 
    Document   : PaginationCategory
    Created on : Mar 7, 2025, 2:53:40 PM
    Author     : admin1
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
            <c:when test="true">
                <div style="margin-top: 20px; margin-bottom: 20px;">
                    <c:if test="${currentPage > 1}">
                        <a href="${forward}&page=${currentPage - 1}"
                           style="padding: 5px 10px; border: 1px solid #ccc; border-radius: 4px; text-decoration: none; color: #333; margin-right: 10px;">
                            Về trước
                        </a>
                    </c:if>

                    <span style="font-weight: bold; color: #555;">
                        Trang ${currentPage} of ${totalPages}
                    </span>

                    <c:if test="${currentPage < totalPages}">
                        <a href="${forward}&page=${currentPage + 1}"
                           style="padding: 5px 10px; border: 1px solid #ccc; border-radius: 4px; text-decoration: none; color: #333; margin-left: 10px;">
                            Kế tiếp
                        </a>
                    </c:if>
                </div>
            </c:when>
            <c:otherwise>
                <!-- Có thể hiển thị thông báo hoặc nội dung khác nếu cần -->
            </c:otherwise>
        </c:choose>
    </body>
</html>



