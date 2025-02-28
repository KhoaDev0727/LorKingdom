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
        <title>Dashboard - Management Admin</title>
        <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="CSS/style.css" />
        <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>

        <script src="JS/SideBarToggle.js"></script>
    </head>
    <style>
        /* Thiết lập ảnh */
        img {
            max-width: 100%;
            height: auto;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(5, 0, 0, 0.1);
            object-fit: cover;
            transition: transform 0.3s ease, background-color 0.3s ease;
            cursor: pointer;
        }

        img:hover {
            transform: scale(1.02);
        }
        img:hover::after {
            content: "";
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: black;
            z-index: -1;
        }
        .error-message {
            color: red;
            font-size: 14px;
            margin-top: 5px;
            display: block;
        }
        .input-error {
            border: 2px solid red !important;
            background-color: #ffe6e6; /* Nhẹ nhàng báo hiệu lỗi */
        }
    </style>
</style>
<body class="sb-nav-fixed">
    <c:if test="${empty sessionScope.roleID}">
        <c:redirect url="/Admin/loginPage.jsp"/>
    </c:if>
    <div id="layoutSidenav">
        <div id="layoutSidenav_content">
            <!-- Side Bar -->
            <%@ include file="Component/SideBar.jsp" %>
            <div class="dashboard-container">
                <!-- Table -->
                <main>
                    <div class="container-fluid px-5">
                        <h1 class="mt-4">Staff Management</h1>
                        <!-- Success/Error Messages -->
                        <c:if test="${not empty message}">
                            <div class="alert alert-${messageType} alert-dismissible fade show" role="alert">
                                ${message}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>

                        <div class="card mb-4">
                            <div class="card-header">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <i class="fa-solid fa-user-tie mr-1"> </i>Staff List
                                    </div>
                                    <div>
                                        <button class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#addCustomerModal">
                                            <i class="fas fa-plus"></i> Add Staff
                                        </button>
                                    </div>
                                </div>
                            </div>

                            <div class="card-body">
                                <!-- Search Form -->
                                <form action="StaffManagementServlet" method="GET" class="mb-4">
                                    <div class="input-group">
                                        <input type="hidden" name="action" value="search">
                                        <input type="text" name="search" class="form-control" placeholder="Search Staff by (ID, Phone Number, Name, Email)..." " 
                                               value="${param.search}">
                                        <button class="btn btn-outline-secondary" type="submit">
                                            <i class="fas fa-search"></i>
                                        </button>
                                        <a href="StaffManagementServlet?&action=list" class="btn btn-outline-danger">
                                            <i class="fas fa-sync"></i>
                                        </a>
                                    </div>
                                </form>

                                <!-- Staff Table -->
                                <div class="table-responsive">
                                    <%@ include file="Component/Pagination.jsp" %>
                                    <table class="table table-bordered table-striped table-hover">
                                        <thead class="table-dark text-center">
                                            <tr>
                                                <th>ID</th>
                                                <th>Avatar</th>
                                                <th>Name</th>
                                                <th>Phone Number</th>
                                                <th>Email</th>
                                                <th>Password</th>
                                                <th>Address</th>
                                                <th>Role Name</th>
                                                <th>Created At</th>
                                                <th>Updated At</th>
                                                <th>Status</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${empty staffs}">
                                                    <tr>
                                                        <td colspan="12" class="text-center text-muted">No staffs found</td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="s" items="${staffs}">
                                                        <tr>
                                                            <td>${s.accountId}</td>
                                                            <td style="width: 50px; height: 80px; overflow: hidden; text-align: center;">
                                                                <c:if test="${not empty s.image}">
                                                                    <img src="${pageContext.request.contextPath}/${s.image}" 
                                                                         alt="Profile Image"
                                                                         class="mt-2 rounded" 
                                                                         style="width: 100%; height: 100%; object-fit: cover;"
                                                                         data-bs-toggle="tooltip" 
                                                                         data-bs-html="true"
                                                                         data-bs-title="<img src='${pageContext.request.contextPath}/${s.image}' style='max-width: 200px; max-height: 200px;'/>">
                                                                </c:if>
                                                            </td>
                                                            <td>${s.userName}</td>
                                                            <td>${s.phoneNumber}</td>
                                                            <td>${s.email}</td>
                                                            <td style="max-width: 100px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
                                                                ${s.password}
                                                            </td>
                                                            <td>${s.address}</td> 
                                                            <td>  <c:forEach var="r" items="${roles}">
                                                                    <c:if test="${s.roleID == r.roleID}">${r.name}</c:if>
                                                                </c:forEach> 
                                                            </td>
                                                            <td>
                                                                <fmt:formatDate value="${s.createdAt}" pattern="dd-MM-yyyy" />
                                                            </td>
                                                            <td>
                                                                <fmt:formatDate value="${s.updateAt}" pattern="dd-MM-yyyy" />
                                                            </td>
                                                            <td>                                                          
                                                                <c:choose>
                                                                    <c:when test="${s.isDeleted eq 1}">
                                                                        <span class="badge bg-secondary">Deleted</span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <c:choose>
                                                                            <c:when test="${s.status eq 'Inactive'}">
                                                                                <span class="badge bg-danger">Inactive</span>
                                                                            </c:when>
                                                                            <c:when test="${s.status eq 'Blocked'}">
                                                                                <span class="badge bg-warning">Blocked</span>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <span class="badge bg-success">Active</span>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td>
                                                                <button class="btn btn-sm btn-warning" data-bs-toggle="modal" data-bs-target="#editCustomerModal${s.accountId}"
                                                                        ${s.isDeleted eq 1 ? 'disabled' : ''}>
                                                                    <i class="fas fa-edit"></i>
                                                                </button>
                                                                <button class="btn btn-sm btn-danger" data-bs-toggle="modal" data-bs-target="#deleteModal${s.accountId}"
                                                                        ${s.isDeleted eq 1 ? 'disabled' : ''}>
                                                                    <i class="fas fa-trash"></i>
                                                                </button>
                                                            </td>
                                                        </tr>

                                                        <!-- Edit Customer Modal -->
                                                    <div class="modal fade" id="editCustomerModal${s.accountId}">
                                                        <div class="modal-dialog modal-lg">
                                                            <div class="modal-content">
                                                                <form action="StaffManagementServlet" method="POST" enctype="multipart/form-data">
                                                                    <input type="hidden" name="action" value="update">
                                                                    <input type="hidden" name="accountId" value="${s.accountId}">

                                                                    <div class="modal-header">
                                                                        <h5 class="modal-title">Edit Staff</h5>
                                                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                                    </div>

                                                                    <div class="modal-body">
                                                                        <div class="row g-3">
                                                                            <div class="col-12">
                                                                                <label class="form-label">Upload Profile Image</label>
                                                                                <input type="file" 
                                                                                       class="form-control" 
                                                                                       name="image" 
                                                                                       accept="image/*">
                                                                                <input type="hidden" name="currentImage" value="${s.image}">
                                                                                <c:if test="${not empty s.image}">
                                                                                    <img src="${pageContext.request.contextPath}/${s.image}" 
                                                                                         alt="Profile Image"
                                                                                         class="mt-2 rounded" 
                                                                                         style="max-height: 100px;">
                                                                                </c:if>
                                                                            </div>

                                                                            <div class="col-md-6">
                                                                                <label class="form-label">Full Name</label>
                                                                                <input type="text" 
                                                                                       class="form-control" 
                                                                                       name="userName" 
                                                                                       value="${s.userName}" 
                                                                                       required>
                                                                            </div>

                                                                            <div class="col-md-6">
                                                                                <label class="form-label">Phone Number</label>
                                                                                <input type="tel" 
                                                                                       class="form-control" 
                                                                                       name="phoneNumber" 
                                                                                       value="${s.phoneNumber}" 
                                                                                       pattern="[0-9]{10}" 
                                                                                       required>
                                                                            </div>

                                                                            <div class="col-md-6">
                                                                                <label class="form-label">Email</label>
                                                                                <input type="email" 
                                                                                       class="form-control" 
                                                                                       name="email" 
                                                                                       value="${s.email}" 
                                                                                       required>
                                                                            </div>
                                                                            <div class="col-md-6">
                                                                                <label class="form-label">Password</label>
                                                                                <input type="password" 
                                                                                       class="form-control" 
                                                                                       name="password" 
                                                                                       value="${s.password}" 
                                                                                       value="${customer.password}" onblur="checkPassword(this)" required>
                                                                                <span id="error-Pass" class="error-message"></span>
                                                                            </div>
                                                                            <div class="col-12">
                                                                                <label class="form-label">Address</label>
                                                                                <textarea class="form-control" 
                                                                                          name="address" 
                                                                                          rows="2">${s.address}</textarea>
                                                                            </div>

                                                                            <div class="col-md-4">
                                                                                <label class="form-label">Status</label>
                                                                                <select class="form-select" name="status">
                                                                                    <option value="Active" ${s.status == 'Active' ? 'selected' : ''}>Active</option>
                                                                                    <option value="Inactive" ${s.status == 'Inactive' ? 'selected' : ''}>Inactive</option>
                                                                                    <option value="Blocked" ${s.status == 'Blocked' ? 'selected' : ''}>Blocked</option>
                                                                                </select>
                                                                            </div>
                                                                            <div class="col-md-4">
                                                                                <label class="form-label">Role</label>
                                                                                <select class="form-select" name="roleID">
                                                                                    <c:forEach var="r" items="${roles}">
                                                                                        <option value="${r.roleID}" ${r.roleID == s.roleID ? 'selected' : ''}>
                                                                                            ${r.name}
                                                                                        </option>
                                                                                    </c:forEach>
                                                                                </select>
                                                                            </div>
                                                                        </div>
                                                                    </div>

                                                                    <div class="modal-footer">
                                                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                                                        <button type="submit" id="submitBtn-Cus" class="btn btn-primary" disable>Save Changes</button>
                                                                    </div>
                                                                </form>
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <!-- Delete Confirmation Modal -->
                                                    <div class="modal fade" id="deleteModal${s.accountId}">
                                                        <div class="modal-dialog">
                                                            <div class="modal-content">
                                                                <div class="modal-header">
                                                                    <h5 class="modal-title">Confirm Delete</h5>
                                                                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                                </div>
                                                                <div class="modal-body">
                                                                    Are you sure you want to delete customer: 
                                                                    <strong>${s.userName}</strong>?
                                                                </div>
                                                                <div class="modal-footer">
                                                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                                                    <form action="StaffManagementServlet" method="GET" class="d-inline">
                                                                        <input type="hidden" name="action" value="delete">
                                                                        <input type="hidden" name="accountId" value="${s.accountId}">
                                                                        <button type="submit" class="btn btn-danger">Delete</button>
                                                                    </form>
                                                                </div>
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


    <!-- Add Staff Or Admin Modal -->
    <div class="modal fade" id="addCustomerModal">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <form action="StaffManagementServlet" method="POST" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="add">
                    <div class="modal-header">
                        <h5 class="modal-title">Add New Staff</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>

                    <div class="modal-body">
                        <div class="row g-3">
                            <div class="col-12">
                                <label class="form-label">Upload Profile Image</label>
                                <input type="file" class="form-control" name="image" accept="image/*">
                                <!-- Giữ ảnh cũ nếu không upload mới -->
                                <input type="hidden" name="currentImage" value="${s.image}">
                                <c:if test="${not empty s.image}">
                                    <img src="${pageContext.request.contextPath}/${s.image}" alt="Profile Image"
                                         class="mt-2 rounded" style="max-height: 100px;">
                                </c:if>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Full Name</label>
                                <input type="text" class="form-control" name="userName" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Phone Number</label>
                                <input type="tel" class="form-control" name="phoneNumber" 
                                       pattern="[0-9]{10}" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Email</label>
                                <input type="email" class="form-control" name="email" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Password</label>
                                <input type="password" class="form-control" name="password"  onblur="checkPassword(this)" required>
                                <span id="error-Pass" class="error-message"></span>
                            </div>
                            <div class="col-12">
                                <label class="form-label">Address</label>
                                <textarea class="form-control" name="address" rows="2"></textarea>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label">Status</label>
                                <select class="form-select" name="status">
                                    <option value="Active" ${s.status == 'active' ? 'selected' : ''}>Active</option>
                                    <option value="Inactive" ${s.status == 'inactive' ? 'selected' : ''}>Inactive</option>
                                    <option value="Blocked" ${s.status == 'blocked' ? 'selected' : ''}>Blocked</option>
                                </select>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label">Role</label>
                                <select class="form-select" name="roleID">
                                    <c:forEach var="r" items="${roles}">
                                        <option value="${r.roleID}" ${r.roleID == s.roleID ? 'selected' : ''}>
                                            ${r.name}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="submit" id="submitBtn" class="btn btn-primary" disable>Add Staff</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <!-- Modal Error -->
    <div class="modal fade" id="errorModal" tabindex="-1" aria-labelledby="errorModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header bg-danger text-white">
                    <h5 class="modal-title" id="errorModalLabel">Error</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <p>${sessionScope.errorMessage}</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
