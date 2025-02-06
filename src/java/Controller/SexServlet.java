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
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            handleRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SexServlet.class.getName()).log(Level.SEVERE, null, ex);
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            handleRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SexServlet.class.getName()).log(Level.SEVERE, null, ex);
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

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ClassNotFoundException {
        String action = request.getParameter("action");

        try {
            if (action == null || action.equals("list")) {
                listSexes(request, response);
            } else {
                switch (action) {
                    case "add":
                        addSex(request, response);
                        break;
                    case "update":
                        updateSex(request, response);
                        break;
                    case "delete":
                        deleteSex(request, response);
                        break;
                    default:
                        listSexes(request, response);
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Database Error: " + e.getMessage());
            response.sendRedirect("SexServlet?action=list");
        }
    }

    /**
     * Lấy danh sách giới tính và hiển thị trên JSP
     */
    private void listSexes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException {
        try {
            List<Sex> sexes = sexDAO.getAllSexes();
            request.setAttribute("sexes", sexes);
            request.getRequestDispatcher("SexManagement.jsp").forward(request, response);
        } catch (SQLException e) {
            request.getSession().setAttribute("errorMessage", "Lỗi khi tải danh sách giới tính: " + e.getMessage());
            response.sendRedirect("SexServlet?action=list");
        }
    }

    private void addSex(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        String name = request.getParameter("name");

        if (name == null || name.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Sex name cannot be empty.");
            response.sendRedirect("SexServlet?action=list");
            return;
        }
        if (name.length() > 255) {
            request.getSession().setAttribute("errorMessage", "Sex name is too long. Maximum 255 characters.");
            response.sendRedirect("SexServlet?action=list");
            return;
        }
        if (sexDAO.isSexExists(name.trim())) {
            request.getSession().setAttribute("errorMessage", "Sex name already exists.");
            response.sendRedirect("SexServlet?action=list");
            return;
        }
        if (!name.matches("^[\\p{L}\\s]+$")) {
            request.getSession().setAttribute("errorMessage", "Sex name must contain only letters (including Vietnamese characters) and spaces.");
            response.sendRedirect("SexServlet?action=list");
            return;
        }
        Sex sex = new Sex(0, name.trim(), null);
        sexDAO.addSex(sex);
        request.getSession().setAttribute("successMessage", "Sex added successfully.");
        response.sendRedirect("SexServlet?action=list");
    }

    private void deleteSex(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, ClassNotFoundException {
        try {
            String sexIDStr = request.getParameter("sexID");
            System.out.println("Received sexID: " + sexIDStr);  // Debug log

            int sexID = Integer.parseInt(sexIDStr);
            sexDAO.deleteSex(sexID);

            request.getSession().setAttribute("successMessage", "Sex deleted successfully.");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid Sex ID: " + e.getMessage());
        } catch (SQLException e) {
            request.getSession().setAttribute("errorMessage", "Error deleting Sex: " + e.getMessage());
        }
        response.sendRedirect("SexServlet?action=list");
    }

    private void updateSex(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, ClassNotFoundException {
        try {
            int sexID = Integer.parseInt(request.getParameter("sexID"));
            String name = request.getParameter("name");

            if (name == null || name.trim().isEmpty()) {
                request.getSession().setAttribute("errorMessage", "Tên giới tính không được để trống.");
                response.sendRedirect("SexServlet?action=list");
                return;
            }

            if (name.length() > 255) {
                request.getSession().setAttribute("errorMessage", "Tên giới tính quá dài. Tối đa 255 ký tự.");
                response.sendRedirect("SexServlet?action=list");
                return;
            }

            if (!name.matches("^[\\p{L}\\s]+$")) {
                request.getSession().setAttribute("errorMessage", "Tên giới tính chỉ được chứa chữ cái và khoảng trắng.");
                response.sendRedirect("SexServlet?action=list");
                return;
            }

            Sex sex = new Sex(sexID, name.trim(), null);
            sexDAO.updateSex(sex);
            request.getSession().setAttribute("successMessage", "Cập nhật giới tính thành công.");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID giới tính không hợp lệ.");
        } catch (SQLException e) {
            request.getSession().setAttribute("errorMessage", "Lỗi khi cập nhật giới tính: " + e.getMessage());
        }
        response.sendRedirect("SexServlet?action=list");
    }

}
