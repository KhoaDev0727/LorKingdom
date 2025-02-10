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
        <title>Dashboard - Order Management</title>
        <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="CSS/style.css" />
        <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script src="JS/SideBarToggle.js"></script>
    </head>
    <body class="sb-nav-fixed">
        <%@ include file="Component/SideBar.jsp" %>
        <div id="layoutSidenav">
            <div id="layoutSidenav_content">
                <div class="dashboard-container">
                    <!-- Table -->
                    <main>
                        <div class="container-fluid px-5">
                            <h1 class="mt-4">Order Management</h1>
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
                                            <i class="fas fa-truck me-1"></i> Order List
                                        </div>
                                    </div>
                                </div>

                                <div class="card-body">
                                    <!-- Search Form -->
                                    <form action="OrderServlet" method="POST" class="mb-4">
                                        <div class="input-group">
                                            <input type="hidden" name="action" value="search">
                                            <input type="text" name="customerName" class="form-control" placeholder="Enter Customer Name" required">
                                            <button class="btn btn-outline-secondary" type="submit">
                                                <i class="fas fa-search"></i>
                                            </button>
                                            <a href="ShippingMethodServlet?action=list" class="btn btn-outline-danger">
                                                <i class="fas fa-sync"></i>
                                            </a>
                                        </div>
                                    </form>

                                    <!-- Shipping Method Table -->
                                    <div class="table-responsive">
                                        <table class="table table-bordered table-striped table-hover">
                                            <thead class="table-dark text-center">
                                                <tr>
                                                    <th>Order ID</th>
                                                    <th>CustomerName</th>
                                                    <th>Payment Method</th>
                                                    <th>Order Date</th>
                                                    <th>Status</th>
                                                    <th>ToTal Money</th>
                                                    <th>Update</th>
                                                    <th>Action</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:choose>
                                                    <c:when test="${empty Order}">
                                                        <tr>
                                                            <td colspan="8" class="text-center text-muted">No order found</td>
                                                        </tr>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach items="${listO}" var="o">
                                                            <tr>
                                                                <td>${o.orderId}</td>
                                                                <td>${o.accountName}</td>
                                                                <td>${o.payMentMethodName}</td>
                                                                <td>${o.orderDate}</td>
                                                                <td>${o.status}</td>
                                                                <td>${o.totalAmount}</td>
                                                                <td>${o.updatedAt}</td>
                                                                <td>
                                                                    <button class="btn btn-sm btn-danger" 
                                                                            data-bs-toggle="modal" 
                                                                            data-bs-target="#confirmDeleteModal"
                                                                            onclick="setDeleteOrderID(${o.orderID})">
                                                                        <i class="fas fa-trash"></i>
                                                                    </button>
                                                                </td>
                                                            </tr>
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
        <!-- Delete Confirmation Modal for Order -->
        <div class="modal fade" id="confirmDeleteModal" tabindex="-1" aria-labelledby="confirmDeleteModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="confirmDeleteModalLabel">Confirm Deletion</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        Are you sure you want to delete this order?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <form id="deleteForm" method="POST" action="orderServlet">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="orderID" id="deleteOrderID">
                            <button type="submit" class="btn btn-danger">Delete</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <script>

            function setDeleteOrderID(orderID) {
                document.getElementById('deleteOrderID').value = orderID;
            }

            document.addEventListener('DOMContentLoaded', function () {
                var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
                var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
                    return new bootstrap.Tooltip(tooltipTriggerEl, {
                        html: true
                    });
                });
            });
        </script>
        <!-- Phân trang -->
        <nav class="pagination">
            <ul>
                <c:forEach begin="1" end="${endPage}" var="i">
                    <li class="page-item <c:if test='${i == currentPage}'>active</c:if>">
                        <a class="page-link" href="orderServlet?index=${i}">${i}</a>
                    </li>
                </c:forEach>
            </ul>
        </nav>

    </body>
</html>
