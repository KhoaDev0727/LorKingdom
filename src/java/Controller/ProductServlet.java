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
import com.oracle.wls.shaded.org.apache.bcel.generic.AALOAD;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author admin1
 */
public class ProductServlet extends HttpServlet {

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
            out.println("<title>Servlet ProductServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProductServlet at " + request.getContextPath() + "</h1>");
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
        handleRequest(request, response);
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
        handleRequest(request, response);
    }

    ProductDAO productDAO = new ProductDAO();

    // Xử lý request chung cho cả GET và POST
    private void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        // Kiểm tra đăng nhập hay quyền truy cập nếu cần...

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        System.out.println("=== [DEBUG] action = " + action);

        try {
            switch (action) {
                case "list":
                    listProducts(request, response);
                    break;
                case "listDeleted":
                    listDeletedProducts(request, response);
                    break;
                case "softDelete":
                    softDeleteProduct(request, response);
                    break;
                case "hardDelete":
                    hardDeleteProduct(request, response);
                    break;
                case "restore":
                    restoreProduct(request, response);
                    break;
                case "search":
                    searchProduct(request, response);
                    break;
                default:
                    listProducts(request, response);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            session.setAttribute("errorMessage", "Error: " + ex.getMessage());
            response.sendRedirect("ProductServlet?action=list&showErrorModal=true");
        }
    }

    private void listProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, ClassNotFoundException {
        int page = 1;
        int pageSize = 10;

        if (request.getParameter("page") != null) {
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        int totalProducts = new ProductDAO().getTotalProductsCount();
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

        List<Product> products = new ProductDAO().getProductsByPage(page, pageSize);
        List<ProductImage> mainImages = ProductImageDAO.getMainProductImages();
        request.setAttribute("mainImages", mainImages);
        request.setAttribute("products", products);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        // forward có thể chứa chuỗi truy vấn đã có sẵn (chẳng hạn "ProductServlet?action=list")
        request.setAttribute("forward", "ProductServlet?action=list");

        request.getRequestDispatcher("ProductManagement.jsp").forward(request, response);
    }

    private void listDeletedProducts(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        String productIDStr = request.getParameter("productID");
        List<Product> products = productDAO.getDeletedProducts();
        List<ProductImage> mainImages = ProductImageDAO.getMainProductImages();

        request.setAttribute("products", products);
        request.setAttribute("mainImages", mainImages);

        request.getRequestDispatcher("ProductManagement.jsp").forward(request, response);
    }

    private void softDeleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {
        int productID = Integer.parseInt(request.getParameter("productID"));
        productDAO.softDeleteProduct(productID);
        request.getSession().setAttribute("successMessage", "Sản phẩm đã được đã được đưa vào thùng rác thành công.");
        response.sendRedirect("ProductServlet?action=list");
    }

    // Xóa cứng sản phẩm
    private void hardDeleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {
        int productID = Integer.parseInt(request.getParameter("productID"));
        productDAO.hardDeleteProduct(productID);
        request.getSession().setAttribute("successMessage", "Sản phẩm đã được xóa thành công.");
        response.sendRedirect("ProductServlet?action=listDeleted");
    }

    private void restoreProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {
        String productIDStr = request.getParameter("productID");
        if (productIDStr == null || productIDStr.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "ID sản phẩm  không hợp lệ.");
            response.sendRedirect("ProductServlet?action=list&showErrorModal=true");
            return;
        }
        int productID = Integer.parseInt(productIDStr);
        productDAO.restoreProduct(productID);
        request.getSession().setAttribute("successMessage", "Sản phẩm đã được khôi phục thành công.");
        response.sendRedirect("ProductServlet?action=list&showSuccessModal=true");
    }

    // Tìm kiếm sản phẩm theo từ khóa
    private void searchProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        String keyword = request.getParameter("search");
        if (keyword == null || keyword.trim().isEmpty()) {
            listProducts(request, response);
            return;
        }
        List<ProductImage> mainImages = ProductImageDAO.getMainProductImages();
        request.setAttribute("mainImages", mainImages);

        List<Product> products = productDAO.searchProducts(keyword.trim().toLowerCase());
        request.setAttribute("products", products);
        request.getRequestDispatcher("ProductManagement.jsp").forward(request, response);
    }
    // Trong ProductServlet.java
    private UploadImageProduct handleImageProduct = new UploadImageProduct();

}
