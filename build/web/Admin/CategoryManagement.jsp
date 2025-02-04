<%@page import="java.sql.Connection"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.SQLException"%>
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
        <%@ include file="Component/SideBar.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Category Management</h1>
                    <ol>
                        <li class="breadcrumb-item active">View Category</li>
                    </ol>

                    <div class="container d-flex" style="justify-content: space-between;" >
                        <form action="CategoryServlet" method="POST">
                            <input type="hidden" name="action" value="add">
                            <label>Category Name</label>
                            <input type="text" name="categoryName" required />
                            <button class="btn btn-primary ms-2" type="submit">Add Category</button>

                        </form>

                        <form action="CategoryServlet" method="POST" >
                            <input type="hidden" name="action" value="list">
                            <button class="btn btn-primary ms-2" type="submit">Refresh</button>
                        </form>
                    </div>

                    <div class="card mb-4">
                        <div class="card-header"><i class="fas fa-table me-1"></i>Category List</div>

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

                                    <!-- Hiển thị thông báo nếu danh sách trống -->
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
                                                <button class="btn btn-sm btn-primary custom-width-btn" data-bs-toggle="modal" data-bs-target="#editCategoryModal-${category.categoryID}">Update</button>
                                                <form method="post" action="CategoryServlet" class="d-inline">
                                                    <input type="hidden" name="action" value="delete">
                                                    <input type="hidden" name="categoryID" value="${category.categoryID}">
                                                    <button type="button" class="btn btn-sm btn-danger custom-width-btn"
                                                            data-bs-toggle="modal"
                                                            data-bs-target="#confirmDeleteModal"
                                                            onclick="setDeleteCategoryID(${category.categoryID})">
                                                        Delete
                                                    </button>
                                                </form>
                                            </td>
                                        </tr>

                                        <!-- Modal Edit -->
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



        <!-- Modal Xác nhận Xóa (đặt trước </body>) -->
        <div class="modal fade" id="confirmDeleteModal" tabindex="-1" aria-labelledby="confirmDeleteModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="confirmDeleteModalLabel">Confirm Deletion</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        Are you sure you want to delete this category?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <!-- Form gửi yêu cầu xóa -->
                        <form id="deleteCategoryForm" method="POST" action="CategoryServlet">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="categoryID" id="deleteCategoryID">
                            <button type="submit" class="btn btn-danger">Delete</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>


        <!-- Modal Chỉnh sửa -->
        <div class="modal fade" id="editCategoryModal-${category.categoryID}" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Edit Category</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" id="editCategoryID-${category.categoryID}" value="${category.categoryID}">
                        <div class="mb-3">
                            <label class="form-label">Category Name</label>
                            <input type="text" class="form-control" id="editCategoryName-${category.categoryID}" value="${category.name}" required>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="button" class="btn btn-primary" onclick="confirmUpdate(${category.categoryID})">
                            Save Changes
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <!-- Modal Thông báo lỗi -->
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

        <!-- Modal Thông báo thành công -->
        <div class="modal fade" id="successModal" tabindex="-1" aria-labelledby="successModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header bg-success text-white">
                        <h5 class="modal-title" id="successModalLabel">Success</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <p id="successMessage">${sessionScope.successMessage}</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>


        <script>
            function confirmUpdate(categoryID) {
                let categoryName = document.getElementById("editCategoryName-" + categoryID).value;

                // Cập nhật dữ liệu vào modal xác nhận cập nhật
                document.getElementById("updateCategoryID").value = categoryID;
                document.getElementById("updateCategoryName").value = categoryName;

                // Hiển thị modal xác nhận
                let updateModal = new bootstrap.Modal(document.getElementById("confirmUpdateModal"));
                updateModal.show();
            }

            function setDeleteCategoryID(categoryID) {
                document.getElementById("deleteCategoryID").value = categoryID;
            }




        </script>

        <script>
            // Kiểm tra nếu có lỗi thì hiển thị modal lỗi
            <c:if test="${not empty sessionScope.errorMessage}">
            var errorModal = new bootstrap.Modal(document.getElementById("errorModal"));
            errorModal.show();
            </c:if>

            // Kiểm tra nếu có thông báo thành công thì hiển thị modal thành công
            <c:if test="${not empty sessionScope.successMessage}">
            var successModal = new bootstrap.Modal(document.getElementById("successModal"));
            successModal.show();
            </c:if>

            // Xóa thông báo lỗi sau khi hiển thị modal
            window.onload = function () {
            <c:if test="${not empty sessionScope.errorMessage}">
                setTimeout(function () {
                <c:remove var="errorMessage" scope="session"/>
                }, 1000);
            </c:if>
            <c:if test="${not empty sessionScope.successMessage}">
                setTimeout(function () {
                <c:remove var="successMessage" scope="session"/>
                }, 1000);
            </c:if>
            };
        </script>



    </div> 
</body>
</html>
