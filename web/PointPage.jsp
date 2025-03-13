<%-- 
    Document   : PointPage
    Created on : Jan 27, 2025, 9:16:42 PM
    Author     : admin1
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="assets/styleUser/PointPage.css"/>
        <title>JSP Page</title>
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
        <div class="rewards-banner">
            <img src="assets/img/PointPage_img/s4ehpensjhesz6pejvcl.png" alt="Reward" class="reward-image">
            <div class="rewards-content">
                <h1 style="color: #FF6600;">Nhận phần thưởng khi khám phá</h1>
                <p>Tìm hiểu cách được giảm giá cho đơn hàng tiếp theo bằng LorKingDom xu oke</p>
                <button>Xem LorKingDom xu</button>
            </div>
        </div>

        <div class="info-content">
            <h2><span>LorKingDom</span> xu là gì?</h2>
            <p>Tiết kiệm nhiều hơn với <span>LorKingDom</span> xu.</p>
            <p>Nhận <span>LorKingDom</span> xu sau khi trải nghiệm hoạt động. Thêm Klook xu cho bài đánh giá dịch vụ.</p>
            <p>Khi có ít nhất 100 <span>LorKingDom</span> xu, bạn có thể sử dụng xu để tiết kiệm cho đơn hàng tiếp theo.</p>
            <p>100 <span>LorKingDom</span> Xu = 1 USD Giá trị của xứ theo các loại ngoại tệ khác sẽ được quy đổi theo tỷ giá tại thời điểm thanh toán đơn hàng</p>
        </div>

        <!-- New Section: How to Earn Klook xu -->
        <div class="earn-xu-section">
            <h1>Nhận <span>LorKingDom</span>  xu bằng cách nào?</h1>
            <div class="earn-methods">
                <div class="earn-method">
                    <img src="assets/img/PointPage_img/section_3_img1.png" alt="Complete Service">
                    <div class="method-content">
                        <h2>Hoàn thành dịch vụ tại <span>LorKingDom</span> </h2>
                        <p>Bạn sẽ nhận được chiết khấu phần trăm giá trị đơn hàng dưới dạng xu hầu hết các hoạt động sau khi hoàn thành đơn hàng.</p>
                    </div>
                </div>
                <div class="earn-method">
                    <img src="assets/img/PointPage_img/section_3_img2.png" alt="Leave a Review">
                    <div class="method-content">
                        <h2>Để lại đánh giá</h2>
                        <p>Nhận 50 xu khi chia sẻ trải nghiệm của bạn.</p>
                    </div>
                </div>
            </div>
        </div>

        <div class="use-xu-section">
            <h2>Dùng <span>LorKingDom</span> xu như thế nào?</h2>
            <p>Sau khi nhận được 10 <span>LorKingDom</span> xu, bạn có thể sử dụng xu tại trang thanh toán. Khám phá ngay!</p>
        </div>


        <%@include file="assets/Component/footer.jsp" %>

    </body>
</html>
