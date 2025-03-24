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
        <style>
            .error-message {
                color: red;
                font-size: 0.9em;
                margin-top: 5px;
            }
            .voucher-container {
                position: relative;
                display: flex;
                align-items: center;
                margin-top: 10px;
            }
            .voucher-image {
                width: 60px;
                height: 40px;
                margin-right: 10px;
                object-fit: cover;
            }
            .voucher-applied {
                display: flex;
                align-items: center;
                background: #f8f9fa;
                padding: 8px;
                border-radius: 5px;
                position: relative;
            }
            .remove-voucher {
                position: absolute;
                right: 5px;
                top: 50%;
                transform: translateY(-50%);
                border: none;
                background: transparent;
                color: red;
                font-size: 18px;
                width: 25px !important;
                height: 80%;
                cursor: pointer;
            }
            .remove-voucher:hover {
                color: darkred;
            }
        </style>
    </head>
    <body>
        <!-- Reset discount trong session khi không có voucher được áp dụng -->
        <c:if test="${empty param.voucherCode}">
            <% session.removeAttribute("discount"); %>
        </c:if>

        <div class="container">
            <!-- Left content -->
            <div class="order-form">
                <div class="logo-box-order-detailts">
                    <img src="./assets/img/logo-login.png" alt="logo" class="logo-order-details">
                </div>

                <form action="OrderServlet" method="POST" id="orderForm">
                    <div class="form-group">
                        <p class="fs-4 fw-bold">Liên hệ</p>
                        <input type="email" name="email" id="email" placeholder="Email của bạn" 
                               value="${sessionScope.formDataJson != null ? '' : sessionScope.email}">
                        <div class="error-message">${sessionScope.validationErrors.email}</div>
                    </div>

                    <div class="form-group">
                        <p class="fs-4 fw-bold">Địa chỉ giao hàng</p> 
                        <label>Họ và Tên</label>
                        <input type="text" name="fullName" id="fullName">
                        <div class="error-message">${sessionScope.validationErrors.fullName}</div>
                    </div>
                    <div class="form-group">
                        <label>Điện thoại</label>
                        <input type="tel" name="phone" id="phone">  
                        <div class="error-message">${sessionScope.validationErrors.phone}</div>                
                    </div>
                    <div class="form-group">
                        <label>Địa chỉ</label>
                        <input type="text" name="address" id="address">  
                        <div class="error-message">${sessionScope.validationErrors.address}</div>          
                    </div>           
                    <div class="form-group">
                        <label>Tỉnh / Thành phố *</label>
                        <select name="province" id="province">
                            <option value="">Chọn tỉnh/thành phố</option>
                        </select>
                        <input type="hidden" name="provinceName" id="provinceName">
                        <div class="error-message">${sessionScope.validationErrors.province}</div>
                    </div>
                    <div class="form-group row">
                        <div class="col-6">
                            <label>Quận / Huyện *</label>
                            <select name="district" id="district">
                                <option value="">Chọn quận/huyện</option>
                            </select>
                            <input type="hidden" name="districtName" id="districtName">
                            <div class="error-message">${sessionScope.validationErrors.district}</div>
                        </div>
                        <div class="col-6">
                            <label>Phường / Xã *</label>
                            <select name="ward" id="ward">
                                <option value="">Chọn phường/xã</option>
                            </select>
                            <input type="hidden" name="wardName" id="wardName">
                            <div class="error-message">${sessionScope.validationErrors.ward}</div>
                        </div>
                    </div>

                    <div class="payment-methods row">                       
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
                            <c:if test="${sessionScope.appliedVoucher != null && sessionScope.appliedVoucher.productID == item.product.productID}">
                                <p class="product-discount">Giảm: <fmt:formatNumber value="${item.price * item.quantity * (sessionScope.appliedVoucher.discountPercent / 100)}" pattern="#,###" /> VND</p>
                            </c:if>
                        </div>
                    </div>
                </c:forEach>

                <!-- Voucher Section -->
                <div class="discount">
                    <label>Mã giảm giá:</label>
                    <div class="voucher-container">
                        <input type="text" id="voucherCode" placeholder="Nhập mã giảm giá">
                        <button onclick="applyVoucher()">Áp dụng</button>
                    </div>
                    <div id="voucherError" class="error-message"></div>

                    <!-- Hiển thị voucher đã áp dụng -->
                    <div id="voucherAppliedContainer" style="display: none;">
                        <div class="voucher-applied">
                            <img src="assets/img/vourcher.jpg" class="voucher-image" alt="Voucher">
                            <span id="voucherAppliedText"></span>
                            <button class="remove-voucher" onclick="removeVoucher()">×</button>
                        </div>
                    </div>

                    <c:if test="${not empty availableVouchers}">
                        <p>Các mã giảm giá có sẵn:</p>
                        <select id="voucherSelect" onchange="fillVoucherCode()">
                            <option value="">Chọn mã</option>
                            <c:forEach items="${availableVouchers}" var="voucher">
                                <option value="${voucher.promotionCode}" data-discount="${voucher.discountPercent}">
                                    ${voucher.name} - Giảm ${voucher.discountPercent}% (Sản phẩm ID: ${voucher.productID})
                                </option>
                            </c:forEach>
                        </select>
                    </c:if>
                    <p id="discountApplied" style="color: green;"></p>
                </div>

                <div class="total">
                    <p>Phí vận chuyển: <span>MIỄN PHÍ</span></p>
                    <p>Tổng ${size} mặt hàng</p>
                    <p>Tổng tạm tính: <fmt:formatNumber value="${totalMoney}" pattern="#,###" /> VND</p>
                    <p style="font-weight: 550;">Tổng sau giảm giá: 
                        <span id="finalTotal">
                            <fmt:formatNumber value="${totalMoney - (sessionScope.discount != null ? sessionScope.discount : 0)}" pattern="#,###" /> VND
                        </span>
                    </p>
                </div>
            </div>
        </div>

        <script>
