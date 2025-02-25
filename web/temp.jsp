<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="./assets/styleUser/styleHome.css">
        <link rel="stylesheet" href="./assets/styleUser/styleheader.css">
        <link rel="stylesheet" href="assets/styleUser/newProductPage.css"/>


        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css"
              rel="stylesheet">
        <!-- Bootstrap JS Bundle -->

        <script src="https://cdn.tailwindcss.com"></script>

        <link rel="stylesheet" href="assets/styleUser/newProductPage.css"/>
        <script src="./assets/js/script.js"></script>
        <title>LorKingdom</title>

    </head>

    <body class="bg-white font-sans text-l" style="margin-top: 120px;">
        <!-- Header Section -->
        <jsp:include page="assets/Component/header.jsp"/>
        <!-- end Header Section -->


        <section class="banner-section">
            <div id="horizontalBannerCarousel" class="carousel slide" data-bs-ride="carousel">
                <!-- Indicators/Dots -->
                <div class="carousel-indicators">
                    <button type="button" data-bs-target="#horizontalBannerCarousel" data-bs-slide-to="0" class="active"
                            aria-current="true" aria-label="Slide 1"></button>
                    <button type="button" data-bs-target="#horizontalBannerCarousel" data-bs-slide-to="1"
                            aria-label="Slide 2"></button>
                    <button type="button" data-bs-target="#horizontalBannerCarousel" data-bs-slide-to="2"
                            aria-label="Slide 3"></button>
                </div>

                <!-- Slides -->
                <div class="carousel-inner">
                    <div class="carousel-item active">
                        <img src="./assets/img/banner1.png" class="d-block w-100" alt="Banner 1">
                    </div>
                    <div class="carousel-item">
                        <img src="./assets/img/banner2.png" class="d-block w-100" alt="Banner 2">
                    </div>
                    <div class="carousel-item">
                        <img src="./assets/img/banner3.png" class="d-block w-100" alt="Banner 3">
                    </div>
                </div>

                <!-- Controls/Arrows -->
                <button class="carousel-control-prev" type="button" data-bs-target="#horizontalBannerCarousel"
                        data-bs-slide="prev">
                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                    <span class="visually-hidden">Previous</span>
                </button>
                <button class="carousel-control-next" type="button" data-bs-target="#horizontalBannerCarousel"
                        data-bs-slide="next">
                    <span class="carousel-control-next-icon" aria-hidden="true"></span>
                    <span class="visually-hidden">Next</span>
                </button>
            </div>
        </section>


        <div class="bg-white" style="margin-top: 30px;">
            <div class="flex flex-col lg:flex-row">
                <!-- Sidebar -->
                <div class="w-full lg:w-1/4 pl-12 ">
                    <form action="getList" method="get" id="filterForm">
                        <!-- Category -->
                        <div class="mb-4" style="border-bottom:1px solid #ccc; margin-bottom: 5px; padding-bottom: 10px; margin-top: 70px;">
                            <h3 class="font-semibold mb-2 text-orange-500">Category</h3>
                            <div class="w-full overflow-y-auto max-h-[400px]">
                                <c:forEach var="superCat" items="${superCategories}" varStatus="status">
                                    <button type="button" onclick="toggleMenu('menu${status.index}')" class="block w-full text-left px-4 py-2 focus:outline-none">
                                        ${superCat.name}
                                    </button>
                                    <div id="menu${status.index}" class="hidden overflow-auto max-h-60 transition-all ease-out duration-300">
                                        <ul class="pl-4">
                                            <c:forEach var="cat" items="${categories}">
                                                <c:if test="${cat.superCategoryID == superCat.superCategoryID}">
                                                    <li>
                                                        <label class="block px-4 py-2" style="border-bottom:1px solid #ccc; margin-bottom: 5px;">
                                                            <input type="radio" name="categoryID" value="${cat.categoryID}" ${param.categoryID == cat.categoryID ? 'checked' : ''} onchange="submitFilter()">
                                                            ${cat.name}
                                                        </label>
                                                    </li>
                                                </c:if>
                                            </c:forEach>
                                        </ul>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>

                        <!-- Price Range -->
                        <div class="mb-4">
                            <h3 class="font-semibold mb-2 text-orange-500">Price Range</h3>
                            <div class="w-full overflow-y-auto max-h-[300px]">
                                <ul class="pl-4">
                                    <c:forEach var="price" items="${listPriceRanges}">
                                        <li style="border-bottom:1px solid #ccc; margin-bottom: 5px;">
                                            <label class="block px-4 py-2">
                                                <input type="radio" name="priceRangeID" value="${price.priceRangeID}" ${param.priceRangeID == price.priceRangeID ? 'checked' : ''} onchange="submitFilter()">
                                                ${price.priceRange}
                                            </label>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>

                        <!-- Age -->
                        <div class="mb-4" style="border-bottom:1px solid #ccc; margin-bottom: 5px;padding-bottom: 10px;">
                            <h3 class="font-semibold mb-2 text-orange-500">Age</h3>
                            <div class="w-full overflow-y-auto max-h-[300px]">
                                <ul class="pl-4">
                                    <c:forEach var="age" items="${ages}">
                                        <li style="border-bottom:1px solid #ccc; margin-bottom: 5px;">
                                            <label class="block px-4 py-2">
                                                <input type="radio" name="ageID" value="${age.ageID}" ${param.ageID == age.ageID ? 'checked' : ''} onchange="submitFilter()">
                                                ${age.ageRange}
                                            </label>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>

                        <!-- Sex -->
                        <div class="mb-4">
                            <h3 class="font-semibold mb-2 text-orange-500">Sex</h3>
                            <div class="w-full overflow-y-auto max-h-[300px]">
                                <ul class="pl-4">
                                    <c:forEach var="sex" items="${listS}">
                                        <li style="border-bottom:1px solid #ccc; margin-bottom: 5px;">
                                            <label class="block px-4 py-2">
                                                <input type="radio" name="sexID" value="${sex.sexID}" ${param.sexID == sex.sexID ? 'checked' : ''} onchange="submitFilter()">
                                                ${sex.name}
                                            </label>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>

                        <!-- Brand -->
                        <div class="mb-4">
                            <h3 class="font-semibold mb-2 text-orange-500">Brand</h3>
                            <div class="w-full overflow-y-auto max-h-[300px]">
                                <ul class="pl-4">
                                    <c:forEach var="brand" items="${listB}">
                                        <li style="border-bottom:1px solid #ccc; margin-bottom: 5px;">
                                            <label class="block px-4 py-2">
                                                <input type="radio" name="brandID" value="${brand.brandID}" ${param.brandID == brand.brandID ? 'checked' : ''} onchange="submitFilter()">
                                                ${brand.name}
                                            </label>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>

                        <!-- Material -->
                        <div class="mb-4">
                            <h3 class="font-semibold mb-2 text-orange-500">Material</h3>
                            <div class="w-full overflow-y-auto max-h-[300px]">
                                <ul class="pl-4">
                                    <c:forEach var="material" items="${listM}">
                                        <li style="border-bottom:1px solid #ccc; margin-bottom: 5px;">
                                            <label class="block px-4 py-2">
                                                <input type="radio" name="materialID" value="${material.materialID}" ${param.materialID == material.materialID ? 'checked' : ''} onchange="submitFilter()">
                                                ${material.name}
                                            </label>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>

                        <!-- Nút Reset Filter -->
                        <button type="button" class="bg-gray-500 text-white px-4 py-2 rounded mt-4" onclick="resetFilter()">Reset</button>
                    </form>
                </div>



                <!-- Main Content -->
                <div class="w-full lg:w-3/4 p-4">
                    <div class="flex justify-between items-center mb-4">
                        <div class="flex items-center">
                            <span class="mr-2">
                                Kết quả:
                            </span>
                            <span class="font-bold">
                                ${totalProducts} products
                            </span>
                        </div>
                        <div class="flex items-center">
                            <span class="mr-2">
                                Sắp xếp theo:
                            </span>
                            <select class="border border-gray-300 rounded p-2">
                                <option value="mới nhất">
                                    Mới nhất
                                </option>
                                <option value="giá thấp đến cao">
                                    Giá thấp đến cao
                                </option>
                                <option value="giá cao đến thấp">
                                    Giá cao đến thấp
                                </option>
                            </select>
                        </div>
                    </div>
                    <div style="margin-bottom: 50px;" >

                        <div class="grid grid-cols-3 gap-4 " id="productListContainer">
                            <c:forEach var="product" items="${listP}" begin="0" end="8">

                                <a href="ProductDetailServlet?productID=${product.productID}"
                                   class="border border-gray-400 rounded-4 p-4 transform transition duration-200 hover:scale-105 no-underline"
                                   style="width: 350px; height: 450px; display: block;"> 

                                    <div class="main-image mb-4 w-full h-50 overflow-hidden group"
                                         style="height: 200px;">
                                        <c:forEach var="img" items="${mainImages}">
                                            <c:if test="${img.productID == product.productID}">
                                                <img 
                                                    src="${pageContext.request.contextPath}/${img.imageUrl}"
                                                    alt="Main Image"
                                                    class="w-full h-full object-cover rounded shadown duration-300" />
                                            </c:if>
                                        </c:forEach>
                                    </div>

                                    <!-- Thông tin sản phẩm -->
                                    <div class="mt-4">
                                        <h3 class="text-gray-600 mb-2">
                                            ${product.name}
                                        </h3>
                                        <div class="d-flex justify-content-between">
                                            <p class="text-gray-400 text-l mb-4">
                                                SKU: ${product.SKU}
                                            </p>
                                            <p class="text-gray-400 mb-2 text-sm">
                                                <!-- Lấy Category Name -->
                                                <c:forEach var="cat" items="${categories}">
                                                    <c:if test="${cat.categoryID == product.categoryID}">
                                                        ${cat.name}
                                                    </c:if>
                                                </c:forEach>
                                            </p>
                                        </div>
                                        <p class="text-red-500 font-bold mb-2">
                                            ${product.price}₫
                                        </p>

                                        <!-- Nút Thêm Vào Giỏ Hàng -->
                                        <button class="bg-red-500 text-white py-2 px-4 rounded-full mb-2 mt-3"
                                                onclick="event.preventDefault(); location.href = 'AddToCartServlet?sku=${product.SKU}'">
                                            Thêm Vào Giỏ Hàng
                                        </button>
                                        <button class="text-red-500" onclick="toggleFavorite('${product.SKU}')">
                                            <i class="far fa-heart"></i>
                                        </button>
                                    </div>
                                </a>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        </div>


        <script>
            document.querySelector('.dropdown-btn').addEventListener('click', function () {
                const menu = document.querySelector('.dropdown-menu');
                if (menu.style.display === 'block') {
                    menu.style.display = 'none';
                } else {
                    menu.style.display = 'block';
                }
            });
            function toggleMenu(menuId) {
                var menu = document.getElementById(menuId);
                if (menu.classList.contains('hidden')) {
                    menu.classList.remove('hidden');
                } else {
                    menu.classList.add('hidden');
                }
            }
            function submitFilter() {
                // Lấy dữ liệu của form
                const form = document.getElementById("filterForm");
                const formData = new FormData(form);
                // Chuyển đổi dữ liệu sang URL parameters
                const params = new URLSearchParams(formData).toString();

                fetch("productList.jsp?" + params, {
                    method: "GET"
                })
                        .then(response => response.text())
                        .then(data => {
                            // Cập nhật lại phần danh sách sản phẩm
                            document.getElementById("productListContainer").innerHTML = data;

                        })
                        .catch(error => console.error("Lỗi khi load dữ liệu:", error));
            }

        </script>

    </body>
