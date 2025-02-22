<%-- 
    Document   : temp
    Created on : Feb 22, 2025, 10:31:51 AM
    Author     : admin1
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <!-- Link Tailwind CSS -->
        <link href="https://cdn.jsdelivr.net/npm/tailwindcss@3.2.7/dist/tailwind.min.css" rel="stylesheet">
        <script src="https://cdn.tailwindcss.com"></script>
        <title>Chi Tiết Sản Phẩm</title>
    </head>
    <body class="bg-gray-50">

        <!-- Header -->
        <header class="bg-white shadow-sm">
            <div class="container mx-auto px-4 py-4">
                <h1 class="text-xl font-bold">Cửa Hàng Đồ Chơi</h1>
            </div>
        </header>

        <!-- Nội dung trang -->
        <main class="container mx-auto px-4 py-6">
            <!-- Hiển thị thông tin sản phẩm -->
            <c:if test="${not empty product}">
                <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
                    <!-- Hình ảnh sản phẩm -->
                    <div>
                        <div class="aspect-w-1 aspect-h-1">
                            <c:forEach var="img" items="${mainImages}">
                                <c:if test="${img.productID == product.productID}">
                                    <img 
                                        src="${pageContext.request.contextPath}/${img.imageUrl}"
                                        alt="Ảnh sản phẩm chính"
                                        class="w-full h-full object-cover rounded shadow duration-300" />
                                </c:if>
                            </c:forEach>
                        </div>
                        <!-- Ảnh thumbnail -->
                        <div class="grid grid-cols-5 gap-2 mt-4">
                            <img src="https://via.placeholder.com/100x100" alt="Thumbnail 1" 
                                 class="w-full h-auto rounded-md cursor-pointer border border-transparent hover:border-gray-300" />
                            <img src="https://via.placeholder.com/100x100" alt="Thumbnail 2" 
                                 class="w-full h-auto rounded-md cursor-pointer border border-transparent hover:border-gray-300" />
                            <img src="https://via.placeholder.com/100x100" alt="Thumbnail 3" 
                                 class="w-full h-auto rounded-md cursor-pointer border border-transparent hover:border-gray-300" />
                            <img src="https://via.placeholder.com/100x100" alt="Thumbnail 4" 
                                 class="w-full h-auto rounded-md cursor-pointer border border-transparent hover:border-gray-300" />
                            <img src="https://via.placeholder.com/100x100" alt="Thumbnail 5" 
                                 class="w-full h-auto rounded-md cursor-pointer border border-transparent hover:border-gray-300" />
                        </div>
                    </div>

                    <!-- Thông tin sản phẩm -->
                    <div>
                        <h1 class="text-2xl font-bold mb-2">${product.name}</h1>
                        <p class="text-gray-500 text-sm">Mã sản phẩm: ${product.SKU}</p>

                        <!-- Giá sản phẩm -->
                        <div class="flex items-center space-x-2 mt-4">
                            <span class="text-red-500 text-xl font-semibold">
                                ${product.price}₫
                            </span>
                            <!-- Giả sử có giá cũ, nếu có thể cập nhật thuộc tính khác -->
                            <!-- <span class="text-gray-400 line-through text-base">Giá cũ</span> -->
                        </div>

                        <!-- Mô tả ngắn gọn -->
                        <div class="mt-4">
                            <p class="text-sm text-gray-600">
                                ${product.description}
                            </p>
                        </div>

                        <!-- Chọn số lượng -->
                        <div class="mt-6 flex items-center space-x-3">
                            <label for="quantity" class="font-medium">Số lượng:</label>
                            <div class="flex items-center border rounded">
                                <button class="px-2 py-1 text-gray-700 hover:bg-gray-100" type="button">-</button>
                                <input id="quantity" type="number" min="1" value="1" 
                                       class="w-16 text-center outline-none border-0" />
                                <button class="px-2 py-1 text-gray-700 hover:bg-gray-100" type="button">+</button>
                            </div>
                        </div>

                        <!-- Nút thêm vào giỏ hàng -->
                        <div class="mt-6">
                            <button class="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded font-semibold">
                                Thêm vào giỏ hàng
                            </button>
                        </div>

                        <!-- Hình thức vận chuyển -->
                        <div class="mt-6">
                            <h2 class="text-lg font-semibold mb-2">Hình thức vận chuyển</h2>
                            <div class="space-y-2">
                                <label class="flex items-center space-x-2">
                                    <input type="radio" name="shipping" class="form-radio h-4 w-4 text-red-500" checked />
                                    <span>Giao hàng tận nơi</span>
                                </label>
                                <label class="flex items-center space-x-2">
                                    <input type="radio" name="shipping" class="form-radio h-4 w-4 text-red-500" />
                                    <span>Nhận tại cửa hàng</span>
                                </label>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Mô tả sản phẩm chi tiết -->
                <div class="mt-8 bg-white rounded-md shadow p-4">
                    <h2 class="text-xl font-bold mb-4">Mô tả sản phẩm</h2>
                    <p class="text-gray-700 mb-4">
                        Chi tiết về sản phẩm: ${product.description}
                    </p>
                    <ul class="list-disc list-inside text-gray-700">
                        <li>Thông số kỹ thuật, nếu có</li>
                        <li>Độ tuổi khuyến nghị</li>
                        <li>Thương hiệu</li>
                        <li>Xuất xứ</li>
                    </ul>
                </div>
            </c:if>

            <!-- Nếu không có thông tin sản phẩm -->
            <c:if test="${empty product}">
                <p class="text-center text-red-500">Không tìm thấy thông tin sản phẩm.</p>
            </c:if>
        </main>

        <!-- Footer -->
        <footer class="bg-white shadow-sm mt-10">
            <div class="container mx-auto px-4 py-4">
                <p class="text-sm text-gray-500">© 2023 - Cửa Hàng Đồ Chơi</p>
            </div>
        </footer>
    </body>
</html>
