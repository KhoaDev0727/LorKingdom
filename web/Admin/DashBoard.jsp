<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="Dashboard for Management Admin" />
        <meta name="author" content="Admin" />

        <title>Dashboard - Management Admin</title>

        <!-- Stylesheets -->
        <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="CSS/style.css" />
        <link rel="stylesheet" href="CSS/DashBoardCss.css"/> 

        <!-- Scripts -->
        <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script src="JS/SideBarToggle.js"></script>
    </head>
    <body class="sb-nav-fixed">
        <c:if test="${empty sessionScope.roleID}">
            <c:redirect url="/Admin/loginPage.jsp"/>
        </c:if>
        <%@ include file="Component/SideBar.jsp" %>
        <!-- Dashboard Container -->
        <div id="layoutSidenav_content"> 
            <div class="container dashboard-container">
                <!-- Metric Cards -->
                <div class="row g-4">
                    <!-- Metric 1: Total Sold -->
                    <div class="col-md-4">
                        <div class="card text-center p-4 shadow">
                            <i class="fas fa-chart-line icon text-primary fs-1"></i>
                            <h5 class="card-header">TOTAL SOLD</h5>
                            <div class="card-body">
                                <h3 id="totalSold">
                                    <fmt:formatNumber value="${totalsolds}" type="number" groupingUsed="true" />
                                </h3>
                                <span class="text-muted">+12% so với tháng trước</span>
                            </div>
                        </div>
                    </div>

                    <!-- Metric 2: Total Revenue -->
                    <div class="col-md-4">
                        <div class="card text-center p-4 shadow">
                            <i class="fas fa-wallet icon text-success fs-1"></i>
                            <h5 class="card-header">TOTAL REVENUE</h5>
                            <div class="card-body">
                                <h3 id="totalRevenue">
                                    <fmt:formatNumber value="${totalrevenues}" type="currency" currencySymbol="$" />
                                </h3>
                                <span class="text-success">▲ 8.5% tăng trưởng</span>
                            </div>
                        </div>
                    </div>

                    <!-- Metric 3: Total Customers -->
                    <div class="col-md-4">
                        <div class="card text-center p-4 shadow">
                            <i class="fas fa-users icon text-info fs-1"></i>
                            <h5 class="card-header">TOTAL CUSTOMER</h5>
                            <div class="card-body">
                                <h3 id="totalUsers">
                                    <fmt:formatNumber value="${totalcustomers}" type="number" groupingUsed="true" />
                                </h3>
                                <span class="text-primary">5% tỷ lệ hoạt động</span>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Charts Section -->
                <div class="row mt-4 g-4">
                    <!-- Column Chart -->
                    <div class="col-lg-6">
                        <div class="chart-container card p-3 shadow">
                            <h5 class="card-header">Column Chart</h5>
                            <div id="chart_column" style="width: 100%; height: 300px;"></div>
                        </div>
                    </div>

                    <!-- Line Chart -->
                    <div class="col-lg-6">
                        <div class="chart-container card p-3 shadow">
                            <h5 class="card-header">Line Chart</h5>
                            <div id="chart_line" style="width: 100%; height: 300px;"></div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </body>
</html>