</body>
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
    window.onload = function () {
        const successMessage = "${sessionScope.successMessage}";
        const errorMessage = "${sessionScope.errorMessage}";
        if (errorMessage && errorMessage.trim() !== "") {
            const errorModal = new bootstrap.Modal(document.getElementById("errorModal"));
            errorModal.show();
    <% request.getSession().removeAttribute("errorMessage"); %>
        }
        if (successMessage && successMessage.trim() !== "") {
            let successModal = new bootstrap.Modal(document.getElementById('successModal'));
            successModal.show();
    <% request.getSession().removeAttribute("successMessage"); %>
        }
    };

    document.addEventListener('DOMContentLoaded', function () {
        var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl, {
                html: true
            });
        });
    });
    const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;

    function checkPassword(input) {
        var password = input.value.trim();
        var errorMessage = input.nextElementSibling; // Lấy phần tử `<span>` ngay sau input
        var submitBtn = document.getElementById("submitBtn"); // Nút submit
        var modal = input.closest(".modal"); // Tìm modal chứa input này
        var submitBtn2 = modal ? modal.querySelector(".btn-primary") : null; // Tìm nút submit trong 

        if (!errorMessage) {
            console.error("Không tìm thấy phần tử lỗi!");
            return;
        }
        if (password === "") {
            errorMessage.textContent = "Password cannot be empty!";
            submitBtn.disabled = true;
            submitBtn2.disabled = true;
        } else if (password.length < 8) {
            errorMessage.textContent = "Password must be at least 8 characters.";
            submitBtn.disabled = true;
            submitBtn2.disabled = true;
        } else if (!regex.test(password)) {
            errorMessage.textContent = "Password must contain at least one letter, one number, and one special character.";
            submitBtn.disabled = true;
            submitBtn2.disabled = true;
        } else {
            errorMessage.textContent = ""; // Hợp lệ
            submitBtn.disabled = false;
            submitBtn2.disabled = false;
        }
    }

</script>
</body>
</html>
