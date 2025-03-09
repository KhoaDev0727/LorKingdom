<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div style="width: 1360px !important; margin: 0 auto;">
    <c:choose>
        <c:when test="${not empty listReviews}">
            <c:forEach var="review" items="${listReviews}">
                <c:set var="customer" value="${inforCustomers.stream()
                                               .filter(c -> c.accountId == review.accountID)
                                               .findFirst()
                                               .orElse(null)}"/>

                <div class="border-bottom pb-4 mb-4">
                    <div class="d-flex align-items-center mb-2">
                        <img src="https://placehold.co/40x40" 
                             alt="User avatar" 
                             class="rounded-circle mr-2" 
                             width="40" 
                             height="40">
                        <div>
                            <span class="font-weight-bold">${customer.userName}</span>
                            <div class="d-flex align-items-center">
                                <c:forEach begin="1" end="${review.rating}" var="i">
                                    <i class="fas fa-star text-danger"></i>
                                </c:forEach>
                            </div>
                        </div>
                    </div>

                    <div class="text-muted mb-2">
                        <fmt:formatDate value="${review.reviewAt}" pattern="dd/MM/yyyy HH:mm"/>
                    </div>

                    <c:if test="${not empty review.comment}">
                        <p class="mb-2">${fn:escapeXml(review.comment)}</p>
                    </c:if>

                    <c:if test="${not empty review.imgReview}">
                        <div class="mb-2">
                            <img src="${review.imgReview}" 
                                 alt="Review image" 
                                 class="img-thumbnail" 
                                 style="width: 100px; height: 100px; object-fit: cover;">
                        </div>
                    </c:if>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <p class="text-center text-muted">Không tìm th?y ?ánh giá phù h?p.</p>
        </c:otherwise>
    </c:choose>
</div>