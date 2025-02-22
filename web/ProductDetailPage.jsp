<%-- 
    Document   : ProductDetailPage
    Created on : Feb 22, 2025, 12:21:41 AM
    Author     : admin1
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <!-- Link Tailwind CSS -->
        <link href="https://cdn.jsdelivr.net/npm/tailwindcss@3.2.7/dist/tailwind.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <script src="https://cdn.tailwindcss.com"></script>
        <title>Chi Tiết Sản Phẩm</title>
    </head>
    <body>
        <section class="p-0">
            <main class="container mx-auto px-4 py-6">

                <c:if test="${not empty product}">
                    <jsp:include page="assets/Component/header.jsp"/>
                    <!-- Phần trên: Thông tin và hình ảnh sản phẩm -->
                    <div class="container mx-auto px-4 py-4 " style="margin-top: 100px;">
                        <h1 class="text-xl font-bold">Chi Tiết Sản Phẩm</h1>
                    </div>
                    <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
                        <!-- Hình ảnh sản phẩm -->
                        <div class="lg:col-span-2">
                            <!-- Ảnh chính -->
                            <div class="aspect-w-1 aspect-h-1 ">
                                <c:forEach var="img" items="${mainImages}">
                                    <c:if test="${img.productID == product.productID}">
                                        <img 
                                            src="${pageContext.request.contextPath}/${img.imageUrl}"
                                            alt="Ảnh sản phẩm chính"
                                            class="w-full  object-cover rounded shadow duration-300" style="height: 480px;"/>
                                    </c:if>
                                </c:forEach>
                            </div>

                            <!-- Bootstrap Carousel container -->
                            <div id="carouselExampleControls" class="carousel slide mt-4" data-ride="carousel">
                                <!-- Bọc tất cả slide trong .carousel-inner -->
                                <div class="carousel-inner">

                                    <c:forEach var="img" items="${listImages}" varStatus="status">
                                        <!-- Mở thẻ slide mới nếu index % 4 == 0 -->
                                        <c:if test="${status.index % 4 == 0}">
                                            <div class="carousel-item
                                                 <c:if test='${status.index == 0}'>active</c:if>">
                                                     <div class="row">
                                                 </c:if>

                                                 <!-- Nội dung 1 ảnh (1 cột) -->
                                                 <div class="col-3">
                                                     <img 
                                                         src="${pageContext.request.contextPath}/${img.imageUrl}"
                                                         class="d-block w-100" 
                                                         alt="Ảnh phụ" />
                                                 </div>

                                                 <!-- Đóng slide khi index % 4 == 3 hoặc là phần tử cuối cùng -->
                                                 <c:if test="${status.index % 4 == 3 || status.last}">
                                                 </div> <!-- đóng .row -->
                                            </div> <!-- đóng .carousel-item -->
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
                            <h1 class="text-2xl font-bold mb-2">
                                ${product.name}
                            </h1>
                            <p class="text-gray-500 text-sm">Mã sản phẩm: ${product.SKU}</p>

                            <!-- Giá sản phẩm -->
                            <div class="flex items-center space-x-2 mt-2">
                                <span class="text-red-500 text-xl font-semibold">
                                    ${product.price}₫
                                </span>
                                <span class="text-gray-400 line-through text-base">899.000₫</span>
                            </div>

                            <!-- Mô tả ngắn gọn -->
                            <ul class=" text-gray-800 " style="padding-left:0;">
                                <!-- Hàng chính hãng -->
                                <li class="flex items-start">
                                    <!-- Icon dấu tick -->
                                    <svg class="w-5 h-5 text-green-500 mt-0.5 flex-shrink-0" 
                                         fill="none" 
                                         stroke="currentColor" 
                                         stroke-width="2" 
                                         viewBox="0 0 24 24" 
                                         xmlns="http://www.w3.org/2000/svg">
                                    <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"></path>
                                    </svg>
                                    <!-- Nội dung -->
                                    <span class="ml-2">Hàng chính hãng</span>
                                </li>

                                <!-- Miễn phí giao hàng toàn quốc đơn trên 500k -->
                                <li class="flex items-start">
                                    <svg class="w-5 h-5 text-green-500 mt-0.5 flex-shrink-0" 
                                         fill="none" 
                                         stroke="currentColor" 
                                         stroke-width="2" 
                                         viewBox="0 0 24 24" 
                                         xmlns="http://www.w3.org/2000/svg">
                                    <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"></path>
                                    </svg>
                                    <span class="ml-2">Miễn phí giao hàng toàn quốc đơn trên 500k</span>
                                </li>

                                <li class="flex items-start">
                                    <svg class="w-5 h-5 text-green-500 mt-0.5 flex-shrink-0" 
                                         fill="none" 
                                         stroke="currentColor" 
                                         stroke-width="2" 
                                         viewBox="0 0 24 24" 
                                         xmlns="http://www.w3.org/2000/svg">
                                    <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"></path>
                                    </svg>
                                    <span class="ml-2">Giao hàng hỏa tốc 4 tiếng</span>
                                </li>
                            </ul>


                            <!-- Chọn số lượng -->
                            <div class="mt-6 flex items-center space-x-3">
                                <label for="quantity" class="font-medium">Số lượng:</label>
                                <div class="flex items-center border rounded">
                                    <button class="px-2 py-1 text-gray-700 hover:bg-gray-100" type="button">-</button>
                                    <input 
                                        id="quantity" 
                                        type="number" 
                                        min="1" 
                                        value="1" 
                                        class="w-16 text-center outline-none border-0"
                                        />
                                    <button class="px-2 py-1 text-gray-700 hover:bg-gray-100" type="button">+</button>
                                </div>
                            </div>
                            <div>
                                <h2 class="text-xl font-bold mb-4 mt-4">Thông tin sản phẩm</h2>
                                <div class="overflow-x-auto">
                                    <table class="w-full text-left border border-gray-200 border-collapse">
                                        <tbody>
                                            <tr class="border-b border-gray-200">
                                                <!-- Cột trái -->
                                                <td class="border-r border-gray-200 px-4 py-2 font-semibold w-1/3">
                                                    Thể Loại
                                                </td>
                                                <!-- Cột phải -->
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
                                                <td class="px-4 py-2">${brand}</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>
            </main>
            <div class="mt-8 bg-white rounded-md shadow p-4">
                <h2 class="text-xl font-bold mb-4">Mô tả sản phẩm</h2>
                <p class="text-gray-700 mb-4">
                    ${product.description}
                </p>
            </div>
            <footer class="bg-white shadow-sm mt-10">
                <div class="container mx-auto px-4 py-4">
                    <p class="text-sm text-gray-500">© 2023 - Cửa Hàng Đồ Chơi</p>
                </div>
            </footer>
        </section>
    </body>

    <!-- Bắt buộc link JS Bootstrap và jQuery, Popper.js -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</html>
