<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Trang Đăng ký Nhân viên</title>
        <!-- Bootstrap 5 CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Font Awesome -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
        <!-- Custom CSS -->
        <link rel="stylesheet" href="./CSS/styleRegisterStaff.css">
        <!-- SweetAlert2 -->
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <!-- jQuery -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
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
                    <form id="registerForm" method="POST">
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
                        <div class="login-link mt-3">
                            <span>Bạn đã có tài khoản? <a href="loginPage.jsp">Đăng nhập</a></span>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
        <!-- Custom Script -->
        <script>
            $(document).ready(function() {
                $('#registerForm').on('submit', function(e) {
                    e.preventDefault(); // Ngăn submit mặc định

                    // Hiển thị loading popup
                    Swal.fire({
                        title: 'Đang xử lý...',
                        text: 'Vui lòng chờ trong khi chúng tôi gửi mã xác minh.',
                        allowOutsideClick: false,
                        didOpen: () => {
                            Swal.showLoading();
                        }
                    });

                    // Gửi dữ liệu đến RegisterPageServlet qua Ajax
                    $.ajax({
                        url: 'RegisterPageServlet',
                        type: 'POST',
                        data: $(this).serialize(),
                        success: function(response) {
                            // Sau khi RegisterPageServlet xử lý xong, gọi SendVerificationPage
                            $.ajax({
                                url: 'SendVerificationPage',
                                type: 'GET',
                                success: function() {
                                    Swal.close(); // Đóng loading popup
                                    window.location.href = 'verifyCodePage.jsp'; // Chuyển hướng
                                },
                                error: function(xhr, status, error) {
                                    Swal.fire({
                                        icon: 'error',
                                        title: 'Lỗi!',
                                        text: 'Không thể gửi mã xác minh: ' + error
                                    });
                                }
                            });
                        },
                        error: function(xhr, status, error) {
                            Swal.fire({
                                icon: 'error',
                                title: 'Lỗi!',
                                text: 'Đăng ký thất bại: ' + error
                            });
                        }
                    });
                });
            });
        </script>
    </body>
</html>