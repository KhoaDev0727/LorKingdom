<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Product Pagination</title>
    </head>
    <body>
        <div style="margin-top: 20px; margin-bottom: 20px;">
            <c:if test="${currentPage > 1}">
                <a href="#" onclick="goToPage(${currentPage - 1}); return false;"
                   style="padding: 5px 10px; border: 1px solid #ccc; border-radius: 4px; text-decoration: none; color: #333; margin-right: 10px;">
                    Previous
                </a>
            </c:if>
            
            <span style="font-weight: bold; color: #555;">
                Page ${currentPage} of ${totalPages}
            </span>
            
            <c:if test="${currentPage < totalPages}">
                <a href="#" onclick="goToPage(${currentPage + 1}); return false;"
                   style="padding: 5px 10px; border: 1px solid #ccc; border-radius: 4px; text-decoration: none; color: #333; margin-left: 10px;">
                    Next
                </a>
            </c:if>
        </div>
    </body>
</html>
