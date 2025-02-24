<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <title>Role Management</title>
        <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="CSS/style.css" />
        <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
        <script src="JS/SideBarToggle.js"></script>
    </head>
    <body class="sb-nav-fixed">
        <%@ include file="Component/SideBar.jsp" %>
        <div id="layoutSidenav">
            <div id="layoutSidenav_content">
                <main>
                    <div class="container-fluid px-5">
                        <h1 class="mt-4">Role Management</h1>

                        <!-- Hiển thị thông báo -->
                        <c:if test="${not empty message}">
                            <div class="alert alert-${messageType} alert-dismissible fade show" role="alert">
                                ${message}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>

                        <div class="card mb-4">
                            <div class="card-header d-flex justify-content-between align-items-center">
                                <span><i class="fas fa-user-shield me-1"></i> Role List</span>
                                <button class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#addRoleModal">
                                    <i class="fas fa-plus"></i> Add Role
                                </button>
                            </div>

                            <div class="card-body">
                                <!-- Form tìm kiếm -->
                                <form action="RoleServlet" method="GET" class="mb-3">
                                    <div class="input-group">
                                        <input type="hidden" name="action" value="search">
                                        <input type="text" name="search" class="form-control" placeholder="Search Role..." value="${param.search}">
                                        <button class="btn btn-outline-secondary" type="submit"><i class="fas fa-search"></i></button>
                                        <a href="RoleServlet?action=list" class="btn btn-outline-danger"><i class="fas fa-sync"></i></a>
                                    </div>
                                </form>

                                <!-- Bảng hiển thị -->
                                <div class="table-responsive">
                                    <table class="table table-bordered table-striped">
                                        <thead class="table-dark text-center">
                                            <tr>
                                                <th>ID</th>
                                                <th>Role Name</th>
                                                <th>Description</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${empty roles}">
                                                    <tr>
                                                        <td colspan="4" class="text-center text-muted">No roles found</td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="role" items="${roles}">
                                                        <tr>
                                                            <td>${role.roleID}</td>
                                                            <td>${role.roleName}</td>
                                                            <td>${role.description}</td>
                                                            <td class="text-center">
                                                                <button class="btn btn-sm btn-warning" data-bs-toggle="modal" data-bs-target="#editRoleModal${role.roleID}">
                                                                    <i class="fas fa-edit"></i>
                                                                </button>
                                                                <button class="btn btn-sm btn-danger" data-bs-toggle="modal" data-bs-target="#deleteRoleModal"
                                                                        onclick="setDeleteRoleID(${role.roleID})">
                                                                    <i class="fas fa-trash"></i>
                                                                </button>

                                                            </td>
                                                        </tr>

                                                        <!-- Modal edit -->
                                                    <div class="modal fade" id="editRoleModal${role.roleID}">
                                                        <div class="modal-dialog">
                                                            <div class="modal-content">
                                                                <form action="RoleServlet" method="POST">
                                                                    <input type="hidden" name="action" value="update">
                                                                    <input type="hidden" name="roleID" value="${role.roleID}">
                                                                    <div class="modal-header">
                                                                        <h5 class="modal-title">Edit Role</h5>
                                                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                                    </div>
                                                                    <div class="modal-body">
                                                                        <div class="mb-3">
                                                                            <label class="form-label">Role Name</label>
                                                                            <input type="text" class="form-control" name="roleName" value="${role.roleName}" required>
                                                                        </div>
                                                                        <div class="mb-3">
                                                                            <label class="form-label">Description</label>
                                                                            <textarea class="form-control" name="description" rows="3" required>${role.description}</textarea>
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

                                                    <!-- Delete Confirmation Modal for Roles -->
                                                    <div class="modal fade" id="deleteRoleModal" tabindex="-1" aria-labelledby="confirmDeleteModalLabel" aria-hidden="true">
                                                        <div class="modal-dialog">
                                                            <div class="modal-content">
                                                                <div class="modal-header">
                                                                    <h5 class="modal-title" id="confirmDeleteModalLabel">Confirm Deletion</h5>
                                                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                                </div>
                                                                <div class="modal-body">
                                                                    Are you sure you want to delete this role?
                                                                </div>
                                                                <div class="modal-footer">
                                                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                                                    <form id="deleteForm" method="POST" action="RoleServlet">
                                                                        <input type="hidden" name="action" value="delete">
                                                                        <input type="hidden" name="roleID" id="deleteRoleID">
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

        <!-- Error Message Modal -->
        <div class="modal fade" id="errorModal" tabindex="-1" aria-labelledby="errorModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header bg-danger text-white">
                        <h5 class="modal-title" id="errorModalLabel">Error</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body text-dark">
                        ${errorMessage}
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>

        <script>
            // Nếu có lỗi, hiển thị modal
            window.onload = function () {
                let errorMessage = "${sessionScope.errorMessage}";
                if (errorMessage && errorMessage.trim() !== "") {
                    let errorModal = new bootstrap.Modal(document.getElementById('errorModal'));
                    errorModal.show();
            <% request.getSession().removeAttribute("errorMessage"); %>  // Xóa thông báo lỗi sau khi hiển thị
                }
            };
        </script>


        <!-- Modal add -->
        <div class="modal fade" id="addRoleModal">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="RoleServlet" method="POST">
                        <input type="hidden" name="action" value="add">
                        <div class="modal-header">
                            <h5 class="modal-title">Add New Role</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <div class="mb-3">
                                <label class="form-label">Role Name</label>
                                <input type="text" class="form-control" name="roleName" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Description</label>
                                <textarea class="form-control" name="description" rows="3" required></textarea>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button type="submit" class="btn btn-primary">Add Role</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <script>
            function setDeleteRoleID(roleID) {
                document.getElementById('deleteRoleID').value = roleID;
            }
        </script>

    </body>
</html>
