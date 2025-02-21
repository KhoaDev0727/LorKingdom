<%-- 
    Document   : resetPassword
    Created on : Feb 19, 2025, 10:51:52 PM
    Author     : le minh khoa
--%><%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reset Password</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@300;400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="./CSS/resetPasswordPage.css"/>
</head>
<body>
    <div class="container mt-5">
        <div class="reset-password-container">
            <h3>Reset Your Password</h3>
            <form action="ResetPasswordServlet" method="POST">
                <input type="hidden" name="token" value="${param.token}">
                <div class="mb-3">
                    <input type="password" class="form-control" name="password" placeholder="New Password">
                </div>
                <button type="submit" class="btn btn-primary">Reset Password</button>
            </form>

            <c:if test="${not empty requestScope.message}">
                <div class="alert alert-info mt-3">${requestScope.message}</div>
            </c:if>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        // Kiểm tra nếu có thông báo thành công
        var message = '${requestScope.message}';
        
        if (message === 'Password has been reset successfully.') {
            var countdown = 5;
            var countdownInterval = setInterval(function() {
                if (countdown <= 0) {
                    clearInterval(countdownInterval);
                    window.location.href = 'login.jsp'; // Chuyển hướng đến login.jsp
                } else {
                    document.querySelector('.alert').innerHTML = 'Password has been reset successfully. Redirecting to login in ' + countdown + ' seconds...';
                    countdown--;
                }
            }, 1000);
        }
    </script>
</body>
</html>

