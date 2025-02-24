<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Detail</title>
    <!-- Include Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom Styles -->
    <link rel="stylesheet" href="CSS/style.css">
</head>
<body>
    <div class="container my-5">
        <h1 class="mb-4">Order Details</h1>

        <!-- Table to display order details -->
        <div class="table-responsive">
            <table class="table table-bordered table-striped table-hover">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Order ID</th>
                        <th>Product</th>
                        <th>Quantity</th>
                        <th>Price</th>
                        <th>Discount</th>
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

        <!-- Back button -->
        <a href="${pageContext.request.contextPath}/Admin/OrderServlet" class="btn btn-primary">Back to Orders</a>
    </div>

    <!-- Include Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
