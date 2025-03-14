<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="" />
        <meta name="author" content="" />
        <title>Dashboard - Order Management</title>
        <!-- External CSS and JS -->
        <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="CSS/style.css" />
        <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script src="JS/SideBarToggle.js"></script>
    </head>
    <body class="sb-nav-fixed">
        <c:choose>
            <c:when test="${empty sessionScope.roleID || sessionScope.roleID eq 4}">
                <c:redirect url="/Admin/loginPage.jsp"/>
            </c:when>
            <c:when test="${sessionScope.roleID eq 3}">
                <c:redirect url="/LogoutServlet"/>
            </c:when>
        </c:choose>
        <!-- Sidebar (Included) -->
        <%@ include file="Component/SideBar.jsp" %>
        <div id="layoutSidenav">
            <div id="layoutSidenav_content">
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

                        <!-- Search Forms in a single row -->
                        <div class="row mb-4">
                            <!-- Search by Customer Name -->
                            <div class="col-md-4">
                                <form action="${pageContext.request.contextPath}/Admin/OrderServlet" method="POST" class="d-flex">
                                    <input type="hidden" name="action" value="search">
                                    <input type="text" name="customerName" class="form-control" placeholder="Enter Customer Name" required>
                                    <button class="btn btn-outline-secondary" type="submit">
                                        <i class="fas fa-search"></i>
                                    </button>
                                    <a href="${pageContext.request.contextPath}/Admin/OrderServlet" class="btn btn-outline-danger">
                                        <i class="fas fa-sync"></i>
                                    </a>
                                </form>
                            </div>

                            <!-- Search by Minimum Amount -->
                            <div class="col-md-4">
                                <form action="${pageContext.request.contextPath}/Admin/OrderView" method="POST" class="d-flex">
                                    <input type="hidden" name="action" value="money">
                                    <input type="text" name="minAmount" class="form-control" placeholder="Enter Minimum Amount" required>
                                    <button class="btn btn-outline-secondary" type="submit">
                                        <i class="fas fa-search"></i> 
                                    </button>
                                    <a href="${pageContext.request.contextPath}/Admin/OrderServlet" class="btn btn-outline-danger">
                                        <i class="fas fa-sync"></i>
                                    </a>
                                </form>
                            </div>

                            <!-- Sort Orders -->
                            <div class="col-md-4">
                                <form action="${pageContext.request.contextPath}/Admin/OrderServlet" method="POST" class="d-flex">
                                    <input type="hidden" name="action" value="sort">
                                    <select name="sortOrder" class="form-control">
                                        <option value="ASC">Sort by Amount (Ascending)</option>
                                        <option value="DESC">Sort by Amount (Descending)</option>
                                    </select>
                                    <button class="btn btn-outline-secondary" type="submit">
                                        <i class="fas fa-sort"></i> 
                                    </button>
                                    <a href="${pageContext.request.contextPath}/Admin/OrderServlet" class="btn btn-outline-danger">
                                        <i class="fas fa-sync"></i> 
                                    </a>
                                </form>
                            </div>
                        </div>

                        <!-- Order List Table -->
                        <div class="card mb-4">
                            <div class="card-header">
                                <div class="d-flex justify-content-between align-items-center">
                                    <i class="fas fa-truck me-1"></i> Order List
                                </div>
                            </div>
                            <div class="card-body">
                                <div class="table-responsive">
                                    <table class="table table-bordered table-striped table-hover">
                                        <thead class="table-dark text-center">
                                            <tr>
                                                <th>Order ID</th>
                                                <th>Customer Name</th>
                                                <th>Payment Method</th>
                                                <th>Order Date</th>
                                                <th>Status</th>
                                                <th>Total Amount</th>
                                                <th>Update</th>
                                                <th>Action</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${empty listO}">
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
                                                                <button class="btn btn-sm btn-danger" data-bs-toggle="modal" data-bs-target="#confirmDeleteModal" onclick="setDeleteOrderID(${o.orderId})">
                                                                    <i class="fas fa-trash"></i>
                                                                </button>
                                                                <a href="${pageContext.request.contextPath}/Admin/OrderView?action=view&orderId=${o.orderId}" class="btn btn-sm btn-info">
                                                                    <i class="fas fa-eye"></i>
                                                                </a>
                                                                <button class="btn btn-sm btn-warning" data-bs-toggle="modal" data-bs-target="#updateStatusModal" 
                                                                        onclick="setUpdateOrderID(${o.orderId}, '${o.status}')">
                                                                    <i class="fas fa-edit"></i>
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

                        <!-- Pagination -->
                        <nav class="pagination">
                            <ul class="pagination justify-content-center">
                                <c:forEach begin="1" end="${endPage}" var="i">
                                    <li class="page-item <c:if test='${i == currentPage}'>active</c:if>">
                                        <a class="page-link" href="${pageContext.request.contextPath}/Admin/OrderServlet?index=${i}">${i}</a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </nav>

                    </div>
                </main>
            </div>
        </div>

        <!-- Delete Confirmation Modal -->
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
                        <form id="deleteForm" method="POST" action="OrderServlet">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="orderId" id="deleteOrderID">
                            <button type="submit" class="btn btn-danger">Delete</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <!-- Update Status Modal -->
        <div class="modal fade" id="updateStatusModal" tabindex="-1" aria-labelledby="updateStatusModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="updateStatusModalLabel">Update Order Status</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form id="updateStatusForm" action="${pageContext.request.contextPath}/Admin/OrderServlet" method="POST">
                            <input type="hidden" name="action" value="updateStatus">
                            <input type="hidden" name="orderId" id="updateOrderID">

                            <div class="mb-3">
                                <label for="newStatus" class="form-label">Select New Status</label>
                                <select class="form-select" name="status" id="newStatus" required>
                                    <option value="Pending">Pending</option>
                                    <option value="Shipped">Shipped</option>
                                    <option value="Delivered">Delivered</option>
                                    <option value="Cancelled">Cancelled</option>
                                </select>
                            </div>

                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                <button type="submit" class="btn btn-primary">Update</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <!-- Script to Set Order ID for Deletion -->
        <script>
            function setDeleteOrderID(orderID) {
                document.getElementById('deleteOrderID').value = orderID;
            }
        </script>
        <script>
            function setUpdateOrderID(orderID, currentStatus) {
                console.log("Order ID set for update:", orderID);  // Debug xem orderId có đúng không
                document.getElementById('updateOrderID').value = orderID;
                document.getElementById('newStatus').value = currentStatus;
            }
        </script>

    </body>
</html>


