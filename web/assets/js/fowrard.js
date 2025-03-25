/* global Swal, $, fetch */

document.addEventListener("DOMContentLoaded", function () {
    let currentPage = 1;
    let totalPages = 1;
    let currentStatus = ''; 
    

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

    // Hàm hiển thị phân trang
    function renderPagination(total, pageSize) {
        const paginationContainer = document.getElementById("pagination-container");
        if (!paginationContainer) {
            console.error("Không tìm thấy #pagination-container");
            return;
        }

        const totalPages = Math.ceil(total / pageSize);
        let html = '';

        // Nút "Previous"
        if (currentPage > 1) {
            html += `<button class="page-btn" data-page="${currentPage - 1}"><i class="fas fa-chevron-left"></i></button>`;
        }

        // Tính toán trang bắt đầu và kết thúc
        let startPage = Math.max(1, currentPage - Math.floor(5 / 2));
        let endPage = Math.min(totalPages, startPage + 5 - 1);

        // Đảm bảo hiển thị đủ 5 trang
        if (endPage - startPage + 1 < 5) {
            startPage = Math.max(1, endPage - 5 + 1);
        }

        // Thêm dấu "..." nếu cần
        if (startPage > 1) {
            html += `<button class="page-btn" data-page="1">1</button>`;
            if (startPage > 2) {
                html += `<span class="ellipsis">...</span>`;
            }
        }

        // Thêm các nút trang
        for (let i = startPage; i <= endPage; i++) {
            html += `<button class="page-btn ${i === currentPage ? 'active' : ''}" data-page="${i}">${i}</button>`;
        }

        // Thêm dấu "..." nếu cần
        if (endPage < totalPages) {
            if (endPage < totalPages - 1) {
                html += `<span class="ellipsis">...</span>`;
            }
            html += `<button class="page-btn" data-page="${totalPages}">${totalPages}</button>`;
        }

        // Nút "Next"
        if (currentPage < totalPages) {
            html += `<button class="page-btn" data-page="${currentPage + 1}"><i class="fas fa-chevron-right"></i></button>`;
        }

        paginationContainer.innerHTML = html;

        // Thêm event listener cho các nút trang
        document.querySelectorAll('.page-btn').forEach(btn => {
            btn.addEventListener('click', async () => {
                currentPage = parseInt(btn.dataset.page);
                await loadOrders(currentStatus, currentPage);
            });
        });
    }

    // Hàm tải đơn hàng
    async function loadOrders(status, page = 1) {
        try {
            currentPage = page;
            currentStatus = status;

            const response = await fetch(`purchase?status=${status}&page=${page}`);
            if (!response.ok)
                throw new Error(`Lỗi HTTP: ${response.status}`);
            const data = await response.json();

            if (data.error || !data.orders || data.orders.length === 0) {
                showNoOrders();
                return;
            }

            renderOrders(data.orders);
            renderPagination(data.total, 3); // pageSize = 3
        } catch (error) {
            console.error("Lỗi khi tải đơn hàng:", error);
            showNoOrders();
    }
    }
    // Hàm tính giá gốc từ giá đã giảm và phần trăm giảm giá
    function calculateOriginalPrice(discountedPrice, discountPercentage) {
        if (!discountPercentage || discountPercentage <= 0) return discountedPrice;
        return discountedPrice / (1 - (discountPercentage / 100));
    }

    // Hàm hiển thị đơn hàng
    function renderOrders(orders) {
        const orderContent = document.querySelector(".order-content");
        if (!orderContent) {
            console.error("Không tìm thấy .order-content");
            return;
        }

        orderContent.innerHTML = orders.map(order => {
            return `     
        <div class="order-item card mb-4">
            <div class="card-header">
                <h3 class="d-inline-block me-3">Đơn hàng ${order.orderId}</h3>
            </div>
            <div class="card-body p-0">
                ${order.orderDetails.map(detail => {
                    // Tính giá gốc nếu có discount
                    const hasDiscount = detail.discount && detail.discount > 0;
                    const originalPrice = detail.originalPrice || (hasDiscount ? calculateOriginalPrice(detail.unitPrice, detail.discount) : null);
                    
                    return `
                    <div class="border-bottom p-3">
                        <div class="row align-items-center mb-3">
                            <div class="col-12 col-md-3 mb-3 mb-md-0">
                                <img alt="${detail.productName}" 
                                    class="img-fluid img-thumbnail img-fixed-size" 
                                    src="http://localhost:8080/LorKingdom${detail.productImage || './assets/img/default-product.png'}" />
                            </div>
                            <div class="col-12 col-md-6 mb-3 mb-md-0">
                                <h5 class="mb-2 text-start">${detail.productName}</h5>
                                <p class="text-muted mb-1 text-start" style="padding-left: 0;">Phân loại hàng: ${detail.categoryName}</p>
                                <p class="text-muted mb-0 text-start" style="padding-left: 0;">Số lượng x${detail.quantity}</p>
                            </div>
                            <div class="col-12 col-md-3 text-md-end">
                                ${originalPrice && originalPrice > detail.unitPrice ?
                                    `<p class="text-decoration-line-through text-muted mb-0 text-end">
                                         ${formatCurrency(detail.unitPrice)} 
                                    </p>
                                    <p class="text-success mb-0">
                                        <small>Giảm ${detail.discount || Math.round((1 - detail.unitPrice/originalPrice) * 100)}%</small>
                                    </p>` : ''
                                }
                                <p class="text-danger fw-bold mb-0">
                                  
                                     ${formatCurrency(detail.TotalPrice)}
                                </p>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-12 d-flex justify-content-end">
                                ${order.status === 'Delivered' && detail.Reviewed === 0
                                ? `
                                    <button 
                                        class="btn btn-danger"
                                        data-bs-toggle="modal" 
                                        data-bs-target="#reviewModal"
                                        data-product-id="${detail.productID}" 
                                        data-order-id="${detail.orderID}"
                                        modal-OrderDetail-Id="${detail.orderDetailID}" 
                                        data-category-name="${detail.categoryName}"
                                        data-product-name="${detail.productName}"
                                        data-product-img="http://localhost:8080/LorKingdom${detail.productImage}">
                                        Đánh Giá
                                    </button>
                                    `
                                : order.status === 'Delivered' && detail.Reviewed !== 0
                                ? `
                                        <span class="text-muted mt-2">
                                            Bạn đã đánh giá
                                        </span>
                                        `
                                : ''
                                }
                            </div>
                        </div>
                    </div>
                    `;
                }).join('')}
                <div class="p-3">
                    <div class="row align-items-center">
                        <div class="col-12 col-md-6 mb-3 mb-md-0">
                            <p class="h5 fw-bold mb-0">
                                Thành tiền:
                                <span class="text-danger text-start">
                                    ${formatCurrency(order.totalAmount)}
                                </span>
                            </p>
                        </div>
                        <div class="col-12 col-md-6 text-md-end">
                            <button class="btn btn-outline-secondary me-2" onclick="showOrderDetail(${order.orderId})">
                                Xem Chi Tiết Đơn Hàng
                            </button>
                            ${order.status === 'Pending'
                            ? `
                                <button class="btn btn-outline-danger" onclick="cancelOrder(${order.orderId})"> 
                                    Hủy Đơn Hàng
                                </button>
                                `
                            : ''
                            }
                        </div>
                    </div>
                </div>
            </div>
        </div>
        `;
        }).join('');
    }

    // Xử lý khi modal đánh giá được mở
    if (reviewModal) {
        reviewModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            if (!button) {
                console.error("Không tìm thấy nút kích hoạt modal");
                return;
            }

            const orderDetailId = button.getAttribute('modal-OrderDetail-Id') || '';
            const productId = button.getAttribute('data-product-id') || '';
            const orderId = button.getAttribute('data-order-id') || '';
            const productName = button.getAttribute('data-product-name') || '';
            const categoryName = button.getAttribute('data-category-name') || '';
            const productImg = button.getAttribute('data-product-img') || './assets/img/default-product.png';

            const modalOrderDetail = document.getElementById('modal-OrderDetail-Id');
            const modalProductId = document.getElementById('modal-productId');
            const modalOrderId = document.getElementById('modal-orderId');
            const modalCategoryName = document.getElementById('modal-productCategory');
            const modalProductName = document.getElementById('modal-productName');
            const modalProductImg = document.getElementById('modal-productImg');

            if (modalProductId)
                modalProductId.value = productId;
            if (modalOrderId)
                modalOrderId.value = orderId;
            if (modalOrderDetail)
                modalOrderDetail.value = orderDetailId;
            if (modalProductName)
                modalProductName.textContent = productName;
            if (modalProductImg)
                modalProductImg.src = productImg;
            if (modalCategoryName)
                modalCategoryName.textContent = categoryName;
        });
    } else {
        console.error("Không tìm thấy #reviewModal");
    }

    // Xử lý đánh giá sao
    const ratingText = document.getElementById("rating-text");
    const ratingInput = document.getElementById("rating-input");
    const starContainer = document.getElementById("star-rating");

    if (starContainer && ratingText && ratingInput) {
        updateRating(5); // Khởi tạo mặc định 5 sao

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

    // Xử lý submit form đánh giá
    document.getElementById('reviewForm').addEventListener('submit', async (e) => {
        e.preventDefault();

        const orderDetailID = document.getElementById('modal-OrderDetail-Id').value;
        const productId = document.getElementById('modal-productId').value;
        const orderId = document.getElementById('modal-orderId').value;
        const rating = document.getElementById('rating-input').value;
        const comment = document.getElementById('review-comment').value;
        const imageInput = document.getElementById('review-image');
        const action = document.getElementById('modal-action').value;
        const imageFile = imageInput.files[0];

        const formData = new FormData();
        formData.append('productId', productId);
        formData.append('orderId', orderId);
        formData.append('rating', rating);
        formData.append('comment', comment);
        formData.append('action', action);
        formData.append('orderDetailID', orderDetailID);
        if (imageFile)
            formData.append('image', imageFile);

        try {
            const response = await fetch('ReviewManagementServlet', {
                method: 'POST',
                body: formData
            });

            const responseText = await response.text();
            let result;
            try {
                result = JSON.parse(responseText);
            } catch (jsonError) {
                console.error("Lỗi khi phân tích JSON:", jsonError);
                throw new Error("Phản hồi từ server không phải là JSON hợp lệ");
            }

            if (result.success) {
                Swal.fire({
                    icon: 'success',
                    title: 'Đánh giá thành công!',
                    text: 'Cảm ơn bạn đã đánh giá sản phẩm!',
                    showConfirmButton: false,
                    timer: 2000
                });

                $('#reviewModal').modal('hide');
                document.getElementById('reviewForm').reset();
                imageInput.value = '';
                await loadOrders(currentStatus, currentPage);
            } else {
                throw new Error(result.message || 'Lỗi khi gửi đánh giá');
            }
        } catch (error) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi',
                text: error.message || 'Có lỗi xảy ra khi gửi đánh giá'
            });
        }
    });
});

