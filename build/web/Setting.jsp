<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản lý thông tin giao hàng</title>
        <link rel="stylesheet" href="assets/styleUser/Setting.css"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" />

    </head>
    <body>
        
        <%@include file="assets/Component/header.jsp" %>
        
        <div class="container-fluid">
            <!-- Sidebar -->
            <div class="sidebar ">
                <div class="profile ">
                    <div class="d-flex container profile-img ">
                        <img src="assets/img/bannerlogin.jpg" alt="User Profile" class="profile-image">
                        <span class="username">Người dùng Klook</span>
                        <a href="#">Cập nhật thông tin cá nhân</a>
                    </div>

                    <div class="klook-rewards">
                        <div class="rewards-header">
                            <span class="rewards-title">Klook Rewards</span>
                        </div>
                        <div class="rewards-content">
                            <div class="level">
                                <span class="level-text">Lv.1</span>
                                <span class="level-badge">Bạc</span>
                            </div>
                            <button class="view-benefits">Xem ưu đãi thành viên</button>
                        </div>
                    </div>
                </div>

                <!-- Navigation Menu -->
                <nav class="nav">
                    <ul>
                        <li><a href="#"><i class="fas fa-gift"></i> Mã ưu đãi </a></li>
                        <li><a href="#"><i class="fas fa-coins"></i> Klook Xu</a></li>
                        <li><a href="#"><i class="fas fa-gift"></i> Phiếu quà tặng Klook</a></li>
                        <li><a href="#"><i class="fas fa-trophy"></i> Klook Rewards</a></li>
                        <li><a href="#"><i class="fas fa-box"></i> Đơn hàng</a></li>
                        <li><a href="#"><i class="fas fa-star"></i> Đánh giá</a></li>
                        <li><a href="#"><i class="fas fa-credit-card"></i> Quản lý phương thức thanh toán</a></li>
                        <li><a href="#"><i class="fas fa-user"></i> Quản lý thông tin khách</a></li>
                        <li><a href="#"><i class="fas fa-truck"></i> Quản lý thông tin giao hàng</a></li>
                        <li><a href="#"><i class="fas fa-heart"></i> Yêu thích</a></li>
                    </ul>   
                </nav>


                <!-- Offer -->
                <div class="offer">
                    <p>Mới bạn bè: Nhận 100,000 khi bạn bè sử dụng Klook thành công!</p>
                </div>
            </div>

            <!-- Main Content -->
            <div class="main-content">
                <h1>Quản lý thông tin giao hàng</h1>
                <button class="add-address">Thêm địa chỉ</button>
                <p>Khi bạn điền địa chỉ giao hàng, bạn sẽ thấy ở đây</p>
            </div>
        </div>
        
        <%@include file="assets/Component/footer.jsp" %>
    </body>
</html>
