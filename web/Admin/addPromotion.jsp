<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Thêm Khuyến Mãi</title>
        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body class="bg-light">
        <div class="container mt-5">
            <div class="card shadow p-4">
                <h2 class="text-center text-primary mb-4">ADD PROMOTION</h2>

                <%-- Hiển thị thông báo lỗi (nếu có) --%>
                <% String errorMessage = (String) request.getAttribute("errorMessage");
               if (errorMessage != null) { %>
                <div class="alert alert-danger" role="alert">
                    <%= errorMessage %>
                </div>
                <% } %>
 
                <form action="addPromotion" method="post">
                    <div class="mb-3">
                        <label for="productID" class="form-label">Select Product</label>
                        <select name="productID" id="productID" class="form-select" required>
                            <option value="">-- Select Product --</option>
                            <c:forEach var="product" items="${productList}">
                                <option value="${product.productID}">${product.productID} - ${product.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="name" class="form-label">Promotion Name</label>
                        <input type="text" name="name" class="form-control" required>
                    </div>

                    <div class="mb-3">
                        <label for="description" class="form-label">Description</label>
                        <textarea name="description" class="form-control" required></textarea>
                    </div>

                    <div class="mb-3">
                        <label for="discountPercent" class="form-label">Discount Percent</label>
                        <input type="number" step="0.01" name="discountPercent" class="form-control" required>
                    </div>

                    <div class="mb-3">
                        <label for="startDate" class="form-label">Start Date</label>
                        <input type="date" name="startDate" class="form-control" required>
                    </div>

                    <div class="mb-3">
                        <label for="endDate" class="form-label">End Date</label>
                        <input type="date" name="endDate" class="form-control" required>
                    </div>

                    <div class="mb-3">
                        <label for="status" class="form-label">Status</label>
                        <select name="status" class="form-select">
                            <option value="Active">Active</option>
                            <option value="Expired">Expired</option>
                        </select>
                    </div>

                    <div class="d-grid">
                        <button type="submit" class="btn btn-success">Add Promotion</button>
                    </div>

                    <a href="${pageContext.request.contextPath}/promotionController" class="btn btn-outline-danger ms-2">
                        <i class="fas fa-sync"></i> Back
                    </a>
                </form>
            </div>
        </div>

        <!-- Bootstrap JS (Optional) -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
