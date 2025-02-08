<%-- 
    Document   : StatusOrderManagement
    Created on : Feb 8, 2025, 12:08:37 PM
    Author     : Truong Van Khang _ CE181852
--%>
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
        <title>Dashboard - Management Admin</title>
        <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="CSS/style.css" />
        <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script src="JS/SideBarToggle.js"></script>
    </head>

    <body class="sb-nav-fixed">
        <div id="layoutSidenav">
            <div id="layoutSidenav_content">
                <!-- Side Bar -->
                <%@ include file="Component/SideBar.jsp" %>
                <div class="dashboard-container">
                    <!-- Table -->
                    <main>
                        <div class="container-fluid px-5">
                            <h1 class="mt-4">Status Management</h1>
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
                                            <i class="fas fa-table me-1"></i> Status Order List
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
                                                            <button class="btn btn-sm btn-warning" 
                                                                    data-bs-toggle="modal" 
                                                                    data-bs-target="#editCustomerModal${s.accountId}">
                                                                <i class="fas fa-edit"></i>
                                                            </button>
                                                            <button class="btn btn-sm btn-danger" 
                                                                    data-bs-toggle="modal" 
                                                                    data-bs-target="#deleteModal${s.accountId}">
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
                                                                                           required>
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
                                                                                        <option value="active" ${s.status == 'Active' ? 'selected' : ''}>
                                                                                            Active
                                                                                        </option>
                                                                                        <option value="inactive" ${s.status == 'Inactive' ? 'selected' : ''}>
                                                                                            Inactive
                                                                                        </option>
                                                                                        <option value="blocked" ${s.status == 'blocked' ? 'selected' : ''}>
                                                                                            Blocked
                                                                                        </option>
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
                                                                            <button type="submit" class="btn btn-primary">Save Changes</button>
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
                </div> 
                </main>
            </div>
        </div>
    </div>
</body>
</html>
