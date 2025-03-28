<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="" />
        <meta name="author" content="" />
        <title>Category Management</title>
        <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="CSS/style.css" />
        <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script src="JS/SideBarToggle.js"></script>
        <style>
            .rating-filter button {
                margin: 5px;
                padding: 8px 15px;
            }
            .rating-filter button.active {
                background-color: #0d6efd;
                color: white;
            }
            .star {
                color: red;
                font-size: 1.2em;
            }
        </style>
    </head>
    <body class="sb-nav-fixed">
        <c:choose>
            <c:when test="${empty sessionScope.roleID || sessionScope.roleID eq 2}">
                <c:redirect url="/Admin/loginPage.jsp"/>
            </c:when>
            <c:when test="${sessionScope.roleID eq 3}">
                <c:redirect url="/LogoutServlet"/>
            </c:when>
        </c:choose>
        <div id="layoutSidenav">
            <div id="layoutSidenav_content">
                <%@ include file="Component/SideBar.jsp" %>
                <div class="dashboard-container">
                    <main>
                        <div class="container-fluid px-5">
                            <h1 class="mt-4">Quản Lí Danh Mục</h1>

                            <form action="CategoryServlet" method="POST" class="mt-4">
                                <input type="hidden" name="action" value="add">

                                <label>Tên Danh Mục</label>
                                <input type="text" name="categoryName" required />

                                <label>Danh Mục</label>
                                <select name="superCategoryID" required>
                                    <option value="">-- Chọn Danh Mục Tổng --</option>
                                    <c:forEach var="superCategory" items="${superCategories}">
                                        <option value="${superCategory.superCategoryID}">${superCategory.name}</option>
                                    </c:forEach>
                                </select>

                                <button class="btn btn-primary ms-2" type="submit">Thêm Danh Mục</button>
                            </form>

                            <div class="card mb-4">
                                <div class="card-header">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div>
                                            <i class="fas fa-table me-1"></i> Danh Sách Danh Mục
                                        </div>
                                    </div>
                                </div>

                                <div class="card-body">
                                    <!-- Search Form -->
                                    <form action="CategoryServlet" method="GET" class="mb-4">
                                        <div class="input-group">
                                            <input type="hidden" name="action" value="search">
                                            <input type="text" name="search" class="form-control" placeholder="Tìm Kiếm Danh Mục Tên " aria-label="Search">
                                            <button class="btn btn-outline-secondary" type="submit">
                                                <i class="fas fa-search"></i>
                                            </button>

                                            <a href="CategoryServlet" class="btn btn-outline-danger">
                                                <i class="fas fa-sync"></i>
                                            </a>
                                            <c:if test="${sessionScope.roleID == 1}">
                                                <a href="CategoryServlet?action=listDeleted" class="btn btn-outline-danger">
                                                    <i class="fas fa-trash"></i>
                                                </a>
                                            </c:if>

                                        </div>
                                    </form>
                                    <!-- Customer Table -->
                                    <div class="table-responsive">
                                        <%@ include file="Component/PaginationCategory.jsp" %>
                                        <table class="table table-bordered table-striped table-hover">
                                            <thead class="table-dark">
                                                <tr>
                                                    <th>Mã Danh Mục</th>
                                                    <th>Mã Danh Mục Tổng</th>
                                                    <th>Tên Danh Mục</th>
                                                    <th>Trạng Thái</th>
                                                    <th>Ngày Tạo</th>
                                                    <th>Hành Động</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:choose>
                                                    <c:when test="${empty categories}">
                                                        <!-- Display message if the list is empty -->
                                                        <tr>
                                                            <td colspan="4" class="text-center text-muted">Không có danh mục nào trong danh sách.</td>
                                                        </tr>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach var="category" items="${categories}">
                                                            <tr class="${category.isDeleted == 1 ? 'deleted-row' : ''}">
                                                                <td>${category.categoryID}</td>
                                                                <td>${category.superCategoryID}</td>
                                                                <td>${category.name}</td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${category.isDeleted == 1}">
                                                                            <span class="badge bg-danger">Không còn hoạt động</span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="badge bg-success">Hoạt Động</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                <td>${category.createdAt}</td>
                                                                <td>
                                                                    <c:if test="${category.isDeleted == 0}">
                                                                        <!-- Chỉ hiển thị nút sửa và xóa khi danh mục đang active -->
                                                                        <button class="btn btn-sm btn-warning" 
                                                                                data-bs-toggle="modal" 
                                                                                data-bs-target="#editCategoryModal-${category.categoryID}">
                                                                            <i class="fas fa-edit"></i> 
                                                                        </button>
                                                                        <button type="button"
                                                                                class="btn btn-sm btn-danger"
                                                                                data-bs-toggle="modal"
                                                                                data-bs-target="#confirmSoftDeleteModal"
                                                                                onclick="setSoftDeleteCategoryID(${category.categoryID})">
                                                                            <i class="fas fa-trash"></i>
                                                                        </button>
                                                                    </c:if>
                                                                    <c:if test="${category.isDeleted == 1}">
                                                                        <button class="btn btn-sm btn-success" 
                                                                                onclick="location.href = 'CategoryServlet?action=restore&categoryID=${category.categoryID}'">
                                                                            Khôi Phục
                                                                        </button>
                                                                        <button type="button"
                                                                                class="btn btn-sm btn-danger"
                                                                                data-bs-toggle="modal"
                                                                                data-bs-target="#confirmHardDeleteModal"
                                                                                onclick="setHardDeleteCategoryID(${category.categoryID})">
                                                                            <i class="fas fa-trash"></i> 
                                                                        </button>
                                                                    </c:if>

                                                                </td>
                                                            </tr>
                                                        <div class="modal fade" id="editCategoryModal-${category.categoryID}" tabindex="-1">
                                                            <div class="modal-dialog">
                                                                <div class="modal-content">
                                                                    <form method="post" action="CategoryServlet">
                                                                        <div class="modal-header">
                                                                            <h5 class="modal-title">Cập Nhật Danh Mục</h5>
                                                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                                        </div>
                                                                        <div class="modal-body">
                                                                            <input type="hidden" name="action" value="update">
                                                                            <input type="hidden" name="categoryID" value="${category.categoryID}">
                                                                            <div class="mb-3">
                                                                                <label class="form-label">Tên Danh Mục</label>
                                                                                <input type="text" class="form-control" name="categoryName" value="${category.name}" required>
                                                                            </div>
                                                                            <div class="mb-3">
                                                                                <label class="form-label">Danh Mục Tổng</label>
                                                                                <select class="form-control" name="superCategoryID">
                                                                                    <c:forEach var="sc" items="${superCategories}">
                                                                                        <option value="${sc.superCategoryID}" <c:if test="${sc.superCategoryID == category.superCategoryID}">selected</c:if>>
                                                                                            ${sc.name}
                                                                                        </option>
                                                                                    </c:forEach>
                                                                                </select>
                                                                            </div>
                                                                        </div>
                                                                        <div class="modal-footer">
                                                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                                                                            <button type="submit" class="btn btn-primary">Lưu Thay Đổi</button>
                                                                        </div>
                                                                    </form>
                                                                </div>
                                                            </div>
                                                        </div>

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
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
        <!-- Success Message Modal -->
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
        <!-- Modal XÓA MỀM -->
        <div class="modal fade" id="confirmSoftDeleteModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Xác nhận xóa</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        Bạn có chắc chắn muốn đưa danh mục này vào thùng rác không?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                        <form id="deletecategoryForm" method="POST" action="CategoryServlet">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="categoryID" id="softDeleteCategoryID">
                            <button type="submit" class="btn btn-danger">Xóa</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="confirmHardDeleteModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Xác nhận xóa</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        Bạn có chắc chắn muốn xóa vĩnh viễn danh mục này không?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                        <form id="hardDeleteForm" method="POST" action="CategoryServlet">
                            <input type="hidden" name="action" value="hardDelete">
                            <input type="hidden" name="categoryID" id="hardDeleteCategoryID">
                            <button type="submit" class="btn btn-danger">Xóa</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>


    </body>
    <script>
        function setSoftDeleteCategoryID(categoryID) {
            document.getElementById("softDeleteCategoryID").value = categoryID;
        }
        function setHardDeleteCategoryID(categoryID) {
            document.getElementById("hardDeleteCategoryID").value = categoryID;
        }
        window.onload = function () {
            let errorMessage = "${sessionScope.errorMessage}";
            if (errorMessage && errorMessage.trim() !== "") {
                let errorModal = new bootstrap.Modal(document.getElementById('errorModal'));
                errorModal.show();
        <% request.getSession().removeAttribute("errorMessage"); %>
            }

            let successMessage = "${sessionScope.successMessage}";
            if (successMessage && successMessage.trim() !== "") {
                let successModal = new bootstrap.Modal(document.getElementById('successModal'));
                successModal.show();
        <% request.getSession().removeAttribute("successMessage"); %>
            }
        };
    </script>
</html>



