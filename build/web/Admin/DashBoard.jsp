<%@page import="java.sql.Connection"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.SQLException"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="" />
        <meta name="author" content="" />
        <title>Dashboard - Management Admin</title>
        <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="CSS/style.css" />
        <link rel="stylesheet" href="CSS/DashBoardCss.css"/> 
        <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script src="JS/SideBarToggle.js"></script>
    </head>

    <body class="sb-nav-fixed">
         <%@ include file="Component/SideBar.jsp" %>
        <!-- Dashboard Container -->
        <div id="layoutSidenav_content"> 
        <div class="container dashboard-container" >
            <!-- Các metric cards -->
            <div class="row g-4">
                <div class="col-md-4 metric-1">
                    <div class="card text-center p-4">
                        <i class="fas fa-chart-line icon"></i>
                        <h5 class="card-header">Sản phẩm bán ra</h5>
                        <div class="card-body">
                            <h3 id="totalSold">55555555</h3>
                            <span class="text-muted">+12% so với tháng trước</span>
                        </div>
                    </div>
                </div>
                <div class="col-md-4 metric-2">
                    <div class="card text-center p-4">
                        <i class="fas fa-wallet icon"></i>
                        <h5 class="card-header">Tổng doanh thu</h5>
                        <div class="card-body">
                            <h3 id="totalRevenue">0 VND</h3>
                            <span class="text-success">▲ 8.5% tăng trưởng</span>
                        </div>
                    </div>
                </div>
                <div class="col-md-4 metric-3">
                    <div class="card text-center p-4">
                        <i class="fas fa-users icon"></i>
                        <h5 class="card-header">Người dùng mới</h5>
                        <div class="card-body">
                            <h3 id="totalUsers">0</h3>
                            <span class="text-primary">5% tỷ lệ hoạt động</span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Charts Section -->
            <div class="row mt-4 g-4">
                <div class="col-lg-6">
                    <div class="chart-container">
                        <div id="chart_column" style="width: 100%; height: 100%;"></div>
                    </div>
                </div>
                <div class="col-lg-6">
                    <div class="chart-container">
                        <div id="chart_line" style="width: 100%; height: 100%;"></div>
                    </div>
                </div>
            </div>
        </div>
            </div>
    </body>
</html>
