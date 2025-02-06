<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <title>Brand Management</title>
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
                    <h1 class="mt-4">Brand Management</h1>
                    <ol class="breadcrumb mb-4">
                        <li class="breadcrumb-item active">Manage Brands</li>
                    </ol>

                    <div class="container d-flex justify-content-between">
                        <form action="BrandServlet" method="POST">
                            <input type="hidden" name="action" value="add">

                            <label for="brandName">Brand Name</label>
                            <input type="text" id="brandName" name="brandName" placeholder="Enter brand name" required />

                            <label for="originBrand">Origin Brand</label>
                            <input type="text" id="originBrand" name="originBrand" placeholder="Enter origin brand" />

                            <!-- Submit Button -->
                            <button class="btn btn-primary ms-2" type="submit">Add Brand</button>
                        </form>

                        <!-- Button Refresh -->
                        <form action="BrandServlet" method="POST">
                            <input type="hidden" name="action" value="list">
                            <button class="btn btn-primary ms-2" type="submit">Refresh</button>
                        </form>
                    </div>

                    <!-- Brand List -->
                    <div class="card mb-4">
                        <div class="card-header"><i class="fas fa-table me-1"></i> Brand List</div>

                        <!-- Search Form -->
                        <form method="GET" action="BrandServlet" class="d-flex">
                            <input name="action" type="hidden" value="search">
                            <input type="text" name="search" class="form-control" placeholder="Find Brand..." aria-label="Search">
                            <button class="btn btn-primary ms-2 taskFind" type="submit">Search</button>
                        </form>

                        <div class="card-body">
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <th>Brand ID</th>
                                        <th>Brand Name</th>
                                        <th>Origin Brand</th>
                                        <th>Date Created</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <!-- Display message if the list is empty -->
                                    <c:if test="${empty brands}">
                                        <tr>
                                            <td colspan="5" class="text-center text-muted">No brands available.</td>
                                        </tr>
                                    </c:if>

                                    <c:forEach var="brand" items="${brands}">
                                        <tr>
                                            <td>${brand.brandID}</td>
                                            <td>${brand.brandName}</td>
                                            <td>${brand.originBrand}</td>
                                            <td>${brand.createdAt}</td>
                                            <td>
                                                <!-- Edit Button -->
                                                <button class="btn btn-sm btn-primary" 
                                                        data-bs-toggle="modal" 
                                                        data-bs-target="#editBrandModal-${brand.brandID}">
                                                    Update
                                                </button>

                                                <!-- Delete Button -->
                                                <button class="btn btn-sm btn-danger"
                                                        data-bs-toggle="modal"
                                                        data-bs-target="#confirmDeleteModal"
                                                        onclick="setDeleteBrandID(${brand.brandID})">
                                                    Delete
                                                </button>
                                            </td>
                                        </tr>

                                        <!-- Edit Modal -->
                                        <div class="modal fade" id="editBrandModal-${brand.brandID}" tabindex="-1">
                                            <div class="modal-dialog">
                                                <div class="modal-content">
                                                    <form method="post" action="BrandServlet">
                                                        <div class="modal-header">
                                                            <h5 class="modal-title">Edit Brand</h5>
                                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                        </div>
                                                        <div class="modal-body">
                                                            <input type="hidden" name="action" value="update">
                                                            <input type="hidden" name="brandID" value="${brand.brandID}">
                                                            <div class="mb-3">
                                                                <label class="form-label">Brand Name</label>
                                                                <input type="text" class="form-control" name="brandName" value="${brand.brandName}" required>
                                                            </div>
                                                            <div class="mb-3">
                                                                <label class="form-label">Origin Brand</label>
                                                                <input type="text" class="form-control" name="originBrand" value="${brand.originBrand}">
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
                        Are you sure you want to delete this brand?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <form id="deleteBrandForm" method="POST" action="BrandServlet">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="brandID" id="deleteBrandID">
                            <button type="submit" class="btn btn-danger">Delete</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <script>
            function setDeleteBrandID(brandID) {
                document.getElementById("deleteBrandID").value = brandID;
            }

            <c:if test="${not empty sessionScope.errorMessage}">
        var errorModal = new bootstrap.Modal(document.getElementById("errorModal"));
        errorModal.show();
        </c:if>
        </script>
    </body>
</html>
