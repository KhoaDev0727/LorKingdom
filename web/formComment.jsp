<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html lang="en">
    <head>
        <meta charset="utf-8"/>
        <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
        <title>
            Product Review
        </title>
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet"/>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>
        <script src="assets\js\JSFormReview.js"></script>
    </head>
    <style>
        #star-rating .fa-star {
            color: #ccc; /* màu cho sao chưa được chọn */
            cursor: pointer;
        }
        #star-rating .fa-star.active {
            color: #ffc107; /* màu vàng cho sao được chọn */
        }
    </style>
</style>
<body class="bg-light">
    <div class="container mt-5">
        <form action="ReviewManagementServlet" method="POST" enctype="multipart/form-data">
            <input type="hidden" name="action" value="add">
            <div class="card shadow-sm">
                <div class="card-body">
                    <h1 class="card-title h4">
                        Product Review
                    </h1>   
                    <div class="d-flex mb-3">
                        <img alt="Product image" class="mr-3" height="60" src="https://storage.googleapis.com/a1aa/image/6sQYus8kRTgTUcgjCJqzxzL_GNhdwQDUTc_Gc_xcTSE.jpg" width="60"/>
                        <div>
                            <p class="font-weight-bold">
                                [NEW LOOK] Simple Face Wash - gentle and effective for all skin types 150ml
                            </p>
                            <p class="text-muted">
                                Category: Deep Moisturizing
                            </p>
                        </div>
                    </div>
                    <div class="mb-3">
                        <p class="font-weight-bold">Product Quality</p>
                        <div class="d-flex align-items-center">
                            <!-- Thêm input ẩn ngay sau phần đánh giá sao -->
                            <input type="hidden" name="rating" id="rating-input" value="5">
                            <div class="text-warning" id="star-rating">
                                <i class="fas fa-star" data-rating="1"></i>
                                <i class="fas fa-star" data-rating="2"></i>
                                <i class="fas fa-star" data-rating="3"></i>
                                <i class="fas fa-star" data-rating="4"></i>
                                <i class="fas fa-star" data-rating="5"></i>
                            </div>
                            <span class="ml-2 text-warning" id="rating-text">Excellent</span>
                        </div>
                    </div>
                    <div class="mb-3">
                        <textarea class="form-control" placeholder="Share what you like about this product with other buyers." rows="5" name="description"></textarea>
                    </div>
                    <div class="d-flex mb-3">
                        <div class="btn btn-outline-danger position-relative" style="cursor: pointer;">
                            <i class="fas fa-camera mr-1"></i>
                            <span id="file-name">Add Image</span>
                            <input type="file" class="form-control position-absolute" name="image" accept="image/*" style="opacity: 0; top: 0; left: 0; width: 100%; height: 100%; cursor: pointer;" onchange="updateFileName(this)">
                        </div>
                    </div>
                    <p class="text-muted small">
                        Add 50 characters and 1 image and 1 video to earn
                        <span class="text-danger">
                            200 points
                        </span>
                    </p>
                    <div class="d-flex justify-content-start mr-3">
                        <button class="btn btn-secondary">
                            BACK
                        </button>
                        <button class="btn btn-danger">
                            SUBMIT
                        </button>
                    </div>
                </div>
            </div>
        </form>
    </div>
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js">
    </script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js">
    </script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js">
    </script>

</body>
</html>
