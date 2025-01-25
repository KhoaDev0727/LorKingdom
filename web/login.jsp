<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Login Page</title>
  <!-- Bootstrap 5 CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <!-- Custom CSS -->
  <link rel="stylesheet" href="./css/stylelogin.css">
</head>
<body>

   <!-- Header Section -->
    <jsp:include page="header.jsp"/>
    <!-- end Header Section -->

  <!-- Banner -->
  <div class="banner container">
    <!-- Login Form -->
    <div class="login-container">
      <h3>Login to LorKingdom</h3>
      <form method="POST" action="LoginServlet">
        <!-- Email Input -->
        <div class="mb-3">
          <input type="text" class="form-control" id="email" name="email" placeholder="Email" value="${param.email}">
          
          <!-- Display email error if any -->
          <c:if test="${not empty requestScope.emailError}">
            <div class="text-danger">${requestScope.emailError}</div>
          </c:if>
        </div>

        <!-- Password Input -->
        <div class="mb-3">
          <input type="password" class="form-control" id="password" name="password" placeholder="Password">

          <!-- Display password error if any -->
          <c:if test="${not empty requestScope.passwordError}">
            <div class="text-danger">${requestScope.passwordError}</div>
          </c:if>
        </div>

        <!-- Display general error -->
        <c:if test="${not empty requestScope.error}">
          <div class="alert alert-danger">
            ${requestScope.error}
          </div>
        </c:if>

        <!-- Login Button -->
        <button type="submit" class="btn btn-primary login-btn">Login</button>

        <!-- Forgot Password Link -->
        <div class="forgot-password">
          <a href="#">Forgot password?</a>
        </div>

        <!-- Register Link -->
        <div class="register-link">
          <span>Don't have an account? <a href="register.jsp">Register</a></span>
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
          <img src="./img/pay1.png" alt="Visa">
          <img src="./img/pay2.png" alt="Mastercard">
          <img src="./img/pay3.png" alt="JCB">
          <br>
          <img src="./img/pay4.png" alt="AE">
          <img src="./img/pay5.png" alt="tra_gop">       
        </div>
        <h4>ĐƠN VỊ VẬN CHUYỂN</h4>
        <div class="shipping-partners">
          <img src="./img/ship1.png" alt="Giao Hàng Nhanh">
          <img src="./img/ship2.png" alt="Viettel Post">
          <br>
          <img src="./img/ship3.png" alt="J&T Express">
          <img src="./img/ship4.png" alt="J&T Express">        
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