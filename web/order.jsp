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
            .continue-shopping a {
                text-decoration: none;
                color: #2196f3;
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


            /* Phương thức vận chuyển theo phong cách Shopee */
            .shipping-methods {
                border: 1px solid #e0e0e0;
                border-radius: 8px;
                padding: 10px;
                background: #fff;
                margin-top: 10px;
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            }

            .form-check {
                display: flex;
                align-items: center;
                padding: 8px 12px;
                border-bottom: 1px solid #f3f3f3;
                transition: background-color 0.2s ease-in-out;
                cursor: pointer;
                border-radius: 6px;
            }

            .form-check:hover {
                background-color: #fafafa;
            }

            .form-check:last-child {
                border-bottom: none;
            }

            .form-check-input {
                margin-right: 10px;
                accent-color: #ee4d2d;
                width: 18px;
                height: 18px;
            }

            .form-check-label {
                font-size: 14px;
                font-weight: 500;
                color: #222;
                flex-grow: 1;
            }

            .form-check-label small {
                display: block;
                font-size: 12px;
                color: #777;
            }

            .shipping-info {
                font-size: 13px;
                color: #ee4d2d;
                font-weight: 600;
            }

            .form-check-input:checked + .form-check-label {
                color: #ee4d2d;
            }

            .form-check .form-check-input {
                float: left;
                margin-left: 0 !important;
            }
            .payment-methods {
                margin-top: 10px;
                display: flex;
                flex-direction: column;
                gap: 10px; /* Khoảng cách giữa các thẻ */
            }

            .payment-card {
                display: flex;
                align-items: center;
                padding: 12px 15px;
                border: 1px solid #e0e0e0;
                border-radius: 8px;
                background: #fff;
                cursor: pointer;
                transition: all 0.3s ease;
                box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
            }

            .payment-card:hover {
                background: #f8f9fa;
                box-shadow: 0 3px 6px rgba(0, 0, 0, 0.1);
            }

            .payment-card.selected {
                border-color: #007bff; /* Màu viền khi được chọn */
                box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.2); /* Hiệu ứng bóng khi được chọn */
                background: #f0f7ff; /* Nền nhẹ khi được chọn */
            }

            .payment-card .payment-icon {
                width: 40px;
                height: 40px;
                margin-right: 15px;
                object-fit: contain;
            }

            .payment-card .payment-info {
                flex-grow: 1;
            }

            .payment-card .payment-info .method-name {
                font-size: 15px;
                font-weight: 500;
                color: #333;
                margin-bottom: 3px;
            }

            .payment-card .payment-info .method-description {
                font-size: 12px;
                color: #777;
            }

            /* Style cho nút Thanh toán (giữ nguyên) */
            .btn-primary {
                background-color: #ee4d2d;
                border-color: #ee4d2d;
                font-size: 16px;
                font-weight: 500;
                padding: 10px;
                border-radius: 6px;
                transition: background-color 0.2s ease-in-out;
            }

            .btn-primary:hover {
                background-color: #d43f21;
                border-color: #d43f21;
            }

            .btn-primary:disabled {
                background-color: #cccccc;
                border-color: #cccccc;
                cursor: not-allowed;
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
                <div class="continue-shopping">
                    <a href="cart.jsp" style="color: orange;">← Quay lại giỏ hàng</a>
                </div>
                <div class="logo-box-order-detailts">
                    <img src="./assets/img/logo-login.png" alt="logo" style="margin-top: 10px;" class="logo-order-details">
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

                    <div class="form-group">
                        <p class="fs-4 fw-bold">Phương thức vận chuyển</p>
                        <div class="shipping-methods" id="shippingMethodsContainer">
                            <!-- Danh sách phương thức vận chuyển sẽ được thêm động bằng JavaScript -->
                        </div>
                        <div class="error-message">${sessionScope.validationErrors.shippingMethod}</div>
                    </div>

                    <div class="form-group">
                        <p class="fs-4 fw-bold">Phương thức thanh toán</p>
                        <div class="payment-methods" id="paymentMethodsContainer">
                            <!-- Danh sách phương thức thanh toán sẽ được thêm động bằng JavaScript -->
                        </div>
                        <input type="hidden" name="paymentMethod" id="selectedPaymentMethod">
                        <div class="error-message">${sessionScope.validationErrors.paymentMethod}</div>
                    </div>

                    <!-- Nút Thanh toán -->
                    <div class="form-group mt-3">
                        <button type="button" id="submitOrderBtn" class="btn btn-primary w-100">Thanh toán</button>
                    </div>
                </form>
            </div>

            <!-- Right content -->
            <div class="order-summary">
                <h3 class="title-order">Đơn hàng</h3>
                <c:forEach items="${listCart}" var="item">
                    <div class="product">
                        <img src="${pageContext.request.contextPath}/${item.product.mainImageUrl}" alt="${item.product.name}">
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

            function loadShippingMethods() {
                $.ajax({
                    url: "GetShippingMethodsServlet",
                    type: "GET",
                    dataType: "json",
                    success: function (data) {
                        console.log("Dữ liệu từ server (chi tiết):", JSON.stringify(data));
                        const container = $("#shippingMethodsContainer");
                        container.empty();

                        if (!Array.isArray(data) || data.length === 0) {
                            container.append("<p>Không có phương thức vận chuyển nào khả dụng.</p>");
                            return;
                        }

                        data.forEach((method, index) => {
                            if (typeof method !== "object" || method === null) {
                                console.error("Phương thức không hợp lệ tại index", index, ":", method);
                                return;
                            }

                            const methodName = method.name || "Không có tên";
                            const methodDescription = method.description || "Không có mô tả";
                            const methodId = method.id || index;
                            const isChecked = index === 0 ? "checked" : "";

                            const $div = $("<div>").addClass("form-check");
                            const $input = $("<input>")
                                    .addClass("form-check-input")
                                    .attr("type", "radio")
                                    .attr("name", "shippingMethod")
                                    .attr("id", `shipping_${methodId}`)
                                    .attr("value", methodId)
                                    .prop("checked", isChecked === "checked");
                            const $label = $("<label>")
                                    .addClass("form-check-label")
                                    .attr("for", `shipping_${methodId}`)
                                    .text(methodName);
                            const $small = $("<small>").text(methodDescription);

                            $label.append("<br>", $small);
                            $div.append($input, $label);
                            container.append($div);
                        });
                    },
                    error: function (xhr, status, error) {
                        console.error("Lỗi Ajax:", status, error);
                        $("#shippingMethodsContainer").html("<p>Có lỗi khi tải phương thức vận chuyển.</p>");
                    }
                });
            }

            $(document).ready(function () {
                loadShippingMethods(); // Gọi hàm khi trang đã sẵn sàng
            });
        </script>
        <script>
            function loadPaymentMethods() {
                $.ajax({
                    url: "GetPaymentMethodsServlet",
                    type: "GET",
                    dataType: "json",
                    success: function (data) {
                        console.log("Dữ liệu phương thức thanh toán từ server:", JSON.stringify(data));
                        const container = $("#paymentMethodsContainer");
                        container.empty();

                        if (!Array.isArray(data) || data.length === 0) {
                            container.append("<p>Không có phương thức thanh toán nào khả dụng.</p>");
                            return;
                        }

                        data.forEach((method, index) => {
                            if (typeof method !== "object" || method === null) {
                                console.error("Phương thức không hợp lệ tại index", index, ":", method);
                                return;
                            }

                            const methodName = method.name || "Không có tên";
                            const methodDescription = method.description || "Không có mô tả";
                            const methodId = method.id || index;

                            // Normalize methodValue để khớp với OrderServlet
                            let methodValue;
                            switch (methodName) {
                                case "Tiền mặt khi nhận hàng":
                                    methodValue = "cash";
                                    break;
                                case "Ví điện tử MOMO":
                                    methodValue = "momo";
                                    break;
                                case "Ví điện tử VNPAY":
                                    methodValue = "vnpay";
                                    break;
                                default:
                                    methodValue = methodName.toLowerCase().replace(/\s/g, '');
                            }

                            // Xác định đường dẫn hình ảnh dựa trên methodValue
                            let iconPath = "assets/img/cash.png";
                            if (methodValue === "vnpay") {
                                iconPath = "assets/img/vnpay.png";
                            } else if (methodValue === "momo") {
                                iconPath = "assets/img/momo.png";
                            }

                            const $card = $("<div>")
                                    .addClass("payment-card")
                                    .attr("data-value", methodValue)
                                    .attr("data-id", methodId);

                            const $icon = $("<img>")
                                    .addClass("payment-icon")
                                    .attr("src", iconPath)
                                    .attr("alt", methodName);

                            const $info = $("<div>").addClass("payment-info");
                            const $name = $("<div>")
                                    .addClass("method-name")
                                    .text(methodName);
                            const $description = $("<div>")
                                    .addClass("method-description")
                                    .text(methodDescription);

                            $info.append($name, $description);
                            $card.append($icon, $info);
                            container.append($card);

                            // Nếu là phương thức đầu tiên, chọn mặc định
                            if (index === 0) {
                                $card.addClass("selected");
                                $("#selectedPaymentMethod").val(methodValue);
                            }
                        });

                        // Thêm sự kiện click cho các thẻ
                        $(".payment-card").on("click", function () {
                            $(".payment-card").removeClass("selected");
                            $(this).addClass("selected");
                            const selectedValue = $(this).attr("data-value");
                            $("#selectedPaymentMethod").val(selectedValue);
                        });
                    },
                    error: function (xhr, status, error) {
                        console.error("Lỗi Ajax:", status, error);
                        $("#paymentMethodsContainer").html("<p>Có lỗi khi tải phương thức thanh toán.</p>");
                    }
                });
            }

            $(document).ready(function () {
                loadPaymentMethods();

                // Xử lý sự kiện khi nhấn nút Thanh toán
                $("#submitOrderBtn").on("click", function () {
                    const paymentMethod = $("#selectedPaymentMethod").val();
                    const formData = $("#orderForm").serialize();

                    if (!paymentMethod) {
                        alert("Vui lòng chọn phương thức thanh toán!");
                        return;
                    }

                    // Gửi form đến OrderServlet mà không kiểm tra lỗi ở đây
                    $("#orderForm").attr("action", "OrderServlet");
                    $("#orderForm").submit();
                });
            });
        </script>

        <script src="https://cdnjs.cloudflare.com/ajax/libs/axios/0.26.1/axios.min.js"
                integrity="sha512-bPh3uwgU5qEMipS/VOmRqynnMXGGSRv+72H/N260MQeXZIK4PG48401Bsby9Nq5P5fz7hy5UGNmC/W1Z51h2GQ=="
        crossorigin="anonymous" referrerpolicy="no-referrer"></script>
        <script src="assets/js/location.js"></script>
    </body>
</html>