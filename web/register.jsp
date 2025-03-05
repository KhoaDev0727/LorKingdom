<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Trang Đăng ký</title>
        <!-- Bootstrap 5 CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Custom CSS -->
        <link rel="stylesheet" href="./assets/styleUser/styleregister.css">
        <!-- SweetAlert2 -->
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <!-- jQuery -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    </head>
    <body>
        <!-- Header Section -->
        <jsp:include page="assets/Component/header.jsp"/>
        <!-- end Header Section -->

        <!-- Banner -->
        <div class="container-fluid2">
            <div class="form-container">
                <div class="container form">
                    <h3>Tạo tài khoản của bạn</h3>
                    <form id="registerForm" method="POST">
                        <!-- Username Input -->
                        <div class="mb-3">
                            <input type="text" class="form-control" id="username" name="username" placeholder="Tên người dùng" 
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
                            <input type="text" class="form-control" id="phone" name="phone" placeholder="Số điện thoại" 
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
                            <input type="password" class="form-control" id="password" name="password" placeholder="Mật khẩu" 
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
                        <button type="submit" class="btn btn-primary register-btn">Đăng ký</button>

                        <!-- Login Link -->
                        <div class="login-link mt-3">
                            <span>Bạn đã có tài khoản? <a href="login.jsp">Đăng nhập</a></span>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Footer -->
        <%@include file="./assets/Component/footer.jsp" %>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
        <!-- Custom Script -->
        <script>
            $(document).ready(function () {
                $('#registerForm').on('submit', function (e) {
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

                    // Gửi dữ liệu đến RegisterServlet qua Ajax
                    $.ajax({
                        url: 'RegisterServlet',
                        type: 'POST',
                        data: $(this).serialize(),
                        dataType: 'json', // Đảm bảo nhận response dạng JSON
                        success: function (response) {
                            if (response.success) {
                                // Gọi SendVerificationServlet mà không đóng Swal ngay
                                $.ajax({
                                    url: 'SendVerificationServlet',
                                    type: 'GET',
                                    success: function (sendResponse) {
                                        // Đóng popup loading và chuyển hướng
                                        Swal.close();
                                        window.location.href = 'verifyCode.jsp';
                                    },
                                    error: function (xhr, status, error) {
                                        Swal.close(); // Đóng popup nếu lỗi
                                        Swal.fire({
                                            icon: 'error',
                                            title: 'Lỗi!',
                                            text: 'Không thể gửi mã xác minh: ' + error
                                        });
                                    }
                                });
                            } else {
                                // Đóng popup và hiển thị lỗi
                                Swal.close();
                                let errorMessage = '';
                                if (response.usernameError)
                                    errorMessage += response.usernameError + '<br>';
                                if (response.emailError)
                                    errorMessage += response.emailError + '<br>';
                                if (response.passwordError)
                                    errorMessage += response.passwordError + '<br>';
                                if (response.phoneNumberError)
                                    errorMessage += response.phoneNumberError + '<br>';

                                Swal.fire({
                                    icon: 'error',
                                    title: 'Lỗi!',
                                    html: errorMessage // Sử dụng html để xuống dòng
                                });

                                // Cập nhật lại giá trị các trường nếu có lỗi
                                $('#username').val(response.usernameValue || '');
                                $('#email').val(response.emailValue || '');
                                $('#phone').val(response.phoneValue || '');
                            }
                        },
                        error: function (xhr, status, error) {
                            Swal.close(); // Đóng popup nếu lỗi
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