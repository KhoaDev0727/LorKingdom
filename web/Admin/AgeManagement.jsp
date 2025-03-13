<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="" />
        <meta name="author" content="" />
        <title>Age Management</title>
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
        <c:choose>
            <c:when test="${empty sessionScope.roleID || sessionScope.roleID eq 2}">
                <c:redirect url="/Admin/loginPage.jsp"/>
            </c:when>
            <c:when test="${sessionScope.roleID eq 3}">
                <c:redirect url="/LogoutServlet"/>
            </c:when>
        </c:choose>
        <div id="layoutSidenav">
            <div id="layoutSidenav_content">
                <%@ include file="Component/SideBar.jsp" %>
                <div class="dashboard-container">
                    <main>
                        <div class="container-fluid px-5">
                            <h1 class="mt-4">Age Management</h1>


                            <div class="card mb-4">
                                <div class="card-header">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div>
                                            <i class="fas fa-table me-1"></i> Age List
                                        </div>
                                        <div>
                                            <!-- Nút mở modal Add Notification -->
                                            <button class="btn btn-primary ms-2" data-bs-toggle="modal" data-bs-target="#addAgeModal">
                                                <i class="fas fa-plus"></i> Add Age
                                            </button>
                                        </div>
                                    </div>
                                </div>

                                <div class="card-body">
                                    <!-- Search Form -->
                                    <form action="AgeServlet" method="GET" class="mb-4">
                                        <div class="input-group">
                                            <input type="hidden" name="action" value="search">
                                            <input type="text" name="search" class="form-control" placeholder="Find Age..." aria-label="Search">
                                            <button class="btn btn-outline-secondary" type="submit">
                                                <i class="fas fa-search"></i>
                                            </button>
                                            <a href="AgeServlet" class="btn btn-outline-danger">
                                                <i class="fas fa-sync"></i>
                                            </a>
                                            <c:if test="${sessionScope.roleID == 1}">
                                                <a href="AgeServlet?action=listDeleted" class="btn btn-outline-danger">
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
                                                    <th>Age ID</th>
                                                    <th>Age Range</th>
                                                    <th>Date Created</th>
                                                    <th>Status</th>
                                                    <th>Actions</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:choose>
                                                    <c:when test="${empty ages}">
                                                        <!-- Display message if the list is empty -->
                                                        <tr>
                                                            <td colspan="4" class="text-center text-muted">No Age available.</td>
                                                        </tr>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach var="ag" items="${ages}">
                                                            <tr class="${ag.isDeleted == 1 ? 'deleted-row' : ''}">
                                                                <td>${ag.ageID}</td>
                                                                <td>${ag.ageRange}</td>
                                                                <td>
                                                                    <!-- Nếu muốn format date -->
                                                                    <fmt:formatDate value="${ag.createdAt}" pattern="yyyy-MM-dd"/>
                                                                </td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${ag.isDeleted == 1}">
                                                                            <span class="badge bg-secondary">Deleted</span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="badge bg-success">Active</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                <td>
                                                                    <!-- Nếu isDeleted=0 => Edit & Xóa mềm -->
                                                                    <c:if test="${ag.isDeleted == 0}">
                                                                        <button class="btn btn-sm btn-warning"
                                                                                data-bs-toggle="modal"
                                                                                data-bs-target="#editAgeModal-${ag.ageID}">
                                                                            <i class="fas fa-edit"></i>
                                                                        </button>

                                                                        <button type="button" class="btn btn-sm btn-danger"
                                                                                data-bs-toggle="modal"
                                                                                data-bs-target="#confirmSoftDeleteModal"
                                                                                onclick="setSoftDeleteAgeID(${ag.ageID})">
                                                                            <i class="fas fa-trash"></i>
                                                                        </button>
                                                                    </c:if>

                                                                    <!-- Nếu isDeleted=1 => Restore & Xóa cứng -->
                                                                    <c:if test="${ag.isDeleted == 1}">
                                                                        <button class="btn btn-sm btn-success"
                                                                                onclick="location.href = 'AgeServlet?action=restore&ageID=${ag.ageID}'">
                                                                            Restore
                                                                        </button>
                                                                        <button type="button" class="btn btn-sm btn-danger"
                                                                                data-bs-toggle="modal"
                                                                                data-bs-target="#confirmHardDeleteModal"
                                                                                onclick="setHardDeleteAgeID(${ag.ageID})">
                                                                            <i class="fas fa-trash"></i>
                                                                        </button>
                                                                    </c:if>
                                                                </td>
                                                            </tr>
                                                            <!-- Modal Edit Age -->
                                                            <c:set var="rangeParts" value="${fn:split(ag.ageRange, ' ')}" />
                                                            <c:set var="bounds" value="${fn:split(rangeParts[0], '-')}" />
                                                        <div class="modal fade" id="editAgeModal-${ag.ageID}" tabindex="-1" aria-labelledby="editAgeModalLabel-${ag.ageID}" aria-hidden="true">
                                                            <div class="modal-dialog">
                                                                <div class="modal-content">
                                                                    <div class="modal-header">
                                                                        <h5 class="modal-title" id="editAgeModalLabel-${ag.ageID}">Edit Age Range</h5>
                                                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                                    </div>
                                                                    <div class="modal-body">
                                                                        <form id="editAgeForm-${ag.ageID}" action="AgeServlet" method="POST">
                                                                            <!-- Đặt action update và truyền kèm ageID -->
                                                                            <input type="hidden" name="action" value="update">
                                                                            <input type="hidden" name="ageID" value="${ag.ageID}">

                                                                            <div class="mb-3">
                                                                                <label for="ageStart-${ag.ageID}" class="form-label">Start Age</label>
                                                                                <input type="number" class="form-control" id="ageStart-${ag.ageID}" name="ageStart" min="0" required
                                                                                       value="${bounds[0]}">
                                                                            </div>

                                                                            <div class="mb-3">
                                                                                <label for="ageEnd-${ag.ageID}" class="form-label">End Age</label>
                                                                                <input type="number" class="form-control" id="ageEnd-${ag.ageID}" name="ageEnd" min="0" required
                                                                                       value="${bounds[1]}">
                                                                            </div>

                                                                            <div class="mb-3">
                                                                                <label for="unit-${ag.ageID}" class="form-label">Unit</label>
                                                                                <select class="form-select" id="unit-${ag.ageID}" name="unit">
                                                                                    <option value="tháng" <c:if test="${rangeParts[1] eq 'tháng'}">selected</c:if>>Tháng</option>
                                                                                    <option value="tuổi" <c:if test="${rangeParts[1] eq 'tuổi'}">selected</c:if>>Tuổi</option>
                                                                                    </select>
                                                                                </div>

                                                                                <button type="submit" class="btn btn-primary">Save changes</button>
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

        <!-- Delete Confirmation Modal -->
        <div class="modal fade" id="successModalLabel" tabindex="-1" aria-labelledby="successModalTitle" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header bg-success text-white">
                        <h5 class="modal-title" id="successModalTitle">Success</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body text-dark">
                        <p id="successMessageContent">${sessionScope.successMessage}</p>
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
                        Bạn có chắc chắn muốn đưa khoảng tuổi này vào thùng rác không?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                        <form method="POST" action="AgeServlet">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="ageID" id="softDeleteAgeID">
                            <button type="submit" class="btn btn-danger"><i class="fas fa-trash"></i> Xóa </button>
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
                        <h5 class="modal-title">Xác nhận xóa </h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        Bạn có chắc chắn muốn xóa khoảng tuổi vĩnh viễn  này không?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                        <form method="POST" action="AgeServlet">
                            <input type="hidden" name="action" value="hardDelete">
                            <input type="hidden" name="ageID" id="hardDeleteAgeID">
                            <button type="submit" class="btn btn-danger"><i class="fas fa-trash"></i> Xóa </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>
