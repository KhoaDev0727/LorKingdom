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
        <link rel="stylesheet" href="./assets/styleUser/styleheader.css">
    </head>
    <body>

        <!-- Header Section -->
        <jsp:include page="header.jsp"/>
        <!-- end Header Section -->

        <!-- Banner -->
        <div class="banner container">
            <!-- Register Form -->
            <div class="register-container">
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
                    <div class="login-link">
                        <span>Already have an account? <a href="login.jsp">Login</a></span>
                    </div>
                </form>
            </div>
        </div>

        <!-- Footer -->
        <footer>
            <div class="footer-container">
                <div class="footer-section">
                    <h4>DỊCH VỤ KHÁCH HÀNG</h4>
                    <ul>
                        <li>Trung Tâm Trợ Giúp</li>
                        <li>Hướng Dẫn Mua Hàng</li>
                        <li>Chính Sách Bảo Hành</li>
                        <li>Chính Sách Hoàn Trả</li>
                        <li>Liên Hệ Chúng Tôi</li>
                    </ul>
                </div>
                <div class="footer-section">
                    <h4>VỀ SHOP ĐỒ CHƠI</h4>
                    <ul>
                        <li>Giới Thiệu</li>
                        <li>Chương Trình Khuyến Mãi</li>
                        <li>Tuyển Dụng</li>
                        <li>Chính Sách Bảo Mật</li>
                        <li>Hợp Tác Kinh Doanh</li>
                    </ul>
                </div>
                <div class="footer-section">
                    <h4>THANH TOÁN</h4>
                    <div class="payment-methods">
                        <img src="./assets/img/pay1.png" alt="Visa">
                        <img src="./assets/img/pay2.png" alt="Mastercard">
                        <img src="./assets/img/pay3.png" alt="JCB">
                        <br>
                        <img src="./assets/img/pay4.png" alt="AE">
                        <img src="./assets/img/pay5.png" alt="tra_gop">       
                    </div>
                    <h4>ĐƠN VỊ VẬN CHUYỂN</h4>
                    <div class="shipping-partners">
                        <img src="./assets/img/ship1.png" alt="Giao Hàng Nhanh">
                        <img src="./assets/img/ship2.png" alt="Viettel Post">
                        <br>
                        <img src="./assets/img/ship3.png" alt="J&T Express">
                        <img src="./assets/img/ship4.png" alt="J&T Express">        
                    </div>
                </div>
                <div class="footer-section">
                    <h4>THEO DÕI CHÚNG TÔI</h4>
                    <ul>
                        <li><a href="#">Facebook</a></li>
                        <li><a href="#">Instagram</a></li>
                        <li><a href="#">YouTube</a></li>
                    </ul>
                </div>
            </div>
        </footer>

        <!-- Bootstrap JS and dependencies -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
