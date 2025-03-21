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
        </style>
    </head>
    <body>
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
                    <div id="voucherError" class="error-message"></div>
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
            // Điền lại giá trị từ formDataJson
            window.onload = function () {
                const formDataJson = '${sessionScope.formDataJson != null ? sessionScope.formDataJson : "{}"}';
                const formData = JSON.parse(formDataJson);

                // Điền giá trị cho các input
                if (formData.email)
                    document.getElementById("email").value = formData.email;
                if (formData.fullName)
                    document.getElementById("fullName").value = formData.fullName;
                if (formData.phone)
                    document.getElementById("phone").value = formData.phone;
                if (formData.address)
                    document.getElementById("address").value = formData.address;

                // Điền giá trị cho các select (sẽ được cập nhật sau khi location.js tải xong)
                if (formData.province) {
                    setTimeout(() => {
                        document.getElementById("province").value = formData.province;
                    }, 1000); // Delay để chờ location.js tải
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
            };

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
                            const discount = response.discount;
                            const totalMoney = ${totalMoney};
                            const finalTotal = totalMoney - discount;

                            $("#discountApplied").text("Đã áp dụng giảm giá: " + formatNumber(discount) + " VND");
                            $("#finalTotal").text(formatNumber(finalTotal) + " VND");
                            voucherError.style.display = "none";
                        } else {
                            voucherError.textContent = "Mã giảm giá không tồn tại!";
                            voucherError.style.display = "block";
                        }
                    },
                    error: function () {
                        voucherError.textContent = "Có lỗi xảy ra khi áp dụng mã giảm giá!";
                        voucherError.style.display = "block";
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