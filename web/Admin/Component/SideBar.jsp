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
                <a class="navbar-brand ps-3" href="FinancialDashboardServlet">Quản trị viên</a>

            </c:if>
            <c:if test="${roleID eq 2}">
                <a class="navbar-brand ps-3" href="FinancialDashboardServlet">Nhân viên bán hàng</a>

            </c:if>
            <c:if test="${roleID eq 4}">
                <a class="navbar-brand ps-3" href="FinancialDashboardServlet">Nhân viên quản lí kho</a>
            </c:if>
            <button class="btn btn-link btn-sm order-1 order-lg-0 me-4 me-lg-0" id="sidebarToggle" href="#!"><i class="fas fa-bars"></i></button>
            <form class="d-none d-md-inline-block form-inline ms-auto me-0 me-md-3 my-2 my-md-0">
            </form>
            <ul class="navbar-nav ms-auto ms-md-0 me-3 me-lg-4">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" id="navbarDropdown" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false"><i class="fas fa-user fa-fw"></i>

                    </a>
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdown">
                        <li><a class="dropdown-item" href="profileStaff.jsp">Cài đặt</a></li>
<!--                        <li><a class="dropdown-item" href="#!">Activity Log</a></li>-->
                        <li>
                            <hr class="dropdown-divider" />
                        </li>
                        <li><a class="dropdown-item" href="LogoutPage">Đăng xuất</a></li>
                    </ul>
                </li>
            </ul>
        </nav>
        <div id="layoutSidenav">
            <div id="layoutSidenav_nav">
                <nav class="sb-sidenav accordion sb-sidenav-dark" id="sidenavAccordion">
                    <div class="sb-sidenav-menu">
                        <div class="nav">
                            <div class="sb-sidenav-menu-heading">Quản lí</div>
                            <c:if test="${roleID eq 1}">
                                <a class="dropdown-item" href="FinancialDashboardServlet">Tổng quan tài chính</a>
                            </c:if>
                            <!-- Dashboard Dropdown -->
                            <c:if test="${roleID eq 1 || roleID eq 2}">
                                <div class="nav-item dropdown">
                                    <a class="nav-link" href="#" data-bs-toggle="dropdown" aria-expanded="false">
                                        <div class="sb-nav-link-icon"><i class="fas fa-tachometer-alt"></i></div>
                                        Người dùng và truy cập 
                                    </a>
                                    <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                                        <c:if test="${roleID eq 1 || roleID eq 2}"><li><a class="dropdown-item" href="CustomerManagementServlet">Quản lí khách hàng</a></li> </c:if> 
                                        <c:if test="${roleID eq 1}">  <li><a class="dropdown-item" href="StaffManagementServlet">Quản lí nhân viên</a></li></c:if> 
                                        <c:if test="${roleID eq 1 || roleID eq 2 }"> <li><a class="dropdown-item" href="NotificationServlet?&action=list">Quản lí thông báo</a></li></c:if> 
                                        <c:if test="${roleID eq 1}"> <li><a class="dropdown-item" href="RoleServlet">Chức vụ</a></li></c:if> 
                                        </ul>
                                    </div>
                            </c:if>
                            <c:if test="${roleID eq 1 || roleID eq 2}">
                                <!-- Configuration Dropdown -->
                                <div class="nav-item dropdown">
                                    <a class="nav-link" href="#" data-bs-toggle="dropdown" aria-expanded="false">
                                        <div class="sb-nav-link-icon"><i class="fas fa-cogs"></i></div>
                                            Đơn hàng và giao dịch
                                    </a>
                                    <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                                        <li><a class="dropdown-item" href="OrderServlet">Quản lí đơn hàng</a></li>
                                        <li><a class="dropdown-item" href="ShippingMethodServlet?&action=list">Phương thức vận chuyển</a></li>
                                        <li><a class="dropdown-item" href="PaymentMethodServlet">Phương thức thanh toán</a></li>
                                    </ul>
                                </div> 
                            </c:if>
                            <c:if test="${roleID eq 1 || roleID eq 4}">
                                <div class="nav-item dropdown">
                                    <a class="nav-link" href="#" data-bs-toggle="dropdown" aria-expanded="false">
                                        <div class="sb-nav-link-icon"><i class="fas fa-cogs"></i></div>
                                        Sản phẩm và danh mục 
                                    </a>
                                    <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                                        <li><a class="dropdown-item" href="SuperCategoryServlet?&action=list">Danh mục Tổng</a></li>
                                        <li><a class="dropdown-item" href="CategoryServlet?&action=list">Danh mục</a></li>
                                        <li><a class="dropdown-item" href="ProductServlet?&action=list">Sản phẩm</a></li>
                                        <li><a class="dropdown-item" href="AgeServlet?&action=list">Tuổi</a></li>
                                        <li><a class="dropdown-item" href="SexServlet?&action=list">Giới tính</a></li>
                                        <li><a class="dropdown-item" href="BrandServlet?&action=list">Thương hiệu</a></li>
                                        <li><a class="dropdown-item" href="MaterialServlet?&action=list">Chất liệu</a></li>
                                        <li><a class="dropdown-item" href="PriceRangeServlet?&action=list">Khoảng giá</a></li>
                                        <li><a class="dropdown-item" href="OriginServlet?&action=list">Xuất xứ</a></li>
                                    </ul>
                                </div>
                            </c:if>
                            <c:if test="${roleID eq 1 || roleID eq 2}">
                                <!-- Feedback Dropdown -->
                                <div class="nav-item dropdown">
                                    <a class="nav-link" href="#" data-bs-toggle="dropdown" aria-expanded="false">
                                        <div class="sb-nav-link-icon"><i class="fas fa-comments"></i></div>
                                        Hệ thống và Đánh giá 
                                    </a>
                                    <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                                        <li><a class="dropdown-item" href="ReviewManagementServlet">Đánh giá</a></li>
                                         <li><a class="dropdown-item" href="promotionController">Khuyến mãi</a></li>
                                    </ul>

                                </div>
                            </div>
                        </div>
                    </c:if>

                    <nav>
                        <div class="sb-sidenav-footer">
                            <div class="small">Đăng nhập với tư cách:</div>
                            <c:if test="${roleID eq 1}">
                                Quản trị viên
                            </c:if>
                            <c:if test="${roleID eq 2}">
                                Nhân viên bán hàng
                            </c:if>
                            <c:if test="${roleID eq 4}">
                                Nhân viên quản lí kho 
                            </c:if>
                        </div>
                    </nav>
                </nav>
            </div>
        </div>
    </body>
</html>
