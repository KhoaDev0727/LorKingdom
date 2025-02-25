<%-- 
    Document   : registerStaff
    Created on : Feb 17, 2025, 11:03:11 PM
    Author     : le minh khoa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Trang Đăng ký</title>
        <!-- Bootstrap 5 CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"> <!-- Bootstrap Import -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
        <!-- Custom CSS -->
        <link rel="stylesheet" href="./CSS/styleRegisterStaff.css">

    </head>
    <body>

        <!-- Banner -->
        <div class="containers">
        <div class="background">
            <img src="./assets/img/back-login.jpg" alt="Sea Background" class="background-img">
        </div>
        <div class="login-section">
            <div class="login-form">
                <div class="logo">
                    <img src="./assets/img/logo-login.png" alt="Lorkingdom Logo" class="logo-img">
                </div>
                <h2 class="mb-4" style="font-weight: 600;">Tạo tài khoản</h2>
                <form action="RegisterPageServlet" method="POST">
                    <!-- Username Input -->
                    <div class="mb-3">
                        <div class="input-group">
                            <span class="input-group-text"><i class="fas fa-user"></i></span>
                            <input type="text" class="form-control" id="username" name="username" placeholder="Tên người dùng" 
                                   value="<%= request.getAttribute("usernameValue") != null ? request.getAttribute("usernameValue") : "" %>" />
                        </div>
                        <% 
                            String usernameError = (String) request.getAttribute("usernameError");
                            if (usernameError != null && !usernameError.isEmpty()) { 
                        %>
                        <span class="text-danger"><%= usernameError %></span>
                        <% 
                            } 
                        %>
                    </div>
                
                    <!-- Phone Number Input -->
                    <div class="mb-3">
                        <div class="input-group">
                            <span class="input-group-text"><i class="fas fa-phone"></i></span>
                            <input type="text" class="form-control" id="phone" name="phone" placeholder="Số điện thoại" 
                                   value="<%= request.getAttribute("phoneValue") != null ? request.getAttribute("phoneValue") : "" %>" />
                        </div>
                        <%
                           String phoneNumberError = (String) request.getAttribute("phoneNumberError");
                           if (phoneNumberError != null && !phoneNumberError.isEmpty()) {
                        %>
                        <span class="text-danger"><%= phoneNumberError %></span>
                        <%
                            }
                        %>
                    </div>
                
                    <!-- Email Input -->
                    <div class="mb-3">
                        <div class="input-group">
                            <span class="input-group-text"><i class="fas fa-envelope"></i></span>
                            <input type="email" class="form-control" id="email" name="email" placeholder="Email" 
                                   value="<%= request.getAttribute("emailValue") != null ? request.getAttribute("emailValue") : "" %>" />
                        </div>
                        <% 
                            String emailError = (String) request.getAttribute("emailError");
                            if (emailError != null && !emailError.isEmpty()) { 
                        %>
                        <span class="text-danger"><%= emailError %></span>
                        <% 
                            } 
                        %>
                    </div>
                
                    <!-- Password Input -->
                    <div class="mb-3">
                        <div class="input-group">
                            <span class="input-group-text"><i class="fas fa-lock"></i></span>
                            <input type="password" class="form-control" id="password" name="password" placeholder="Mật khẩu" 
                                   value="<%= request.getAttribute("passwordValue") != null ? request.getAttribute("passwordValue") : "" %>" />
                        </div>                   
                        <% 
                            String passwordError = (String) request.getAttribute("passwordError");
                            if (passwordError != null && !passwordError.isEmpty()) { 
                        %>
                        <span class="text-danger"><%= passwordError %></span>
                        <% 
                            } 
                        %>
                    </div>                   
                
                    <!-- Register Button -->
                    <button type="submit" class="btn btn-primary register-btn">Đăng ký</button>
                
                    <!-- Login Link -->
                    <div class="login-link mt-3" >
                        <span>Bạn đã có tài khoản? <a href="loginPage.jsp">Đăng nhập</a></span>
                    </div>
                </form>

            </div>
        </div>
    </div>

        <!-- Bootstrap JS and dependencies -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>

