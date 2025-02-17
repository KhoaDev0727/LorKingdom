/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.OriginDAO;
import Model.Origin;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author admin1
 */
public class OriginServlet extends HttpServlet {

    OriginDAO originDAO = new OriginDAO();

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
            out.println("<title>Servlet OriginServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet OriginServlet at " + request.getContextPath() + "</h1>");
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

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    // Xử lý các request dựa theo tham số action

    private void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            if (action == null || action.equals("list")) {
                listOrigins(request, response);
            } else {
                switch (action) {
                    case "add":
                        addOrigin(request, response);
                        break;
                    case "update":
                        updateOrigin(request, response);
                        break;
                    case "delete":
                        deleteOrigin(request, response);
                        break;
                    case "search":
                        searchOrigin(request, response);
                        break;
                    default:
                        listOrigins(request, response);
                        break;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    // Hiển thị danh sách Origin
    private void listOrigins(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        List<Origin> origins = originDAO.getAllOrigins();
        request.setAttribute("origins", origins);
        request.getRequestDispatcher("OriginManagement.jsp").forward(request, response);
    }

    // Thêm mới Origin
    private void addOrigin(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        String name = request.getParameter("name");
        if (name == null || name.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Origin name cannot be empty.");
            response.sendRedirect("OriginServlet?action=list&showErrorModal=true");
            return;
        }
        if (name.length() > 255) {
            request.getSession().setAttribute("errorMessage", "Origin name is too long. Maximum 255 characters allowed.");
            response.sendRedirect("OriginServlet?action=list&showErrorModal=true");
            return;
        }
        if (originDAO.isOriginExists(name.trim())) {
            request.getSession().setAttribute("errorMessage", "Origin already exists.");
            response.sendRedirect("OriginServlet?action=list&showErrorModal=true");
            return;
        }
        Origin origin = new Origin(0, name.trim(), null);
        originDAO.addOrigin(origin);
        request.getSession().setAttribute("successMessage", "Origin added successfully.");
        response.sendRedirect("OriginServlet?action=list&showSuccessModal=true");
    }

    // Cập nhật Origin
    private void updateOrigin(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        try {
            int originID = Integer.parseInt(request.getParameter("originID"));
            String name = request.getParameter("name");
            if (name == null || name.trim().isEmpty()) {
                request.getSession().setAttribute("errorMessage", "Origin name cannot be empty.");
                response.sendRedirect("OriginServlet?action=list&showErrorModal=true");
                return;
            }
            if (name.length() > 255) {
                request.getSession().setAttribute("errorMessage", "Origin name is too long. Maximum 255 characters allowed.");
                response.sendRedirect("OriginServlet?action=list&showErrorModal=true");
                return;
            }
            Origin origin = new Origin(originID, name.trim(), null);
            originDAO.updateOrigin(origin);
            request.getSession().setAttribute("successMessage", "Origin updated successfully.");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid Origin ID.");
        }
        response.sendRedirect("OriginServlet?action=list");
    }

    // Xóa Origin
    private void deleteOrigin(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        try {
            int originID = Integer.parseInt(request.getParameter("originID"));
            originDAO.deleteOrigin(originID);
            request.getSession().setAttribute("successMessage", "Origin deleted successfully.");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid Origin ID.");
        }
        response.sendRedirect("OriginServlet?action=list");
    }

    // Tìm kiếm Origin theo từ khóa
    private void searchOrigin(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        String keyword = request.getParameter("search");
        if (keyword == null || keyword.trim().isEmpty()) {
            listOrigins(request, response);
            return;
        }
        List<Origin> origins = originDAO.searchOrigin(keyword.trim());
        request.setAttribute("origins", origins);
        request.getRequestDispatcher("OriginManagement.jsp").forward(request, response);
    }
}
