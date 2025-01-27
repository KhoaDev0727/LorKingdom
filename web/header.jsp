        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>s
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"> <!-- Bootstrap Import -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
        <link rel="stylesheet" href="./assets/styleUser/styleHeader.css"/>
        <title>header page</title>
    </head>
    <body>
        <!-- Header Section -->
        <header class="header">
            <div class="header_first">
                <div class="header-left d-flex align-items-center gap-3">
                    <a href="home.jsp" class="logo">
                        <img src="./assets/img/logo.png" alt="Lor Travel Logo">
                    </a>
                    <div class="input-group search-container">
                        <span class="input-group-text bg-white border-end-0">
                            <i class="fa fa-search"></i>
                        </span>
                        <input type="text" class="form-control border-start-0" placeholder="Tìm sản phẩm">
                    </div>
                </div>

                <div class="header-right">
                    <div class="dropdown">
                        <span class="dropdown-btn">VN <i class="fas fa-caret-down"></i></span>
                        <ul class="dropdown-menu">
                            <li><a href="#" onclick="changeLanguage('vn')">VN</a></li>
                            <li><a href="#" onclick="changeLanguage('en')">EN</a></li>
                        </ul>
                    </div>
                    <div class="dropdown" style="position: relative;">
                        <span class="dropdown-btn">VND <i class="fas fa-caret-down"></i></span>
                        <ul class="dropdown-menu">
                            <li><a href="#">VND</a></li>
                            <li><a href="#">USD</a></li>
                        </ul>
                    </div>
                    <a href="aboutus.jsp" data-key="aboutUs">Giới Thiệu</a>
                    <a href="#" data-key="help">Trợ giúp</a>
                    <a href="cart.jsp" data-key="cart">Giỏ hàng</a>

                    <div class="avatar-container">
                        <div class="avatar">
                            <img src="./assets/img/profile.png" alt="logo">
                        </div>
                        <div class="menu-avatar">
                            <a href="login.jsp" class="menu-item-avatar text-dark p-3">Login</a>
                            <a href="register.jsp" class="menu-item-avatar text-dark p-3">Register</a>
                        </div>
                    </div>

                </div>

            </div>


            <nav class="navbar">
                <ul class="menu">
                    <li class="menu-item">
                        <a href="#" data-key="newArrivals">Hàng Mới</a>
                    </li>
                    <li class="menu-item dropdown">
                        <a href="#" data-key="products">Sản Phẩm</a>
                        <div class="dropdown-menu">

                            <!-- dropdown menu -->

                            <div class="dropdown-column">
                                <h4>Đồ Chơi Theo Phim</h4>
                                <ul>
                                    <li><a href="#">Siêu Anh Hùng</a></li>
                                    <li><a href="#">Siêu Robot</a></li>
                                    <li><a href="#">Siêu Thú</a></li>
                                </ul>
                            </div>

                            <div class="dropdown-column">
                                <h4>Đồ Chơi Phương Tiện</h4>
                                <ul>
                                    <li><a href="#">Xe Điều Khiển</a></li>
                                    <li><a href="#">Xe Lắp Ráp</a></li>
                                </ul>
                            </div>

                            <!-- dropdown menu -->
                        </div>
                    </li>
                    <li class="menu-item">
                        <a href="#" data-key="brands">Thương Hiệu</a>
                    </li>

                    <li class="menu-item">
                        <a href="#" data-key="promotions">Khuyến Mãi</a>
                    </li>

                    <li class="menu-item">
                        <a href="#" data-key="memberProgram">Chương Trình Thành Viên</a>
                    </li>
                    
                     <li class="menu-item">
                         <a href="PointPage.jsp" data-key="memberProgram">Lor Xu</a>
                    </li>

                </ul>
            </nav>
        </header>
        <!-- end Header Section -->
    </body>
</html>
