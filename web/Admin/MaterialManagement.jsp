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
        <title>Material Management</title>
        <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="CSS/style.css" />
        <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script src="JS/SideBarToggle.js"></script>
    </head>
    <body class="sb-nav-fixed">
        <c:if test="${empty sessionScope.roleID}">
            <c:redirect url="/Admin/loginPage.jsp"/>
        </c:if>
        <div id="layoutSidenav">
            <div id="layoutSidenav_content">
                <%@ include file="Component/SideBar.jsp" %>
                <div class="dashboard-container">
                    <main>
                        <div class="container-fluid px-5">
                            <h1 class="mt-4">Material Management</h1>
                            <!-- Add Material Form -->
                            <form action="MaterialServlet" method="POST" class="d-flex align-items-end">
                                <input type="hidden" name="action" value="add">
                                <div class="me-2">
                                    <label for="materialName">Material Name</label>
                                    <input type="text" id="materialName" name="name" class="form-control" required />
                                </div>
                                <div class="me-2">
                                    <label for="materialDescription">Description</label>
                                    <input type="text" id="materialDescription" name="description" class="form-control" />
                                </div>
                                <!-- Submit Button -->
                                <button class="btn btn-primary ms-2" type="submit">Add Material</button>
                            </form>
                            <div class="card mb-4">
                                <div class="card-header">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div>
                                            <i class="fas fa-table me-1"></i> Material List
                                        </div>
                                    </div>
                                </div>

                                <div class="card-body">
                                    <!-- Search Form -->
                                    <form action="MaterialServlet" method="GET" class="mb-4">
                                        <div class="input-group">
                                            <input type="hidden" name="action" value="search">
                                            <input type="text" name="search" class="form-control" placeholder="Find Material Name..." aria-label="Search">
                                            <button class="btn btn-outline-secondary" type="submit">
                                                <i class="fas fa-search"></i>
                                            </button>
                                            <a href="MaterialServlet" class="btn btn-outline-danger">
                                                <i class="fas fa-sync"></i>
                                            </a>
                                        </div>
                                    </form>
                                    <!-- Customer Table -->
                                    <div class="table-responsive">
                                        <table class="table table-bordered table-striped table-hover">
                                            <thead class="table-dark">
                                                <tr>
                                                    <th>Material ID</th>
                                                    <th>Material Name</th>
                                                    <th>Description</th>
                                                    <th>Actions</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:choose>
                                                    <c:when test="${empty materials}">
                                                        <!-- Display message if the list is empty -->
                                                        <tr>
                                                            <td colspan="4" class="text-center text-muted">No Material available.</td>
                                                        </tr>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach var="material" items="${materials}">
                                                            <tr>
                                                                <td>${material.materialID}</td>
                                                                <td>${material.name}</td>
                                                                <td>${material.description}</td>
                                                                <td>
                                                                    <!-- Edit Button -->
                                                                    <button class="btn btn-sm btn-warning" 
                                                                            data-bs-toggle="modal" 
                                                                            data-bs-target="#editMaterialModal-${material.materialID}">
                                                                        <i class="fas fa-edit"></i>
                                                                    </button>
                                                                    <!-- Delete Button -->
                                                                    <button class="btn btn-sm btn-danger"
                                                                            data-bs-toggle="modal"
                                                                            data-bs-target="#confirmDeleteModal"
                                                                            onclick="setDeleteMaterialID(${material.materialID})">
                                                                        <i class="fas fa-trash"></i> 
                                                                    </button>

                                                                </td>
                                                            </tr>
                                                            <!-- Edit Material Modal -->
                                                        <div class="modal fade" id="editMaterialModal-${material.materialID}" tabindex="-1">
                                                            <div class="modal-dialog">
                                                                <div class="modal-content">
                                                                    <form method="post" action="MaterialServlet">
                                                                        <div class="modal-header">
                                                                            <h5 class="modal-title">Edit Material</h5>
                                                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                                        </div>
                                                                        <div class="modal-body">
                                                                            <input type="hidden" name="action" value="update">
                                                                            <input type="hidden" name="materialID" value="${material.materialID}">
                                                                            <div class="mb-3">
                                                                                <label class="form-label">Material Name</label>
                                                                                <input type="text" class="form-control" name="name" value="${material.name}" required>
                                                                            </div>
                                                                            <div class="mb-3">
                                                                                <label class="form-label">Description</label>
                                                                                <input type="text" class="form-control" name="description" value="${material.description}">
                                                                            </div>
                                                                        </div>
                                                                        <div class="modal-footer">
                                                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                                                            <button type="submit" class="btn btn-primary">Save changes</button>
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
        <div class="modal fade" id="successModalLabel" tabindex="-1" aria-labelledby="successModalTitle" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header bg-success text-white">
                        <h5 class="modal-title" id="successModalTitle">Success</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body text-dark">
                        <p id="successMessageContent">${sessionScope.successMessage}</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Confirm Delete Modal -->
        <div class="modal fade" id="confirmDeleteModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Confirm Deletion</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        Are you sure you want to delete this Material entry?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <form id="deleteMaterialForm" method="POST" action="MaterialServlet">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="materialID" id="deleteMaterialID">
                            <button type="submit" class="btn btn-danger">Delete</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <script>
            function setDeleteMaterialID(materialID) {
                document.getElementById("deleteMaterialID").value = materialID;
            }
            window.onload = function () {
                const errorMessage = "${sessionScope.errorMessage}";
                if (errorMessage && errorMessage.trim() !== "") {
                    const errorModal = new bootstrap.Modal(document.getElementById("errorModal"));
                    errorModal.show();
            <% request.getSession().removeAttribute("errorMessage"); %>
                }

                let successMessage = '<%= session.getAttribute("successMessage") %>';
                if (successMessage && successMessage.trim() !== "null" && successMessage.trim() !== "") {
                    let successModal = new bootstrap.Modal(document.getElementById("successModalLabel"));
                    successModal.show();
            <% session.removeAttribute("successMessage"); %>
                }
            };
        </script>

    </body>
</html>
