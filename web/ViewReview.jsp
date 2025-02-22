<%-- 
    Document   : ViewReview
    Created on : Feb 20, 2025, 7:45:44 PM
    Author     : Truong Van Khang _ CE181852
--%>

<%@page contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Product Review</title>
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet">
    </head>
    <body class="bg-white text-dark">
        <div class="container my-4">
            <h1 class="mb-4">ĐÁNH GIÁ SẢN PHẨM</h1>
            <div class="bg-light p-4 rounded mb-4">

                <div class="d-flex align-items-center mb-3">
                    <span class="display-4 font-weight-bold text-danger">
                        <fmt:setLocale value="vi_VN" />
                        <fmt:formatNumber value="${mediumRatings}" type="number" minFractionDigits="1" maxFractionDigits="1" />
                        <span class="h4 ml-2">trên 5</span>
                </div>
                <div class="d-flex flex-wrap">
                    <button class="btn btn-danger mr-2 mb-2">Tất Cả</button>
                    <button class="btn btn-outline-secondary mr-2 mb-2">5 Sao (${rating5 != null ? rating5 : 0})</button>
                    <button class="btn btn-outline-secondary mr-2 mb-2">4 Sao (${rating4 != null ? rating4 : 0})</button>
                    <button class="btn btn-outline-secondary mr-2 mb-2">3 Sao (${rating3 != null ? rating3 : 0})</button>
                    <button class="btn btn-outline-secondary mr-2 mb-2">2 Sao (${rating2 != null ? rating2 : 0})</button>
                    <button class="btn btn-outline-secondary mr-2 mb-2">1 Sao (${rating1 != null ? rating1 : 0})</button>
                    <button class="btn btn-outline-secondary mr-2 mb-2">Có Bình Luận (${totalComment != null ? totalComment : 0})</button>
                    <button class="btn btn-outline-secondary mr-2 mb-2">Có Hình Ảnh (${totalImage != null ? totalImage : 0})</button>
                </div>
            </div>
            <c:choose>
                <c:when test="${not empty listReviews}">
                    <c:forEach var="review" items="${listReviews}">
                        <c:forEach var="inforCustomer" items="${inforCustomers}">
                            <c:if test="${review.accountID eq inforCustomer.accountId}">
                                <c:set var="customer" value="${inforCustomer}"/>
                            </c:if>
                        </c:forEach>
                        <div class="border-bottom pb-4 mb-4">
                            <div class="d-flex align-items-center mb-2">
                                <img src="https://placehold.co/40x40" alt="User avatar" class="rounded-circle mr-2" width="40" height="40">
                                <div>
                                    <span class="font-weight-bold">${customer.userName}</span>
                                    <div class="d-flex align-items-center">
                                        <c:forEach begin="1" end="${review.rating}" var="i">
                                            <i class="fas fa-star text-danger"></i>
                                        </c:forEach>
                                        <c:if test="${review.rating % 1 != 0}">
                                            <i class="fas fa-star-half-alt text-danger"></i>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                            <div class="text-muted mb-2">
                                <fmt:formatDate value="${review.reviewAt}" pattern="dd/MM/yyyy HH:mm"/>
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
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>
