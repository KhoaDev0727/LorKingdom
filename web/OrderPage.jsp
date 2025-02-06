<%-- 
    Document   : OrderPage
    Created on : Feb 6, 2025, 11:20:38 AM
    Author     : Truong Van Khang _ CE181852
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Đặt Hàng - Tên Cửa Hàng</title>
        <style>
            /* Reset CSS */
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            body {
                font-family: 'Arial', sans-serif;
                background-color: #f5f5f5;
            }

            /* Header */
            .header {
                background-color: #ffffff;
                padding: 20px;
                box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            }

            .progress-bar {
                display: flex;
                justify-content: space-between;
                max-width: 800px;
                margin: 20px auto;
            }

            .progress-step {
                text-align: center;
                color: #666;
            }

            /* Main Content */
            .container {
                max-width: 1200px;
                margin: 30px auto;
                padding: 20px;
                display: flex;
                gap: 30px;
            }

            /* Left Column - Cart Items */
            .cart-items {
                flex: 2;
                background: white;
                padding: 25px;
                border-radius: 10px;
            }

            .cart-item {
                display: flex;
                align-items: center;
                margin-bottom: 20px;
                padding-bottom: 20px;
                border-bottom: 1px solid #eee;
            }

            .item-image {
                width: 80px;
                margin-right: 15px;
            }

            /* Right Column - Order Summary */
            .order-summary {
                flex: 1;
                background: white;
                padding: 25px;
                border-radius: 10px;
                height: fit-content;
            }

            /* Form Elements */
            .form-group {
                margin-bottom: 15px;
            }

            label {
                display: block;
                margin-bottom: 5px;
                color: #333;
            }

            input, select {
                width: 100%;
                padding: 10px;
                border: 1px solid #ddd;
                border-radius: 5px;
            }

            /* Payment Section */
            .payment-methods {
                margin: 20px 0;
            }

            /* Button */
            .checkout-btn {
                background-color: #ff5722;
                color: white;
                padding: 15px 30px;
                border: none;
                border-radius: 5px;
                width: 100%;
                font-size: 16px;
                cursor: pointer;
            }

            /* Responsive Design */
            @media (max-width: 768px) {
                .container {
                    flex-direction: column;
                }
            }
        </style>
    </head>
    <body>
        <header class="header">
            <h1>Tên Cửa Hàng</h1>
            <div class="progress-bar">
                <div class="progress-step">Giỏ Hàng</div>
                <div class="progress-step">Thông Tin</div>
                <div class="progress-step">Thanh Toán</div>
                <div class="progress-step">Xác Nhận</div>
            </div>
        </header>

        <div class="container">
            <!-- Left Column - Cart Items & Delivery Info -->
            <div class="cart-items">
                <h2>Giỏ Hàng (3 sản phẩm)</h2>

                <!-- Cart Item 1 -->
                <div class="cart-item">
                    <img src="product1.jpg" alt="Product 1" class="item-image">
                    <div class="item-details">
                        <h3>Tên Sản Phẩm 1</h3>
                        <p>Số lượng: 1</p>
                        <p>Giá: 500.000₫</p>
                    </div>
                </div>

                <!-- Delivery Information -->
                <h2>Thông Tin Giao Hàng</h2>
                <form>
                    <div class="form-group">
                        <label>Họ và Tên:</label>
                        <input type="text" required>
                    </div>
                    <div class="form-group">
                        <label>Số Điện Thoại:</label>
                        <input type="tel" required>
                    </div>
                    <div class="form-group">
                        <label>Địa Chỉ:</label>
                        <textarea rows="3"></textarea>
                    </div>
                </form>
            </div>

            <!-- Right Column - Order Summary -->
            <div class="order-summary">
                <h2>Tóm Tắt Đơn Hàng</h2>
                <div class="summary-details">
                    <p>Tạm Tính: <span>1.500.000₫</span></p>
                    <p>Phí Vận Chuyển: <span>30.000₫</span></p>
                    <hr>
                    <p class="total">Tổng Cộng: <span>1.530.000₫</span></p>
                </div>

                <!-- Payment Section -->
                <div class="payment-methods">
                    <h3>Phương Thức Thanh Toán</h3>
                    <select>
                        <option>Thẻ Tín Dụng</option>
                        <option>COD</option>
                        <option>Ví Điện Tử</option>
                    </select>
                </div>

                <button class="checkout-btn">ĐẶT HÀNG</button>
            </div>
        </div>
    </body>
</html>