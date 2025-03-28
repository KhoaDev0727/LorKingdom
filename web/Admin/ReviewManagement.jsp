<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <title>Dashboard - Review Management</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="CSS/style.css" />
        <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <style>
            .form-select {
                font-size: 13px !important;
            }
            .form-control {
                font-size: 13px !important;
            }
        </style>
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
        <div id="layoutSidenav">
            <div id="layoutSidenav_content">
                <%@ include file="Component/SideBar.jsp" %>
                <div class="dashboard-container">
                    <main>
                        <div class="container-fluid px-5">
                            <h1 class="mt-4">Quản Lí Đánh Giá</h1>
                            <div class="card mb-4">
                                <div class="card-header">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div>
                                            <i class="fas fa-commenting me-1"></i> Danh sách đánh giá
                                        </div>
                                    </div>
                                </div>
                                <div class="card-body">
                                    <!-- Search & Filter Form -->
                                    <form action="ReviewManagementServlet" method="GET" class="mb-4">
                                        <input type="hidden" name="action" value="search">
                                        <div class="row g-3 align-items-center">
                                            <!-- Search Input -->
                                            <div class="col-12 col-md-4 col-lg-3">
                                                <div class="input-group">
                                                    <span class="input-group-text">
                                                        <i class="fas fa-search"></i>
                                                    </span>
                                                    <input type="text" 
                                                           class="form-control" 
                                                           name="filterUserProduct" 
                                                           placeholder="Tìm theo ID người dùng/sản phẩm">
                                                </div>
                                            </div>

                                            <!-- Rating Filter -->
                                            <div class="col-6 col-md-3 col-lg-2">
                                                <div class="input-group">
                                                    <span class="input-group-text" >
                                                        <i class="fas fa-star"></i>
                                                    </span>
                                                    <select class="form-select" name="filterRating">
                                                        <option value="0">Tất Cả đánh giá</option>
                                                        <option value="5">★★★★★ (5 sao)</option>
                                                        <option value="4">★★★★☆ (4 sao)</option>
                                                        <option value="3">★★★☆☆ (3 sao)</option>
                                                        <option value="2">★★☆☆☆ (2 sao)</option>
                                                        <option value="1">★☆☆☆☆ (1 sao)</option>
                                                    </select>
                                                </div>
                                            </div>

                                            <!-- Status Filter -->
                                            <div class="col-6 col-md-3 col-lg-2">
                                                <div class="input-group">
                                                    <span class="input-group-text">
                                                        <i class="fas fa-filter"></i>
                                                    </span>
                                                    <select class="form-select" name="filterStatus">
                                                        <option value="-1">Trạng Thái</option>
                                                        <option value="0">Đã Phê Duyệt</option>
                                                        <option value="1">Đang Chờ Duyệt</option>
                                                        <option value="2">Bị Từ Chối</option>
                                                    </select>
                                                </div>
                                            </div>

                                            <!-- Action Buttons -->
                                            <div class="col-12 col-md-2 col-lg-5">
                                                <div class="d-flex gap-2 justify-content-md-end flex-wrap">
                                                    <button type="submit" 
                                                            class="btn btn-primary flex-grow-1 flex-md-grow-0"
                                                            title="Tìm kiếm">
                                                        <i class="fas fa-search me-1"></i>
                                                        <span class="d-none d-md-inline">Tìm kiếm</span>
                                                    </button>

                                                    <a href="ReviewManagementServlet" 
                                                       class="btn btn-outline-secondary flex-grow-1 flex-md-grow-0"
                                                       title="Đặt lại bộ lọc">
                                                        <i class="fas fa-sync me-1"></i>
                                                        <span class="d-none d-md-inline">Đặt lại</span>
                                                    </a>

                                                    <c:if test="${sessionScope.roleID == 1}">
                                                        <a href="ReviewManagementServlet?action=trash" 
                                                           class="btn btn-outline-danger flex-grow-1 flex-md-grow-0"
                                                           title="Xem thùng rác">
                                                            <i class="fas fa-trash me-1"></i>
                                                            <span class="d-none d-md-inline">Thùng rác</span>
                                                        </a>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>

                                    </form>
                                    <!-- Review Table -->
                                    <div class="table-responsive">
                                        <%@include file="Component/PaginationReview.jsp" %>
                                        <table class="table table-bordered table-striped table-hover">
                                            <thead class="table-dark text-center">
                                                <tr>
                                                    <th>Mã</th>
                                                    <th>Mã Khách Hàng</th>
                                                    <th>Mã Sản Phẩm</th>
                                                    <th>IMG</th>
                                                    <th>Xếp hạng</th>
                                                    <th>Bình luận</th>
                                                    <th>Trạng Thái</th>
                                                    <th>Đánh giá vào lúc</th>
                                                    <th>Hành động</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:choose>
                                                    <c:when test="${empty reviews}">
                                                        <tr>
                                                            <td colspan="9" class="text-center text-muted">Không tìm thấy đánh giá nào.</td>
                                                        </tr>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach var="s" items="${reviews}">
                                                            <tr>
                                                                <td>${s.reviewID}</td>
                                                                <td>${s.accountID}</td>
                                                                <td>${s.productID}</td>
                                                                <td style="width: 50px; height: 80px; overflow: hidden; text-align: center;">
                                                                    <c:if test="${not empty s.imgReview}">
                                                                        <img src="${pageContext.request.contextPath}/${s.imgReview}" 
                                                                             alt="Review Image"
                                                                             class="mt-2 rounded" 
                                                                             style="width: 100%; height: 100%; object-fit: cover;">
                                                                    </c:if>
                                                                </td>
                                                                <td style="
                                                                    width: 125px;
                                                                    ">
                                                                    <c:forEach var="i" begin="1" end="${s.rating}">
                                                                        <i  class="fas fa-star text-warning "></i>
                                                                    </c:forEach>
                                                                    <c:forEach var="i" begin="${s.rating + 1}" end="5">
                                                                        <i class="far fa-star text-secondary"></i>
                                                                    </c:forEach>
                                                                </td>
                                                                <td>${s.comment}</td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${s.isDeleted eq 1}">
                                                                            <span class="badge bg-danger">Đã xóa</span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <c:choose>
                                                                                <c:when test="${s.status == 0}">
                                                                                    <span class="badge bg-success">Đã Phê Duyệt</span>
                                                                                </c:when>
                                                                                <c:when test="${s.status == 1}">
                                                                                    <span class="badge bg-warning">Đang Chờ Duyệt</span>
                                                                                </c:when>
                                                                                <c:otherwise>
                                                                                    <span class="badge bg-danger">Bị Từ Chối</span>
                                                                                </c:otherwise>
                                                                            </c:choose>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                <td>
                                                                    <fmt:formatDate value="${s.reviewAt}" pattern="dd-MM-yyyy" />
                                                                </td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${s.isDeleted eq 1}">
                                                                            <button class="btn btn-sm btn-success"
                                                                                    onclick="location.href = 'ReviewManagementServlet?action=restore&reviewID=${s.reviewID}'">
                                                                                Khôi Phục
                                                                            </button>
                                                                            <button type="button" class="btn btn-sm btn-danger"
                                                                                    data-bs-toggle="modal"
                                                                                    data-bs-target="#confirmHardDeleteModal"
                                                                                    onclick="setHardDeleteReviewID(${s.reviewID})">
                                                                                <i class="fas fa-trash"></i>
                                                                            </button>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <button class="btn btn-sm btn-warning"
                                                                                    data-bs-toggle="modal"
                                                                                    data-bs-target="#editStatusReviewModal${s.reviewID}">
                                                                                <i class="fas fa-edit"></i>
                                                                            </button>
                                                                            <button type="button" class="btn btn-sm btn-danger"
                                                                                    data-bs-toggle="modal"
                                                                                    data-bs-target="#confirmSoftDeleteModal"
                                                                                    onclick="setSoftDeleteReviewID(${s.reviewID})">
                                                                                <i class="fas fa-trash"></i>
                                                                            </button>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                            </tr>

                                                            <!-- Edit Review Modal -->
                                                        <div class="modal fade" id="editStatusReviewModal${s.reviewID}">
                                                            <div class="modal-dialog modal-lg">
                                                                <div class="modal-content">
                                                                    <form action="ReviewManagementServlet" method="POST">
                                                                        <input type="hidden" name="action" value="update">
                                                                        <input type="hidden" name="reviewID" value="${s.reviewID}">
                                                                        <div class="modal-header">
                                                                            <h5 class="modal-title">Chỉnh sửa trạng thái đánh giá</h5>
                                                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                                        </div>
                                                                        <div class="modal-body">
                                                                            <div class="mb-3">
                                                                                <label class="form-label">Trạng Thái Đánh Giá</label>
                                                                                <select class="form-select" name="status">
                                                                                    <option value="0" ${s.status == 0 ? 'selected' : ''}>Phê Duyệt</option>
                                                                                    <option value="1" ${s.status == 1 ? 'selected' : ''}>Chờ Duyệt</option>
                                                                                    <option value="2" ${s.status == 2 ? 'selected' : ''}>Từ Chối</option>
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
            <!-- Error Message Modal -->
            <div class="modal fade" id="errorModal" tabindex="-1" aria-labelledby="errorModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header bg-danger text-white">
                            <h5 class="modal-title" id="errorModalLabel">Trạng thái Thất Bại</h5>
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

            <!-- Success Message Modal -->
            <div class="modal fade" id="successModal" tabindex="-1" aria-labelledby="successModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header bg-success text-white">
                            <h5 class="modal-title" id="successModalLabel">Trạng Thái Thành Công</h5>
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

            <!-- Soft Delete Modal -->
            <div class="modal fade" id="confirmSoftDeleteModal" tabindex="-1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Xác nhận Chuyển vào Thùng rác</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            Bạn có chắc chắn muốn đưa đánh giá sản phẩm này vào thùng rác !
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                            <form method="POST" action="ReviewManagementServlet">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="reviewID" id="softDeleteReviewID">
                                <button type="submit" class="btn btn-danger">Chuyển vào thùng rác</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Hard Delete Modal -->
            <div class="modal fade" id="confirmHardDeleteModal" tabindex="-1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Xác nhận xóa vĩnh viễn</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            Bạn có chắc chắn muốn xóa vĩnh viễn đánh giá này !
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                            <form method="POST" action="ReviewManagementServlet">
                                <input type="hidden" name="action" value="hardDelete">
                                <input type="hidden" name="reviewID" id="hardDeleteReviewID">
                                <button type="submit" class="btn btn-danger">Xóa vĩnh viễn</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <!-- JavaScript -->
            <script>
                function setSoftDeleteReviewID(reviewID) {
                    document.getElementById("softDeleteReviewID").value = reviewID;
                }

                function setHardDeleteReviewID(reviewID) {
                    document.getElementById("hardDeleteReviewID").value = reviewID;
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
                        z
                <% session.removeAttribute("successMessage"); %>
                    }
                };
            </script>
    </body>
</html>