// Hàm hủy đơn hàng// Hàm hủy đơn hàng
function cancelOrder(orderID) {
    console.log(orderID)
    Swal.fire({
        title: 'Bạn có chắc chắn muốn hủy đơn hàng này không?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Đồng ý',
        cancelButtonText: 'Hủy bỏ',
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`http://localhost:8080/LorKingdom/CancelOrder?orderID=${orderID}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
            })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error(`Lỗi HTTP: ${response.status}`);
                        }
                        return response.json();
                    })
                    .then(data => {
                        if (data.success) {
                            Swal.fire({
                                icon: 'success',
                                title: 'Thành công!',
                                text: 'Đơn hàng đã được hủy thành công.',
                            }).then(() => {
                                location.reload(); // Tải lại trang để cập nhật danh sách đơn hàng
                            });
                        } else {
                            Swal.fire({
                                icon: 'error',
                                title: 'Lỗi!',
                                text: data.message || 'Có lỗi xảy ra khi hủy đơn hàng.',
                            });
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        Swal.fire({
                            icon: 'error',
                            title: 'Lỗi!',
                            text: 'Có lỗi xảy ra khi kết nối đến máy chủ.',
                        });
                    });
        }
    });
}

//Form show review chi tieet
async function showOrderDetail(orderId) {
    console.log(orderId);
    try {
        const response = await fetch(`http://localhost:8080/LorKingdom/CancelOrder?orderId=${orderId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) {
            throw new Error(`Lỗi HTTP: ${response.status}`);
        }

        const data = await response.json();
        const order = data.order;
        const orderDetails = data.orderDetails;

        const orderDetailContent = document.getElementById('order-detail-content');
        if (orderDetailContent) {
            orderDetailContent.innerHTML = `
                <div class="order-detail card">
                    <div class="card-header bg-light">
                        <h4 class="mb-0">Đơn hàng #${order.orderId}</h4>
                    </div>
                    <div class="card-body">
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <p class="mb-2"><strong>Trạng thái:</strong> <span class="badge ${getStatusBadgeClass(order.status)}">${order.status}</span></p>
                                <p class="mb-2"><strong>Ngày đặt hàng:</strong> ${new Date(order.orderDate).toLocaleDateString()}</p>
                            </div>
                            <div class="col-md-6">
                                <p class="mb-2"><strong>Phương thức thanh toán:</strong> <span class="text-primary">${order.payMentMethodName}</span></p>
                                <p class="mb-2"><strong>Phương thức vận chuyển:</strong> ${order.shipingMethodName}</p>
                            </div>
                        </div>
                        
                        <hr>
                        <h5 class="mb-3">Chi tiết sản phẩm:</h5>
                        <div class="product-list">
                            ${orderDetails.map(detail => `
                                <div class="product-item card mb-3">
                                    <div class="card-body p-3">
                                        <div class="row align-items-center">
                                            <div class="col-12 col-md-3 mb-3 mb-md-0">
                                                <img src="http://localhost:8080/LorKingdom${detail.productImage}" 
                                                     alt="${detail.productName}" 
                                                     class="img-fluid img-thumbnail product-image">
                                            </div>
                                            <div class="col-12 col-md-9">
                                                <h6 class="fw-bold text-start mb-2">${detail.productName}</h6>
                                                <div class="text-start mb-2">
                                                    <span class="text-muted">Phân loại: ${detail.categoryName}</span>
                                                </div>
                                                <div class="row mb-2">
                                                    <div class="col-6 text-start">
                                                        <span>Số lượng: ${detail.quantity}</span>
                                                    </div>
                                                    <div class="col-6 text-start text-md-end">
                                                        <span>Đơn giá: ${formatCurrency(detail.unitPrice)}</span>
                                                    </div>
                                                </div>
                                                <div class="row mb-3">
                                                    <div class="col-6 text-start">
                                                        <span>Giảm giá: ${detail.discount}%</span>
                                                    </div>
                                                    <div class="col-6 text-start text-md-end">
                                                        <span class="fw-bold">Thành tiền: ${formatCurrency(detail.TotalPrice)}</span>
                                                    </div>
                                                </div>
                                                <div class="d-flex ${detail.Reviewed === 0 ? 'justify-content-between' : 'justify-content-end'}">
                                                    ${detail.Reviewed === 0 && order.status === 'Pending'
                                                        ? `<button class="btn btn-danger" onclick="cancelOrder(${detail.orderID})">
                                                            <i class="bi bi-x-circle me-1"></i>Hủy đơn hàng
                                                           </button>`
                                                        : ''
                                                    }
                                                    ${order.status === 'Delivered' && detail.Reviewed === 0
                                                        ? `<button class="btn btn-primary" 
                                                                 data-bs-toggle="modal" 
                                                                 data-bs-target="#reviewModal" 
                                                                 data-product-id="${detail.productID}" 
                                                                 data-order-id="${detail.orderID}" 
                                                                 modal-OrderDetail-Id="${detail.orderDetailID}" 
                                                                 data-category-name="${detail.categoryName}" 
                                                                 data-product-name="${detail.productName}" 
                                                                 data-product-img="http://localhost:8080/LorKingdom${detail.productImage}">
                                                            <i class="bi bi-star me-1"></i>Đánh giá
                                                           </button>`
                                                        : detail.Reviewed === 1
                                                            ? `<span class="badge bg-success"><i class="bi bi-check-circle me-1"></i>Đã đánh giá</span>`
                                                            : ''
                                                    }
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            `).join('')}
                        </div>
                    </div>
                    <div class="card-footer bg-light d-flex justify-content-end">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                    </div>
                </div>
            `;
        }

        // Hiển thị modal
        const orderDetailModalElement = document.getElementById('orderDetailModal');
        if (orderDetailModalElement) {
            const orderDetailModal = new bootstrap.Modal(orderDetailModalElement);
            orderDetailModal.show();

            // Thêm sự kiện click để đóng modal
            const closeButton = document.querySelector('.btn-close');
            if (closeButton) {
                closeButton.addEventListener('click', function () {
                    orderDetailModal.hide();
                });
            }
        } else {
            console.error("Không tìm thấy phần tử modal với ID 'orderDetailModal'");
        }
    } catch (error) {
        console.error("Lỗi khi tải chi tiết đơn hàng:", error);
        Swal.fire({
            icon: 'error',
            title: 'Lỗi',
            text: 'Có lỗi xảy ra khi tải chi tiết đơn hàng'
        });
    }
}

// Hàm hỗ trợ để lấy class cho trạng thái đơn hàng
function getStatusBadgeClass(status) {
    console.log(status)
    switch(status) {
        case 'Đang Xử Lí': return 'bg-warning text-dark';
        case 'Đang Vận Chuyển': return 'bg-primary';    
        case 'Đã Nhận Hàng': return 'bg-success';
        case 'Đã Hủy': return 'bg-danger';
        default: return 'bg-secondary';
    }
}

// Hàm định dạng tiền tệ (giả định)
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'}).format(amount);
}