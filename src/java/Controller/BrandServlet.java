/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.BrandDAO;
import Model.Brand;
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
public class BrandServlet extends HttpServlet {

    BrandDAO brandDAO = new BrandDAO();

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
            out.println("<title>Servlet BrandServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet BrandServlet at " + request.getContextPath() + "</h1>");
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
    // Xử lý các request từ client

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
                    listBrands(request, response);
                    break;
                case "listDeleted":
                    listDeletedBrands(request, response);
                    break;
                case "add":
                    addBrand(request, response);
                    break;
                case "update":
                    updateBrand(request, response);
                    break;
                case "delete": // Xóa mềm
                    softDeleteBrand(request, response);
                    break;
                case "hardDelete": // Xóa cứng
                    hardDeleteBrand(request, response);
                    break;
                case "restore":
                    restoreBrand(request, response);
                    break;
                case "search":
                    searchBrand(request, response);
                    break;
                default:
                    listBrands(request, response);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            session.setAttribute("errorMessage", "Error: " + ex.getMessage());
            response.sendRedirect("BrandServlet?action=list&showErrorModal=true");
        }
    }

    private void listBrands(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        List<Brand> brands = brandDAO.getAllBrands();
        request.setAttribute("brands", brands);
        request.getRequestDispatcher("BrandManagement.jsp").forward(request, response);
    }

    private void listDeletedBrands(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        List<Brand> brands = brandDAO.getDeletedBrands();
        request.setAttribute("brands", brands);
        request.getRequestDispatcher("BrandManagement.jsp").forward(request, response);
    }

    private void addBrand(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        String name = request.getParameter("name");

        // Kiểm tra tên không được để trống
        if (name == null || name.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Tên thương hiệu không được để trống.");
            response.sendRedirect("BrandServlet?action=list&showErrorModal=true");
            return;
        }

        if (name.length() > 100) {
            request.getSession().setAttribute("errorMessage", "Tên thương hiệu quá dài. Tối đa 100 ký tự được phép.");
            response.sendRedirect("BrandServlet?action=list&showErrorModal=true");
            return;
        }
        String validNamePattern = "^[\\p{L}\\p{M}\\s\\-_]+$";
        if (!name.matches(validNamePattern)) {
            request.getSession().setAttribute("errorMessage",
                    "Tên thương hiệu chỉ được phép chứa chữ cái tiếng Việt (có dấu), "
                    + "dấu gạch ngang (-), dấu gạch dưới (_)"
                    + (/*nếu cho phép*/" và khoảng trắng"/*nếu không cho phép thì bỏ \s khỏi regex*/));
            response.sendRedirect("BrandServlet?action=list&showErrorModal=true");
            return;
        }

        if (brandDAO.isBrandExists(name.trim())) {
            request.getSession().setAttribute("errorMessage", "Thương hiệu đã tồn tại.");
            response.sendRedirect("BrandServlet?action=list&showErrorModal=true");
            return;
        }

        Brand brand = new Brand(0, name.trim(), 0, null);
        brandDAO.addBrand(brand);
        request.getSession().setAttribute("successMessage", "Đã thêm thương hiệu thành công.");
        response.sendRedirect("BrandServlet?action=list&showSuccessModal=true");
    }

    private void updateBrand(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        try {
            int brandID = Integer.parseInt(request.getParameter("brandID"));
            String name = request.getParameter("name");

            if (name == null || name.trim().isEmpty()) {
                request.getSession().setAttribute("errorMessage", "Tên thương hiệu không được để trống.");
                response.sendRedirect("BrandServlet?action=list&showErrorModal=true");
                return;
            }
            String validNamePattern = "^[\\p{L}\\p{M}\\s\\-_]+$";
            if (!name.matches(validNamePattern)) {
                request.getSession().setAttribute("errorMessage",
                        "Tên thương hiệu chỉ được phép chứa chữ cái tiếng Việt (có dấu), "
                        + "dấu gạch ngang (-), dấu gạch dưới (_)"
                        + (/*nếu cho phép*/" và khoảng trắng"/*nếu không cho phép thì bỏ \s khỏi regex*/));
                response.sendRedirect("BrandServlet?action=list&showErrorModal=true");
                return;
            }
            if (name.length() > 100) {
                request.getSession().setAttribute("errorMessage", "Tên thương hiệu quá dài. Tối đa 100 ký tự được phép.");
                response.sendRedirect("BrandServlet?action=list&showErrorModal=true");
                return;
            }

            if (brandDAO.isBrandExists(name.trim())) {
                request.getSession().setAttribute("errorMessage", "Thương hiệu đã tồn tại.");
                response.sendRedirect("BrandServlet?action=list&showErrorModal=true");
                return;
            }

            Brand brand = new Brand(brandID, name.trim(), 0, null);
            brandDAO.updateBrand(brand);
            request.getSession().setAttribute("successMessage", "Đã cập nhật thành công!");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID không hợp lệ.");
        }
        response.sendRedirect("BrandServlet?action=list");
    }

    private void softDeleteBrand(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("brandID"));
            brandDAO.softDeleteBrand(id);
            request.getSession().setAttribute("successMessage", "Thương hiệu này đã được chuyển vào thùng rác!");
            response.sendRedirect("BrandServlet?action=list");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid brand ID.");
            response.sendRedirect("BrandServlet?action=list");
        }
    }

 
    private void hardDeleteBrand(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("brandID"));
            brandDAO.hardDeleteBrand(id);
            request.getSession().setAttribute("successMessage", "Đã xóa vĩnh viễn!");
            response.sendRedirect("BrandServlet?action=listDeleted");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID không hợp lệ.");
            response.sendRedirect("BrandServlet?action=listDeleted");
        }
    }

    private void searchBrand(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException, ClassNotFoundException {
        String keyword = request.getParameter("search");
        if (keyword == null || keyword.trim().isEmpty()) {
            listBrands(request, response);
            return;
        }
        List<Brand> brands = brandDAO.searchBrand(keyword.trim().toLowerCase());
        request.setAttribute("brands", brands);
        request.getRequestDispatcher("BrandManagement.jsp").forward(request, response);
    }

    private void restoreBrand(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        try {
            int brandID = Integer.parseInt(request.getParameter("brandID"));
            brandDAO.restoreBrand(brandID);
            request.getSession().setAttribute("successMessage", "Khôi phục thành công.");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID không hợp lệ.");
        }
        response.sendRedirect("BrandServlet?action=list&showSuccessModal=true");
    }
}
