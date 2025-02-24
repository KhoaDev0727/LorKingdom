<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@include file="assets/Component/header.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Giỏ hàng</title>
    <link rel="stylesheet" href="assets/styleUser/cart.css">
</head>
<body>
    <c:if test="${not empty sessionScope.errorMessage}">
        <div class="error-message">
            ${sessionScope.errorMessage}
            <% session.removeAttribute("errorMessage"); %>
        </div>
    </c:if>

    <div class="cart-container">
        <div class="cart-content">
            <div class="cart-items">
                <div class="cart-header">
                    <span>Sản phẩm trong giỏ hàng:</span> 
                    <span>${not empty sessionScope.size ? sessionScope.size : 0}</span>
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
                            <c:choose>
                                <c:when test="${empty listCart}">
                                    <tr>
                                        <td colspan="7" class="empty-cart">Giỏ hàng của bạn đang trống</td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach items="${listCart}" var="item" varStatus="loop">
                                        <tr>
                                            <td>${loop.index + 1}</td>
                                            <td>
                                                <img src="${item.product.mainImageUrl}" 
                                                     alt="${item.product.name}" 
                                                     class="product-img">
                                            </td>
                                            <td>${item.product.name}</td>
                                            <td class="unit-price">
                                                <fmt:formatNumber value="${item.price}" pattern="#,###" /> VND
                                            </td>
                                            <td>
                                                <div class="quantity-controls">
                                                    <button class="quantity-btn" 
                                                            onclick="updateQuantity(${item.product.productID}, 'update','decrease')">-</button>
                                                    <input type="text" class="quantity-input" value="${item.quantity}" readonly>
                                                    <button class="quantity-btn" 
                                                            onclick="updateQuantity(${item.product.productID},'update' ,'increase')">+</button>
                                                </div>
                                            </td>
                                            <td class="total-price">
                                                <fmt:formatNumber value="${item.price * item.quantity}" pattern="#,###" /> VND
                                            </td>
                                            <td>
                                                <button class="delete-btn" 
                                                        onclick="deleteItem(${item.product.productID})">Xóa</button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="section3">
                <div class="continue-shopping">
                    <a href="${pageContext.request.contextPath}/getList">← Tiếp tục mua hàng</a>
                </div>
                <c:if test="${not empty listCart}">
                    <div class="delete-all">
                        <form action="Cart?&action=deleteAllItem" method="GET">
                            <input type="hidden" name="action" value="deleteAll">
                            <button type="submit">Xóa tất cả</button>
                        </form>
                    </div>
                </c:if>
            </div>
        </div>

        <div class="order-summary">
            <h3>THÔNG TIN CHUNG</h3>
            <div class="summary-details">
                <p>Tổng sản phẩm: <span>${not empty size ? size : 0}</span></p>
                <p>Tổng tạm tính: 
                    <span class="total-amount">
                        <fmt:formatNumber value="${not empty totalMoney ? totalMoney : 0}" pattern="#,###" /> VND
                    </span>
                </p>
            </div>
            <div class="total">
                <p>Tổng đơn hàng: 
                    <span class="total-amount">
                        <fmt:formatNumber value="${not empty totalMoney ? totalMoney : 0}" pattern="#,###" /> VND
                    </span>
                </p>
            </div>
            <c:if test="${not empty listCart}">
                <form action="checkOutServlet" method="POST" class="pay">
                    <button type="submit">Thanh Toán</button>
                </form>
            </c:if>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <<script src="assets/js/cartJs.js"></script>
</body>
</html>