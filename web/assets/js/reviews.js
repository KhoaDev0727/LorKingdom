

function loadReviews(star, page, productId) {
    console.log("Loading reviews for Product ID: ", productId);
    $.ajax({
        url: 'ReviewManagementServlet',
        type: 'POST',
        data: {
            action: "fillterCustomer",
            star: star,
            page: page,
            productID: productId
        },
        success: function (response) {
            updatePagination(response.currentPage, response.totalPages, star, productId);
            updateReviewsUI(response.reviews);

        },
        error: function () {
            alert("Có lỗi xảy ra khi tải dữ liệu đánh giá.");
        }
    });
}

function updateReviewsUI(reviews) {
    var html = "";
    if (reviews && reviews.length > 0) {
        $.each(reviews, function (index, review) {
            html += '<div class="border-bottom pb-4 mb-4">';
            html += '    <div class="d-flex align-items-center mb-2">';
            html += '        <img src="' + review.account.image + '" alt="User avatar" class="rounded-circle mr-2" width="40" height="40">';
            html += '        <div>';
            html += '            <span class="font-weight-bold">' + review.account.userName + '</span>';
            html += '            <div class="d-flex align-items-center">';
            for (var i = 1; i <= Math.floor(review.Rating); i++) {
                html += '<i class="fas fa-star text-danger"></i>';
            }
            for (var i = Math.ceil(review.Rating); i < 5; i++) {
                html += '<i class="far fa-star text-danger"></i>';
            }
            html += '            </div>';
            html += '        </div>';
            html += '    </div>';
            html += '    <div class="text-muted mb-2">' + new Date(review.reviewAt).toLocaleString() + '</div>';
            html += '    <p class="mb-2">' + review.Comment + '</p>';
            if (review.imgReview && review.imgReview.trim() !== "") {
                html += '<div class="mb-2"><img src="http://localhost:8080/LorKingdom/' + review.imgReview + '" alt="Product image" class="img-thumbnail review-image" style="width: 300px; height: 300px;"></div>';
            }
            html += '</div>';
        });
    } else {
        html = '<p class="text-center text-muted">Chưa có đánh giá nào.</p>';
    }
    $("#reviews-container").html(html);
}


//Phần Trang Review phân trang
function updatePagination(currentPage, totalPages, star, productId) {
    console.log("Current Page:", currentPage, "Total Pages:", totalPages, "Star:", star, "ProductId:", productId);

    // Xử lý các giá trị không hợp lệ
    let safeCurrentPage = currentPage && !isNaN(currentPage) ? parseInt(currentPage) : 1; // Mặc định là 1 nếu không hợp lệ
    let safeTotalPages = totalPages && !isNaN(totalPages) && totalPages >= 0 ? parseInt(totalPages) : 1; // Mặc định là 1 nếu không hợp lệ

    // Luôn tạo HTML phân trang
    let paginationHtml = '<ul class="pagination">';

    // Nút "Previous"
    if (safeCurrentPage > 1) {
        paginationHtml += '<li class="page-item"><a class="page-link" href="javascript:void(0)" data-page="' + (safeCurrentPage - 1) + '" data-star="' + star + '" data-product-id="' + productId + '">«</a></li>';
    } else {
        paginationHtml += '<li class="page-item disabled"><a class="page-link" href="javascript:void(0)">«</a></li>';
    }

    // Hiển thị trang đầu tiên
    if (safeTotalPages > 0) {
        paginationHtml += '<li class="page-item ' + (1 === safeCurrentPage ? 'active' : '') + '">';
        paginationHtml += '<a class="page-link" href="javascript:void(0)" data-page="1" data-star="' + star + '" data-product-id="' + productId + '">1</a>';
        paginationHtml += '</li>';
    }

    // Hiển thị dấu "..." nếu currentPage > 3
    if (safeCurrentPage > 3) {
        paginationHtml += '<li class="page-item disabled"><a class="page-link" href="javascript:void(0)">...</a></li>';
    }

    // Hiển thị các trang xung quanh currentPage
    let startPage = Math.max(2, safeCurrentPage - 1);
    let endPage = Math.min(safeTotalPages - 1, safeCurrentPage + 1);

    for (let i = startPage; i <= endPage; i++) {
        if (i !== 1 && i !== safeTotalPages) { // Đảm bảo không hiển thị lại trang 1 và trang cuối
            paginationHtml += '<li class="page-item ' + (i === safeCurrentPage ? 'active' : '') + '">';
            paginationHtml += '<a class="page-link" href="javascript:void(0)" data-page="' + i + '" data-star="' + star + '" data-product-id="' + productId + '">' + i + '</a>';
            paginationHtml += '</li>';
        }
    }

    // Hiển thị dấu "..." nếu currentPage < totalPages - 2
    if (safeCurrentPage < safeTotalPages - 2) {
        paginationHtml += '<li class="page-item disabled"><a class="page-link" href="javascript:void(0)">...</a></li>';
    }

    // Hiển thị 2 trang cuối cùng (nếu cần)
    if (safeTotalPages > 1) {
        let secondLastPage = safeTotalPages - 1;
        if (secondLastPage > endPage) { // Chỉ hiển thị nếu chưa được hiển thị trong vòng lặp trước
            paginationHtml += '<li class="page-item ' + (secondLastPage === safeCurrentPage ? 'active' : '') + '">';
            paginationHtml += '<a class="page-link" href="javascript:void(0)" data-page="' + secondLastPage + '" data-star="' + star + '" data-product-id="' + productId + '">' + secondLastPage + '</a>';
            paginationHtml += '</li>';
        }

        paginationHtml += '<li class="page-item ' + (safeTotalPages === safeCurrentPage ? 'active' : '') + '">';
        paginationHtml += '<a class="page-link" href="javascript:void(0)" data-page="' + safeTotalPages + '" data-star="' + star + '" data-product-id="' + productId + '">' + safeTotalPages + '</a>';
        paginationHtml += '</li>';
    }

    // Nút "Next"
    if (safeCurrentPage < safeTotalPages) {
        paginationHtml += '<li class="page-item"><a class="page-link" href="javascript:void(0)" data-page="' + (safeCurrentPage + 1) + '" data-star="' + star + '" data-product-id="' + productId + '">»</a></li>';
    } else {
        paginationHtml += '<li class="page-item disabled"><a class="page-link" href="javascript:void(0)">»</a></li>';
    }

    paginationHtml += '</ul>';

    // Ghi HTML vào #pagination-container
    $('#pagination-container').html(paginationHtml);
    console.log("Pagination HTML:", paginationHtml); // Log để kiểm tra HTML được tạo
}
// Sử dụng event delegation để xử lý sự kiện click
$(document).on('click', '.page-link', function (event) {
    event.preventDefault(); // Ngăn chặn hành vi mặc định

    // Lấy dữ liệu từ data-* attributes
    const page = $(this).data('page');
    const star = $(this).data('star');
    const productId = $(this).data('product-id');

    // Gọi hàm tải đánh giá
    loadReviews(star, page, productId);
});
function changePage(newPage, star, productId) {
    loadReviews(star, newPage, productId);
}