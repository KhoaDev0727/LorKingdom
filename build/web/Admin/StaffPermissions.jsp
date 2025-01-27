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
        <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>

    </head>

    <body class="sb-nav-fixed">


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
                            <a class="nav-link" href="DashBoard.jsp">
                                <div class="sb-nav-link-icon"><i class="fas fa-address-book"></i></div>
                                Financial Dashboard
                            </a>
                            <a class="nav-link" href="StaffPermissions.jsp">
                                <div class="sb-nav-link-icon"><i class="fas fa-address-book"></i></div>
                                Staff Permissions
                            </a>
                            <a class="nav-link" href="UserManagement.jsp">
                                <div class="sb-nav-link-icon"><i class="fas fa-address-book"></i></div>
                                User Management
                            </a>
                            <a class="nav-link" href="StaffManagement.jsp">
                                <div class="sb-nav-link-icon"><i class="fas fa-address-book"></i></div>
                                Staff Management
                            </a>
                            <a class="nav-link" href="AdminManagement.jsp">
                                <div class="sb-nav-link-icon"><i class="fas fa-address-book"></i></div>
                                Admin Management
                            </a>
                            <a class="nav-link" href="ProductManagement.jsp">
                                <div class="sb-nav-link-icon"><i class="fas fa-address-book"></i></div>
                                Product Management
                            </a>
                            <a class="nav-link" href="CategoryManagement.jsp">
                                <div class="sb-nav-link-icon"><i class="fas fa-address-book"></i></div>
                                Category Management
                            </a>

                            <a class="nav-link" href="ShippingMethod.jsp">
                                <div class="sb-nav-link-icon"><i class="fas fa-address-book"></i></div>
                                Shipping Method
                            </a>
                            <a class="nav-link" href="PaymentMethod.jsp">
                                <div class="sb-nav-link-icon"><i class="fas fa-address-book"></i></div>
                                Payment Method
                            </a>
                            <a class="nav-link" href="FeedBack.jsp">
                                <div class="sb-nav-link-icon"><i class="fas fa-address-book"></i></div>
                                FeedBack
                            </a>
                        </div>
                        <div class="sb-sidenav-footer">
                            <div class="small">Logged in as:</div>
                            Management Admin
                        </div>
                </nav>
            </div>


        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script>
            document.addEventListener("DOMContentLoaded", function () {
                const sidebarToggle = document.getElementById('sidebarToggle');
                if (sidebarToggle) {
                    sidebarToggle.addEventListener('click', function (event) {
                        event.preventDefault();
                        document.body.classList.toggle('sb-sidenav-toggled');
                    });
                }
            });
        </script>
    </body>
</html>
