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
        <title>Sex Management</title>
        <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="CSS/style.css" />
        <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
        <script src="https://cdn.tailwindcss.com"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script src="JS/SideBarToggle.js"></script>

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
                <div class="dashboard-container mt-10 ml-5">
                    <main>
                        <div class="container-fluid px-5">
                            <h1 style="font-size: calc(1.375rem + 1.5vw); font-weight: 600">Sex Management</h1>
                            <div class="card mt-4">
                                <div class="card-header">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div>
                                            <i class="fas fa-table me-1"></i> Sex List
                                        </div>
                                    </div>
                                </div>

                                <div class="card-body">
                                    <div class="table-responsive mt-3">
                                        <table class="table table-bordered table-striped table-hover">
                                            <thead class="table-dark">
                                                <tr>
                                                    <th>Sex ID</th>
                                                    <th>Sex Name</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:choose>
                                                    <c:when test="${empty sexes}">
                                                        <tr>
                                                            <td colspan="4" class="text-center text-muted">No Sex available.</td>
                                                        </tr>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach var="sex" items="${sexes}">
                                                            <tr>
                                                                <td>${sex.sexID}</td>
                                                                <td>${sex.name}</td>
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
    </body>
</html>
