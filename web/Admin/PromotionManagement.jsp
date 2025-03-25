<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="" />
        <meta name="author" content="" />
        <title>Dashboard - Promotion Management</title>
        <!-- External CSS and JS -->
        <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="CSS/style.css" />
        <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script src="JS/SideBarToggle.js"></script>
    </head>
    <body class="sb-nav-fixed">
        <!-- Sidebar (Included) -->
        <%@ include file="Component/SideBar.jsp" %>

        <div id="layoutSidenav">
            <div id="layoutSidenav_content">
                <main>
                    <div class="container-fluid px-5">
                        <h1 class="mt-4">Quản lý khuyến mãi</h1>

                        <!-- Success/Error Messages -->
                        <c:if test="${not empty message}">
                            <div class="alert alert-${messageType} alert-dismissible fade show" role="alert">
                                ${message}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>

                        <!-- Search Forms in a single row -->
                        <div class="row mb-4">
                            <!-- Search by Customer Name -->
                            <div class="search-box mb-3">
                                <form action="${pageContext.request.contextPath}/Admin/promotionController" method="POST" class="input-group">
                                    <input type="hidden" name="action" value="searchByDiscount">
                                    <input type="number" step="0.1" name="minDiscount" class="form-control" placeholder="Nhập phần trăm giảm giá tối thiểu %" required>
                                    <button class="btn btn-outline-primary" type="submit"><i class="fas fa-search"></i></button>
                                </form>
                            </div>
                            <!-- Modal Thêm Khuyến Mãi -->
                            <div class="modal fade" id="addPromotionModal" tabindex="-1" aria-labelledby="addPromotionModalLabel" aria-hidden="true">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title" id="addPromotionModalLabel">Thêm khuyến mãi mới</h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                                        </div>
                                        <div class="modal-body">
                                            <form action="addPromotion" id="addPromotionForm" method="post">
                                                <div class="mb-3">
                                                    <label for="productID" class="form-label">Chọn sản phẩm</label>
                                                    <select name="productID" id="productID" class="form-select" required>
                                                        <option value="">-- Chọn sản phẩm --</option>
                                                        <c:forEach var="product" items="${productList}">
                                                            <option value="${product.productID}">${product.productID} - ${product.name}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>

                                                <div class="mb-3">
                                                    <label for="name" class="form-label">Tên khuyến mãi</label>
                                                    <input type="text" name="name" class="form-control" required>
                                                </div>

                                                <div class="mb-3">
                                                    <label for="description" class="form-label">Mô tả</label>
                                                    <textarea name="description" class="form-control" required></textarea>
                                                </div>

                                                <div class="mb-3">
                                                    <label for="discountPercent" class="form-label">Phần trăm giảm giá</label>
                                                    <input type="number" step="0.01" name="discountPercent" class="form-control" required>
                                                </div>

                                                <div class="mb-3">
                                                    <label for="startDate" class="form-label">Ngày bắt đầu</label>
                                                    <input type="date" name="startDate" class="form-control" required>
                                                </div>

                                                <div class="mb-3">
                                                    <label for="endDate" class="form-label">Ngày kết thúc</label>
                                                    <input type="date" name="endDate" class="form-control" required>
                                                </div>
                                                <div class="d-grid">
                                                    <button type="submit" class="btn btn-success">Thêm khuyến mãi</button>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>


                            <!-- Promotion List Table -->
                            <div class="card mb-4">
                                <div class="card-header">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div>
                                            <i class="fas fa fa-gift me-1"></i> Danh sách khuyến mãi
                                        </div>
                                        <div>
                                            <button class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#addPromotionModal">
                                                <i class="fas fa-plus"></i> Thêm khuyến mãi
                                            </button>
                                        </div>
                                    </div>
                                </div>
                                <div class="card-body">
                                    <div class="search-box mb-3">
                                        <form action="${pageContext.request.contextPath}/Admin/promotionController" method="POST" class="input-group">
                                            <input type="hidden" name="action" value="search">
                                            <input type="text" name="promotionName" class="form-control" placeholder="Nhập tên khuyến mãi" required>
                                            <button class="btn btn-outline-primary" type="submit"><i class="fas fa-search"></i></button>
                                            <a href="${pageContext.request.contextPath}/Admin/promotionController" class="btn btn-outline-danger">
                                                <i class="fas fa-sync"></i>
                                            </a>
                                        </form>
                                    </div>
                                </div>
                                <div class="card-body">
                                    <div class="table-responsive">
                                        <table class="table table-bordered table-striped table-hover">
                                            <thead class="table-dark text-center">
                                                <tr>
                                                    <th>Mã</th>
                                                    <th>Mã sản phẩm</th>
                                                    <th>Tên</th>
                                                    <th>Mã khuyến mãi</th>
                                                    <th>Mô tả</th>
                                                    <th>Giảm giá (%)</th>
                                                    <th>Ngày bắt đầu</th>
                                                    <th>Ngày kết thúc</th>
                                                    <th>Ngày tạo</th>
                                                    <th>Trạng thái</th>
                                                    <th>Hành động</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach items="${listP}" var="o">
                                                    <tr>
                                                        <td>${o.promotionID}</td>
                                                        <td>${o.productID}</td>
                                                        <td>${o.name}</td>
                                                        <td>${o.promotionCode}</td>
                                                        <td>${o.description}</td>
                                                        <td>${o.discountPercent}%</td>                                                
                                                        <td>${o.startDate}</td>
                                                        <td>${o.endDate}</td>
                                                        <td>${o.createdAt}</td>
                                                        <td>
                                                            <span class="badge ${o.status == 'Active' ? 'bg-success' : 'bg-danger'}">
                                                                ${o.status == 'Active' ? 'Hoạt động' : 'Hết hạn'}
                                                            </span>
                                                        </td>
                                                        <td class="text-center">
                                                            <button class="btn btn-sm btn-primary" onclick="openEditModal('${o.promotionID}', '${o.productID}', '${o.name}', '${o.description}', '${o.discountPercent}', '${o.startDate}', '${o.endDate}', '${o.status}', '${o.promotionCode}')">
                                                                <i class="fas fa-edit"></i> 
                                                            </button>
                                                            <button class="btn btn-sm btn-danger" data-bs-toggle="modal" data-bs-target="#confirmDeleteModal" onclick="setDeletePromotionID(${o.promotionID})">
                                                                <i class="fas fa-trash"></i>
                                                            </button>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>

                            <!-- Pagination -->
                            <nav aria-label="Page navigation">
                                <ul class="pagination justify-content-center">
                                    <c:forEach begin="1" end="${endPage}" var="i">
                                        <li class="page-item <c:if test='${i == currentPage}'>active</c:if>'">
                                            <a class="page-link" href="${pageContext.request.contextPath}/Admin/promotionController?index=${i}">${i}</a>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </nav>


                        </div>
                </main>
            </div>
        </div>

        <!-- Delete Confirmation Modal -->
        <div class="modal fade" id="confirmDeleteModal" tabindex="-1" aria-labelledby="confirmDeleteModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title text-danger" id="confirmDeleteModalLabel"><i class="fas fa-exclamation-triangle"></i> Xác Nhận Xóa</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        Bạn có chắc chắn muốn xóa khuyến mãi này không?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                        <form id="deleteForm" method="POST" action="promotionController">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="promotionID" id="deletePromotionID">
                            <button type="submit" class="btn btn-danger">Xóa</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <!-- Modal Cập Nhật Khuyến Mãi -->
        <div class="modal fade" id="editPromotionModal" tabindex="-1" aria-labelledby="editPromotionModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editPromotionModalLabel">Cập nhật khuyến mãi</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                    </div>
                    <div class="modal-body">
                        <form action="UpdatePromotion" method="post">
                            <input type="hidden" id="editPromotionID" name="promotionID">

                            <div class="mb-3">
                                <label for="editProductID" class="form-label">Chọn sản phẩm</label>
                                <select name="productID" id="editProductID" class="form-select" required>
                                    <option value="">-- Chọn sản phẩm --</option>
                                    <c:forEach var="product" items="${productList}">
                                        <option value="${product.productID}">${product.productID} - ${product.name}</option>
                                    </c:forEach>
                                </select>

                                <input type="hidden" id="editPromotionCode" name="promotionCode">    
                            </div>

                            <div class="mb-3">
                                <label for="editName" class="form-label">Tên khuyến mãi</label>
                                <input type="text" id="editName" name="name" class="form-control" required>
                            </div>

                            <div class="mb-3">
                                <label for="editDescription" class="form-label">Mô tả</label>
                                <textarea id="editDescription" name="description" class="form-control" required></textarea>
                            </div>

                            <div class="mb-3">
                                <label for="editDiscountPercent" class="form-label">Phần trăm giảm giá</label>
                                <input type="number" step="0.01" id="editDiscountPercent" name="discountPercent" class="form-control" required>
                            </div>

                            <div class="mb-3">
                                <label for="editStartDate" class="form-label">Ngày bắt đầu</label>
                                <input type="date" id="editStartDate" name="startDate" class="form-control" required>
                            </div>

                            <div class="mb-3">
                                <label for="editEndDate" class="form-label">Ngày kết thúc</label>
                                <input type="date" id="editEndDate" name="endDate" class="form-control" required>
                            </div>

                            <div class="mb-3">
                                <label for="editStatus" class="form-label">Trạng thái</label>
                                <select name="status" id="editStatus" class="form-select">
                                    <option value="Active">Hoạt động</option>
                                    <option value="Expired">Hết hạn</option>
                                </select>
                            </div>

                            <div class="d-grid">
                                <button type="submit" class="btn btn-primary">Cập nhật khuyến mãi</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <% 
            String successMessage = (String) session.getAttribute("successModal");
            String errorMessage = (String) session.getAttribute("errorModal");
        %>
        <!-- Bootstrap Modal -->
        <div class="modal fade" id="notificationModal" tabindex="-1" aria-labelledby="notificationModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title text-center" id="notificationModalLabel">
                            <%= (successMessage != null) ? "Thành công" : "Lỗi" %>
                        </h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body text-center">
                        <p class="<%= (successMessage != null) ? "text-success" : "text-danger" %> fw-bold">
                            <%= (successMessage != null) ? successMessage : errorMessage %>
                        </p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                    </div>
                </div>
            </div>
        </div>
        <%-- Xóa thông báo khỏi session để không hiển thị lại --%>
        <% session.removeAttribute("successModal"); %>
        <% session.removeAttribute("errorModal"); %>



        <!-- Script to Set Order ID for Deletion -->
        <!-- Script Xóa -->
        <script>
            function setDeletePromotionID(promotionID) {
                document.getElementById('deletePromotionID').value = promotionID;
            }
        </script>
        <!-- Script mở Modal -->
        <script>
            function openEditModal(id, productID, name, description, discountPercent, startDate, endDate, status, promotionCode) {
                document.getElementById("editPromotionID").value = id;
                document.getElementById("editProductID").value = productID;
                document.getElementById("editName").value = name;
                document.getElementById("editPromotionCode").value = promotionCode;
                document.getElementById("editDescription").value = description;
                document.getElementById("editDiscountPercent").value = discountPercent;
                document.getElementById("editStartDate").value = startDate;
                document.getElementById("editEndDate").value = endDate;
                document.getElementById("editStatus").value = status;
                new bootstrap.Modal(document.getElementById("editPromotionModal")).show();
            }
        </script>
        <!-- Script Hiển Thị Modal -->
        <script>
            document.addEventListener("DOMContentLoaded", function () {
                var successMessage = "<%= successMessage %>";
                var errorMessage = "<%= errorMessage %>";

                if (successMessage.trim() !== "null" || errorMessage.trim() !== "null") {
                    var myModal = new bootstrap.Modal(document.getElementById("notificationModal"));
                    myModal.show();

                    // Tự động đóng modal sau 3 giây
                    setTimeout(() => {
                        myModal.hide();
                    }, 3000);
                }
            });
        </script>


    </body>
</html>


