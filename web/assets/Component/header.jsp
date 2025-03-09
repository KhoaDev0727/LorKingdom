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

            /* Phong cách notification box giống Shopee */
            .notification-container {
                position: relative;
                display: inline-block;
            }

            .notification-icon {
                color: gold;
                font-size: 28px;
                background: none;
                text-decoration: none;
                position: relative;
            }

            .notification-badge {
                position: absolute;
                top: -5px;
                right: -10px;
                background: #ff2a00; 
                color: white;
                border-radius: 50%;
                width: 20px;
                height: 20px;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 12px;
            }

            .notification-box {
                display: none;
                position: absolute;
                right: 0;
                top: 40px;
                width: 350px;
                background: white;
                border-radius: 4px;
                box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
                z-index: 1000;
                max-height: 400px;
                overflow-y: auto;
            }

            .notification-box.active {
                display: block;
            }

            .notification-box h4 {
                padding: 10px 15px;
                margin: 0;
                font-size: 16px;
                color: #333;
                border-bottom: 1px solid #eee;
            }

            .notification-item {
                padding: 10px 15px;
                border-bottom: 1px solid #f5f5f5;
                display: flex;
                align-items: center;
                gap: 10px;
                transition: background 0.2s;
                cursor: pointer;
            }

            .notification-item:hover {
                background: #f8f8f8;
            }

            .notification-item img {
                width: 40px;
                height: 40px;
                border-radius: 4px;
                object-fit: cover;
            }

            .notification-item p {
                margin: 0;
                font-size: 14px;
                color: #555;
            }

            .notification-item p strong {
                color: #333;
                font-size: 15px;
            }

            .notification-empty {
                padding: 20px;
                text-align: center;
                color: #999;
                font-size: 14px;
            }

            .status-dot {
                width: 8px;
                height: 8px;
                background-color: green;
                border-radius: 50%;
                display: inline-block;
                margin-left: 5px;
            }

            .hidden {
                display: none;
            }

            .notification-item.read {
                opacity: 0.6;
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
                        <a href="#" class="notification-icon" style="font-size: 28px;" onclick="toggleNotification(event)">
                            <i class="fas fa-bell"></i>
                            <span class="notification-badge">
                                ${sessionScope.notificationCount != null ? sessionScope.notificationCount : '0'}
                            </span>
                        </a>
                        <div class="notification-box" id="notificationBox">
                            <h4>Thông Báo</h4>
                            <c:choose>
                                <c:when test="${empty sessionScope.userNotifications}">
                                    <div class="notification-empty">
                                        Chưa có thông báo nào!
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="notification" items="${sessionScope.userNotifications}">
                                        <div class="notification-item ${notification.status eq 'Read' ? 'read' : ''}" onclick="markAsRead(this, ${notification.notificationID})">
                                            <div>
                                                <p><strong>${notification.title}</strong> 
                                                    <span class="status-dot ${notification.read ? 'hidden' : ''}"></span>
                                                </p>
                                                <p>${notification.content}</p>
                                            </div>
                                        </div>
                                    </c:forEach>

                                </c:otherwise>
                            </c:choose>
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


                                            function markAsRead(element, notificationId) {
                                                // Ẩn chấm xanh và làm mờ thông báo
                                                element.classList.add("read");
                                                let dot = element.querySelector(".status-dot");
                                                if (dot)
                                                    dot.classList.add("hidden");

                                                // Gửi AJAX cập nhật trạng thái đã đọc
                                                $.ajax({
                                                    url: "MarkNotificationReadServlet",
                                                    type: "POST",
                                                    data: {id: notificationId},
                                                    dataType: "json",
                                                    success: function (response) {
                                                        if (response.status === "success") {
                                                            console.log("Notification marked as read.");
                                                            // Cập nhật số lượng thông báo trên giao diện
                                                            let badge = document.querySelector(".notification-badge");
                                                            badge.textContent = response.unreadCount;
                                                            // Bỏ phần ẩn badge khi unreadCount === 0
                                                        } else {
                                                            console.error("Error: " + response.message);
                                                        }
                                                    },
                                                    error: function (xhr, status, error) {
                                                        console.error("AJAX Error: " + error);
                                                    }
                                                });
                                            }

        </script>

    </body>
</html>
