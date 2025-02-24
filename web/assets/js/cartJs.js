 function updateQuantity(productId, action, phep) {
            console.log(phep)
            console.log(productId)
            $.ajax({
                url: 'Cart',
                type: 'POST',
                data: {
                    productID: productId,
                    action: action,
                    phep: phep
                },
                dataType: "json",
                success: function (response) {
                    if (response && response.newQuantity !== undefined && response.itemTotal !== undefined) {
                        var row = $('button[onclick*="' + productId + '"]').closest('tr');
                        row.find('.quantity-input').val(response.newQuantity);
                        row.find('.total-price').text(formatNumber(response.itemTotal) + ' VND');
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
                    alert("Lỗi: " + xhr.status + " - " + xhr.statusText + "\nChi tiết: " + xhr.responseText);
                }
            });
        }

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