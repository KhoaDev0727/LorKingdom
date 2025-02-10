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
        <jsp:include page="assets/Component/header.jsp"/>
        <!-- end Header Section -->


        <s  ection class="banner-section">
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
        </section>

        <div class="product-carousel " style="background-color: red; width: 600px; height: 300px;   ">
            <div class="menu-container">
                <ul class="list-group">
                    <c:forEach var="superCat" items="${superCategories}">
                        <!-- SuperCategory -->
                        <li class="list-group-item">
                            <a data-bs-toggle="collapse" href="#collapse${superCat.superCategoryID}" role="button" 
                               aria-expanded="false" aria-controls="collapse${superCat.superCategoryID}">
                                ${superCat.name} 
                            </a>
                            <!-- Danh mục con (Categories) -->
                            <div class="collapse" id="collapse${superCat.superCategoryID}">
                                <ul class="list-group ms-3">
                                    <c:forEach var="cat" items="${categories}">
                                        <c:if test="${cat.superCategoryID == superCat.superCategoryID}">
                                            <li class="list-group-item">
                                                <a href="CategoryServlet?action=view&categoryID=${cat.categoryID}">
                                                    ${cat.name}
                                                </a>
                                            </li>
                                        </c:if>
                                    </c:forEach>
                                </ul>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </div>
            <div></div>
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
