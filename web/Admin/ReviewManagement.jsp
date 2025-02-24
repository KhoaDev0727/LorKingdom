<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <title>Dashboard - Management Admin</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="CSS/style.css" />
        <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script src="JS/SideBarToggle.js"></script>
    </head>
    <body class="sb-nav-fixed">
        <div id="layoutSidenav">
            <div id="layoutSidenav_content">
                <%@ include file="Component/SideBar.jsp" %>
                <div class="dashboard-container">
                    <main>
                        <div class="container-fluid px-5">
                            <h1 class="mt-4">Review Management</h1>
                            <!-- Success/Error Messages -->
                            <c:if test="${not empty message}">
                                <div class="alert alert-${messageType} alert-dismissible fade show" role="alert">
                                    ${message}
                                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                                </div>
                            </c:if>

                            <div class="card mb-4">
                                <div class="card-header">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div>
                                            <i class="fas fa-table me-1"></i> Review List
                                        </div>
                                    </div>
                                </div>
                                <div class="card-body">
                                    <!-- Search & Filter Form -->
                                    <form action="ReviewManagementServlet" method="GET" class="mb-4">
                                        <input type="hidden" name="action" value="search">
                                        <div class="row g-2">
                                            <div class="col-md-4">
                                                <input type="text" class="form-control hide-zero" name="filterUserProduct" placeholder="Search ID by user or product">
                                            </div>
                                            <div class="col-md-2">
                                                <select class="form-select" name="filterRating">
                                                    <option value="0">Filter by rating</option>
                                                    <option value="5">5 Stars</option>
                                                    <option value="4">4 Stars</option>
                                                    <option value="3">3 Stars</option>
                                                    <option value="2">2 Stars</option>
                                                    <option value="1">1 Star</option>
                                                </select>
                                            </div>
                                            <div class="col-md-2">
                                                <select class="form-select" name="filterStatus">
                                                    <option value="-1">Filter by status</option>
                                                    <option value="0">Approved</option>
                                                    <option value="1">Pending</option>
                                                    <option value="2">Rejected</option>
                                                </select>
                                            </div>
                                            <div class="col-md-1 d-flex gap-2">
                                                <button type="submit" class="btn btn-primary w-100">
                                                    <i class="fas fa-search"></i>
                                                </button>
                                                <a href="ReviewManagementServlet" class="btn btn-outline-danger w-100">
                                                    <i class="fas fa-sync"></i>
                                                </a>
                                            </div>
                                        </div>
                                    </form>

                                    <!-- Review Table -->
                                    <div class="table-responsive">
                                        <%@include file="Component/PaginationReview.jsp" %>
                                        <table class="table table-bordered table-striped table-hover">
                                            <thead class="table-dark text-center">
                                                <tr>
                                                    <th>ID</th>
                                                    <th>Customer</th>
                                                    <th>Product ID</th>
                                                    <th>IMG</th>
                                                    <th>Rating</th>
                                                    <th>Comment</th>
                                                    <th>Status</th>
                                                    <th>Review At</th>
                                                    <th>Actions</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:choose>
                                                    <c:when test="${empty reviews}">
                                                        <tr>
                                                            <td colspan="9" class="text-center text-muted">No Reviews found</td>
                                                        </tr>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach var="s" items="${reviews}">
                                                            <tr>
                                                                <td>${s.reviewID}</td>
                                                                <td>${s.accountID}</td>
                                                                <td>${s.productID}</td>
                                                                <td style="width: 50px; height: 80px; overflow: hidden; text-align: center;">
                                                                    <c:if test="${not empty s.imgReview}">
                                                                        <img src="${pageContext.request.contextPath}/${s.imgReview}" 
                                                                             alt="Profile Image"
                                                                             class="mt-2 rounded" 
                                                                             style="width: 100%; height: 100%; object-fit: cover;">
                                                                    </c:if>
                                                                </td>
                                                                <td>
                                                                    <c:forEach var="i" begin="1" end="${s.rating}">
                                                                        <i class="fas fa-star text-warning"></i>
                                                                    </c:forEach>
                                                                    <c:forEach var="i" begin="${s.rating + 1}" end="5">
                                                                        <i class="far fa-star text-secondary"></i>
                                                                    </c:forEach>
                                                                </td>
                                                                <td>${s.comment}</td> 
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${s.status == 0}">
                                                                            <span class="badge bg-success">Approved</span>
                                                                        </c:when>
                                                                        <c:when test="${s.status == 1}">
                                                                            <span class="badge bg-warning">Pending</span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="badge bg-danger">Rejected</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                <td>
                                                                    <fmt:formatDate value="${s.reviewAt}" pattern="dd-MM-yyyy" />
                                                                </td>
                                                                <td>
                                                                    <button class="btn btn-sm btn-warning" 
                                                                            data-bs-toggle="modal" 
                                                                            data-bs-target="#editStatusReviewModal${s.reviewID}">
                                                                        <i class="fas fa-edit"></i>
                                                                    </button>
                                                                    <button class="btn btn-sm btn-danger" 
                                                                            data-bs-toggle="modal" 
                                                                            data-bs-target="#deleteModal${s.reviewID}">
                                                                        <i class="fas fa-trash"></i>
                                                                    </button>
                                                                </td>
                                                            </tr>
                                                            <!-- Edit Review Modal -->
                                                            <div class="modal fade" id="editStatusReviewModal${s.reviewID}">
                                                                <div class="modal-dialog modal-lg">
                                                                    <div class="modal-content">
                                                                        <form action="ReviewManagementServlet" method="POST">
                                                                            <input type="hidden" name="action" value="update">
                                                                            <input type="hidden" name="reviewID" value="${s.reviewID}">
                                                                            <div class="modal-header">
                                                                                <h5 class="modal-title">Edit Review Status</h5>
                                                                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                                            </div>
                                                                            <div class="modal-body">
                                                                                <div class="mb-3">
                                                                                    <label class="form-label">Review Status</label>
                                                                                    <select class="form-select" name="status">
                                                                                        <option value="0" ${s.status == 0 ? 'selected' : ''}>Approved</option>
                                                                                        <option value="1" ${s.status == 1 ? 'selected' : ''}>Pending</option>
                                                                                        <option value="2" ${s.status == 2 ? 'selected' : ''}>Rejected</option>
                                                                                    </select>
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
                                                            <div class="modal fade" id="deleteModal${s.reviewID}">
                                                                <div class="modal-dialog">
                                                                    <div class="modal-content">
                                                                        <div class="modal-header">
                                                                            <h5 class="modal-title">Confirm Delete</h5>
                                                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                                        </div>
                                                                        <div class="modal-body">
                                                                            Are you sure you want to delete review: <strong>${s.reviewID}</strong>?
                                                                        </div>
                                                                        <div class="modal-footer">
                                                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                                                            <a href="ReviewManagementServlet?action=delete&reviewID=${s.reviewID}" class="btn btn-danger">Delete</a>
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
    </body>
</html>
