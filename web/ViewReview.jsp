<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:forEach items="${reviews}" var="review">
    <div class="review-item mb-4 p-3 border-bottom">
        <div class="d-flex align-items-center mb-2">
            <img src="${review.userAvatar}" 
                 alt="User avatar" 
                 class="rounded-circle me-2" 
                 width="40" 
                 height="40">
            <div>
                <h5 class="mb-0">${review.userName}</h5>
                <div class="rating-stars">
                    <c:forEach begin="1" end="${review.rating}">
                        <i class="fas fa-star text-warning"></i>
                    </c:forEach>
                </div>
            </div>
        </div>
        <div class="review-meta text-muted small mb-2">
            <fmt:formatDate value="${review.reviewDate}" 
                            pattern="dd/MM/yyyy HH:mm" />
        </div>

        <c:if test="${not empty review.comment}">
            <p class="review-comment">${review.comment}</p>
        </c:if>

        <c:if test="${not empty review.images}">
            <div class="review-images">
                <c:forEach items="${review.images}" var="image">
                    <img src="${image}" 
                         alt="Review image" 
                         class="img-thumbnail me-2" 
                         width="100">
                </c:forEach>
            </div>
        </c:if>
    </div>
</c:forEach>

<c:if test="${empty reviews}">
    <p class="text-center text-muted">Không tìm th?y ?ánh giá phù h?p.</p>
</c:if>