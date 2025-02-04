<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <title>SuperCategory Management</title>
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
                    <h1 class="mt-4">SuperCategory Management</h1>
                    <ol class="breadcrumb mb-4">
                        <li class="breadcrumb-item active">Manage SuperCategories</li>
                    </ol>

                    <div class="container d-flex justify-content-between">
                        <!-- Form Add SuperCategory -->
                        <form action="SuperCategoryServlet" method="POST">
                            <input type="hidden" name="action" value="add">
                            <label>Category Name</label>
                            <input type="text" name="superCategoryName" required />
                            <button class="btn btn-primary ms-2" type="submit">Add Category</button>
                        </form>

                        <!-- Button Refresh -->
                        <form action="SuperCategoryServlet" method="POST">
                            <input type="hidden" name="action" value="list">
                            <button class="btn btn-primary ms-2" type="submit">Refresh</button>
                        </form>
                    </div>

                    <!-- Category List -->
                    <div class="card mb-4">
                        <div class="card-header"><i class="fas fa-table me-1"></i> SuperCategory List</div>

                        <!-- Search Form -->
                        <form method="GET" action="SuperCategoryServlet" class="d-flex">
                            <input name="action" type="hidden" value="search">
                            <input type="text" name="search" class="form-control" placeholder="Find Category..." aria-label="Search">
                            <button class="btn btn-primary ms-2 taskFind" type="submit">Search</button>
                        </form>

                        <div class="card-body">
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <th>Category ID</th>
                                        <th>Category Name</th>
                                        <th>Date Created</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <!-- Display message if the list is empty -->
                                    <c:if test="${empty superCategories}">
                                        <tr>
                                            <td colspan="4" class="text-center text-muted">No categories available.</td>
                                        </tr>
                                    </c:if>

                                    <c:forEach var="category" items="${superCategories}">
                                        <tr>
                                            <td>${category.superCategoryID}</td>
                                            <td>${category.name}</td>
                                            <td>${category.createdAt}</td>
                                            <td>
                                                <!-- Edit Button -->
                                                <button class="btn btn-sm btn-primary" 
                                                        data-bs-toggle="modal" 
                                                        data-bs-target="#editCategoryModal-${category.superCategoryID}">
                                                    Update
                                                </button>

                                                <!-- Delete Button -->
                                                <form method="post" action="SuperCategoryServlet" class="d-inline">
                                                    <input type="hidden" name="action" value="delete">
                                                    <input type="hidden" name="superCategoryID" value="${category.superCategoryID}">
                                                    <button type="button" class="btn btn-sm btn-danger"
                                                            data-bs-toggle="modal"
                                                            data-bs-target="#confirmDeleteModal"
                                                            onclick="setDeleteCategoryID(${category.superCategoryID})">
                                                        Delete
                                                    </button>
                                                </form>
                                            </td>
                                        </tr>

                                        <!-- Edit Modal -->
                                    <div class="modal fade" id="editCategoryModal-${category.superCategoryID}" tabindex="-1">
                                        <div class="modal-dialog">
                                            <div class="modal-content">
                                                <form method="post" action="SuperCategoryServlet">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title">Edit Category</h5>
                                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                    </div>
                                                    <div class="modal-body">
                                                        <input type="hidden" name="action" value="update">
                                                        <input type="hidden" name="superCategoryID" value="${category.superCategoryID}">
                                                        <div class="mb-3">
                                                            <label class="form-label">Category Name</label>
                                                            <input type="text" class="form-control" name="name" value="${category.name}" required>
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
        <!-- Modal Error -->
        <div class="modal fade" id="errorModal" tabindex="-1" aria-labelledby="errorModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header bg-danger text-white">
                        <h5 class="modal-title" id="errorModalLabel">Error</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <p id="errorMessage">${sessionScope.errorMessage}</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Delete Confirmation Modal -->
        <div class="modal fade" id="confirmDeleteModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Confirm Deletion</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        Are you sure you want to delete this category?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <form id="deleteCategoryForm" method="POST" action="SuperCategoryServlet">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="superCategoryID" id="deleteCategoryID">
                            <button type="submit" class="btn btn-danger">Delete</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <script>
            function setDeleteCategoryID(superCategoryID) {
                document.getElementById("deleteCategoryID").value = superCategoryID;
            }
            <c:if test="${not empty sessionScope.errorMessage}">
            var errorModal = new bootstrap.Modal(document.getElementById("errorModal"));
            errorModal.show();
            </c:if>

            // Xóa thông báo lỗi sau khi hiển thị modal
            window.onload = function () {
            <c:if test="${not empty sessionScope.errorMessage}">
                setTimeout(function () {
                <c:remove var="errorMessage" scope="session"/>
                }, 1000);
            </c:if>
            };
        </script>

    </body>
</html>