<!-- Modal thêm độ tuổi -->
<div class="modal fade" id="addAgeModal" tabindex="-1" aria-labelledby="addAgeModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="addAgeModalLabel">Thêm khoảng tuổi</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id="addAgeForm" action="AgeServlet" method="POST">
                    <input type="hidden" name="action" value="add">

                    <div class="mb-3">
                        <label for="ageStart" class="form-label">Khoảng tuổi bắt đầu</label>
                        <input type="number" class="form-control" id="ageStart" name="ageStart" min="0" required>
                    </div>

                    <div class="mb-3">
                        <label for="ageEnd" class="form-label">Khoảng tuổi kết thúc</label>
                        <input type="number" class="form-control" id="ageEnd" name="ageEnd" min="0" required>
                    </div>

                    <div class="mb-3">
                        <label for="unit" class="form-label">Đơn vị</label>
                        <select class="form-select" id="unit" name="unit">
                            <option value="tháng">Tháng</option>
                            <option value="tuổi">Tuổi</option>
                        </select>
                    </div>

                    <button type="submit" class="btn btn-primary">Save</button>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- Modal Cập Nhật Tuổi -->


<script>
    function setSoftDeleteAgeID(id) {
        document.getElementById("softDeleteAgeID").value = id;
    }
    function setHardDeleteAgeID(id) {
        document.getElementById("hardDeleteAgeID").value = id;
    }
    window.onload = function () {
        const errorMessage = "${sessionScope.errorMessage}";
        if (errorMessage && errorMessage.trim() !== "") {
            const errorModal = new bootstrap.Modal(document.getElementById("errorModal"));
            errorModal.show();
    <% request.getSession().removeAttribute("errorMessage"); %>
        }

        let successMessage = '<%= session.getAttribute("successMessage") %>';
        if (successMessage && successMessage.trim() !== "null" && successMessage.trim() !== "") {
            let successModal = new bootstrap.Modal(document.getElementById("successModalLabel"));
            successModal.show();
    <% session.removeAttribute("successMessage"); %>
        }
    };
</script>
</body>
</html>
