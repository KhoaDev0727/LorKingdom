<%-- 
    Document   : verifyCodePage
    Created on : Feb 18, 2025, 5:54:56 PM
    Author     : le minh khoa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Trang xác minh</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="./CSS/styleVerifyCodeAdmin.css"/>
    </head>
    <body>
        <div class="verify-container">
            <i class="fas fa-envelope"></i>
            <h3>Xác minh Email</h3>
            <p>Chúng tôi đã gửi mã xác minh đến địa chỉ email của bạn. Vui lòng nhập mã bên dưới để xác minh tài khoản của bạn.</p>
            <form action="VerifyCodePage" method="POST">
                <div class="mb-3 text-start">
                    <label for="code" class="form-label">Mã xác minh</label>
                    <input type="text" class="form-control" id="code" name="code" placeholder="Nhập mã xác minh">
                    <% 
                        String errorMessage = (String) request.getAttribute("errorMessage");
                        if (errorMessage != null) { 
                    %>
                    <div class="text-danger mt-2"><%= errorMessage %></div>
                    <% } %>
                </div>
                <button type="submit" class="btn btn-primary w-100">Xác minh</button>
            </form>
            <form action="ResendVerificationPage" method="POST" class="mt-3">
                <button type="submit" class="btn btn-link">Gửi lại mã xác minh</button>
            </form>
            <div class="timer">
                Thời gian còn lại: <span id="time">02:00</span>
            </div>
            <div class="mt-3">
                <button class="btn btn-secondary w-100" onclick="location.href = 'loginPage.jsp'">Quay lại Đăng nhập</button>
            </div>
            <div class="footer text-center">
                <p>Cần giúp đỡ? <a href="ContactUs.jsp">Liên hệ với chúng tôi</a></p>
            </div>
        </div>

        <!-- Modal thông báo thành công -->
        <div class="modal fade" id="successModal" tabindex="-1" aria-labelledby="successModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="successModalLabel">Xác minh thành công</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body" style="display: flex; flex-direction: column; align-items: center;">
                        <img src="./assets/img/verified.gif" alt="Success" width="100">
                        Tài khoản của bạn đã được xác minh thành công! Bây giờ bạn có thể đăng nhập.
                    </div>
                    <div class="modal-footer">
                        <a href="loginPage.jsp" class="btn btn-primary">Đi đến Đăng nhập</a>
                    </div>
                </div>
            </div>
        </div>


        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            function startTimer(duration, display) {
                var timer = duration, minutes, seconds;
                var countdown = setInterval(function () {
                    minutes = parseInt(timer / 60, 10);
                    seconds = parseInt(timer % 60, 10);

                    minutes = minutes < 10 ? "0" + minutes : minutes;
                    seconds = seconds < 10 ? "0" + seconds : seconds;

                    display.textContent = minutes + ":" + seconds;

                    if (--timer < 0) {
                        clearInterval(countdown);
                        display.textContent = "Mã đã hết hạn.";
                    }
                }, 1000);
            }

            window.onload = function () {
            <% 
                    if (session != null && session.getAttribute("codeExpiryTime") != null) {
                        long expiryTime = (Long) session.getAttribute("codeExpiryTime");
                        long currentTime = System.currentTimeMillis();
                        long remainingTime = (expiryTime - currentTime) / 1000;
                        if (remainingTime < 0) {
                            remainingTime = 0;
                        }
            %>
                var duration = <%= remainingTime %>;
                var display = document.querySelector('#time');
                startTimer(duration, display);
            <% 
                    } else { 
            %>
                session.setAttribute("codeExpiryTime", System.currentTimeMillis() + 120 * 1000);
                var duration = 120;
                var display = document.querySelector('#time');
                startTimer(duration, display);
            <% 
                    }
            %>
            };

            // Kiểm tra tham số status từ URL
            function getQueryParam(param) {
                let params = new URLSearchParams(window.location.search);
                return params.get(param);
            }

            document.addEventListener("DOMContentLoaded", function () {
                if (getQueryParam("status") === "success") {
                    let successModal = new bootstrap.Modal(document.getElementById("successModal"));
                    successModal.show();
                }
            });
        </script>
    </body>
</html>

