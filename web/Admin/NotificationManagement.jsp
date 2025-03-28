<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="" />
        <meta name="author" content="" />
        <title>Dashboard - Notification Management</title>

        <!-- Simple Datatables CSS (nếu dùng) -->
        <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" crossorigin="anonymous">
        <!-- Font Awesome -->
        <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
        <!-- Quill CSS -->
        <link href="https://cdn.quilljs.com/1.3.6/quill.snow.css" rel="stylesheet">
        <!-- Custom CSS của bạn -->
        <link rel="stylesheet" href="CSS/style.css" />
        <!-- Tùy chọn: bạn có thể thêm script toggle sidebar, v.v. -->
        <script src="JS/SideBarToggle.js"></script>
    </head>
    <body class="sb-nav-fixed">
        <c:choose>
            <c:when test="${empty sessionScope.roleID }">
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
                            <h1 class="mt-4">Quản Lí Thông Báo</h1>

                            <div class="card mb-4">
                                <div class="card-header">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div>
                                            <i class="fas fa-bell me-1"></i> Danh Sách Thông Báo
                                        </div>
                                        <div>
                                            <!-- Nút mở modal Add Notification -->
                                            <button class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#addNotificationModal">
                                                <i class="fas fa-plus"></i> Thêm Thông Báo
                                            </button>
                                        </div>
                                    </div>
                                </div>

                                <div class="card-body">
                                    <!-- Search Form -->
                                    <form action="NotificationServlet" method="GET" class="mb-4">
                                        <div class="input-group">
                                            <input type="hidden" name="action" value="search">
                                            <input type="text" name="search" class="form-control" placeholder="Tìm Kiếm Thông Báo......" 
                                                   value="${param.search}">
                                            <button class="btn btn-outline-secondary" type="submit">
                                                <i class="fas fa-search"></i>
                                            </button>
                                            <a href="NotificationServlet?action=list" class="btn btn-outline-danger">
                                                <i class="fas fa-sync"></i>
                                            </a>
                                            <c:if test="${sessionScope.roleID == 1}">
                                                <a href="NotificationServlet?action=listDeleted" class="btn btn-outline-danger">
                                                    <i class="fas fa-trash"></i>
                                                </a>
                                            </c:if>
                                        </div>
                                    </form>

                                    <!-- Notification Table -->
                                    <div class="table-responsive">
                                        <%@ include file="Component/PaginationNotification.jsp" %>
                                        <table class="table table-bordered table-striped table-hover">
                                            <thead class="table-dark text-center">
                                                <tr>
                                                    <th>Mã</th>
                                                    <th>Tiêu Đề</th>
                                                    <th>Nội Dung</th>
                                                    <th>Thể Loại</th>
                                                    <th>Trạng Thái</th>
                                                    <th>Mã Tài Khoản</th>
                                                    <th>Ngày Tạo</th>
                                                    <th>Hành Động</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:choose>
                                                    <c:when test="${empty notifications}">
                                                        <tr>
                                                            <td colspan="8" class="text-center text-muted">Không Có Thông Báo Khả Dụng</td>
                                                        </tr>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach var="notification" items="${notifications}">
                                                            <tr class="${notification.isDeleted ? 'deleted-row' : ''}">
                                                                <td>${notification.notificationID}</td>
                                                                <td>${notification.title}</td>
                                                                <td>
                                                                    <!-- 
                                                                        RÚT GỌN NỘI DUNG: 
                                                                        Nếu <= 100 ký tự, hiển thị tất cả
                                                                        Nếu > 100 ký tự, hiển thị 100 ký tự + nút "View More" 
                                                                    -->
                                                                    <c:choose>
                                                                        <c:when test="${fn:length(notification.content) <= 100}">
                                                                            <button class="btn btn-link btn-sm"
                                                                                    data-bs-toggle="modal"
                                                                                    data-bs-target="#viewContentModal${notification.notificationID}">
                                                                                Xem Thêm
                                                                            </button>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <button class="btn btn-link btn-sm"
                                                                                    data-bs-toggle="modal"
                                                                                    data-bs-target="#viewContentModal${notification.notificationID}">
                                                                                Xem Thêm
                                                                            </button>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                <td>${notification.type}</td>
                                                                <td>${notification.status}</td>
                                                                <td>${notification.accountID != null ? notification.accountID : 'N/A'}</td>
                                                                <td>${notification.createdAt}</td>
                                                                <td>
                                                                    <!-- Nếu chưa bị xóa -->
                                                                    <c:if test="${!notification.isDeleted}">
                                                                        <button class="btn btn-sm btn-warning"
                                                                                data-bs-toggle="modal"
                                                                                data-bs-target="#editNotificationModal${notification.notificationID}">
                                                                            <i class="fas fa-edit"></i>
                                                                        </button>
                                                                        <button type="button" class="btn btn-sm btn-danger"
                                                                                data-bs-toggle="modal"
                                                                                data-bs-target="#confirmSoftDeleteModal"
                                                                                onclick="setSoftDeleteNotificationID(${notification.notificationID})">
                                                                            <i class="fas fa-trash"></i>
                                                                        </button>
                                                                        <!-- Thêm nút Send Notification -->
                                                                        <button type="button" class="btn btn-sm btn-success"
                                                                                data-bs-toggle="modal"
                                                                                data-bs-target="#sendNotificationModal${notification.notificationID}"
                                                                                onclick="setSendNotificationID(${notification.notificationID})">
                                                                            <i class="fas fa-paper-plane"></i>
                                                                        </button>
                                                                    </c:if>
                                                                    <!-- Nếu đã bị xóa -->
                                                                    <c:if test="${notification.isDeleted}">
                                                                        <button class="btn btn-sm btn-success"
                                                                                onclick="location.href = 'NotificationServlet?action=restore&notificationID=${notification.notificationID}'">
                                                                            Restore
                                                                        </button>
                                                                        <button type="button" class="btn btn-sm btn-danger"
                                                                                data-bs-toggle="modal"
                                                                                data-bs-target="#confirmHardDeleteModal"
                                                                                onclick="setHardDeleteNotificationID(${notification.notificationID})">
                                                                            <i class="fas fa-trash"></i>
                                                                        </button>
                                                                    </c:if>
                                                                </td>
                                                            </tr>

                                                            <!-- Modal: Xem toàn bộ nội dung (View More) -->
                                                        <div class="modal fade" id="viewContentModal${notification.notificationID}" tabindex="-1" aria-hidden="true">
                                                            <div class="modal-dialog modal-lg">
                                                                <div class="modal-content">
                                                                    <div class="modal-header">
                                                                        <h5 class="modal-title">Nội Dung Thông Báo</h5>
                                                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                                    </div>
                                                                    <div class="modal-body">
                                                                        <!-- Hiển thị toàn bộ nội dung -->
                                                                        <c:out value="${notification.content}" escapeXml="false" />
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <!-- Send Notification Modal -->


                                                        <div class="modal fade" id="sendNotificationModal${notification.notificationID}" tabindex="-1" aria-hidden="true">
                                                            <div class="modal-dialog modal-lg">
                                                                <div class="modal-content">
                                                                    <div class="modal-header">
                                                                        <h5 class="modal-title">Gửi Thông Báo</h5>
                                                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                                    </div>
                                                                    <div class="modal-body">
                                                                        <input type="hidden" id="sendNotificationID${notification.notificationID}" value="${notification.notificationID}">
                                                                        <div class="mb-3">
                                                                            <label class="form-label">Lựa Chọn Gửi:</label>
                                                                            <div>
