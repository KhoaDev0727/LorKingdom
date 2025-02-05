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
import java.sql.SQLException;
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

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if (action == null || action.equals("list")) {
                listMaterials(request, response);
            } else {
                switch (action) {
                    case "add":
                        addMaterial(request, response);
                        break;
                    case "update":
                        updateMaterial(request, response);
                        break;
                    case "delete":
                        deleteMaterial(request, response);
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
            request.getRequestDispatcher("Admin/error.jsp").forward(request, response);
        }
    }

    /**
     * Hiển thị danh sách tất cả Materials
     */
    private void listMaterials(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        List<Material> materials = materialDAO.getAllMaterials();
        request.setAttribute("materials", materials);
        request.getRequestDispatcher("MaterialManagement.jsp").forward(request, response);
    }

    /**
     * Thêm Material mới
     */
    private void addMaterial(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        String name = request.getParameter("materialName");
        String description = request.getParameter("description");

        if (name == null || name.trim().isEmpty()) {
            request.getSession().setAttribute("errorMessage", "Material name cannot be empty.");
            response.sendRedirect("MaterialServlet?action=list&showErrorModal=true");
            return;
        }

        name = name.trim();

        if (name.length() > 50) {
            request.getSession().setAttribute("errorMessage", "Material name is too long. Maximum 50 characters.");
            response.sendRedirect("MaterialServlet?action=list&showErrorModal=true");
            return;
        }
        if (description.length() > 50) {
            request.getSession().setAttribute("errorMessage", "Description is too long. Maximum 50 characters.");
            response.sendRedirect("MaterialServlet?action=list&showErrorModal=true");
            return;
        }

        if (!name.matches("^[\\p{L}\\s]+$")) {
            request.getSession().setAttribute("errorMessage", "Material name must contain only letters (including Vietnamese characters) and spaces.");
            response.sendRedirect("MaterialServlet?action=list&showErrorModal=true");
            return;
        }
        if (!description.matches("^[\\p{L}\\s]+$")) {
            request.getSession().setAttribute("errorMessage", "Description must contain only letters (including Vietnamese characters) and spaces.");
            response.sendRedirect("MaterialServlet?action=list&showErrorModal=true");
            return;
        }

        if (materialDAO.isMaterialExists(name)) {
            request.getSession().setAttribute("errorMessage", "Material name already exists.");
            response.sendRedirect("MaterialServlet?action=list&showErrorModal=true");
            return;
        }

        Material material = new Material(0, name, description);
        materialDAO.addMaterial(material);

        request.getSession().setAttribute("successMessage", "Material added successfully.");
        response.sendRedirect("MaterialServlet?action=list&showSuccessModal=true");
    }

    /**
     * Cập nhật Material
     */
    private void updateMaterial(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        try {
            int materialID = Integer.parseInt(request.getParameter("materialID"));
            String name = request.getParameter("materialName");
            String description = request.getParameter("description");

            name = name.trim();

            if (name.length() > 50) {
                request.getSession().setAttribute("errorMessage", "Material name is too long. Maximum 50 characters.");
                response.sendRedirect("MaterialServlet?action=list&showErrorModal=true");
                return;
            }
            if (description.length() > 50) {
                request.getSession().setAttribute("errorMessage", "Description is too long. Maximum 50 characters.");
                response.sendRedirect("MaterialServlet?action=list&showErrorModal=true");
                return;
            }

            if (!name.matches("^[\\p{L}\\s]+$")) {
                request.getSession().setAttribute("errorMessage", "Material name must contain only letters (including Vietnamese characters) and spaces.");
                response.sendRedirect("MaterialServlet?action=list&showErrorModal=true");
                return;
            }
            if (!description.matches("^[\\p{L}\\s]+$")) {
                request.getSession().setAttribute("errorMessage", "Description must contain only letters (including Vietnamese characters) and spaces.");
                response.sendRedirect("MaterialServlet?action=list&showErrorModal=true");
                return;
            }

            if (materialDAO.isMaterialExists(name)) {
                request.getSession().setAttribute("errorMessage", "Material name already exists.");
                response.sendRedirect("MaterialServlet?action=list&showErrorModal=true");
                return;
            }
            Material material = new Material(materialID, name.trim(), description);
            materialDAO.updateMaterial(material);
            request.getSession().setAttribute("successMessage", "Material updated successfully.");
            response.sendRedirect("MaterialServlet?action=list&showSuccessModal=true");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid Material ID.");
            response.sendRedirect("MaterialServlet?action=list&showErrorModal=true");
        }
    }

    /**
     * Xóa Material
     */
    private void deleteMaterial(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        try {
            int materialID = Integer.parseInt(request.getParameter("materialID"));
            materialDAO.deleteMaterial(materialID);
            request.getSession().setAttribute("successMessage", "Material deleted successfully.");
            response.sendRedirect("MaterialServlet?action=list&showSuccessModal=true");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid Material ID.");
            response.sendRedirect("MaterialServlet?action=list&showErrorModal=true");
        }
    }

    /**
     * Tìm kiếm Material
     */
    private void searchMaterial(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException, ClassNotFoundException {
        String keyword = request.getParameter("search");

        if (keyword == null || keyword.trim().isEmpty()) {
            listMaterials(request, response);
            return;
        }

        List<Material> materials = materialDAO.searchMaterials(keyword);
        request.setAttribute("materials", materials);
        request.getRequestDispatcher("MaterialManagement.jsp").forward(request, response);
    }
}
