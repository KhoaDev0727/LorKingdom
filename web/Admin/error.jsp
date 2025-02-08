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
        <title>Review Management</title>
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
                            <h1 class="mt-4">Review Management</h1>
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
                                            <i class="fas fa-table me-1"></i> Review List
                                        </div>
                                        <div class="rating-filter">


                                        </div>

                                    </div>
                                </div>

                                <div class="card-body">
                                    <!-- Search & Filter Form -->
                                    <form action="ReviewManagementServlet" method="GET" class="mb-4">
                                        <input type="hidden" name="action" value="search">
                                        <div class="row g-2">
                                            <!-- Search Input -->
                                            <div class="col-md-4">
                                                <input type="text" name="search" class="form-control" placeholder="Search by content, user name..." 
                                                       value="${param.search}">
                                            </div>

                                            <!-- Filter by User/Product -->
                                            <div class="col-md-3">
                                                <input type="text" class="form-control" name="filterUserProduct" placeholder="Search by user or product">
                                            </div>

                                            <!-- Filter by Rating -->
                                            <div class="col-md-2">
                                                <select class="form-select" name="filterRating">
                                                    <option value="">Filter by rating</option>
                                                    <option value="5">5 Stars</option>
                                                    <option value="4">4 Stars</option>
                                                    <option value="3">3 Stars</option>
                                                    <option value="2">2 Stars</option>
                                                    <option value="1">1 Star</option>
                                                </select>
                                            </div>

                                            <!-- Filter by Status -->
                                            <div class="col-md-2">
                                                <select class="form-select" name="filterStatus">
                                                    <option value="">Filter by status</option>
                                                    <option value="approved">Approved</option>
                                                    <option value="pending">Pending</option>
                                                    <option value="rejected">Rejected</option>
                                                </select>
                                            </div>

                                            <!-- Action Buttons -->
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
                                        <table class="table table-bordered table-striped table-hover">
                                            <thead class="table-dark">
                                                <tr>
                                                    <th>ID</th>
                                                    <th>Product</th>
                                                    <th>User</th>
                                                    <th>Rating</th>
                                                    <th>Content</th>
                                                    <th>Created At</th>
                                                    <th>Status</th>
                                                    <th>Actions</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:choose>
                                                    <c:when test="${empty reviews}">
                                                        <tr>
                                                            <td colspan="8" class="text-center text-muted">No Reviews Found</td>
                                                        </tr>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach var="review" items="${reviews}">
                                                            <tr>
                                                                <td>${review.reviewId}</td>
                                                                <td>${review.product.name}</td>
                                                                <td>${review.user.userName}</td>
                                                                <td>
                                                                    <div class="star">
                                                                        <c:forEach begin="1" end="${review.rating}">★</c:forEach>
                                                                        </div>
                                                                    </td>
                                                                    <td>${review.content}</td>
                                                                <td>
                                                                    <fmt:formatDate value="${review.createdAt}" pattern="dd-MM-yyyy HH:mm" />
                                                                </td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${review.status eq 'APPROVED'}">
                                                                            <span class="badge bg-success">Approved</span>
                                                                        </c:when>
                                                                        <c:when test="${review.status eq 'PENDING'}">
                                                                            <span class="badge bg-warning">Pending</span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="badge bg-danger">Rejected</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                <td>
                                                                    <button class="btn btn-sm btn-warning" 
                                                                            data-bs-toggle="modal" 
                                                                            data-bs-target="#editReviewModal${review.reviewId}">
                                                                        <i class="fas fa-edit"></i> 
                                                                    </button>
                                                                    <button class="btn btn-sm btn-danger" 
                                                                            data-bs-toggle="modal" 
                                                                            data-bs-target="#deleteReviewModal${review.reviewId}">
                                                                        <i class="fas fa-trash"></i>
                                                                    </button>
                                                                </td>
                                                            </tr>

                                                            <!-- Edit Review Modal -->
                                                        <div class="modal fade" id="editReviewModal${review.reviewId}">
                                                            <div class="modal-dialog">
                                                                <div class="modal-content">
                                                                    <form action="ReviewManagementServlet" method="POST">
                                                                        <input type="hidden" name="action" value="update">
                                                                        <input type="hidden" name="reviewId" value="${review.reviewId}">
                                                                        <div class="modal-header">
                                                                            <h5 class="modal-title">Edit Review</h5>
                                                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                                        </div>
                                                                        <div class="modal-body">
                                                                            <div class="mb-3">
                                                                                <label class="form-label">Rating</label>
                                                                                <select class="form-select" name="rating">
                                                                                    <c:forEach begin="1" end="5" var="i">
                                                                                        <option value="${i}" ${review.rating eq i ? 'selected' : ''}>${i} ★</option>
                                                                                    </c:forEach>
                                                                                </select>
                                                                            </div>
                                                                            <div class="mb-3">
                                                                                <label class="form-label">Content</label>
                                                                                <textarea class="form-control" name="content" rows="3">${review.content}</textarea>
                                                                            </div>
                                                                            <div class="mb-3">
                                                                                <label class="form-label">Status</label>
                                                                                <select class="form-select" name="status">
                                                                                    <option value="APPROVED" ${review.status eq 'APPROVED' ? 'selected' : ''}>Approved</option>
                                                                                    <option value="PENDING" ${review.status eq 'PENDING' ? 'selected' : ''}>Pending</option>
                                                                                    <option value="REJECTED" ${review.status eq 'REJECTED' ? 'selected' : ''}>Rejected</option>
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

                                                        <!-- Delete Review Modal -->
                                                        <div class="modal fade" id="deleteReviewModal${review.reviewId}">
                                                            <div class="modal-dialog">
                                                                <div class="modal-content">
                                                                    <div class="modal-header">
                                                                        <h5 class="modal-title">Confirm Delete</h5>
                                                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                                    </div>
                                                                    <div class="modal-body">
                                                                        Are you sure you want to delete this review by <strong>${review.user.userName}</strong>?
                                                                    </div>
                                                                    <div class="modal-footer">
                                                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                                                        <form action="ReviewManagementServlet" method="POST" class="d-inline">
                                                                            <input type="hidden" name="action" value="delete">
                                                                            <input type="hidden" name="reviewId" value="${review.reviewId}">
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
    </body>
</html>