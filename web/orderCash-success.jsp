<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <title>Chi Tiết Sản Phẩm</title>
    <style>
        html, body {
            scroll-behavior: auto !important;
        }
        .product-image {
            height: 600px;
            object-fit: cover;
        }
        .thumbnail-image {
            height: 150px;
            object-fit: cover;
        }
        .product-card {
            transition: transform 0.2s;
            height: 450px;
        }
        .product-card:hover {
            transform: scale(1.05);
        }
        .related-product-card {
            transition: transform 0.2s;
        }
        .related-product-card:hover {
            transform: scale(1.05);
        }
        .related-image {
            height: 200px;
            object-fit: cover;
        }
        .price-text {
            color: #fd7e14;
            font-weight: bold;
        }
        .btn-quantity {
            background: none;
            border: none;
        }
    </style>
</head>
<body>
    <section class="p-0">
        <div class="container-fluid px-0">
            <jsp:include page="assets/Component/header.jsp"/>
            
            <main class="container" style="margin-top: 100px;"> 
                
                <c:if test="${not empty product}">
                    <!-- Product title -->
                    <div class="row mt-4 mb-4">
                        <div class="col-12">
                            <h1 class="h4 font-weight-bold">Chi Tiết Sản Phẩm</h1>
                        </div>
                    </div>
                    
                    <!-- Product detail section -->
                    <div class="row">
                        <!-- Product images -->
                        <div class="col-lg-8 mb-4">
                            <!-- Main image -->
                            <div class="card mb-4 border">
                                <c:forEach var="img" items="${mainImages}">
                                    <c:if test="${img.productID == product.productID}">
                                        <img 
                                            src="${pageContext.request.contextPath}/${img.imageUrl}"
                                            alt="Ảnh sản phẩm chính"
                                            class="card-img-top product-image rounded shadow"/>
                                    </c:if>
                                </c:forEach>
                            </div>

                            <!-- Image carousel -->
                            <div id="carouselExampleControls" class="carousel slide" data-ride="carousel">
                                <div class="carousel-inner">
                                    <c:forEach var="img" items="${listImages}" varStatus="status">
                                        <c:if test="${status.index % 4 == 0}">
                                            <div class="carousel-item <c:if test='${status.index == 0}'>active</c:if>">
                                                <div class="row">
                                        </c:if>
                                                <div class="col-3">
                                                    <img 
                                                        src="${pageContext.request.contextPath}/${img.imageUrl}"
                                                        class="img-fluid thumbnail-image"
                                                        alt="Ảnh phụ" />
                                                </div>
                                        <c:if test="${status.index % 4 == 3 || status.last}">
                                                </div>
                                            </div>
                                        </c:if>
                                    </c:forEach>
                                </div>
                                <a class="carousel-control-prev" href="#carouselExampleControls" role="button" data-slide="prev">
                                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                                    <span class="sr-only">Previous</span>
                                </a>
                                <a class="carousel-control-next" href="#carouselExampleControls" role="button" data-slide="next">
                                    <span class="carousel-control-next-icon" aria-hidden="true"></span>
                                    <span class="sr-only">Next</span>
                                </a>
                            </div>
                        </div>

                        <!-- Product information -->
                        <div class="col-lg-4">
                            <div class="card border mb-4">
                                <div class="card-body">
                                    <h1 class="h4 font-weight-bold mb-2">${product.name}</h1>
                                    <p class="text-muted small">Mã sản phẩm: ${product.SKU}</p>
                                    <div class="h4 text-danger font-weight-bold mb-3">
                                        <fmt:formatNumber value="${product.price}" pattern="#,###" /> VND
                                    </div>

                                    <ul class="list-unstyled">
                                        <li class="d-flex align-items-start mb-2">
                                            <i class="fas fa-check-circle text-success mt-1 mr-2"></i>
                                            <span>Hàng chính hãng</span>
                                        </li>
                                        <li class="d-flex align-items-start mb-2">
                                            <i class="fas fa-check-circle text-success mt-1 mr-2"></i>
                                            <span>Miễn phí giao hàng toàn quốc đơn trên 500k</span>
                                        </li>
                                        <li class="d-flex align-items-start mb-2">
                                            <i class="fas fa-check-circle text-success mt-1 mr-2"></i>
                                            <span>Giao hàng hỏa tốc 4 tiếng</span>
                                        </li>
                                    </ul>
                                    
                                    <div class="mt-4">
                                        <form action="${pageContext.request.contextPath}/CartManagementServlet" 
                                            method="POST"
                                            class="d-flex align-items-center">

                                            <!-- Hidden fields -->
                                            <input type="hidden" name="productID" value="${product.productID}">
                                            <input type="hidden" name="price" value="${product.price}">

                                            <!-- Quantity inputs -->
                                            <label for="quantity" class="font-weight-medium mr-2">Số lượng:</label>

                                            <div class="input-group mr-3" style="width: 120px;">
                                                <div class="input-group-prepend">
                                                    <button class="btn btn-outline-secondary btn-quantity" 
                                                            type="button" 
                                                            onclick="decrementQuantity()">
                                                        <i class="fas fa-minus"></i>
                                                    </button>
                                                </div>
                                                <input id="quantity" 
                                                    name="quantity" 
                                                    type="number" 
                                                    class="form-control text-center" 
                                                    value="1" 
                                                    min="1"/>
                                                <div class="input-group-append">
                                                    <button class="btn btn-outline-secondary btn-quantity" 
                                                            type="button" 
                                                            onclick="incrementQuantity()">
                                                        <i class="fas fa-plus"></i>
                                                    </button>
                                                </div>
                                            </div>

                                            <!-- Add to cart button -->
                                            <button type="submit" class="btn btn-danger">
                                                <i class="fas fa-shopping-cart mr-1"></i> Thêm Vào Giỏ Hàng
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Product specifications -->
                            <div class="card border">
                                <div class="card-header bg-white">
                                    <h2 class="h5 font-weight-bold mb-0">Thông tin sản phẩm</h2>
                                </div>
                                <div class="card-body p-0">
                                    <table class="table table-bordered mb-0">
                                        <tbody>
                                            <tr>
                                                <td class="font-weight-bold" style="width: 40%">Thể Loại</td>
                                                <td>${category}</td>
                                            </tr>
                                            <tr>
                                                <td class="font-weight-bold">Xuất xứ</td>
                                                <td>${origin}</td>
                                            </tr>
                                            <tr>
                                                <td class="font-weight-bold">Chất liệu</td>
                                                <td>${material}</td>
                                            </tr>
                                            <tr>
                                                <td class="font-weight-bold">Tuổi</td>
                                                <td>${age}</td>
                                            </tr>
                                            <tr>
                                                <td class="font-weight-bold">Thương hiệu</td>
                                                <td>${brand}</td>
                                            </tr>
                                            <tr>
                                                <td class="font-weight-bold">Giới tính</td>
                                                <td>${sex}</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>
                
                <!-- Product description -->
                <div class="card border mb-5">
                    <div class="card-header bg-white">
                        <h2 class="h5 font-weight-bold mb-0">Mô tả sản phẩm</h2>
                    </div>
                    <div class="card-body">
                        <p class="text-secondary">${product.description}</p>
                    </div>
                </div>
                
                <!-- Reviews section -->
                <jsp:include page="reviews.jsp" />
                
                <!-- Other products -->
                <div class="row mb-5">
                    <c:forEach var="product" items="${listP}">
                        <div class="col-md-4 mb-4">
                            <a href="ProductDetailServlet?productID=${product.productID}"
                            class="card product-card border text-decoration-none h-100">
                                <div class="card-img-top overflow-hidden" style="height: 200px;">
                                    <c:forEach var="img" items="${mainImages}">
                                        <c:if test="${img.productID == product.productID}">
                                            <img src="${pageContext.request.contextPath}/${img.imageUrl}" 
                                                alt="Main Image"
                                                class="img-fluid w-100 h-100 object-fit-cover" />
                                        </c:if>
                                    </c:forEach>
                                </div>
                                <div class="card-body">
                                    <h4 class="card-title text-secondary h6">${product.name}</h4>
                                    <div class="d-flex justify-content-between">
                                        <p class="text-muted small mb-3">SKU: ${product.SKU}</p>
                                        <p class="text-muted small mb-2">
                                            <c:forEach var="cat" items="${categories}">
                                                <c:if test="${cat.categoryID == product.categoryID}">
                                                    ${cat.name}
                                                </c:if>
                                            </c:forEach>
                                        </p>
                                    </div>
                                    <p class="price-text h5">
                                        <fmt:formatNumber value="${product.price}" pattern="#,###" /> VND
                                    </p>
                                </div>
                            </a>
                        </div>
                    </c:forEach>
                </div>
                
                <!-- Related products -->
                <div class="mb-5">
                    <h2 class="h4 font-weight-bold mb-4">Sản phẩm liên quan</h2>
                    <div class="row">
                        <c:forEach var="rp" items="${relatedProducts}">
                            <div class="col-md-3 mb-4">
                                <a href="ProductDetailServlet?productID=${rp.productID}"
                                   class="card related-product-card border text-decoration-none h-100">
                                    <div class="card-img-top">
                                        <c:set var="foundImage" value="false" />
                                        <c:forEach var="img" items="${mainImages}">
                                            <c:if test="${img.productID == rp.productID}">
                                                <img src="${pageContext.request.contextPath}/${img.imageUrl}"
                                                     alt="Ảnh liên quan"
                                                     class="img-fluid related-image w-100" />
                                                <c:set var="foundImage" value="true" />
                                            </c:if>
                                        </c:forEach>

                                        <c:if test="${not foundImage}">
                                            <img src="${pageContext.request.contextPath}/assets/img/noimage.png"
                                                 alt="Ảnh liên quan"
                                                 class="img-fluid related-image w-100" />
                                        </c:if>
                                    </div>
                                    <div class="card-body">
                                        <h4 class="text-secondary h6 mb-2">${rp.name}</h4>
                                        <p class="price-text mb-0">
                                            <fmt:formatNumber value="${rp.price}" pattern="#,###" /> VND
                                        </p>
                                    </div>
                                </a>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </main>

            <!-- Footer -->
            <footer class="bg-light border-top mt-5">
                <div class="container py-4">
                    <p class="text-muted small mb-0">© 2023 - Cửa Hàng Đồ Chơi</p>
                </div>
            </footer>
        </div>
    </section>

    <!-- Bootstrap JS and dependencies -->
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

    <script>
        function incrementQuantity() {
            const quantityInput = document.getElementById('quantity');
            let currentValue = parseInt(quantityInput.value) || 1;
            quantityInput.value = currentValue + 1;
        }

        function decrementQuantity() {
            const quantityInput = document.getElementById('quantity');
            let currentValue = parseInt(quantityInput.value) || 1;
            if (currentValue > 1) {
                quantityInput.value = currentValue - 1;
            }
        }
    </script>
</body>
</html>