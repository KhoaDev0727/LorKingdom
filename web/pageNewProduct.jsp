<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
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
                    <h1 class="text-xl font-bold mb-4 text-orange-500 text-3xl" style="font-size: 35px;" >
                        Danh Mục
                    </h1>
                    <div class="mb-4 pl-12"  style="border-bottom:1px solid #ccc; margin-bottom: 5px; padding-bottom: 10px;">
                        <h3 class="font-semibold mb-2">
                            Sản phẩm
                        </h3>
                        <div class="w-full overflow-y-auto max-h-[400px]">
                            <c:forEach var="superCat" items="${superCategories}" varStatus="status">
                                <!-- SuperCategory Heading -->
                                <button onclick="toggleMenu('menu${status.index}')" class="block w-full text-left px-4 py-2 focus:outline-none">
                                    ${superCat.name}
                                </button>
                                <!-- Categories Dropdown as Radio Buttons -->
                                <div id="menu${status.index}" class="hidden overflow-auto max-h-60 transition-all ease-out duration-300">
                                    <form action="CategoryServlet" method="get">
                                        <ul class="pl-4">
                                            <c:forEach var="cat" items="${categories}">
                                                <c:if test="${cat.superCategoryID == superCat.superCategoryID}">
                                                    <li>
                                                        <label class="block px-4 py-2" style="border-bottom:1px solid #ccc; margin-bottom: 5px;">
                                                            <input type="radio" name="categoryID" value="${cat.categoryID}" onchange="this.form.submit()">
                                                            ${cat.name}
                                                        </label>
                                                    </li>
                                                </c:if>
                                            </c:forEach>
                                        </ul>
                                    </form>
                                </div>
                            </c:forEach>
                        </div>


                    </div>
                    <div class="mb-4" >
                        <h3 class="font-semibold mb-2">
                            Giá
                        </h3>
                        <ul>
                            <li>
                                <input class="mr-2" id="price1" type="checkbox" />
                                <label for="price1">
                                    Dưới 200.000Đ
                                </label>
                            </li>
                            <li>
                                <input class="mr-2" id="price2" type="checkbox" />
                                <label for="price2">
                                    200.000Đ - 500.000Đ
                                </label>
                            </li>
                            <li>
                                <input class="mr-2" id="price3" type="checkbox" />
                                <label for="price3">
                                    500.000Đ - 1.000.000Đ
                                </label>
                            </li>
                            <li>
                                <input class="mr-2" id="price4" type="checkbox" />
                                <label for="price4">
                                    1.000.000Đ - 2.000.000Đ
                                </label>
                            </li>
                            <li>
                                <input class="mr-2" id="price5" type="checkbox" />
                                <label for="price5">
                                    2.000.000Đ - 4.000.000Đ
                                </label>
                            </li>
                            <li>
                                <input class="mr-2" id="price6" type="checkbox" />
                                <label for="price6">
                                    Trên 4.000.000Đ
                                </label>
                            </li>
                        </ul>
                    </div>
                    <div class="mb-4 pl-8"  style="border-bottom:1px solid #ccc; margin-bottom: 5px;padding-bottom: 10px;">
                        <h3 class="font-semibold mb-2">
                            Tuổi
                        </h3>
                        <div class="w-full overflow-y-auto max-h-[300px]">
                            <form action="AgeSelectionServlet" method="get"> <!-- Update the action to your servlet that handles age selection -->
                                <ul class="pl-4">
                                    <c:forEach var="age" items="${ages}">
                                        <li  style="border-bottom:1px solid #ccc; margin-bottom: 5px;">
                                            <label class="block px-4 py-2" >
                                                <input type="radio" name="ageID" value="${age.ageID}" onchange="this.form.submit()">
                                                ${age.ageRange}
                                            </label>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </form>
                        </div>
                    </div>
                    <div class="mb-4">
                        <h3 class="font-semibold mb-2">
                            Giới Tính
                        </h3>
                        <ul>
                            <li>
                                <input class="mr-2" id="gender1" type="checkbox" />
                                <label for="gender1">
                                    Bé trai
                                </label>
                            </li>
                            <li>
                                <input class="mr-2" id="gender2" type="checkbox" />
                                <label for="gender2">
                                    Bé gái
                                </label>
                            </li>
                            <li>
                                <input class="mr-2" id="gender3" type="checkbox" />
                                <label for="gender3">
                                    Unisex
                                </label>
                            </li>
                        </ul>
                    </div>
                    <div class="mb-4">
                        <h3 class="font-semibold mb-2">
                            Thương Hiệu
                        </h3>
                        <ul>
                            <li>
                                <input class="mr-2" id="brand1" type="checkbox" />
                                <label for="brand1">
                                    3C4G
                                </label>
                            </li>
                            <li>
                                <input class="mr-2" id="brand2" type="checkbox" />
                                <label for="brand2">
                                    4CATS
                                </label>
                            </li>
                            <li>
                                <input class="mr-2" id="brand3" type="checkbox" />
                                <label for="brand3">
                                    5 Stars
                                </label>
                            </li>
                            <li>
                                <input class="mr-2" id="brand4" type="checkbox" />
                                <label for="brand4">
                                    ANGRY BIRDS
                                </label>
                            </li>
                            <li>
                                <input class="mr-2" id="brand5" type="checkbox" />
                                <label for="brand5">
                                    AVENGERS
                                </label>
                            </li>
                            <li>
                                <input class="mr-2" id="brand6" type="checkbox" />
                                <label for="brand6">
                                    AWESOME BLOSSOM
                                </label>
                            </li>
                            <li>
                                <input class="mr-2" id="brand7" type="checkbox" />
                                <label for="brand7">
                                    BANDAI
                                </label>
                            </li>
                            <li>
                                <input class="mr-2" id="brand8" type="checkbox" />
                                <label for="brand8">
                                    BANDAI CANDY
                                </label>
                            </li>
                        </ul>
                    </div>
                </div>
                <!-- Main Content -->
                <div class="w-full lg:w-3/4 p-4">
                    <div class="flex justify-between items-center mb-4">
                        <div class="flex items-center">
                            <span class="mr-2">
                                Kết quả:
                            </span>
                            <span class="font-bold">
                                6362 products
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
                    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
                        <!-- Product Card -->
                        <div class="border border-gray-300 rounded p-4">
                            <img alt="Mô Hình Doraemon Concert Series 2" class="w-full h-48 object-cover mb-4" height="200"
                                 src="https://storage.googleapis.com/a1aa/image/DtqhZTRwLIOP0PRSF1FIyuU5nyDsbti2fBaJKODEZA8.jpg"
                                 width="200" />
                            <div class="text-center">
                                <p class="text-red-500 font-bold mb-2">
                                    300.000Đ
                                </p>
                                <p class="text-gray-600 mb-2">
                                    Mô Hình Doraemon Concert Series 2
                                </p>
                                <p class="text-gray-400 text-sm mb-4">
                                    SKU: 4897056203456
                                </p>
                                <button class="bg-red-500 text-white py-2 px-4 rounded-full mb-2">
                                    Thêm Vào Giỏ Hàng
                                </button>
                                <button class="text-red-500">
                                    <i class="far fa-heart">
                                    </i>
                                </button>
                            </div>
                        </div>
                        <!-- Repeat Product Cards as needed -->
                        <div class="border border-gray-300 rounded p-4">
                            <img alt="Mô Hình Disney Donald Duck Club" class="w-full h-48 object-cover mb-4" height="200"
                                 src="https://storage.googleapis.com/a1aa/image/AHDnwJreaqe9DvbD5SjD_OfK5nttJY5co5d3XaqeICw.jpg"
                                 width="200" />
                            <div class="text-center">
                                <p class="text-red-500 font-bold mb-2">
                                    300.000Đ
                                </p>
                                <p class="text-gray-600 mb-2">
                                    Mô Hình Disney Donald Duck Club
                                </p>
                                <p class="text-gray-400 text-sm mb-4">
                                    SKU: 4897056203457
                                </p>
                                <button class="bg-red-500 text-white py-2 px-4 rounded-full mb-2">
                                    Thêm Vào Giỏ Hàng
                                </button>
                                <button class="text-red-500">
                                    <i class="far fa-heart">
                                    </i>
                                </button>
                            </div>
                        </div>
                        <div class="border border-gray-300 rounded p-4">
                            <img alt="Mô Hình The Aristocats Marie" class="w-full h-48 object-cover mb-4" height="200"
                                 src="https://storage.googleapis.com/a1aa/image/4Tbahiywowjq2oOg0xEyvEUPJJvsKti0AlMchVm0KSU.jpg"
                                 width="200" />
                            <div class="text-center">
                                <p class="text-red-500 font-bold mb-2">
                                    350.000Đ
                                </p>
                                <p class="text-gray-600 mb-2">
                                    Mô Hình The Aristocats Marie
                                </p>
                                <p class="text-gray-400 text-sm mb-4">
                                    SKU: 4897056203458
                                </p>
                                <button class="bg-red-500 text-white py-2 px-4 rounded-full mb-2">
                                    Thêm Vào Giỏ Hàng
                                </button>
                                <button class="text-red-500">
                                    <i class="far fa-heart">
                                    </i>
                                </button>
                            </div>
                        </div>
                        <div class="border border-gray-300 rounded p-4">
                            <img alt="Mô Hình Lotso Huggin Bear" class="w-full h-48 object-cover mb-4" height="200"
                                 src="https://storage.googleapis.com/a1aa/image/AtlDDcFwbyYtKin5gKCI0adqYsjGysBbOY5ha0VEE-g.jpg"
                                 width="200" />
                            <div class="text-center">
                                <p class="text-red-500 font-bold mb-2">
                                    90.000Đ
                                </p>
                                <p class="text-gray-600 mb-2">
                                    Mô Hình Lotso Huggin Bear
                                </p>
                                <p class="text-gray-400 text-sm mb-4">
                                    SKU: 4897056203459
                                </p>
                                <button class="bg-red-500 text-white py-2 px-4 rounded-full mb-2">
                                    Thêm Vào Giỏ Hàng
                                </button>
                                <button class="text-red-500">
                                    <i class="far fa-heart">
                                    </i>
                                </button>
                            </div>
                        </div>
                        <div class="border border-gray-300 rounded p-4">
                            <img alt="Mô Hình Nhân Vật Động Vật" class="w-full h-48 object-cover mb-4" height="200"
                                 src="https://storage.googleapis.com/a1aa/image/83N5QSbdGVlpRQTwGuzuSq32Qjgv9x8wzWOxMSQhLV4.jpg"
                                 width="200" />
                            <div class="text-center">
                                <p class="text-red-500 font-bold mb-2">
                                    440.000Đ
                                </p>
                                <p class="text-gray-600 mb-2">
                                    Mô Hình Nhân Vật Động Vật
                                </p>
                                <p class="text-gray-400 text-sm mb-4">
                                    SKU: 4897056203460
                                </p>
                                <button class="bg-red-500 text-white py-2 px-4 rounded-full mb-2">
                                    Thêm Vào Giỏ Hàng
                                </button>
                                <button class="text-red-500">
                                    <i class="far fa-heart">
                                    </i>
                                </button>
                            </div>
                        </div>
                        <div class="border border-gray-300 rounded p-4">
                            <img alt="Mô Hình Disney The Cute Stitch" class="w-full h-48 object-cover mb-4" height="200"
                                 src="https://storage.googleapis.com/a1aa/image/Taz4yei1yboRr40Z4jTrrVx_cfNCdDcB-dTkQ-YnlSI.jpg"
                                 width="200" />
                            <div class="text-center">
                                <p class="text-red-500 font-bold mb-2">
                                    300.000Đ
                                </p>
                                <p class="text-gray-600 mb-2">
                                    Mô Hình Disney The Cute Stitch
                                </p>
                                <p class="text-gray-400 text-sm mb-4">
                                    SKU: 4897056203461
                                </p>
                                <button class="bg-red-500 text-white py-2 px-4 rounded-full mb-2">
                                    Thêm Vào Giỏ Hàng
                                </button>
                                <button class="text-red-500">
                                    <i class="far fa-heart">
                                    </i>
                                </button>
                            </div>
                        </div>
                        <div class="border border-gray-300 rounded p-4">
                            <img alt="Mô Hình Tom And Jerry" class="w-full h-48 object-cover mb-4" height="200"
                                 src="https://storage.googleapis.com/a1aa/image/dyGCwpvOC3ynXEprMSgIugs8Mlo-RtR-y4lnjXeabzA.jpg"
                                 width="200" />
                            <div class="text-center">
                                <p class="text-red-500 font-bold mb-2">
                                    300.000Đ
                                </p>
                                <p class="text-gray-600 mb-2">
                                    Mô Hình Tom And Jerry
                                </p>
                                <p class="text-gray-400 text-sm mb-4">
                                    SKU: 4897056203462
                                </p>
                                <button class="bg-red-500 text-white py-2 px-4 rounded-full mb-2">
                                    Thêm Vào Giỏ Hàng
                                </button>
                                <button class="text-red-500">
                                    <i class="far fa-heart">
                                    </i>
                                </button>
                            </div>
                        </div>
                        <div class="border border-gray-300 rounded p-4">
                            <img alt="Mô Hình Nhân Vật Shin" class="w-full h-48 object-cover mb-4" height="200"
                                 src="https://storage.googleapis.com/a1aa/image/YCohV8Jrnd7Ly3Du1AsfHU-mmXaTQMcj1mC_kFvkCR4.jpg"
                                 width="200" />
                            <div class="text-center">
                                <p class="text-red-500 font-bold mb-2">
                                    330.000Đ
                                </p>
                                <p class="text-gray-600 mb-2">
                                    Mô Hình Nhân Vật Shin
                                </p>
                                <p class="text-gray-400 text-sm mb-4">
                                    SKU: 4897056203463
                                </p>
                                <button class="bg-red-500 text-white py-2 px-4 rounded-full mb-2">
                                    Thêm Vào Giỏ Hàng
                                </button>
                                <button class="text-red-500">
                                    <i class="far fa-heart">
                                    </i>
                                </button>
                            </div>
                        </div>
                        <div class="border border-gray-300 rounded p-4">
                            <img alt="Mô Hình Nhân Vật Gấu Trúc" class="w-full h-48 object-cover mb-4" height="200"
                                 src="https://storage.googleapis.com/a1aa/image/mpY8Qrn-VLqnb22CYMPCifTGy2u2qHO03JWlSdL6s1M.jpg"
                                 width="200" />
                            <div class="text-center">
                                <p class="text-red-500 font-bold mb-2">
                                    280.000Đ
                                </p>
                                <p class="text-gray-600 mb-2">
                                    Mô Hình Nhân Vật Gấu Trúc
                                </p>
                                <p class="text-gray-400 text-sm mb-4">
                                    SKU: 4897056203464
                                </p>
                                <button class="bg-red-500 text-white py-2 px-4 rounded-full mb-2">
                                    Thêm Vào Giỏ Hàng
                                </button>
                                <button class="text-red-500">
                                    <i class="far fa-heart">
                                    </i>
                                </button>
                            </div>
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

        </script>

    </body>

</html>