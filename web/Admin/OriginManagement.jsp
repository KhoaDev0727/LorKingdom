<%-- 
    Document   : OriginManagement
    Created on : Feb 15, 2025, 10:03:23 PM
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
        <title>Origin Management</title>
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
                    <h1 class="mt-4">Origin Management</h1>
                    <ol class="breadcrumb mb-4">
                        <li class="breadcrumb-item active">Manage Origin Entities</li>
                    </ol>
                    <div class="container d-flex justify-content-between">
                        <!-- Add Origin Form -->
                        <form action="OriginServlet" method="POST">
                            <input type="hidden" name="action" value="add">
                            <label for="originName">Origin Name</label>
                            <input type="text" id="originName" name="name" required />
                            <button class="btn btn-primary ms-2" type="submit">Add Origin</button>
                        </form>
                        <!-- Refresh Button -->
                        <form action="OriginServlet" method="POST">
                            <input type="hidden" name="action" value="list">
                            <button class="btn btn-primary ms-2" type="submit">Refresh</button>
                        </form>
                    </div>
                    <!-- Origin List -->
                    <div class="card mb-4">
                        <div class="card-header"><i class="fas fa-table me-1"></i> Origin List</div>
                        <!-- Search Form -->
                        <form method="GET" action="OriginServlet" class="d-flex">
                            <input type="hidden" name="action" value="search">
                            <input type="text" name="search" class="form-control" placeholder="Find Origin..." aria-label="Search">
                            <button class="btn btn-primary ms-2" type="submit">Search</button>
                        </form>
                        <div class="card-body">
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <th>Origin ID</th>
                                        <th>Origin Name</th>
                                        <th>Date Created</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                <c:if test="${empty origins}">
                                    <tr>
                                        <td colspan="4" class="text-center text-muted">No origins available.</td>
                                    </tr>
                                </c:if>
                                <c:forEach var="origin" items="${origins}">
                                    <tr>
                                        <td>${origin.originID}</td>
                                        <td>${origin.name}</td>
                                        <td>${origin.createdAt}</td>
                                        <td>
                                            <!-- Update Button -->
                                            <button class="btn btn-sm btn-primary" data-bs-toggle="modal" data-bs-target="#editOriginModal-${origin.originID}">
                                                Update
                                            </button>
                                            <!-- Delete Button -->
                                            <button class="btn btn-sm btn-danger" data-bs-toggle="modal" data-bs-target="#confirmDeleteModal" onclick="setDeleteOriginID(${origin.originID})">
                                                Delete
                                            </button>
                                        </td>
                                    </tr>
                                    <!-- Edit Modal -->
                                    <div class="modal fade" id="editOriginModal-${origin.originID}" tabindex="-1">
                                        <div class="modal-dialog">
                                            <div class="modal-content">
                                                <form method="post" action="OriginServlet">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title">Edit Origin</h5>
                                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                    </div>
                                                    <div class="modal-body">
                                                        <input type="hidden" name="action" value="update">
                                                        <input type="hidden" name="originID" value="${origin.originID}">
                                                        <div class="mb-3">
                                                            <label class="form-label">Origin Name</label>
                                                            <input type="text" class="form-control" name="name" value="${origin.name}" required>
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
        <!-- Error Modal -->
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
        <!-- Confirm Delete Modal -->
        <div class="modal fade" id="confirmDeleteModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Confirm Deletion</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        Are you sure you want to delete this Origin entry?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <form id="deleteOriginForm" method="POST" action="OriginServlet">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="originID" id="deleteOriginID">
                            <button type="submit" class="btn btn-danger">Delete</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <script>
            function setDeleteOriginID(originID) {
                document.getElementById("deleteOriginID").value = originID;
            }

            window.onload = function () {
                const errorMessage = "${sessionScope.errorMessage}";
                if (errorMessage && errorMessage.trim() !== "") {
                    const errorModal = new bootstrap.Modal(document.getElementById("errorModal"));
                    errorModal.show();
                }
            };
        </script>
    </body>
</html>
