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
        <title>Price Range Management</title>
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
                            <h1 class="mt-4">Price Range Management</h1>
                            <!-- Form thêm Price Range -->
                            <form action="PriceRangeServlet" method="POST">
                                <input type="hidden" name="action" value="add">
                                <label for="priceRange">Price Range</label>
                                <input type="text" id="priceRange" name="priceRange" placeholder="Enter price range" required />
                                <button class="btn btn-primary ms-2" type="submit">Add Price Range</button>
                            </form>
                            <div class="card mb-4">
                                <div class="card-header">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div>
                                            <i class="fas fa-table me-1"></i> Origin List
                                        </div>
                                    </div>
                                </div>

                                <div class="card-body">
                                    <!-- Search Form -->
                                    <form action="PriceRangeServlet" method="GET" class="mb-4">
                                        <div class="input-group">
                                            <input type="hidden" name="action" value="search">
                                            <input type="text" name="search" class="form-control" placeholder="Find Price Range..." aria-label="Search">
                                            <button class="btn btn-outline-secondary" type="submit">
                                                <i class="fas fa-search"></i>
                                            </button>
                                            <a href="PriceRangeServlet" class="btn btn-outline-danger">
                                                <i class="fas fa-sync"></i>
                                            </a>
                                            <c:if test="${sessionScope.roleID == 1}">
                                                <a href="PriceRangeServlet?action=listDeleted" class="btn btn-outline-danger">
                                                    <i class="fas fa-trash"></i>
                                                </a>
                                            </c:if>
                                        </div>
                                    </form>
                                    <!-- Customer Table -->
                                    <div class="table-responsive">
                                        <table class="table table-bordered table-striped table-hover">
                                            <thead class="table-dark">
                                                <tr>
                                                    <th>Price Range ID</th>
                                                    <th>Price Range</th>
                                                    <th>Date Created</th>
                                                    <th>Status</th>
                                                    <th>Actions</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:choose>
                                                    <c:when test="${empty priceRanges}">
                                                        <!-- Display message if the list is empty -->
                                                        <tr>
                                                            <td colspan="4" class="text-center text-muted">No Origin available.</td>
                                                        </tr>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach var="pr" items="${priceRanges}">
                                                            <tr class="${pr.isDeleted == 1 ? 'deleted-row' : ''}">
                                                                <td>${pr.priceRangeID}</td>
                                                                <td>${pr.priceRange}</td>
                                                                <td>
                                                                    <fmt:formatDate value="${pr.createdAt}" pattern="yyyy-MM-dd"/>
                                                                </td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${pr.isDeleted == 1}">
                                                                            <span class="badge bg-secondary">Deleted</span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="badge bg-success">Active</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                <td>
                                                                    <!-- Nếu isDeleted=0 => Edit & Xóa mềm -->
                                                                    <c:if test="${pr.isDeleted == 0}">
                                                                        <button class="btn btn-sm btn-warning"
                                                                                data-bs-toggle="modal"
                                                                                data-bs-target="#editPriceRangeModal-${pr.priceRangeID}">
                                                                            <i class="fas fa-edit"></i>
                                                                        </button>
                                                                        <button type="button" class="btn btn-sm btn-danger"
                                                                                data-bs-toggle="modal"
                                                                                data-bs-target="#confirmSoftDeleteModal"
                                                                                onclick="setSoftDeletePriceRangeID(${pr.priceRangeID})">
                                                                            <i class="fas fa-trash"></i>
                                                                        </button>
                                                                    </c:if>

                                                                    <!-- Nếu isDeleted=1 => Restore & Xóa cứng -->
                                                                    <c:if test="${pr.isDeleted == 1}">
                                                                        <button class="btn btn-sm btn-success"
                                                                                onclick="location.href = 'PriceRangeServlet?action=restore&priceRangeID=${pr.priceRangeID}'">
                                                                            Restore
                                                                        </button>
                                                                        <button type="button" class="btn btn-sm btn-danger"
                                                                                data-bs-toggle="modal"
                                                                                data-bs-target="#confirmHardDeleteModal"
                                                                                onclick="setHardDeletePriceRangeID(${pr.priceRangeID})">
                                                                            <i class="fas fa-trash"></i>
                                                                        </button>
                                                                    </c:if>
                                                                </td>
                                                            </tr>
                                                            <!-- Modal Edit PriceRange -->
                                                        <div class="modal fade" id="editPriceRangeModal-${pr.priceRangeID}" tabindex="-1">
                                                            <div class="modal-dialog">
                                                                <div class="modal-content">
                                                                    <form method="POST" action="PriceRangeServlet">
                                                                        <div class="modal-header">
                                                                            <h5 class="modal-title">Edit Price Range</h5>
                                                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                                        </div>
                                                                        <div class="modal-body">
                                                                            <input type="hidden" name="action" value="update">
                                                                            <input type="hidden" name="priceRangeID" value="${pr.priceRangeID}">
                                                                            <div class="mb-3">
                                                                                <label class="form-label">Price Range</label>
                                                                                <input type="text" class="form-control" name="priceRange" value="${pr.priceRange}" required>
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
        <!-- Success Message Modal -->
        <div class="modal fade" id="successModal" tabindex="-1" aria-labelledby="successModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header bg-success text-white">
                        <h5 class="modal-title" id="successModalLabel">Success</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body text-dark">
                        <p id="successMessage">${sessionScope.successMessage}</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
        <!-- Modal XÓA MỀM -->
        <div class="modal fade" id="confirmSoftDeleteModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Xác nhận xóa </h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        Bạn có chắc chắn muốn đưa phạm vi giá này vào thùng rác không?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                        <form method="POST" action="PriceRangeServlet">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="priceRangeID" id="softDeletePriceRangeID">
                            <button type="submit" class="btn btn-danger"><i class="fas fa-trash"></i> Xóa</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal XÓA CỨNG -->
        <div class="modal fade" id="confirmHardDeleteModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Xác nhận xóa</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        Bạn có chắc chắn muốn xóa vĩnh viễn phạm vi giá này không?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                        <form method="POST" action="PriceRangeServlet">
                            <input type="hidden" name="action" value="hardDelete">
                            <input type="hidden" name="priceRangeID" id="hardDeletePriceRangeID">
                            <button type="submit" class="btn btn-danger"><i class="fas fa-trash"></i> Xóa</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- JavaScript hỗ trợ thiết lập ID cho delete -->
        <script>
            function setSoftDeletePriceRangeID(id) {
                document.getElementById("softDeletePriceRangeID").value = id;
            }
            function setHardDeletePriceRangeID(id) {
                document.getElementById("hardDeletePriceRangeID").value = id;
            }

            window.onload = function () {
                let errorMessage = "${sessionScope.errorMessage}";
                if (errorMessage && errorMessage.trim() !== "") {
                    let errorModal = new bootstrap.Modal(document.getElementById('errorModal'));
                    errorModal.show();
            <% request.getSession().removeAttribute("errorMessage"); %>
                }

                let successMessage = "${sessionScope.successMessage}";
                if (successMessage && successMessage.trim() !== "") {
                    let successModal = new bootstrap.Modal(document.getElementById('successModal'));
                    successModal.show();
            <% request.getSession().removeAttribute("successMessage"); %>
                }
            };
        </script>
    </body>
</html>
