/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.CategoryDAO;
import DAO.SuperCategoryDAO;
import Model.Category;
import Model.SuperCategory;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin1
 */
public class CategoryMenuServlet extends HttpServlet {

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
            out.println("<title>Servlet CategoryMenuServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CategoryMenuServlet at " + request.getContextPath() + "</h1>");
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
        try {
            SuperCategoryDAO superCategoryDAO = new SuperCategoryDAO();
            CategoryDAO categoryDAO = new CategoryDAO();

            // Lấy danh sách SuperCategory và Category
            List<SuperCategory> superCategories = superCategoryDAO.getAllSuperCategories();
            List<Category> categories = categoryDAO.getAllCategories();

            // In danh sách SuperCategory ra console
            System.out.println("Danh sách SuperCategory:");
            if (superCategories != null && !superCategories.isEmpty()) {
                for (SuperCategory sc : superCategories) {
                    System.out.println("- ID: " + sc.getSuperCategoryID() + ", Name: " + sc.getName() + ", CreatedAt: " + sc.getCreatedAt());
                }
            } else {
                System.out.println("Danh sách SuperCategory hiện đang trống.");
            }

            // In danh sách Category ra console
            System.out.println("Danh sách Category:");
            if (categories != null && !categories.isEmpty()) {
                for (Category c : categories) {
                    System.out.println("- ID: " + c.getCategoryID() + ", SuperCategoryID: " + c.getSuperCategoryID() + ", Name: " + c.getName() + ", CreatedAt: " + c.getCreatedAt());
                }
            } else {
                System.out.println("Danh sách Category hiện đang trống.");
            }

            // Gửi dữ liệu sang JSP
            if (superCategories == null || superCategories.isEmpty()) {
                request.setAttribute("errorMessage", "Danh sách SuperCategory hiện đang trống.");
            } else if (categories == null || categories.isEmpty()) {
                request.setAttribute("errorMessage", "Danh sách Category hiện đang trống.");
            } else {
                request.setAttribute("superCategories", superCategories);
                request.setAttribute("categories", categories);
            }

            // Forward về JSP
            request.getRequestDispatcher("menu.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi lấy dữ liệu.");
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
        processRequest(request, response);
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
