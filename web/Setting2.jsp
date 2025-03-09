<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="Model.Account" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="account" value="${sessionScope.account}" />
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản lý thông tin</title>
        <link rel="stylesheet" href="assets/styleUser/Setting.css"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" />
        <!-- Bootstrap 5 CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
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
                        <li><a href="#" id="profile"><i class="fas fa-user"></i> Quản lý hồ sơ</a></li>
                        <li><a href="#" id="orders"><i class="fas fa-box"></i> Đơn hàng</a></li>
                        <li><a href="#"><i class="fas fa-gift"></i> Mã ưu đãi </a></li>
                        <li><a href="#"><i class="fas fa-star"></i> Đánh giá</a></li>
                        <li><a href="#"><i class="fas fa-heart"></i> Yêu thích</a></li>
                    </ul>
                </nav>
            </div>
            <div class="right-menu">

                <div id="profile-section">
                    <!-- Nội dung quản lý hồ sơ -->
                    <%@include file="profile.jsp" %>
                </div>

                <div id="orders-section" class="hidden">
                    <div class="order-container">
                        <div class="order-tabs">
                            <span class="tab active">Tất cả</span>
                            <span class="tab">Chờ thanh toán</span>
                            <span class="tab">Vận chuyển</span>
                            <span class="tab">Chờ giao hàng</span>
                            <span class="tab">Hoàn thành</span>
                            <span class="tab">Đã hủy</span>
                            <span class="tab">Trả hàng/Hoàn tiền</span>
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

        <script>
            document.addEventListener("DOMContentLoaded", function () {
                let profileSection = document.getElementById("profile-section");
                let ordersSection = document.getElementById("orders-section");
                let profileBtn = document.getElementById("profile");
                let ordersBtn = document.getElementById("orders");

                if (profileBtn && ordersBtn && profileSection && ordersSection) {
                    profileBtn.addEventListener("click", function () {
                        profileSection.classList.remove("hidden");
                        ordersSection.classList.add("hidden");
                    });

                    ordersBtn.addEventListener("click", function () {
                        profileSection.classList.add("hidden");
                        ordersSection.classList.remove("hidden");
                    });
                }
            });

            // Toggle thông báo khi click vào chuông
            document.addEventListener("DOMContentLoaded", function () {
                var bellIcon = document.querySelector(".notification-icon");
                var notificationBox = document.getElementById("notificationBox");

                // Toggle thông báo khi click vào chuông
                bellIcon.addEventListener("click", function (event) {
                    event.preventDefault();
                    event.stopPropagation(); // Ngăn sự kiện lan lên document
                    notificationBox.classList.toggle("active");
                });

                // Đóng thông báo khi click ra ngoài
                document.addEventListener("click", function (event) {
                    if (!notificationBox.contains(event.target) && !bellIcon.contains(event.target)) {
                        notificationBox.classList.remove("active");
                    }
                });
            });
        </script>
    </body>
</html>