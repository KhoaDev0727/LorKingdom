<%-- 
    Document   : loginAdmin
    Created on : Feb 17, 2025, 10:51:24 PM
    Author     : le minh khoa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login Page</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    <!-- Custom CSS -->
    <link rel="stylesheet" href="./CSS/styleLoginAdmin.css">
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
                <h2 class="mb-4" style="font-weight: 600;">Login Page</h2>
                <form method="POST" action="LoginPageServlet">
                    <!-- Email Input -->
                    <div class="mb-4">
                        <div class="input-group">
                            <span class="input-group-text"><i class="fas fa-user"></i></span>
                            <input type="text" class="form-control" id="email" name="email" placeholder="Email"
                                value="${param.email}">
                        </div>
                        <!-- Display email error if any -->
                        <c:if test="${not empty requestScope.emailError}">
                            <div class="text-danger">${requestScope.emailError}</div>
                        </c:if>
                    </div>

                    <!-- Password Input -->
                    <div class="mb-4">
                        <div class="input-group">
                            <span class="input-group-text"><i class="fas fa-key"></i></span>
                            <input type="password" class="form-control" id="password" name="password"
                                placeholder="Password">
                        </div>
                        <!-- Display password error if any -->
                        <c:if test="${not empty requestScope.passwordError}">
                            <div class="text-danger">${requestScope.passwordError}</div>
                        </c:if>
                    </div>

                    <!-- Display general error -->
                    <c:if test="${not empty requestScope.error}">
                        <div class="alert alert-danger">
                            ${requestScope.error}
                        </div>
                    </c:if>

                    <!-- Login Button -->
                    <button type="submit" class="btn btn-primary login-btn">Login</button>

                    <!-- Forgot Password Link -->
                    <div class="FP mt-4">

                        <div class="register-link">
                            <span>Don't have an account? <a href="registerPage.jsp">Register</a></span>
                        </div>

                        <div class="forgot-password">
                            <a href="forgotPasswordPage.jsp">Forgot password?</a>
                        </div>

                        <!-- Register Link -->

                    </div>
                </form>

            </div>
        </div>
    </div>

    <!-- Bootstrap JS and dependencies -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>

</html>