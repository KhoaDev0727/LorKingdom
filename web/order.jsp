<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
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
                        <select name="province" id="province">
                        </select>
                    </div>
                    <div class="form-group row">
                        <div class="col-6">
                            <label>Quận / Huyện</label>
                            <select name="district" id="district">
                                <option value="">chọn quận</option>
                            </select>
                        </div>
                        <div class="col-6">
                            <label>Phường / Xã</label>
                            <select name="ward" id="ward">
                                <option value="">chọn phường</option>
                            </select>
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

                <!-- Voucher Section -->
                <div class="discount">
                    <label>Mã giảm giá:</label>
                    <input type="text" id="voucherCode" placeholder="Nhập mã giảm giá">
                    <button onclick="applyVoucher()">Áp dụng</button>
                    <c:if test="${not empty availableVouchers}">
                        <p>Các mã giảm giá có sẵn:</p>
                        <select id="voucherSelect" onchange="fillVoucherCode()">
                            <option value="">Chọn mã</option>
                            <c:forEach items="${availableVouchers}" var="voucher">
                                <option value="${voucher.promotionCode}" data-discount="${voucher.discountPercent}">
                                    ${voucher.name} - Giảm ${voucher.discountPercent}%
                                </option>
                            </c:forEach>
                        </select>
                    </c:if>
                    <p id="discountApplied" style="color: green;">
                        <c:if test="${not empty discount && discount > 0}">
                            Đã áp dụng giảm giá: <fmt:formatNumber value="${discount}" pattern="#,###" /> VND
                        </c:if>
                    </p>
                </div>

                <div class="total">
                    <p>Phí vận chuyển: <span>MIỄN PHÍ</span></p>
                    <p>Tổng ${size} mặt hàng</p>
                    <p>Tổng tạm tính: <fmt:formatNumber value="${totalMoney}" pattern="#,###" /> VND</p>
                    <p style="font-weight: 550;">Tổng sau giảm giá: 
                        <span id="finalTotal">
                            <fmt:formatNumber value="${totalMoney - (discount != null ? discount : 0)}" pattern="#,###" /> VND
                        </span>
                    </p>
                </div>
            </div>

        </div>

        <script>
            function fillVoucherCode() {
                const select = document.getElementById("voucherSelect");
                const selectedCode = select.value;
                document.getElementById("voucherCode").value = selectedCode;
            }

            function applyVoucher() {
                const voucherCode = document.getElementById("voucherCode").value;
                if (!voucherCode) {
                    alert("Vui lòng nhập mã giảm giá!");
                    return;
                }

                $.ajax({
                    url: "ApplyVoucherServlet",
                    type: "POST",
                    data: {voucherCode: voucherCode},
                    success: function (response) {
                        if (response.success) {
                            const discount = response.discount;
                            const totalMoney = ${totalMoney};
                            const finalTotal = totalMoney - discount;

                            $("#discountApplied").text("Đã áp dụng giảm giá: " + formatNumber(discount) + " VND");
                            $("#finalTotal").text(formatNumber(finalTotal) + " VND");
                        } else {
                            alert(response.message || "Mã giảm giá không hợp lệ!");
                        }
                    },
                    error: function () {
                        alert("Có lỗi xảy ra khi áp dụng mã giảm giá!");
                    }
                });
            }

            function formatNumber(num) {
                return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            }
        </script>
        
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"
                integrity="sha512-894YE6QWD5I59HgZOGReFYm4dnWc1Qt5NtvYSaNcOP+u1T9qYdvdihz0PPSiiqn/+/3e7Jo4EaG7TubfWGUrMQ=="
        crossorigin="anonymous" referrerpolicy="no-referrer"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/axios/0.26.1/axios.min.js"
                integrity="sha512-bPh3uwgU5qEMipS/VOmRqynnMXGGSRv+72H/N260MQeXZIK4PG48401Bsby9Nq5P5fz7hy5UGNmC/W1Z51h2GQ=="
        crossorigin="anonymous" referrerpolicy="no-referrer"></script>
        <script src="assets/js/location.js"></script>
    </body>
</html>