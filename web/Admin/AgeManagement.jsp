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
                            <h1 class="mt-4">Quản Lí Khoảng Tuổi</h1>
                            <div class="card mb-4">
                                <div class="card-header">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div>
                                            <i class="fas fa-table me-1"></i> Danh Sách Khoảng Tuổi
                                        </div>
                                    </div>
                                </div>

                                <div class="card-body">
                                    <!-- Search Form -->
                                    <form action="AgeServlet" method="GET" class="mb-4">
                                        <div class="input-group">
                                           
                                        </div>
                                    </form>
                                    <!-- Customer Table -->
                                    <div class="table-responsive">
                                        <table class="table table-bordered table-striped table-hover">
                                            <thead class="table-dark">
                                                <tr>
                                                    <th>Mã</th>
                                                    <th>Khoảng Tuổi</th>
                                                    <th>Ngày Tạo</th>
                                                    <th>Trạng Thái</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:choose>
                                                    <c:when test="${empty ages}">
                                                        <!-- Display message if the list is empty -->
                                                        <tr>
                                                            <td colspan="4" class="text-center text-muted">Khoảng tuổi không có sẵn.</td>
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
                                                                            <span class="badge bg-success">Hoạt Động</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                
                                                            </tr>
                                                            <!-- Modal Edit Age -->
                                                            <c:set var="rangeParts" value="${fn:split(ag.ageRange, ' ')}" />
                                                            <c:set var="bounds" value="${fn:split(rangeParts[0], '-')}" />
                                                      
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
