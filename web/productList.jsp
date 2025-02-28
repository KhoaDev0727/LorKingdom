<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<div class="grid grid-cols-3 gap-4">
    <c:forEach var="product" items="${listP}" begin="0" end="8">
        <a href="ProductDetailServlet?productID=${product.productID}"
           class="border border-gray-400 rounded-4 p-4 transform transition duration-200 hover:scale-105 no-underline"
           style="width: 350px; height: 450px; display: block;"> 


            <div class="main-image mb-4 w-full h-50 overflow-hidden group" style="height: 200px;">
                <c:forEach var="img" items="${mainImages}">
                    <c:if test="${img.productID == product.productID}">
                        <img src="${pageContext.request.contextPath}/${img.imageUrl}" alt="Main Image"
                             class="w-full h-full object-cover rounded shadown duration-300" />
                    </c:if>
                </c:forEach>
            </div>


            <div class="mt-4">
                <h3 class="text-gray-600 mb-2">${product.name}</h3>
                <div class="d-flex justify-content-between">
                    <p class="text-gray-400 text-l mb-4">SKU: ${product.SKU}</p>
                    <p class="text-gray-400 mb-2 text-sm">
                        <c:forEach var="cat" items="${categories}">
                            <c:if test="${cat.categoryID == product.categoryID}">
                                ${cat.name}
                            </c:if>
                        </c:forEach>
                    </p>
                </div>
                <p class="text-red-500 font-bold mb-2">${product.price}₫</p>
                <form action="${pageContext.request.contextPath}/CartManagementServlet" method="POST">
                    <input type="hidden" name="productID" value="${product.productID}">
                    <input type="hidden" name="price" value="${product.price}">
                    <input type="hidden" name="quantity" value="1">
                    <button type="submit" class="bg-red-500 text-white py-2 px-4 rounded-full mb-2 mt-3">
                        Thêm Vào Giỏ Hàng 
                    </button>
                </form>
                <button class="text-red-500" onclick="toggleFavorite('${product.SKU}')">
                    <i class="far fa-heart"></i>
                </button>
            </div>
        </a>
    </c:forEach>
</div>
