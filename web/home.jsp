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
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
        <script src="https://cdn.tailwindcss.com"></script>
        <script src="./assets/js/script.js"></script>
        <style>
            /* Back to Top Button */
            .shopee-back-to-top {
                background: #EE4D2D;
                color: white;
                width: 60px;
                height: 60px;
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
                transition: all 0.3s ease;
                border: none;
                outline: none;
                opacity: 0.8;
                right: 20px;
                bottom: 90px;
            }

            .shopee-back-to-top:hover {
                background: #f05d40;
                transform: scale(1.1);
                box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
                opacity: 1;
            }

            .shopee-back-to-top i {
                font-size: 20px;
            }
        </style>
        <title>LorKingdom</title>
    </head>

    <body class="bg-white font-sans text-l" style="margin-top: 80px;">
        <!-- Header Section -->
        <jsp:include page="assets/Component/header.jsp"/>
        <!-- end Header Section -->

        <!---------------------------------- Banner Section ---------------------------------->
        <section id="hero" class="hero section dark-background">

            <div id="hero-carousel" data-bs-interval="5000" class="container carousel carousel-fade"
                 data-bs-ride="carousel">

                <!-- Slide 1 -->
                <div class="carousel-item active">
                    <div class="carousel-container">
                        <img src="./assets/img/banner01.png" alt="">
                    </div>
                </div>

                <!-- Slide 2 -->
                <div class="carousel-item">
                    <div class="carousel-container">
                        <img src="./assets/img/banner02.png" alt="">
                    </div>
                </div>

                <!-- Slide 3 -->
                <div class="carousel-item">
                    <div class="carousel-container">
                        <img src="./assets/img/banner2.png" alt="">
                    </div>
                </div>

                <a class="carousel-control-prev" href="#hero-carousel" role="button" data-bs-slide="prev">
                    <span class="carousel-control-prev-icon bi bi-chevron-left" aria-hidden="true"></span>
                </a>

                <a class="carousel-control-next" href="#hero-carousel" role="button" data-bs-slide="next">
                    <span class="carousel-control-next-icon bi bi-chevron-right" aria-hidden="true"></span>
                </a>

            </div>

            <svg class="hero-waves" viewBox="0 24 150 28 " preserveAspectRatio="none">
            <defs>
            <path id="wave-path" d="M-160 44c30 0 58-18 88-18s 58 18 88 18 58-18 88-18 58 18 88 18 v44h-352z">
            </path>
            </defs>
            <g class="wave1">
            <use xlink:href="#wave-path" x="50" y="3"></use>
            </g>
            <g class="wave2">
            <use xlink:href="#wave-path" x="50" y="0"></use>
            </g>
            <g class="wave3">
            <use xlink:href="#wave-path" x="50" y="9"></use>
            </g>
            </svg>

        </section><!-- /Hero Section -->


        <!-- Call To Action Section -->
        <section id="call-to-action" class="call-to-action section dark-background">

            <div class="container">

                <div class="row" data-aos="zoom-in" data-aos-delay="100">
                    <div class="col-xl-9 text-center text-xl-start">
                        <h3 class="playful-title">Khơi Gợi Sáng Tạo - Chắp Cánh Tuổi Thơ</h3>
                        <p class="playful-text">
                            Khám phá thế giới đồ chơi an toàn, sáng tạo và đầy màu sắc dành cho bé yêu.  
                            Giúp bé phát triển trí tuệ, rèn luyện kỹ năng và thỏa sức vui chơi mỗi ngày!  
                        </p>
                    </div>
                    <div class="col-xl-3 cta-btn-container text-center d-flex flex-column align-items-center">
                        <img src="./assets/img/fbLorKingDom.png" class="img-fluid" style="max-width: 50%;" alt="alt">
                        <p class="playful-text mt-2">Trang của chúng tôi</p>
                    </div>

                </div>


            </div>

        </section><!-- /Call To Action Section -->

        <!---------------------------------- Banner Section ---------------------------------->

        <div class="bg-white">

            <div class="flex flex-col lg:flex-row" >
                <!-- Sidebar -->

                <div class="w-full lg:w-1/4 pl-12  " style="margin-top: 30px;">
                    <form action="getList" method="get" id="filterForm">
                        <!-- Category -->
                        <div class="mb-4" style="border-bottom:1px solid #ccc; margin-bottom: 5px; padding-bottom: 10px; margin-top: 70px;">
                            <h3 class="font-semibold mb-2 text-orange-500">Danh Mục</h3>
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
                            <h3 class="font-semibold mb-2 text-orange-500">Giá</h3>
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
                            <h3 class="font-semibold mb-2 text-orange-500">Tuổi</h3>
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

                        <!-- Brand -->
                        <div class="mb-4">
                            <h3 class="font-semibold mb-2 text-orange-500">Thương Hiệu</h3>
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

                        <!-- Sex -->
                        <div class="mb-4">
                            <h3 class="font-semibold mb-2 text-orange-500">Giới Tính</h3>
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


                    </form>
                </div>



                <!-- Main Content -->
                <div class="w-full lg:w-3/4 p-4" >
                    <div class="flex justify-between items-center mb-4">
                        <div class="flex items-center">
                            <button type="button"
                                    class=" text-white px-4 py-2 rounded mt-4" 
                                    style="background: #FF6600"
                                    onclick="resetFilter()">
                                Xoá bộ lọc
                            </button>
