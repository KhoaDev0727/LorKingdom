<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <title>Dashboard - Payment Method Management</title>
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
                    <main>
                        <div class="container-fluid px-5">
                            <h1 class="mt-4">Quản Lí Phương Thức Thanh Toán</h1>
                            <c:if test="${not empty message}">
                                <div class="alert alert-${messageType} alert-dismissible fade show" role="alert">
                                    ${message}
                                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                                </div>
                            </c:if>

                            <div class="card mb-4">
                                <div class="card-header d-flex justify-content-between align-items-center">
                                    <div>
                                        <i class="fas fa-credit-card me-1"></i> Danh Sách Phương Thức Thanh Toán
                                    </div>
                                    <button class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#addPaymentMethodModal">
                                        <i class="fas fa-plus"></i> Thêm Phương Thức Thanh Toán
                                    </button>
                                </div>
                                <div class="card-body">
                                    <form action="PaymentMethodServlet" method="GET" class="mb-4">
                                        <div class="input-group">
                                            <input type="hidden" name="action" value="search">
                                            <input type="text" name="search" class="form-control" placeholder="Tìm Kiếm Phương Thức Thanh Toán..." value="${param.search}">
                                            <button class="btn btn-outline-secondary" type="submit">
                                                <i class="fas fa-search"></i>
                                            </button>
                                            <a href="PaymentMethodServlet?action=list" class="btn btn-outline-danger">
                                                <i class="fas fa-sync"></i>
                                            </a>
                                            <c:if test="${sessionScope.roleID == 1}">
                                                <a href="PaymentMethodServlet?action=listDeleted" class="btn btn-outline-danger">
                                                    <i class="fas fa-trash"></i>
                                                </a>
                                            </c:if>
                                        </div>
                                    </form>

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
                                                    <c:when test="${empty paymentMethods}">
                                                        <tr>
                                                            <td colspan="5" class="text-center text-muted">Không Tìm Thấy Phương Thức Thanh Toán.</td>
                                                        </tr>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach var="method" items="${paymentMethods}">
                                                            <tr class="${method.isDeleted == 1 ? 'deleted-row' : ''}">
                                                                <td>${method.paymentMethodID}</td>
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
                                                                                data-bs-target="#editPaymentModal${method.paymentMethodID}">
                                                                            <i class="fas fa-edit"></i>
                                                                        </button>
                                                                        <button type="button" class="btn btn-sm btn-danger"
                                                                                data-bs-toggle="modal"
                                                                                data-bs-target="#confirmSoftDeleteModal"
                                                                                onclick="setSoftDeletePaymentMethodID(${method.paymentMethodID})">
                                                                            <i class="fas fa-trash"></i>
                                                                        </button>
                                                                    </c:if>
                                                                    <!-- Nếu isDeleted=1 => Restore & Xóa cứng -->
                                                                    <c:if test="${method.isDeleted == 1}">
                                                                        <button class="btn btn-sm btn-success"
                                                                                onclick="location.href = 'PaymentMethodServlet?action=restore&paymentMethodID=${method.paymentMethodID}'">
                                                                            Khôi Phục
                                                                        </button>
                                                                        <button type="button" class="btn btn-sm btn-danger"
                                                                                data-bs-toggle="modal"
                                                                                data-bs-target="#confirmHardDeleteModal"
                                                                                onclick="setHardDeletePaymentMethodID(${method.paymentMethodID})">
                                                                            <i class="fas fa-trash"></i>
                                                                        </button>
                                                                    </c:if>
                                                                </td>
                                                            </tr>

                                                            <!-- Edit Modal -->
                                                        <div class="modal fade" id="editPaymentModal${method.paymentMethodID}">
                                                            <div class="modal-dialog">
                                                                <div class="modal-content">
                                                                    <form action="PaymentMethodServlet" method="POST">
                                                                        <input type="hidden" name="action" value="update">
                                                                        <input type="hidden" name="paymentMethodID" value="${method.paymentMethodID}">
                                                                        <div class="modal-header">
                                                                            <h5 class="modal-title">Cập Nhật Phương Thanh Toán</h5>
                                                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                                        </div>
                                                                        <div class="modal-body">
                                                                            <div class="mb-3">
                                                                                <label class="form-label">Tên Phương Thức Thanh Toán</label>
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

        <!-- Add Payment Method Modal -->
        <div class="modal fade" id="addPaymentMethodModal">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="PaymentMethodServlet" method="POST">
                        <input type="hidden" name="action" value="add">
                        <div class="modal-header">
                            <h5 class="modal-title">Thêm Phương Thức Thanh Toán</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <div class="mb-3">
                                <label for="methodName" class="form-label">Tên Phương Thanh Toán</label>
                                <input type="text" class="form-control" id="methodName" name="methodName" required>
                            </div>
                            <div class="mb-3">
                                <label for="description" class="form-label">Miêu Tả</label>
                                <textarea class="form-control" id="description" name="description" rows="3" required></textarea>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                            <button type="submit" class="btn btn-primary">Thêm Phương Thức Thanh Toán</button>
                        </div>
                    </form>
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
                        ${sessionScope.errorMessage}
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                    </div>
                </div>
            </div>
        </div>

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

        <div class="modal fade" id="confirmSoftDeleteModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Xác nhận xóa</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        Phương thức này sẽ được chuyển vào thùng rác!
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                        <form method="POST" action="PaymentMethodServlet">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="paymentMethodID" id="softDeletePaymentMethodID">
                            <button type="submit" class="btn btn-danger">
                               Xóa
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="confirmHardDeleteModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Xác nhận xóa vĩnh viễn</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        Phương thức này sẽ bị xóa vĩnh viễn!
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                        <form method="POST" action="PaymentMethodServlet">
                            <input type="hidden" name="action" value="hardDelete">
                            <input type="hidden" name="paymentMethodID" id="hardDeletePaymentMethodID">
                            <button type="submit" class="btn btn-danger">
                                <i class="fas fa-trash"></i>
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <script>
            function setSoftDeletePaymentMethodID(paymentMethodID) {
                document.getElementById("softDeletePaymentMethodID").value = paymentMethodID;
            }

            function setHardDeletePaymentMethodID(paymentMethodID) {
                document.getElementById("hardDeletePaymentMethodID").value = paymentMethodID;
            }

            window.onload = function () {
                const errorMessage = "${sessionScope.errorMessage}";
                if (errorMessage && errorMessage.trim() !== "") {
                    const errorModal = new bootstrap.Modal(document.getElementById("errorModal"));
                    errorModal.show();
            <% session.removeAttribute("errorMessage"); %>
                }

                const successMessage = "${sessionScope.successMessage}";
                if (successMessage && successMessage.trim() !== "" && successMessage.trim() !== "null") {
                    const successModal = new bootstrap.Modal(document.getElementById("successModal"));
                    successModal.show();
            <% session.removeAttribute("successMessage"); %>
                }
            };
        </script>
    </body>
</html>