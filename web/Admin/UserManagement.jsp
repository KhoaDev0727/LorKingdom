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

    <body class="sb-nav-fixed">
        <%@ include file="Component/SideBar.jsp" %>
        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Customer Management</h1>

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
                                    <i class="fas fa-table me-1"></i> Customer List
                                </div>
                                <div>
                                    <button class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#addCustomerModal">
                                        <i class="fas fa-plus"></i> Add Customer
                                    </button>
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
                                <table class="table table-bordered table-striped table-hover" >
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
                                            <c:when test="${empty customers }">
                                                <tr>
                                                    <td colspan="12" class="text-center text-muted">No customers found</td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="customer" items="${customers}">
                                                    <tr>
                                                        <td>${customer.accountId}</td>
                                                        <td style="width: 50px; height: 80px; overflow: hidden; text-align: center;">
                                                            <c:if test="${not empty s.image}">
                                                                <img src="${pageContext.request.contextPath}/${s.image}" 
                                                                     alt="Profile Image"
                                                                     class="mt-2 rounded" 
                                                                     style="width: 100%; height: 100%; object-fit: cover;">
                                                            </c:if>
                                                        </td>
                                                        <td>${customer.userName}</td>
                                                        <td>${customer.phoneNumber}</td>
                                                        <td>${customer.email}</td>
                                                        <td style="max-width: 100px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
                                                            ${customer.password}
                                                        </td>
                                                        <td>${customer.address}</td>
                                                        <td>
                                                            <fmt:formatNumber value="${customer.balance}" 
                                                                              type="currency"/></td>
                                                        <td>
                                                            <fmt:formatDate value="${customer.createdAt}" pattern="dd-MM-yyyy" />
                                                        </td>
                                                        <td>
                                                            <fmt:formatDate value="${customer.updateAt}" pattern="dd-MM-yyyy" />
                                                        </td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${customer != null and customer.isDeleted eq 1}">
                                                                    <span class="badge bg-secondary">Deleted</span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <c:choose>
                                                                        <c:when test="${customer != null and customer.status eq 'Inactive'}">
                                                                            <span class="badge bg-danger">Inactive</span>
                                                                        </c:when>
                                                                        <c:when test="${customer != null and customer.status eq 'Blocked'}">
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
                                                            <button class="btn btn-sm btn-warning" 
                                                                    data-bs-toggle="modal" 
                                                                    data-bs-target="#editCustomerModal${customer.accountId}">
                                                                <i class="fas fa-edit"></i>
                                                            </button>
                                                            <button class="btn btn-sm btn-danger" 
                                                                    data-bs-toggle="modal" 
                                                                    data-bs-target="#deleteModal${customer.accountId}">
                                                                <i class="fas fa-trash"></i>
                                                            </button>
                                                        </td>
                                                    </tr>

                                                    <!-- Edit Customer Modal -->
                                                <div class="modal fade" id="editCustomerModal${customer.accountId}">
                                                    <div class="modal-dialog modal-lg">
                                                        <div class="modal-content">
                                                            <form action="CustomerMangementServlet" method="POST" enctype="multipart/form-data">
                                                                <input type="hidden" name="action" value="update">
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
                                                                            <!-- Giữ ảnh cũ nếu không upload mới -->
                                                                            <input type="hidden" name="currentImage" value="${customer.image}">
                                                                            <c:if test="${not empty customer.image}">
                                                                                <img src="${pageContext.request.contextPath}/${s.image}" alt="Profile Image"
                                                                                     class="mt-2 rounded" style="max-height: 100px;">
                                                                            </c:if>
                                                                        </div>
                                                                        <div class="col-md-6">
                                                                            <label class="form-label">Full Name</label>
                                                                            <input type="text" class="form-control" name="userName" 
                                                                                   value="${customer.userName}" required>
                                                                        </div>
                                                                        <div class="col-md-6">
                                                                            <label class="form-label">Phone Number</label>
                                                                            <input type="tel" class="form-control" name="phoneNumber" 
                                                                                   value="${customer.phoneNumber}" pattern="[0-9]{10}" required>
                                                                        </div>
                                                                        <div class="col-md-6">
                                                                            <label class="form-label">Email</label>
                                                                            <input type="email" class="form-control" name="email" 
                                                                                   value="${customer.email}" required>
                                                                        </div>
                                                                        <div class="col-md-6">
                                                                            <label class="form-label">Password</label>
                                                                            <input type="password" class="form-control" name="password" 
                                                                                   value="${customer.password}" required>
                                                                        </div>
                                                                        <div class="col-12">
                                                                            <label class="form-label">Address</label>
                                                                            <textarea class="form-control" name="address" rows="2">${customer.address}</textarea>
                                                                        </div>
                                                                        <div class="col-md-4">
                                                                            <label class="form-label">Balance</label>
                                                                            <div class="input-group">
                                                                                <span class="input-group-text">$</span>
                                                                                <input type="number" class="form-control" name="balance" 
                                                                                       value="${customer.balance}" step="0.01" required disabled>
                                                                            </div>
                                                                        </div>
                                                                        <div class="col-md-4">
                                                                            <label class="form-label">Status</label>
                                                                            <select class="form-select" name="status">
                                                                                <option value="active" ${customer.status == 'active' ? 'selected' : ''}>Active</option>
                                                                                <option value="inactive" ${customer.status == 'inactive' ? 'selected' : ''}>Inactive</option>
                                                                                <option value="blocked" ${customer.status == 'blocked' ? 'selected' : ''}>Blocked</option>
                                                                            </select>
                                                                        </div>
                                                                        <div class="col-md-4">
                                                                            <label class="form-label">Role</label>
                                                                            <select class="form-select" name="roleID">
                                                                                <c:forEach var="r" items="${roles}">
                                                                                    <option value="${r.roleID}" ${r.roleID == customer.roleID ? 'selected' : ''}>
                                                                                        ${r.name}
                                                                                    </option>
                                                                                </c:forEach>
                                                                            </select>
                                                                        </div>
                                                                    </div>
                                                                </div>

                                                                <div class="modal-footer">
                                                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                                                    <button type="submit" class="btn btn-primary">Save Changes</button>
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
                                                                Are you sure you want to delete customer: 
                                                                <strong>${customer.userName}</strong>?
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

        <!-- Add Customer Modal -->
        <div class="modal fade" id="addCustomerModal">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <form action="CustomerServlet" method="POST">
                        <input type="hidden" name="action" value="create">

                        <div class="modal-header">
                            <h5 class="modal-title">Add New Customer</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>

                        <div class="modal-body">
                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label class="form-label">Full Name</label>
                                    <input type="text" class="form-control" name="accountName" required>
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
                                    <input type="password" class="form-control" name="password" required>
                                </div>
                                <div class="col-12">
                                    <label class="form-label">Address</label>
                                    <textarea class="form-control" name="address" rows="2"></textarea>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label">Initial Balance</label>
                                    <div class="input-group">
                                        <span class="input-group-text">$</span>
                                        <input type="number" class="form-control" name="balance" 
                                               value="0.00" step="0.01" required>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label">Status</label>
                                    <select class="form-select" name="status">
                                        <option value="true">Active</option>
                                        <option value="false">Inactive</option>
                                    </select>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label">Role</label>
                                    <select class="form-select" name="roleID">
                                        <option value="2">Customer</option>
                                        <option value="1">Admin</option>
                                    </select>
                                </div>
                            </div>
                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button type="submit" class="btn btn-primary">Add Customer</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

    </body>
</html>