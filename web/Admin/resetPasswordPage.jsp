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
    <title>Trang Đặt lại mật khẩu</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@300;400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="./CSS/resetPasswordPage.css"/>
</head>
<body style="background-image: url(./assets/img/back-forgot.jpg);
             background-size: cover;">
    <div class="container mt-5">
        <div class="reset-password-container" style="background-color: rgb(231, 231, 231);">
            <h3>Đặt lại mật khẩu của bạn</h3>
            <form action="ResetPasswordPage" method="POST">
                <input type="hidden" name="token" value="${param.token}">
                <div class="mb-3">
                    <input type="password" class="form-control" name="password" placeholder="Mật khẩu mới">
                </div>
                <button type="submit" class="btn btn-primary">Đặt lại mật khẩu</button>
            </form>

            <c:if test="${not empty requestScope.message}">
                <div class="alert alert-info mt-3" style="background-color: rgb(231, 231, 231);">${requestScope.message}</div>
            </c:if>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        // Kiểm tra nếu có thông báo thành công
        var message = '${requestScope.message}';
        
        if (message === 'Mật khẩu đã được đặt lại thành công.') {
            var countdown = 5;
            var countdownInterval = setInterval(function() {
                if (countdown <= 0) {
                    clearInterval(countdownInterval);
                    window.location.href = 'loginPage.jsp'; // Chuyển hướng đến login
                } else {
                    document.querySelector('.alert').innerHTML = 'Mật khẩu đã được đặt lại thành công. Đang chuyển hướng đến đăng nhập trong ' + countdown + ' giây...';
                    countdown--;
                }
            }, 1000);
        }
    </script>
</body>
</html>

