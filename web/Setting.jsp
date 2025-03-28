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
        <!-- Font Awesome (Chỉ giữ phiên bản mới nhất) -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        <!-- Bootstrap CSS (Chỉ giữ phiên bản mới nhất) -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- jQuery (Giữ bản đầy đủ thay vì slim) -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <!-- Bootstrap JS (Bao gồm Popper.js) -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <!-- SweetAlert2 CSS & JS (Chỉ giữ một bộ) -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <style>
            .line-through {
                text-decoration: line-through;
            }

            .text-red-500 {
                color: #e3342f;
            }

            .text-blue-500 {
                color: #3490dc;
            }

            .bg-red-500 {
                background-color: #e3342f;
            }

            .bg-gray-200 {
                background-color: #e2e8f0;
            }

            .text-gray-500 {
                color: #6c757d;
            }

            .text-gray-700 {
                color: #495057;
            }

            #image-preview {
                max-width: 200px;
                margin-top: 10px;
                display: none;
            }

            #image-preview img {
                width: 100%;
                height: auto;
                border-radius: 5px;
            }
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
            */* Custom style for the cancel button */
            .btn-custom-cancel {
                background-color: #8B0000; /* Red-brown color (dark red) */
                color: #FFFFFF; /* White text */
                border: none; /* Remove default border if needed */
            }

            /* Hover effect for the cancel button */
            .btn-custom-cancel:hover {
                background-color: #A52A2A; /* Slightly lighter red-brown for hover */
                color: #FFFFFF;
            }

            .img-fixed-size {
                width: 160px;
                height: 100px;
                object-fit: cover; /* Đảm bảo hình ảnh không bị méo */
            }
            .order-item {
                padding: 20px;
                margin-bottom: 20px;
                border-radius: 8px;
                overflow: hidden;
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
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
                        </div>
                        <div class="order-content">
                        </div>
                    </div>
                </div>
            </div>
        </div> 


        <div class="modal fade" id="orderDetailModal" tabindex="-1" aria-labelledby="orderDetailModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="orderDetailModalLabel">Chi tiết đơn hàng</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body" id="order-detail-content">
                        <!-- Nội dung chi tiết đơn hàng sẽ được hiển thị ở đây -->
                    </div>
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
                    <form id="reviewForm" enctype="multipart/form-data">
                        <div class="modal-body"> 
                            <input type="hidden" id="modal-OrderDetail-Id" name="orderDetailID">
                            <input type="hidden" id="modal-productId">
                            <input type="hidden" id="modal-orderId">
                            <input type="hidden" id="modal-action" value="add">
                            <div class="mb-3">
                                <img id="modal-productImg" src="" alt="Product" class="img-fluid" style="max-height: 100px;">
                                <h6 id="modal-productName"></h6>
                                <small class="text-muted" id="modal-productCategory"></small>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Đánh giá của bạn:</label>
                                <div id="star-rating">
                                    <i class="fa fa-star" data-rating="1"></i>
                                    <i class="fa fa-star" data-rating="2"></i>
                                    <i class="fa fa-star" data-rating="3"></i>
                                    <i class="fa fa-star" data-rating="4"></i>
                                    <i class="fa fa-star" data-rating="5"></i>
                                </div>
                                <span id="rating-text">Tuyệt vời</span>
                                <input type="hidden" id="rating-input" value="5">
                            </div>
                            <div class="mb-3">
                                <label for="review-comment" class="form-label">Nhận xét:</label>
                                <textarea class="form-control" id="review-comment" rows="3" ></textarea>
                            </div>
                            <!-- Thêm trường upload ảnh -->
                            <div class="mb-3">
                                <label for="review-image" class="form-label">Tải lên ảnh đánh giá:</label>
                                <input type="file" class="form-control" id="review-image" accept="image/*">
                                <small class="text-muted">(Tối đa 1 ảnh, định dạng JPG/PNG)</small>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                            <button type="submit" class="btn btn-primary">Gửi đánh giá</button>
                        </div>
                    </form>
                </div>
            </div>
        </div> 

        <%@include file="assets/Component/footer.jsp" %>
        <script src="assets/js/fowrard.js">
        </script>
    </body>
</html>