<!-- Thêm phần tử mốc vào nơi bạn muốn cuộn đến -->
<div class="your-class" id="scroll-marker"></div>
                        </div>
                        <div class="flex items-center text-xl">
                            <span class="mr-2">
                                Kết quả:
                            </span>
                            <span class="font-bold">
                                ${totalProducts} Sản Phẩm  
                            </span>
                        </div>
                    </div>
                    <div style="margin-bottom: 50px;" >
                        <div id="productListContainer" >
                            <jsp:include page="assets/Component/partialProduct.jsp" />
                        </div>

                    </div>

                </div>

            </div>
        </div>

        <!-- Back to Top Button -->
        <button id="back-to-top" class="hidden fixed bottom-35 right-5 shopee-back-to-top">
            <i class="bi bi-arrow-up"></i>
        </button>

        <!-- Vendor JS Files -->
        <script src="assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

        <script>
                                        // Ví dụ điều khiển hiển thị menu dropdown
                                        function toggleMenu(menuId) {
                                            var menu = document.getElementById(menuId);
                                            menu.classList.toggle('hidden');
                                        }

                                        // Hàm gửi bộ lọc bằng AJAX
                                        function submitFilter() {
                                            const form = document.getElementById("filterForm");
                                            const formData = new FormData(form);
                                            formData.append("partial", "true");
                                            const params = new URLSearchParams(formData).toString();
                                            fetch("getList?" + params, {
                                                method: "GET"
                                            })

                                                    .then(response => response.text())
                                                    .then(data => {
                                                        document.getElementById("productListContainer").innerHTML = data;
                                                    })
                                                    .catch(error => console.error("Lỗi khi load dữ liệu:", error));
                                        }

                                        function resetFilter() {
                                            document.getElementById("filterForm").reset();
                                            submitFilter();
                                        }


                                        document.addEventListener("DOMContentLoaded", function () {
                                            var bellIcon = document.querySelector(".notification-icon");
                                            var notificationBox = document.getElementById("notificationBox");
                                            // Toggle thông báo khi click vào chuông
                                            bellIcon.addEventListener("click", function (event) {
                                                event.preventDefault();
                                                event.stopPropagation(); // Ngăn sự kiện lan lên document
                                                notificationBox.classList.toggle("active");
                                            });
                                            // Đóng thông báo khi click ra ngoài
                                            document.addEventListener("click", function (event) {
                                                if (!notificationBox.contains(event.target) && !bellIcon.contains(event.target)) {
                                                    notificationBox.classList.remove("active");
                                                }
                                            });
                                        });
                                        document.addEventListener("DOMContentLoaded", function () {
                                            var bellIcon = document.querySelector(".notification-icon");
                                            var notificationBox = document.getElementById("notificationBox");
                                            // Toggle thông báo khi click vào chuông
                                            bellIcon.addEventListener("click", function (event) {
                                                event.preventDefault();
                                                event.stopPropagation(); // Ngăn sự kiện lan lên document
                                                notificationBox.classList.toggle("active");
                                            });
                                            // Đóng thông báo khi click ra ngoài
                                            document.addEventListener("click", function (event) {
                                                if (!notificationBox.contains(event.target) && !bellIcon.contains(event.target)) {
                                                    notificationBox.classList.remove("active");
                                                }
                                            });
                                        });
//            function goToPage(pageNumber) {
//                const form = document.getElementById("filterForm");
//                const formData = new FormData(form);
//                formData.append("partial", "true");
//                formData.append("page", pageNumber);
//                const params = new URLSearchParams(formData).toString();
//                fetch("getList?" + params, {method: "GET"})
//                        .then(response => response.text())
//                        .then(data => {
//                            document.getElementById("productListContainer").innerHTML = data;
//
//                        })
//                        .catch(error => console.error("Error while loading data:", error));
//                document.getElementById("resultsCount").scrollIntoView({
//                    behavior: 'smooth'
//                });
//            }
                                        function goToPage(pageNumber) {
                                            const form = document.getElementById("filterForm");
                                            const formData = new FormData(form);
                                            formData.append("partial", "true");
                                            formData.append("page", pageNumber);

                                            const params = new URLSearchParams(formData).toString();

                                            fetch("getList?" + params, {method: "GET"})
                                                    .then(response => response.text())
                                                    .then(data => {
                                                        // 1. Thay thế nội dung danh sách
                                                        document.getElementById("productListContainer").innerHTML = data;

                                                        // 2. Sau khi cập nhật xong, cuộn đến #resultsCount
                                                        document.getElementById("resultsCount").scrollIntoView({
                                                            behavior: 'smooth'
                                                        });
                                                    })
                                                    .catch(error => console.error("Error while loading data:", error));
                                        }
                                        function resetFilter() {
                                            // Reset form về trạng thái ban đầu
                                            document.getElementById("filterForm").reset();
                                            // Gửi lại request AJAX để hiển thị toàn bộ sản phẩm
                                            submitFilter();
                                        }

        </script>
        <script>
            // Back to Top Button
            document.addEventListener("DOMContentLoaded", function () {
                const backToTopBtn = document.getElementById("back-to-top");

                window.addEventListener("scroll", function () {
                    if (window.scrollY > 300) {
                        backToTopBtn.classList.remove("hidden");
                        backToTopBtn.classList.add("block");
                    } else {
                        backToTopBtn.classList.add("hidden");
                        backToTopBtn.classList.remove("block");
                    }
                });

                backToTopBtn.addEventListener("click", function () {
                    window.scrollTo({
                        top: 0,
                        behavior: "smooth"
                    });
                });
            });
        </script>

    </body>
</html>
