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
import jakarta.servlet.http.HttpSession;
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

    private void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
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
                    listActive(request, response);
                    break;
                case "listDeleted":
                    listDeleted(request, response);
                    break;
                case "add":
                    addSuperCategory(request, response);
                    break;
                case "update":
                    updateSuperCategory(request, response);
                    break;
                case "delete": 
                    softDelete(request, response);
                    break;
                case "hardDelete": 
                    hardDelete(request, response);
                    break;
                case "restore":
                    restoreSuperCategory(request, response);
                    break;
                case "search":
                    search(request, response);
                    break;
                default:
                    listActive(request, response);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            session.setAttribute("errorMessage", "Error: " + ex.getMessage());
            response.sendRedirect("SuperCategoryServlet?action=list&showErrorModal=true");
        }
    }

    private void restoreSuperCategory(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ClassNotFoundException {
        int superCategoryID = Integer.parseInt(request.getParameter("superCategoryID"));

        try {
            superCategoryDAO.restoreSuperCategory(superCategoryID);
            request.getSession().setAttribute("successMessage", "Danh Mục Khôi Phục Thành Công.");
            response.sendRedirect("SuperCategoryServlet?action=list&showSuccessModal=true");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID danh mục không hợp lệ.");
            response.sendRedirect("SuperCategoryServlet?action=list&showErrorModal=true");
        }
    }

    private void listActive(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        List<SuperCategory> list = superCategoryDAO.getActiveSuperCategories();
        request.setAttribute("superCategories", list);
        request.getRequestDispatcher("SupperCategoryManagement.jsp").forward(request, response);
    }

    private void listDeleted(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        List<SuperCategory> list = superCategoryDAO.getDeletedSuperCategories();
        request.setAttribute("superCategories", list);
        request.getRequestDispatcher("SupperCategoryManagement.jsp").forward(request, response);
    }

    private void addSuperCategory(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {
        String name = request.getParameter("superCategoryName");
        if (name == null || name.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Tên danh mục không được để trống.");
            response.sendRedirect("SuperCategoryServlet?action=list&showErrorModal=true");
            return;
        }

        name = name.trim();

        if (name.length() > 50) {
            request.getSession().setAttribute("errorMessage", "Tên danh mục quá dài. Tối đa 50 ký tự.");
            response.sendRedirect("SuperCategoryServlet?action=list&showErrorModal=true");
            return;
        }

        if (!name.matches("^[\\p{L} _-]+$")) {
            request.getSession().setAttribute("errorMessage", "Tên danh mục chỉ được chứa chữ cái và khoảng trắng.");
            response.sendRedirect("SuperCategoryServlet?action=list&showErrorModal=true");
            return;
        }

        if (superCategoryDAO.isSuperCategoryExists(name)) {
            request.getSession().setAttribute("errorMessage", "Tên danh mục đã tồn tại.");
            response.sendRedirect("SuperCategoryServlet?action=list&showErrorModal=true");
            return;
        }
        SuperCategory sc = new SuperCategory();
        sc.setName(name);
        superCategoryDAO.addSuperCategory(sc);

        request.getSession().setAttribute("successMessage", "Đã thêm danh mục thành công.");
        response.sendRedirect("SuperCategoryServlet?action=list&showSuccessModal=true");
    }

    private void updateSuperCategory(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {
        String idStr = request.getParameter("superCategoryID");
        String name = request.getParameter("name");
        try {
            int id = Integer.parseInt(idStr);
            if (name == null || name.trim().isEmpty()) {
                request.getSession().setAttribute("errorMessage", "Tên danh mục không được để trống.");
                response.sendRedirect("SuperCategoryServlet?action=list&showErrorModal=true");
                return;
            }

            name = name.trim();

            if (name.length() > 50) {
                request.getSession().setAttribute("errorMessage", "Tên danh mục quá dài. Tối đa 50 ký tự.");
                response.sendRedirect("SuperCategoryServlet?action=list&showErrorModal=true");
                return;
            }

            if (!name.matches("^[\\p{L} _-]+$")) {
                request.getSession().setAttribute("errorMessage", "Tên SuperCategory chỉ được chứa chữ cái và khoảng trắng.");
                response.sendRedirect("SuperCategoryServlet?action=list&showErrorModal=true");
                return;
            }

            if (superCategoryDAO.isSuperCategoryExists(name)) {
                request.getSession().setAttribute("errorMessage", "Tên danh mục đã tồn tại.");
                response.sendRedirect("SuperCategoryServlet?action=list&showErrorModal=true");
                return;
            }
            superCategoryDAO.updateSuperCategory(id, name.trim());
            request.getSession().setAttribute("successMessage", "Đã cập nhật danh mục thành công.");
            response.sendRedirect("SuperCategoryServlet?action=list&showSuccessModal=true");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID không hợp lệ..");
            response.sendRedirect("SuperCategoryServlet?action=list&showErrorModal=true");
        }
    }

    private void softDelete(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("superCategoryID"));
            superCategoryDAO.softDeleteSuperCategory(id);
            request.getSession().setAttribute("successMessage", "Đã chuyển danh mục vào thùng rác thành công.");
            response.sendRedirect("SuperCategoryServlet?action=list");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid ID.");
            response.sendRedirect("SuperCategoryServlet?action=list");
        }
    }

    private void hardDelete(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("superCategoryID"));
            superCategoryDAO.hardDeleteSuperCategory(id);
            request.getSession().setAttribute("successMessage", "Danh mục đã bị xóa vĩnh viễn.");
            response.sendRedirect("SuperCategoryServlet?action=listDeleted");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID không hợp lệ.");
            response.sendRedirect("SuperCategoryServlet?action=listDeleted");
        }
    }

    private void search(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        String keyword = request.getParameter("search");
        if (keyword == null || keyword.trim().isEmpty()) {
            listActive(request, response);
            return;
        }
        List<SuperCategory> results = superCategoryDAO.searchSuperCategory(keyword.trim().toLowerCase());
        request.setAttribute("superCategories", results);
        request.getRequestDispatcher("SupperCategoryManagement.jsp").forward(request, response);
    }
}
