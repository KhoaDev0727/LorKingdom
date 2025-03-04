/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.ProductDAO;
import DAO.ProductImageDAO;
import DAO.CategoryDAO;
import DAO.AgeDAO;
import DAO.BrandDAO;
import DAO.MaterialDAO;
import DAO.OriginDAO;
import DAO.PriceRangeDAO;
import DAO.SexDAO;

import Model.Age;
import Model.Brand;
import Model.Category;
import Model.Material;
import Model.Origin;
import Model.PriceRange;
import Model.Product;
import Model.ProductImage;
import Model.Sex;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin1
 */
@MultipartConfig
public class updateProductServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet updateProductServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet updateProductServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy productID từ request
        try {

            int productID = Integer.parseInt(request.getParameter("productID"));

            // Lấy thông tin sản phẩm theo productID
            Product product = ProductDAO.getProductById(productID);
            request.setAttribute("product", product);

            // Lấy danh sách các thuộc tính cần thiết (Category, Brand, Age, Sex, Material, PriceRange, Origin...)
            CategoryDAO categoryDAO = new CategoryDAO();
            BrandDAO brandDAO = new BrandDAO();
            AgeDAO ageDAO = new AgeDAO();
            SexDAO sexDAO = new SexDAO();
            MaterialDAO materialDAO = new MaterialDAO();
            PriceRangeDAO priceRangeDAO = new PriceRangeDAO();
            OriginDAO originDAO = new OriginDAO();

            List<Category> listCategories = categoryDAO.getAllCategories();
            List<Brand> listBrands = brandDAO.getActiveBrand();
            List<Age> listAges = ageDAO.getAllAges();
            List<Sex> listGenders = sexDAO.getActiveSex();
            List<Material> listMaterials = materialDAO.getAllActiveMaterials();
            List<PriceRange> listPriceRanges = priceRangeDAO.getAllActivePriceRanges();
            List<Origin> listOrigins = originDAO.getAllActiveOrigins();

            // Đưa các danh sách và đối tượng sản phẩm vào request
            request.setAttribute("Categories", listCategories);
            request.setAttribute("brands", listBrands);
            request.setAttribute("ages", listAges);
            request.setAttribute("sexes", listGenders);
            request.setAttribute("materials", listMaterials);
            request.setAttribute("priceRanges", listPriceRanges);
            request.setAttribute("listOrigin", listOrigins);

            // Forward sang trang UpdateProduct.jsp để hiển thị form cập nhật
            request.getRequestDispatcher("UpdateProduct.jsp").forward(request, response);
        } catch (Exception e) {
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            // Kiểm tra các trường bắt buộc không được rỗng
            String productName = request.getParameter("productName");
            String priceStr = request.getParameter("price");
            String stockQuantityStr = request.getParameter("stockQuantity");
            String description = request.getParameter("description");

            if (productName == null || productName.trim().isEmpty()
                    || priceStr == null || priceStr.trim().isEmpty()
                    || stockQuantityStr == null || stockQuantityStr.trim().isEmpty()
                    || description == null || description.trim().isEmpty()) {
                request.getSession().setAttribute("errorMessage", "Vui lòng nhập đầy đủ thông tin bắt buộc!");
                request.getRequestDispatcher("UpdateProduct.jsp").forward(request, response);
                return;
            }

            // Lấy các tham số còn lại
            int productID = Integer.parseInt(request.getParameter("productID"));
            int category = Integer.parseInt(request.getParameter("category"));
            int gender = Integer.parseInt(request.getParameter("gender"));
            int priceRange = Integer.parseInt(request.getParameter("priceRange"));
            int brand = Integer.parseInt(request.getParameter("brand"));
            int ageGroup = Integer.parseInt(request.getParameter("ageGroup"));
            int origin = Integer.parseInt(request.getParameter("origin"));
            int material = Integer.parseInt(request.getParameter("material"));
            String SKU = request.getParameter("SKU");

            // Nếu cần: kiểm tra định dạng số cho price và stockQuantity
            // Chuyển đổi price (có thể định dạng 1.000,50 => 1000.50)
            String namePattern = "^[\\p{L}\\s]+$";
            if (!productName.matches(namePattern)) {
                session.setAttribute("errorMessage", "Tên sản phẩm chỉ được chứa chữ cái (có dấu) và khoảng trắng.");
                request.getRequestDispatcher("AddNewProduct.jsp").forward(request, response);
                return;
            }

            // Kiểm tra tên sản phẩm có tồn tại chưa
            if (ProductDAO.isProductNameExists(productName, 0)) {
                session.setAttribute("errorMessage", "Tên sản phẩm đã tồn tại trong hệ thống!");
                request.getRequestDispatcher("AddNewProduct.jsp").forward(request, response);
                return;
            }
            priceStr = priceStr.replace(".", "").replace(",", ".");
            if (!priceStr.matches("^\\d+(\\.\\d+)?$")) {
                request.getSession().setAttribute("errorMessage", "Giá sản phẩm phải là số.");
                request.getRequestDispatcher("UpdateProduct.jsp").forward(request, response);
                return;
            }
            if (!stockQuantityStr.matches("^\\d+$")) {
                request.getSession().setAttribute("errorMessage", "Số lượng sản phẩm phải là số nguyên.");
                request.getRequestDispatcher("UpdateProduct.jsp").forward(request, response);
                return;
            }
            double price = Double.parseDouble(priceStr);
            int stockQuantity = Integer.parseInt(stockQuantityStr);

            // Debug thông tin
            System.out.println("----- [DEBUG] updateProductServlet.doPost() -----");
            System.out.println("productID = " + productID);
            System.out.println("productName = " + productName);
            System.out.println("category = " + category);
            System.out.println("gender = " + gender);
            System.out.println("priceRange = " + priceRange);
            System.out.println("brand = " + brand);
            System.out.println("ageGroup = " + ageGroup);
            System.out.println("origin = " + origin);
            System.out.println("material = " + material);
            System.out.println("description = " + description);
            System.out.println("price = " + price);
            System.out.println("stockQuantity = " + stockQuantity);
            System.out.println("SKU = " + SKU);
            System.out.println("--------------------------------");

            // Tạo đối tượng sản phẩm với các thông tin cập nhật
            Product p = new Product();
            p.setProductID(productID);
            p.setSKU(SKU);
            p.setName(productName);
            p.setCategoryID(category);
            p.setSexID(gender);
            p.setPriceRangeID(priceRange);
            p.setBrandID(brand);
            p.setAgeID(ageGroup);
            p.setOriginID(origin);
            p.setMaterialID(material);
            p.setDescription(description);
            p.setPrice(price);
            p.setQuantity(stockQuantity);

            // Xử lý upload ảnh
            UploadImageProduct handleImageProduct = new UploadImageProduct();
            String uploadPath = getServletContext().getRealPath("/uploads");
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Xử lý ảnh chính
            Part mainImagePart = request.getPart("mainImageUpload");
            if (mainImagePart != null && mainImagePart.getSize() > 0) {
                String mainImageFileName = handleImageProduct.generateUniqueFileName(mainImagePart);
                String mainImageFilePath = handleImageProduct.saveFile(mainImagePart, uploadPath, mainImageFileName);

                // Lấy ảnh chính hiện tại của sản phẩm từ CSDL
                ProductImage currentMainImage = ProductImageDAO.getMainImage(productID);
                if (currentMainImage != null) {
                    // Nếu có ảnh chính, cập nhật lại
                    boolean imgUpdated = ProductImageDAO.updateProductImage(currentMainImage.getImageID(), mainImageFilePath, 1);
                    if (!imgUpdated) {
                        System.out.println("Lỗi cập nhật ảnh chính.");
                    }
                } else {
                    // Nếu chưa có ảnh chính, thêm mới
                    boolean imgAdded = ProductImageDAO.addProductImage(productID, mainImageFilePath, 1);
                    if (!imgAdded) {
                        System.out.println("Lỗi thêm ảnh chính.");
                    }
                }
            }

            // Xử lý ảnh chi tiết (nếu có)
            for (Part part : request.getParts()) {
                if (part.getName().equals("detailImages") && part.getSize() > 0) {
                    String detailImageFileName = handleImageProduct.generateUniqueFileName(part);
                    String detailImageFilePath = handleImageProduct.saveFile(part, uploadPath, detailImageFileName);
                    // Thêm ảnh chi tiết với IsMain = 0
                    boolean detailAdded = ProductImageDAO.addProductImage(productID, detailImageFilePath, 0);
                    if (!detailAdded) {
                        System.out.println("Lỗi thêm ảnh chi tiết: " + detailImageFileName);
                    }
                }
            }

            // Cập nhật thông tin sản phẩm (các thuộc tính khác)
            boolean updated = ProductDAO.updateProduct(p);

            if (updated) {
                request.getSession().setAttribute("successMessage", "Cập nhật sản phẩm thành công!");
                response.sendRedirect("ProductServlet?&action=list");
            } else {
                request.getSession().setAttribute("errorMessage", "Cập nhật sản phẩm thất bại!");
                request.getRequestDispatcher("UpdateProduct.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật sản phẩm.");
            request.getRequestDispatcher("UpdateProduct.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
