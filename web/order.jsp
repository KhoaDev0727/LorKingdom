<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <title>MyKingdom - Đặt Hàng</title>
        <link rel="stylesheet" href="assets/styleUser/styleOrder.css">
    </head>
    <body>
        <div class="container">
            <!-- Left content -->
            <div class="order-form">
                <div class="logo-box-order-detailts">
                    <img src="./assets/img/logo-login.png" alt="logo" class="logo-order-details">
                </div>

                <form action="OrderServlet" method="POST">
                    <div class="form-group">
                        <p class="fs-4 fw-bold">Liên hệ</p>
                        <input type="email" name="email" placeholder="Email của bạn" value="${sessionScope.email}" required>
                    </div>

                    <div class="form-group">
                        <p class="fs-4 fw-bold">Địa chỉ giao hàng</p> 
                        <label>Họ và Tên</label>
                        <input type="text" name="fullName" required>
                    </div>
                    <div class="form-group">
                        <label>Điện thoại</label>
                        <input type="tel" name="phone" required>                   
                    </div>
                    <div class="form-group">
                        <label>Địa chỉ</label>
                        <input type="text" name="address" required>            
                    </div>           
                    <div class="form-group">
                        <label>Tỉnh / Thành phố</label>
                        <input type="text" name="city" required>                   
                    </div>
                    <div class="form-group row">
                        <div class="col-6">
                            <label>Quận / Huyện</label>
                            <input type="text" name="district" required>
                        </div>
                        <div class="col-6">
                            <label>Phường / Xã</label>
                            <input type="text" name="ward" required>
                        </div>   
                    </div>

                    <div class="payment-methods row">
                        <div class="col-6">
                            <button type="submit" name="paymentMethod" value="zalopay" class="payment-button zalopay">
                                <img src="assets/img/zalopay.png" alt="ZaloPay" class="payment-icons"> Thanh toán bằng ZaloPay
                            </button>
                        </div>
                        <div class="col-6">
                            <button type="submit" name="paymentMethod" value="vnpay" class="payment-button vnpay">
                                <img src="assets/img/vnpay.png" alt="VNPay" class="payment-icon"> Thanh toán bằng VNPay
                            </button>
                        </div>     
                        <div class="col-6">
                            <button type="submit" name="paymentMethod" value="momo" class="payment-button momo">
                                <img src="assets/img/momo.png" alt="MoMo" class="payment-icon"> Thanh toán bằng MoMo
                            </button>
                        </div>
                        <div class="col-6">
                            <button type="submit" name="paymentMethod" value="cash" class="payment-button cash">
                                <img src="assets/img/cash.png" alt="Cash" class="payment-icon"> Thanh toán tiền mặt
                            </button>
                        </div>   
                    </div>
                </form>
            </div>

            <!-- Right content -->
            <div class="order-summary">
                <h3 class="title-order">Đơn hàng</h3>
                <c:forEach items="${listCart}" var="item">
                    <div class="product">
                        <img src="${item.product.mainImageUrl}" alt="${item.product.name}">
                        <div>
                            <p>${item.product.name}</p>
                            <p><fmt:formatNumber value="${item.price * item.quantity}" pattern="#,###" /> VND</p>
                        </div>
                    </div>
                </c:forEach>
                <div class="discount">
                    <input type="text" placeholder="Mã giảm giá hoặc quà tặng">
                    <button>Áp dụng</button>
                </div>
                <div class="total">
                    <p>Phí vận chuyển: <span>MIỄN PHÍ</span></p>
                    <p>Tổng ${size} mặt hàng</p>
                    <p>Tổng: <fmt:formatNumber value="${totalMoney}" pattern="#,###" /> VND</p>
                </div>
            </div>
        </div>
    </body>
</html>