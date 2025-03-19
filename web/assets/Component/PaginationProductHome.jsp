<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Product Pagination</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
        
        <style>
            .pagination-container {
                margin: 20px 0;
                display: flex;
                justify-content: center;
                align-items: center;
                gap: 5px;
            }

            .pagination-container a, 
            .pagination-container span {
                display: inline-block;
                margin: 0;
                border: 2px solid #EF4444;
                border-radius: 4px;
                text-decoration: none;
                color: #EF4444;
                font-size: 18px;
                width: 60px;
                height: 60px;
                line-height: 56px;
                text-align: center;
                transition: all 0.3s ease;
                position: relative;
                user-select: none;
            }

            /* Trang hiện tại - NỔI BẬT */
            .pagination-container a.current {
                background-color: #EF4444;
                color: white !important;
                font-weight: 700;
                border-width: 3px;
                box-shadow: 0 0 8px rgba(239, 68, 68, 0.5);
                pointer-events: none; /* Ngăn không cho click */
            }

            /* Hover chỉ áp dụng cho trang KHÔNG phải hiện tại */
            .pagination-container a:not(.current):hover {
                background-color: #EF4444;
                color: white !important;
            }

            /* Ellipsis */
            .pagination-container span.ellipsis {
                border: none !important;
                color: #666 !important;
                pointer-events: none;
                width: auto;
                min-width: 20px;
            }

            /* Nút Previous/Next */
            .pagination-container a.prev-next {
                background-color: #f8fafc;
            }

            /* Responsive */
            @media (max-width: 768px) {
                .pagination-container a, 
                .pagination-container span {
                    width: 40px;
                    height: 40px;
                    line-height: 36px;
                    font-size: 16px;
                }
            }
        </style>
    </head>
    <body>
        <div class="pagination-container">
            <!-- Previous -->
            <c:if test="${currentPage > 1}">
                <a href="javascript:void(0)" 
                   onclick="event.preventDefault(); goToPage(${currentPage - 1})" 
                   class="prev-next" 
                   data-page="${currentPage - 1}">
                    <i class="fas fa-chevron-left"></i>
                </a>
            </c:if>

            <!-- Page 1 -->
            <a href="javascript:void(0)"
               onclick="event.preventDefault(); goToPage(1)"
               class="${currentPage == 1 ? 'current' : ''}"
               data-page="1">1</a>

            <!-- Ellipsis trước -->
            <c:if test="${currentPage > 3}">
                <span class="ellipsis">&hellip;</span>
            </c:if>

            <!-- Các trang giữa -->
            <c:forEach begin="${Math.max(2, currentPage - 1)}" 
                       end="${Math.min(totalPages - 1, currentPage + 1)}" 
                       var="page">
                <a href="javascript:void(0)" 
                   onclick="goToPage(${page})"
                   class="${currentPage == page ? 'current' : ''}"
                   data-page="${page}">${page}</a>
            </c:forEach>

            <!-- Ellipsis sau -->
            <c:if test="${currentPage < totalPages - 2}">
                <span class="ellipsis">&hellip;</span>
            </c:if>

            <!-- Trang cuối -->
            <c:if test="${totalPages > 1}">
                <a href="javascript:void(0)" 
                   onclick="goToPage(${totalPages})"
                   class="${currentPage == totalPages ? 'current' : ''}"
                   data-page="${totalPages}">${totalPages}</a>
            </c:if>

            <!-- Next -->
            <c:if test="${currentPage < totalPages}">
                <a href="javascript:void(0)" 
                   onclick="goToPage(${currentPage + 1})" 
                   class="prev-next"
                   data-page="${currentPage + 1}">
                    <i class="fas fa-chevron-right"></i>
                </a>
            </c:if>
        </div>

         <script>
    function goToPage(page) {
        // Lưu thông tin cần scroll vào sessionStorage
        sessionStorage.setItem('shouldScroll', 'true');
        
        // Chuyển trang
        const url = new URL(window.location.href);
        url.searchParams.set('page', page);
        window.location.href = url.toString();
    }

    // Tự động cuộn khi trang tải xong
    window.addEventListener('load', () => {
        // Kiểm tra nếu cần scroll
        if (sessionStorage.getItem('shouldScroll') === 'true') {
            const marker = document.querySelector('.your-class'); // Thay bằng class của bạn
            if (marker) {
                marker.scrollIntoView({
                    behavior: 'smooth', // Cuộn mượt
                    block: 'start'      // Căn đầu phần tử vào đầu viewport
                });
            }
            sessionStorage.removeItem('shouldScroll');
        }
    });
</script>
    </body>
</html>