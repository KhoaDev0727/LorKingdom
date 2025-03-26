<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thanh Toán Thành Công</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <h1 class="text-success">Thanh toán MoMo thành công!</h1>
        <p>${sessionScope.successMessage}</p>
        <h3>Thông tin đơn hàng</h3>
        <p><strong>Tên khách hàng:</strong> ${sessionScope.order.accountName}</p>
        <p><strong>Địa chỉ:</strong> ${sessionScope.address}</p>
        <p><strong>Số điện thoại:</strong> ${sessionScope.phone}</p>
        <p><strong>Email:</strong> ${sessionScope.email}</p>
        <p><strong>Tổng tiền:</strong> <fmt:formatNumber value="${sessionScope.order.totalAmount}" pattern="#,###" /> VND</p>
        <a href="${pageContext.request.contextPath}/getList" class="btn btn-primary">Quay lại trang chủ</a>
    </div>
</body>
</html>