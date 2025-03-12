<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Trang đăng nhập</title>
        <!-- Bootstrap 5 CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Custom CSS -->
        <link rel="stylesheet" href="./assets/styleUser/stylelogin.css">
    </head>
    <body>

        <!-- Header Section -->
        <jsp:include page="/assets/Component/header.jsp"/>
        <!-- end Header Section -->

        <!-- Banner -->
        <div class="container-fluid2">  
            <div class="form-container">
                <div class="container form-login">
                    <h3 class="mb-4">Đăng nhập vào LorKingdom</h3>
                    <form method="POST" action="LoginServlet">
                        <!-- Email Input -->
                        <div class="mb-4">
                            <input type="text" class="form-control" id="email" name="email" placeholder="Email" value="${param.email}">

                            <!-- Display email error if any -->
                            <c:if test="${not empty requestScope.emailError}">
                                <div class="text-danger">${requestScope.emailError}</div>
                            </c:if>
                        </div>

                        <!-- Password Input -->
                        <div class="mb-4    ">
                            <input type="password" class="form-control" id="password" name="password" placeholder="Mật khẩu">

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
                        <button type="submit" class="btn btn-primary login-btn">Đăng nhập</button>

                        <!-- Forgot Password Link -->
                        <div class="FP mt-4"> 
                            
                            <div class="register-link">
                                <span>Bạn chưa có tài khoản? <a href="register.jsp">Đăng ký</a></span>
                            </div>
                            
                            <div class="forgot-password">
                                <a href="forgotPassword.jsp">Quên mật khẩu?</a>
                            </div>

                            <!-- Register Link -->
                            
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <%@include file="/assets/Component/footer.jsp" %>

        <!-- Bootstrap JS and dependencies -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>