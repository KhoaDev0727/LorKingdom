<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="Model.Account" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="account" value="${sessionScope.account}" />
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản lý thông tin</title>
        <link rel="stylesheet" href="assets/styleUser/Setting.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <style>
            .text-muted {
                text-align: left;
            }
            .hidden {
                display: none;
            }
            .no-order-img {
                max-width: 200px;
                margin: 20px auto;
            }
        </style>
    </head>
    <body>
        <%@include file="assets/Component/header.jsp" %>

        <div class="container-fluid">
            <div class="sidebar">
                <div class="profile">
                    <div class="d-flex container profile-img">
                        <c:if test="${not empty account.image}">
                            <img src="${account.image}" alt="Avatar" class="avatar">
                        </c:if>
                        <span class="username">Xin chào, ${account.userName}</span>
                    </div>
                </div>
                <nav class="nav">
                    <ul>
                        <li><a href="#" id="profile-link"><i class="fas fa-user"></i> Quản lý hồ sơ</a></li>
                        <li><a href="#" id="orders-link"><i class="fas fa-box"></i> Đơn hàng</a></li>
                        <li><a href="#"><i class="fas fa-gift"></i> Mã ưu đãi</a></li>
                        <li><a href="#"><i class="fas fa-star"></i> Đánh giá</a></li>
                        <li><a href="#"><i class="fas fa-heart"></i> Yêu thích</a></li>
                    </ul>
                </nav>
            </div>
            <div class="right-menu">
                <div id="profile-section">
                    <%@include file="profile.jsp" %>
                </div>
                <div id="orders-section" class="hidden">
                    <div class="order-container">
                        <div class="order-tabs">
                            <span class="tab active" data-status="all">Tất cả</span>
                            <span class="tab" data-status="shipping">Vận chuyển</span>
                            <span class="tab" data-status="pending">Chờ giao hàng</span>
                            <span class="tab" data-status="completed">Hoàn thành</span>
                            <span class="tab" data-status="cancelled">Đã hủy</span>
                        </div>
                        <div class="order-content">
                            <img src="./assets/img/notifi-order.png" alt="Chưa có đơn hàng" class="no-order-img">
                            <p>Chưa có đơn hàng</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <%@include file="assets/Component/footer.jsp" %>

        <script src="assets/js/fowrard.js">

</script>
    </body>
</html>