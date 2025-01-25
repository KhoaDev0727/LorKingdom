<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Email Verification</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            margin: 0;
            padding: 0;
            background-color: #fef5f5;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }

        .header {
            background-color: #ff5722;
            color: white;
            text-align: center;
            padding: 15px;
            border-bottom: 3px solid #ffb300;
        }

        .header h1 {
            font-size: 2.5rem;
            margin: 0;
            font-weight: 600;
        }

        .verify-container {
            max-width: 600px;
            background-color: white;
            padding: 25px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            text-align: center;
        }

        .verify-container i {
            font-size: 3rem;
            color: #ff5722;
            margin-bottom: 10px;
        }

        .verify-container h3 {
            font-weight: bold;
            color: #ff5722;
            margin-bottom: 20px;
        }

        .btn-primary {
            background-color: #ff5722;
            border: none;
        }

        .btn-primary:hover {
            background-color: #e64a19;
        }

        .btn-link {
            color: #ff5722;
            font-size: 0.9rem;
        }

        .btn-link:hover {
            text-decoration: underline;
        }

        .timer {
            margin-top: 15px;
            font-weight: bold;
            color: #333;
        }

        .btn-secondary {
            background-color: #f5f5f5;
            border: 1px solid #ccc;
            color: #555;
        }

        .btn-secondary:hover {
            background-color: #e0e0e0;
        }

        .footer {
            margin-top: 20px;
            font-size: 0.85rem;
            color: #888;
        }

        .footer a {
            color: #ff5722;
            text-decoration: none;
        }

        .footer a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="verify-container">
        <i class="fas fa-envelope"></i>
        <h3>Email Verification</h3>
        <p>We’ve sent a verification code to your email address. Please enter it below to verify your account.</p>
        <form action="VerifyCodeServlet" method="POST">
            <div class="mb-3 text-start">
                <label for="code" class="form-label">Verification Code</label>
                <input type="text" class="form-control" id="code" name="code" placeholder="Enter verification code" required>
                <% 
                    String errorMessage = (String) request.getAttribute("errorMessage");
                    if (errorMessage != null) { 
                %>
                <div class="text-danger mt-2"><%= errorMessage %></div>
                <% } %>
            </div>
            <button type="submit" class="btn btn-primary w-100">Verify</button>
        </form>
        <form action="ResendVerificationServlet" method="POST" class="mt-3">
            <button type="submit" class="btn btn-link">Resend Verification Code</button>
        </form>
        <div class="timer">
            Remaining time: <span id="time">02:00</span>
        </div>
        <div class="mt-3">
            <button class="btn btn-secondary w-100" onclick="location.href = 'SignIn.jsp'">Back to Sign In</button>
        </div>
        <div class="footer text-center">
            <p>Need help? <a href="ContactUs.jsp">Contact us</a></p>
        </div>
    </div>

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
    </script>
</body>
</html>
