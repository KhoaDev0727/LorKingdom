/* global Swal */


function deleteItem(productId) {
    Swal.fire({
        title: 'Xác nhận',
        text: 'Bạn có chắc muốn xóa sản phẩm này?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Xóa',
        cancelButtonText: 'Hủy'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: 'Cart',
                type: 'GET',
                data: {
                    productID: productId,
                    action: 'delete'
                },
                success: function (response) {
                    $('button[onclick*="' + productId + '"]').closest('tr').remove();
                    $('.total-amount').text(formatNumber(response.totalMoney) + ' VND');
                    $('.cart-header span:last').text(response.cartSize);
                    if (response.cartSize == 0) {
                        $('tbody').html('<tr><td colspan="7" class="empty-cart">Giỏ hàng của bạn đang trống</td></tr>');
                    }
                    Swal.fire({
                        icon: 'success',
                        title: 'Thành công',
                        text: 'Sản phẩm đã được xóa khỏi giỏ hàng!',
                        showConfirmButton: false,
                        timer: 1500
                    });
                },
                error: function (xhr) {
                    let errorMessage = 'Có lỗi xảy ra khi xóa sản phẩm';
                    try {
                        const jsonResponse = JSON.parse(xhr.responseText);
                        if (jsonResponse.error)
                            errorMessage = jsonResponse.error;
                    } catch (e) {
                        errorMessage += ': ' + xhr.responseText;
                    }
                    Swal.fire({
                        icon: 'error',
                        title: 'Lỗi',
                        text: errorMessage,
                        confirmButtonColor: '#d33',
                        confirmButtonText: 'Đã hiểu'
                    });
                }
            });
        }
    });
}

function deleteAllItems() {
    Swal.fire({
        title: 'Xác nhận',
        text: 'Bạn có chắc muốn xóa tất cả sản phẩm trong giỏ hàng?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Xóa tất cả',
        cancelButtonText: 'Hủy'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: 'Cart',
                type: 'GET',
                data: {
                    action: 'deleteAllItem'
                },
                success: function (response) {
                    $('tbody').html('<tr><td colspan="7" class="empty-cart">Giỏ hàng của bạn đang trống</td></tr>');
                    $('.total-amount').text('0 VND');
                    $('.cart-header span:last').text('0');
                    Swal.fire({
                        icon: 'success',
                        title: 'Thành công',
                        text: 'Đã xóa toàn bộ giỏ hàng!',
                        showConfirmButton: false,
                        timer: 1500
                    });
                },
                error: function (xhr) {
                    let errorMessage = 'Có lỗi xảy ra khi xóa giỏ hàng';
                    try {
                        const jsonResponse = JSON.parse(xhr.responseText);
                        if (jsonResponse.error)
                            errorMessage = jsonResponse.error;
                    } catch (e) {
                        errorMessage += ': ' + xhr.responseText;
                    }
                    Swal.fire({
                        icon: 'error',
                        title: 'Lỗi',
                        text: errorMessage,
                        confirmButtonColor: '#d33',
                        confirmButtonText: 'Đã hiểu'
                    });
                }
            });
        }
    });
}

function updateQuantity(productId, action, operation) {
    console.log(operation);
    console.log(productId);
    $.ajax({
        url: 'Cart',
        type: 'POST',
        data: {
            productID: productId,
            action: action,
            operation: operation
        },
        dataType: "json",
        success: function (response) {
            if (response && response.newQuantity !== undefined && response.itemTotal !== undefined) {
                var row = $('button[onclick*="' + productId + '"]').closest('tr');

                // If quantity is 0, remove the row from the table
                if (response.newQuantity === 0) {
                    row.remove();
                    // Check if cart is empty after removal
                    if (response.cartSize === 0) {
                        $('.cart-table tbody').html('<tr><td colspan="7" class="empty-cart">Giỏ hàng của bạn đang trống</td></tr>');
                        $('.delete-all').remove(); // Remove "Delete All" button
                        $('.pay').remove(); // Remove "Thanh Toán" button
                        $('.total-product').text('0');
                    }
                } else {
                    // Update quantity and total price for the item
                    row.find('.quantity-input').val(response.newQuantity);
                    row.find('.total-price').text(formatNumber(response.itemTotal) + ' VND');
                }

                // Update total money and cart size
                if (response.totalMoney !== undefined) {
                    $('.total-amount').text(formatNumber(response.totalMoney) + ' VND');
                }
                $('.cart-header span:last').text(response.cartSize !== undefined ? response.cartSize : 0);
            } else {
                console.error("Lỗi dữ liệu từ server:", response);
            }
        },

        error: function (xhr, status, error) {
            console.error("AJAX Error:", status, error);
            console.error("Server Response:", xhr.responseText);

            // Tạo thông báo lỗi chi tiết hơn
            let errorMessage = 'Có lỗi xảy ra'; // Mặc định nếu không parse được
            try {
                const jsonResponse = JSON.parse(xhr.responseText);
                if (jsonResponse.error) {
                    errorMessage = jsonResponse.error; // Chỉ lấy giá trị của "error"
                }
            } catch (e) {
                console.error("Error parsing JSON:", e);
            }

            Swal.fire({
                icon: 'error',
                title: 'Lỗi',
                text: errorMessage,
                confirmButtonColor: '#d33',
                confirmButtonText: 'Đã hiểu'
            });
        }





    });
}

// Utility function to format numbers
function formatNumber(num) {
    return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}