// Điền lại giá trị từ formDataJson (không thay đổi)
            window.onload = function () {
                const formDataJson = '${sessionScope.formDataJson != null ? sessionScope.formDataJson : "{}"}';
                const formData = JSON.parse(formDataJson);

                if (formData.email)
                    document.getElementById("email").value = formData.email;
                if (formData.fullName)
                    document.getElementById("fullName").value = formData.fullName;
                if (formData.phone)
                    document.getElementById("phone").value = formData.phone;
                if (formData.address)
                    document.getElementById("address").value = formData.address;

                if (formData.province) {
                    setTimeout(() => {
                        document.getElementById("province").value = formData.province;
                    }, 1000);
                }
                if (formData.district) {
                    setTimeout(() => {
                        document.getElementById("district").value = formData.district;
                    }, 1000);
                }
                if (formData.ward) {
                    setTimeout(() => {
                        document.getElementById("ward").value = formData.ward;
                    }, 1000);
                }

                // Hiển thị voucher đã áp dụng từ session
            <c:if test="${sessionScope.discount != null}">
                $("#voucherAppliedContainer").show();
                $("#voucherAppliedText").text("Đã áp dụng: ${sessionScope.appliedVoucher.promotionCode} - Giảm ${sessionScope.discount} VND");
                $("#discountApplied").text("Đã áp dụng giảm giá: ${sessionScope.discount} VND");
                $("#finalTotal").text(formatNumber(${totalMoney - sessionScope.discount}) + " VND");
            </c:if>
            };

            let originalTotal = ${totalMoney}; // Lưu giá trị gốc
            let currentDiscount = ${sessionScope.discount != null ? sessionScope.discount : 0};

            function fillVoucherCode() {
                const select = document.getElementById("voucherSelect");
                const selectedCode = select.value;
                document.getElementById("voucherCode").value = selectedCode;
            }

            function applyVoucher() {
                const voucherCode = document.getElementById("voucherCode").value.trim();
                const voucherError = document.getElementById("voucherError");

                if (!voucherCode) {
                    voucherError.textContent = "Vui lòng nhập mã giảm giá!";
                    voucherError.style.display = "block";
                    return;
                }

                $.ajax({
                    url: "ApplyVoucherServlet",
                    type: "POST",
                    data: {voucherCode: voucherCode},
                    success: function (response) {
                        if (response.success) {
                            currentDiscount = response.discount;
                            const finalTotal = originalTotal - currentDiscount;

                            // Hiển thị voucher đã áp dụng
                            $("#voucherAppliedContainer").show();
                            $("#voucherAppliedText").text("Đã áp dụng: " + voucherCode + " - Giảm " + formatNumber(currentDiscount) + " VND");
                            $("#discountApplied").text("Đã áp dụng giảm giá: " + formatNumber(currentDiscount) + " VND");
                            $("#finalTotal").text(formatNumber(finalTotal) + " VND");
                            voucherError.style.display = "none";

                            // Reset input
                            document.getElementById("voucherCode").value = "";
                        } else {
                            voucherError.textContent = response.message || "Mã giảm giá không hợp lệ!";
                            voucherError.style.display = "block";
                        }
                    },
                    error: function () {
                        voucherError.textContent = "Có lỗi xảy ra khi áp dụng mã giảm giá!";
                        voucherError.style.display = "block";
                    }
                });
            }

            function removeVoucher() {
                $("#voucherAppliedContainer").hide();
                currentDiscount = 0;
                $("#discountApplied").text("");
                $("#finalTotal").text(formatNumber(originalTotal) + " VND");

                // Xóa discount khỏi session bằng Ajax
                $.ajax({
                    url: "ApplyVoucherServlet",
                    type: "POST",
                    data: {voucherCode: ""}, // Gửi yêu cầu trống để reset
                    success: function () {
                        console.log("Voucher removed from session");
                    }
                });
            }

            function formatNumber(num) {
                return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            }
        </script>

        <script src="https://cdnjs.cloudflare.com/ajax/libs/axios/0.26.1/axios.min.js"
                integrity="sha512-bPh3uwgU5qEMipS/VOmRqynnMXGGSRv+72H/N260MQeXZIK4PG48401Bsby9Nq5P5fz7hy5UGNmC/W1Z51h2GQ=="
        crossorigin="anonymous" referrerpolicy="no-referrer"></script>
        <script src="assets/js/location.js"></script>
    </body>
</html>