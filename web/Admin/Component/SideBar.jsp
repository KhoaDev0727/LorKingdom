<%-- 
    Document   : SideBar
    Created on : Feb 3, 2025, 8:47:28 AM
    Author     : Truong Van Khang _ CE181852
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <nav class="sb-topnav navbar navbar-expand navbar-dark bg-dark">
            <c:if test="${roleID eq 1}">
                <a class="navbar-brand ps-3" href="FinancialDashboardServlet">Management ADMIN</a>

            </c:if>
            <c:if test="${roleID eq 2}">
                <a class="navbar-brand ps-3" href="FinancialDashboardServlet">Management STAFF</a>

            </c:if>
            <c:if test="${roleID eq 4}">
                <a class="navbar-brand ps-3" href="FinancialDashboardServlet">Management WAREHOUSE</a>
            </c:if>
            <button class="btn btn-link btn-sm order-1 order-lg-0 me-4 me-lg-0" id="sidebarToggle" href="#!"><i class="fas fa-bars"></i></button>
            <form class="d-none d-md-inline-block form-inline ms-auto me-0 me-md-3 my-2 my-md-0">
            </form>
            <ul class="navbar-nav ms-auto ms-md-0 me-3 me-lg-4">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" id="navbarDropdown" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false"><i class="fas fa-user fa-fw"></i>

                    </a>
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdown">
                        <li><a class="dropdown-item" href="profileStaff.jsp">Settings</a></li>
<!--                        <li><a class="dropdown-item" href="#!">Activity Log</a></li>-->
                        <li>
                            <hr class="dropdown-divider" />
                        </li>
                        <li><a class="dropdown-item" href="LogoutPage">Logout</a></li>
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
                            <c:if test="${roleID eq 1}">
                                <a class="dropdown-item" href="FinancialDashboardServlet">Financial Dashboard</a>
                            </c:if>
                            <!-- Dashboard Dropdown -->
                            <c:if test="${roleID eq 1 || roleID eq 2}">
                                <div class="nav-item dropdown">
                                    <a class="nav-link" href="#" data-bs-toggle="dropdown" aria-expanded="false">
                                        <div class="sb-nav-link-icon"><i class="fas fa-tachometer-alt"></i></div>
                                        User & Access Management 
                                    </a>
                                    <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                                        <c:if test="${roleID eq 1 || roleID eq 2}"><li><a class="dropdown-item" href="CustomerManagementServlet">Customer Management</a></li> </c:if> 
                                        <c:if test="${roleID eq 1}">  <li><a class="dropdown-item" href="StaffManagementServlet">Staff Management</a></li></c:if> 
                                        <c:if test="${roleID eq 1}"> <li><a class="dropdown-item" href="NotificationServlet?&action=list">Notification Management</a></li></c:if> 
                                        <c:if test="${roleID eq 1}"> <li><a class="dropdown-item" href="RoleServlet">Role Management</a></li></c:if> 
                                        </ul>
                                    </div>
                            </c:if>
                            <c:if test="${roleID eq 1 || roleID eq 2}">
                                <!-- Configuration Dropdown -->
                                <div class="nav-item dropdown">
                                    <a class="nav-link" href="#" data-bs-toggle="dropdown" aria-expanded="false">
                                        <div class="sb-nav-link-icon"><i class="fas fa-cogs"></i></div>
                                        Orders & Transactions
                                    </a>
                                    <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                                        <li><a class="dropdown-item" href="OrderServlet">Order Management</a></li>
                                        <li><a class="dropdown-item" href="ShippingMethodServlet?&action=list">Shipping Method</a></li>
                                        <li><a class="dropdown-item" href="PaymentMethodServlet">Payment Method</a></li>
                                    </ul>
                                </div> 
                            </c:if>
                            <c:if test="${roleID eq 1 || roleID eq 4}">
                                <div class="nav-item dropdown">
                                    <a class="nav-link" href="#" data-bs-toggle="dropdown" aria-expanded="false">
                                        <div class="sb-nav-link-icon"><i class="fas fa-cogs"></i></div>
                                        Product & Category Management
                                    </a>
                                    <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                                        <li><a class="dropdown-item" href="SuperCategoryServlet?&action=list">Super Category
                                                Management</a></li>
                                        <li><a class="dropdown-item" href="CategoryServlet?&action=list">Category
                                                Management</a></li>
                                        <li><a class="dropdown-item" href="ProductServlet?&action=list">Product Management</a></li>
                                        <li><a class="dropdown-item" href="AgeServlet?&action=list">Age
                                                Management</a></li>
                                        <li><a class="dropdown-item" href="SexServlet?&action=list">Sex
                                                Management</a></li>
                                        <li><a class="dropdown-item" href="BrandServlet?&action=list">Brand
                                                Management</a></li>
                                        <li><a class="dropdown-item" href="MaterialServlet?&action=list">Material
                                                Management</a></li>
                                        <li><a class="dropdown-item" href="PriceRangeServlet?&action=list">Price Range Management</a></li>
                                        <li><a class="dropdown-item" href="OriginServlet?&action=list">Origin Management</a></li>
                                    </ul>
                                </div>
                            </c:if>
                            <c:if test="${roleID eq 1 || roleID eq 2}">
                                <!-- Feedback Dropdown -->
                                <div class="nav-item dropdown">
                                    <a class="nav-link" href="#" data-bs-toggle="dropdown" aria-expanded="false">
                                        <div class="sb-nav-link-icon"><i class="fas fa-comments"></i></div>
                                        System & Feedback
                                    </a>
                                    <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                                        <li><a class="dropdown-item" href="ReviewManagementServlet">View Review</a></li>
                                         <li><a class="dropdown-item" href="promotionController">Promotion Management</a></li>
                                    </ul>

                                </div>
                            </div>
                        </div>
                    </c:if>

                    <nav>
                        <div class="sb-sidenav-footer">
                            <div class="small">Logged in as:</div>
                            <c:if test="${roleID eq 1}">
                                Management ADMIN
                            </c:if>
                            <c:if test="${roleID eq 2}">
                                Management STAFF
                            </c:if>
                            <c:if test="${roleID eq 4}">
                                Management WAREHOUSE
                            </c:if>
                        </div>
                    </nav>
                </nav>
            </div>
        </div>
    </body>
</html>
