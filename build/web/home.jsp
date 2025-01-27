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
        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css"
              rel="stylesheet">
        <!-- Bootstrap JS Bundle -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
        <script src="./assets/js/script.js"></script>
        <title>LorKingdom</title>

    </head>

    <body>
        <!-- Header Section -->
        <jsp:include page="header.jsp"/>
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




        <section class="item-box">
            <div class="category-section text-center">
                <button class="category-btn active">Tất Cả</button>
                <button class="category-btn">Hàng Mới</button>
                <button class="category-btn">Sự Kiện</button>
                <button class="category-btn">Giảm Giá</button>
            </div>


            <!-- cart-item blocks -->
            <sql:setDataSource var="conn"
                       driver="com.microsoft.sqlserver.jdbc.SQLServerDriver"
                       url="jdbc:sqlserver://LEMINHKHOA:1433;databaseName=LorKingDom;encrypt=true;trustServerCertificate=true;"
                       user="sa"
                       password="12345" />
    <sql:query var="products" dataSource="${conn}">
        SELECT * FROM Product WHERE Status = 'Available'
    </sql:query>

    <!-- Hiển thị sản phẩm -->
    <div class="cart-items">
        <c:forEach var="product" items="${products.rows}">
            <div class="cart-item">
                <div class="item-image">
                    <img src="${product.Image}" alt="${product.Name}" class="product-image">
                    <span class="discount-badge">-35%</span> 
                </div>
                <div class="item-info">
                    <span class="exclusive-tag">Độc Quyền Online</span>
                    <h4>${product.Name}</h4>
                    <div class="price">
                        <span class="new-price">${product.Price} Đ</span>
                        <!-- Bạn có thể tính giá cũ giảm 35% nếu cần -->
                        <span class="old-price">${product.Price} Đ</span>
                    </div>
                    <form action="AddToCartServlet" method="POST">
                        <input type="hidden" name="productId" value="${product.ProductID}">
                        <button class="add-to-cart-btn" type="submit">Thêm Vào Giỏ Hàng</button>
                    </form>
                </div>
            </div>
        </c:forEach>
    </div>
            <!-- ==== -->
            
            

        </section>

        <div class="product-carousel">
            <h2>LEGO Giảm Giá Tặng Quà</h2>
            <button class="view-more">Xem Thêm</button>
            <div class="product-items">
                <div class="product-item">
                    <img src="./assets/img/lego1.png" alt="Product 1">
                    <div class="product-info">
                        <p>LEGO MARKETING</p>
                        <p>SKU:MKT-5008978</p>
                        <p>[Quà Tặng, Không Bán] Bộ Lắp Ráp Con Rắn LEGO</p>
                        <p class="price">329.000 Đ</p>
                        <button class="gift-button">Hàng Tặng Không Bán</button>
                    </div>
                </div>
                <div class="product-item">
                    <img src="./assets/img/lego2.png" alt="Product 2">
                    <div class="product-info">
                        <p>LEGO TECHNIC</p>
                        <p>SKU:42154</p>
                        <p>Đồ Chơi Lắp Ráp Siêu Xe Thể Thao Ford GT LEGO TECHNIC</p>
                        <p class="old-price">3.799.000 Đ</p>
                        <p class="price">2.659.000 Đ</p>
                        <button class="cart-button">Thêm Vào Giỏ Hàng</button>
                    </div>
                </div>
                <div class="product-item">
                    <img src="./assets/img/lego3.png" alt="Product 2">
                    <div class="product-info">
                        <p>LEGO TECHNIC</p>
                        <p>SKU:42154</p>
                        <p>Đồ Chơi Lắp Ráp Siêu Xe Thể Thao Ford GT LEGO TECHNIC</p>
                        <p class="old-price">3.799.000 Đ</p>
                        <p class="price">2.659.000 Đ</p>
                        <button class="cart-button">Thêm Vào Giỏ Hàng</button>
                    </div>
                </div>
                <div class="product-item">
                    <img src="./assets/img/lego4.png" alt="Product 2">
                    <div class="product-info">
                        <p>LEGO TECHNIC</p>
                        <p>SKU:42154</p>
                        <p>Đồ Chơi Lắp Ráp Siêu Xe Thể Thao Ford GT LEGO TECHNIC</p>
                        <p class="old-price">3.799.000 Đ</p>
                        <p class="price">2.659.000 Đ</p>
                        <button class="cart-button">Thêm Vào Giỏ Hàng</button>
                    </div>
                </div>
                <!-- Add more product-item elements as needed -->
            </div>
        </div>



        <div class="container my-5">
            <h2 class="text-center mb-4" style="color: #001FFF; font-weight: 750;">Thương Hiệu Nổi Bật</h2>
            <div class="row g-4">
                <!-- Card 1 -->
                <div class="col-6 col-md-3">
                    <div class="brand-card">
                        <img src="./assets/img/brand1.png" alt="brand1">
                    </div>
                </div>
                <!-- Card 2 -->
                <div class="col-6 col-md-3">
                    <div class="brand-card">
                        <img src="./assets/img/brand2.png" alt="brand2">
                    </div>
                </div>
                <!-- Card 3 -->
                <div class="col-6 col-md-3">
                    <div class="brand-card">
                        <img src="./assets/img/brand3.png" alt="brand3">
                    </div>
                </div>
                <!-- Card 4 -->
                <div class="col-6 col-md-3">
                    <div class="brand-card">
                        <img src="./assets/img/brand4.png" alt="brand4">
                    </div>
                </div>
                <!-- Card 5 -->
                <div class="col-6 col-md-3">
                    <div class="brand-card">
                        <img src="./assets/img/brand5.png" alt="brand5">
                    </div>
                </div>
                <!-- Card 6 -->
                <div class="col-6 col-md-3">
                    <div class="brand-card">
                        <img src="./assets/img/brand6.png" alt="brand6">
                    </div>
                </div>
                <!-- Card 7 -->
                <div class="col-6 col-md-3">
                    <div class="brand-card">
                        <img src="./assets/img/brand7.png" alt="brand7">
                    </div>
                </div>
                <!-- Card 8 -->
                <div class="col-6 col-md-3">
                    <div class="brand-card">
                        <img src="./assets/img/brand8.png" alt="brand8">
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
            });</script>

    </body>

</html>