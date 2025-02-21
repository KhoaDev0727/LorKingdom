package Controller;

import DAO.RoleDAO;
import Model.Role;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Admin/RoleServlet") // Chỉnh sửa URL mapping
public class RoleServlet extends HttpServlet {

    private RoleDAO roleDAO = new RoleDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequest(request, response);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
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
                        searchRoles(request, response);
                        break;
                    default:
                        listRoles(request, response);
                        break;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            request.getRequestDispatcher("/Admin/error.jsp").forward(request, response);
        }
    }

    private void listRoles(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ClassNotFoundException, ServletException, IOException {
        List<Role> roles = roleDAO.getAllRoles();
        request.setAttribute("roles", roles);
        request.getRequestDispatcher("/Admin/RoleManagement.jsp").forward(request, response);
    }

    private void addRole(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        String roleName = request.getParameter("roleName");
        String description = request.getParameter("description");

        if (roleName == null || roleName.trim().isEmpty()) {
        request.getSession().setAttribute("errorMessage", "Role name cannot be empty.");
        response.sendRedirect("RoleServlet?action=list&showErrorModal=true");
        return;
        }
        
        if (roleDAO.isRoleExists(roleName)) {
            request.getSession().setAttribute("errorMessage", "Role already exists.");
            response.sendRedirect(request.getContextPath() + "/Admin/RoleServlet?action=list&showErrorModal=true");
            return;
        }
        if (!roleName.matches("^[a-zA-Z_\\-\\s]+$")) {
            request.getSession().setAttribute("errorMessage", "Role name can only contain letters, '_' and '-'.");
            response.sendRedirect("RoleServlet?action=list&showErrorModal=true");
            return;
        }

        if (!description.matches("^[a-zA-Z_\\-\\s]+$")) {
            request.getSession().setAttribute("errorMessage", "Description can only contain letters, '_' and '-'.");
            response.sendRedirect("RoleServlet?action=list&showErrorModal=true");
            return;
        }
        
        roleDAO.addRole(roleName, description);
        request.getSession().setAttribute("successMessage", "Role added successfully.");
        response.sendRedirect(request.getContextPath() + "/Admin/RoleServlet?action=list&showSuccessModal=true");
    }

    private void updateRole(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        int id = Integer.parseInt(request.getParameter("roleID"));
        String roleName = request.getParameter("roleName");
        String description = request.getParameter("Description");

        if (roleName == null || roleName.trim().isEmpty()) {
        request.getSession().setAttribute("errorMessage", "Role name cannot be empty.");
        response.sendRedirect("RoleServlet?action=list&showErrorModal=true");
        return;
        }
        
        if (roleDAO.isRoleExists(roleName)) {
            request.getSession().setAttribute("errorMessage", "Role already exists.");
            response.sendRedirect(request.getContextPath() + "/Admin/RoleServlet?action=list&showErrorModal=true");
            return;
        }
        if (!roleName.matches("^[a-zA-Z_\\-\\s]+$")) {
            request.getSession().setAttribute("errorMessage", "Role name can only contain letters, ' ', '_' and '-'.");
            response.sendRedirect("RoleServlet?action=list&showErrorModal=true");
            return;
        }
        
        if (!description.matches("^[a-zA-Z_\\-\\s]+$")) {
            request.getSession().setAttribute("errorMessage", "Description can only contain letters, ' ', '_' and '-'.");
            response.sendRedirect("RoleServlet?action=list&showErrorModal=true");
            return;
        }
        
        roleDAO.updateRole(id, roleName, description);
        request.getSession().setAttribute("successMessage", "Role updated successfully.");
        response.sendRedirect(request.getContextPath() + "/Admin/RoleServlet?action=list&showSuccessModal=true");
    }

    private void deleteRole(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException, ClassNotFoundException {
        int id = Integer.parseInt(request.getParameter("roleID"));
        roleDAO.deleteRole(id);
        request.getSession().setAttribute("successMessage", "Role deleted successfully.");
        response.sendRedirect(request.getContextPath() + "/Admin/RoleServlet?action=list&showSuccessModal=true");
    }

    private void searchRoles(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException, ClassNotFoundException {
        String keyword = request.getParameter("search");
        List<Role> roles = roleDAO.searchRoles(keyword);
        request.setAttribute("roles", roles);
        request.getRequestDispatcher("/Admin/RoleManagement.jsp").forward(request, response);
    }
}
