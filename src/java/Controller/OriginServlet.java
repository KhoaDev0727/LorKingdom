
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
import jakarta.servlet.http.HttpSession;
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

        HttpSession session = request.getSession();
        // Kiểm tra đăng nhập (nếu cần)
        Integer roleID = (Integer) session.getAttribute("roleID");
        if (roleID == null) {
            response.sendRedirect(request.getContextPath() + "/Admin/loginPage.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "list":
                    listOrigins(request, response);
                    break;
                case "listDeleted":
                    listDeletedOrigins(request, response);
                    break;
                case "add":
                    addOrigin(request, response);
                    break;
                case "update":
                    updateOrigin(request, response);
                    break;
                case "delete": // Xóa mềm
                    softDeleteOrigin(request, response);
                    break;
                case "hardDelete": // Xóa cứng
                    hardDeleteOrigin(request, response);
                    break;
                case "restore":
                    restoreOrigin(request, response);
                    break;
                case "search":
                    searchOrigin(request, response);
                    break;
                default:
                    listOrigins(request, response);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            session.setAttribute("errorMessage", "Error: " + ex.getMessage());
            response.sendRedirect("OriginServlet?action=list&showErrorModal=true");
        }
    }

    // 1) Hiển thị danh sách active
    private void listOrigins(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        List<Origin> origins = originDAO.getAllActiveOrigins();
        request.setAttribute("origins", origins);
        request.getRequestDispatcher("OriginManagement.jsp").forward(request, response);
    }

    // 2) Hiển thị danh sách đã xóa mềm
    private void listDeletedOrigins(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        List<Origin> origins = originDAO.getDeletedOrigins();
        request.setAttribute("origins", origins);
        request.getRequestDispatcher("OriginManagement.jsp").forward(request, response);
    }

    
    private void addOrigin(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        String name = request.getParameter("name");
        if (name.length() > 255) {
            request.getSession().setAttribute("errorMessage", "Origin name is too long. Maximum 255 characters allowed.");
            response.sendRedirect("OriginServlet?action=list&showErrorModal=true");
            return;
        }
        if (!name.matches("^[\\p{L} _-]+$")) {
            request.getSession().setAttribute("errorMessage", "Origin names must contain only letters and spaces.");
            response.sendRedirect("OriginServlet?action=list&showErrorModal=true");
            return;
        }
        if (originDAO.isOriginExists(name.trim())) {
            request.getSession().setAttribute("errorMessage", "Origin already exists.");
            response.sendRedirect("OriginServlet?action=list&showErrorModal=true");
            return;
        }
        Origin origin = new Origin(0, name, null, 0);
        originDAO.addOrigin(origin);
        request.getSession().setAttribute("successMessage", "Origin added successfully.");
        response.sendRedirect("OriginServlet?action=list&showSuccessModal=true");
    }

    // Cập nhật Origin
    private void updateOrigin(HttpServletRequest request, HttpServletResponse response)
        throws SQLException, ClassNotFoundException, ServletException, IOException {
    try {
        // Kiểm tra và parse originID
        String originIDStr = request.getParameter("originID");
        if (originIDStr == null || originIDStr.isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Origin ID is missing.");
            response.sendRedirect("OriginServlet?action=list&showErrorModal=true");
            return;
        }
        int originID;
        try {
            originID = Integer.parseInt(originIDStr);
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid Origin ID.");
            response.sendRedirect("OriginServlet?action=list&showErrorModal=true");
            return;
        }

        // Kiểm tra name
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
        if (!name.matches("^[\\p{L} _-]+$")) {
            request.getSession().setAttribute("errorMessage", "Origin names must contain only letters and spaces.");
            response.sendRedirect("OriginServlet?action=list&showErrorModal=true");
            return;
        }

        // Kiểm tra nếu originDAO có bị null không
        if (originDAO == null) {
            request.getSession().setAttribute("errorMessage", "Database connection error.");
            response.sendRedirect("OriginServlet?action=list&showErrorModal=true");
            return;
        }

        // Kiểm tra xem origin đã tồn tại chưa
        if (originDAO.isOriginExists(name.trim())) {
            request.getSession().setAttribute("errorMessage", "Origin already exists.");
            response.sendRedirect("OriginServlet?action=list&showErrorModal=true");
            return;
        }

        // Cập nhật Origin
        Origin origin = new Origin(originID, name, null, 0);
        originDAO.updateOrigin(origin);
        request.getSession().setAttribute("successMessage", "Origin updated successfully.");

    } catch (Exception e) {
        request.getSession().setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
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
    // 5) Xóa mềm

    private void softDeleteOrigin(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {
        try {
            int originID = Integer.parseInt(request.getParameter("originID"));
            originDAO.softDeleteOrigin(originID);
            request.getSession().setAttribute("successMessage", "Xuất xứ đã được đưa vào thùng rác");
            response.sendRedirect("OriginServlet?action=list");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID xuất xứ không hợp lệ.");
            response.sendRedirect("OriginServlet?action=list");
        }
    }

    // 6) Xóa cứng
    private void hardDeleteOrigin(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {
        try {
            int originID = Integer.parseInt(request.getParameter("originID"));
            originDAO.hardDeleteOrigin(originID);
            request.getSession().setAttribute("successMessage", "Xuất xứ đã được xóa vĩnh viễn.");
            response.sendRedirect("OriginServlet?action=listDeleted");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID xuất xứ không hợp lệ.");
            response.sendRedirect("OriginServlet?action=listDeleted");
        }
    }

    // 7) Khôi phục
    private void restoreOrigin(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {
        try {
            int originID = Integer.parseInt(request.getParameter("originID"));
            originDAO.restoreOrigin(originID);
            request.getSession().setAttribute("successMessage", "Xuất xứ đã được khôi phục.");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID xuất xứ không hợp lệ.");
        }
        response.sendRedirect("OriginServlet?action=list");
    }
}
