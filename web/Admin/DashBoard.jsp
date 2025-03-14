<%-- DashBoard.jsp --%>
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

        <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="CSS/style.css" />
        <link rel="stylesheet" href="CSS/DashBoardCss.css"/> 

        <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script src="JS/SideBarToggle.js"></script>
    </head>
    <body class="sb-nav-fixed">
        <c:choose>
            <c:when test="${empty sessionScope.roleID}">
                <c:redirect url="/Admin/loginPage.jsp"/>
            </c:when>
            <c:when test="${sessionScope.roleID eq 3}">
                <c:redirect url="/LogoutServlet"/>
            </c:when>
        </c:choose>
        <%@ include file="Component/SideBar.jsp" %>

        <div id="layoutSidenav_content"> 
            <div class="container dashboard-container">
                <div class="row g-4">
                    <div class="col-md-4">
                        <div class="card text-center p-4 shadow">
                            <i class="fas fa-chart-line icon text-primary fs-1"></i>
                            <h5 class="card-header">Tổng Sản Phẩm Bán Ra</h5>
                            <div class="card-body">
                                <h3 id="totalSold">
                                    <fmt:formatNumber value="${totalsolds}" type="number" groupingUsed="true" />
                                </h3>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-4">
                        <div class="card text-center p-4 shadow">
                            <i class="fas fa-wallet icon text-success fs-1"></i>
                            <h5 class="card-header">Tổng Doanh Thu</h5>
                            <div class="card-body">
                                <h3 id="totalRevenue">
                                    <fmt:formatNumber value="${totalrevenues}" type="currency" currencyCode="VND" />
                                </h3>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-4">
                        <div class="card text-center p-4 shadow">
                            <i class="fas fa-users icon text-info fs-1"></i>
                            <h5 class="card-header">Tổng Số Khách Hàng</h5>
                            <div class="card-body">
                                <h3 id="totalUsers">
                                    <fmt:formatNumber value="${totalcustomers}" type="number" groupingUsed="true" />
                                </h3>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row mt-4 g-4">
                    <div class="col-lg-6">
                        <div class="chart-container card p-3 shadow">
                            <h5 class="card-header">Column Chart</h5>
                            <div id="chart_column" style="width: 100%; height: 300px;"></div>
                        </div>
                    </div>

                    <div class="col-lg-6">
                        <div class="chart-container card p-3 shadow">
                            <h5 class="card-header">Line Chart</h5>
                            <div id="chart_line" style="width: 100%; height: 300px;"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
        <script >
            google.charts.load('current', {'packages': ['corechart']});
            google.charts.setOnLoadCallback(drawColumnChart);
            google.charts.setOnLoadCallback(drawLineChart);

            document.addEventListener('DOMContentLoaded', function () {
                google.charts.load('current', {'packages': ['corechart']});
                google.charts.setOnLoadCallback(drawColumnChart);
            });

            function drawColumnChart() {
                try {
                    // Lấy dữ liệu từ JSP và parse
                    var jsonData = JSON.parse('${categorySalesJson}');
                    // Tạo DataTable
                    var data = new google.visualization.DataTable();
                    data.addColumn('string', 'Category');
                    data.addColumn('number', 'Sales');
                    jsonData.forEach(function (item) {
                        data.addRow([item.categoryName, item.QuantityCartegory]);
                    });
                    // Cấu hình options
                    var options = {
                        title: 'Doanh số theo danh mục',
                        chartArea: {width: '70%'},
                        height: 300
                    };
                    // Vẽ biểu đồ
                    var chart = new google.visualization.ColumnChart(document.getElementById('chart_column'));
                    chart.draw(data, options);
                } catch (e) {
                    console.error("Lỗi khi vẽ biểu đồ:", e);
                }
            }

            function drawLineChart() {
                try {
                    var jsonData = '<c:out value="${revenueDataJson}" escapeXml="false"/>';
                    var revenueData = JSON.parse(jsonData);

                    if (!revenueData || revenueData.length === 0) {
                        document.getElementById('chart_line').innerHTML = "Không có dữ liệu để hiển thị";
                        return;
                    }

                    var data = new google.visualization.DataTable();
                    data.addColumn('string', 'Tháng');
                    data.addColumn('number', 'Doanh thu');

                    revenueData.forEach(function (item) {
                        data.addRow([item.MonthYear, parseFloat(item.Revenue)]);
                    });

                    var options = {
                        title: 'DOANH THU THEO THÁNG',
                        curveType: 'function',
                        legend: {position: 'bottom'},
                        colors: ['#2dd36f'],
                        vAxis: {minValue: 0}
                    };

                    var chart = new google.visualization.LineChart(document.getElementById('chart_line'));
                    chart.draw(data, options);
                } catch (e) {
                    console.error('Error drawing line chart:', e);
                    document.getElementById('chart_line').innerHTML = "Lỗi khi vẽ biểu đồ";
                }
            }
        </script>
    </body>
</html>