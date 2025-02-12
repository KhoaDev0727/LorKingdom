<%-- 
    Document   : SideBar
    Created on : Feb 3, 2025, 8:47:28 AM
    Author     : Truong Van Khang _ CE181852
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <nav class="sb-topnav navbar navbar-expand navbar-dark bg-dark">
            <a class="navbar-brand ps-3" href="index.html">Management Admin</a>
            <button class="btn btn-link btn-sm order-1 order-lg-0 me-4 me-lg-0" id="sidebarToggle" href="#!"><i class="fas fa-bars"></i></button>
            <form class="d-none d-md-inline-block form-inline ms-auto me-0 me-md-3 my-2 my-md-0">
                <!--                <div class="input-group">
                                    <input class="form-control" type="text" name="search" placeholder="Tìm kiếm người dùng..." aria-label="Search">
                                    <button class="btn btn-primary" id="btnNavbarSearch" type="submit">Tìm kiếm</button>
                                </div>-->
            </form>
            <ul class="navbar-nav ms-auto ms-md-0 me-3 me-lg-4">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" id="navbarDropdown" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false"><i class="fas fa-user fa-fw"></i>

                    </a>
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdown">
                        <li><a class="dropdown-item" href="#!">Settings</a></li>
                        <li><a class="dropdown-item" href="#!">Activity Log</a></li>
                        <li>
                            <hr class="dropdown-divider" />
                        </li>
                        <li><a class="dropdown-item" href="/logOutAdmin">Logout</a></li>
                    </ul>
                </li>
            </ul>
        </nav>
        <div id="layoutSidenav">
            <div id="layoutSidenav_nav">
                <nav class="sb-sidenav accordion sb-sidenav-dark" id="sidenavAccordion">
                    <div class="sb-sidenav-menu">
                        <div class="nav">
                            <div class="sb-sidenav-menu-heading">Management</div>

                            <!-- Dashboard Dropdown -->
                            <div class="nav-item dropdown">
                                <a class="nav-link" href="#" data-bs-toggle="dropdown" aria-expanded="false">
                                    <div class="sb-nav-link-icon"><i class="fas fa-tachometer-alt"></i></div>
                                    Dashboard
                                </a>
                                <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                                    <li><a class="dropdown-item" href="FinancialDashboardServlet">Financial Dashboard</a></li>
                                    <li><a class="dropdown-item" href="StaffPermissions.jsp">Staff Permissions</a></li>
                                    <li><a class="dropdown-item" href="CustomerManagementServlet">User Management</a></li>
                                    <li><a class="dropdown-item" href="StaffManagementServlet">Staff Management</a></li>
                                    <li><a class="dropdown-item" href="AdminManagementServlet">Admin Management</a></li>
                                    <li><a class="dropdown-item" href="ProductManagement.jsp">Product Management</a></li>
                                </ul>
                            </div>

                            <!-- Configuration Dropdown -->
                            <div class="nav-item dropdown">
                                <a class="nav-link" href="#" data-bs-toggle="dropdown" aria-expanded="false">
                                    <div class="sb-nav-link-icon"><i class="fas fa-cogs"></i></div>
                                    Configuration
                                </a>
                                <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                                    <li><a class="dropdown-item" href="SuperCategoryServlet?&action=list">SuperCategory
                                            Management</a></li>
                                    <li><a class="dropdown-item" href="ShippingMethod.jsp">Shipping Method</a></li>
                                    <li><a class="dropdown-item" href="PaymentMethod.jsp">Payment Method</a></li>
                                     <li><a class="dropdown-item" href="PriceRangeManagementServlet">Price Range Management</a></li>
                                </ul>
                            </div>

                            <!-- Feedback Dropdown -->
                            <div class="nav-item dropdown">
                                <a class="nav-link" href="#" data-bs-toggle="dropdown" aria-expanded="false">
                                    <div class="sb-nav-link-icon"><i class="fas fa-comments"></i></div>
                                    Feedback
                                </a>
                                <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                                    <li><a class="dropdown-item" href="ReviewManagementServlet">View Review</a></li>
                                </ul>
                            </div>
                        </div>
                    </div>

                    <nav>
                        <div class="sb-sidenav-footer">
                            <div class="small">Logged in as:</div>
                            Management Admin
                        </div>
                    </nav>
                </nav>
            </div>
        </div>
    </body>
</html>
