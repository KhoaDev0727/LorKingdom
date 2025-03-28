<%-- 
    Document   : ProductManagement
    Created on : Feb 28, 2025, 12:01:21 PM
    Author     : admin1
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="" />
        <meta name="author" content="" />
        <title>Product Management</title>
        <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="CSS/style.css" />
        <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script src="JS/SideBarToggle.js"></script>
        <style>
            .deleted-row {
                background-color: #f8d7da;
            }
        </style>
    </head>
    <body class="sb-nav-fixed">
        <div id="layoutSidenav">
            <c:choose>
                <c:when test="${empty sessionScope.roleID || sessionScope.roleID eq 2}">
                    <c:redirect url="/Admin/loginPage.jsp"/>
                </c:when>
                <c:when test="${sessionScope.roleID eq 3}">
                    <c:redirect url="/LogoutServlet"/>
                </c:when>
            </c:choose>

            <div id="layoutSidenav_content">
                <%@ include file="Component/SideBar.jsp" %>
                <div class="dashboard-container">
                    <main>
                        <div class="container-fluid px-5">
                            <h1 class="mt-4">Quản Lí Sản Phẩm</h1>
                            <div class="card mb-4 mt-3">
                                <div class="card-header">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div>
                                            <i class="fas fa-table me-1"></i> Danh Sách Sản Phẩm 
                                        </div>
                                        <div>
                                            <button class="btn btn-primary btn-sm" data-bs-toggle="modal" onclick="location.href = 'ProductManagementServlet'">
                                                <i class="fas fa-plus"></i> Thêm Sản Phẩm
                                            </button>
                                        </div>
                                    </div>

                                </div>

                                <div class="card-body mb-3">
                                    <form action="ProductServlet" method="GET" class="mb-4 mt-3">
                                        <div class="input-group">
                                            <input type="hidden" name="action" value="search">
                                            <input type="text" name="search" class="form-control" placeholder="Tìm Kiếm Sản Phẩm Theo Tên Và SKU..." aria-label="Search">
                                            <button type="submit" class="btn btn-outline-secondary">
                                                <i class="fas fa-search"></i>
                                            </button>
                                            <a href="ProductServlet" class="btn btn-outline-danger">
                                                <i class="fas fa-sync"></i>
                                            </a>
                                            <c:if test="${sessionScope.roleID == 1}">
                                                <a href="ProductServlet?action=listDeleted" class="btn btn-outline-danger">
                                                    <i class="fas fa-trash"></i>
                                                </a>
                                            </c:if>
                                        </div>
                                    </form>
                                    <div class="table-responsive">
                                        <%@ include file="Component/PaginationProduct.jsp" %>
                                        <table class="table table-bordered table-striped table-hover">
                                            <thead class="table-dark">
                                                <tr>
                                                    <th>Mã</th>
                                                    <th>SKU</th>
                                                    <th>Hình Ảnh</th>
                                                    <th>Tên Sản Phẩm</th>
                                                    <th>Giá</th>
                                                    <th>Số Lượng</th>
                                                    <th>Trạng Thái</th>
                                                    <th>Hành Động</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:choose>
                                                    <c:when test="${empty products}">
                                                        <tr>
                                                            <td colspan="7" class="text-center text-muted">Không có sản phẩm.</td>
                                                        </tr>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach var="prod" items="${products}">
                                                            <tr class="${prod.isDeleted == 1 ? 'deleted-row' : ''}">
                                                                <td>${prod.productID}</td>
                                                                <td>${prod.SKU}</td>
                                                                <td>
                                                                    <c:set var="foundImage" value="false" />

                                                                    <c:forEach var="img" items="${mainImages}">
                                                                        <c:if test="${img.productID == prod.productID}">
                                                                            <img src="${pageContext.request.contextPath}/${img.imageUrl}" 
                                                                                 alt="Main Image" 
                                                                                 width="80" 
                                                                                 style="cursor: pointer;"
                                                                                 data-bs-toggle="modal" 
                                                                                 data-bs-target="#imageModal"
                                                                                 onclick="setModalImage('${pageContext.request.contextPath}/${img.imageUrl}')" />
                                                                            <c:set var="foundImage" value="true" />
                                                                        </c:if>
                                                                    </c:forEach>

                                                                    <c:if test="${not foundImage}">
                                                                        <img src="${pageContext.request.contextPath}/images/no-image.png" 
                                                                             alt="No Image" width="80" />
                                                                    </c:if>
                                                                </td>


                                                                <td>${prod.name}</td>
                                                                <td><fmt:formatNumber value="${prod.price}" pattern="#,###" /></td>
                                                                <td>${prod.quantity}</td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${prod.isDeleted == 1}">
                                                                            <span class="badge bg-danger">Ngừng bán</span>
                                                                        </c:when>

                                                                        <c:when test="${prod.quantity <= 0}">
                                                                            <span class="badge bg-warning text-dark">Hết hàng</span>
                                                                        </c:when>

                                                                        <c:otherwise>
                                                                            <span class="badge bg-success">Còn hàng</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>



                                                                <td>
                                                                    <c:if test="${prod.isDeleted == 0}">
                                                                        <button class="btn btn-sm btn-warning"
                                                                                onclick="location.href = 'updateProductServlet?productID=${prod.productID}'">
                                                                            <i class="fas fa-edit"></i>
                                                                        </button>

                                                                        <button type="button" class="btn btn-sm btn-danger"
                                                                                data-bs-toggle="modal"
                                                                                data-bs-target="#confirmSoftDeleteModal"
                                                                                onclick="setSoftDeleteProductID(${prod.productID})">
                                                                            <i class="fas fa-trash"></i>
                                                                        </button>
                                                                    </c:if>

                                                                    <c:if test="${prod.isDeleted == 1}">
                                                                        <button class="btn btn-sm btn-success"
                                                                                onclick="location.href = 'ProductServlet?action=restore&productID=${prod.productID}'">
                                                                            Khôi Phục
                                                                        </button>
                                                                        <button type="button" class="btn btn-sm btn-danger"
                                                                                data-bs-toggle="modal"
                                                                                data-bs-target="#confirmHardDeleteModal"
                                                                                onclick="setHardDeleteProductID(${prod.productID})">
                                                                            <i class="fas fa-trash"></i>
                                                                        </button><!-- Nút View chi tiết (hiển thị modal) -->
                                                                    </c:if>
                                                                </td>
                                                            </tr>

                                                        </c:forEach>
                                                    </c:otherwise>
                                                </c:choose>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </main>
                </div>
            </div>
        </div>
        
        <!-- Modal Error -->
        <div class="modal fade" id="errorModal" tabindex="-1" aria-labelledby="errorModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header bg-danger text-white">
                        <h5 class="modal-title" id="errorModalLabel">Thông báo lỗi</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <p id="errorMessage">${sessionScope.errorMessage}</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal Success -->
        <div class="modal fade" id="successModal" tabindex="-1" aria-labelledby="successModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header bg-success text-white">
                        <h5 class="modal-title" id="successModalLabel">Thông báo thành công</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body text-dark">
                        <p id="successMessage">${sessionScope.successMessage}</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                    </div>
                </div>
            </div>
        </div>
        <!-- Modal xem ảnh lớn -->
        <div class="modal fade" id="imageModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-body p-0 text-center">
                        <img id="largeImage" src="" alt="Large Image" style="width: 100%; height: auto;" />
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal Soft Delete Product -->
        <div class="modal fade" id="confirmSoftDeleteModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Xác nhận xóa</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        Bạn có muốn đưa sản phẩm này vào thùng rác không
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                        <form method="POST" action="ProductServlet">
                            <input type="hidden" name="action" value="softDelete">
                            <input type="hidden" name="productID" id="softDeleteProductID">
                            <button type="submit" class="btn btn-danger">Xóa</i></button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal Hard Delete Product -->
        <div class="modal fade" id="confirmHardDeleteModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Xác nhận xóa</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        Bạn có muốn xóa sản phẩm này vĩnh viễn không?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                        <form method="POST" action="ProductServlet">
                            <input type="hidden" name="action" value="hardDelete">
                            <input type="hidden" name="productID" id="hardDeleteProductID">
                            <button type="submit" class="btn btn-danger">Xóa</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <script>
            function setSoftDeleteProductID(id) {
                document.getElementById("softDeleteProductID").value = id;
            }
            function setHardDeleteProductID(id) {
                document.getElementById("hardDeleteProductID").value = id;
            }

            function setModalImage(imageUrl) {
                document.getElementById("largeImage").src = imageUrl;
            }


            window.onload = function () {
                let errorMessage = "${sessionScope.errorMessage}";
                if (errorMessage && errorMessage.trim() !== "") {
                    let errorModal = new bootstrap.Modal(document.getElementById('errorModal'));
                    errorModal.show();
            <% request.getSession().removeAttribute("errorMessage"); %>
                }

                let successMessage = "${sessionScope.successMessage}";
                if (successMessage && successMessage.trim() !== "" && successMessage.trim() !== "null") {
                    let successModal = new bootstrap.Modal(document.getElementById('successModal'));
                    successModal.show();
            <% request.getSession().removeAttribute("successMessage"); %>
                }
            };
        </script>
    </body>
</html>





