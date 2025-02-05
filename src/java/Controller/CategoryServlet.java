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
        String action = request.getParameter("action");

        try {
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
                    case "search":
                        searchCategory(request, response);
                        break;
                    default:
                        listCategories(request, response);
                        break;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void listCategories(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        SuperCategoryDAO superCategoryDAO = new SuperCategoryDAO();
        List<SuperCategory> superCategories = superCategoryDAO.getAllSuperCategories();

        List<Category> categories = categoryDAO.getAllCategories();

        request.setAttribute("superCategories", superCategories);
        request.setAttribute("categories", categories);

        request.getRequestDispatcher("CategoryManagement.jsp").forward(request, response);
    }

    private void addCategory(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        String name = request.getParameter("categoryName");
        String superCategoryIDStr = request.getParameter("superCategoryID");

        // Validate Category Name
        if (name == null || name.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Category name cannot be empty.");
            response.sendRedirect("CategoryServlet?action=list&showErrorModal=true");
            return;
        }

        name = name.trim();

        if (name.length() > 255) {
            request.getSession().setAttribute("errorMessage", "Category name is too long. Maximum 255 characters.");
            response.sendRedirect("CategoryServlet?action=list&showErrorModal=true");
            return;
        }

        if (!name.matches("^[a-zA-Z0-9\\s\\p{L}]+$")) {
            request.getSession().setAttribute("errorMessage", "Category name must contain only letters, numbers, and spaces.");
            response.sendRedirect("CategoryServlet?action=list&showErrorModal=true");
            return;
        }

        // Check for Duplicate Category
        if (categoryDAO.isCategoryExists(name)) {
            request.getSession().setAttribute("errorMessage", "Category name already exists.");
            response.sendRedirect("CategoryServlet?action=list&showErrorModal=true");
            return;
        }

        // Validate SuperCategory ID
        if (superCategoryIDStr == null || superCategoryIDStr.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "SuperCategory must be selected.");
            response.sendRedirect("CategoryServlet?action=list&showErrorModal=true");
            return;
        }

        int superCategoryID;
        try {
            superCategoryID = Integer.parseInt(superCategoryIDStr);
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid SuperCategory ID.");
            response.sendRedirect("CategoryServlet?action=list&showErrorModal=true");
            return;
        }

        // Add Category
        Category category = new Category(0, superCategoryID, name, null);
        categoryDAO.addCategory(category);
        request.getSession().setAttribute("successMessage", "Category added successfully.");
        response.sendRedirect("CategoryServlet?action=list&showSuccessModal=true");
    }

    private void deleteCategory(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        try {
            int categoryID = Integer.parseInt(request.getParameter("categoryID"));
            categoryDAO.deleteCategory(categoryID);
            request.getSession().setAttribute("successMessage", "Category deleted successfully.");
            response.sendRedirect("CategoryServlet?action=list");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid category ID.");
            response.sendRedirect("CategoryServlet?action=list");
        }
    }

    private void searchCategory(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException, ClassNotFoundException {
        String keyword = request.getParameter("search");
        if (keyword == null || keyword.trim().isEmpty()) {
            listCategories(request, response);
            return;
        }
        keyword = keyword.trim();
        List<Category> categories = categoryDAO.searchCategory(keyword);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("CategoryManagement.jsp").forward(request, response);
    }

    public List<SuperCategory> getAllSuperCategories() throws SQLException, ClassNotFoundException {
        List<SuperCategory> superCategories = new ArrayList<>();
        String query = "SELECT SuperCategoryID, Name FROM SuperCategory";
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement ps = conn.prepareStatement(query);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                superCategories.add(new SuperCategory(
                        rs.getInt("SuperCategoryID"),
                        rs.getString("Name"),
                        null // Assuming CreatedAt is not needed here
                ));
            }
        }
        return superCategories;
    }

}
