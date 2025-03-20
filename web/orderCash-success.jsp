<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MyKingdom - Đặt Hàng Thành Công</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome for icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f5f5f5;
            color: #333;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }
        .success-header {
            text-align: center;
            padding: 20px 0;
            background: linear-gradient(90deg, #ff5e62, #f09819);
            color: white;
            border-radius: 10px 10px 0 0;
        }
        .success-header h1 {
            font-size: 28px;
            margin: 0;
        }
        .order-details, .customer-info {
            background: white;
            padding: 20px;
            border-radius: 0 0 10px 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            margin-bottom: 20px;
        }
        .order-details h3, .customer-info h3 {
            font-size: 20px;
            color: #ff5e62;
            border-bottom: 2px solid #ff5e62;
            padding-bottom: 10px;
            margin-bottom: 20px;
        }
        .product-item {
            display: flex;
            align-items: center;
            padding: 10px 0;
            border-bottom: 1px solid #eee;
        }
        .product-item img {
            width: 80px;
            height: 80px;
            object-fit: cover;
            margin-right: 15px;
            border-radius: 5px;
        }
        .product-info {
            flex-grow: 1;
        }
        .product-info p {
            margin: 0;
            font-size: 14px;
        }
        .total-amount {
            font-size: 18px;
            font-weight: bold;
            color: #ff5e62;
            text-align: right;
            margin-top: 20px;
        }
        .customer-info p {
            margin: 5px 0;
            font-size: 16px;
        }
        .btn-continue {
            display: block;
            width: 200px;
            margin: 20px auto;
            padding: 10px;
            background: #ff5e62;
            color: white;
            text-align: center;
            text-decoration: none;
            border-radius: 5px;
            font-size: 16px;
            transition: background 0.3s;
        }
        .btn-continue:hover {
            background: #f09819;
        }
        .info-label {
            font-weight: bold;
            color: #555;
        }
    </style>
</head>
<body>
    <div class="container">
        <!-- Header thông báo thành công -->
        <div class="success-header">
            <h1><i class="fas fa-check-circle"></i> Đặt Hàng Thành Công!</h1>
            <p>Cảm ơn bạn đã mua sắm tại LorKingdom. Đơn hàng của bạn đã được ghi nhận.</p>
        </div>

        <!-- Thông tin khách hàng -->
        <div class="customer-info">
            <h3>Thông Tin Giao Hàng</h3>
            <p><span class="info-label">Họ và Tên:</span> ${sessionScope.order.accountName}</p>
            <p><span class="info-label">Số Điện Thoại:</span> ${sessionScope.phone}</p>
            <p><span class="info-label">Email:</span> ${sessionScope.email}</p>
            <p><span class="info-label">Địa Chỉ:</span> ${sessionScope.address}</p>
            <p><span class="info-label">Phương Thức Thanh Toán:</span> ${sessionScope.order.payMentMethodName}</p>
            <p><span class="info-label">Phương Thức Giao Hàng:</span> ${sessionScope.order.shipingMethodName}</p>
        </div>

        <!-- Chi tiết đơn hàng -->
        <div class="order-details">
            <h3>Chi Tiết Đơn Hàng</h3>
            <c:forEach items="${sessionScope.order.orderDetails}" var="detail">
                <div class="product-item">
                    <img src="${detail.productImage}" alt="${detail.productName}">
                    <div class="product-info">
                        <p><strong>${detail.productName}</strong></p>
                        <p>Số lượng: ${detail.quantity}</p>
                        <p>Đơn giá: <fmt:formatNumber value="${detail.unitPrice}" pattern="#,###" /> VND</p>
                        <c:if test="${detail.discount > 0}">
                            <p>Giảm giá: ${detail.discount}%</p>
                        </c:if>
                    </div>
                </div>
            </c:forEach>
            <div class="total-amount">
                Tổng Tiền: <fmt:formatNumber value="${sessionScope.order.totalAmount}" pattern="#,###" /> VND
            </div>
        </div>

        <!-- Nút tiếp tục mua sắm -->
        <a href="home.jsp" class="btn-continue">Tiếp Tục Mua Sắm</a>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>