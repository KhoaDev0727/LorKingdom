<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Update Product</title>

        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.quilljs.com/1.3.6/quill.snow.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="CSS/NewProductCss.css"/>
    </head>
    <body>
        <c:choose>
            <c:when test="${empty sessionScope.roleID ||sessionScope.roleID eq 2 }">
                <c:redirect url="/Admin/loginPage.jsp"/>
            </c:when>
            <c:when test="${sessionScope.roleID eq 3}">
                <c:redirect url="/LogoutServlet"/>
            </c:when>
        </c:choose>
        <div class="container">
            <form action="updateProductServlet" method="POST" enctype="multipart/form-data" class="needs-validation" novalidate>
                <!-- Ẩn action và productID -->
                <input name="action" value="update" type="hidden" />
                <input type="hidden" name="productID" value="${product.productID}" />
                <input type="hidden" name="SKU" value="${product.SKU}" />
                <div class="row">
                    <h2 class="text-center mb-4 fw-bold text-primary">Cập Nhật Sản Phẩm</h2>
                    <!-- Cột trái -->
                    <div class="col-md-6">

                        <!-- Basic Information Section -->
                        <div class="form-section">
                            <h5 class="section-title">
                                <i class="fas fa-info-circle"></i>
                                Thông Tin Cơ Bản
                            </h5>
                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label class="form-label">Tên Sản Phẩm</label>
                                    <input type="text" class="form-control" 
                                           name="productName" 
                                           value="${product.name}" 
                                           required 
                                           oninvalid="this.setCustomValidity('Please enter the product name.')"
                                           oninput="this.setCustomValidity('')" />
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Danh mục</label>
                                    <select class="form-select" name="category" required>
                                        <c:forEach items="${Categories}" var="cat">
                                            <option value="${cat.categoryID}"
                                                    <c:if test="${cat.categoryID == product.categoryID}">selected</c:if>>
                                                ${cat.name}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="col-md-4">
                                    <label class="form-label">Giới Tính</label>
                                    <select class="form-select" name="gender">
                                        <c:forEach items="${sexes}" var="sex">
                                            <option value="${sex.sexID}"
                                                    <c:if test="${sex.sexID == product.sexID}">selected</c:if>>
                                                ${sex.name}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="col-md-4">
                                    <label class="form-label">Khoảng Giá</label>
                                    <select class="form-select" name="priceRange">
                                        <c:forEach items="${priceRanges}" var="pr">
                                            <option value="${pr.priceRangeID}"
                                                    <c:if test="${pr.priceRangeID == product.priceRangeID}">selected</c:if>>
                                                ${pr.priceRange}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="col-md-4">
                                    <label class="form-label">Thương Hiệu</label>
                                    <select class="form-select" name="brand">
                                        <c:forEach items="${brands}" var="brand">
                                            <option value="${brand.brandID}"
                                                    <c:if test="${brand.brandID == product.brandID}">selected</c:if>>
                                                ${brand.name}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="col-md-4">
                                    <label class="form-label">Khoảng Tuổi</label>
                                    <select class="form-select" name="ageGroup">
                                        <c:forEach items="${ages}" var="age">
                                            <option value="${age.ageID}"
                                                    <c:if test="${age.ageID == product.ageID}">selected</c:if>>
                                                ${age.ageRange}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="col-md-4">
                                    <label class="form-label">Chất Liệu</label>
                                    <select class="form-select" name="material">
                                        <c:forEach items="${materials}" var="material">
                                            <option value="${material.materialID}"
                                                    <c:if test="${material.materialID == product.materialID}">selected</c:if>>
                                                ${material.name}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label">Nguồn Gốc</label>
                                    <select class="form-select" name="origin" required>
                                        <!-- Placeholder, có thể disabled nếu muốn -->
                                        <option value="" disabled>Select Origin</option>

                                        <c:forEach items="${listOrigin}" var="origin" varStatus="st">
                                            <option value="${origin.originID}"
                                                    <c:if test="${st.first}">selected</c:if>>
                                                ${origin.name}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>



                            </div>
                        </div>

                        <!-- Description Section -->
                        <div class="form-section">
                            <h5 class="section-title">
                                <i class="fas fa-align-left"></i>
                                Miêu Tả Sản Phẩm
                            </h5>
                            <!-- Quill editor container -->
                            <div id="editor-container"></div>
                            <!-- Hidden field to store description -->
                            <input type="hidden" name="description" id="descriptionInput" />

                        </div>
                    </div>

                    <!-- Cột phải -->
                    <div class="col-md-6">

                        <!-- Pricing Section -->
                        <div class="form-section">
                            <h5 class="section-title">
                                <i class="fas fa-dollar-sign"></i>
                                Giá cả & Số Lượng Sản Phẩm
                            </h5>
                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label class="form-label">Giá</label>
                                    <div class="input-group">
                                        <span class="input-group-text"><i class="fas fa-tag"></i></span>
                                        <input type="text" class="form-control" name="price"
                                               value="${priceTruncated}" required />


                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Số Lượng</label>
                                    <div class="input-group">
                                        <span class="input-group-text"><i class="fas fa-cubes"></i></span>
                                        <input type="number" class="form-control" min="0" 
                                               name="stockQuantity" 
                                               value="${product.quantity}" 
                                               required />
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Images Section -->
                        <div class="form-section">
                            <h5 class="section-title">
                                <i class="fas fa-images"></i>
                                Ảnh Sản Phẩm
                            </h5>
                            <div class="row g-4">
                                <div class="row">
                                    <!-- Main Image Column -->
                                    <div class="col-md-4">
                                        <h6 class="fw-bold mb-3">Ảnh Chính</h6>
                                        <div class="upload-container d-flex align-items-center">
                                            <div class="upload-box" onclick="document.getElementById('mainImageUpload').click()">
                                                <input type="file" id="mainImageUpload" name="mainImageUpload" hidden accept="image/*">
                                                <i class="fas fa-cloud-upload-alt"></i>
                                                <span>Chọn Để Cập Nhật</span>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- Preview Image Column -->
                                    <div class="col-md-8">
                                        <h6 class="fw-bold mb-3">Ảnh Mẫu</h6>
                                        <div id="previewContainer" class="d-flex flex-wrap gap-2">
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="col-md-12">
                                <div class="image-upload-section">
                                    <h4>Ảnh Chi Tiết (Tối Đa 8 Ảnh)</h4>
                                    <div class="upload-container mess-error">
                                        <!-- Hidden file input -->
                                        <input type="file" id="detailImagesInput" name="detailImages" multiple accept="image/*" hidden onchange="handleFileSelect(event)">
                                        <!-- Drop zone -->
                                        <div class="drop-zone" onclick="document.getElementById('detailImagesInput').click()" ondragover="handleDragOver(event)" ondrop="handleDrop(event)">
                                            <div class="upload-prompt">
                                                <i class="fas fa-cloud-upload-alt"></i>
                                                <p>Kéo và thả hình ảnh vào đây hoặc nhấp để tải lên</p>
                                                <small class="text-muted">(Tối Đa 8 Ảnh, JPEG/PNG/WEBP)</small>
                                            </div>
                                        </div>
                                        <!-- Preview grid -->
                                        <div id="previewGrid" class="preview-grid"></div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Submit Buttons -->
                        <div class="d-flex justify-content-end gap-3 mt-4">
                            <a href="ProductServlet?&action=list" class="btn btn-primary btn-sm">
                                Trở Về
                            </a>
                            <button type="submit" class="btn btn-primary px-4">
                                <i class="fas fa-save me-2"></i>Cập Nhật Sản Phẩm
                            </button>

                        </div>
                    </div>
                </div>
            </form>
        </div>

        <!-- Bootstrap & Quill JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.quilljs.com/1.3.6/quill.js"></script>

        <script src="JS/AddNewProduct.js"></script>

        <!-- Success Message Modal -->
        <div class="modal fade" id="successModal" tabindex="-1" aria-labelledby="successModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header bg-success text-white">
                        <h5 class="modal-title" id="successModalLabel">Thông Báo Thành Công</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body text-dark">
                        <p id="successMessage">${sessionScope.successMessage}</p>
                    </div>
                    <div class="modal-footer">
                        <!-- Nút quay lại trang ProductManagement -->
                        <button type="button" class="btn btn-primary" onclick="window.location.href = 'ProductServlet?&action=list'">Quay lại</button>
                    </div>
                </div>
            </div>
        </div>
        <!-- Chỉ định nghĩa 1 modal lỗi duy nhất -->
        <div class="modal fade" id="errorModal" tabindex="-1" aria-labelledby="errorModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header bg-danger text-white">
                        <h5 class="modal-title" id="errorModalLabel">Thông Báo Thất Bại</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <p id="errorMessage">${sessionScope.errorMessage}</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Ðóng</button>
                    </div>
                </div>
            </div>
        </div>

        <script>
            window.onload = function () {
                var errorMessage = "${sessionScope.errorMessage}";
                if (errorMessage && errorMessage.trim() !== "") {
                    var errorModal = new bootstrap.Modal(document.getElementById("errorModal"));
                    errorModal.show();
            <% request.getSession().removeAttribute("errorMessage"); %>
                }
            };
        </script>

        <script>

            var quill = new Quill('#editor-container', {
                theme: 'snow'
            });
            var oldDesc = document.querySelector('input[name="description"]').value;
            quill.root.innerHTML = oldDesc;

        </script>
        <script>

            var oldDesc = '<c:out value="${product.description}" escapeXml="false"/>';

            console.log("oldDesc = ", oldDesc);

            quill.root.innerHTML = oldDesc;

            document.querySelector('form').addEventListener('submit', function () {
                document.getElementById('descriptionInput').value = quill.root.innerHTML;
            });
        </script>
    </body>
</html>