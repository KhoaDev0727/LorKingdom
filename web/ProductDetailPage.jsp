<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <!-- Link Tailwind CSS -->
        <link href="https://cdn.jsdelivr.net/npm/tailwindcss@3.2.7/dist/tailwind.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <script src="https://cdn.tailwindcss.com"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <!-- Thêm jQuery -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <title>Chi Tiết Sản Phẩm</title>
    </head> 
    <style>
    html, body {
    scroll-behavior: auto !important;
}
</style>
    <body>
        <section class="p-0 ">
            <main class=" mx-auto px-4 py-6"  style="width: 1400px; margin: 0 auto;">
                <c:if test="${not empty product}">
                    <jsp:include page="assets/Component/header.jsp"/>
                    <!-- Phần trên: Thông tin và hình ảnh sản phẩm -->
                    <div class="container mx-auto px-4 py-4" style="margin-top: 100px;">
                        <h1 class="text-xl font-bold">Chi Tiết Sản Phẩm</h1>
                    </div>
                    <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
                        <!-- Hình ảnh sản phẩm -->
                        <div class="lg:col-span-2">
                            <!-- Ảnh chính -->
                            <div class="aspect-w-1 aspect-h-1">
                                <c:forEach var="img" items="${mainImages}">
                                    <c:if test="${img.productID == product.productID}">
                                        <img 
                                            src="${pageContext.request.contextPath}/${img.imageUrl}"
                                            alt="Ảnh sản phẩm chính"
                                            class="w-full object-cover rounded shadow duration-300" style="height: 600px;"/>
                                    </c:if>
                                </c:forEach>
                            </div>

                            <!-- Bootstrap Carousel container -->
                            <div id="carouselExampleControls" class="carousel slide mt-4" data-ride="carousel">
                                <div class="carousel-inner">
                                    <c:forEach var="img" items="${listImages}" varStatus="status">
                                        <c:if test="${status.index % 4 == 0}">
                                            <div class="carousel-item <c:if test='${status.index == 0}'>active</c:if>">
                                                    <div class="row">
                                                </c:if>
                                                <div class="col-3" style="width: 210px; height: 150px; overflow: hidden;">
                                                    <img 
                                                        src="${pageContext.request.contextPath}/${img.imageUrl}"
                                                        class="object-cover w-full h-full"
                                                        alt="Ảnh phụ" />
                                                </div>
                                                <c:if test="${status.index % 4 == 3 || status.last}">
                                                </div>
                                            </div>
                                        </c:if>
                                    </c:forEach>
                                </div>
                                <a class="carousel-control-prev" href="#carouselExampleControls" role="button" data-slide="prev">
                                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                                    <span class="sr-only">Previous</span>
                                </a>
                                <a class="carousel-control-next" href="#carouselExampleControls" role="button" data-slide="next">
                                    <span class="carousel-control-next-icon" aria-hidden="true"></span>
                                    <span class="sr-only">Next</span>
                                </a>
                            </div>
                        </div>

                        <!-- Thông tin sản phẩm -->
                        <div>
                            <h1 class="text-2xl font-bold mb-2">${product.name}</h1>
                            <p class="text-gray-500 text-sm">Mã sản phẩm: ${product.SKU}</p>
                            <div class="flex items-center space-x-2 mt-2 text-red-600 font-bold text-xl">
                                <fmt:formatNumber value="${product.price}" pattern="#,###" /> VND
                            </div>

                            <ul class="text-gray-800" style="padding-left:0;">
                                <li class="flex items-start">
                                    <svg class="w-5 h-5 text-green-500 mt-0.5 flex-shrink-0" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                                    <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"></path>
                                    </svg>
                                    <span class="ml-2">Hàng chính hãng</span>
                                </li>
                                <li class="flex items-start">
                                    <svg class="w-5 h-5 text-green-500 mt-0.5 flex-shrink-0" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                                    <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"></path>
                                    </svg>
                                    <span class="ml-2">Miễn phí giao hàng toàn quốc đơn trên 500k</span>
                                </li>
                                <li class="flex items-start">
                                    <svg class="w-5 h-5 text-green-500 mt-0.5 flex-shrink-0" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                                    <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"></path>
                                    </svg>
                                    <span class="ml-2">Giao hàng hỏa tốc 4 tiếng</span>
                                </li>
                            </ul>
                            <div class="mt-6">
                                <form action="${pageContext.request.contextPath}/CartManagementServlet" 
                                      method="POST"
                                      class="flex items-center space-x-3">

                                    <!-- Trường ẩn: productID và price -->
                                    <input type="hidden" name="productID" value="${product.productID}">
                                    <input type="hidden" name="price" value="${product.price}">

                                    <!-- Nhãn số lượng -->
                                    <label for="quantity" class="font-medium">Số lượng:</label>

                                    <!-- Khối ô nhập số lượng + nút +/- -->
                                    <div class="flex items-center border rounded">
                                        <button class="px-2 py-1 text-gray-700 hover:bg-gray-100" 
                                                type="button" 
                                                onclick="decrementQuantity()">
                                            -
                                        </button>
                                        <input id="quantity" 
                                               name="quantity" 
                                               type="number" 
                                               class="w-16 text-center outline-none border-0" 
                                               value="1" 
                                               min="1"/>
                                        <button class="px-2 py-1 text-gray-700 hover:bg-gray-100" 
                                                type="button" 
                                                onclick="incrementQuantity()">
                                            +
                                        </button>
                                    </div>

                                    <!-- Nút Thêm Vào Giỏ Hàng -->
                                    <button type="submit" class="bg-red-500 text-white px-4 py-2 rounded-md">
                                        Thêm Vào Giỏ Hàng
                                    </button>
                                </form>
                            </div>
                            <div>
                                <h2 class="text-xl font-bold mb-4 mt-4">Thông tin sản phẩm</h2>
                                <div class="overflow-x-auto">
                                    <table class="w-full text-left border border-gray-200 border-collapse">
                                        <tbody>
                                            <tr class="border-b border-gray-200">
                                                <td class="border-r border-gray-200 px-4 py-2 font-semibold w-1/3">Thể Loại</td>
                                                <td class="px-4 py-2">${category}</td>
                                            </tr>
                                            <tr class="border-b border-gray-200">
                                                <td class="border-r border-gray-200 px-4 py-2 font-semibold">Xuất xứ</td>
                                                <td class="px-4 py-2">${origin}</td>
                                            </tr>
                                            <tr class="border-b border-gray-200">
                                                <td class="border-r border-gray-200 px-4 py-2 font-semibold">Chất liệu</td>
                                                <td class="px-4 py-2">${materail}</td>
                                            </tr>
                                            <tr class="border-b border-gray-200">
                                                <td class="border-r border-gray-200 px-4 py-2 font-semibold">Tuổi</td>
                                                <td class="px-4 py-2">${age}</td>
                                            </tr>
                                            <tr class="border-b border-gray-200">
                                                <td class="border-r border-gray-200 px-4 py-2 font-semibold">Thương hiệu</td>
                                                <td class="px-4 py-2">${brand}</td>
                                            </tr>
                                            <tr>
                                                <td class="border-r border-gray-200 px-4 py-2 font-semibold">Giới tính</td>
                                                <td class="px-4 py-2">${sex}</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>
            </main>
            <div class="mt-8 mb-5 bg-white rounded-md shadow p-4" style="width: 1360px; margin: 0 auto;">
                <h2 class="text-xl font-bold mb-4">Mô tả sản phẩm</h2>
                <p class="text-gray-700 mb-4">${product.description}</p>
            </div>

            <jsp:include page="reviews.jsp" />
            <div class="grid grid-cols-3 gap-4 mb-8">
                <c:forEach var="product" items="${listP}">
                    <a href="ProductDetailServlet?productID=${product.productID}"
                       class="border border-gray-400 rounded-4 p-4 transform transition duration-200 hover:scale-105 no-underline"
                       style="width: 350px; height: 450px; display: block;">

                        <div class="main-image mb-4 w-full h-50 overflow-hidden group" style="height: 200px;">
                            <c:forEach var="img" items="${mainImages}">
                                <c:if test="${img.productID == product.productID}">
                                    <img src="${pageContext.request.contextPath}/${img.imageUrl}" alt="Main Image"
                                         class="w-full h-full object-cover rounded shadow duration-300" />
                                </c:if>
                            </c:forEach>
                        </div>

                        <div class="mt-4">
                            <h4 class="text-gray-600 mb-2">${product.name}</h4>
                            <div class="d-flex justify-content-between">
                                <p class="text-gray-400 text-l mb-3">SKU: ${product.SKU}</p>
                                <p class="text-gray-400 mb-2 text-sm">
                                    <c:forEach var="cat" items="${categories}">
                                        <c:if test="${cat.categoryID == product.categoryID}">
                                            ${cat.name}
                                        </c:if>
                                    </c:forEach>
                                </p>
                            </div>
                            <p class="text-orange-500 text-xl">
                                <fmt:formatNumber value="${product.price}" pattern="#,###" />
                            </p>
                        </div>
                    </a>
                </c:forEach>
            </div>

            <div class="mb-8" style="width: 1360px; margin:0 auto;">
                <h2 class="text-xl font-bold mb-4">Sản phẩm liên quan</h2>
                <!-- Sử dụng grid 4 cột, gap 4 -->
                <div class="grid grid-cols-4 gap-4">
                    <c:forEach var="rp" items="${relatedProducts}">
                        <a href="ProductDetailServlet?productID=${rp.productID}"
                           class="border border-gray-400 rounded p-4 no-underline transform transition duration-200 hover:scale-105"
                           style="display: flex; flex-direction: column;">

                            <!-- Thử hiển thị ảnh chính nếu có -->
                            <div class="mb-2">
                                <c:set var="foundImage" value="false" />
                                <c:forEach var="img" items="${mainImages}">
                                    <c:if test="${img.productID == rp.productID}">
                                        <img src="${pageContext.request.contextPath}/${img.imageUrl}"
                                             alt="Ảnh liên quan"
                                             class="w-full h-48 object-cover" />
                                        <c:set var="foundImage" value="true" />
                                    </c:if>
                                </c:forEach>

                                <!-- Nếu không tìm thấy ảnh chính, hiển thị noimage.png -->
                                <c:if test="${not foundImage}">
                                    <img src="${pageContext.request.contextPath}/assets/img/noimage.png"
                                         alt="Ảnh liên quan"
                                         class="w-full h-48 object-cover" />
                                </c:if>
                            </div>

                            <h4 class="text-gray-600 mb-1 flex-grow">${rp.name}</h4>
                            <p class="text-red-500 font-bold">
                                <fmt:formatNumber value="${rp.price}" pattern="#,###" /> VND
                            </p>
                        </a>
                    </c:forEach>
                </div>
            </div>


            <footer class="bg-white shadow-sm mt-10">
                <div class="mx-auto px-4 py-4" style="width: 1380px; margin: 50 auto;">
                    <p class="text-sm text-gray-500">© 2023 - Cửa Hàng Đồ Chơi</p>
                </div>
            </footer>


        </section>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

        
        <script>
                                                    function incrementQuantity() {
                                                        const quantityInput = document.getElementById('quantity');
                                                        let currentValue = parseInt(quantityInput.value) || 1;
                                                        quantityInput.value = currentValue + 1;
                                                    }

                                                    function decrementQuantity() {
                                                        const quantityInput = document.getElementById('quantity');
                                                        let currentValue = parseInt(quantityInput.value) || 1;
                                                        if (currentValue > 1) {
                                                            quantityInput.value = currentValue - 1;
                                                        }
                                                    }

                                                    // Khi carousel tạo xong, ta clone ảnh tiếp theo vào slide hiện tại
                                                    $('#multiItemCarousel .carousel-item').each(function () {
                                                        // nextItem: trỏ tới slide kế
                                                        var nextItem = $(this).next();
                                                        if (!nextItem.length) {
                                                            // nếu hết slide thì quay vòng lại slide đầu
                                                            nextItem = $(this).siblings(':first');
                                                        }
                                                        // Clone phần tử con đầu tiên của slide kế (tức 1 ảnh) rồi append vào slide hiện tại
                                                        nextItem.children(':first-child').clone().appendTo($(this));

                                                        // Muốn hiển thị 4 ảnh => ta clone tiếp 2 ảnh nữa
                                                        // (vì slide gốc đã có 1 ảnh + clone 1 ảnh => mới có 2, cần 2 ảnh nữa)
                                                        for (var i = 0; i < 2; i++) {
                                                            nextItem = nextItem.next();
                                                            if (!nextItem.length) {
                                                                nextItem = $(this).siblings(':first');
                                                            }
                                                            nextItem.children(':first-child').clone().appendTo($(this));
                                                        }
                                                    });
        </script>

    </body>
</html>