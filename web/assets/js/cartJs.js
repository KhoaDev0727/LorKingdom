
function formatNumber(num) {
    return num.toLocaleString("vi-VN");
}

function deleteItem(productId) {
    if (confirm('Bạn có chắc muốn xóa sản phẩm này?')) {
        $.ajax({
            url: 'Cart',
            type: 'GET',
            data: {
                productID: productId,
                action: 'delete'
            },
            success: function (response) {
                $('button[onclick*="' + productId + '"]').closest('tr').remove();
                $('.total-amount').text(response.totalMoney + ' VND');
                $('.cart-header span:last').text(response.cartSize);
                if (response.cartSize == 0) {
                    $('tbody').html('<tr><td colspan="7" class="empty-cart">Giỏ hàng của bạn đang trống</td></tr>');
                }
            },
            error: function (xhr) {
                alert('Có lỗi xảy ra: ' + xhr.responseText);
            }
        });
    }
}

function deleteAllItems() {
    if (confirm('Bạn có chắc muốn xóa tất cả sản phẩm trong giỏ hàng?')) {
        $.ajax({
            url: 'Cart', // Đảm bảo URL trỏ đúng đến Servlet
            type: 'GET',
            data: {
                action: 'deleteAllItem' // Khớp với tên action trong Servlet
            },
            success: function (response) {
                // Xóa tất cả các hàng trong bảng giỏ hàng
                $('tbody').html('<tr><td colspan="7" class="empty-cart">Giỏ hàng của bạn đang trống</td></tr>');
                
                // Cập nhật tổng tiền và số lượng sản phẩm trong giỏ hàng
                $('.total-amount').text('0 VND');
                $('.cart-header span:last').text('0');
            },
            error: function (xhr) {
                alert('Có lỗi xảy ra: ' + xhr.responseText);
            }
        });
    }
}