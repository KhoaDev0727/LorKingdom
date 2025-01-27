
<%@page import="java.util.List"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Giỏ hàng</title>
        <link rel="stylesheet" href="assets/styleUser/cart.css">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="js/cartJs.js"></script>
    </head>
    <style>
        .error-message {
            color: red;
            font-weight: bold;
            margin: 10px 0;
        }

    </style>
    <%@include file="header.jsp" %>
    <body>

        <div class="cart-container">
            <div class="cart-content">
                <div class="cart-items">
                    <div class="cart-header">
                        <span>Sản phẩm trong giỏ hàng:</span> <span>${sessionScope.size}</span>
                    </div>
                    <div class="cart-table">
                        <table>
                            <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Hình ảnh</th>
                                    <th>Tên sản phẩm</th>
                                    <th>Giá</th>
                                    <th>Số lượng</th>
                                    <th>Tổng cộng</th>
                                    <th>Hành động</th>
                                </tr>
                            </thead>
                            <tbody>

                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="section3">
                    <div class="continue-shopping">
                        <a href="${pageContext.request.contextPath}/product">← Tiếp tục mua hàng</a>
                    </div>
                    <div class="delete-all">
                        <form action="cartServlet" method="POST" >
                            <input type="hidden" name="productID" value="0">
                            <input type="hidden" name="quantity" value="0">
                            <input type="hidden" name="action" value="deleteAll">
                            <button type="submit">Xóa tất cả sản phẩm</button>
                        </form>
                    </div>
                </div>

            </div>

            <div class="order-summary">
                <h3>THÔNG TIN CHUNG</h3>
                <div class="summary-details">
                    <p>Tổng sản phẩm: <span>${sessionScope.size}</span></p>
                    <p>Tổng tạm tính: <span class="total-amount"><fmt:formatNumber value="" pattern="#,###" /> VND</span></p>
                </div>
                <div class="total">

                    <p>Tổng đơn hàng: <span class="total-amount"><fmt:formatNumber value="" pattern="#,###" /> VND</span></p>
                </div>
                <form action="checkOutServlet" method="POST" class="pay">
                    <input type="hidden" name="pay" value="1">
                    <button type="submit">Thanh Toán</button>
                </form>

            </div>
        </div>
    </body>
    <%@include file="footer.jsp" %>

</html>
