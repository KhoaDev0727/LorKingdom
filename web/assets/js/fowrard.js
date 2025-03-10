document.addEventListener("DOMContentLoaded", function () {
    // Lấy các phần tử DOM chính
    const profileSection = document.getElementById("profile-section");
    const ordersSection = document.getElementById("orders-section");
    const profileLink = document.getElementById("profile-link");
    const ordersLink = document.getElementById("orders-link");
    const reviewModal = document.getElementById("reviewModal");

    // Kiểm tra các phần tử chính có tồn tại không
    if (!profileSection || !ordersSection || !profileLink || !ordersLink) {
        console.error("Không tìm thấy các phần tử profile hoặc orders trong DOM");
        return;
    }

    // Toggle giữa profile và orders section
    profileLink.addEventListener("click", (e) => {
        e.preventDefault();
        profileSection.classList.remove("hidden");
        ordersSection.classList.add("hidden");
    });

    ordersLink.addEventListener("click", (e) => {
        e.preventDefault();
        profileSection.classList.add("hidden");
        ordersSection.classList.remove("hidden");
    });

    // Xử lý tabs đơn hàng
    const tabs = document.querySelectorAll(".order-tabs .tab");
    if (tabs.length === 0) {
        console.error("Không tìm thấy tab đơn hàng");
        return;
    }

    tabs.forEach(tab => {
        tab.addEventListener("click", async function () {
            tabs.forEach(t => t.classList.remove("active"));
            this.classList.add("active");
            const status = this.getAttribute("data-status");
            if (status) {
                await loadOrders(status);
            } else {
                console.error("Không tìm thấy thuộc tính data-status trên tab");
            }
        });
    });

    // Hàm định dạng tiền tệ
    function formatCurrency(amount) {
        return new Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'}).format(amount || 0);
    }

    // Hiển thị thông báo không có đơn hàng
    function showNoOrders() {
        const orderContent = document.querySelector(".order-content");
        if (orderContent) {
            orderContent.innerHTML = `
                <div class="text-center py-4">
                    <img src="./assets/img/notifi-order.png" alt="Chưa có đơn hàng" class="no-order-img">
                    <p>Chưa có đơn hàng</p>
                </div>`;
        } else {
            console.error("Không tìm thấy .order-content");
        }
    }

    // Load dữ liệu đơn hàng từ API
    async function loadOrders(status) {
        try {
            const response = await fetch(`purchase?status=${status}`);
            if (!response.ok) {
                throw new Error(`Lỗi HTTP: ${response.status} - ${response.statusText}`);
            }
            const orders = await response.json();
            console.log("Dữ liệu đơn hàng:", orders); // Debug
            renderOrders(orders);
        } catch (error) {
            console.error("Lỗi khi tải đơn hàng:", error);
            showNoOrders();
        }
    }

    // Render danh sách đơn hàng
    function renderOrders(orders) {
        const orderContent = document.querySelector(".order-content");
        if (!orderContent) {
            console.error("Không tìm thấy .order-content để render đơn hàng");
            return;
        }

        if (!orders || !Array.isArray(orders) || orders.length === 0) {
            showNoOrders();
            return;
        }

        orderContent.innerHTML = orders.map(order => `
            <div class="order-item">
                <h3>Đơn hàng ${order.orderId || "Không xác định"}</h3>
                ${(order.orderDetails && Array.isArray(order.orderDetails) ? order.orderDetails : []).map(detail => `
                    <div class="d-flex mb-3 align-items-center">
                        <img src="${detail.productImage || './assets/img/default-product.png'}" 
                             class="me-3" style="width: 100px; height: 100px;" alt="Hình sản phẩm">
                        <div class="flex-grow-1">
                            <div class="fw-bold">${detail.productName || 'Không có tên'}</div>
                            <div class="text-muted">Phân loại: ${detail.categoryName || 'Mặc định'}</div>
                            <div class="text-muted">Số lượng: x${detail.quantity || 0}</div>
                        </div>
                        <div class="text-end">
                            <div class="text-danger fw-bold">${formatCurrency(detail.Price)}</div>
                            ${order.status === 'Delivered' ? `
                                <button class="btn btn-warning mt-2" 
                                        data-bs-toggle="modal" 
                                        data-bs-target="#reviewModal"
                                        data-product-id="${detail.productID || ''}"
                                        data-order-id="${order.orderId || ''}"
                                        data-product-name="${detail.productName || ''}"
                                        data-product-img="${detail.productImage || ''}">
                                    Đánh giá
                                </button>
                            ` : ''}
                        </div>
                    </div>
                `).join('')}
                <div class="border-top pt-3 text-end">
                    <div class="h4 text-danger fw-bold">Tổng: ${formatCurrency(order.totalAmount)}</div>
                </div>
            </div>
        `).join('');
    }

    // Xử lý khi modal đánh giá được mở
    if (reviewModal) {
        reviewModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            if (!button) {
                console.error("Không tìm thấy nút kích hoạt modal");
                return;
            }

            const productId = button.getAttribute('data-product-id') || '';
            const orderId = button.getAttribute('data-order-id') || '';
            const productName = button.getAttribute('data-product-name') || '';
            const productImg = button.getAttribute('data-product-img') || './assets/img/default-product.png';

            const modalProductId = document.getElementById('modal-productId');
            const modalOrderId = document.getElementById('modal-orderId');
            const modalProductName = document.getElementById('modal-productName');
            const modalProductImg = document.getElementById('modal-productImg');

            if (modalProductId)
                modalProductId.value = productId;
            if (modalOrderId)
                modalOrderId.value = orderId;
            if (modalProductName)
                modalProductName.textContent = productName;
            if (modalProductImg)
                modalProductImg.src = productImg;
        });
    } else {
        console.error("Không tìm thấy #reviewModal");
    }

 const ratingText = document.getElementById("rating-text");
    const ratingInput = document.getElementById("rating-input");
    const starContainer = document.getElementById("star-rating");

    if (starContainer && ratingText && ratingInput) {
        // Khởi tạo trạng thái mặc định 5 sao
        updateRating(5);
        
        // Sử dụng event delegation để xử lý click trên các sao
        starContainer.addEventListener("click", function (e) {
            const star = e.target.closest(".fa-star");
            if (star) {
                const rating = parseInt(star.dataset.rating);
                updateRating(rating);
                ratingInput.value = rating;
            }
        });

        function updateRating(rating) {
            const stars = starContainer.querySelectorAll(".fa-star");
            stars.forEach((star, index) => {
                const starRating = parseInt(star.dataset.rating);
                star.classList.toggle("active", starRating <= rating);
            });

            // Cập nhật văn bản đánh giá
            const ratingMessages = {
                1: "Kém",
                2: "Tạm được",
                3: "Tốt",
                4: "Rất tốt",
                5: "Tuyệt vời"
            };
            ratingText.textContent = ratingMessages[rating] || "Tuyệt vời";
        }
    } else {
        console.error("Không tìm thấy các phần tử rating");
    }
    // Load tab mặc định
    const initialTab = document.querySelector('.order-tabs .tab.active');
    if (initialTab) {
        initialTab.click();
    } else {
        console.warn("Không tìm thấy tab active, load mặc định 'Shipping'");
        loadOrders('Shipping');
    }
});