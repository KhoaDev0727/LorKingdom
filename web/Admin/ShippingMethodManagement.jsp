<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="" />
        <meta name="author" content="" />
        <title>Dashboard - Shipping Method Management</title>
        <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="CSS/style.css" />
        <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script src="JS/SideBarToggle.js"></script>
    </head>
    <body class="sb-nav-fixed">
        <c:choose>
            <c:when test="${empty sessionScope.roleID || sessionScope.roleID eq 4}">
                <c:redirect url="/Admin/loginPage.jsp"/>
            </c:when>
            <c:when test="${sessionScope.roleID eq 3}">
                <c:redirect url="/LogoutServlet"/>
            </c:when>
        </c:choose>
        <%@ include file="Component/SideBar.jsp" %>
        <div id="layoutSidenav">
            <div id="layoutSidenav_content">
                <div class="dashboard-container">
                    <!-- Table -->
                    <main>
                        <div class="container-fluid px-5">
                            <h1 class="mt-4">Quản Lý Phương Thức Giao Hàng</h1>
                            <div class="card mb-4">
                                <div class="card-header">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div>
                                            <i class="fas fa-truck me-1"></i> Danh Sách Phương Thức Giao Hàng
                                        </div>
                                        <div>
                                            <button class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#addShippingMethodModal">
                                                <i class="fas fa-plus"></i> Thêm Phương Thức Giao Hàng
                                            </button>
                                        </div>
                                    </div>
                                </div>

                                <div class="card-body">
                                    <!-- Search Form -->
                                    <form action="ShippingMethodServlet" method="GET" class="mb-4">
                                        <div class="input-group">
                                            <input type="hidden" name="action" value="search">
                                            <input type="text" name="search" class="form-control" placeholder="Search Shipping Method by Name, Description..." 
                                                   value="${param.search}">
                                            <button class="btn btn-outline-secondary" type="submit">
                                                <i class="fas fa-search"></i>
                                            </button>
                                            <a href="ShippingMethodServlet?&action=list" class="btn btn-outline-danger">
                                                <i class="fas fa-sync"></i>
                                            </a>
                                            <c:if test="${sessionScope.roleID == 1}">
                                                <a href="ShippingMethodServlet?action=listDeleted" class="btn btn-outline-danger">
                                                    <i class="fas fa-trash"></i> 
                                                </a>
                                            </c:if>
                                        </div>
                                    </form>

                                    <!-- Shipping Method Table -->
                                    <div class="table-responsive">
                                        <table class="table table-bordered table-striped table-hover">
                                            <thead class="table-dark text-center">
                                                <tr>
                                                    <th>Mã</th>
                                                    <th>Tên Phương Thức Giao Hàng</th>
                                                    <th>Miêu Tả</th>
                                                    <th>Trạng Thái</th>
                                                    <th>Hành Động</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:choose>
                                                    <c:when test="${empty shippingMethods}">
                                                        <tr>
                                                            <td colspan="5" class="text-center text-muted">Không Tìm Thấy Phương Thức Giao Hàng</td>
                                                        </tr>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach var="method" items="${shippingMethods}">
                                                            <tr class="${method.isDeleted == 1 ? 'deleted-row' : ''}">
                                                                <td>${method.shippingMethodID}</td>
                                                                <td>${method.methodName}</td>
                                                                <td>${method.description}</td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${method.isDeleted == 1}">
                                                                            <span class="badge bg-danger">Đã Xóa</span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="badge bg-success">Hoạt Động</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                <td>
                                                                    <!-- Nếu isDeleted=0 => Edit & Xóa mềm -->
                                                                    <c:if test="${method.isDeleted == 0}">
                                                                        <button class="btn btn-sm btn-warning"
                                                                                data-bs-toggle="modal"
                                                                                data-bs-target="#editShippingMethodModal${method.shippingMethodID}">
                                                                            <i class="fas fa-edit"></i>
                                                                        </button>
                                                                        <button type="button" class="btn btn-sm btn-danger"
                                                                                data-bs-toggle="modal"
                                                                                data-bs-target="#confirmSoftDeleteModal"
                                                                                onclick="setSoftDeleteshippingMethodID(${method.shippingMethodID})">
                                                                            <i class="fas fa-trash"></i>
                                                                        </button>
                                                                    </c:if>
                                                                    <!-- Nếu isDeleted=1 => Restore & Xóa cứng -->
                                                                    <c:if test="${method.isDeleted == 1}">
                                                                        <button class="btn btn-sm btn-success"
                                                                                onclick="location.href = 'ShippingMethodServlet?action=restore&shippingMethodID=${method.shippingMethodID}'">
                                                                            Restore
                                                                        </button>
                                                                        <button type="button" class="btn btn-sm btn-danger"
                                                                                data-bs-toggle="modal"
                                                                                data-bs-target="#confirmHardDeleteModal"
                                                                                onclick="setHardDeleteShippingMethodID(${method.shippingMethodID})">
                                                                            <i class="fas fa-trash"></i>
                                                                        </button>
                                                                    </c:if>
                                                                </td>
                                                            </tr>

                                                            <!-- Edit Shipping Method Modal -->
                                                        <div class="modal fade" id="editShippingMethodModal${method.shippingMethodID}">
                                                            <div class="modal-dialog">
                                                                <div class="modal-content">
                                                                    <form action="ShippingMethodServlet" method="POST" onsubmit="return validateForm()">
                                                                        <input type="hidden" name="action" value="update">
                                                                        <input type="hidden" name="shippingMethodID" value="${method.shippingMethodID}">
                                                                        <div class="modal-header">
                                                                            <h5 class="modal-title">Cập Nhật Phương Thức Giao Hàng</h5>
                                                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                                        </div>
                                                                        <div class="modal-body">
                                                                            <div class="mb-3">
                                                                                <label class="form-label">Tên Phương Thức</label>
                                                                                <input type="text" class="form-control" name="methodName" value="${method.methodName}" required>
                                                                            </div>
                                                                            <div class="mb-3">
                                                                                <label class="form-label">Miêu Tả</label>
                                                                                <textarea class="form-control" name="description" rows="3">${method.description}</textarea>
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

        <!-- Error Message Modal -->
        <div class="modal fade" id="errorModal" tabindex="-1" aria-labelledby="errorModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header bg-danger text-white">
                        <h5 class="modal-title" id="errorModalLabel">Thông báo lỗi</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body text-dark">
                        ${errorMessage}
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Add Shipping Method Modal -->
        <div class="modal fade" id="addShippingMethodModal">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="ShippingMethodServlet" method="POST">
                        <input type="hidden" name="action" value="add">
                        <div class="modal-header">
                            <h5 class="modal-title">Thêm Phương Thức Giao Hàng</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <div class="mb-3">
                                <label for="methodName" class="form-label">Tên Phương Thức</label>
                                <input type="text" class="form-control" id="methodName" name="methodName" required>
                            </div>
                            <div class="mb-3">
                                <label for="description" class="form-label">Miêu Tả</label>
                                <textarea class="form-control" id="description" name="description" rows="3" required></textarea>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                            <button type="submit" class="btn btn-primary">Thêm Phương Thức Giao Hàng</button>
                        </div>
                    </form>
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
                        ${sessionScope.successMessage}
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
                        Phương thức vận chuyển này sẽ được chuyển vào thùng rác!
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                        <form method="POST" action="ShippingMethodServlet">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="shippingMethodID" id="softDeleteshippingMethodID">
                            <button type="submit" class="btn btn-danger">
                                <i class="fas fa-trash"></i>
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal XÓA CỨNG -->
        <div class="modal fade" id="confirmHardDeleteModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Xác nhận xóa</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        Phương thức vận chuyển sẽ bị xóa vĩnh viễn!
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                        <form method="POST" action="ShippingMethodServlet">
                            <input type="hidden" name="action" value="hardDelete">
                            <input type="hidden" name="shippingMethodID" id="hardDeleteShippingMethodID">
                            <button type="submit" class="btn btn-danger">
                                <i class="fas fa-trash"></i> 
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <script>
            function setSoftDeleteshippingMethodID(shippingMethodID) {
                document.getElementById("softDeleteshippingMethodID").value = shippingMethodID;
            }

            function setHardDeleteShippingMethodID(shippingMethodID) {
                document.getElementById("hardDeleteShippingMethodID").value = shippingMethodID;
            }

            window.onload = function () {
                const errorMessage = "${sessionScope.errorMessage}";
                if (errorMessage && errorMessage.trim() !== "") {
                    const errorModal = new bootstrap.Modal(document.getElementById("errorModal"));
                    errorModal.show();
            <% request.getSession().removeAttribute("errorMessage"); %>
                }

                let successMessage = '${sessionScope.successMessage}';
                if (successMessage && successMessage.trim() !== "null" && successMessage.trim() !== "") {
                    let successModal = new bootstrap.Modal(document.getElementById("successModal"));
                    successModal.show();
            <% session.removeAttribute("successMessage"); %>
                }
            };
        </script>
    </body>
</html>