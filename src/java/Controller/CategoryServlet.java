/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Model.Category;
import DAO.CategoryDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
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
        System.out.println("CategoryServlet called. Action: " + action);

        try {
            if (action == null || action.equals("list")) {
                listCategories(request, response);
            } else {
                switch (action) {
                    case "add":
                        addCategory(request, response);
                        break;
                    case "update":
                        updateCategory(request, response);
                        break;
                    case "delete":
                        deleteCategory(request, response);
                        break;
                    case "search":
                        searchCategory(request, response);
                        break;
                    default:
                        listCategories(request, response); // Hiển thị danh sách mặc định
                        break;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            request.getRequestDispatcher("Admin/error.jsp").forward(request, response);
        }

    }

    private void listCategories(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        List<Category> categories = categoryDAO.getAllCategories();

        if (categories == null || categories.isEmpty()) {
            System.out.println("No categories found.");
        } else {
            System.out.println("Categories found: " + categories.size());
        }

        request.setAttribute("categories", categories);

        request.getRequestDispatcher("CategoryManagement.jsp").forward(request, response);
    }

    private void addCategory(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {

        String name = request.getParameter("categoryName");

        if (name == null || name.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Category name cannot be empty.");
            response.sendRedirect("CategoryServlet?action=list&showErrorModal=true");
            return;
        }

        name = name.trim().toLowerCase();

        if (name.length() > 50) {
            request.getSession().setAttribute("errorMessage", "Category name is too long. Maximum 50 characters.");
            response.sendRedirect("CategoryServlet?action=list&showErrorModal=true");
            return;
        }

        if (!name.matches("^[a-z\\s]+$")) {
            request.getSession().setAttribute("errorMessage", "Category name must contain only letters and spaces.");
            response.sendRedirect("CategoryServlet?action=list&showErrorModal=true");
            return;
        }

        if (categoryDAO.isCategoryExists(name)) {
            request.getSession().setAttribute("errorMessage", "Category name already exists.");
            response.sendRedirect("CategoryServlet?action=list&showErrorModal=true");
            return;
        }

        Category category = new Category(0, name, null);
        categoryDAO.addCategory(category);
        request.getSession().setAttribute("successMessage", "Category added successfully.");
        response.sendRedirect("CategoryServlet?action=list&showSuccessModal=true");
    }

    private void updateCategory(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        try {
            int categoryID = Integer.parseInt(request.getParameter("categoryID"));
            String name = request.getParameter("name");

            if (name == null || name.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Category name cannot be empty.");
                listCategories(request, response);
                return;
            }

            Category category = new Category(categoryID, name, null);
            categoryDAO.updateCategory(category);
            response.sendRedirect("CategoryServlet?action=list");
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid category ID.");
            listCategories(request, response);
        }
    }

    private void deleteCategory(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        try {
            int categoryID = Integer.parseInt(request.getParameter("categoryID"));
            categoryDAO.deleteCategory(categoryID);
            response.sendRedirect("CategoryServlet?action=list");
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid category ID.");
            listCategories(request, response);
        }
    }

    private void searchCategory(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException, ClassNotFoundException {
        String keyword = request.getParameter("search");
        if (keyword == null || keyword.trim().isEmpty()) {
            listCategories(request, response);
            return;
        }

        List<Category> categories = categoryDAO.searchCategory(keyword);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("CategoryManagement.jsp").forward(request, response);
    }

}
