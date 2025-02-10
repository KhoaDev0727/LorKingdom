<%-- 
    Document   : PriceRangeManagement
    Created on : Feb 10, 2025, 11:55:11 AM
    Author     : Truong Van Khang _ CE181852
--%>

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
    <div id="layoutSidenav">
        <div id="layoutSidenav_content">
            <!-- Side Bar -->
            <%@ include file="Component/SideBar.jsp" %>
            <div class="dashboard-container">
                <!-- Main Content -->
                <main>
                    <div class="container-fluid px-5">
                        <h1 class="mt-4">Price Range Management</h1>
                        <!-- Success/Error Messages -->
                        <c:if test="${not empty message}">
                            <div class="alert alert-${messageType} alert-dismissible fade show" role="alert">
                                ${message}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>

                        <div class="card mb-4">
                            <div class="card-header">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <i class="fas fa-table me-1"></i> Price Range List
                                    </div>
                                    <div>
                                        <button class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#addPriceRangeModal">
                                            <i class="fas fa-plus"></i> Add Price Range
                                        </button>
                                    </div>
                                </div>
                            </div>

                            <div class="card-body">
                                <!-- Search Form -->
                                <form action="PriceRangeManagementServlet" method="GET" class="mb-4">
                                    <div class="input-group">
                                        <input type="hidden" name="action" value="search">
                                        <input type="text" name="search" class="form-control" placeholder="Search Price Range by (ID, Price From, Price To)..." 
                                               value="${param.search}">
                                        <button class="btn btn-outline-secondary" type="submit">
                                            <i class="fas fa-search"></i>
                                        </button>
                                        <a href="PriceRangeManagementServlet" class="btn btn-outline-danger">
                                            <i class="fas fa-sync"></i>
                                        </a>
                                    </div>
                                </form>

                                <!-- Price Range Table -->
                                <div class="table-responsive">
                                    <table class="table table-bordered table-striped table-hover">
                                        <thead class="table-dark">
                                            <tr>
                                                <th>ID</th>
                                                <th>Price From</th>
                                                <th>Price To</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${empty priceranges}">
                                                    <tr>
                                                        <td colspan="4" class="text-center text-muted">No Price Range Found</td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="s" items="${priceranges}">
                                                        <tr>
                                                            <td>${s.priceRangeID}</td>
                                                            <td>${s.priceFrom}</td>
                                                            <td>${s.priceTo}</td>
                                                            <td>
                                                                <button class="btn btn-sm btn-warning" 
                                                                        data-bs-toggle="modal" 
                                                                        data-bs-target="#editPriceRangeModal${s.priceRangeID}">
                                                                    <i class="fas fa-edit"></i>
                                                                </button>
                                                                <button class="btn btn-sm btn-danger" 
                                                                        data-bs-toggle="modal" 
                                                                        data-bs-target="#deleteModal${s.priceRangeID}">
                                                                    <i class="fas fa-trash"></i>
                                                                </button>
                                                            </td>
                                                        </tr>

                                                        <!-- Edit Price Range Modal -->
                                                        <div class="modal fade" id="editPriceRangeModal${s.priceRangeID}" tabindex="-1" aria-labelledby="editPriceRangeModalLabel${s.priceRangeID}" aria-hidden="true">
                                                            <div class="modal-dialog modal-lg">
                                                                <div class="modal-content">
                                                                    <form action="PriceRangeManagementServlet" method="POST">
                                                                        <input type="hidden" name="action" value="update">
                                                                        <input type="hidden" name="priceRangeID" value="${s.priceRangeID}">
                                                                        <div class="modal-header">
                                                                            <h5 class="modal-title" id="editPriceRangeModalLabel${s.priceRangeID}">Edit Price Range</h5>
                                                                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                                        </div>
                                                                        <div class="modal-body">
                                                                            <div class="row g-3">
                                                                                <div class="col-md-6">
                                                                                    <label class="form-label">Price From</label>
                                                                                    <input type="text" class="form-control" name="priceFrom" 
                                                                                           value="${s.priceFrom}" pattern="^[0-9]+(\.[0-9]{1,2})?$" required>
                                                                                </div>
                                                                                <div class="col-md-6">
                                                                                    <label class="form-label">Price To</label>
                                                                                    <input type="text" class="form-control" name="priceTo" 
                                                                                           value="${s.priceTo}" pattern="^[0-9]+(\.[0-9]{1,2})?$" required>
                                                                                </div>
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

                                                        <!-- Delete Confirmation Modal -->
                                                        <div class="modal fade" id="deleteModal${s.priceRangeID}" tabindex="-1" aria-labelledby="deleteModalLabel${s.priceRangeID}" aria-hidden="true">
                                                            <div class="modal-dialog">
                                                                <div class="modal-content">
                                                                    <div class="modal-header">
                                                                        <h5 class="modal-title" id="deleteModalLabel${s.priceRangeID}">Confirm Delete</h5>
                                                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                                    </div>
                                                                    <div class="modal-body">
                                                                        Are you sure you want to delete Price Range with ID: 
                                                                        <strong>${s.priceRangeID}</strong>?
                                                                    </div>
                                                                    <div class="modal-footer">
                                                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                                                        <form action="PriceRangeManagementServlet" method="GET" class="d-inline">
                                                                            <input type="hidden" name="action" value="delete">
                                                                            <input type="hidden" name="priceRangeID" value="${s.priceRangeID}">
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

                        <!-- Add Price Range Modal -->
                        <div class="modal fade" id="addPriceRangeModal" tabindex="-1" aria-labelledby="addPriceRangeModalLabel" aria-hidden="true">
                            <div class="modal-dialog modal-lg">
                                <div class="modal-content">
                                    <form action="PriceRangeManagementServlet" method="POST">
                                        <input type="hidden" name="action" value="add">
                                        <div class="modal-header">
                                            <h5 class="modal-title" id="addPriceRangeModalLabel">Add Price Range</h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                        </div>
                                        <div class="modal-body">
                                            <div class="row g-3">
                                                <div class="col-md-6">
                                                    <label class="form-label">Price From</label>
                                                    <input type="text" class="form-control" name="priceFrom" pattern="^[0-9]+(\.[0-9]{1,2})?$" required>
                                                </div>
                                                <div class="col-md-6">
                                                    <label class="form-label">Price To</label>
                                                    <input type="text" class="form-control" name="priceTo" pattern="^[0-9]+(\.[0-9]{1,2})?$" required>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                            <button type="submit" class="btn btn-primary">Add Price Range</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>

                    </div> 
                </main>
            </div>
        </div>
    </div>
</body>
</html>
