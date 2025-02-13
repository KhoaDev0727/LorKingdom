<%-- 
    Document   : PriceRangeManagement
    Created on : Feb 12, 2025, 11:09:19 AM
    Author     : admin1
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <title>Price Range Management</title>
        <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="CSS/style.css" />
        <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
    </head>
    <body class="sb-nav-fixed">
        <%@ include file="Component/SideBar.jsp" %>
        <div id="layoutSidenav_content">
            <main>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Price Range Management</h1>
                    <ol class="breadcrumb mb-4">
                        <li class="breadcrumb-item active">Manage Price Ranges</li>
                    </ol>

                    <div class="container d-flex justify-content-between">
                        <!-- Form thêm Price Range -->
                        <form action="PriceRangeServlet" method="POST">
                            <input type="hidden" name="action" value="add">
                            <label for="priceRange">Price Range</label>
                            <input type="text" id="priceRange" name="priceRange" placeholder="Enter price range" required />
                            <button class="btn btn-primary ms-2" type="submit">Add Price Range</button>
                        </form>

                        <!-- Nút Refresh -->
                        <form action="PriceRangeServlet" method="POST">
                            <input type="hidden" name="action" value="list">
                            <button class="btn btn-primary ms-2" type="submit">Refresh</button>
                        </form>
                    </div>

                    <!-- Danh sách Price Range -->
                    <div class="card mb-4">
                        <div class="card-header"><i class="fas fa-table me-1"></i> Price Range List</div>

                        <!-- Form tìm kiếm -->
                        <form method="GET" action="PriceRangeServlet" class="d-flex">
                            <input name="action" type="hidden" value="search">
                            <input type="text" name="search" class="form-control" placeholder="Find Price Range..." aria-label="Search">
                            <button class="btn btn-primary ms-2 taskFind" type="submit">Search</button>
                        </form>

                        <div class="card-body">
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <th>Price Range ID</th>
                                        <th>Price Range</th>
                                        <th>Date Created</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <!-- Hiển thị thông báo nếu danh sách rỗng -->
                                <c:if test="${empty priceRanges}">
                                    <tr>
                                        <td colspan="4" class="text-center text-muted">No price ranges available.</td>
                                    </tr>
                                </c:if>

                                <!-- Duyệt danh sách Price Range -->
                                <c:forEach var="priceRange" items="${priceRanges}">
                                    <tr>
                                        <td>${priceRange.priceRangeID}</td>
                                        <td>${priceRange.priceRange}</td>
                                        <td>${priceRange.createdAt}</td>
                                        <td>
                                            <!-- Nút Update -->
                                            <button class="btn btn-sm btn-primary" 
                                                    data-bs-toggle="modal" 
                                                    data-bs-target="#editPriceRangeModal-${priceRange.priceRangeID}">
                                                Update
                                            </button>

                                            <!-- Nút Delete -->
                                            <button class="btn btn-sm btn-danger"
                                                    data-bs-toggle="modal"
                                                    data-bs-target="#confirmDeleteModal"
                                                    onclick="setDeletePriceRangeID(${priceRange.priceRangeID})">
                                                Delete
                                            </button>
                                        </td>
                                    </tr>

                                    <!-- Modal Edit Price Range -->
                                    <div class="modal fade" id="editPriceRangeModal-${priceRange.priceRangeID}" tabindex="-1">
                                        <div class="modal-dialog">
                                            <div class="modal-content">
                                                <form method="post" action="PriceRangeServlet">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title">Edit Price Range</h5>
                                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                    </div>
                                                    <div class="modal-body">
                                                        <input type="hidden" name="action" value="update">
                                                        <input type="hidden" name="priceRangeID" value="${priceRange.priceRangeID}">
                                                        <div class="mb-3">
                                                            <label class="form-label">Price Range</label>
                                                            <input type="text" class="form-control" name="priceRange" value="${priceRange.priceRange}" required>
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

        <!-- Modal xác nhận xóa Price Range -->
        <div class="modal fade" id="confirmDeleteModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Confirm Deletion</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        Are you sure you want to delete this price range?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <form id="deletePriceRangeForm" method="POST" action="PriceRangeServlet">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="priceRangeID" id="deletePriceRangeID">
                            <button type="submit" class="btn btn-danger">Delete</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- JavaScript hỗ trợ thiết lập ID cho delete -->
        <script>
            function setDeletePriceRangeID(priceRangeID) {
                document.getElementById("deletePriceRangeID").value = priceRangeID;
            }
        </script>
    </body>
</html>
