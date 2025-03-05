/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.CategoryDAO;
import DAO.SuperCategoryDAO;
import DBConnect.DBConnection;
import Model.Category;
import Model.SuperCategory;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin1
 */
public class CategoryServlet extends HttpServlet {

    SuperCategoryDAO superCategoryDAO = new SuperCategoryDAO();
    CategoryDAO categoryDAO = new CategoryDAO();

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
            out.println("<title>Servlet CategoryServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CategoryServlet at " + request.getContextPath() + "</h1>");
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
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

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        try {
            Integer roleID = (Integer) session.getAttribute("roleID");
            if (roleID == null) {
                response.sendRedirect(request.getContextPath() + "/Admin/loginPage.jsp"); // Chưa đăng nhập, chuyển hướng đến trang login
                return;
            }
            if (action == null || action.equals("list")) {
                listCategories(request, response);
            } else {
                switch (action) {
                    case "add":
                        addCategory(request, response);
                        break;
                    case "delete":
                        deleteCategory(request, response);
                        break;
                    case "hardDelete":
                        hardDeleteCategory(request, response);
                        break;
                    case "update":
                        updateCategory(request, response);
                        break;
                    case "search":
                        searchCategory(request, response);
                        break;
                    case "restore":
                        restoreCategory(request, response);
                        break;
                    case "listDeleted": // Thêm case cho danh mục đã xóa
                        listDeletedCategories(request, response);
                        break;
                    default:
                        listCategories(request, response);
                        break;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void listCategories(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        List<Category> categories = categoryDAO.getAllCategories();
        List<SuperCategory> superCategories = superCategoryDAO.getActiveSuperCategories();

        request.setAttribute("superCategories", superCategories);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("CategoryManagement.jsp").forward(request, response);
    }

    private void addCategory(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        String name = request.getParameter("categoryName");
        String superCategoryIDStr = request.getParameter("superCategoryID");

        if (name == null || name.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Tên danh mục không được để trống.");
            response.sendRedirect("CategoryServlet?action=list&showErrorModal=true");
            return;
        }

        name = name.trim();

        if (name.length() > 255) {
            request.getSession().setAttribute("errorMessage", "Tên danh mục quá dài. Tối đa 255 ký tự.");
            response.sendRedirect("CategoryServlet?action=list&showErrorModal=true");
            return;
        }

        if (!name.matches("^[\\p{L} '_-]+$")) {
            request.getSession().setAttribute("errorMessage", "Tên danh mục chỉ được chứa chữ cái, khoảng trắng, dấu gạch dưới (_), dấu gạch ngang (-) và dấu phẩy đơn (').");
            response.sendRedirect("CategoryServlet?action=list&showErrorModal=true");
            return;
        }

        if (categoryDAO.isCategoryExists(name)) {
            request.getSession().setAttribute("errorMessage", "Tên danh mục đã tồn tại.");
            response.sendRedirect("CategoryServlet?action=list&showErrorModal=true");
            return;
        }

        if (superCategoryIDStr == null || superCategoryIDStr.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Vui lòng chọn Category.");
            response.sendRedirect("CategoryServlet?action=list&showErrorModal=true");
            return;
        }

        int superCategoryID;
        try {
            superCategoryID = Integer.parseInt(superCategoryIDStr);
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Category ID không hợp lệ.");
            response.sendRedirect("CategoryServlet?action=list&showErrorModal=true");
            return;
        }

        Category category = new Category(0, superCategoryID, name, null, 0);
        categoryDAO.addCategory(category);

        request.getSession().setAttribute("successMessage", "Danh mục đã được thêm thành công.");
        response.sendRedirect("CategoryServlet?action=list&showSuccessModal=true");
    }

    private void deleteCategory(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        try {
            int categoryID = Integer.parseInt(request.getParameter("categoryID"));
            categoryDAO.deleteCategory(categoryID);
            request.getSession().setAttribute("successMessage", "Danh mục đã đã được xóa mềm.");
            response.sendRedirect("CategoryServlet?action=list");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID danh mục không hợp lệ.");
            response.sendRedirect("CategoryServlet?action=list");
        }
    }

    private void hardDeleteCategory(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        try {
            int categoryID = Integer.parseInt(request.getParameter("categoryID"));
            categoryDAO.hardDeleteCategory(categoryID);
            request.getSession().setAttribute("successMessage", "Danh mục đã được xóa cứng.");
            response.sendRedirect("CategoryServlet?action=listDeleted");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID danh mục không hợp lệ.");
            response.sendRedirect("CategoryServlet?action=listDeleted");
        }
    }

    private void searchCategory(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException, ClassNotFoundException {
        String keyword = request.getParameter("search");
        if (keyword == null || keyword.trim().isEmpty()) {
            listCategories(request, response);
            return;
        }

        List<Category> categories = categoryDAO.searchCategory(keyword.trim());
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("CategoryManagement.jsp").forward(request, response);
    }

    private void updateCategory(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        String categoryIdStr = request.getParameter("categoryID");
        String name = request.getParameter("categoryName");
        String superCategoryIDStr = request.getParameter("superCategoryID");

        int categoryID;
        try {
            categoryID = Integer.parseInt(categoryIdStr);
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID danh mục không hợp lệ.");
            response.sendRedirect("CategoryServlet?action=list&showErrorModal=true");
            return;
        }

        if (name == null || name.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Tên danh mục không được để trống.");
            response.sendRedirect("CategoryServlet?action=list&showErrorModal=true");
            return;
        }

        name = name.trim();

        if (name.length() > 255) {
            request.getSession().setAttribute("errorMessage", "Tên danh mục quá dài. Tối đa 255 ký tự.");
            response.sendRedirect("CategoryServlet?action=list&showErrorModal=true");
            return;
        }

        if (!name.matches("^[\\p{L} '_-]+$")) {
            request.getSession().setAttribute("errorMessage", "Tên danh mục chỉ được chứa chữ cái, khoảng trắng, dấu gạch dưới (_), dấu gạch ngang (-) và dấu phẩy đơn (').");
            response.sendRedirect("CategoryServlet?action=list&showErrorModal=true");
            return;
        }

        if (categoryDAO.isCategoryExists(name)) {
            request.getSession().setAttribute("errorMessage", "Tên danh mục đã tồn tại.");
            response.sendRedirect("CategoryServlet?action=list&showErrorModal=true");
            return;
        }

        if (superCategoryIDStr == null || superCategoryIDStr.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Vui lòng chọn Category.");
            response.sendRedirect("CategoryServlet?action=list&showErrorModal=true");
            return;
        }
        int superCategoryID;
        try {
            superCategoryID = Integer.parseInt(superCategoryIDStr);
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Category ID không hợp lệ.");
            response.sendRedirect("CategoryServlet?action=list&showErrorModal=true");
            return;
        }

        Category category = new Category(categoryID, superCategoryID, name, null, 0);
        categoryDAO.updateCategory(category);

        request.getSession().setAttribute("successMessage", "Danh mục đã được cập nhật thành công.");
        response.sendRedirect("CategoryServlet?action=list&showSuccessModal=true");
    }

    private void restoreCategory(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ClassNotFoundException {
        try {
            int categoryID = Integer.parseInt(request.getParameter("categoryID"));
            categoryDAO.restoreCategory(categoryID);
            request.getSession().setAttribute("successMessage", "Category restored successfully.");
            response.sendRedirect("CategoryServlet?action=list&showSuccessModal=true");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid category ID.");
            response.sendRedirect("CategoryServlet?action=list&showErrorModal=true");
        } catch (SQLException | ClassNotFoundException ex) {
            request.getSession().setAttribute("errorMessage", "Error restoring category: " + ex.getMessage());
            response.sendRedirect("CategoryServlet?action=list&showErrorModal=true");
        }
    }

    private void listDeletedCategories(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        List<Category> categories = categoryDAO.getAcctiveCategories();
        List<SuperCategory> superCategories = superCategoryDAO.getActiveSuperCategories();

        request.setAttribute("categories", categories);
        request.setAttribute("superCategories", superCategories);
        request.getRequestDispatcher("CategoryManagement.jsp").forward(request, response);
    }
}
