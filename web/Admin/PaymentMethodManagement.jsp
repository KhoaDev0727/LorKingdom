<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <title>Dashboard - Payment Method Management</title>
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
                    <main>
                        <div class="container-fluid px-5">
                            <h1 class="mt-4">Payment Method Management</h1>

                            <c:if test="${not empty message}">
                                <div class="alert alert-${messageType} alert-dismissible fade show" role="alert">
                                    ${message}
                                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                                </div>
                            </c:if>

                            <div class="card mb-4">
                                <div class="card-header d-flex justify-content-between align-items-center">
                                    <div>
                                        <i class="fas fa-credit-card me-1"></i> Payment Methods List
                                    </div>
                                    <button class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#addPaymentMethodModal">
                                        <i class="fas fa-plus"></i> Add Payment Method
                                    </button>
                                </div>
                                <div class="card-body">
                                    <form action="PaymentMethodServlet" method="GET" class="mb-4">
                                        <div class="input-group">
                                            <input type="hidden" name="action" value="search">
                                            <input type="text" name="search" class="form-control" placeholder="Search Payment Method..." value="${param.search}">
                                            <button class="btn btn-outline-secondary" type="submit">
                                                <i class="fas fa-search"></i>
                                            </button>
                                            <a href="PaymentMethodServlet?action=list" class="btn btn-outline-danger">
                                                <i class="fas fa-sync"></i>
                                            </a>
                                        </div>
                                    </form>

                                    <div class="table-responsive">
                                        <table class="table table-bordered table-striped table-hover">
                                            <thead class="table-dark text-center">
                                                <tr>
                                                    <th>ID</th>
                                                    <th>Payment Method Name</th>
                                                    <th>Description</th>
                                                    <th>Actions</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:choose>
                                                    <c:when test="${empty paymentMethods}">
                                                        <tr>
                                                            <td colspan="4" class="text-center text-muted">No payment methods found</td>
                                                        </tr>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach var="method" items="${paymentMethods}">
                                                            <tr>
                                                                <td>${method.paymentMethodID}</td>
                                                                <td>${method.methodName}</td>
                                                                <td>${method.description}</td>
                                                                <td>
                                                                    <button class="btn btn-sm btn-warning" 
                                                                            data-bs-toggle="modal" 
                                                                            data-bs-target="#editPaymentModal${method.paymentMethodID}">
                                                                        <i class="fas fa-edit"></i>
                                                                    </button>

                                                                    <button class="btn btn-sm btn-danger" data-bs-toggle="modal" 
                                                                            data-bs-target="#confirmDeleteModal" 
                                                                            onclick="setDeletePaymentMethodID(${method.paymentMethodID})">
                                                                        <i class="fas fa-trash"></i>
                                                                    </button>
                                                                </td>
                                                            </tr>

                                                            <!-- Edit Modal -->
                                                        <div class="modal fade" id="editPaymentModal${method.paymentMethodID}">
                                                            <div class="modal-dialog">
                                                                <div class="modal-content">
                                                                    <form action="PaymentMethodServlet" method="POST">
                                                                        <input type="hidden" name="action" value="update">
                                                                        <input type="hidden" name="paymentMethodID" value="${method.paymentMethodID}">
                                                                        <div class="modal-header">
                                                                            <h5 class="modal-title">Edit Payment Method</h5>
                                                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                                        </div>
                                                                        <div class="modal-body">
                                                                            <div class="mb-3">
                                                                                <label class="form-label">Method Name</label>
                                                                                <input type="text" class="form-control" name="methodName" value="${method.methodName}" required>
                                                                            </div>
                                                                            <div class="mb-3">
                                                                                <label class="form-label">Description</label>
                                                                                <textarea class="form-control" name="description" rows="3">${method.description}</textarea>
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
                                                        <div class="modal fade" id="confirmDeleteModal">
                                                            <div class="modal-dialog">
                                                                <div class="modal-content">
                                                                    <div class="modal-header">
                                                                        <h5 class="modal-title">Confirm Deletion</h5>
                                                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                                    </div>
                                                                    <div class="modal-body">Are you sure you want to delete this payment method?</div>
                                                                    <div class="modal-footer">
                                                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                                                        <form id="deleteForm" method="POST" action="PaymentMethodServlet">
                                                                            <input type="hidden" name="action" value="delete">
                                                                            <input type="hidden" name="paymentMethodID" id="deletePaymentMethodID">
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
                        </div> 
                    </main>
                </div>
            </div>
        </div>

        <!-- Add Payment Method Modal -->
        <div class="modal fade" id="addPaymentMethodModal">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="PaymentMethodServlet" method="POST">
                        <input type="hidden" name="action" value="add">
                        <div class="modal-header">
                            <h5 class="modal-title">Add New Payment Method</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <div class="mb-3">
                                <label for="methodName" class="form-label">Method Name</label>
                                <input type="text" class="form-control" id="methodName" name="methodName" required>
                            </div>
                            <div class="mb-3">
                                <label for="description" class="form-label">Description</label>
                                <textarea class="form-control" id="description" name="description" rows="3" required></textarea>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button type="submit" class="btn btn-primary">Add Payment Method</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="modal fade" id="errorModal" tabindex="-1" aria-labelledby="errorModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header bg-danger text-white">
                        <h5 class="modal-title" id="errorModalLabel">Error</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body text-dark">
                        ${errorMessage}
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>

        <script>
            // Nếu có lỗi, hiển thị modal
            window.onload = function () {
                let errorMessage = "${sessionScope.errorMessage}";
                if (errorMessage && errorMessage.trim() !== "") {
                    let errorModal = new bootstrap.Modal(document.getElementById('errorModal'));
                    errorModal.show();
            <% request.getSession().removeAttribute("errorMessage"); %>  // Xóa thông báo lỗi sau khi hiển thị
                }
            };
        </script>

        <script>
            function setDeletePaymentMethodID(paymentMethodID) {
                document.getElementById('deletePaymentMethodID').value = paymentMethodID;
            }
        </script>
    </body>
</html>
