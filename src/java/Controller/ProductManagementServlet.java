/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.util.List;
import Model.*;
import DAO.*;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;

/**
 *
 * @author Le Trong Luan - CE181151
 */
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class ProductManagementServlet extends HttpServlet {

    private UploadImageProduct handleImageProduct = new UploadImageProduct();
    private CategoryDAO c = new CategoryDAO();
    private SexDAO s = new SexDAO();
    private PriceRangeDAO p = new PriceRangeDAO();
    private BrandDAO b = new BrandDAO();
    private AgeDAO a = new AgeDAO();
    private MaterialDAO m = new MaterialDAO();
    OriginDAO originDAO = new OriginDAO();
    private String mainImagePath = null;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Category> listCategorys = c.getAllCategories();
            List<Sex> listGenders = s.getActiveSex();
            List<PriceRange> listPriceRanges = p.getAllActivePriceRanges();
            List<Brand> listBrands = b.getActiveBrand();
            List<Age> listAges = a.getAllAges();
            List<Material> listMaterials = m.getAllActiveMaterials();
            List<Origin> listOrigins = originDAO.getAllActiveOrigins();
            request.setAttribute("ages", listAges);
            request.setAttribute("sexes", listGenders);
            request.setAttribute("Categories", listCategorys);
            request.setAttribute("priceRanges", listPriceRanges);
            request.setAttribute("materials", listMaterials);
            request.setAttribute("brands", listBrands);
            request.setAttribute("listOrigin", listOrigins);
            request.getRequestDispatcher("AddNewProduct.jsp").forward(request, response);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Tạo session để lưu thông báo (nếu muốn hiển thị qua session)
        HttpSession session = request.getSession();

        // Nạp lại dữ liệu cho trang AddNewProduct.jsp
        try {

            List<Category> listCategorys = c.getAllCategories();
            List<Sex> listGenders = s.getActiveSex();
            List<PriceRange> listPriceRanges = p.getAllActivePriceRanges();
            List<Brand> listBrands = b.getActiveBrand();
            List<Age> listAges = a.getAllAges();
            List<Material> listMaterials = m.getAllActiveMaterials();
            List<Origin> listOrigins = originDAO.getAllActiveOrigins();
            request.setAttribute("ages", listAges);
            request.setAttribute("sexes", listGenders);
            request.setAttribute("Categories", listCategorys);
            request.setAttribute("priceRanges", listPriceRanges);
            request.setAttribute("materials", listMaterials);
            request.setAttribute("brands", listBrands);
            request.setAttribute("listOrigin", listOrigins);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // Lấy các tham số đầu vào
            String productName = request.getParameter("productName");
            String priceStartStr = request.getParameter("price");
            String quantityStr = request.getParameter("stockQuantity");
            String description = request.getParameter("description");

            if (productName == null || productName.trim().isEmpty()
                    || priceStartStr == null || priceStartStr.trim().isEmpty()
                    || quantityStr == null || quantityStr.trim().isEmpty()
                    || description == null || description.trim().isEmpty()) {
                session.setAttribute("errorMessage", "Vui lòng nhập đầy đủ thông tin bắt buộc!");
                request.setAttribute("enteredProductName", productName);
                request.setAttribute("enteredPrice", priceStartStr);
                request.setAttribute("enteredQuantity", quantityStr);
                request.setAttribute("enteredCategory", request.getParameter("category"));
                request.setAttribute("enteredGender", request.getParameter("gender"));
                request.setAttribute("enteredPriceRange", request.getParameter("priceRange"));
                request.setAttribute("enteredBrand", request.getParameter("brand"));
                request.setAttribute("enteredAgeGroup", request.getParameter("ageGroup"));
                request.setAttribute("enteredMaterial", request.getParameter("material"));
                request.setAttribute("enteredOrigin", request.getParameter("origin"));
                request.getRequestDispatcher("AddNewProduct.jsp").forward(request, response);
                return;
            }

            Integer category = Integer.parseInt(request.getParameter("category"));
            Integer gender = Integer.parseInt(request.getParameter("gender"));
            Integer priceRange = Integer.parseInt(request.getParameter("priceRange"));
            Integer brand = Integer.parseInt(request.getParameter("brand"));
            Integer ageGroup = Integer.parseInt(request.getParameter("ageGroup"));
            Integer origin = Integer.parseInt(request.getParameter("origin"));
            Integer material = Integer.parseInt(request.getParameter("material"));

            priceStartStr = priceStartStr.replace(".", "").replace(",", ".");
            if (!priceStartStr.matches("^\\d+(\\.\\d+)?$")) {
                session.setAttribute("errorMessage", "Giá sản phẩm phải là số.");
                request.setAttribute("enteredProductName", productName);
                request.setAttribute("enteredPrice", priceStartStr);
                request.setAttribute("enteredQuantity", quantityStr);
                request.setAttribute("enteredCategory", request.getParameter("category"));
                request.setAttribute("enteredGender", request.getParameter("gender"));
                request.setAttribute("enteredPriceRange", request.getParameter("priceRange"));
                request.setAttribute("enteredBrand", request.getParameter("brand"));
                request.setAttribute("enteredAgeGroup", request.getParameter("ageGroup"));
                request.setAttribute("enteredMaterial", request.getParameter("material"));
                request.setAttribute("enteredOrigin", request.getParameter("origin"));
                request.getRequestDispatcher("AddNewProduct.jsp").forward(request, response);
                return;
            }

            String cleanedPriceStart = priceStartStr.replaceAll("[^\\d]", "");
            if (cleanedPriceStart.length() < 4) {
                session.setAttribute("errorMessage", "Giá tiền phải có ít nhất 4 chữ số.");
                request.setAttribute("enteredProductName", productName);
                request.setAttribute("enteredPrice", priceStartStr);
                request.setAttribute("enteredQuantity", quantityStr);
                request.setAttribute("enteredCategory", request.getParameter("category"));
                request.setAttribute("enteredGender", request.getParameter("gender"));
                request.setAttribute("enteredPriceRange", request.getParameter("priceRange"));
                request.setAttribute("enteredBrand", request.getParameter("brand"));
                request.setAttribute("enteredAgeGroup", request.getParameter("ageGroup"));
                request.setAttribute("enteredMaterial", request.getParameter("material"));
                request.setAttribute("enteredOrigin", request.getParameter("origin"));
                request.getRequestDispatcher("AddNewProduct.jsp").forward(request, response);
                return;
            }

            String plainTextContent = Jsoup.parse(description).text();
            if (plainTextContent == null || plainTextContent.trim().isEmpty()) {
                request.getSession().setAttribute("errorMessage", "Miêu tả không được để trống.");
                request.setAttribute("enteredProductName", productName);
                request.setAttribute("enteredPrice", priceStartStr);
                request.setAttribute("enteredQuantity", quantityStr);
                request.setAttribute("enteredCategory", request.getParameter("category"));
                request.setAttribute("enteredGender", request.getParameter("gender"));
                request.setAttribute("enteredPriceRange", request.getParameter("priceRange"));
                request.setAttribute("enteredBrand", request.getParameter("brand"));
                request.setAttribute("enteredAgeGroup", request.getParameter("ageGroup"));
                request.setAttribute("enteredMaterial", request.getParameter("material"));
                request.setAttribute("enteredOrigin", request.getParameter("origin"));
                request.getRequestDispatcher("AddNewProduct.jsp").forward(request, response);
                return;
            }
            if (!quantityStr.matches("^\\d+$")) {
                session.setAttribute("errorMessage", "Số lượng sản phẩm phải là số nguyên.");
                request.setAttribute("enteredProductName", productName);
                request.setAttribute("enteredPrice", priceStartStr);
                request.setAttribute("enteredQuantity", quantityStr);
                request.setAttribute("enteredCategory", request.getParameter("category"));
                request.setAttribute("enteredGender", request.getParameter("gender"));
                request.setAttribute("enteredPriceRange", request.getParameter("priceRange"));
                request.setAttribute("enteredBrand", request.getParameter("brand"));
                request.setAttribute("enteredAgeGroup", request.getParameter("ageGroup"));
                request.setAttribute("enteredMaterial", request.getParameter("material"));
                request.setAttribute("enteredOrigin", request.getParameter("origin"));
                request.getRequestDispatcher("AddNewProduct.jsp").forward(request, response);
                return;
            }

            double priceStart = Double.parseDouble(priceStartStr);
            int stockQuantity = Integer.parseInt(quantityStr);

            if (priceStart > 50000000) {
                session.setAttribute("errorMessage", "Giá sản phẩm không được vượt quá 50,000,000.");
                request.setAttribute("enteredProductName", productName);
                request.setAttribute("enteredPrice", priceStartStr);
                request.setAttribute("enteredQuantity", quantityStr);
                request.setAttribute("enteredCategory", request.getParameter("category"));
                request.setAttribute("enteredGender", request.getParameter("gender"));
                request.setAttribute("enteredPriceRange", request.getParameter("priceRange"));
                request.setAttribute("enteredBrand", request.getParameter("brand"));
                request.setAttribute("enteredAgeGroup", request.getParameter("ageGroup"));
                request.setAttribute("enteredMaterial", request.getParameter("material"));
                request.setAttribute("enteredOrigin", request.getParameter("origin"));
                request.getRequestDispatcher("AddNewProduct.jsp").forward(request, response);
                return;
            }
            if (stockQuantity > 200) {
                session.setAttribute("errorMessage", "Số lượng sản phẩm không được vượt quá 200");
                request.setAttribute("enteredProductName", productName);
                request.setAttribute("enteredPrice", priceStartStr);
                request.setAttribute("enteredQuantity", quantityStr);
                request.setAttribute("enteredGender", request.getParameter("gender"));
                request.setAttribute("enteredPriceRange", request.getParameter("priceRange"));
                request.setAttribute("enteredBrand", request.getParameter("brand"));
                request.setAttribute("enteredAgeGroup", request.getParameter("ageGroup"));
                request.setAttribute("enteredMaterial", request.getParameter("material"));
                request.setAttribute("enteredOrigin", request.getParameter("origin"));
                request.getRequestDispatcher("AddNewProduct.jsp").forward(request, response);
                return;
            }
            if (stockQuantity <= 0) {
                session.setAttribute("errorMessage", "Số lượng sản phẩm không được nhỏ hơn hoặc bằng 0");
                request.setAttribute("enteredProductName", productName);
                request.setAttribute("enteredPrice", priceStartStr);
                request.setAttribute("enteredQuantity", quantityStr);
                request.setAttribute("enteredGender", request.getParameter("gender"));
                request.setAttribute("enteredPriceRange", request.getParameter("priceRange"));
                request.setAttribute("enteredBrand", request.getParameter("brand"));
                request.setAttribute("enteredAgeGroup", request.getParameter("ageGroup"));
                request.setAttribute("enteredMaterial", request.getParameter("material"));
                request.setAttribute("enteredOrigin", request.getParameter("origin"));
                request.getRequestDispatcher("AddNewProduct.jsp").forward(request, response);
                return;
            }
            String namePattern = "^[\\p{L}\\d\\s]+$";

            if (!productName.matches(namePattern)) {
                session.setAttribute("errorMessage", "Tên sản phẩm chỉ được chứa chữ cái (có dấu) và khoảng trắng và số.");
                request.setAttribute("enteredProductName", productName);
                request.setAttribute("enteredPrice", priceStartStr);
                request.setAttribute("enteredQuantity", quantityStr);
                request.setAttribute("enteredCategory", request.getParameter("category"));
                request.setAttribute("enteredGender", request.getParameter("gender"));
                request.setAttribute("enteredPriceRange", request.getParameter("priceRange"));
                request.setAttribute("enteredBrand", request.getParameter("brand"));
                request.setAttribute("enteredAgeGroup", request.getParameter("ageGroup"));
                request.setAttribute("enteredMaterial", request.getParameter("material"));
                request.setAttribute("enteredOrigin", request.getParameter("origin"));
                request.getRequestDispatcher("AddNewProduct.jsp").forward(request, response);
                return;
            }
            if (productName.length() > 250) {
                session.setAttribute("errorMessage", "Tên sản phẩm chỉ không được dài quá 250 kí tự");
                request.setAttribute("enteredProductName", productName);
                request.setAttribute("enteredPrice", priceStartStr);
                request.setAttribute("enteredQuantity", quantityStr);
                request.setAttribute("enteredCategory", request.getParameter("category"));
                request.setAttribute("enteredGender", request.getParameter("gender"));
                request.setAttribute("enteredPriceRange", request.getParameter("priceRange"));
                request.setAttribute("enteredBrand", request.getParameter("brand"));
                request.setAttribute("enteredAgeGroup", request.getParameter("ageGroup"));
                request.setAttribute("enteredMaterial", request.getParameter("material"));
                request.setAttribute("enteredOrigin", request.getParameter("origin"));
                request.getRequestDispatcher("AddNewProduct.jsp").forward(request, response);
                return;
            }

            if (ProductDAO.isProductNameExists(productName, 0)) {
                session.setAttribute("errorMessage", "Tên sản phẩm đã tồn tại trong hệ thống!");
                request.setAttribute("enteredProductName", productName);
                request.setAttribute("enteredPrice", priceStartStr);
                request.setAttribute("enteredQuantity", quantityStr);
                request.setAttribute("enteredCategory", request.getParameter("category"));
                request.setAttribute("enteredGender", request.getParameter("gender"));
                request.setAttribute("enteredPriceRange", request.getParameter("priceRange"));
                request.setAttribute("enteredBrand", request.getParameter("brand"));
                request.setAttribute("enteredAgeGroup", request.getParameter("ageGroup"));
                request.setAttribute("enteredMaterial", request.getParameter("material"));
                request.setAttribute("enteredOrigin", request.getParameter("origin"));
                request.getRequestDispatcher("AddNewProduct.jsp").forward(request, response);
                return;
            }

            String SKU = MyUtils.generateProductID();

            Product p = new Product(
                    SKU,
                    category,
                    material,
                    ageGroup,
                    gender,
                    priceRange,
                    brand,
                    origin,
                    productName,
                    priceStart,
                    stockQuantity,
                    description
            );

            List<String> imagePaths = new ArrayList<>();

            String uploadPath = getServletContext().getRealPath("/uploads");
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            Part mainImagePart = request.getPart("mainImageUpload");
            if (mainImagePart == null || mainImagePart.getSize() <= 0) {
                // Chưa chọn ảnh chính
                session.setAttribute("errorMessage", "Vui lòng chọn ảnh chính (Main Image).");
                request.setAttribute("enteredProductName", productName);
                request.setAttribute("enteredPrice", priceStartStr);
                request.setAttribute("enteredQuantity", quantityStr);
                request.setAttribute("enteredCategory", request.getParameter("category"));
                request.setAttribute("enteredGender", request.getParameter("gender"));
                request.setAttribute("enteredPriceRange", request.getParameter("priceRange"));
                request.setAttribute("enteredBrand", request.getParameter("brand"));
                request.setAttribute("enteredAgeGroup", request.getParameter("ageGroup"));
                request.setAttribute("enteredMaterial", request.getParameter("material"));
                request.setAttribute("enteredOrigin", request.getParameter("origin"));
                request.getRequestDispatcher("AddNewProduct.jsp").forward(request, response);
                return;
            }

            List<Part> detailImageParts = new ArrayList<>();
            for (Part part : request.getParts()) {
                if ("detailImages".equals(part.getName()) && part.getSize() > 0) {
                    detailImageParts.add(part);
                }
            }
            if (detailImageParts.isEmpty()) {
                session.setAttribute("errorMessage", "Vui lòng chọn ít nhất 1 ảnh chi tiết (Detail Images).");
                request.setAttribute("enteredProductName", productName);
                request.setAttribute("enteredPrice", priceStartStr);
                request.setAttribute("enteredQuantity", quantityStr);
                request.setAttribute("enteredGender", request.getParameter("gender"));
                request.setAttribute("enteredPriceRange", request.getParameter("priceRange"));
                request.setAttribute("enteredBrand", request.getParameter("brand"));
                request.setAttribute("enteredAgeGroup", request.getParameter("ageGroup"));
                request.setAttribute("enteredMaterial", request.getParameter("material"));
                request.setAttribute("enteredOrigin", request.getParameter("origin"));
                request.getRequestDispatcher("AddNewProduct.jsp").forward(request, response);
                return;
            }
            if (mainImagePart != null && mainImagePart.getSize() > 0) {
                String mainImageFileName = handleImageProduct.generateUniqueFileName(mainImagePart);
                String mainImageFilePath = handleImageProduct.saveFile(mainImagePart, uploadPath, mainImageFileName);
                imagePaths.add(mainImageFilePath);
            }

            for (Part part : request.getParts()) {
                if (part.getName().equals("detailImages") && part.getSize() > 0) {
                    String detailImageFileName = handleImageProduct.generateUniqueFileName(part);
                    String detailImageFilePath = handleImageProduct.saveFile(part, uploadPath, detailImageFileName);
                    imagePaths.add(detailImageFilePath);
                }
            }

            boolean addRow = ProductDAO.addProduct(p, imagePaths, 1);
            if (addRow) {
                session.setAttribute("successMessage", "Thêm sản phẩm thành công!");
                response.sendRedirect("ProductServlet?&action=list");
            } else {
                request.setAttribute("errorMessage", "Thêm sản phẩm thất bại.");
                request.getRequestDispatcher("AddNewProduct.jsp").forward(request, response);
            }

        } catch (Exception e) {
            request.setAttribute("errorMessage", "Lỗi thêm sản phẩm: " + e.getMessage());
            request.getRequestDispatcher("AddNewProduct.jsp").forward(request, response);
        }
    }
}
