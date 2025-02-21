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
        <title>Verification Page</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="./CSS/styleVerifyCodeAdmin.css"/>
    </head>
    <body>
        <div class="verify-container">
            <i class="fas fa-envelope"></i>
            <h3>Email Verification</h3>
            <p>We’ve sent a verification code to your email address. Please enter it below to verify your account.</p>
            <form action="VerifyCodePage" method="POST">
                <div class="mb-3 text-start">
                    <label for="code" class="form-label">Verification Code</label>
                    <input type="text" class="form-control" id="code" name="code" placeholder="Enter verification code">
                    <% 
                        String errorMessage = (String) request.getAttribute("errorMessage");
                        if (errorMessage != null) { 
                    %>
                    <div class="text-danger mt-2"><%= errorMessage %></div>
                    <% } %>
                </div>
                <button type="submit" class="btn btn-primary w-100">Verify</button>
            </form>
            <form action="ResendVerificationPage" method="POST" class="mt-3">
                <button type="submit" class="btn btn-link">Resend Verification Code</button>
            </form>
            <div class="timer">
                Remaining time: <span id="time">02:00</span>
            </div>
            <div class="mt-3">
                <button class="btn btn-secondary w-100" onclick="location.href = 'loginPage.jsp'">Back to Login</button>
            </div>
            <div class="footer text-center">
                <p>Need help? <a href="ContactUs.jsp">Contact us</a></p>
            </div>
        </div>

        <!-- Modal thông báo thành công -->
        <div class="modal fade" id="successModal" tabindex="-1" aria-labelledby="successModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="successModalLabel">Verification Successful</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body" style="display: flex; flex-direction: column; align-items: center;">
                        <img src="./assets/img/verified.gif" alt="Success" width="100">
                        Your account has been successfully verified! You can now log in.
                    </div>
                    <div class="modal-footer">
                        <a href="loginPage.jsp" class="btn btn-primary">Go to Login</a>
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
                        display.textContent = "Code has expired.";
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

