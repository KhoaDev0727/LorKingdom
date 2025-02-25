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
        <title>Super Category Management</title>
        <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="CSS/style.css" />
        <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script src="JS/SideBarToggle.js"></script>
        <style>
            .rating-filter button {
                margin: 5px;
                padding: 8px 15px;
            }
            .rating-filter button.active {
                background-color: #0d6efd;
                color: white;
            }
            .star {
                color: red;
                font-size: 1.2em;
            }
        </style>
    </head>
    <body class="sb-nav-fixed">
        <div id="layoutSidenav">
            <div id="layoutSidenav_content">
                <%@ include file="Component/SideBar.jsp" %>
                <div class="dashboard-container">
                    <main>
                        <div class="container-fluid px-5">
                            <h1 class="mt-4">Super Category Management</h1>
                            <!-- Form Add SuperCategory -->
                            <form action="SuperCategoryServlet" method="POST">
                                <input type="hidden" name="action" value="add">
                                <label>Category Name</label>
                                <input type="text" name="superCategoryName" required />
                                <button class="btn btn-primary ms-2" type="submit">Add Category</button>
                            </form>
                            <div class="card mb-4">
                                <div class="card-header">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div>
                                            <i class="fas fa-table me-1"></i> Super Category List
                                        </div>
                                    </div>
                                </div>

                                <div class="card-body">
                                    <!-- Search Form -->
                                    <form action="SuperCategoryServlet" method="GET" class="mb-4">
                                        <div class="input-group">
                                            <input type="hidden" name="action" value="search">
                                            <input type="text" name="search" class="form-control" placeholder="Find Category..." aria-label="Search">
                                            <button class="btn btn-outline-secondary" type="submit">
                                                <i class="fas fa-search"></i>
                                            </button>
                                            <a href="SuperCategoryServlet" class="btn btn-outline-danger">
                                                <i class="fas fa-sync"></i>
                                            </a>
                                        </div>
                                    </form>
                                    <!-- Customer Table -->
                                    <div class="table-responsive">
                                        <table class="table table-bordered table-striped table-hover">
                                            <thead class="table-dark">
                                                <tr>
                                                    <th>Category ID</th>
                                                    <th>Category Name</th>
                                                    <th>Date Created</th>
                                                    <th>Actions</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:choose>
                                                    <c:when test="${empty superCategories}">
                                                        <!-- Display message if the list is empty -->
                                                        <tr>
                                                            <td colspan="4" class="text-center text-muted">No categories available.</td>
                                                        </tr>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach var="category" items="${superCategories}">
                                                            <tr>
                                                                <td>${category.superCategoryID}</td>
                                                                <td>${category.name}</td>
                                                                <td>${category.createdAt}</td>
                                                                <td>
                                                                    <!-- Edit Button -->
                                                                    <button class="btn btn-sm btn-warning" 
                                                                            data-bs-toggle="modal" 
                                                                            data-bs-target="#editCategoryModal-${category.superCategoryID}">
                                                                        <i class="fas fa-edit"></i> 
                                                                    </button>
                                                                    <!-- Delete Button -->
                                                                    <form method="post" action="SuperCategoryServlet" class="d-inline">
                                                                        <input type="hidden" name="action" value="delete">
                                                                        <input type="hidden" name="superCategoryID" value="${category.superCategoryID}">
                                                                        <button type="button" class="btn btn-sm btn-danger"
                                                                                data-bs-toggle="modal"
                                                                                data-bs-target="#confirmDeleteModal"
                                                                                onclick="setDeleteCategoryID(${category.superCategoryID})">
                                                                            <i class="fas fa-trash"></i>
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
        <script>
            function setDeleteCategoryID(superCategoryID) {
                document.getElementById("deleteCategoryID").value = superCategoryID;
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