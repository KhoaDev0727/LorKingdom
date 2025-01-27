<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Register Page</title>
        <!-- Bootstrap 5 CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"> <!-- Bootstrap Import -->
        <!-- Custom CSS -->
        <link rel="stylesheet" href="./assets/styleUser/styleregister.css">

    </head>
    <body>

        <!-- Header Section -->
        <jsp:include page="header.jsp"/>
        <!-- end Header Section -->

        <!-- Banner -->
        <div class="container-fluid2"> <!-- Đổi tên lớp ở đây -->
            <div class="form-container">
                <div class="container form">
                    <h3>Create Your Account</h3>
                    <form action="RegisterServlet" method="POST">
                        <!-- Username Input -->
                        <div class="mb-3">
                            <input type="text" class="form-control" id="username" name="username" placeholder="Username" 
                                   value="<%= request.getAttribute("usernameValue") != null ? request.getAttribute("usernameValue") : "" %>" />
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
                            <input type="text" class="form-control" id="phone" name="phone" placeholder="Phone Number" 
                                   value="<%= request.getAttribute("phoneValue") != null ? request.getAttribute("phoneValue") : "" %>" />
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
                            <input type="email" class="form-control" id="email" name="email" placeholder="Email" 
                                   value="<%= request.getAttribute("emailValue") != null ? request.getAttribute("emailValue") : "" %>" />
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
                            <input type="password" class="form-control" id="password" name="password" placeholder="Password" 
                                   value="<%= request.getAttribute("passwordValue") != null ? request.getAttribute("passwordValue") : "" %>" />
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
                        <button type="submit" class="btn btn-primary register-btn">Register</button>

                        <!-- Login Link -->
                        <div class="login-link mt-3" >
                            <span>Already have an account? <a href="login.jsp">Login</a></span>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Footer -->
        <%@include file="footer.jsp" %>
        <!-- Bootstrap JS and dependencies -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
