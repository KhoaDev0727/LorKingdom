const translations = {
    en: {
        introduction: "About Us",
        help: "Help",
        cart: "Cart",
        login: "Login",
        register: "Register",
        newProducts: "New Arrivals",
        products: "Products",
        brands: "Brands",
        promotions: "Promotions",
        membership: "Membership Program",
        categoryAll: "All",
        categoryNew: "New",
        categoryEvents: "Events",
        categoryDiscount: "Discounts",
        addToCart: "Add to Cart",
        exclusiveOnline: "Exclusive Online",
        price: "Price"
    },
    vn: {
        introduction: "Giới Thiệu",
        help: "Trợ giúp",
        cart: "Giỏ hàng",
        login: "Đăng Nhập",
        register: "Đăng Ký",
        newProducts: "Hàng Mới",
        products: "Sản Phẩm",
        brands: "Thương Hiệu",
        promotions: "Khuyến Mãi",
        membership: "Chương Trình Thành Viên",
        categoryAll: "Tất Cả",
        categoryNew: "Hàng Mới",
        categoryEvents: "Sự Kiện",
        categoryDiscount: "Giảm Giá",
        addToCart: "Thêm Vào Giỏ Hàng",
        exclusiveOnline: "Độc Quyền Online",
        price: "Giá"
    }
};

function changeLanguage(lang) {
    document.querySelectorAll("[data-key]").forEach(element => {
        const key = element.getAttribute("data-key");
        element.textContent = translations[lang][key];
    });
    localStorage.setItem("language", lang); // Lưu ngôn ngữ vào localStorage để duy trì sau khi reload
}

// Tải ngôn ngữ được lưu trước đó (nếu có)
const savedLanguage = localStorage.getItem("language") || "vn";
changeLanguage(savedLanguage);

