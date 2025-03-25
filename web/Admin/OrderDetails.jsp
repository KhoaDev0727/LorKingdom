<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Chi Tiết Đơn Hàng</title>
        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- CSS Tùy Chỉnh -->
        <link rel="stylesheet" href="CSS/style.css">
    </head>
    <body>
        <div class="container my-5">
            <h1 class="mb-4">Chi Tiết Đơn Hàng</h1>
            <c:choose>
                <c:when test="${empty sessionScope.roleID || sessionScope.roleID eq 4}">
                    <c:redirect url="/Admin/loginPage.jsp"/>
                </c:when>
                <c:when test="${sessionScope.roleID eq 3}">
                    <c:redirect url="/LogoutServlet"/>
                </c:when>
            </c:choose>
            <!-- Bảng hiển thị chi tiết đơn hàng -->
            <div class="table-responsive">
                <table class="table table-bordered table-striped table-hover">
                    <thead class="table-dark">
                        <tr>
                            <th>Mã</th>
                            <th>Mã Đơn Hàng</th>
                            <th>Sản Phẩm</th>
                            <th>Số Lượng</th>
                            <th>Giá</th>
                            <th>Giảm Giá</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${listDetail}" var="detail">
                            <tr>
                                <td>${detail.orderDetailID}</td>
                                <td>${detail.orderID}</td>
                                <td>${detail.productName}</td>
                                <td>${detail.quantity}</td>
                                <td>${detail.unitPrice}</td>
                                <td>${detail.discount}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <!-- Nút quay lại -->
            <a href="${pageContext.request.contextPath}/Admin/OrderServlet" class="btn btn-primary">Quay lại danh sách đơn hàng</a>
        </div>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
