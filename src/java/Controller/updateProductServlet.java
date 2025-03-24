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
import DAO.UpdateProductDataLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;

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
            System.out.println("product.description = " + product.getDescription());
            String priceStr = String.valueOf(product.getPrice());
            int dotIndex = priceStr.indexOf('.');
            if (dotIndex != -1) {
                priceStr = priceStr.substring(0, dotIndex);
            }
            List<Category> listCategories = categoryDAO.getActiveCategoriesWithActiveSuperCategory();
            List<Brand> listBrands = brandDAO.getActiveBrand();
            List<Age> listAges = ageDAO.getAllAges();
            List<Sex> listGenders = sexDAO.getActiveSex();
            List<Material> listMaterials = materialDAO.getAllActiveMaterials();
            List<PriceRange> listPriceRanges = priceRangeDAO.getAllActivePriceRanges();
            List<Origin> listOrigins = originDAO.getAllActiveOrigins();

            // Đưa các danh sách và đối tượng sản phẩm vào request
            request.setAttribute("priceTruncated", priceStr);
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
            int productID = Integer.parseInt(request.getParameter("productID"));
            String productName = request.getParameter("productName");
            String priceStr = request.getParameter("price");
            String stockQuantityStr = request.getParameter("stockQuantity");
            String description = request.getParameter("description");

            int category = Integer.parseInt(request.getParameter("category"));
            int gender = Integer.parseInt(request.getParameter("gender"));
            int priceRange = Integer.parseInt(request.getParameter("priceRange"));
            int brand = Integer.parseInt(request.getParameter("brand"));
            int ageGroup = Integer.parseInt(request.getParameter("ageGroup"));
            int origin = Integer.parseInt(request.getParameter("origin"));
            int material = Integer.parseInt(request.getParameter("material"));
            String SKU = request.getParameter("SKU");

            if (productName == null || productName.trim().isEmpty()
                    || priceStr == null || priceStr.trim().isEmpty()
                    || stockQuantityStr == null || stockQuantityStr.trim().isEmpty()
                    || description == null || description.trim().isEmpty()) {

                session.setAttribute("errorMessage", "Vui lòng nhập đầy đủ thông tin bắt buộc!");
                UpdateProductDataLoader.loadDataForUpdate(request, productID);
                request.getRequestDispatcher("UpdateProduct.jsp").forward(request, response);
                return;
            }
            if (productName.length() > 250) {
                session.setAttribute("errorMessage", "Tên sản phẩm chỉ không được dài quá 250 kí tự");
                UpdateProductDataLoader.loadDataForUpdate(request, productID);
                request.getRequestDispatcher("UpdateProduct.jsp").forward(request, response);
                return;
            }
            String plainTextContent = Jsoup.parse(description).text();
            if (plainTextContent == null || plainTextContent.trim().isEmpty()) {
                request.getSession().setAttribute("errorMessage", "Miêu tả không được để trống.");
                UpdateProductDataLoader.loadDataForUpdate(request, productID);
                request.getRequestDispatcher("UpdateProduct.jsp").forward(request, response);
                return;
            }
            String namePattern = "^[\\p{L}\\d\\s]+$";
            if (!productName.matches(namePattern)) {
                session.setAttribute("errorMessage", "Tên sản phẩm chỉ được chứa chữ cái (có dấu) và khoảng trắng và số.");
                UpdateProductDataLoader.loadDataForUpdate(request, productID);
                request.getRequestDispatcher("UpdateProduct.jsp").forward(request, response);
                return;
            }
            String cleanedPriceStart = priceStr.replaceAll("[^\\d]", "");
            if (cleanedPriceStart.length() < 4) {
                UpdateProductDataLoader.loadDataForUpdate(request, productID);
                session.setAttribute("errorMessage", "Giá trị phải có ít nhất 4 chữ số.");
                UpdateProductDataLoader.loadDataForUpdate(request, productID);
                request.getRequestDispatcher("UpdateProduct.jsp").forward(request, response);
                return;
            }
            if (ProductDAO.isProductNameExists(productName, 0)) {
                session.setAttribute("errorMessage", "Tên sản phẩm đã tồn tại trong hệ thống!");
                UpdateProductDataLoader.loadDataForUpdate(request, productID);
                request.getRequestDispatcher("UpdateProduct.jsp").forward(request, response);
                return;
            }
            priceStr = priceStr.replace(".", "").replace(",", ".");
            if (!priceStr.matches("^\\d+(\\.\\d+)?$")) {
                session.setAttribute("errorMessage", "Giá sản phẩm phải là số.");
                UpdateProductDataLoader.loadDataForUpdate(request, productID);
                request.getRequestDispatcher("UpdateProduct.jsp").forward(request, response);
                return;
            }
            if (!stockQuantityStr.matches("^\\d+$")) {
                session.setAttribute("errorMessage", "Số lượng sản phẩm phải là số nguyên.");
                UpdateProductDataLoader.loadDataForUpdate(request, productID);
                request.getRequestDispatcher("UpdateProduct.jsp").forward(request, response);
                return;
            }
            double price = Double.parseDouble(priceStr);
            int stockQuantity = Integer.parseInt(stockQuantityStr);

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
            if (price > 50000000) {
                session.setAttribute("errorMessage", "Giá sản phẩm không được vượt quá 50,000,000.");
                UpdateProductDataLoader.loadDataForUpdate(request, productID);
                request.getRequestDispatcher("UpdateProduct.jsp").forward(request, response);
                return;
            }

            if (stockQuantity > 200) {
                session.setAttribute("errorMessage", "Số lượng sản phẩm không được vượt quá 200");
                UpdateProductDataLoader.loadDataForUpdate(request, productID);
                request.getRequestDispatcher("UpdateProduct.jsp").forward(request, response);
                return;
            }
            UploadImageProduct handleImageProduct = new UploadImageProduct();
            String uploadPath = getServletContext().getRealPath("/uploads");
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            Part mainImagePart = request.getPart("mainImageUpload");
            List<Part> detailImageParts = new ArrayList<>();
            for (Part part : request.getParts()) {
                if ("detailImages".equals(part.getName()) && part.getSize() > 0) {
                    detailImageParts.add(part);
                }
            }

            if (mainImagePart.getSize() > 0) {
                String mainImageFileName = handleImageProduct.generateUniqueFileName(mainImagePart);
                String mainImageFilePath = handleImageProduct.saveFile(mainImagePart, uploadPath, mainImageFileName);
                ProductImage currentMainImage = ProductImageDAO.getMainImage(productID);
                if (currentMainImage != null) {
                    boolean imgUpdated = ProductImageDAO.updateProductImage(currentMainImage.getImageID(), mainImageFilePath, 1);
                    if (!imgUpdated) {
                        System.out.println("Lỗi cập nhật ảnh chính.");
                    }
                } else {
                    boolean imgAdded = ProductImageDAO.addProductImage(productID, mainImageFilePath, 1);
                    if (!imgAdded) {
                        System.out.println("Lỗi thêm ảnh chính.");
                    }
                }
            }

            for (Part part : detailImageParts) {
                String detailImageFileName = handleImageProduct.generateUniqueFileName(part);
                String detailImageFilePath = handleImageProduct.saveFile(part, uploadPath, detailImageFileName);
                boolean detailAdded = ProductImageDAO.addProductImage(productID, detailImageFilePath, 0);
                if (!detailAdded) {
                    System.out.println("Lỗi thêm ảnh chi tiết: " + detailImageFileName);
                }
            }

            boolean updated = ProductDAO.updateProduct(p);
            System.out.println("=== [DEBUG] Received update form data ===");
            System.out.println("productID = " + productID);
            System.out.println("productName = " + productName);
            System.out.println("priceStr = " + priceStr);
            System.out.println("stockQuantityStr = " + stockQuantityStr);
            System.out.println("description = " + description);
            System.out.println("category = " + category);
            System.out.println("gender = " + gender);
            System.out.println("priceRange = " + priceRange);
            System.out.println("brand = " + brand);
            System.out.println("ageGroup = " + ageGroup);
            System.out.println("origin = " + origin);
            System.out.println("material = " + material);
            System.out.println("SKU = " + SKU);

            if (updated) {
                session.setAttribute("successMessage", "Cập nhật sản phẩm thành công!");
                response.sendRedirect("ProductServlet?action=list");
            } else {
                session.setAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật sản phẩm (DB update fail).");
                UpdateProductDataLoader.loadDataForUpdate(request, productID);
                request.getRequestDispatcher("UpdateProduct.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật sản phẩm (Exception).");
            String productIDStr = request.getParameter("productID");
            if (productIDStr != null) {
                int productID = Integer.parseInt(productIDStr);
                try {
                    UpdateProductDataLoader.loadDataForUpdate(request, productID);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(updateProductServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
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
