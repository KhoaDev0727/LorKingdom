/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.MaterialDAO;
import Model.Material;
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
public class MaterialServlet extends HttpServlet {

    MaterialDAO materialDAO = new MaterialDAO();

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
            out.println("<title>Servlet MaterialServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet MaterialServlet at " + request.getContextPath() + "</h1>");
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
                listMaterials(request, response);
            } else {
                switch (action) {
                    case "add":
                        addMaterial(request, response);
                        break;
                    case "delete":
                        deleteMaterial(request, response);
                        break;
                    case "update":
                        updateMaterial(request, response);
                        break;
                    case "search":
                        searchMaterial(request, response);
                        break;
                    default:
                        listMaterials(request, response);
                        break;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void listMaterials(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        List<Material> materials = materialDAO.getAllMaterials();
        request.setAttribute("materials", materials);
        request.getRequestDispatcher("MaterialManagement.jsp").forward(request, response);
    }

    private void addMaterial(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");

        if (name == null || name.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Tên chất vật liệu không được để trống.");
            response.sendRedirect("MaterialServlet?action=list&showErrorModal=true");
            return;
        }
        if (!name.matches("^[\\p{L} _-]+$")) {
            request.getSession().setAttribute("errorMessage", "Tên vật liệu chỉ được chứa chữ cái và khoảng trắng.");
            response.sendRedirect("MaterialServlet?action=list&showErrorModal=true");
            return;
        }
        if (materialDAO.isMaterial(name.trim())) {
            request.getSession().setAttribute("errorMessage", "Tên vật liệu đã tồn tại. Vui lòng nhập tên khác.");
            response.sendRedirect("MaterialServlet?action=list&showErrorModal=true");
            return;
        }
        Material material = new Material(0, name.trim(), description);
        materialDAO.addMaterial(material);

        request.getSession().setAttribute("successMessage", "Đã thêm vật liệu thành công.");
        response.sendRedirect("MaterialServlet?action=list&showSuccessModal=true");
    }

    private void updateMaterial(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        try {
            int materialID = Integer.parseInt(request.getParameter("materialID"));
            String name = request.getParameter("name");
            String description = request.getParameter("description");

            if (name == null || name.trim().isEmpty()) {
                request.getSession().setAttribute("errorMessage", "Tên chất vật liệu không được để trống.");
                response.sendRedirect("MaterialServlet?action=list&showErrorModal=true");
                return;
            }
            if (!name.matches("^[\\p{L} _-]+$")) {
                request.getSession().setAttribute("errorMessage", "Tên chất vật liệu chỉ được chứa chữ cái và khoảng trắng.");
                response.sendRedirect("MaterialServlet?action=list&showErrorModal=true");
                return;
            }
            if (materialDAO.isMaterial(name.trim())) {
                request.getSession().setAttribute("errorMessage", "Tên chất vật liệu đã tồn tại. Vui lòng nhập tên khác.");
                response.sendRedirect("MaterialServlet?action=list&showErrorModal=true");
                return;
            }

            Material material = new Material(materialID, name.trim(), description);
            materialDAO.updateMaterial(material);
            request.getSession().setAttribute("successMessage", "Chất liệu đã được cập nhật thành công");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID không hợp lệ.");
        }
        response.sendRedirect("MaterialServlet?action=list");
    }

    private void deleteMaterial(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        try {
            int materialID = Integer.parseInt(request.getParameter("materialID"));
            materialDAO.deleteMaterial(materialID);
            request.getSession().setAttribute("successMessage", "Đã xóa chất liệu thành công.");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID không hợp lệ.");
        }
        response.sendRedirect("MaterialServlet?action=list");
    }

    private void searchMaterial(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        String keyword = request.getParameter("search".toLowerCase());
        if (keyword == null || keyword.trim().isEmpty()) {
            listMaterials(request, response);
            return;
        }

        List<Material> filteredMaterials = materialDAO.searchMaterials(keyword.toLowerCase());
        request.setAttribute("materials", filteredMaterials);
        request.getRequestDispatcher("MaterialManagement.jsp").forward(request, response);
    }

}
