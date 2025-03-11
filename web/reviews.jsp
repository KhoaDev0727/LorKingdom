<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Đánh Giá Sản Phẩm</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    </head>
    <body>
        <div class=" bg-white rounded-md shadow p-4 mt-10" style="width: 1360px; margin:0 auto;">
            <div class="container my-4 ">
                <h1 class="mb-4">ĐÁNH GIÁ SẢN PHẨM</h1>
                <div class="bg-light p-4 rounded mb-4">
                    <div class="d-flex align-items-center mb-3">
                        <span class="display-4 font-weight-bold text-danger">
                            <fmt:setLocale value="vi_VN" />
                            <fmt:formatNumber value="${mediumRatings}" type="number" maxFractionDigits="1" />
                            <span class="h4 ml-2">trên 5</span>
                        </span>
                    </div>
                    <div class="d-flex flex-wrap">
                        <!-- Truyền vào keyword tương ứng với bộ lọc -->
                        <button class="btn btn-danger mr-2 mb-2" onclick="filterReviews(6)">Tất Cả</button>
                        <button class="btn btn-outline-secondary mr-2 mb-2" onclick="filterReviews(5)">5 Sao (${rating5 != null ? rating5 : 0})</button>
                        <button class="btn btn-outline-secondary mr-2 mb-2" onclick="filterReviews(4)">4 Sao (${rating4 != null ? rating4 : 0})</button>
                        <button class="btn btn-outline-secondary mr-2 mb-2" onclick="filterReviews(3)">3 Sao (${rating3 != null ? rating3 : 0})</button>
                        <button class="btn btn-outline-secondary mr-2 mb-2" onclick="filterReviews(2)">2 Sao (${rating2 != null ? rating2 : 0})</button>
                        <button class="btn btn-outline-secondary mr-2 mb-2" onclick="filterReviews(1)">1 Sao (${rating1 != null ? rating1 : 0})</button>
                        <button class="btn btn-outline-secondary mr-2 mb-2" onclick="filterReviews(0)">Có Bình Luận (${totalComment != null ? totalComment : 0})</button>
                        <button class="btn btn-outline-secondary mr-2 mb-2" onclick="filterReviews(-1)">Có Hình Ảnh (${totalImage != null ? totalImage : 0})</button>
                    </div>
                </div>

                <!-- Container chứa danh sách đánh giá -->
                <div id="reviews-container">
                    <c:choose>
                        <c:when test="${not empty listReviews}">
                            <c:forEach var="review" items="${listReviews}">
                                <c:set var="customer" value="" />
                                <c:forEach var="inforCustomer" items="${inforCustomers}">
                                    <c:if test="${review.accountID eq inforCustomer.accountId}">
                                        <c:set var="customer" value="${inforCustomer}" />
                                    </c:if>
                                </c:forEach>
                                <div class="border-bottom pb-4 mb-4">
                                    <div class="d-flex align-items-center mb-2">
                                        <img src="${customer.image}" alt="User avatar" class="rounded-circle mr-2" style="width:60px; height:60px">
                                        <div>
                                            <span class="font-weight-bold">${customer.userName}</span>
                                            <div class="d-flex align-items-center">
                                                <c:forEach begin="1" end="${review.rating}" var="i">
                                                    <i class="fas fa-star text-danger"></i>
                                                </c:forEach>
                                                <!-- Sao rỗng (empty stars) -->
                                                <c:forEach begin="${review.rating + 1}" end="5" var="i">
                                                    <i class="far fa-star text-danger"></i>
                                                </c:forEach>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="text-muted mb-2">
                                        <fmt:formatDate value="${review.reviewAt}" pattern=" HH:mm:ss dd/MM/yyyy"/>
                                    </div>
                                    <p class="mb-2">${review.comment}</p>
                                    <c:if test="${not empty review.imgReview}">
                                        <div class="mb-2">
                                            <img src="${review.imgReview}" alt="Product image" class="img-thumbnail" width="100" height="100">
                                        </div>
                                    </c:if>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <p class="text-center text-muted">Chưa có đánh giá nào.</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <script>
            // Sử dụng biểu thức EL để lấy productID (đảm bảo product đã được đặt trong request)
            var productId = '${product.productID}';
            // Hàm gửi yêu cầu AJAX đến Servlet để lọc đánh giá
            function filterReviews(star) {
                $.ajax({
                    url: 'ReviewManagementServlet', // Đường dẫn Servlet
                    method: 'POST',
                    data: {
                        action: "fillterCustomer",
                        keyWord: star,
                        productID: productId
                    },
                    dataType: 'json',
                    success: function (data) {
                        var html = "";
                        if (data && data.length > 0) {
                            $.each(data, function (index, review) {
                                html += '<div class="border-bottom pb-4 mb-4">';
                                html += '    <div class="d-flex align-items-center mb-2">';
                                html += '        <img src= "' + review.account.image + '" alt="User avatar" class="rounded-circle mr-2" width="40" height="40">';
                                html += '        <div>';
                                html += '            <span class="font-weight-bold">' + review.account.userName + '</span>';
                                html += '            <div class="d-flex align-items-center">';
                                for (var i = 1; i <= Math.floor(review.Rating); i++) {
                                    html += '<i class="fas fa-star text-danger"></i>';
                                }
                                for (var i = Math.ceil(review.Rating); i < 5; i++) {
                                    html += '<i class="far fa-star text-danger"></i>';
                                }
                                html += '            </div>';
                                html += '        </div>';
                                html += '    </div>';
                                html += '    <div class="text-muted mb-2">' + new Date(review.reviewAt).toLocaleString() + '</div>';
                                html += '    <p class="mb-2">' + review.Comment + '</p>';
                                if (review.imgReview && review.imgReview.trim() !== "") {
                                    html += '<div class="mb-2"><img src="' + review.imgReview + '" alt="Product image" class="img-thumbnail" width="100" height="100"></div>';
                                }
                                html += '</div>';
                            });
                        } else {
                            html = '<p class="text-center text-muted">Chưa có đánh giá nào.</p>';
                        }
                        $("#reviews-container").html(html);
                    },
                    error: function () {
                        alert("Có lỗi xảy ra khi tải dữ liệu đánh giá.");
                    }
                });
            }
        </script>
    </body>
</html>
