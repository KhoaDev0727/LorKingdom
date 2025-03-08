<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"> <!-- Bootstrap Import -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
        <link rel="stylesheet" href="./assets/styleUser/styleHeader.css"/>
        <title>header page</title>
        <style>
            .avatar img {
                width: 50px;
                height: 50px;
                border-radius: 50%; /* Làm tròn ảnh */
                border: 3px solid #ccc; /* Thêm viền màu xám */
                object-fit: cover; /* Đảm bảo ảnh không bị méo */
            }
        </style>
    </head>
    <body>
        <!-- Header Section -->
        <header class="header">
            <div class="header_first">
                <div class="header-left d-flex align-items-center gap-3">
                    <a href="home.jsp" class="logo">
                        <img src="assets/img/logo-login.png" alt="Lor Logo">
                    </a>
                    <div class="input-group search-container">
                        <span class="input-group-text bg-white border-end-0">
                            <i class="fa fa-search"></i>
                        </span>
                        <input type="text" class="form-control border-start-0" placeholder="Tìm sản phẩm">
                    </div>
                </div>

                <div class="header-right">
                    <a href="getList" >Sản Phẩm </a>
                    <a href="aboutus.jsp" data-key="aboutUs">Giới Thiệu</a>
                    <a href="#" data-key="help">Trợ giúp</a>

                    <!-- Icon notification here -->
                    <div class="notification-container">
                        <a href="#" class="notification-icon" style="color: gold; font-size: 28px; background: none;"
                           onclick="toggleNotification(event)">
                            <i class="fas fa-bell"></i>
                            <span class="notification-badge">
                                0
                            </span>
                        </a>
                        <!-- Box thông báo -->
                        <div class="notification-box" id="notificationBox">
                            <h4>Thông Báo</h4>
                            <div class="notification-item">
                                <img src="" alt="icon">
                                <p><strong>Bộ sưu tập dành cho bạn</strong></p>
                                <p>Top sản phẩm hot nhất đang giảm giá!</p>
                            </div>
                            <div class="notification-item">
                                <img src="" alt="icon">
                                <p><strong>Mã Freeship</strong></p>
                                <p>Sắp hết hạn, dùng ngay hôm nay!</p>
                            </div>
                        </div>
                    </div>


                    <a href="Cart" data-key="cart" class="cart-link position-relative">
                        <i class="fas fa-shopping-cart fa-lg"></i>
                        <span class="cart-badge">${not empty sessionScope.totalCart and sessionScope.totalCart ne 0 ? sessionScope.totalCart : '0'}</span>
                    </a>
                    <div class="avatar-container">
                        <div class="avatar">
                            <c:choose>
                                <c:when test="${not empty sessionScope.imgePath}">
                                    <img src="${pageContext.request.contextPath}/${sessionScope.imgePath}" alt="User Avatar">
                                </c:when>
                                <c:otherwise>
                                    <img src="${pageContext.request.contextPath}/assets/img/profile.png" alt="Default Avatar">
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div class="menu-avatar">
                            <c:choose>
                                <c:when test="${not empty sessionScope.imgePath}">                                 
                                    <a href="Setting.jsp" class="menu-item-avatar text-dark p-3">
                                        <i class="fas fa-gear"></i> Setting
                                    </a>
                                    <a href="LogoutServlet" class="menu-item-avatar text-dark p-3">
                                        <i class="fas fa-sign-out-alt"></i> Logout
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a href="login.jsp" class="menu-item-avatar text-dark p-3">
                                        <i class="fas fa-sign-in-alt"></i> Login
                                    </a>
                                    <a href="register.jsp" class="menu-item-avatar text-dark p-3">
                                        <i class="fas fa-user-plus"></i> Register
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </header>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script>
                               function updateCart() {
                                   $.ajax({
                                       url: 'CartManagementServlet',
                                       type: 'POST',
                                       data: {action: 'getTotalCart'},
                                       dataType: 'json',
                                       success: function (data) {
                                           $('.cart-badge').text(data.cartSize);
                                       },
                                       error: function (xhr, status, error) {
                                           console.error('Error:', error);
                                       }
                                   });
                               }
                               // Cập nhật mỗi 5 giây
                               setInterval(updateCart, 2000);

                               // Gọi ngay khi trang load
                               $(document).ready(function () {
                                   updateCart();
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
