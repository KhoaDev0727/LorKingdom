/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.SexDAO;
import Model.Sex;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin1
 */
public class SexServlet extends HttpServlet {

    SexDAO sexDAO = new SexDAO();

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
            out.println("<title>Servlet SexServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SexServlet at " + request.getContextPath() + "</h1>");
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

    /**
     * Xử lý yêu cầu dựa trên tham số action
     */
    private void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        try {
            Integer roleID = (Integer) session.getAttribute("roleID");
            if (roleID == null) {
                response.sendRedirect(request.getContextPath() + "/Admin/loginPage.jsp"); // Chưa đăng nhập, chuyển hướng đến trang login
                return;
            }
            if (action == null || action.equals("list")) {
                listSexes(request, response);
            } else {
                switch (action) {
                    case "add":
                        addSex(request, response);
                        break;
                    case "delete":
                        deleteSex(request, response);
                        break;
                    case "update":
                        updateSex(request, response);
                        break;
                    case "search":
                        searchSex(request, response);
                        break;
                    default:
                        listSexes(request, response);
                        break;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void listSexes(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        List<Sex> sexes = sexDAO.getAllSexes();
        request.setAttribute("sexes", sexes);
        request.getRequestDispatcher("SexManagement.jsp").forward(request, response);
    }

    private void addSex(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        String name = request.getParameter("name");

        if (name == null || name.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Gender cannot be left blank.");
            response.sendRedirect("SexServlet?action=list&showErrorModal=true");
            return;
        }
        if (name.length() > 50) {
            request.getSession().setAttribute("errorMessage", "Maximum 50 characters allowed.");
            response.sendRedirect("SexServlet?action=list&showErrorModal=true");
            return;
        }
        if (!name.matches("^[\\p{L} _-]+$")) {
            request.getSession().setAttribute("errorMessage", "Gender must contain only letters and spaces.");
            response.sendRedirect("SexServlet?action=list&showErrorModal=true");
            return;
        }
        if (sexDAO.isSexExists(name)) {
            request.getSession().setAttribute("errorMessage", "Gender already exists.");
            response.sendRedirect("SexServlet?action=list&showErrorModal=true");
            return;
        }
        Sex sex = new Sex(0, name.trim(), null);
        sexDAO.addSex(sex);

        request.getSession().setAttribute("successMessage", "Gender added successfully.");
        response.sendRedirect("SexServlet?action=list&showSuccessModal=true");
    }

    private void updateSex(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        try {
            int sexID = Integer.parseInt(request.getParameter("sexID"));
            String name = request.getParameter("name");

            if (name == null || name.trim().isEmpty()) {
                request.getSession().setAttribute("errorMessage", "Gender cannot be left blank.");
                response.sendRedirect("SexServlet?action=list&showErrorModal=true");
                return;
            }
            if (name.length() > 50) {
                request.getSession().setAttribute("errorMessage", "Maximum 50 characters allowed.");
                response.sendRedirect("SexServlet?action=list&showErrorModal=true");
                return;
            }
            if (!name.matches("^[\\p{L} _-]+$")) {
                request.getSession().setAttribute("errorMessage", "Gender must contain only letters and spaces.");
                response.sendRedirect("SexServlet?action=list&showErrorModal=true");
                return;
            }
            if (sexDAO.isSexExists(name)) {
                request.getSession().setAttribute("errorMessage", "Gender already exists.");
                response.sendRedirect("SexServlet?action=list&showErrorModal=true");
                return;
            }

            Sex sex = new Sex(sexID, name.trim(), null);
            sexDAO.updateSex(sex);
            request.getSession().setAttribute("showSuccessModal", "Gender updated successfully.");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid ID.");
        }
        response.sendRedirect("SexServlet?action=list");
    }

    private void deleteSex(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        try {
            int sexID = Integer.parseInt(request.getParameter("sexID"));
            sexDAO.deleteSex(sexID);

            request.getSession().setAttribute("successMessage", "Gender removed successfully.");

            response.sendRedirect("SexServlet?action=list");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid ID.");
            response.sendRedirect("SexServlet?action=list");
        }
    }

    private void searchSex(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        String keyword = request.getParameter("search");
        if (keyword == null || keyword.trim().isEmpty()) {
            listSexes(request, response);
            return;
        }
        List<Sex> sexes = sexDAO.searchSex(keyword.trim().toLowerCase());
        request.setAttribute("sexes", sexes);
        request.getRequestDispatcher("SexManagement.jsp").forward(request, response);
    }

}
