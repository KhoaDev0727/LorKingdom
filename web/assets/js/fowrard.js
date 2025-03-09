/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


document.addEventListener("DOMContentLoaded", function () {
    const profileSection = document.getElementById("profile-section");
    const ordersSection = document.getElementById("orders-section");
    const profileLink = document.getElementById("profile-link");
    const ordersLink = document.getElementById("orders-link");
    
    // Toggle giữa các phần
    profileLink.addEventListener("click", () => {
        profileSection.classList.remove("hidden");
        ordersSection.classList.add("hidden");
    });
    
    ordersLink.addEventListener("click", () => {
        profileSection.classList.add("hidden");
        ordersSection.classList.remove("hidden");
    });

    // Xử lý tab đơn hàng
    const tabs = document.querySelectorAll(".order-tabs .tab");
    tabs.forEach(tab => {
        tab.addEventListener("click", async function () {
            tabs.forEach(t => t.classList.remove("active"));
            this.classList.add("active");
            const status = this.getAttribute("data-status");
            try {
                const response = await fetch(`purchase?status=${status}`);
                if (!response.ok) throw new Error('Lỗi kết nối');
                const orders = await response.json();
                renderOrders(orders);
            } catch (error) {
                console.error('Lỗi:', error);
                showNoOrders();
            }
        });
    });

    // Hàm định dạng tiền tệ
    function formatCurrency(amount) {
        return new Intl.NumberFormat('vi-VN', { 
            style: 'currency', 
            currency: 'VND' 
        }).format(amount);
    }

    // Hiển thị thông báo khi không có đơn hàng
    function showNoOrders() {
        const orderContent = document.querySelector(".order-content");
        orderContent.innerHTML = '<div class="text-center py-4">Không có đơn hàng nào.</div>';
    }

    // Render danh sách đơn hàng
    function renderOrders(orders) {
        const orderContent = document.querySelector(".order-content");
        if (!orders || orders.length === 0) {
            showNoOrders();
            return;
        }

        orderContent.innerHTML = orders.map(order => `
            <div class="order-item">
                <h3>Đơn hàng ${order.orderId}</h3>
                <div class="border-top pt-4">
                    <div class="bg-white p-4 rounded-lg shadow-sm">
                        ${order.orderDetails.map(detail => `
                            <div class="d-flex mb-4">
                                <img src="${detail.productImage || './assets/img/default-product.png'}" 
                                    class="img-fluid mr-4" 
                                    style="width: 100px; height: 100px;">
                                <div class="flex-grow-1">
                                    <div class="font-weight-bold">${detail.productName}</div>
                               <div class="text-muted">Phân loại hàng: ${detail.productVariant || 'Mặc định'}</div>
                                    <div class="text-muted">x${detail.quantity}</div>
                                </div>
                                <div class="text-right">
                                    <div class="text-danger font-weight-bold">
                                        ${formatCurrency(detail.unitPrice)}
                                    </div>
                                </div>
                            </div>
                        `).join('')}
                        <div class="border-top pt-4">
                            <div class="d-flex justify-content-between align-items-center">
                                <div class="h4 text-danger font-weight-bold">
                                    Thành tiền: ${formatCurrency(order.totalAmount)}
                                </div>
                                ${order.status === 'pending' ? `
                                    <div class="d-flex">
                                        <button class="btn btn-warning text-white mr-2">Đánh Giá</button>
                                    </div>
                                ` : ''}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        `).join('');
    }

    // Tải dữ liệu ban đầu cho tab active
    const initialTab = document.querySelector('.order-tabs .tab.active');
    if (initialTab) initialTab.click();
});