/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.RoleDAO;
import Model.Role;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Lenovo
 */
public class RoleServlet extends HttpServlet {

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
            out.println("<title>Servlet RoleServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RoleServlet at " + request.getContextPath() + "</h1>");
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
    RoleDAO roleDAO = new RoleDAO();
    private void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        try {
            // Kiểm tra đăng nhập (ở đây giả sử roleID của người dùng được lưu trong session)
            Integer userRoleID = (Integer) session.getAttribute("roleID");
            if (userRoleID == null) {
                response.sendRedirect(request.getContextPath() + "/Admin/loginPage.jsp");
                return;
            }
            if (action == null || action.equals("list")) {
                listRoles(request, response);
            } else {
                switch (action) {
                    case "add":
                        addRole(request, response);
                        break;
                    case "update":
                        updateRole(request, response);
                        break;
                    case "delete":
                        deleteRole(request, response);
                        break;
                    case "search":
                        searchRole(request, response);
                        break;
                    default:
                        listRoles(request, response);
                        break;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void listRoles(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        List<Role> roles = roleDAO.getAllRoles();
        request.setAttribute("roles", roles);
        request.getRequestDispatcher("RoleManagement.jsp").forward(request, response);
    }

    private void addRole(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");

        if (name == null || name.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Tên vai trò không được để trống.");
            response.sendRedirect("RoleServlet?action=list&showErrorModal=true");
            return;
        }
        if (!name.matches("^[\\p{L} _-]+$")) {
            request.getSession().setAttribute("errorMessage", "Tên vai trò chỉ được chứa chữ cái và khoảng trắng.");
            response.sendRedirect("RoleServlet?action=list&showErrorModal=true");
            return;
        }
        if (roleDAO.isRole(name.trim())) {
            request.getSession().setAttribute("errorMessage", "Tên vai trò đã tồn tại. Vui lòng nhập tên khác.");
            response.sendRedirect("RoleServlet?action=list&showErrorModal=true");
            return;
        }
        Role role = new Role(0, name.trim(), description);
        roleDAO.addRole(role);
        request.getSession().setAttribute("successMessage", "Đã thêm vai trò thành công.");
        response.sendRedirect("RoleServlet?action=list&showSuccessModal=true");
    }

    private void updateRole(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        try {
            int roleID = Integer.parseInt(request.getParameter("roleID"));
            String name = request.getParameter("name");
            String description = request.getParameter("description");

            if (name == null || name.trim().isEmpty()) {
                request.getSession().setAttribute("errorMessage", "Tên vai trò không được để trống.");
                response.sendRedirect("RoleServlet?action=list&showErrorModal=true");
                return;
            }
            if (!name.matches("^[\\p{L} _-]+$")  ) {
                request.getSession().setAttribute("errorMessage", "Tên vai trò chỉ được chứa chữ cái và khoảng trắng.");
                response.sendRedirect("RoleServlet?action=list&showErrorModal=true");
                return;
            }
//            if (roleDAO.isRole(name.trim())) {
//                request.getSession().setAttribute("errorMessage", "Tên vai trò đã tồn tại. Vui lòng nhập tên khác.");
//                response.sendRedirect("RoleServlet?action=list&showErrorModal=true");
//                return;
//            }

            Role role = new Role(roleID, name.trim(), description);
            roleDAO.updateRole(role);
            request.getSession().setAttribute("successMessage", "Vai trò đã được cập nhật thành công.");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID không hợp lệ.");
        }
        response.sendRedirect("RoleServlet?action=list");
    }

    private void deleteRole(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        try {
            int roleID = Integer.parseInt(request.getParameter("roleID"));
            roleDAO.deleteRole(roleID);
            request.getSession().setAttribute("successMessage", "Đã xóa vai trò thành công.");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID không hợp lệ.");
        }
        response.sendRedirect("RoleServlet?action=list");
    }

    private void searchRole(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        String keyword = request.getParameter("search".toLowerCase());
        if (keyword == null || keyword.trim().isEmpty()) {
            listRoles(request, response);
            return;
        }

        List<Role> filteredRoles = roleDAO.searchRoles(keyword.toLowerCase());
        request.setAttribute("roles", filteredRoles);
        request.getRequestDispatcher("RoleManagement.jsp").forward(request, response);
    }
}
