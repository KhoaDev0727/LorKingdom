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
        String action = request.getParameter("action");

        try {
            if (action == null || action.equals("list")) {
                listBrands(request, response);
            } else {
                switch (action) {
                    case "add":
                        addBrand(request, response);
                        break;
                    case "update":
                        updateBrand(request, response);
                        break;
                    case "delete":
                        deleteBrand(request, response);
                        break;
                    case "search":
                        searchBrand(request, response);
                        break;
                    default:
                        listBrands(request, response);
                        break;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    // Hiển thị danh sách Brand
    private void listBrands(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        List<Brand> brands = brandDAO.getAllBrands();
        request.setAttribute("brands", brands);
        request.getRequestDispatcher("BrandManagement.jsp").forward(request, response);
    }

    // Thêm mới Brand
    private void addBrand(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        String name = request.getParameter("name");

        // Kiểm tra tên không được để trống
        if (name == null || name.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Brand name cannot be empty.");
            response.sendRedirect("BrandServlet?action=list&showErrorModal=true");
            return;
        }

        // Kiểm tra độ dài tối đa (dựa trên định nghĩa trong database là 100 ký tự)
        if (name.length() > 100) {
            request.getSession().setAttribute("errorMessage", "Brand name is too long. Maximum 100 characters allowed.");
            response.sendRedirect("BrandServlet?action=list&showErrorModal=true");
            return;
        }

        // Kiểm tra xem Brand đã tồn tại chưa
        if (brandDAO.isBrandExists(name.trim())) {
            request.getSession().setAttribute("errorMessage", "Brand already exists.");
            response.sendRedirect("BrandServlet?action=list&showErrorModal=true");
            return;
        }

        Brand brand = new Brand(0, name.trim(), null);
        brandDAO.addBrand(brand);
        request.getSession().setAttribute("successMessage", "Brand added successfully.");
        response.sendRedirect("BrandServlet?action=list&showSuccessModal=true");
    }

    // Cập nhật Brand
    private void updateBrand(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        try {
            int brandID = Integer.parseInt(request.getParameter("brandID"));
            String name = request.getParameter("name");

            if (name == null || name.trim().isEmpty()) {
                request.getSession().setAttribute("errorMessage", "Brand name cannot be empty.");
                response.sendRedirect("BrandServlet?action=list&showErrorModal=true");
                return;
            }
            if (name.length() > 100) {
                request.getSession().setAttribute("errorMessage", "Brand name is too long. Maximum 100 characters allowed.");
                response.sendRedirect("BrandServlet?action=list&showErrorModal=true");
                return;
            }

            Brand brand = new Brand(brandID, name.trim(), null);
            brandDAO.updateBrand(brand);
            request.getSession().setAttribute("successMessage", "Brand updated successfully.");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid Brand ID.");
        }
        response.sendRedirect("BrandServlet?action=list");
    }

    // Xóa Brand
    private void deleteBrand(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        try {
            int brandID = Integer.parseInt(request.getParameter("brandID"));
            brandDAO.deleteBrand(brandID);
            request.getSession().setAttribute("successMessage", "Brand deleted successfully.");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid Brand ID.");
        }
        response.sendRedirect("BrandServlet?action=list");
    }

    // Tìm kiếm Brand theo từ khóa
    private void searchBrand(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException, ClassNotFoundException {
        String keyword = request.getParameter("search");
        if (keyword == null || keyword.trim().isEmpty()) {
            listBrands(request, response);
            return;
        }
        List<Brand> brands = brandDAO.searchBrand(keyword.trim());
        request.setAttribute("brands", brands);
        request.getRequestDispatcher("BrandManagement.jsp").forward(request, response);
    }
}
    