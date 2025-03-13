<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="Customer Management System" />
        <meta name="author" content="" />
        <title>Customer Management</title>
        <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="CSS/style.css" />
        <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script src="JS/SideBarToggle.js"></script>
    </head>
    <style>
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
                <!-- Side Bar -->
                <%@ include file="Component/SideBar.jsp" %>
                <div class="dashboard-container">
                    <!-- Table -->
                    <main>
                        <div class="container-fluid px-5">
                            <h1 class="mt-4">Customer Management</h1>
                            <div class="card mb-4">
                                <div class="card-header">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div>
                                            <i class="fa-solid fa-user"></i> Customer List
                                        </div>
                                    </div>
                                </div>

                                <div class="card-body">
                                    <!-- Search Form -->
                                    <form action="CustomerManagementServlet" method="GET" class="mb-4">
                                        <div class="input-group">
                                            <input type="hidden" name="action" value="search">
                                            <input type="text" name="search" class="form-control" placeholder="Search Customer by (ID, Phone Number, Name, Email)..." 
                                                   value="${param.search}">
                                            <button class="btn btn-outline-secondary" type="submit">
                                                <i class="fas fa-search"></i>
                                            </button>
                                            <a href="CustomerManagementServlet" class="btn btn-outline-danger">
                                                <i class="fas fa-sync"></i>
                                            </a>
                                        </div>
                                    </form>

                                    <!-- Customer Table -->
                                    <div class="table-responsive">
                                        <!-- Pagination -->
                                        <%@ include file="Component/Pagination.jsp" %>
                                        <table class="table table-bordered table-striped table-hover">
                                            <thead class="table-dark">
                                                <tr>
                                                    <th>ID</th>
                                                    <th>Link IMG</th>
                                                    <th>Name</th>
                                                    <th>Phone Number</th>
                                                    <th>Email</th>
                                                    <th>Password</th>
                                                    <th>Address</th>
                                                    <th>Balance</th>
                                                    <th>Created At</th>
                                                    <th>Updated At</th>
                                                    <th>Status</th>
                                                    <th>Actions</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:choose>
                                                    <c:when test="${empty customers}">
                                                        <tr>
                                                            <td colspan="12" class="text-center text-muted">No customers found</td>
                                                        </tr>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach var="customer" items="${customers}">
                                                            <tr>
                                                                <td>${customer.accountId}</td>
                                                                <td style="max-width: 50px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">${customer.image}</td>
                                                                <td style="max-width: 100px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis">${customer.userName}</td>
                                                                <td style="max-width: 100px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis">${customer.phoneNumber}</td>
                                                                <td style="max-width: 100px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis" >${customer.email}</td>
                                                                <td style="max-width: 100px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
                                                                    ${customer.password}
                                                                </td>
                                                                <td style="max-width: 100px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis" >${customer.address}</td>
                                                                <td>
                                                                    <fmt:formatNumber value="${customer.balance}" type="currency" />
                                                                </td>
                                                                <td>
                                                                    <fmt:formatDate value="${customer.createdAt}" pattern="dd-MM-yyyy" />
                                                                </td>
                                                                <td>
                                                                    <fmt:formatDate value="${customer.updateAt}" pattern="dd-MM-yyyy" />
                                                                </td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${customer.isDeleted eq 1}">
                                                                            <span class="badge bg-secondary">Deleted</span>
                                                                        </c:when>
                                                                        <c:when test="${customer.status eq 'Inactive'}">
                                                                            <span class="badge bg-danger">Inactive</span>
                                                                        </c:when>
                                                                        <c:when test="${customer.status eq 'Blocked'}">
                                                                            <span class="badge bg-warning">Blocked</span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="badge bg-success">Active</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                <td>
                                                                    <c:if test="${sessionScope.roleID eq 1}"> 
                                                                        <button class="btn btn-sm btn-warning" data-bs-toggle="modal" data-bs-target="#editCustomerModal${customer.accountId}"
                                                                                ${customer.isDeleted eq 1 ? 'disabled' : ''}>
                                                                            <i class="fas fa-edit"></i>
                                                                        </button>
                                                                        <button class="btn btn-sm btn-danger" data-bs-toggle="modal" data-bs-target="#deleteModal${customer.accountId}"
                                                                                ${customer.isDeleted eq 1 ? 'disabled' : ''}>
                                                                            <i class="fas fa-trash"></i>
                                                                        </button>
                                                                    </c:if>
                                                                </td>
                                                            </tr>

                                                            <!-- Edit Customer Modal -->
                                                        <div class="modal fade" id="editCustomerModal${customer.accountId}">
                                                            <div class="modal-dialog modal-lg">
                                                                <div class="modal-content">
                                                                    <form action="CustomerManagementServlet" method="POST" enctype="multipart/form-data">
                                                                        <input type="hidden" name="action" value="update">
                                                                        <input type="hidden" name="page" value="${currentPage}">
                                                                        <input type="hidden" name="accountId" value="${customer.accountId}">
                                                                        <div class="modal-header">
                                                                            <h5 class="modal-title">Edit Customer</h5>
                                                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                                        </div>
                                                                        <div class="modal-body">
                                                                            <div class="row g-3">
                                                                                <div class="col-12">
                                                                                    <label class="form-label">Upload Profile Image</label>
                                                                                    <input type="file" class="form-control" name="image" accept="image/*">
                                                                                    <input type="hidden" name="currentImage" value="${customer.image}">
                                                                                    <c:if test="${not empty customer.image}">
                                                                                        <img src="${pageContext.request.contextPath}/${customer.image}" alt="Profile Image" class="mt-2 rounded" style="max-height: 100px;">
                                                                                    </c:if>
                                                                                </div>
                                                                                <div class="col-md-6">
                                                                                    <label class="form-label">Full Name</label>
                                                                                    <input type="text" class="form-control" name="userName" value="${customer.userName}" required>
                                                                                </div>
                                                                                <div class="col-md-6">
                                                                                    <label class="form-label">Phone Number</label>
                                                                                    <input type="tel" class="form-control" name="phoneNumber" value="${customer.phoneNumber}" pattern="[0-9]{10}" required>
                                                                                </div>
                                                                                <div class="col-md-6">
                                                                                    <label class="form-label">Email</label>
                                                                                    <input type="email" class="form-control" name="email" id="email" value="${customer.email}" required>
                                                                                </div>      
                                                                                <div class="col-md-6">
                                                                                    <label class="form-label">Password</label>
                                                                                    <input type="password" class="form-control" id="password" name="password" 
                                                                                           value="${customer.password}" onblur="checkPassword(this)" required>
                                                                                    <span id="error-Pass" class="error-message"></span>
                                                                                </div>
                                                                                <div class="col-12">
                                                                                    <label class="form-label">Address</label>
                                                                                    <textarea class="form-control" name="address" rows="2">${customer.address}</textarea>
                                                                                </div>
                                                                                <div class="col-md-4">
                                                                                    <label class="form-label">Balance</label>
                                                                                    <div class="input-group">
                                                                                        <span class="input-group-text">$</span>
                                                                                        <input type="number" class="form-control" name="balance" value="${customer.balance}" step="0.01"  disabled>
                                                                                    </div>
                                                                                </div>
                                                                                <div class="col-md-4">
                                                                                    <label class="form-label">Status</label>
                                                                                    <select class="form-select" name="status">
                                                                                        <option value="Active" ${customer.status == 'Active' ? 'selected' : ''}>Active</option>
                                                                                        <option value="Inactive" ${customer.status == 'Inactive' ? 'selected' : ''}>Inactive</option>
                                                                                        <option value="Blocked" ${customer.status == 'Blocked' ? 'selected' : ''}>Blocked</option>
                                                                                    </select>
                                                                                </div>
                                                                                <div class="col-md-4">
                                                                                    <label class="form-label">Role</label>
                                                                                    <select class="form-select" name="roleID">
                                                                                        <c:forEach var="r" items="${roles}" >
                                                                                            <option value="${r.roleID}" ${r.roleID == customer.roleID ? 'selected' : ''} >${r.name}</option>
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
                                                        <div class="modal fade" id="deleteModal${customer.accountId}">
                                                            <div class="modal-dialog">
                                                                <div class="modal-content">
                                                                    <div class="modal-header">
                                                                        <h5 class="modal-title">Confirm Delete</h5>
                                                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                                    </div>
                                                                    <div class="modal-body">
                                                                        Are you sure you want to delete customer: <strong>${customer.userName}</strong>?
                                                                    </div>
                                                                    <div class="modal-footer">
                                                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                                                        <form action="CustomerManagementServlet" method="GET" class="d-inline">
                                                                            <input type="hidden" name="action" value="delete">
                                                                            <input type="hidden" name="accountId" value="${customer.accountId}">
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
                    <h5 class="modal-title" id="successModalLabel">Thành Công</h5>
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
        const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;

        function checkPassword(input) {
            var password = input.value.trim();
            var errorMessage = input.nextElementSibling; // Lấy phần tử `<span>` ngay sau input
            var modal = input.closest(".modal"); // Tìm modal chứa input này
            var submitBtn = modal ? modal.querySelector(".btn-primary") : null; // Tìm nút submit trong modal

            console.log("Submit button:", submitBtn); // Kiểm tra xem nút có tồn tại không

            if (!errorMessage) {
                console.error("Không tìm thấy phần tử lỗi!");
                return;
            }

            if (!submitBtn) {
                console.error("Không tìm thấy nút submit!");
                return;
            }

            if (password === "") {
                errorMessage.textContent = "Password cannot be empty!";
                submitBtn.disabled = true;
            } else if (password.length < 8) {
                errorMessage.textContent = "Password must be at least 8 characters.";
                submitBtn.disabled = true;
            } else if (!regex.test(password)) {
                errorMessage.textContent = "Password must contain at least one letter, one number, and one special character.";
                submitBtn.disabled = true;
            } else {
                errorMessage.textContent = ""; // Hợp lệ
                submitBtn.disabled = false;
            }
        }


    </script>

</html>