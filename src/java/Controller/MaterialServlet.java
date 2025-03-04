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
        // Kiểm tra login (nếu cần)
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
                    listMaterials(request, response);
                    break;
                case "listDeleted":
                    listDeletedMaterials(request, response);
                    break;
                case "add":
                    addMaterial(request, response);
                    break;
                case "update":
                    updateMaterial(request, response);
                    break;
                case "delete": // Xóa mềm
                    softDeleteMaterial(request, response);
                    break;
                case "hardDelete": // Xóa cứng
                    hardDeleteMaterial(request, response);
                    break;
                case "restore":
                    restoreMaterial(request, response);
                    break;
                case "search":
                    searchMaterial(request, response);
                    break;
                default:
                    listMaterials(request, response);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            session.setAttribute("errorMessage", "Error: " + ex.getMessage());
            response.sendRedirect("MaterialServlet?action=list&showErrorModal=true");
        }
    }

    private void listMaterials(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        List<Material> materials = materialDAO.getAllActiveMaterials();
        request.setAttribute("materials", materials);
        request.getRequestDispatcher("MaterialManagement.jsp").forward(request, response);
    }

    // 2) Hiển thị danh sách đã xóa mềm
    private void listDeletedMaterials(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        List<Material> materials = materialDAO.getDeletedMaterials();
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
        Material material = new Material(0, name, description, 0);
        materialDAO.addMaterial(material);

        request.getSession().setAttribute("successMessage", "Đã thêm vật liệu thành công.");
        response.sendRedirect("MaterialServlet?action=list&showSuccessModal=true");
    }

    private void updateMaterial(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        try {
            int materialID = Integer.parseInt(request.getParameter("materialID"));
            String name = request.getParameter("materialName");
            String description = request.getParameter("description");

            System.out.println(materialID);
            System.out.println(name);
            System.out.println(description);

            if (name == null || name.trim().isEmpty()) {
                request.getSession().setAttribute("errorMessage", "Tên chất liệu không được để trống.");
                response.sendRedirect("MaterialServlet?action=list&showErrorModal=true");
                return;
            }
            if (!name.matches("^[\\p{L} _-]+$")) {
                request.getSession().setAttribute("errorMessage", "Tên chất liệu chỉ được chứa chữ cái và khoảng trắng.");
                response.sendRedirect("MaterialServlet?action=list&showErrorModal=true");
                return;
            }
            if (materialDAO.isMaterial(name.trim())) {
                request.getSession().setAttribute("errorMessage", "Tên chất vật liệu đã tồn tại. Vui lòng nhập tên khác.");
                response.sendRedirect("MaterialServlet?action=list&showErrorModal=true");
                return;
            }

            Material material = new Material(materialID, name, description, 0);
            materialDAO.updateMaterial(material);
            request.getSession().setAttribute("successMessage", "Chất liệu đã được cập nhật thành công");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID không hợp lệ.");
        }
        response.sendRedirect("MaterialServlet?action=list");
    }

    // 5) Xóa mềm
    private void softDeleteMaterial(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {
        try {
            int materialID = Integer.parseInt(request.getParameter("materialID"));
            materialDAO.softDeleteMaterial(materialID);
            request.getSession().setAttribute("successMessage", "Bạn có muốn đưa chất liệu này vào thùng rác không");
            response.sendRedirect("MaterialServlet?action=list");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid material ID.");
            response.sendRedirect("MaterialServlet?action=list");
        }
    }

    // 6) Xóa cứng
    private void hardDeleteMaterial(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {
        try {
            int materialID = Integer.parseInt(request.getParameter("materialID"));
            materialDAO.hardDeleteMaterial(materialID);
            request.getSession().setAttribute("successMessage", "Chất liệu đã bị xóa vĩnh viễn.");
            response.sendRedirect("MaterialServlet?action=listDeleted");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID chất liệu không hợp lệ.");
            response.sendRedirect("MaterialServlet?action=listDeleted");
        }
    }

    private void restoreMaterial(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, IOException {
        try {
            int materialID = Integer.parseInt(request.getParameter("materialID"));
            materialDAO.restoreMaterial(materialID);
            request.getSession().setAttribute("successMessage", "Chất liệu đã được phục hồi thành công.");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID chất liệu không hợp lệ.");
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

        List<Material> filteredMaterials = materialDAO.searchMaterial(keyword.trim().toLowerCase());
        request.setAttribute("materials", filteredMaterials);
        request.getRequestDispatcher("MaterialManagement.jsp").forward(request, response);
    }

}
