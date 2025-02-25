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
        <%@ include file="Component/SideBar.jsp" %>
        <div id="layoutSidenav">
            <div id="layoutSidenav_content">
                <div class="dashboard-container">
                    <!-- Table -->
                    <main>
                        <div class="container-fluid px-5">
                            <h1 class="mt-4">Shipping Method Management</h1>
                            <!-- Success/Error Messages -->


                            <div class="card mb-4">
                                <div class="card-header">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div>
                                            <i class="fas fa-truck me-1"></i> Shipping Methods List
                                        </div>
                                        <div>
                                            <button class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#addShippingMethodModal">
                                                <i class="fas fa-plus"></i> Add Shipping Method
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
                                        </div>
                                    </form>

                                    <!-- Shipping Method Table -->
                                    <div class="table-responsive">
                                        <table class="table table-bordered table-striped table-hover">
                                            <thead class="table-dark text-center">
                                                <tr>
                                                    <th>ID</th>
                                                    <th>Method Name</th>
                                                    <th>Description</th>
                                                    <th>Actions</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:choose>
                                                    <c:when test="${empty shippingMethods}">
                                                        <tr>
                                                            <td colspan="5" class="text-center text-muted">No shipping methods found</td>
                                                        </tr>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach var="method" items="${shippingMethods}">
                                                            <tr>
                                                                <td>${method.shippingMethodID}</td>
                                                                <td>${method.methodName}</td>
                                                                <td>${method.description}</td>
                                                                <td>
                                                                    <button class="btn btn-sm btn-warning" 
                                                                            data-bs-toggle="modal" 
                                                                            data-bs-target="#editShippingMethodModal${method.shippingMethodID}">
                                                                        <i class="fas fa-edit"></i>
                                                                    </button>
                                                                    <button class="btn btn-sm btn-danger" 
                                                                            data-bs-toggle="modal" 
                                                                            data-bs-target="#confirmDeleteModal"
                                                                            onclick="setDeleteShippingMethodID(${method.shippingMethodID})">
                                                                        <i class="fas fa-trash"></i>
                                                                    </button>
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
                                                                            <h5 class="modal-title">Edit Shipping Method</h5>
                                                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                                        </div>
                                                                        <div class="modal-body">
                                                                            <div class="mb-3">
                                                                                <label class="form-label">Method Name</label>
                                                                                <input type="text" class="form-control" name="methodName" value="${method.methodName}" required>
                                                                            </div>

                                                                            <div class="mb-3">
                                                                                <label class="form-label">Description</label>
                                                                                <textarea class="form-control" name="description" rows="3">${method.description}</textarea>
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

                                                        <!-- Delete Confirmation Modal for Shipping Methods -->
                                                        <div class="modal fade" id="confirmDeleteModal" tabindex="-1" aria-labelledby="confirmDeleteModalLabel" aria-hidden="true">
                                                            <div class="modal-dialog">
                                                                <div class="modal-content">
                                                                    <div class="modal-header">
                                                                        <h5 class="modal-title" id="confirmDeleteModalLabel">Confirm Deletion</h5>
                                                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                                    </div>
                                                                    <div class="modal-body">
                                                                        Are you sure you want to delete this shipping method?
                                                                    </div>
                                                                    <div class="modal-footer">
                                                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                                                        <form id="deleteForm" method="POST" action="ShippingMethodServlet">
                                                                            <input type="hidden" name="action" value="delete">
                                                                            <input type="hidden" name="shippingMethodID" id="deleteShippingMethodID">
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

        <!-- Add Shipping Method Modal -->
        <div class="modal fade" id="addShippingMethodModal">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="ShippingMethodServlet" method="POST">
                        <input type="hidden" name="action" value="add">
                        <div class="modal-header">
                            <h5 class="modal-title">Add New Shipping Method</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>

                        <div class="modal-body">
                            <div class="mb-3">
                                <label for="methodName" class="form-label">Method Name</label>
                                <input type="text" class="form-control" id="methodName" name="methodName" required>
                            </div>

                            <div class="mb-3">
                                <label for="description" class="form-label">Description</label>
                                <textarea class="form-control" id="description" name="description" rows="3" required=></textarea>
                            </div>
                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button type="submit" class="btn btn-primary">Add Shipping Method</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>


        <script>
            function setDeleteShippingMethodID(shippingMethodID) {
                document.getElementById("deleteShippingMethodID").value = shippingMethodID;
            }

            window.onload = function () {
                const errorMessage = "${sessionScope.errorMessage}";
                if (errorMessage && errorMessage.trim() !== "") {
                    const errorModal = new bootstrap.Modal(document.getElementById("errorModal"));
                    errorModal.show();
                    <% request.getSession().removeAttribute("errorMessage"); %>  //
                }
            };
        </script>
    </body>
</html>
