<%-- 
    Document   : aboutus
    Created on : Jan 15, 2025, 9:07:58 AM
    Author     : danny
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>About Us - LorKingdom</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
        <!-- jQuery -->
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
        <link rel="stylesheet" href="https://unpkg.com/swiper/swiper-bundle.min.css">
        <link rel="stylesheet" href="./assets/styleUser/styleaboutus.css"/>       
    </head>

    <body>
        
        <!-- Header Section -->
    <jsp:include page="header.jsp"/>
    <!-- end Header Section -->
        
        <div class="image-banner">
            <div class="image-banner">
                <img src="./img/banneraboutus.png" alt="LorKingdom Banner">
            </div>
        </div> 

        <!-- About LorTravel Section -->
        <section>
            <div class="section-2">
                <div class="header-section2">
                    <img src="./img/logo.png" alt="LorTravel Logo" />
                    <h3>Hành Trình </h3>
                    <h2>Chặng Đường Phát Triển Của LorKingdom</h2>
                </div>

                <!-- Swiper Container -->
                <div class="swiper-container time-slide">
                    <div class="swiper-wrapper">

                        <!-- Slide 1: Establishment -->
                        <div class="swiper-slide">
                            <div class="time-line-item">
                                <div class="time-line-img">
                                    <img src="./img/slide1.png" alt="Our Fashion Site Launch" />
                                </div>
                                <div class="time-line-content">
                                    <h4>2015</h4>
                                    <p>Trang web đồ chơi trẻ em của chúng tôi được ra đời với mục tiêu mang đến những món đồ chơi chất lượng, an toàn và giàu tính giáo dục cho trẻ em khắp nơi.</p>
                                </div>                                
                            </div>
                        </div>

                        <!-- Slide 2: Vision and Mission -->
                        <div class="swiper-slide">
                            <div class="time-line-item">
                                <div class="time-line-img">
                                    <img src="./img/slide2.png" alt="Our Fashion Mission" />
                                </div>
                                <div class="time-line-content">
                                    <h4>2016</h4>
                                    <p>Chúng tôi không ngừng sáng tạo và cập nhật các sản phẩm mới, nhằm mang lại niềm vui và sự phát triển trí tuệ cho trẻ nhỏ thông qua những món đồ chơi độc đáo và ý nghĩa.</p>
                                </div>                                
                            </div>
                        </div>

                        <!-- Slide 3: Expansion -->
                        <div class="swiper-slide">
                            <div class="time-line-item">
                                <div class="time-line-img">
                                    <img src="./img/slide3.png" alt="Expansion" />
                                </div>
                                <div class="time-line-content">
                                    <h4>2020</h4>
                                    <p>Với sự tin tưởng từ hàng ngàn gia đình, chúng tôi đã mở rộng danh mục sản phẩm, hợp tác với các thương hiệu uy tín toàn cầu để đáp ứng nhu cầu của trẻ em ở mọi lứa tuổi.</p>
                                </div>                                
                            </div>
                        </div>

                        <!-- Slide 4: Present and Future -->
                        <div class="swiper-slide">
                            <div class="time-line-item">
                                <div class="time-line-img">
                                    <img src="./img/slide4.png" alt="Today and Beyond" />
                                </div>
                                <div class="time-line-content">
                                    <h4>Hiện Tại</h4>
                                    <p>Hiện tại, trang web của chúng tôi không chỉ cung cấp đồ chơi mà còn là người bạn đồng hành cùng các bậc phụ huynh trong việc giáo dục và phát triển toàn diện cho con trẻ.</p>
                                </div>                                
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </section>

        <!-- History Section -->
        <section class="section-history">
            <div class="text-content">
                <h2>Lịch Sử Đồ Chơi</h2>
                <div class="underline"></div>
                <p>
                    Từ những món đồ chơi thủ công đơn giản đến các sản phẩm công nghệ hiện đại, lịch sử đồ chơi phản ánh sự sáng tạo và tình yêu dành cho trẻ em qua từng thời kỳ. 
                </p>
                <p>
                    Ngày nay, đồ chơi không chỉ là công cụ giải trí mà còn hỗ trợ giáo dục, khuyến khích sự phát triển trí tuệ, tư duy sáng tạo và kỹ năng xã hội của trẻ nhỏ.
                </p>
            </div>
        </section>        

        
        <!-- Unique Experiences Section -->
        <section class="section1">
            <div class="video-content" style="margin-left: 30px; text-align: center;">
                <!-- Video Embed -->
                <iframe width="560" height="315"
                        src="https://www.youtube.com/embed/TDgESP7-hpk"
                        frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen>
                </iframe>
        
                <!-- Fallback Option -->
                <p style="margin-top: 10px;">Video không khả dụng? Xem trực tiếp trên <a href="https://www.youtube.com/watch?v=TDgESP7-hpk" target="_blank">YouTube</a></p>
            </div>
        
            <div class="text-content" style="margin-top: 20px;">
                <h2>Khám Phá Thế Giới Đồ Chơi Sáng Tạo</h2>
                <div class="underline" style="margin: 10px 0; width: 80px; height: 2px; background-color: #ff9800;"></div>
                <p>
                    Chúng tôi mang đến những món đồ chơi đầy sáng tạo và ý nghĩa, giúp khơi gợi trí tưởng tượng và kích thích sự phát triển trí tuệ của trẻ nhỏ.
                    Từ những bộ lắp ráp thông minh đến các mô hình giáo dục thực tế, mỗi sản phẩm đều được chọn lọc kỹ lưỡng để đảm bảo an toàn và chất lượng.
                </p>
                <p>
                    Đội ngũ của chúng tôi cam kết mang lại trải nghiệm mua sắm tốt nhất, nơi các bậc phụ huynh có thể tìm thấy những sản phẩm phù hợp với mọi lứa tuổi và sở thích của con em mình.
                </p>
            </div>
        </section>
        

        <section class="section-history">
            <div class="text-content">
                <h2>Sứ Mệnh Của LorKingdom</h2>
                <div class="underline"></div>
                <p>
                    LorKingdom được sáng lập với mục tiêu mang đến cho trẻ em một thế giới đồ chơi sáng tạo, an toàn và phát triển trí tuệ. 
                    Chúng tôi cam kết cung cấp các sản phẩm đồ chơi không chỉ giúp trẻ nhỏ vui chơi mà còn kích thích sự sáng tạo, khám phá và học hỏi.
                </p>
                <p>
                    Với LorKingdom, mỗi món đồ chơi không chỉ đơn thuần là món quà mà còn là công cụ giúp xây dựng tuổi thơ hạnh phúc, khuyến khích trẻ phát triển trí tưởng tượng và tư duy logic. 
                    Chúng tôi luôn lấy sự hài lòng của trẻ em và phụ huynh làm động lực để không ngừng phát triển.
                </p>
            </div>
        </section>
        
        <section class="section1">
            <div class="text-content">
                <h2>Khám Phá Thế Giới Đồ Chơi Đầy Sáng Tạo</h2>
                <div class="underline"></div>
                <p>
                    Trang web của chúng tôi cung cấp đa dạng các loại đồ chơi phù hợp với nhiều lứa tuổi và sở thích khác nhau, từ đồ chơi trí tuệ, xếp hình, đến các mô hình sáng tạo. 
                    Mỗi sản phẩm đều được kiểm định chất lượng chặt chẽ, đảm bảo an toàn tuyệt đối cho trẻ nhỏ.
                </p>
                <p>
                    Hãy cùng khám phá các bộ sưu tập đồ chơi phong phú, giúp trẻ vừa học vừa chơi, phát triển toàn diện cả về thể chất lẫn tinh thần.
                </p>
            </div>
        
            <div class="video-content" style="margin-left: 30px; display: flex; justify-content: center; margin-top: 20px;">
                <iframe width="560" height="315"
                        src="https://www.youtube.com/embed/c12cv5JMVYM"
                        frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen>
                </iframe>
            </div>            
        </section>
        
        <!-- Footer Section -->
        <footer>
            <div class="footer-content">
                <div class="footer-links">
                    <a href="#">Trang Chủ</a>
                    <a href="#">Sản Phẩm</a>
                    <a href="#">Giới Thiệu</a>
                    <a href="#">Liên Hệ</a>
                </div>
                <div class="footer-info">
                    <p>&copy; 2025 LorKingdom. Tất cả các quyền được bảo lưu.</p>
                    <p>Địa chỉ: Ninh Kiều, Cần Thơ, Việt Nam</p>
                    <p>Điện thoại: +84 123 456 789</p>
                </div>
            </div>
        </footer>
        

        <!-- Swiper Script -->
        <script src="https://unpkg.com/swiper/swiper-bundle.min.js"></script>
        <script>
            var swiper = new Swiper('.swiper-container', {
                slidesPerView: 1,
                spaceBetween: 1,
                loop: true,
                speed: 2000,
                autoplay: {
                    delay: 5000,
                    disableOnInteraction: false,
                },
                watchSlidesVisibility: true,
                on: {
                    init: function () {
                        this.slides[this.activeIndex].style.opacity = 1;
                    },
                    slideChange: function () {
                        this.slides.forEach(slide => slide.style.opacity = 0);
                        this.slides[this.activeIndex].style.opacity = 1;
                    }
                }
            });
        </script>
    </body>
</html>