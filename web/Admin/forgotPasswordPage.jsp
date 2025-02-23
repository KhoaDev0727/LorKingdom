<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Forgot Password</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@300;400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="./CSS/forgotPasswordPage.css"/>
</head>
<body style="background-image: url(./assets/img/back-forgot.jpg);
             background-size: cover;">
    <div class="container mt-5">
        <div class="forgot-password-container" style="background-color: rgb(231, 231, 231);">
            <h3>Forgot Your Password?</h3>
            <p>Enter your email address to receive a reset link.</p>
            <form action="ForgotPasswordPage" method="POST">
                <div class="mb-3">
                    <input type="email" class="form-control" name="email" placeholder="Enter your email">
                </div>
                <button type="submit" class="btn btn-primary">Send Reset Link</button>
            </form>

            <c:if test="${not empty requestScope.message}">
                <div class="alert alert-info mt-3" style="background-color: rgb(231, 231, 231);">${requestScope.message}</div>
            </c:if>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
