<%-- 
    Document   : MaterialManagement
    Created on : Feb 12, 2025, 7:47:42 AM
    Author     : admin1
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <title>Material Management</title>
        <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="CSS/style.css" />
        <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script src="JS/SideBarToggle.js"></script>
        <link rel="stylesheet" href="CSS/ModalAddMaterial.css"/>
    </head>

    <body class="sb-nav-fixed">
        <%@ include file="Component/SideBar.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Material Management</h1>
                    <ol class="breadcrumb mb-4">
                        <li class="breadcrumb-item active">Manage Materials</li>
                    </ol>

                    <div class="container d-flex justify-content-between">
                        <!-- Add Material Form -->
                        <form action="MaterialServlet" method="POST">
                            <input type="hidden" name="action" value="add">

                            <label for="materialName">Material Name</label>
                            <input type="text" id="materialName" name="name" required />

                            <label for="description">Description</label>
                            <input type="text" id="description" name="description" required />

                            <!-- Submit Button -->
                            <button class="btn btn-primary ms-2" type="submit">Add Material</button>
                        </form>

                        <!-- Button Refresh -->
                        <form action="MaterialServlet" method="POST">
                            <input type="hidden" name="action" value="list">
                            <button class="btn btn-primary ms-2" type="submit">Refresh</button>
                        </form>
                    </div>

                    <!-- Material List -->
                    <div class="card mb-4">
                        <div class="card-header"><i class="fas fa-table me-1"></i> Material List</div>

                        <!-- Search Form -->
                        <form method="GET" action="MaterialServlet" class="d-flex">
                            <input name="action" type="hidden" value="search">
                            <input type="text" name="search" class="form-control" placeholder="Find Material..." aria-label="Search">
                            <button class="btn btn-primary ms-2" type="submit">Search</button>
                        </form>

                        <div class="card-body">
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <th>Material ID</th>
                                        <th>Material Name</th>
                                        <th>Description</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <!-- Display message if the list is empty -->
                                <c:if test="${empty materials}">
                                    <tr>
                                        <td colspan="4" class="text-center text-muted">No materials available.</td>
                                    </tr>
                                </c:if>

                                <c:forEach var="material" items="${materials}">
                                    <tr>
                                        <td>${material.materialID}</td>
                                        <td>${material.name}</td>
                                        <td>${material.description}</td>
                                        <td>
                                            <!-- Edit Button -->
                                            <button class="btn btn-sm btn-primary" 
                                                    data-bs-toggle="modal" 
                                                    data-bs-target="#editMaterialModal-${material.materialID}">
                                                Update
                                            </button>

                                            <!-- Delete Button -->
                                            <button class="btn btn-sm btn-danger"
                                                    data-bs-toggle="modal"
                                                    data-bs-target="#confirmDeleteModal"
                                                    onclick="setDeleteMaterialID(${material.materialID})">
                                                Delete
                                            </button>
                                        </td>
                                    </tr>

                                    <!-- Edit Modal -->
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
                                                            <input type="text" class="form-control" name="description" value="${material.description}" required>
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
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </main>
        </div>

        <script>
            function setDeleteMaterialID(materialID) {
                document.getElementById("deleteMaterialID").value = materialID;
            }
        </script>
    </body>
</html>
