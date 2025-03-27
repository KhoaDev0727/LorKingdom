<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thanh Toán Thành Công</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f5f5f5;
            font-family: Arial, sans-serif;
        }
        .payment-success {
            max-width: 500px;
            margin: 50px auto;
            padding: 30px;
            background: white;
            border-radius: 10px;
            text-align: center;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            border-top: 5px solid #a50064;
        }
        .payment-success h1 {
            color: #a50064;
            font-weight: bold;
        }
        .payment-success .checkmark {
            font-size: 60px;
            color: #a50064;
        }
        .order-info {
            text-align: left;
            margin-top: 20px;
        }
        .order-info p {
            font-size: 16px;
            margin-bottom: 10px;
        }
        .btn-momo {
            background-color: #a50064;
            color: white;
            border: none;
            padding: 10px 20px;
            font-size: 18px;
            border-radius: 5px;
        }
        .btn-momo:hover {
            background-color: #8c004e;
        }
    </style>
</head>
<body>
    <div class="payment-success">
        <div class="checkmark">✔</div>
        <h1>Thanh toán MoMo thành công!</h1>
        <p class="text-muted">Giao dịch của bạn đã được xử lý thành công.</p>
        <div class="order-info">
<!--            <p><strong>Tên khách hàng:</strong> ${sessionScope.order.accountName}</p>-->
            <p><strong>Địa chỉ:</strong> ${sessionScope.address}</p>
            <p><strong>Số điện thoại:</strong> ${sessionScope.phone}</p>
            <p><strong>Email:</strong> ${sessionScope.email}</p>
<!--            <p><strong>Tổng tiền:</strong> <fmt:formatNumber value="${sessionScope.order.totalAmount}" pattern="#,###" /> VND</p>-->
        </div>
        <a href="${pageContext.request.contextPath}/getList" class="btn btn-momo mt-3">Quay lại trang chủ</a>
    </div>
</body>
</html>