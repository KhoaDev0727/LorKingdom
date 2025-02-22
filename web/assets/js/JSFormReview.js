/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

document.addEventListener('DOMContentLoaded', function () {
    const stars = document.querySelectorAll('#star-rating .fa-star');
    const ratingText = document.getElementById('rating-text');
    const ratingInput = document.getElementById('rating-input');

    stars.forEach(star => {
        star.addEventListener('click', function () {
            const rating = this.getAttribute('data-rating');
            updateRating(rating);
            // Lưu giá trị số sao vào input ẩn
            ratingInput.value = rating;
            console.log("Số sao đã chọn:", rating);
        });
    });

    function updateRating(rating) {
        stars.forEach(star => {
            if (star.getAttribute('data-rating') <= rating) {
                star.classList.add('active');
            } else {
                star.classList.remove('active');
            }
        });

        // Cập nhật văn bản đánh giá
        switch (rating) {
            case '1':
                ratingText.textContent = 'Poor';
                break;
            case '2':
                ratingText.textContent = 'Fair';
                break;
            case '3':
                ratingText.textContent = 'Good';
                break;
            case '4':
                ratingText.textContent = 'Very Good';
                break;
            case '5':
                ratingText.textContent = 'Excellent';
                break;
            default:
                ratingText.textContent = 'Excellent';
        }
    }
});

function updateFileName(input) {
    const fileNameSpan = document.getElementById('file-name');
    if (input.files.length > 0) {
        fileNameSpan.textContent = input.files[0].name; // Display the selected file name
    } else {
        fileNameSpan.textContent = 'Add Image'; // Reset to default text if no file is selected
    }
}