<!--                                                                                <button type="button" class="btn btn-primary" onclick="sendToAllCustomers(${notification.notificationID})">
                                                                                    Gửi thông báo đến tất cả khách hàng
                                                                                </button>-->
                                                                                <button type="button" class="btn btn-secondary" data-bs-toggle="collapse" data-bs-target="#selectCustomers${notification.notificationID}">
                                                                                    Chọn khach hàng nhận thông báo
                                                                                </button>
                                                                            </div>
                                                                        </div>
                                                                        <div class="collapse" id="selectCustomers${notification.notificationID}">
                                                                            <label class="form-label">Select Customers:</label>
                                                                            <form id="sendSpecificForm${notification.notificationID}" action="NotificationServlet" method="POST">
                                                                                <input type="hidden" name="action" value="sendSpecific">
                                                                                <input type="hidden" name="notificationID" value="${notification.notificationID}">
                                                                                <select class="form-control" name="accountIDs" multiple required>
                                                                                    <c:forEach var="account" items="${accounts}">
                                                                                        <c:if test="${account.roleID == 3}">
                                                                                            <option value="${account.accountId}">${account.userName} (ID: ${account.accountId})</option>
                                                                                        </c:if>
                                                                                    </c:forEach>
                                                                                </select>
                                                                                <button type="submit" class="btn btn-primary mt-2">Gửi</button>
                                                                            </form>
                                                                        </div>
                                                                    </div>
                                                                    <div class="modal-footer">
                                                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <!-- Edit Notification Modal -->
                                                        <div class="modal fade" id="editNotificationModal${notification.notificationID}">
                                                            <div class="modal-dialog modal-lg">
                                                                <div class="modal-content">
                                                                    <form action="NotificationServlet" method="POST">
                                                                        <input type="hidden" name="action" value="update">
                                                                        <input type="hidden" name="notificationID" value="${notification.notificationID}">
                                                                        <div class="modal-header">
                                                                            <h5 class="modal-title">Cập Nhật Thông Báo</h5>
                                                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                                        </div>
                                                                        <div class="modal-body">
                                                                            <div class="mb-3">
                                                                                <label class="form-label">Tiêu Đề</label>
                                                                                <input type="text" class="form-control" name="title"
                                                                                       value="${notification.title}" required maxlength="255">
                                                                            </div>

                                                                            <!-- Phần content dùng Quill -->
                                                                            <div class="mb-3">
                                                                                <label class="form-label">Nội Dung</label>
                                                                                <!-- Quill container -->
                                                                                <div id="editor-container-edit-${notification.notificationID}"
                                                                                     style="height: 200px; background-color: #fff;"></div>
                                                                                <!-- Input ẩn để lưu nội dung -->
                                                                                <input type="hidden" id="content-edit-${notification.notificationID}"
                                                                                       name="content">
                                                                            </div>

                                                                            <div class="mb-3">
                                                                                <label class="form-label">Kiểu Loại</label>
                                                                                <select class="form-control" name="type" required>
                                                                                    <option value="System" ${notification.type == 'System' ? 'selected' : ''}>Hệ Thống</option>
                                                                                    <option value="Promotional" ${notification.type == 'Promotional' ? 'selected' : ''}>Khuyến Mãi</option>
                                                                                    <option value="User" ${notification.type == 'User' ? 'selected' : ''}>Người Dùng</option>
                                                                                </select>
                                                                            </div>
                                                                            <div class="mb-3">
                                                                                <label class="form-label">Trạng Thái</label>
                                                                                <select class="form-control" name="status" required>
                                                                                    <option value="Read" ${notification.status == 'Read' ? 'selected' : ''}>Đã Đọc </option>
                                                                                    <option value="Unread" ${notification.status == 'Unread' ? 'selected' : ''}>Chưa Đọc</option>
                                                                                </select>
                                                                            </div>
                                                                        </div>
                                                                        <div class="modal-footer">
                                                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                                                                            <button type="submit" class="btn btn-primary">Cập Nhật</button>
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

        <!-- Add Notification Modal -->
        <div class="modal fade" id="addNotificationModal">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <form action="NotificationServlet" method="POST">
                        <input type="hidden" name="action" value="add">
                        <div class="modal-header">
                            <h5 class="modal-title">Thêm Thông Báo Mới</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <div class="mb-3">
                                <label for="title" class="form-label">Tiêu Đề</label>
                                <input type="text" class="form-control" id="title" name="title" required maxlength="255">
                            </div>

                            <!-- Phần content dùng Quill -->
                            <div class="mb-3">
                                <label class="form-label">Nội Dung</label>
                                <!-- Quill container -->
                                <div id="editor-container" style="height: 200px; background-color: #fff;"></div>
                                <!-- Input ẩn để lưu nội dung -->
                                <input type="hidden" id="content" name="content">
                            </div>

                            <div class="mb-3">
                                <label for="type" class="form-label">Thể Loại</label>
                                <select class="form-control" id="type" name="type" required>
                                    <option value="System">Hệ Thống</option>
                                    <option value="Promotional">Khuyến Mãi</option>
                                    <option value="User">Người Dùng</option>
                                </select>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                            <button type="submit" class="btn btn-primary">Thêm Thông Báo</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Update Notification Modal -->
        <div class="modal fade" id="updateNotificationModal">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <form action="NotificationServlet" method="POST">
                        <input type="hidden" name="action" value="update">
                        <!-- Hidden input chứa ID của notification cần cập nhật -->
                        <input type="hidden" id="notificationID" name="notificationID" value="${notification.notificationID}">
                        <div class="modal-header">
                            <h5 class="modal-title">Update Notification</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <div class="mb-3">
                                <label for="updateTitle" class="form-label">Title</label>
                                <input type="text" class="form-control" id="updateTitle" name="title" required maxlength="255" value="${notification.title}">
                            </div>

                            <!-- Phần content dùng Quill -->
                            <div class="mb-3">
                                <label class="form-label">Content</label>
                                <!-- Quill container với nội dung hiện có -->
                                <div id="editor-container-update" style="height: 200px; background-color: #fff;">
                                    ${notification.content}
                                </div>
                                <!-- Input ẩn để lưu nội dung cập nhật từ Quill -->
                                <input type="hidden" id="updateContent" name="content" value="${notification.content}">
                            </div>

                            <div class="mb-3">
                                <label for="updateType" class="form-label">Type</label>
                                <select class="form-control" id="updateType" name="type" required>
                                    <option value="System" ${notification.type == 'System' ? 'selected' : ''}>System</option>
                                    <option value="Promotional" ${notification.type == 'Promotional' ? 'selected' : ''}>Promotional</option>
                                    <option value="User" ${notification.type == 'User' ? 'selected' : ''}>User</option>
                                </select>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button type="submit" class="btn btn-primary">Update Notification</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Soft Delete Modal -->
        <div class="modal fade" id="confirmSoftDeleteModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Xác nhận xóa</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        Thông báo này sẽ được chuyển vào thùng rác!
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                        <form method="POST" action="NotificationServlet">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="notificationID" id="softDeleteNotificationID">
                            <button type="submit" class="btn btn-danger">
                                <i class="fas fa-trash"></i>
                            </button>
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
                        Thông báo này sẽ được xóa vĩnh viễn!
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                        <form method="POST" action="NotificationServlet">
                            <input type="hidden" name="action" value="hardDelete">
                            <input type="hidden" name="notificationID" id="hardDeleteNotificationID">
                            <button type="submit" class="btn btn-danger">
                                <i class="fas fa-trash"></i>
                            </button>
                        </form>
                    </div>
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

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>

        <!-- Quill JS -->
        <script src="https://cdn.quilljs.com/1.3.6/quill.js"></script>

        <script>
                                                                                    // =========================
                                                                                    //  Quill cho Add Modal
                                                                                    // =========================
                                                                                    const quillAdd = new Quill('#editor-container', {
                                                                                        theme: 'snow',
                                                                                        modules: {
                                                                                            toolbar: [
                                                                                                ['bold', 'italic', 'underline', 'strike'],
                                                                                                [{'header': [1, 2, 3, false]}],
                                                                                                [{'list': 'ordered'}, {'list': 'bullet'}],
                                                                                                [{'script': 'sub'}, {'script': 'super'}],
                                                                                                [{'indent': '-1'}, {'indent': '+1'}],
                                                                                                [{'direction': 'rtl'}],
                                                                                                [{'size': ['small', false, 'large', 'huge']}],
                                                                                                [{'header': [1, 2, 3, 4, 5, 6, false]}],
                                                                                                [{'color': []}, {'background': []}],
                                                                                                [{'font': []}],
                                                                                                [{'align': []}],
                                                                                                ['link'],
                                                                                                ['clean']
                                                                                            ]
                                                                                        },
                                                                                        placeholder: 'Describe your product in detail...'
                                                                                    });

                                                                                    // Lấy form Add Notification
                                                                                    var formAdd = document.querySelector('#addNotificationModal form');
                                                                                    formAdd.onsubmit = function () {
                                                                                        // Gán nội dung Quill vào input ẩn
                                                                                        var contentInput = this.querySelector('input[name="content"]');
                                                                                        contentInput.value = quillAdd.root.innerHTML;
                                                                                    };

                                                                                    // =========================
                                                                                    //  Quill cho Edit Modals
                                                                                    // =========================
            <c:forEach var="notification" items="${notifications}">
                <c:if test="${!notification.isDeleted}">
                                                                                    const quillEdit${notification.notificationID} = new Quill('#editor-container-edit-${notification.notificationID}', {
                                                                                        theme: 'snow',
                                                                                        modules: {
                                                                                            toolbar: [
                                                                                                ['bold', 'italic', 'underline', 'strike'],
                                                                                                [{'header': [1, 2, 3, false]}],
                                                                                                [{'list': 'ordered'}, {'list': 'bullet'}],
                                                                                                [{'script': 'sub'}, {'script': 'super'}],
                                                                                                [{'indent': '-1'}, {'indent': '+1'}],
                                                                                                [{'direction': 'rtl'}],
                                                                                                [{'size': ['small', false, 'large', 'huge']}],
                                                                                                [{'header': [1, 2, 3, 4, 5, 6, false]}],
                                                                                                [{'color': []}, {'background': []}],
                                                                                                [{'font': []}],
                                                                                                [{'align': []}],
                                                                                                ['link'],
                                                                                                ['clean']
                                                                                            ]
                                                                                        },
                                                                                        placeholder: 'Describe your notification in detail...'
                                                                                    });

                                                                                    // Set nội dung ban đầu cho editor (tránh lỗi escape HTML)
                                                                                    quillEdit${notification.notificationID}.root.innerHTML = '${notification.content}'
                                                                                            .replace(/&amp;/g, '&')
                                                                                            .replace(/&lt;/g, '<')
                                                                                            .replace(/&gt;/g, '>');

                                                                                    // Xử lý submit form Edit
                                                                                    document.querySelector('#editNotificationModal${notification.notificationID} form').onsubmit = function () {
                                                                                        const contentInput = document.getElementById('content-edit-${notification.notificationID}');
                                                                                        contentInput.value = quillEdit${notification.notificationID}.root.innerHTML;
                                                                                    };
                </c:if>
            </c:forEach>

                                                                                    // Hàm set ID cho Soft Delete
                                                                                    function setSoftDeleteNotificationID(notificationID) {
                                                                                        document.getElementById("softDeleteNotificationID").value = notificationID;
                                                                                    }

                                                                                    // Hàm set ID cho Hard Delete
                                                                                    function setHardDeleteNotificationID(notificationID) {
                                                                                        document.getElementById("hardDeleteNotificationID").value = notificationID;
                                                                                    }


        </script>
        <script>
            window.onload = function () {
                const errorMessage = "${sessionScope.errorMessage}";
                if (errorMessage && errorMessage.trim() !== "") {
                    // Nếu có nội dung lỗi trong session => mở modal
                    const errorModal = new bootstrap.Modal(document.getElementById("errorModal"));
                    errorModal.show();
            <% request.getSession().removeAttribute("errorMessage"); %>
                }

                const successMessage = "${sessionScope.successMessage}";
                if (successMessage && successMessage.trim() !== "") {
                    const successModal = new bootstrap.Modal(document.getElementById("successModal"));
                    successModal.show();
            <% request.getSession().removeAttribute("successMessage"); %>
                }
            };
        </script>

        <script>
            function setSendNotificationID(notificationID) {
                document.getElementById("sendNotificationID" + notificationID).value = notificationID;
            }

            function sendToAllCustomers(notificationID) {
                if (confirm("Are you sure you want to send this notification to all customers?")) {
                    fetch('NotificationServlet?action=sendToAll&notificationID=' + notificationID, {
                        method: 'POST'
                    })
                            .then(response => response.text())
                            .then(data => {
                                alert(data);
                                location.reload();
                            })
                            .catch(error => {
                                alert("Error: " + error);
                            });
                }
            }
        </script>

    </body>
</html>
