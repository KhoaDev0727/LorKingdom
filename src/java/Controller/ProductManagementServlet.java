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
import java.io.File;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Truong Van Khang - CE181852
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
            List<Sex> listGenders = s.getAllSexes();
            List<PriceRange> listPriceRanges = p.getAllPriceRanges();
            List<Brand> listBrands = b.getAllBrands();
            List<Age> listAges = a.getAllAges();
            List<Material> listMaterials = m.getAllMaterials();
            List<Origin> listOrigins = originDAO.getAllOrigins();
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
        try {
            // Lấy thông tin sản phẩm từ form
            // Lấy thông tin sản phẩm từ form
            String productName = request.getParameter("productName");
            Integer category = Integer.parseInt(request.getParameter("category"));
            Integer gender = Integer.parseInt(request.getParameter("gender"));
            Integer priceRange = Integer.parseInt(request.getParameter("priceRange"));
            Integer brand = Integer.parseInt(request.getParameter("brand"));
            Integer ageGroup = Integer.parseInt(request.getParameter("ageGroup"));
            Integer origin = Integer.parseInt(request.getParameter("origin")); // Lấy từ request
            Integer material = Integer.parseInt(request.getParameter("material"));
            String description = request.getParameter("description");
            double price = Double.parseDouble(request.getParameter("price"));
            int stockQuantity = Integer.parseInt(request.getParameter("stockQuantity"));
            String SKU = MyUtils.generateProductID();

            // Tạo đối tượng sản phẩm
            Product p = new Product(SKU, category, material, ageGroup, gender, priceRange, brand, origin, productName, price, stockQuantity, description);

            // Tạo danh sách lưu đường dẫn ảnh
            List<String> imagePaths = new ArrayList<>();
            String uploadPath = getServletContext().getRealPath("/uploads");
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Lưu ảnh chính
            Part mainImagePart = request.getPart("mainImageUpload");
            System.out.println(mainImagePart.getName());
            if (mainImagePart != null && mainImagePart.getSize() > 0) {
                String mainImageFileName = handleImageProduct.generateUniqueFileName(mainImagePart);
                String mainImageFilePath = handleImageProduct.saveFile(mainImagePart, uploadPath, mainImageFileName);
                imagePaths.add(mainImageFilePath);
            }

            // Lưu ảnh chi tiết
            for (Part part : request.getParts()) {
                System.out.println(part.getName());
                if (part.getName().equals("detailImages") && part.getSize() > 0) {
                    String detailImageFileName = handleImageProduct.generateUniqueFileName(part);
                    String detailImageFilePath = handleImageProduct.saveFile(part, uploadPath, detailImageFileName);
                    System.out.println(detailImageFilePath);
                    imagePaths.add(detailImageFilePath);
                    System.out.println(detailImageFilePath);
                }
            }
            // Thêm sản phẩm và ảnh vào DB
            boolean addRow = ProductDAO.addProduct(p, imagePaths, 1);
            if (addRow) {
                response.sendRedirect("AddNewProduct.jsp?updateSuccess=true");
            } else {
                request.setAttribute("errorMessage", "Failed to add product to database.");
                request.getRequestDispatcher("AddNewProduct.jsp").forward(request, response);
            }

        } catch (Exception e) {
            request.setAttribute("errorMessage", "Failed to add product can not empty.");
            request.getRequestDispatcher("AddNewProduct.jsp").forward(request, response);
        }
    }

}
