<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="Model.Account" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="account" value="${sessionScope.account}" />
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản lý thông tin</title>
        <link rel="stylesheet" href="assets/styleUser/Setting.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <style>
            .text-muted {
                text-align: left;
            }
            .hidden {
                display: none;
            }
            .no-order-img {
                max-width: 200px;
                margin: 20px auto;
            }
            .order-item {
                margin-bottom: 20px;
                border: 1px solid #eee;
                padding: 15px;
            }
            .modal {
                z-index: 1050;
            }
            .modal-backdrop {
                z-index: 1040;
            }
            #star-rating .fa-star {
                color: #e4e5e9; /* Màu xám cho sao chưa chọn */
                cursor: pointer; /* Con trỏ chuột hiển thị kiểu nhấp được */
            }

            #star-rating .fa-star.active {
                color: #ffc107; /* Màu vàng cho sao được chọn */
            }
            /* Thêm vào phần style */
            .page-btn {
                padding: 5px 12px;
                border: 1px solid #ddd;
                margin: 0 2px;
                cursor: pointer;
                border-radius: 4px;
            }

            .page-btn.active {
                background-color: #dc3545;
                color: white;
                border-color: #dc3545;
            }
        </style>
    </head>
    <body>
        <c:choose>
            <c:when test="${empty sessionScope.roleID}">
                <c:redirect url="/login.jsp"/>
            </c:when>
            <c:when test="${sessionScope.roleID eq 2 || sessionScope.roleID eq 4 ||  sessionScope.roleID eq 1}">
                <c:redirect url="/Admin/loginPage.jsp"/>
            </c:when>
        </c:choose>
        <%@include file="assets/Component/header.jsp" %>

        <div class="container-fluid">
            <div class="sidebar">
                <div class="profile">
                    <div class="d-flex container profile-img">
                        <c:if test="${not empty account.image}">
                            <img style="width: 150px; height: 150px" src="${account.image}" alt="Avatar" class="avatar">
                        </c:if>
                        <span class="username">Xin chào, ${account.userName}</span>
                    </div>
                </div>
                <nav class="nav">
                    <ul>
                        <li><a href="#" id="profile-link"><i class="fas fa-user"></i> Quản lý hồ sơ</a></li>
                        <li><a href="#" id="orders-link"><i class="fas fa-box"></i> Đơn hàng</a></li>
                        <li><a href="#" id="promotion-link"><i class="fas fa-gift"></i> Mã ưu đãi</a></li> <!-- Link to Promotions -->
                        <li><a href="#"><i class="fas fa-heart"></i> Yêu thích</a></li>
                    </ul>
                </nav>
            </div>
            <div class="right-menu">
                <div id="profile-section">
                    <%@include file="profile.jsp" %>
                </div>
                <div id="orders-section" class="hidden">
                    <div class="order-container">
                        <div class="order-tabs">
                            <span class="tab active" data-status="Pending">Đang xử lý</span>
                            <span class="tab" data-status="Shipping">Chờ giao hàng</span>
                            <span class="tab" data-status="Delivered">Hoàn thành</span>
                            <span class="tab" data-status="Cancelled">Đã hủy</span>
                        </div>
                        <div id="pagination-container" class="d-flex justify-content-center gap-2 my-4"> 
                            <h3>KLhang</h3></div>
                        <div class="order-content">
                            <img src="./assets/img/notifi-order.png" alt="Chưa có đơn hàng" class="no-order-img">
                            <p>Chưa có đơn hàng</p>
                        </div>
                    </div>
                </div>
                
                <div id="promotion-section" class="hidden">
                    <%@ include file="promotionGetAll.jsp" %>
                </div>
            </div>     
        </div>


        <!-- Modal Đánh giá -->
        <div class="modal fade" id="reviewModal" tabindex="-1" aria-labelledby="reviewModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="reviewModalLabel">Đánh giá sản phẩm</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form action="ReviewManagementServlet" method="POST" enctype="multipart/form-data">
                            <input type="hidden" name="action" value="add">
                            <input type="hidden" name="productId" id="modal-productId">
                            <input type="hidden" name="orderId" id="modal-orderId">
                            <div class="mb-3">
                                <div class="d-flex align-items-center">
                                    <img id="modal-productImg" alt="Product image" class="me-3" height="60" width="60"/>
                                    <div>
                                        <p id="modal-productName" class="fw-bold mb-1"></p>
                                        <p class="text-muted mb-0">Danh mục: <span id="modal-productCategory">Không xác định</span></p>
                                    </div>
                                </div>
                            </div>
                            <div class="mb-3">
                                <label class="fw-bold">Chất lượng sản phẩm</label>
                                <div class="d-flex align-items-center">
                                    <input type="hidden" name="rating" id="rating-input" value="5">
                                    <div class="text-warning me-2" id="star-rating">
                                        <i class="fas fa-star" data-rating="1"></i>
                                        <i class="fas fa-star" data-rating="2"></i>
                                        <i class="fas fa-star" data-rating="3"></i>
                                        <i class="fas fa-star" data-rating="4"></i>
                                        <i class="fas fa-star" data-rating="5"></i>
                                    </div>
                                    <span id="rating-text" class="text-warning">Tuyệt vời</span>
                                </div>
                            </div>
                            <div class="mb-3">
                                <textarea class="form-control" name="description" rows="5" placeholder="Chia sẻ trải nghiệm của bạn về sản phẩm này..."></textarea>
                            </div>
                            <div class="mb-3">
                                <div class="btn btn-outline-danger position-relative">
                                    <i class="fas fa-camera me-1"></i>
                                    <span id="file-name">Thêm hình ảnh</span>
                                    <input type="file" name="image" accept="image/*" class="form-control position-absolute" 
                                           style="opacity: 0; top: 0; left: 0; width: 100%; height: 100%; cursor: pointer;" 
                                           onchange="document.getElementById('file-name').textContent = this.files[0].name || 'Thêm hình ảnh'">
                                </div>
                            </div>
                            <div class="d-flex justify-content-end">
                                <button type="button" class="btn btn-secondary me-2" data-bs-dismiss="modal">Quay lại</button>
                                <button type="submit" class="btn btn-danger">Gửi đánh giá</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <%@include file="assets/Component/footer.jsp" %>
        <script src="assets/js/fowrard.js">
        </script>
    </body>
</html>