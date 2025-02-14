<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <title>Category Management</title>
        <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="CSS/style.css" />
        <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="CSS/ModalAddMaterial.css"/>
    </head>

    <body class="sb-nav-fixed">
        <%@ include file="Component/SideBar.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Category Management</h1>
                    <ol class="breadcrumb mb-4">
                        <li class="breadcrumb-item active">Manage Categories</li>
                    </ol>

                    <div class="container d-flex justify-content-between">
                        <form action="CategoryServlet" method="POST">
                            <input type="hidden" name="action" value="add">

                            <label for="categoryName">Category Name</label>
                            <input type="text" id="categoryName" name="categoryName" placeholder="Enter category name" required />

                            <!-- Submit Button -->
                            <button class="btn btn-primary ms-2" type="submit">Add Category</button>
                        </form>

                        <!-- Button Refresh -->
                        <form action="CategoryServlet" method="POST">
                            <input type="hidden" name="action" value="list">
                            <button class="btn btn-primary ms-2" type="submit">Refresh</button>
                        </form>
                    </div>

                    <!-- Category List -->
                    <div class="card mb-4">
                        <div class="card-header"><i class="fas fa-table me-1"></i> Category List</div>

                        <!-- Search Form -->
                        <form method="GET" action="CategoryServlet" class="d-flex">
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
                                    <c:if test="${empty categories}">
                                        <tr>
                                            <td colspan="4" class="text-center text-muted">No categories available.</td>
                                        </tr>
                                    </c:if>

                                    <c:forEach var="category" items="${categories}">
                                        <tr>
                                            <td>${category.categoryID}</td>
                                            <td>${category.name}</td>
                                            <td>${category.createdAt}</td>
                                            <td>
                                                <!-- Edit Button -->
                                                <button class="btn btn-sm btn-primary" 
                                                        data-bs-toggle="modal" 
                                                        data-bs-target="#editCategoryModal-${category.categoryID}">
                                                    Update
                                                </button>

                                                <!-- Delete Button -->
                                                <button class="btn btn-sm btn-danger"
                                                        data-bs-toggle="modal"
                                                        data-bs-target="#confirmDeleteModal"
                                                        onclick="setDeleteCategoryID(${category.categoryID})">
                                                    Delete
                                                </button>
                                            </td>
                                        </tr>

                                        <!-- Edit Modal -->
                                    <div class="modal fade" id="editCategoryModal-${category.categoryID}" tabindex="-1">
                                        <div class="modal-dialog">
                                            <div class="modal-content">
                                                <form method="post" action="CategoryServlet">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title">Edit Category</h5>
                                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                    </div>
                                                    <div class="modal-body">
                                                        <input type="hidden" name="action" value="update">
                                                        <input type="hidden" name="categoryID" value="${category.categoryID}">
                                                        <div class="mb-3">
                                                            <label class="form-label">Category Name</label>
                                                            <input type="text" class="form-control" name="categoryName" value="${category.name}" required>
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
                        <form id="deleteCategoryForm" method="POST" action="CategoryServlet">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="categoryID" id="deleteCategoryID">
                            <button type="submit" class="btn btn-danger">Delete</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <script>
            function setDeleteCategoryID(categoryID) {
                document.getElementById("deleteCategoryID").value = categoryID;
            }
        </script>
    </body>
</html>
