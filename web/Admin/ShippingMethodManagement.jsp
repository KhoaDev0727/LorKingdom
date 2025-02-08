<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <title>Shipping Method Management</title>
        <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="CSS/style.css" />
        <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script src="JS/SideBarToggle.js"></script>
        <link rel="stylesheet" href="CSS/ModalAddMaterial.css"/>
    </head>

    <body class="sb-nav-fixed">
        <%@ include file="Component/SideBar.jsp" %>

        <div id="layoutSidenav_content">
            <main>
                <div class="header container-fluid px-4">
                    <h1 class="mt-4">Shipping Method Management</h1>
                    <style>
                        .header{
                            margin-top: 70px;
                        }
                    </style>



                    <div class="card mb-4">
                        <div class="card-header"><i class="fas fa-table me-1"></i> Shipping Method List</div>

                        <div class="d-flex align-items-center p-3 border rounded bg-light" style="max-width: 100%; gap: 15px;">
                            <!-- Form tìm kiếm -->
                            <form method="GET" action="ShippingMethodServlet" class="d-flex align-items-center flex-grow-1" style="gap: 10px;">
                                <input name="action" type="hidden" value="search">
                                <input type="text" name="search" class="form-control flex-grow-1" 
                                       placeholder="Find Shipping Method..." aria-label="Search"
                                       style="border-radius: 8px; padding: 10px; font-size: 16px;">
                                <button class="btn btn-primary taskFind" type="submit" 
                                        style="border-radius: 8px; padding: 10px 14px; background-color: #007bff; border-color: #007bff;">
                                    <i class="fas fa-search"></i>
                                </button>
                            </form>

                            <!-- Nút thêm phương thức vận chuyển -->
                            <button class="btn btn-success" data-bs-toggle="modal" data-bs-target="#addShippingMethodModal"
                                    style="border-radius: 8px; padding: 10px 14px; background-color: #28a745; border-color: #28a745;">
                                <i class="fas fa-plus"></i>
                            </button>

                            <!-- Nút refresh -->
                            <form action="<%= request.getContextPath() %>/Admin/ShippingMethodServlet" method="POST">
                                <input type="hidden" name="action" value="list">
                                <button class="btn btn-warning" type="submit" 
                                        style="border-radius: 8px; padding: 10px 14px; background-color: #ffc107; border-color: #ffc107;">
                                    <i class="fas fa-sync-alt"></i>
                                </button>
                            </form>

                        </div>

                        <!-- Modal Thêm Shipping Method -->
                        <div class="modal fade" id="addShippingMethodModal" tabindex="-1" aria-labelledby="addShippingMethodModalLabel" aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <form action="ShippingMethodServlet" method="POST">
                                        <div class="modal-header bg-primary text-white">
                                            <h5 class="modal-title" id="addShippingMethodModalLabel">Add Shipping Method</h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                        </div>

                                        <div class="modal-body">
                                            <input type="hidden" name="action" value="add">

                                            <div class="mb-3">
                                                <label for="methodName" class="form-label">Method Name:</label>
                                                <input type="text" id="methodName" name="methodName" class="form-control" required>
                                            </div>

                                            <div class="mb-3">
                                                <label for="price" class="form-label">Price:</label>
                                                <input type="number" id="price" name="price" step="0.01" class="form-control" required>
                                            </div>

                                            <div class="mb-3">
                                                <label for="description" class="form-label">Description:</label>
                                                <textarea id="description" name="description" rows="4" class="form-control" required></textarea>
                                            </div>
                                        </div>

                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                            <button type="submit" class="btn btn-primary">Add Shipping Method</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>

                        <div class="card-body">
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <th>Shipping Method ID</th>
                                        <th>Method Name</th>
                                        <th>Price</th>
                                        <th>Description</th>
                                    </tr>
                                </thead>
                                
                                <tbody>
                                    <c:if test="${empty shippingMethods}">
                                        <tr>
                                            <td colspan="5" class="text-center text-muted">No shipping methods available.</td>
                                        </tr>
                                    </c:if>

                                    <c:forEach var="shippingMethod" items="${shippingMethods}">
                                        <tr>
                                            <td>${shippingMethod.shippingMethodID}</td>
                                            <td>${shippingMethod.methodName}</td>
                                            <td>${shippingMethod.price}</td>
                                            <td>${shippingMethod.description}</td>
                                            <td>
                                                <!-- Edit Button -->
                                                <button class="btn btn-sm btn-primary" 
                                                        data-bs-toggle="modal" 
                                                        data-bs-target="#editShippingMethodModal-${shippingMethod.shippingMethodID}">
                                                    Update
                                                </button>

                                                <form method="post" action="ShippingMethodServlet" class="d-inline">
                                                    <input type="hidden" name="action" value="delete">
                                                    <input type="hidden" name="shippingMethodID" value="${shippingMethod.shippingMethodID}">
                                                    <button type="button" class="btn btn-sm btn-danger"
                                                            data-bs-toggle="modal"
                                                            data-bs-target="#confirmDeleteModal"
                                                            onclick="setDeleteShippingMethodID(${shippingMethod.shippingMethodID})">
                                                        Delete
                                                    </button>
                                                </form>
                                            </td>
                                        </tr>

                                    <div class="modal fade" id="editShippingMethodModal-${shippingMethod.shippingMethodID}" tabindex="-1">
                                        <div class="modal-dialog">
                                            <div class="modal-content">
                                                <form method="post" action="ShippingMethodServlet">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title">Edit Shipping Method</h5>
                                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                    </div>
                                                    <div class="modal-body">
                                                        <input type="hidden" name="action" value="update">
                                                        <input type="hidden" name="shippingMethodID" value="${shippingMethod.shippingMethodID}">
                                                        <div class="mb-3">
                                                            <label class="form-label">Method Name</label>
                                                            <input type="text" class="form-control" name="methodName" value="${shippingMethod.methodName}" required>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label class="form-label">Price</label>
                                                            <input type="number" class="form-control" name="price" value="${shippingMethod.price}" step="0.01" required>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label class="form-label">Description</label>
                                                            <textarea class="form-control" name="description" rows="4" required>${shippingMethod.description}</textarea>
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

        <div class="modal fade" id="confirmDeleteModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Confirm Deletion</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        Are you sure you want to delete this shipping method?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <form id="deleteShippingMethodForm" method="POST" action="ShippingMethodServlet">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="shippingMethodID" id="deleteShippingMethodID">
                            <button type="submit" class="btn btn-danger">Delete</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <script>
            function setDeleteShippingMethodID(shippingMethodID) {
                document.getElementById("deleteShippingMethodID").value = shippingMethodID;
            }
            <c:if test="${not empty sessionScope.errorMessage}">
            var errorModal = new bootstrap.Modal(document.getElementById("errorModal"));
            errorModal.show();
            </c:if>

            window.onload = function () {
            <c:if test="${not empty sessionScope.errorMessage}">
                setTimeout(function () {
                <c:remove var="errorMessage" scope="session" />
                }, 1000);
            </c:if>
            };
        </script>
    </body>
</html>  