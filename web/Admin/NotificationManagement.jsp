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
        <title>Dashboard - Notification Management</title>
        <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="CSS/style.css" />
        <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script src="JS/SideBarToggle.js"></script>
    </head>
    <body class="sb-nav-fixed">
        <%@ include file="Component/SideBar.jsp" %>
        <div id="layoutSidenav">
            <div id="layoutSidenav_content">
                <div class="dashboard-container">
                    <main>
                        <div class="container-fluid px-5">
                            <h1 class="mt-4">Notification Management</h1>

                            <div class="card mb-4">
                                <div class="card-header">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div>
                                            <i class="fas fa-bell me-1"></i> Notifications List
                                        </div>
                                        <div>
                                            <button class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#addNotificationModal">
                                                <i class="fas fa-plus"></i> Add Notification
                                            </button>
                                        </div>
                                    </div>
                                </div>

                                <div class="card-body">
                                    <!-- Search Form -->
                                    <form action="NotificationServlet" method="GET" class="mb-4">
                                        <div class="input-group">
                                            <input type="hidden" name="action" value="search">
                                            <input type="text" name="search" class="form-control" placeholder="Search Notification by Title, Content..." 
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
                                        <table class="table table-bordered table-striped table-hover">
                                            <thead class="table-dark text-center">
                                                <tr>
                                                    <th>ID</th>
                                                    <th>Title</th>
                                                    <th>Content</th>
                                                    <th>Type</th>
                                                    <th>Status</th>
                                                    <th>Account ID</th>
                                                    <th>Created At</th>
                                                    <th>Actions</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:choose>
                                                    <c:when test="${empty notifications}">
                                                        <tr>
                                                            <td colspan="8" class="text-center text-muted">No notifications found</td>
                                                        </tr>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach var="notification" items="${notifications}">
                                                            <tr class="${notification.isDeleted ? 'deleted-row' : ''}">
                                                                <td>${notification.notificationID}</td>
                                                                <td>${notification.title}</td>
                                                                <td>${notification.content}</td>
                                                                <td>${notification.type}</td>
                                                                <td>${notification.status}</td>
                                                                <td>${notification.accountID != null ? notification.accountID : 'N/A'}</td>
                                                                <td>${notification.createdAt}</td>
                                                                <td>
                                                                    <!-- If not deleted: Edit & Soft Delete -->
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
                                                                    </c:if>
                                                                    <!-- If deleted: Restore & Hard Delete -->
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

                                                            <!-- Edit Notification Modal -->
                                                            <div class="modal fade" id="editNotificationModal${notification.notificationID}">
                                                                <div class="modal-dialog">
                                                                    <div class="modal-content">
                                                                        <form action="NotificationServlet" method="POST">
                                                                            <input type="hidden" name="action" value="update">
                                                                            <input type="hidden" name="notificationID" value="${notification.notificationID}">
                                                                            <div class="modal-header">
                                                                                <h5 class="modal-title">Edit Notification</h5>
                                                                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                                            </div>
                                                                            <div class="modal-body">
                                                                                <div class="mb-3">
                                                                                    <label class="form-label">Title</label>
                                                                                    <input type="text" class="form-control" name="title" value="${notification.title}" required maxlength="255">
                                                                                </div>
                                                                                <div class="mb-3">
                                                                                    <label class="form-label" >Content</label>
                                                                                    <textarea class="form-control" name="content" rows="3">${notification.content}</textarea>
                                                                                </div>
                                                                                <div class="mb-3">
                                                                                    <label class="form-label">Type</label>
                                                                                    <select class="form-control" name="type" required>
                                                                                        <option value="System" ${notification.type == 'System' ? 'selected' : ''}>System</option>
                                                                                        <option value="Promotional" ${notification.type == 'Promotional' ? 'selected' : ''}>Promotional</option>
                                                                                        <option value="User" ${notification.type == 'User' ? 'selected' : ''}>User</option>
                                                                                    </select>
                                                                                </div>
                                                                                <div class="mb-3">
                                                                                    <label class="form-label">Status</label>
                                                                                    <select class="form-control" name="status" required>
                                                                                        <option value="Read" ${notification.status == 'Read' ? 'selected' : ''}>Read</option>
                                                                                        <option value="Unread" ${notification.status == 'Unread' ? 'selected' : ''}>Unread</option>
                                                                                    </select>
                                                                                </div>
                                                                                <div class="mb-3">
                                                                                    <label class="form-label">Account ID (Optional)</label>
                                                                                    <input type="number" class="form-control" name="accountID" value="${notification.accountID}" min="1">
                                                                                </div>
                                                                            </div>
                                                                            <div class="modal-footer">
                                                                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                                                                <button type="submit" class="btn btn-primary">Update</button>
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
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="NotificationServlet" method="POST">
                        <input type="hidden" name="action" value="add">
                        <div class="modal-header">
                            <h5 class="modal-title">Add New Notification</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <div class="mb-3">
                                <label for="title" class="form-label">Title</label>
                                <input type="text" class="form-control" id="title" name="title" required maxlength="255">
                            </div>
                            <div class="mb-3">
                                <label for="content" class="form-label">Content</label>
                                <textarea class="form-control" id="content" name="content" rows="3"></textarea>
                            </div>
                            <div class="mb-3">
                                <label for="type" class="form-label">Type</label>
                                <select class="form-control" id="type" name="type" required>
                                    <option value="System">System</option>
                                    <option value="Promotional">Promotional</option>
                                    <option value="User">User</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="accountID" class="form-label">Account ID (Optional)</label>
                                <input type="number" class="form-control" id="accountID" name="accountID" min="1">
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button type="submit" class="btn btn-primary">Add Notification</button>
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
                        <h5 class="modal-title">Confirm Deletion</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        This notification will be moved to trash!
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
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
                        <h5 class="modal-title">Confirm Permanent Deletion</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        This notification will be permanently deleted!
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
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
                        <h5 class="modal-title" id="errorModalLabel">Error</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body text-dark">
                        ${sessionScope.errorMessage}
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
                        <h5 class="modal-title" id="successModalLabel">Success</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body text-dark">
                        ${sessionScope.successMessage}
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>

         <script>
            // Initialize Quill for Add Modal
           const quill = new Quill('#editor-container', {
    theme: 'snow',
    modules: {
        toolbar: [
            ['bold', 'italic', 'underline', 'strike'], // In đậm, nghiêng, gạch chân, gạch ngang
            [{'header': [1, 2, 3, false]}], // Tiêu đề (Heading)
            [{'list': 'ordered'}, {'list': 'bullet'}], // Danh sách có thứ tự và không thứ tự
            [{'script': 'sub'}, {'script': 'super'}], // Chỉ số trên, chỉ số dưới
            [{'indent': '-1'}, {'indent': '+1'}], // Thụt lề trái, phải
            [{'direction': 'rtl'}], // Hướng văn bản (trái sang phải, phải sang trái)
            [{'size': ['small', false, 'large', 'huge']}], // Kích thước chữ
            [{'header': [1, 2, 3, 4, 5, 6, false]}], // Cỡ tiêu đề
            [{'color': []}, {'background': []}], // Màu chữ, màu nền
            [{'font': []}], // Font chữ
            [{'align': []}], // Căn chỉnh văn bản
            ['link'], // Chèn link
            ['clean']                                         // Xóa định dạng
        ]
    },
    placeholder: 'Describe your product in detail...'

});
var form = document.querySelector('form');
form.onsubmit = function () {
    var descriptionInput = document.querySelector('input[name="description"]');
    descriptionInput.value = quill.root.innerHTML;
};
            // Initialize Quill for Edit Modals
            <c:forEach var="notification" items="${notifications}">
                <c:if test="${notification.isDeleted == 0}">
                    
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

                        // Set initial content for Edit
                        quillEdit${notification.notificationID}.root.innerHTML = '${notification.content}'.replace(/&/g, '&').replace(/</g, '<').replace(/>/g, '>');

                        // Handle form submission for Edit
                        document.querySelector('#editNotificationModal${notification.notificationID} form').onsubmit = function () {
                            const contentInput = document.getElementById('content-edit-${notification.notificationID}');
                            contentInput.value = quillEdit${notification.notificationID}.root.innerHTML;
                        };
                </c:if>
            </c:forEach>

            function setSoftDeleteNotificationID(notificationID) {
                document.getElementById("softDeleteNotificationID").value = notificationID;
            }

            function setHardDeleteNotificationID(notificationID) {
                document.getElementById("hardDeleteNotificationID").value = notificationID;
            }

            window.onload = function () {
                const errorMessage = "${sessionScope.errorMessage}";
                if (errorMessage && errorMessage.trim() !== "") {
                    const errorModal = new bootstrap.Modal(document.getElementById("errorModal"));
                    errorModal.show();
                    <% request.getSession().removeAttribute("errorMessage"); %>
                }

                let successMessage = "${sessionScope.successMessage}";
                if (successMessage && successMessage.trim() !== "" && successMessage.trim() !== "null") {
                    let successModal = new bootstrap.Modal(document.getElementById("successModal"));
                    successModal.show();
                    <% request.getSession().removeAttribute("successMessage"); %>
                }
            };
        </script>
    </body>
</html>