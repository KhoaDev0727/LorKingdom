<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Promotion</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

    <div class="container mt-5">
        <h2 class="text-center">Promotion List</h2>
        
                  <% String errorMessage = (String) request.getAttribute("errorMessage");
               if (errorMessage != null) { %>
                <div class="alert alert-danger" role="alert">
                    <%= errorMessage %>
                </div>
                <% } %>
        <!-- Nút Update Promotion, khi bấm vào sẽ mở modal -->
        <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#updatePromotionModal">
            <i class="fas fa-edit"></i> Update Promotion
        </button>

        <!-- Modal Update Promotion -->
        <div class="modal fade" id="updatePromotionModal" tabindex="-1" aria-labelledby="updatePromotionModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="updatePromotionModalLabel">Update Promotion</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <!-- Form update promotion -->
                        <form action="${pageContext.request.contextPath}/updatePromotion" method="POST">
                            <!-- Hidden field for promotionID -->
                            <input type="hidden" name="promotionID" value="${promotion.promotionID}">

                            <div class="mb-3">
                                <label for="productID" class="form-label">Select Product</label>
                                <select name="productID" id="productID" class="form-select" required>
                                    <c:forEach var="product" items="${productList}">
                                        <option value="${product.productID}" ${product.productID == promotion.productID ? 'selected' : ''}>
                                            ${product.productID} - ${product.name}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="mb-3">
                                <label for="name" class="form-label">Promotion Name</label>
                                <input type="text" name="name" id="name" class="form-control" value="${promotion.name}" required>
                            </div>

                            <div class="mb-3">
                                <label for="description" class="form-label">Description</label>
                                <textarea name="description" id="description" class="form-control" required>${promotion.description}</textarea>
                            </div>

                            <div class="mb-3">
                                <label for="discountPercent" class="form-label">Discount Percent</label>
                                <input type="number" name="discountPercent" id="discountPercent" class="form-control" value="${promotion.discountPercent}" step="0.01" required>
                            </div>

                            <div class="mb-3">
                                <label for="startDate" class="form-label">Start Date</label>
                                <input type="date" name="startDate" id="startDate" class="form-control" value="${promotion.startDate}" required>
                            </div>

                            <div class="mb-3">
                                <label for="endDate" class="form-label">End Date</label>
                                <input type="date" name="endDate" id="endDate" class="form-control" value="${promotion.endDate}" required>
                            </div>

                            <div class="mb-3">
                                <label for="status" class="form-label">Status</label>
                                <select name="status" id="status" class="form-control">
                                    <option value="Active" ${promotion.status == 'Active' ? 'selected' : ''}>Active</option>
                                    <option value="Expired" ${promotion.status == 'Expired' ? 'selected' : ''}>Expired</option>
                                </select>
                            </div>

                            <button type="submit" class="btn btn-success">Update</button>
                            <a href="${pageContext.request.contextPath}/promotionController" class="btn btn-secondary">Cancel</a>
                        </form>
                        
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
