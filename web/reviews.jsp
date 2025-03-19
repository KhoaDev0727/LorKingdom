<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style>
    /* Tăng chiều cao của container phân trang */
    .pagination {
        height: 50px; /* Điều chỉnh giá trị này theo nhu cầu */
        display: flex;
        align-items: center; /* Căn giữa các phần tử theo chiều dọc */
    }

    /* Tăng chiều cao của mỗi item phân trang */
    .page-item {
        height: 40px; /* Điều chỉnh giá trị này theo nhu cầu */
        display: flex;
        align-items: center; /* Căn giữa nội dung theo chiều dọc */
        justify-content: center; /* Căn giữa nội dung theo chiều ngang */
        margin: 0 5px; /* Khoảng cách giữa các item */
    }

    /* Tăng chiều cao của liên kết phân trang */
    .page-link {
        height: 100%; /* Chiếm toàn bộ chiều cao của .page-item */
        padding: 10px 15px; /* Điều chỉnh padding để tăng kích thước */
        display: flex;
        align-items: center; /* Căn giữa nội dung theo chiều dọc */
        justify-content: center; /* Căn giữa nội dung theo chiều ngang */
        font-size: 16px; /* Tăng kích thước chữ nếu cần */
        border-radius: 5px; /* Bo góc nếu muốn */
    }
    *.page-item.active .page-link {
        z-index: 3;
        color: #fff;
        background-color: #BB2D3B;
        border-color: white;

    }
    html, body {
        scroll-behavior: auto !important;
        overflow-x: hidden; /* Ngăn cuộn ngang */
        overflow-y: auto; /* Cho phép cuộn dọc nhưng không gây hiệu ứng không mong muốn */
        height: 100%; /* Đảm bảo chiều cao cố định */
    }
    *#reviews-container {
        min-height: 400px; /* Điều chỉnh giá trị tùy theo giao diện */
    }
</style>

<div class="bg-white rounded-md shadow p-4 mt-10" style="width: 1360px; margin: 0 auto;">
    <div class="container my-4">
        <h1 class="mb-4">ĐÁNH GIÁ SẢN PHẨM</h1>
        <div class="bg-light p-4 rounded mb-4">
            <div class="d-flex align-items-center mb-3">
                <span class="display-4 font-weight-bold text-danger">
                    <c:choose>
                        <c:when test="${not empty mediumRatings and mediumRatings > 0}">
                            <fmt:formatNumber value="${mediumRatings}" pattern="#.0"/>
                        </c:when>
                        <c:otherwise>
                            0
                        </c:otherwise>
                    </c:choose>

                    <span class="h4 ml-2">trên 5</span>
                </span>
            </div>
            <div class="d-flex flex-wrap">
                <button class="btn btn-danger mr-2 mb-2" data-star="6" onclick="filterReviews(event, 6)">Tất Cả</button>
                <button class="btn btn-outline-secondary mr-2 mb-2" data-star="5" onclick="filterReviews(event, 5)">5 Sao (${rating5})</button>
                <button class="btn btn-outline-secondary mr-2 mb-2" data-star="4" onclick="filterReviews(event, 4)">4 Sao (${rating4})</button>
                <button class="btn btn-outline-secondary mr-2 mb-2" data-star="3" onclick="filterReviews(event, 3)">3 Sao (${rating3})</button>
                <button class="btn btn-outline-secondary mr-2 mb-2" data-star="2" onclick="filterReviews(event, 2)">2 Sao (${rating2})</button>
                <button class="btn btn-outline-secondary mr-2 mb-2" data-star="1" onclick="filterReviews(event, 1)">1 Sao (${rating1})</button>
                <button class="btn btn-outline-secondary mr-2 mb-2" data-star="0" onclick="filterReviews(event, 0)">Có Bình Luận (${totalComment})</button>
                <button class="btn btn-outline-secondary mr-2 mb-2" data-star="-1" onclick="filterReviews(event, -1)">Có Hình Ảnh (${totalImage})</button>
            </div>
        </div>

        <div id="reviews-container">
            <!-- Nội dung đánh giá sẽ được load bằng AJAX ở đây -->
        </div>

        <div id="pagination-container" class="pagination justify-content-center">
            <!-- Phân trang sẽ được tạo động ở đây -->
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="assets/js/reviews.js"></script> 


<script>
                    $(document).ready(function () {
                        var productId = '${product.productID}';
                        console.log("Product ID: ", productId);

                        if (!productId) {
                            console.error("Product ID is not defined!");
                            $('#reviews-container').html('<p class="text-danger">Lỗi: Không tìm thấy ID sản phẩm.</p>');
                        } else {
                            // Tải đánh giá mặc định (tất cả) khi trang vừa tải xong
                            loadReviews(6, 1, productId);
                        }
                    });

                    var productId = '${product.productID}';
                    console.log("Product ID: ", productId);

                    if (!productId) {
                        console.error("Product ID is not defined!");
                        document.getElementById('reviews-container').innerHTML = '<p class="text-danger">Lỗi: Không tìm thấy ID sản phẩm.</p>';
                    } else {
                        function filterReviews(event, star) {
                            // Xóa class 'btn-danger' khỏi tất cả các nút
                            document.querySelectorAll('.d-flex .btn').forEach(button => {
                                button.classList.remove('btn-danger');
                                button.classList.add('btn-outline-secondary');
                            });
                            event.target.classList.add('btn-danger');
                            event.target.classList.remove('btn-outline-secondary');

                            loadReviews(star, 1, productId);
                        }
                    }
</script>