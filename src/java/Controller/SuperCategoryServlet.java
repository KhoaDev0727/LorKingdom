/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.SuperCategoryDAO;
import Model.SuperCategory;
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
public class SuperCategoryServlet extends HttpServlet {

    private SuperCategoryDAO superCategoryDAO = new SuperCategoryDAO();

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
            out.println("<title>Servlet SuperCategoryServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SuperCategoryServlet at " + request.getContextPath() + "</h1>");
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
                listSuperCategories(request, response);
            } else {
                switch (action) {
                    case "add":
                        addSuperCategory(request, response);
                        break;
                    case "update":
                        updateSuperCategory(request, response);
                        break;
                    case "delete":
                        deleteSuperCategory(request, response);
                        break;
                    case "search":
                        searchSuperCategory(request, response);
                        break;
                    default:
                        listSuperCategories(request, response);
                        break;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            request.getRequestDispatcher("Admin/error.jsp").forward(request, response);
        }
    }

    private void listSuperCategories(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        List<SuperCategory> superCategories = superCategoryDAO.getAllSuperCategories();
        request.setAttribute("superCategories", superCategories);
        request.getRequestDispatcher("SupperCategoryManagement.jsp").forward(request, response);
    }

    private void addSuperCategory(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        String name = request.getParameter("superCategoryName");

        if (name == null || name.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Category name cannot be empty.");
            response.sendRedirect("SuperCategoryServlet?action=list&showErrorModal=true");
            return;
        }

        name = name.trim();

        if (name.length() > 50) {
            request.getSession().setAttribute("errorMessage", "Category name is too long. Maximum 50 characters.");
            response.sendRedirect("SuperCategoryServlet?action=list&showErrorModal=true");
            return;
        }

        if (!name.matches("^[\\p{L}\\s]+$")) {
            request.getSession().setAttribute("errorMessage", "Category name must contain only letters (including Vietnamese characters) and spaces.");
            response.sendRedirect("SuperCategoryServlet?action=list&showErrorModal=true");
            return;
        }

        if (superCategoryDAO.isSuperCategoryExists(name)) {
            request.getSession().setAttribute("errorMessage", "Category name already exists.");
            response.sendRedirect("SuperCategoryServlet?action=list&showErrorModal=true");
            return;
        }

        // Thêm danh mục mới
        SuperCategory superCategory = new SuperCategory(0, name, null);
        superCategoryDAO.addSuperCategory(superCategory);
        request.getSession().setAttribute("successMessage", "Category added successfully.");
        response.sendRedirect("SuperCategoryServlet?action=list&showSuccessModal=true");
    }

    private void updateSuperCategory(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        int superCategoryID = Integer.parseInt(request.getParameter("superCategoryID"));
        String newName = request.getParameter("name");

        if (newName == null || newName.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Category name cannot be empty.");
            response.sendRedirect("SuperCategoryServlet?action=list&showErrorModal=true");
            return;
        }

        superCategoryDAO.updateSuperCategory(superCategoryID, newName);
        request.getSession().setAttribute("successMessage", "Category updated successfully.");
        response.sendRedirect("SuperCategoryServlet?action=list&showSuccessModal=true");
    }

    private void deleteSuperCategory(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        try {
            int superCategoryID = Integer.parseInt(request.getParameter("superCategoryID"));
            superCategoryDAO.deleteSuperCategory(superCategoryID);
            request.getSession().setAttribute("successMessage", "Category deleted successfully.");
            response.sendRedirect("SuperCategoryServlet?action=list&showSuccessModal=true");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid category ID.");
            response.sendRedirect("SuperCategoryServlet?action=list&showErrorModal=true");
        }
    }

    private void searchSuperCategory(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException, ClassNotFoundException {
        String keyword = request.getParameter("search");
        if (keyword == null || keyword.trim().isEmpty()) {
            listSuperCategories(request, response);
            return;
        }

        List<SuperCategory> superCategories = superCategoryDAO.searchSuperCategory(keyword);
        request.setAttribute("superCategories", superCategories);
        request.getRequestDispatcher("SupperCategoryManagement.jsp").forward(request, response);
    }
}
